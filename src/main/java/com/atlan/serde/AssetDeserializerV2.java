/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.cache.AtlanTagCache;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.*;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.*;

/**
 * Deserialization of all {@link Asset} objects, down through the entire inheritance hierarchy.
 * This custom deserialization is necessary to flatten some specific aspects of complexity in Atlan's payloads:
 * <ul>
 *     <li>The nested <code>attributes</code> and <code>relationshipAttributes</code> structures.</li>
 *     <li>The possibility that the same (relationship) attribute could appear in either of these nested structures.</li>
 *     <li>Handling the extension of properties as you traverse down the inheritance structures, without needing to also extend these nested structures through inheritance.</li>
 *     <li>Automatically translating the nested <code>businessAttributes</code> structure into custom metadata, including translating from Atlan's internal hashed-string representations into human-readable names.</li>
 * </ul>
 */
public class AssetDeserializerV2 extends StdDeserializer<Asset> implements ResolvableDeserializer {

    private static final long serialVersionUID = 2L;
    private final JsonDeserializer<?> defaultDeserializer;

    public AssetDeserializerV2(JsonDeserializer<?> defaultDeserializer) {
        super(Asset.class);
        this.defaultDeserializer = defaultDeserializer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resolve(DeserializationContext ctxt) throws JsonMappingException {
        ((ResolvableDeserializer) defaultDeserializer).resolve(ctxt);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object deserializeWithType(
            JsonParser parser, DeserializationContext context, TypeDeserializer typeDeserializer) throws IOException {
        return deserialize(parser, context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Asset deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode root = parser.getCodec().readTree(parser);
        if (root != null) {
            //  1. Create a revised structure that mirrors what the POJOs will expect
            JsonNode revised = revisePayload(root);
            //  2. Hand-off that revised structure as a parser to the default deserializer
            return deserializeRevised(revised.traverse(parser.getCodec()), context);
        }
        return null;
    }

    private JsonNode revisePayload(JsonNode root) throws IOException {

        JsonNode typeName = root.get("typeName");
        JsonNode attributes = root.get("attributes");
        JsonNode relationshipGuid = root.get("relationshipGuid");
        JsonNode relationshipAttributes = root.get("relationshipAttributes");
        JsonNode businessAttributes = root.get("businessAttributes");
        JsonNode classificationNames = root.get("classificationNames");

        Map<String, JsonNode> leftOverAttributes = new HashMap<>();
        // If the same attribute appears in both 'attributes' and 'relationshipAttributes'
        // then retain the 'relationshipAttributes' (more information) and skip the 'attributes'
        // copy of the same
        Set<String> processedAttributes = new HashSet<>();

        // Only process relationshipAttributes if this is a full asset, not a relationship
        // reference. (If it is a relationship reference, the relationshipGuid will be non-null.)
        if (relationshipGuid == null || relationshipGuid.isNull()) {
            if (relationshipAttributes != null && !relationshipAttributes.isNull()) {
                Iterator<String> itr = relationshipAttributes.fieldNames();
                while (itr.hasNext()) {
                    String relnKey = itr.next();
                    ((ObjectNode) root).set(relnKey, relationshipAttributes.get(relnKey));
                    processedAttributes.add(relnKey);
                }
            }
        }
        ((ObjectNode) root).remove("relationshipAttributes");

        if (attributes != null && !attributes.isNull()) {
            Iterator<String> itr = attributes.fieldNames();
            while (itr.hasNext()) {
                String attrKey = itr.next();
                // Only proceed with deserializing the 'attributes' copy of an attribute if
                // it was not already deserialized as a more complete relationship (above)
                if (!processedAttributes.contains(attrKey)) {
                    if (attrKey.equals("meanings")) {
                        // Override meanings / assignedTerms explicitly, as they collide otherwise
                        ((ObjectNode) root).set("assignedTerms", attributes.get(attrKey));
                    } else if (!attrKey.contains(".")) {
                        // TODO: Confirm this is a long-term reliable mechanism to detect custom metadata,
                        //  but so far it should be
                        ((ObjectNode) root).set(attrKey, attributes.get(attrKey));
                    } else {
                        // If the name has a '.' in it, it should be custom metadata in search results,
                        // so retain it for later processing
                        leftOverAttributes.put(attrKey, attributes.get(attrKey));
                    }
                }
            }
        }
        ((ObjectNode) root).remove("attributes");

        // Custom attributes can come from either of two places (should be mutually-exclusive)...
        // 1. For search results, they're embedded in `attributes` in the form <cmId>.<attrId>
        getCustomMetadataFromSearchResult(root, leftOverAttributes);

        // 2. For asset retrievals, they're all in a `businessAttributes` dict
        consolidateCustomMetadata(root, businessAttributes);

        // Translate hashed-string classifications into human-readable names
        ArrayNode clsNames = null;
        if (classificationNames != null && classificationNames.isArray()) {
            clsNames = JsonNodeFactory.instance.arrayNode(classificationNames.size());
            try {
                for (JsonNode element : classificationNames) {
                    String name = AtlanTagCache.getNameForId(element.asText());
                    clsNames.add(name);
                }
            } catch (AtlanException e) {
                throw new IOException("Unable to deserialize Atlan tag name.", e);
            }
        }
        if (clsNames != null) {
            ((ObjectNode) root).set("classificationNames", clsNames);
        }

        // Special cases to wrap-up:
        // Decode the Readme's description after deserialization
        if (typeName != null && !typeName.isNull() && typeName.asText().equals("Readme")) {
            JsonNode description = root.get("description");
            if (description != null && description.isTextual()) {
                ((ObjectNode) root).put("description", StringUtils.decodeContent(description.asText()));
            }
        }

        return root;
    }

    private void getCustomMetadataFromSearchResult(JsonNode root, Map<String, JsonNode> searchResults) {
        // Build-up a businessAttributes JsonNode combining all {cmId: { attrId: ... }}
        if (searchResults != null && !searchResults.isEmpty()) {
            ObjectNode builder = JsonNodeFactory.instance.objectNode();
            for (Map.Entry<String, JsonNode> entry : searchResults.entrySet()) {
                String compositeId = entry.getKey();
                int indexOfDot = compositeId.indexOf(".");
                if (indexOfDot > 0) {
                    String cmId = compositeId.substring(0, indexOfDot);
                    ObjectNode cmObj;
                    if (!builder.has(cmId)) {
                        cmObj = builder.putObject(cmId);
                    } else {
                        cmObj = (ObjectNode) builder.get(cmId);
                    }
                    String attrId = compositeId.substring(indexOfDot + 1);
                    cmObj.set(attrId, entry.getValue());
                }
            }
            consolidateCustomMetadata(root, builder);
        }
    }

    private void consolidateCustomMetadata(JsonNode root, JsonNode businessAttributes) {
        if (businessAttributes != null && !businessAttributes.isNull()) {
            // Initialize their target location in the JSON...
            ObjectNode customMetadata = ((ObjectNode) root).putObject("customMetadataSets");
            Iterator<String> itrCM = businessAttributes.fieldNames();
            while (itrCM.hasNext()) {
                String cmId = itrCM.next();
                customMetadata.set(cmId, businessAttributes.get(cmId));
            }
        }
    }

    private Asset deserializeRevised(JsonParser revised, DeserializationContext context) throws IOException {
        revised.nextToken(); // Prepare parser for deserialization
        return (Asset) defaultDeserializer.deserialize(revised, context);
    }
}
