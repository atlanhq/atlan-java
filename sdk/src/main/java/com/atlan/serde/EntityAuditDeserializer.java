/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.AtlanClient;
import com.atlan.model.search.AuditDetail;
import com.atlan.model.search.EntityAudit;
import com.atlan.util.JacksonUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.io.IOException;

/**
 * Deserialization of all {@link EntityAudit} objects, down through the entire inheritance hierarchy.
 * This custom deserialization is necessary to distinguish between Atlan tag and Entity objects.
 */
public class EntityAuditDeserializer extends StdDeserializer<EntityAudit> {

    private static final long serialVersionUID = 2L;

    private final transient AtlanClient client;

    public EntityAuditDeserializer(AtlanClient client) {
        this(AuditDetail.class, client);
    }

    public EntityAuditDeserializer(Class<?> t, AtlanClient client) {
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
    public EntityAudit deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode root = parser.getCodec().readTree(parser);
        Long timestamp = JacksonUtils.deserializeLong(root, "timestamp");
        long ts = timestamp == null ? Long.MAX_VALUE : timestamp;

        EntityAudit.EntityAuditBuilder<?, ?> builder = EntityAudit.builder()
                .entityQualifiedName(JacksonUtils.deserializeString(root, "entityQualifiedName"))
                .typeName(JacksonUtils.deserializeString(root, "typeName"))
                .entityId(JacksonUtils.deserializeString(root, "entityId"))
                .timestamp(timestamp)
                .created(JacksonUtils.deserializeLong(root, "created"))
                .user(JacksonUtils.deserializeString(root, "user"))
                .action(JacksonUtils.deserializeObject(client, root, "action", new TypeReference<>() {}))
                .eventKey(JacksonUtils.deserializeString(root, "eventKey"))
                .entityDetail(JacksonUtils.deserializeObject(client, root, "entityDetail", new TypeReference<>() {}))
                .headers(JacksonUtils.deserializeObject(client, root, "headers", new TypeReference<>() {}));

        JsonNode detail = root.get("detail");
        if (detail != null && !detail.isNull()) {
            JsonNode guid = detail.get("guid"); // only exists on entities
            JsonNode attributes = detail.get("attributes"); // exists on entities and custom metadata
            AuditDetail auditDetail;
            if (guid != null && guid.isTextual()) {
                // Delegate to entity deserialization
                auditDetail = client.getAssetDeserializer().deserialize(detail, ts);
            } else if (attributes != null) {
                // Delegate to the custom metadata deserialization
                auditDetail = client.getCustomMetadataAuditDeserializer().deserialize(detail, ts);
            } else {
                // Delegate to Atlan tag deserialization
                auditDetail = client.getAtlanTagDeserializer().deserialize(detail, ts);
            }
            auditDetail.setRawJsonObject(detail);
            builder.detail(auditDetail);
        }
        return builder.build();
    }
}
