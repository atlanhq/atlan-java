/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.AtlanClient;
import com.atlan.cache.ReflectionCache;
import com.atlan.exception.AtlanException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.assets.*;
import com.atlan.model.core.AtlanTag;
import com.atlan.model.core.CustomMetadataAttributes;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.util.JacksonUtils;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class AssetDeserializer extends StdDeserializer<Asset> {

    private static final long serialVersionUID = 2L;
    private final transient AtlanClient client;

    public AssetDeserializer(AtlanClient client) {
        this(Asset.class, client);
    }

    public AssetDeserializer(Class<?> t, AtlanClient client) {
        super(t);
        this.client = client;
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
        return deserialize(parser.getCodec().readTree(parser), Long.MAX_VALUE);
    }

    /**
     * Actually do the work of deserializing an asset.
     *
     * @param root of the parsed JSON tree
     * @param minimumTime epoch-based time (in milliseconds) to compare against the time the cache was last refreshed
     * @return the deserialized asset
     * @throws IOException on any issues parsing the JSON
     */
    @SuppressWarnings("deprecation") // Suppress deprecation notice on use of atlanTagNames builder
    Asset deserialize(JsonNode root, long minimumTime) throws IOException {

        JsonNode attributes = root.get("attributes");
        JsonNode relationshipGuid = root.get("relationshipGuid");
        JsonNode relationshipAttributes = root.get("relationshipAttributes");
        JsonNode businessAttributes = root.get("businessAttributes");
        JsonNode classificationNames = root.get("classificationNames");

        Asset.AssetBuilder<?, ?> builder;
        Class<?> assetClass;

        JsonNode typeNameJson = root.get("typeName");
        String typeName = null;

        if (typeNameJson == null || typeNameJson.isNull()) {
            builder = IndistinctAsset._internal();
            assetClass = IndistinctAsset.class;
        } else {
            typeName = root.get("typeName").asText();
            try {
                assetClass = Serde.getAssetClassForType(typeName);
                Method method = assetClass.getMethod("_internal");
                Object result = method.invoke(null);
                builder = (Asset.AssetBuilder<?, ?>) result;
            } catch (ClassNotFoundException
                    | NoSuchMethodException
                    | InvocationTargetException
                    | IllegalAccessException e) {
                log.warn(
                        "Unable to dynamically retrieve asset for typeName {}, falling back to an IndistinctAsset.",
                        typeName,
                        e);
                builder = IndistinctAsset._internal();
                assetClass = IndistinctAsset.class;
            }
        }

        // Start by deserializing all the non-attribute properties (defined at Asset-level)
        builder.typeName(JacksonUtils.deserializeString(root, "typeName"))
                .guid(JacksonUtils.deserializeString(root, "guid"))
                .displayText(JacksonUtils.deserializeString(root, "displayText"))
                // Include reference attributes, for related entities
                .entityStatus(JacksonUtils.deserializeString(root, "entityStatus"))
                .relationshipType(JacksonUtils.deserializeString(root, "relationshipType"))
                .relationshipGuid(JacksonUtils.deserializeString(root, "relationshipGuid"))
                .relationshipStatus(
                        JacksonUtils.deserializeObject(client, root, "relationshipStatus", new TypeReference<>() {}))
                .uniqueAttributes(
                        JacksonUtils.deserializeObject(client, root, "uniqueAttributes", new TypeReference<>() {}))
                .status(JacksonUtils.deserializeObject(client, root, "status", new TypeReference<>() {}))
                .createdBy(JacksonUtils.deserializeString(root, "createdBy"))
                .updatedBy(JacksonUtils.deserializeString(root, "updatedBy"))
                .createTime(JacksonUtils.deserializeLong(root, "createTime"))
                .updateTime(JacksonUtils.deserializeLong(root, "updateTime"))
                .deleteHandler(JacksonUtils.deserializeString(root, "deleteHandler"))
                .isIncomplete(JacksonUtils.deserializeBoolean(root, "isIncomplete"))
                .depth(JacksonUtils.deserializeLong(root, "depth"));
        Set<AtlanTag> atlanTags =
                JacksonUtils.deserializeObject(client, root, "classifications", new TypeReference<>() {});
        if (atlanTags != null) {
            builder.atlanTags(atlanTags);
        }
        TreeSet<String> meaningNames =
                JacksonUtils.deserializeObject(client, root, "meaningNames", new TypeReference<>() {});
        if (meaningNames != null) {
            builder.meaningNames(meaningNames);
        }
        TreeSet<Meaning> meanings = JacksonUtils.deserializeObject(client, root, "meanings", new TypeReference<>() {});
        if (meanings != null) {
            builder.meanings(meanings);
        }
        TreeSet<String> pendingTasks =
                JacksonUtils.deserializeObject(client, root, "pendingTasks", new TypeReference<>() {});
        if (pendingTasks != null) {
            builder.pendingTasks(pendingTasks);
        }
        List<LineageRef> immediateUpstream =
                JacksonUtils.deserializeObject(client, root, "immediateUpstream", new TypeReference<>() {});
        if (immediateUpstream != null) {
            builder.immediateUpstream(immediateUpstream);
        }
        List<LineageRef> immediateDownstream =
                JacksonUtils.deserializeObject(client, root, "immediateDownstream", new TypeReference<>() {});
        if (immediateDownstream != null) {
            builder.immediateDownstream(immediateDownstream);
        }
        TreeMap<String, String> customAttributes =
                JacksonUtils.deserializeObject(client, root, "customAttributes", new TypeReference<>() {});

        Class<?> builderClass = builder.getClass();

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
                    String deserializeName = ReflectionCache.getDeserializedName(assetClass, relnKey);
                    Method method = ReflectionCache.getSetter(builderClass, deserializeName);
                    if (method != null) {
                        try {
                            Object value = Serde.deserialize(
                                    client, relationshipAttributes.get(relnKey), method, deserializeName);
                            boolean set = ReflectionCache.setValue(builder, deserializeName, value);
                            if (set) {
                                processedAttributes.add(deserializeName);
                            }
                        } catch (NoSuchMethodException e) {
                            throw new IOException("Missing fromValue method for enum.", e);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new IOException("Failed to deserialize through reflection.", e);
                        }
                    }
                }
            }
        }

        if (attributes != null && !attributes.isNull()) {
            Iterator<String> itr = attributes.fieldNames();
            while (itr.hasNext()) {
                String attrKey = itr.next();
                String deserializeName = ReflectionCache.getDeserializedName(assetClass, attrKey);
                // Only proceed with deserializing the 'attributes' copy of an attribute if
                // it was not already deserialized as a more complete relationship (above)
                if (!processedAttributes.contains(deserializeName)) {
                    Method method = ReflectionCache.getSetter(builderClass, deserializeName);
                    if (method != null) {
                        if (deserializeName.equals("assetIcon")
                                && typeName != null
                                && (typeName.equals("Catalog") || typeName.equals("CustomEntity"))) {
                            JsonNode value = attributes.get(attrKey);
                            if (value != null && !value.isNull()) {
                                builder.iconUrl(value.asText());
                            }
                        } else if (deserializeName.equals("connectorName")) {
                            JsonNode value = attributes.get(attrKey);
                            if (value != null && !value.isNull()) {
                                AtlanConnectorType v = AtlanConnectorType.fromValue(value.asText());
                                if (v == AtlanConnectorType.UNKNOWN_CUSTOM) {
                                    builder.customConnectorType(value.asText());
                                } else {
                                    builder.connectorType(v);
                                }
                            }
                        } else {
                            try {
                                Object value =
                                        Serde.deserialize(client, attributes.get(attrKey), method, deserializeName);
                                ReflectionCache.setValue(builder, deserializeName, value);
                            } catch (NoSuchMethodException e) {
                                throw new IOException("Missing fromValue method for enum.", e);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new IOException("Failed to deserialize through reflection.", e);
                            }
                        }
                    } else {
                        // If the setter was not found, still retain it for later processing
                        // (this is where custom attributes will end up for search results)
                        leftOverAttributes.put(attrKey, attributes.get(attrKey));
                    }
                }
            }
        }

        // Custom (metadata) attributes can come from two places, only one of which should ever have data...
        Map<String, CustomMetadataAttributes> cm = null;

        // 1. For search results, they're embedded in `attributes` in the form <cmId>.<attrId> (custom metadata)
        //    or just __customAttributes (source-specific custom attributes)
        if (!leftOverAttributes.isEmpty()) {
            // Translate these into custom metadata structure
            try {
                cm = client.getCustomMetadataCache().getCustomMetadataFromSearchResult(leftOverAttributes, minimumTime);
            } catch (AtlanException e) {
                throw new IOException(e);
            }
            if (leftOverAttributes.containsKey("__customAttributes")) {
                JsonNode caSearch = leftOverAttributes.get("__customAttributes");
                if (caSearch != null && !caSearch.isNull()) {
                    customAttributes = Serde.allInclusiveMapper.readValue(caSearch.asText(), new TypeReference<>() {});
                }
            }
        }

        // Note that these are source-provided custom attributes, not custom METADATA attributes (different things)
        if (customAttributes != null) {
            builder.customAttributes(customAttributes);
        }

        // 2. For asset retrievals, they're all in a `businessAttributes` dict (custom metadata)
        //    or directly under `customAttributes` (source-specific custom attributes, handled above)
        if (businessAttributes != null) {
            // Translate these into custom metadata structure
            try {
                cm = client.getCustomMetadataCache()
                        .getCustomMetadataFromBusinessAttributes(businessAttributes, minimumTime);
            } catch (AtlanException e) {
                throw new IOException(e);
            }
        }

        Set<String> clsNames = null;
        if (classificationNames != null && classificationNames.isArray()) {
            clsNames = new HashSet<>();
            // Translate these IDs in to human-readable names
            for (JsonNode element : classificationNames) {
                String tagId = element.asText();
                String name = null;
                try {
                    name = client.getAtlanTagCache().getNameForSid(tagId, minimumTime);
                } catch (NotFoundException e) {
                    log.debug(
                            "Unable to find tag with ID {}, deserializing as {}.",
                            tagId,
                            Serde.DELETED_AUDIT_OBJECT,
                            e);
                } catch (AtlanException e) {
                    throw new IOException(e);
                }
                if (name == null) {
                    // Note: the name could be null here either because it was not found in the
                    // first place (NotFoundException), or because it is already tracked as deleted
                    // (in which case it'll come back from getNameForSid as null rather than a NFE).
                    name = Serde.DELETED_AUDIT_OBJECT;
                }
                clsNames.add(name);
            }
        }

        // Special cases to wrap-up:
        // Decode the Readme's description after deserialization
        if (typeName != null && typeName.equals("Readme")) {
            builder.description(StringUtils.decodeContent(builder.build().getDescription()));
        }

        if (cm != null) {
            builder.customMetadataSets(cm);
        }
        if (clsNames != null) {
            builder.atlanTagNames(clsNames);
        }

        Asset result = builder.build();
        result.setRawJsonObject(root);
        return result;
    }
}
