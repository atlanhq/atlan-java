/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.AtlanClient;
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

    private final transient AtlanClient client;

    public StringToSetDeserializer(AtlanClient client) {
        this(null, client);
    }

    public StringToSetDeserializer(Class<?> vc, AtlanClient client) {
        super(vc);
        this.client = client;
    }

    @Override
    public Set<String> deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException {
        return deserialize(client, jsonparser.getText());
    }

    /**
     * Deserialize the provided string value into a set.
     *
     * @param client connectivity to Atlan
     * @param value to deserialize
     * @return a set, as if the provided value were an actual set rather than a string
     * @throws IOException on any errors during parsing
     */
    public static Set<String> deserialize(AtlanClient client, String value) throws IOException {
        return client.readValue(value, new TypeReference<>() {});
    }
}
