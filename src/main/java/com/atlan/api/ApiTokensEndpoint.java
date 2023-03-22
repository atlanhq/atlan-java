/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.exception.AtlanException;
import com.atlan.model.admin.ApiToken;
import com.atlan.model.admin.ApiTokenResponse;
import com.atlan.net.ApiResource;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * API endpoints for managing Atlan's API tokens.
 */
public class ApiTokensEndpoint extends HeraclesEndpoint {

    private static final String endpoint = "/apikeys";

    /**
     * Retrieves a list of the 100 most recently created tokens defined in Atlan.
     *
     * @return a list of the 100 most recently created tokens in Atlan
     * @throws AtlanException on any API communication issue
     */
    public static ApiTokenResponse getTokens() throws AtlanException {
        return getTokens(null, "-createdAt", 0, 100);
    }

    /**
     * Retrieves a list of the API tokens defined in Atlan.
     *
     * @param filter which tokens to retrieve
     * @param sort property by which to sort the results
     * @param offset starting point for results to return, for paging
     * @param limit maximum number of results to be returned
     * @return a list of tokens that match the provided criteria
     * @throws AtlanException on any API communication issue
     */
    public static ApiTokenResponse getTokens(String filter, String sort, int offset, int limit) throws AtlanException {
        String url = String.format("%s%s?limit=%s&offset=%s", getBaseUrl(), endpoint, limit, offset);
        if (sort != null) {
            url = String.format("%s&sort=%s", url, sort);
        }
        if (filter != null) {
            url = String.format("%s&filter=%s", url, filter);
        }
        return ApiResource.request(ApiResource.RequestMethod.GET, url, "", ApiTokenResponse.class, null);
    }

    /**
     * Create a new API token with the provided settings.
     *
     * @param displayName human-readable name for the API token
     * @param description optional explanation of the API token
     * @param personas unique identifiers (GUIDs) of personas that should be linked to the token
     * @param validitySeconds time in seconds after which the token should expire
     * @return the created API token
     * @throws AtlanException on any API communication issue
     */
    public static ApiToken create(String displayName, String description, Set<String> personas, Long validitySeconds)
            throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), endpoint);
        ApiTokenRequest atr = new ApiTokenRequest(displayName, description, personas, validitySeconds);
        WrappedApiToken response =
                ApiResource.request(ApiResource.RequestMethod.POST, url, atr, WrappedApiToken.class, null);
        if (response != null) {
            return response.getToken();
        }
        return null;
    }

    /**
     * Update an existing API token with the provided settings.
     *
     * @param displayName human-readable name for the API token
     * @param description optional explanation of the API token
     * @param personas unique identifiers (GUIDs) of personas that should be linked to the token
     * @return the updated API token
     * @throws AtlanException on any API communication issue
     */
    public static ApiToken update(String guid, String displayName, String description, Set<String> personas)
            throws AtlanException {
        String url = String.format("%s%s/%s", getBaseUrl(), endpoint, guid);
        ApiTokenRequest atr = new ApiTokenRequest(displayName, description, personas, null);
        WrappedApiToken response =
                ApiResource.request(ApiResource.RequestMethod.POST, url, atr, WrappedApiToken.class, null);
        if (response != null) {
            return response.getToken();
        }
        return null;
    }

    /**
     * Delete (purge) the specified API token.
     *
     * @param guid unique identifier (GUID) of the API token to delete
     * @throws AtlanException on any API communication issue
     */
    public static void delete(String guid) throws AtlanException {
        String url = String.format("%s%s/%s", getBaseUrl(), endpoint, guid);
        ApiResource.request(ApiResource.RequestMethod.DELETE, url, "", null, null);
    }

    /**
     * Necessary for wrapping a request for a new API token into an object that extends ApiResource for API interactions.
     */
    @Getter
    @EqualsAndHashCode(callSuper = false)
    private static final class ApiTokenRequest extends ApiResource {
        /** Human-readable name provided when creating the token. */
        String displayName;

        /** Explanation of the token. */
        @JsonInclude(JsonInclude.Include.ALWAYS)
        String description;

        /** Unique identifiers (GUIDs) of personas that are associated with the token. */
        @JsonInclude(JsonInclude.Include.ALWAYS)
        Set<String> personas;

        /** Length of time, in seconds, after which the token will expire and no longer be usable. */
        Long validitySeconds;

        public ApiTokenRequest(String displayName, String description, Set<String> personas, Long validitySeconds) {
            this.displayName = displayName;
            this.description = description == null ? "" : description;
            this.personas = personas == null ? Collections.emptySet() : personas;
            if (validitySeconds != null) {
                if (validitySeconds < 0) {
                    // Treat negative numbers as "infinite" (never expire)
                    this.validitySeconds = 409968000L;
                } else {
                    // Otherwise use "infinite" as the ceiling for values
                    this.validitySeconds = Math.max(validitySeconds, 409968000L);
                }
            }
        }
    }

    /**
     * Necessary for handling responses that are plain ApiTokens without any wrapping.
     */
    @Getter
    @JsonSerialize(using = WrappedApiTokenSerializer.class)
    @JsonDeserialize(using = WrappedApiTokenDeserializer.class)
    @EqualsAndHashCode(callSuper = false)
    private static final class WrappedApiToken extends ApiResource {
        ApiToken token;

        public WrappedApiToken(ApiToken token) {
            this.token = token;
        }
    }

    private static class WrappedApiTokenDeserializer extends StdDeserializer<WrappedApiToken> {
        private static final long serialVersionUID = 2L;

        public WrappedApiTokenDeserializer() {
            this(null);
        }

        public WrappedApiTokenDeserializer(Class<?> t) {
            super(t);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public WrappedApiToken deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            ApiToken token = parser.getCodec().readValue(parser, new TypeReference<>() {});
            return new WrappedApiToken(token);
        }
    }

    private static class WrappedApiTokenSerializer extends StdSerializer<WrappedApiToken> {
        private static final long serialVersionUID = 2L;

        public WrappedApiTokenSerializer() {
            this(null);
        }

        public WrappedApiTokenSerializer(Class<WrappedApiToken> t) {
            super(t);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void serialize(WrappedApiToken wrappedToken, JsonGenerator gen, SerializerProvider sp)
                throws IOException, JsonProcessingException {
            ApiToken token = wrappedToken.getToken();
            Serde.mapper.writeValue(gen, token);
        }
    }
}
