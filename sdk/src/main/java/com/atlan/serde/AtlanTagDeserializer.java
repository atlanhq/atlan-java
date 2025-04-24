/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.AtlanTag;
import com.atlan.model.structs.SourceTagAttachment;
import com.atlan.util.JacksonUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Custom deserialization of {@link AtlanTag} objects.
 * In particular, this translates from the Atlan-internal hashed-string representation for an Atlan tag into
 * the human-readable name for an Atlan tag.
 */
public class AtlanTagDeserializer extends StdDeserializer<AtlanTag> {
    private static final long serialVersionUID = 2L;

    private final transient AtlanClient client;

    public AtlanTagDeserializer(AtlanClient client) {
        super(AtlanTag.class);
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
    public AtlanTag deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        return deserialize(parser.getCodec().readTree(parser), Long.MAX_VALUE);
    }

    /**
     * Actually do the work of deserializing an Atlan tag.
     *
     * @param root of the parsed JSON tree
     * @param minimumTime epoch-based time (in milliseconds) to compare against the time the cache was last refreshed
     * @return the deserialized Atlan tag
     * @throws IOException on any issues parsing the JSON
     */
    AtlanTag deserialize(JsonNode root, long minimumTime) throws IOException {
        String clsId = root.get("typeName").asText();
        if (clsId == null) {
            throw new IOException("Unable to deserialize Atlan tag from: " + root);
        }
        String clsName;
        String sourceAttachmentsAttrId;
        try {
            // Translate the ID-string to a human-readable name
            clsName = client.getAtlanTagCache().getNameForSid(clsId, minimumTime);
            sourceAttachmentsAttrId = client.getAtlanTagCache().getSourceTagsAttrId(clsId, false);
        } catch (NotFoundException e) {
            // Do nothing: if not found, the Atlan tag was deleted since but the
            // audit record remains
            clsName = Serde.DELETED_AUDIT_OBJECT;
            sourceAttachmentsAttrId = "";
        } catch (AtlanException e) {
            throw new IOException("Unable to find Atlan tag with ID-string: " + clsId, e);
        }
        if (clsName == null) {
            clsName = Serde.DELETED_AUDIT_OBJECT;
            sourceAttachmentsAttrId = "";
        }

        // Tags that are source-synced can have an embedded attributes containing details of the
        // source tag attachment(s)
        List<SourceTagAttachment> attachments = List.of();
        JsonNode attributes = root.get("attributes");
        if (attributes != null && !attributes.isNull()) {
            Iterator<String> itr = attributes.fieldNames();
            while (itr.hasNext()) {
                String attrKey = itr.next();
                if (attrKey.equals(sourceAttachmentsAttrId)) {
                    attachments = JacksonUtils.deserializeObject(client, attributes, attrKey, new TypeReference<>() {});
                }
            }
        }

        // TODO: Unfortunately, attempts to use a AtlanTagBeanDeserializerModifier to avoid the direct
        //  deserialization below were not successful — something to investigate another time
        AtlanTag result = AtlanTag.builder()
                .typeName(clsName)
                .entityGuid(JacksonUtils.deserializeString(root, "entityGuid"))
                .entityStatus(JacksonUtils.deserializeObject(client, root, "entityStatus", new TypeReference<>() {}))
                .propagate(JacksonUtils.deserializeBoolean(root, "propagate"))
                .removePropagationsOnEntityDelete(
                        JacksonUtils.deserializeBoolean(root, "removePropagationsOnEntityDelete"))
                .restrictPropagationThroughLineage(
                        JacksonUtils.deserializeBoolean(root, "restrictPropagationThroughLineage"))
                .restrictPropagationThroughHierarchy(
                        JacksonUtils.deserializeBoolean(root, "restrictPropagationThroughHierarchy"))
                .sourceTagAttachments(attachments)
                .build();
        result.setRawJsonObject(root);
        return result;
    }
}
