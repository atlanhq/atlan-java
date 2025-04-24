/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.ApiConnectionException;
import com.atlan.net.ApiResource;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

public abstract class AbstractEndpoint {
    protected final transient AtlanClient client;

    protected AbstractEndpoint(AtlanClient client) {
        this.client = client;
    }

    /**
     * Retrieve the base URL to use to access the endpoint.
     *
     * @param service the internal service used to access the endpoint
     * @param prefix the prefix used for routing external access to the endpoint
     * @return the base URL (including any prefix) to use to access the API endpoint
     * @throws ApiConnectionException if no base URL has been defined, and the SDK has not been configured for internal access
     */
    protected String getBaseUrl(String service, String prefix) throws ApiConnectionException {
        return client.isInternal() ? service : client.getBaseUrl() + prefix;
    }

    /**
     * Utility class for introspecting raw responses from API endpoints.
     */
    @Getter
    @JsonDeserialize(using = RawResponseDeserializer.class)
    @EqualsAndHashCode(callSuper = false)
    public static final class RawResponse extends ApiResource {
        private static final long serialVersionUID = 2L;
        String payload;

        public RawResponse(String payload) {
            this.payload = payload;
        }
    }

    private static class RawResponseDeserializer extends StdDeserializer<RawResponse> {
        private static final long serialVersionUID = 2L;

        public RawResponseDeserializer() {
            this(RawResponse.class);
        }

        public RawResponseDeserializer(Class<?> t) {
            super(t);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public RawResponse deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            return new RawResponse(parser.getCodec().readTree(parser).toString());
        }
    }
}
