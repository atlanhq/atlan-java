/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.AtlanClient;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.Set;

/**
 * Serialization of sets into plain strings, as expected in certain places in the API.
 */
public class SetToStringSerializer extends StdSerializer<Set<String>> {
    private static final long serialVersionUID = 2L;

    private final transient AtlanClient client;

    public SetToStringSerializer(AtlanClient client) {
        this(null, client);
    }

    public SetToStringSerializer(Class<Set<String>> t, AtlanClient client) {
        super(t);
        this.client = client;
    }

    @Override
    public void serialize(Set<String> value, JsonGenerator gen, SerializerProvider arg2)
            throws IOException, JsonProcessingException {
        gen.writeString(serialize(client, value));
    }

    /**
     * Serialize the provided set of values into a single string.
     *
     * @param client connectivity to Atlan
     * @param value set of values to serialize into a string
     * @return a string representing the entire set of values
     * @throws IOException on any errors during parsing
     */
    public static String serialize(AtlanClient client, Set<String> value) throws IOException {
        return client.writeValueAsString(value);
    }
}
