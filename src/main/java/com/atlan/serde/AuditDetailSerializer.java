/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.model.core.Classification;
import com.atlan.model.core.Entity;
import com.atlan.model.search.AuditDetail;
import com.atlan.model.search.CustomMetadataAttributesAuditDetail;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

/**
 * Serialization of all {@link AuditDetail} objects, down through the entire inheritance hierarchy.
 * This custom serialization is necessary to distinguish between Classifications and Entities.
 */
@Slf4j
public class AuditDetailSerializer extends StdSerializer<AuditDetail> {
    private static final long serialVersionUID = 2L;

    public AuditDetailSerializer() {
        this(null);
    }

    public AuditDetailSerializer(Class<AuditDetail> t) {
        super(t);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serializeWithType(
            AuditDetail value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer)
            throws IOException {
        serialize(value, gen, serializers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(AuditDetail detail, JsonGenerator gen, SerializerProvider sp)
            throws IOException, JsonProcessingException {
        if (detail instanceof Classification) {
            ClassificationSerializer classification = new ClassificationSerializer();
            classification.serialize((Classification) detail, gen, sp);
        } else if (detail instanceof Entity) {
            EntitySerializer entity = new EntitySerializer();
            entity.serialize((Entity) detail, gen, sp);
        } else if (detail instanceof CustomMetadataAttributesAuditDetail) {
            CustomMetadataAuditSerializer cm = new CustomMetadataAuditSerializer();
            cm.serialize((CustomMetadataAttributesAuditDetail) detail, gen, sp);
        }
    }
}
