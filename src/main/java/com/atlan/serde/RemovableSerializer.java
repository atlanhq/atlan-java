/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

/**
 * Serialization of the {@link Removable} placeholder object, to allow the SDK to serialize null values only when
 * it should intentionally clear out some value.
 */
public class RemovableSerializer extends StdSerializer<Removable> {
    private static final long serialVersionUID = 2L;

    public RemovableSerializer() {
        this(null);
    }

    public RemovableSerializer(Class<Removable> t) {
        super(t);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(Removable removable, JsonGenerator gen, SerializerProvider sp)
            throws IOException, JsonProcessingException {
        if (removable.isJsonNull()) {
            if (removable.getType() == Removable.TYPE.PRIMITIVE) {
                gen.writeNull();
            } else {
                gen.writeStartArray();
                gen.writeEndArray();
            }
        }
    }
}
