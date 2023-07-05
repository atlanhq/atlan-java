/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.net.ApiResource;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * API endpoints for access Atlan's event logs.
 */
public class LogsEndpoint extends HeraclesEndpoint {

    private static final String endpoint = "/events";
    private static final String endpoint_main = endpoint + "/main";
    private static final String endpoint_login = endpoint + "/login";

    private final AtlanClient client;

    public LogsEndpoint(AtlanClient client) {
        this.client = client;
    }

    // TODO: Looks like this maps to the Keycloak endpoint GET /{realm}/admin-events
    //  - Can reuse the model from AtlanUser.AdminEvent
    public RawResponse getMain() throws AtlanException {
        String url = String.format("%s%s?operationTypes=CREATE", getBaseUrl(client), endpoint_main);
        return ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", RawResponse.class, null);
    }

    // TODO: Looks like this maps to the Keycloak endpoint ???
    //  - Can reuse the model from AtlanUser.LoginEvent
    public RawResponse getLogins() throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(client), endpoint_login);
        return ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", RawResponse.class, null);
    }

    /**
     * Necessary for handling responses as plain text.
     */
    @Getter
    @JsonSerialize(using = RawResponseSerializer.class)
    @JsonDeserialize(using = RawResponseDeserializer.class)
    @EqualsAndHashCode(callSuper = false)
    public static final class RawResponse extends ApiResource {
        String response;

        public RawResponse(String response) {
            this.response = response;
        }
    }

    private static class RawResponseDeserializer extends StdDeserializer<RawResponse> {
        private static final long serialVersionUID = 2L;

        public RawResponseDeserializer() {
            this(null);
        }

        public RawResponseDeserializer(Class<?> t) {
            super(t);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public RawResponse deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            String response = parser.getCodec().readTree(parser).toString();
            return new RawResponse(response);
        }
    }

    private static class RawResponseSerializer extends StdSerializer<RawResponse> {
        private static final long serialVersionUID = 2L;

        public RawResponseSerializer() {
            this(null);
        }

        public RawResponseSerializer(Class<RawResponse> t) {
            super(t);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void serialize(RawResponse rawResponse, JsonGenerator gen, SerializerProvider sp)
                throws IOException, JsonProcessingException {
            String response = rawResponse.getResponse();
            gen.writeRaw(response);
        }
    }
}
