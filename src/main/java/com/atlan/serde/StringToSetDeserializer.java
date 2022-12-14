/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.Set;

/**
 * Deserialization of plain strings into sets, as expected in certain places in the API.
 */
public class StringToSetDeserializer extends StdDeserializer<Set<String>> {
    private static final long serialVersionUID = 2L;

    public StringToSetDeserializer() {
        this(null);
    }

    public StringToSetDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Set<String> deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException {
        return Serde.mapper.readValue(jsonparser.getText(), new TypeReference<>() {});
    }
}
