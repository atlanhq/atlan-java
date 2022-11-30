/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.cache.CustomMetadataCache;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.CustomMetadataAttributes;
import com.atlan.model.search.CustomMetadataAttributesAuditDetail;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.io.IOException;

/**
 * Custom deserialization of {@link CustomMetadataAttributesAuditDetail} objects.
 * In particular, this translates from the Atlan-internal hashed-string representation for custom metadata into
 * the human-readable names for custom metadata.
 */
public class CustomMetadataAuditDeserializer extends StdDeserializer<CustomMetadataAttributesAuditDetail> {
    private static final long serialVersionUID = 2L;

    public CustomMetadataAuditDeserializer() {
        super(CustomMetadataAttributesAuditDetail.class);
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
    public CustomMetadataAttributesAuditDetail deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        return deserialize(parser.getCodec().readTree(parser));
    }

    /**
     * Actually do the work of deserializing custom metadata audit details.
     *
     * @param root of the parsed JSON tree
     * @return the deserialized custom metadata audit details
     * @throws IOException on any issues parsing the JSON
     */
    CustomMetadataAttributesAuditDetail deserialize(JsonNode root) throws IOException {

        String cmId = root.get("typeName").asText();
        if (cmId == null) {
            throw new IOException("Unable to deserialize custom metadata from: " + root);
        }

        String cmName;
        try {
            // Translate the ID-string to a human-readable name
            cmName = CustomMetadataCache.getNameForId(cmId);
        } catch (AtlanException e) {
            throw new IOException("Unable to find custom metadata with ID-string: " + cmId, e);
        }

        JsonNode attributes = root.get("attributes");

        CustomMetadataAttributes cma;
        try {
            cma = CustomMetadataCache.getCustomMetadataAttributes(cmId, attributes);
        } catch (AtlanException e) {
            throw new IOException("Unable to translate custom metadata attributes: " + attributes, e);
        }

        return CustomMetadataAttributesAuditDetail.builder()
                .typeName(cmName)
                .attributes(cma.getAttributes())
                .archivedAttributes(cma.getArchivedAttributes())
                .build();
    }
}
