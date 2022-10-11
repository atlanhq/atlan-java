/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.model.admin.Session;
import com.atlan.model.admin.SessionResponse;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Serialization of the SessionResponse object, since it is only a list of sessions (no wrapper),
 * and for all our API interactions we need an object that extends ApiResource.
 */
@Slf4j
public class SessionResponseSerializer extends StdSerializer<SessionResponse> {
    private static final long serialVersionUID = 2L;

    public SessionResponseSerializer() {
        this(null);
    }

    public SessionResponseSerializer(Class<SessionResponse> t) {
        super(t);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(SessionResponse sessionResponse, JsonGenerator gen, SerializerProvider sp)
            throws IOException, JsonProcessingException {
        List<Session> sessions = sessionResponse.getSessions();
        Serde.mapper.writeValue(gen, sessions);
    }
}
