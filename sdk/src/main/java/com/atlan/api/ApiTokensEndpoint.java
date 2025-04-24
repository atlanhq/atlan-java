/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.ApiToken;
import com.atlan.model.admin.ApiTokenResponse;
import com.atlan.net.ApiResource;
import com.atlan.net.RequestOptions;
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
import lombok.Singular;

/**
 * API endpoints for managing Atlan's API tokens.
 */
public class ApiTokensEndpoint extends HeraclesEndpoint {

    private static final long MAX_VALIDITY = 157680000L;
    private static final String endpoint = "/apikeys";

    public ApiTokensEndpoint(AtlanClient client) {
        super(client);
    }

    /**
     * Retrieves a list of the 20 most recently created tokens defined in Atlan.
     *
     * @return a list of the 20 most recently created tokens in Atlan
     * @throws AtlanException on any API communication issue
     */
    public ApiTokenResponse list() throws AtlanException {
        return list(null);
    }

    /**
     * Retrieves a list of the 20 most recently created tokens defined in Atlan.
     *
     * @param options to override default client settings
     * @return a list of the 20 most recently created tokens in Atlan
     * @throws AtlanException on any API communication issue
     */
    public ApiTokenResponse list(RequestOptions options) throws AtlanException {
        return list(null, "-createdAt", 0, 20, options);
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
    public ApiTokenResponse list(String filter, String sort, int offset, int limit) throws AtlanException {
        return list(filter, sort, offset, limit, null);
    }

    /**
     * Retrieves a list of the API tokens defined in Atlan.
     *
     * @param filter which tokens to retrieve
     * @param sort property by which to sort the results
     * @param offset starting point for results to return, for paging
     * @param limit maximum number of results to be returned
     * @param options to override default client settings
     * @return a list of tokens that match the provided criteria
     * @throws AtlanException on any API communication issue
     */
    public ApiTokenResponse list(String filter, String sort, int offset, int limit, RequestOptions options)
            throws AtlanException {
        String url = String.format("%s%s?limit=%s&offset=%s", getBaseUrl(), endpoint, limit, offset);
        if (sort != null) {
            url = String.format("%s&sort=%s", url, sort);
        }
        if (filter != null) {
            url = String.format("%s&filter=%s", url, filter);
        }
        return ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", ApiTokenResponse.class, options);
    }

    /**
     * Retrieves the API token with a name that exactly matches the provided string.
     *
     * @param displayName name (as it appears in the UI) by which to retrieve the API token
     * @return the API token whose name (in the UI) matches the provided string, or null if there is none
     * @throws AtlanException on any error during API invocation
     */
    public ApiToken get(String displayName) throws AtlanException {
        ApiTokenResponse response = list("{\"displayName\":\"" + displayName + "\"}", "-createdAt", 0, 2);
        if (response != null
                && response.getRecords() != null
                && !response.getRecords().isEmpty()) {
            return response.getRecords().get(0);
        } else {
            return null;
        }
    }

    /**
     * Retrieves the API token with a client ID that exactly matches the provided string.
     *
     * @param clientId unique client identifier by which to retrieve the API token
     * @return the API token whose cliendId matches the provided string, or null if there is none
     * @throws AtlanException on any error during API invocation
     */
    public ApiToken getById(String clientId) throws AtlanException {
        if (clientId != null && clientId.startsWith(ApiToken.API_USERNAME_PREFIX)) {
            clientId = clientId.substring(ApiToken.API_USERNAME_PREFIX.length());
        }
        ApiTokenResponse response = list("{\"clientId\":\"" + clientId + "\"}", "-createdAt", 0, 2);
        if (response != null
                && response.getRecords() != null
                && !response.getRecords().isEmpty()) {
            return response.getRecords().get(0);
        } else {
            return null;
        }
    }

    /**
     * Retrieves the API token with a unique ID (GUID) that exactly matches the provided string.
     *
     * @param guid unique identifier by which to retrieve the API token
     * @return the API token whose GUID matches the provided string, or null if there is none
     * @throws AtlanException on any error during API invocation
     */
    public ApiToken getByGuid(String guid) throws AtlanException {
        ApiTokenResponse response = list("{\"id\":\"" + guid + "\"}", "-createdAt", 0, 2);
        if (response != null
                && response.getRecords() != null
                && !response.getRecords().isEmpty()) {
            return response.getRecords().get(0);
        } else {
            return null;
        }
    }

    /**
     * Create a new API token with the provided settings.
     *
     * @param displayName human-readable name for the API token
     * @param description optional explanation of the API token
     * @param personas unique names (qualifiedNames) of personas that should be linked to the token
     * @param validitySeconds time in seconds after which the token should expire
     * @return the created API token
     * @throws AtlanException on any API communication issue
     */
    public ApiToken create(String displayName, String description, Set<String> personas, Long validitySeconds)
            throws AtlanException {
        return create(displayName, description, personas, validitySeconds, null);
    }

    /**
     * Create a new API token with the provided settings.
     *
     * @param displayName human-readable name for the API token
     * @param description optional explanation of the API token
     * @param personas unique names (qualifiedNames) of personas that should be linked to the token
     * @param validitySeconds time in seconds after which the token should expire
     * @param options to override default client settings
     * @return the created API token
     * @throws AtlanException on any API communication issue
     */
    public ApiToken create(
            String displayName, String description, Set<String> personas, Long validitySeconds, RequestOptions options)
            throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), endpoint);
        ApiTokenRequest atr = new ApiTokenRequest(displayName, description, personas, validitySeconds);
        if (options == null) {
            options = RequestOptions.from(client)
                    .readTimeout(client.getReadTimeout() * 3)
                    .build();
        }
        WrappedApiToken response =
                ApiResource.request(client, ApiResource.RequestMethod.POST, url, atr, WrappedApiToken.class, options);
        if (response != null) {
            ApiToken token = response.getToken();
            token.setRawJsonObject(response.getRawJsonObject());
            return token;
        }
        return null;
    }

    /**
     * Update an existing API token with the provided settings.
     *
     * @param displayName human-readable name for the API token
     * @param description optional explanation of the API token
     * @param personas unique names (qualifiedNames) of personas that should be linked to the token
     * @return the updated API token
     * @throws AtlanException on any API communication issue
     */
    public ApiToken update(String guid, String displayName, String description, Set<String> personas)
            throws AtlanException {
        return update(guid, displayName, description, personas, null);
    }

    /**
     * Update an existing API token with the provided settings.
     *
     * @param guid unique identifier (GUID) of the API token
     * @param displayName human-readable name for the API token
     * @param description optional explanation of the API token
     * @param personas unique names (qualifiedNames) of personas that should be linked to the token
     * @param options to override default client settings
     * @return the updated API token
     * @throws AtlanException on any API communication issue
     */
    public ApiToken update(
            String guid, String displayName, String description, Set<String> personas, RequestOptions options)
            throws AtlanException {
        String url = String.format("%s%s/%s", getBaseUrl(), endpoint, guid);
        ApiTokenRequest atr = new ApiTokenRequest(displayName, description, personas, null);
        WrappedApiToken response =
                ApiResource.request(client, ApiResource.RequestMethod.POST, url, atr, WrappedApiToken.class, options);
        if (response != null) {
            ApiToken token = response.getToken();
            token.setRawJsonObject(response.getRawJsonObject());
            return token;
        }
        return null;
    }

    /**
     * Delete (purge) the specified API token.
     *
     * @param guid unique identifier (GUID) of the API token to delete
     * @throws AtlanException on any API communication issue
     */
    public void purge(String guid) throws AtlanException {
        purge(guid, null);
    }

    /**
     * Delete (purge) the specified API token.
     *
     * @param guid unique identifier (GUID) of the API token to delete
     * @param options to override default client settings
     * @throws AtlanException on any API communication issue
     */
    public void purge(String guid, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s/%s", getBaseUrl(), endpoint, guid);
        ApiResource.request(client, ApiResource.RequestMethod.DELETE, url, "", options);
    }

    /**
     * Necessary for wrapping a request for a new API token into an object that extends ApiResource for API interactions.
     */
    @Getter
    @EqualsAndHashCode(callSuper = false)
    @SuppressWarnings("serial")
    private static final class ApiTokenRequest extends ApiResource {
        private static final long serialVersionUID = 2L;

        /** Human-readable name provided when creating the token. */
        String displayName;

        /** Explanation of the token. */
        @JsonInclude(JsonInclude.Include.ALWAYS)
        String description;

        /**
         * Unique identifiers (GUIDs) of personas that are associated with the token.
         * @deprecated see {@link #personaQualifiedNames}
         */
        @JsonInclude(JsonInclude.Include.ALWAYS)
        @Deprecated
        final Set<String> personas = Collections.emptySet();

        /** Unique names (qualifiedNames) of personas that are associated with the token. */
        @JsonInclude(JsonInclude.Include.ALWAYS)
        @Singular
        Set<String> personaQualifiedNames;

        /** Length of time, in seconds, after which the token will expire and no longer be usable. */
        Long validitySeconds;

        public ApiTokenRequest(String displayName, String description, Set<String> personas, Long validitySeconds) {
            this.displayName = displayName;
            this.description = description == null ? "" : description;
            this.personaQualifiedNames = personas == null ? Collections.emptySet() : personas;
            if (validitySeconds != null) {
                if (validitySeconds < 0) {
                    // Treat negative numbers as "infinite" (never expire)
                    this.validitySeconds = MAX_VALIDITY;
                } else {
                    // Otherwise use "infinite" as the ceiling for values
                    this.validitySeconds = Math.min(validitySeconds, MAX_VALIDITY);
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
        private static final long serialVersionUID = 2L;

        ApiToken token;

        public WrappedApiToken(ApiToken token) {
            this.token = token;
        }
    }

    private static class WrappedApiTokenDeserializer extends StdDeserializer<WrappedApiToken> {
        private static final long serialVersionUID = 2L;

        public WrappedApiTokenDeserializer() {
            this(WrappedApiToken.class);
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

        private final transient AtlanClient client;

        @SuppressWarnings("UnusedMethod")
        public WrappedApiTokenSerializer(AtlanClient client) {
            this(WrappedApiToken.class, client);
        }

        public WrappedApiTokenSerializer(Class<WrappedApiToken> t, AtlanClient client) {
            super(t);
            this.client = client;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void serialize(WrappedApiToken wrappedToken, JsonGenerator gen, SerializerProvider sp)
                throws IOException, JsonProcessingException {
            ApiToken token = wrappedToken.getToken();
            client.writeValue(gen, token);
        }
    }
}
