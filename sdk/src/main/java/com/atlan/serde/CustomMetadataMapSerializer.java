/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.CustomMetadataAttributes;
import com.atlan.util.JacksonUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom serialization of a map of custom metadata.
 * In particular, this translates from the human-readable name into the Atlan-internal hashed-string representation for
 * custom metadata.
 */
@Slf4j
public class CustomMetadataMapSerializer extends StdSerializer<Map<String, CustomMetadataAttributes>> {
    private static final long serialVersionUID = 2L;

    private final transient AtlanClient client;

    // TODO: Pass the Map<String, CustomMetadataAttributes> to the other constructor, rather than null
    public CustomMetadataMapSerializer(AtlanClient client) {
        this(null, client);
    }

    public CustomMetadataMapSerializer(Class<Map<String, CustomMetadataAttributes>> t, AtlanClient client) {
        super(t);
        this.client = client;
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
                    String cmId;
                    if (cmName.equals(Serde.DELETED_AUDIT_OBJECT)) {
                        cmId = Serde.DELETED_AUDIT_OBJECT;
                        JacksonUtils.serializeObject(gen, cmId, Collections.emptyMap());
                    } else {
                        try {
                            cmId = client.getCustomMetadataCache().getSidForName(cmName);
                            if (cma != null) {
                                Map<String, Object> idToValue = new HashMap<>();
                                client.getCustomMetadataCache()
                                        .getIdMapFromNameMap(cmName, cma.getAttributes(), idToValue);
                                JacksonUtils.serializeObject(gen, cmId, idToValue);
                            } else {
                                JacksonUtils.serializeObject(gen, cmId, Collections.emptyMap());
                            }
                        } catch (AtlanException e) {
                            log.error(
                                    "Unable to find custom metadata with name {}, or translate one of its attributes.",
                                    cmName,
                                    e);
                        }
                    }
                }
            }
        }
        gen.writeEndObject();
    }
}
