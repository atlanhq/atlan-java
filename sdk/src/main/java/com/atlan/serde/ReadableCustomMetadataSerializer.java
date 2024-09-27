/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.model.core.CustomMetadataAttributes;
import com.atlan.util.JacksonUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom serialization of a map of custom metadata.
 * In particular, this retains human-readable names throughout.
 */
@Slf4j
public class ReadableCustomMetadataSerializer extends StdSerializer<Map<String, CustomMetadataAttributes>> {
    private static final long serialVersionUID = 2L;

    // TODO: Pass the Map<String, CustomMetadataAttributes> to the other constructor, rather than null
    public ReadableCustomMetadataSerializer() {
        super(TypeFactory.defaultInstance().constructType(Map.class));
    }

    public ReadableCustomMetadataSerializer(Class<Map<String, CustomMetadataAttributes>> t) {
        super(t);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serializeWithType(
            Map<String, CustomMetadataAttributes> value,
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
    public void serialize(Map<String, CustomMetadataAttributes> cmMap, JsonGenerator gen, SerializerProvider sp)
            throws IOException, JsonProcessingException {
        gen.writeStartObject();
        if (cmMap != null) {
            for (Map.Entry<String, CustomMetadataAttributes> entry : cmMap.entrySet()) {
                String cmName = entry.getKey();
                if (cmName != null) {
                    CustomMetadataAttributes cma = entry.getValue();
                    if (cma != null && !cma.isEmpty()) {
                        JacksonUtils.serializeObject(gen, cmName, cma.getAttributes());
                    }
                }
            }
        }
        gen.writeEndObject();
    }
}
