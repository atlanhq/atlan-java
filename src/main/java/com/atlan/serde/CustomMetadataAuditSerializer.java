/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.cache.CustomMetadataCache;
import com.atlan.exception.AtlanException;
import com.atlan.model.search.CustomMetadataAttributesAuditDetail;
import com.atlan.util.JacksonUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Custom serialization of {@link CustomMetadataAttributesAuditDetail} objects.
 * In particular, this translates from the human-readable name into the Atlan-internal hashed-string representation for
 * custom metadata.
 */
public class CustomMetadataAuditSerializer extends StdSerializer<CustomMetadataAttributesAuditDetail> {
    private static final long serialVersionUID = 2L;

    public CustomMetadataAuditSerializer() {
        this(null);
    }

    public CustomMetadataAuditSerializer(Class<CustomMetadataAttributesAuditDetail> t) {
        super(t);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serializeWithType(
            CustomMetadataAttributesAuditDetail value,
            JsonGenerator gen,
            SerializerProvider serializers,
            TypeSerializer typeSer)
            throws IOException {
        serialize(value, gen, serializers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(CustomMetadataAttributesAuditDetail cm, JsonGenerator gen, SerializerProvider sp)
            throws IOException, JsonProcessingException {

        String cmName = cm.getTypeName();
        String cmId;
        try {
            cmId = CustomMetadataCache.getIdForName(cmName);
        } catch (AtlanException e) {
            throw new IOException("Unable to find custom metadata with name: " + cmName, e);
        }
        cm.setTypeName(cmId);

        gen.writeStartObject();
        JacksonUtils.serializeString(gen, "typeName", cm.getTypeName());
        Map<String, Object> attributes = new LinkedHashMap<>();
        try {
            CustomMetadataCache.getAttributesFromCustomMetadata(cmId, cmName, cm, attributes);
        } catch (AtlanException e) {
            throw new IOException("Unable to translate custom metadata attributes: " + cm, e);
        }
        sp.defaultSerializeField("attributes", attributes, gen);
        gen.writeEndObject();
    }
}
