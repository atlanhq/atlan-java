/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.cache.ClassificationCache;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.Classification;
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
 * Custom serialization of {@link Classification} objects.
 * In particular, this translates from the human-readable name into the Atlan-internal hashed-string representation for
 * a classification.
 */
@Slf4j
public class ClassificationSerializer extends StdSerializer<Classification> {
    private static final long serialVersionUID = 2L;

    public ClassificationSerializer() {
        this(null);
    }

    public ClassificationSerializer(Class<Classification> t) {
        super(t);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serializeWithType(
            Classification value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer)
            throws IOException {
        serialize(value, gen, serializers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(Classification cls, JsonGenerator gen, SerializerProvider sp)
            throws IOException, JsonProcessingException {

        String clsName = cls.getTypeName();
        if (clsName == null) {
            log.debug("Attempt to serialize a null classification — skipping.");
        } else {
            String clsId;
            if (clsName.equals(Serde.DELETED_AUDIT_OBJECT)) {
                clsId = Serde.DELETED_AUDIT_OBJECT;
            } else {
                try {
                    clsId = ClassificationCache.getIdForName(clsName);
                } catch (AtlanException e) {
                    throw new IOException("Unable to find classification with name: " + clsName, e);
                }
            }

            // TODO: Unfortunately, the use of ClassificationBeanSerializerModifier to avoid the direct
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
            gen.writeEndObject();
        }
    }
}
