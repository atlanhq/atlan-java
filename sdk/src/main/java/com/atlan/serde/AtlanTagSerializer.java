/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanTag;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.util.JacksonUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom serialization of {@link AtlanTag} objects.
 * In particular, this translates from the human-readable name into the Atlan-internal hashed-string representation for
 * an Atlan tag.
 */
@Slf4j
public class AtlanTagSerializer extends StdSerializer<AtlanTag> {
    private static final long serialVersionUID = 2L;

    private final transient AtlanClient client;

    public AtlanTagSerializer(AtlanClient client) {
        this(AtlanTag.class, client);
    }

    public AtlanTagSerializer(Class<AtlanTag> t, AtlanClient client) {
        super(t);
        this.client = client;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serializeWithType(
            AtlanTag value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer)
            throws IOException {
        serialize(value, gen, serializers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(AtlanTag cls, JsonGenerator gen, SerializerProvider sp)
            throws IOException, JsonProcessingException {

        String clsName = cls.getTypeName();
        if (clsName == null) {
            log.debug("Attempt to serialize a null Atlan tag — skipping.");
        } else {
            String clsId;
            String sourceAttachmentsAttrId;
            if (clsName.equals(Serde.DELETED_AUDIT_OBJECT)) {
                clsId = Serde.DELETED_AUDIT_OBJECT;
                sourceAttachmentsAttrId = "";
            } else {
                try {
                    clsId = client.getAtlanTagCache().getSidForName(clsName);
                    sourceAttachmentsAttrId = client.getAtlanTagCache().getSourceTagsAttrId(clsId);
                } catch (AtlanException e) {
                    throw new IOException("Unable to find Atlan tag with name: " + clsName, e);
                }
            }

            // TODO: Unfortunately, the use of AtlanTagBeanSerializerModifier to avoid the direct
            //  deserialization below made things too complicated when trying to incorporate AuditDetail interface
            gen.writeStartObject();
            JacksonUtils.serializeString(gen, "typeName", clsId);
            JacksonUtils.serializeString(gen, "entityGuid", cls.getEntityGuid());
            AtlanStatus status = cls.getEntityStatus();
            if (status != null) {
                JacksonUtils.serializeString(gen, "entityStatus", status.getValue());
            }
            JacksonUtils.serializeBoolean(gen, "propagate", cls.getPropagate());
            JacksonUtils.serializeBoolean(
                    gen, "removePropagationsOnEntityDelete", cls.getRemovePropagationsOnEntityDelete());
            JacksonUtils.serializeBoolean(
                    gen, "restrictPropagationThroughLineage", cls.getRestrictPropagationThroughLineage());
            JacksonUtils.serializeBoolean(
                    gen, "restrictPropagationThroughHierarchy", cls.getRestrictPropagationThroughHierarchy());
            if (!sourceAttachmentsAttrId.isEmpty()) {
                gen.writeObjectFieldStart("attributes");
                JacksonUtils.serializeObject(gen, sourceAttachmentsAttrId, cls.getSourceTagAttachments());
                gen.writeEndObject();
            }
            gen.writeEndObject();
        }
    }
}
