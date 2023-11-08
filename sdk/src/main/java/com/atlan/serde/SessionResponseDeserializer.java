/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.model.admin.Session;
import com.atlan.model.admin.SessionResponse;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.*;

/**
 * Deserialization of the SessionResponse object, since it is only a list of sessions (no wrapper),
 * and for all our API interactions we need an object that extends ApiResource.
 */
public class SessionResponseDeserializer extends StdDeserializer<SessionResponse> {

    private static final long serialVersionUID = 2L;

    public SessionResponseDeserializer() {
        this(null);
    }

    public SessionResponseDeserializer(Class<?> t) {
        super(t);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SessionResponse deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        List<Session> sessions = parser.getCodec().readValue(parser, new TypeReference<>() {});
        return new SessionResponse(sessions);
    }
}
