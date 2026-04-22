/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.List;

/**
 * Deserializes a field that can arrive as either a plain string or a JSON array of strings.
 * Redis stores user attributes as flat strings, while Keycloak returns them as arrays.
 */
public class StringOrListDeserializer extends StdDeserializer<List<String>> {
    private static final long serialVersionUID = 2L;

    public StringOrListDeserializer() {
        super(List.class);
    }

    @Override
    public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.currentToken() == JsonToken.VALUE_STRING) {
            return List.of(p.getText());
        }
        List<String> result = new java.util.ArrayList<>();
        while (p.nextToken() != JsonToken.END_ARRAY) {
            result.add(p.getText());
        }
        return result;
    }
}
