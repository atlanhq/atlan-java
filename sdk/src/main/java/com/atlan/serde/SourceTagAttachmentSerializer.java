/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.AtlanClient;
import com.atlan.cache.SourceTagCache;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.Connection;
import com.atlan.model.assets.ITag;
import com.atlan.model.structs.SourceTagAttachment;
import com.atlan.util.JacksonUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom serialization of {@link SourceTagAttachment} objects.
 * In particular, this translates from one of many representations of the source-tag to its other representations.
 */
@Slf4j
public class SourceTagAttachmentSerializer extends StdSerializer<SourceTagAttachment> {
    private static final long serialVersionUID = 2L;

    private final AtlanClient client;

    public SourceTagAttachmentSerializer(AtlanClient client) {
        this(SourceTagAttachment.class, client);
    }

    public SourceTagAttachmentSerializer(Class<SourceTagAttachment> t, AtlanClient client) {
        super(t);
        this.client = client;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serializeWithType(
            SourceTagAttachment value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer)
            throws IOException {
        serialize(value, gen, serializers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(SourceTagAttachment sta, JsonGenerator gen, SerializerProvider sp)
            throws IOException, JsonProcessingException {

        String stGuid = sta.getSourceTagGuid();
        String stQN = sta.getSourceTagQualifiedName();
        String stName = sta.getSourceTagName();

        ITag source = null;
        if (stGuid != null && !stGuid.isEmpty()) {
            try {
                source = (ITag) client.getSourceTagCache().getByGuid(stGuid);
            } catch (AtlanException e) {
                log.warn("Unable to find source tag by GUID: {}", stGuid);
                log.debug("Details:", e);
            }
        }
        if (source == null && stQN != null && !stQN.isEmpty()) {
            try {
                source = (ITag) client.getSourceTagCache().getByQualifiedName(stQN);
            } catch (AtlanException e) {
                log.warn("Unable to find source tag by qualifiedName: {}", stQN);
                log.debug("Details:", e);
            }
        }
        if (source == null && stName != null && !stName.isEmpty()) {
            try {
                SourceTagCache.SourceTagName name = new SourceTagCache.SourceTagName(stName);
                source = (ITag) client.getSourceTagCache().getByName(name);
            } catch (AtlanException e) {
                log.warn("Unable to find source tag by name: {}", stName);
                log.debug("Details:", e);
            }
        }
        if (source == null) {
            log.debug("Attempt to serialize a null source tag attachment — skipping.");
        } else {
            // TODO: Unfortunately, the use of AtlanTagBeanSerializerModifier to avoid the direct
            //  deserialization below made things too complicated when trying to incorporate AuditDetail interface
            gen.writeStartObject();
            JacksonUtils.serializeString(gen, "typeName", SourceTagAttachment.TYPE_NAME);
            JacksonUtils.serializeString(gen, "sourceTagName", source.getName());
            JacksonUtils.serializeString(gen, "sourceTagQualifiedName", source.getQualifiedName());
            JacksonUtils.serializeString(gen, "sourceTagGuid", source.getGuid());
            JacksonUtils.serializeString(
                    gen,
                    "sourceTagConnectorName",
                    Connection.getConnectorTypeFromQualifiedName(source.getQualifiedName())
                            .getValue());
            if (!sta.getSourceTagValues().isEmpty()) {
                JacksonUtils.serializeObject(gen, "sourceTagValue", sta.getSourceTagValues());
            }
            JacksonUtils.serializeBoolean(gen, "isSourceTagSynced", sta.getIsSourceTagSynced());
            JacksonUtils.serializeLong(gen, "sourceTagSyncTimestamp", sta.getSourceTagSyncTimestamp());
            JacksonUtils.serializeString(gen, "sourceTagSyncError", sta.getSourceTagSyncError());
            gen.writeEndObject();
        }
    }
}
