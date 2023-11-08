/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.AtlanClient;
import com.atlan.model.search.AuditDetail;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.io.IOException;

/**
 * Deserialization of all {@link AuditDetail} objects, down through the entire inheritance hierarchy.
 * This custom deserialization is necessary to distinguish between Atlan tag and Entity objects.
 */
public class AuditDetailDeserializer extends StdDeserializer<AuditDetail> {

    private static final long serialVersionUID = 2L;

    private final AtlanClient client;

    public AuditDetailDeserializer(AtlanClient client) {
        this(AuditDetail.class, client);
    }

    public AuditDetailDeserializer(Class<?> t, AtlanClient client) {
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
    public AuditDetail deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode root = parser.getCodec().readTree(parser);
        JsonNode guid = root.get("guid"); // only exists on entities
        JsonNode attributes = root.get("attributes"); // exists on entities and custom metadata
        if (guid != null && guid.isTextual()) {
            // Delegate to entity deserialization
            return client.getAssetDeserializer().deserialize(root);
        } else if (attributes != null) {
            // Delegate to the custom metadata deserialization
            return client.getCustomMetadataAuditDeserializer().deserialize(root);
        } else {
            // Delegate to Atlan tag deserialization
            return client.getAtlanTagDeserializer().deserialize(root);
        }
    }
}
