/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.OAuthClient;
import com.atlan.model.admin.OAuthClientResponse;
import com.atlan.model.admin.OAuthExchangeResponse;
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

/**
 * API endpoints for managing Atlan's OAuth clients.
 */
public class OAuthClientsEndpoint extends HeraclesEndpoint {

    private static final String endpoint = "/oauth-clients";
    private static final String exchangeEndpoint = endpoint + "/token";

    public OAuthClientsEndpoint(AtlanClient client) {
        super(client);
    }

    /**
     * Retrieves a list of the 20 most recently created clients defined in Atlan.
     *
     * @return a list of the 20 most recently created clients in Atlan
     * @throws AtlanException on any API communication issue
     */
    public OAuthClientResponse list() throws AtlanException {
        return list(null);
    }

    /**
     * Retrieves a list of the 20 most recently created clients defined in Atlan.
     *
     * @param options to override default client settings
     * @return a list of the 20 most recently created clients in Atlan
     * @throws AtlanException on any API communication issue
     */
    public OAuthClientResponse list(RequestOptions options) throws AtlanException {
        return list(null, "-createdAt", 0, 20, options);
    }

    /**
     * Retrieves a list of the OAuth clients defined in Atlan.
     *
     * @param filter which clients to retrieve
     * @param sort property by which to sort the results
     * @param offset starting point for results to return, for paging
     * @param limit maximum number of results to be returned
     * @return a list of clients that match the provided criteria
     * @throws AtlanException on any API communication issue
     */
    public OAuthClientResponse list(String filter, String sort, int offset, int limit) throws AtlanException {
        return list(filter, sort, offset, limit, null);
    }

    /**
     * Retrieves a list of the OAuth clients defined in Atlan.
     *
     * @param filter which clients to retrieve
     * @param sort property by which to sort the results
     * @param offset starting point for results to return, for paging
     * @param limit maximum number of results to be returned
     * @param options to override default client settings
     * @return a list of clients that match the provided criteria
     * @throws AtlanException on any API communication issue
     */
    public OAuthClientResponse list(String filter, String sort, int offset, int limit, RequestOptions options)
        throws AtlanException {
        String url = String.format("%s%s?limit=%s&offset=%s&count=true", getBaseUrl(), endpoint, limit, offset);
        if (sort != null) {
            url = String.format("%s&sort=%s", url, sort);
        }
        if (filter != null) {
            url = String.format("%s&filter=%s", url, filter);
        }
        return ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", OAuthClientResponse.class, options);
    }

    /**
     * Retrieves the OAuth client with a name that exactly matches the provided string.
     *
     * @param displayName name (as it appears in the UI) by which to retrieve the OAuth client
     * @return the OAuth client whose name (in the UI) matches the provided string, or null if there is none
     * @throws AtlanException on any error during API invocation
     */
    public OAuthClient get(String displayName) throws AtlanException {
        OAuthClientResponse response = list("{\"displayName\":\"" + displayName + "\"}", "-createdAt", 0, 2);
        if (response != null
            && response.getRecords() != null
            && !response.getRecords().isEmpty()) {
            return response.getRecords().get(0);
        } else {
            return null;
        }
    }

    /**
     * Retrieves the OAuth client with a client ID that exactly matches the provided string.
     *
     * @param clientId unique client identifier by which to retrieve the OAuth client
     * @return the OAuth client whose clientId matches the provided string, or null if there is none
     * @throws AtlanException on any error during API invocation
     */
    public OAuthClient getById(String clientId) throws AtlanException {
        OAuthClientResponse response = list("{\"clientId\":\"" + clientId + "\"}", "-createdAt", 0, 2);
        if (response != null
            && response.getRecords() != null
            && !response.getRecords().isEmpty()) {
            return response.getRecords().get(0);
        } else {
            return null;
        }
    }

    /**
     * Retrieves the OAuth client with a unique ID (GUID) that exactly matches the provided string.
     *
     * @param guid unique identifier by which to retrieve the OAuth client
     * @return the OAuth client whose GUID matches the provided string, or null if there is none
     * @throws AtlanException on any error during API invocation
     */
    public OAuthClient getByGuid(String guid) throws AtlanException {
        OAuthClientResponse response = list("{\"id\":\"" + guid + "\"}", "-createdAt", 0, 2);
        if (response != null
            && response.getRecords() != null
            && !response.getRecords().isEmpty()) {
            return response.getRecords().get(0);
        } else {
            return null;
        }
    }

    /**
     * Create a new OAuth client with the provided settings.
     *
     * @param displayName human-readable name for the OAuth client
     * @param description optional explanation of the OAuth client
     * @param personas unique names (qualifiedNames) of personas that should be linked to the client
     * @param role workspace role the client should be bound to
     * @return the created OAuth client
     * @throws AtlanException on any API communication issue
     */
    public OAuthClient create(String displayName, String description, Set<String> personas, String role)
        throws AtlanException {
        return create(displayName, description, personas, role, null);
    }

    /**
     * Create a new OAuth client with the provided settings.
     *
     * @param displayName human-readable name for the OAuth client
     * @param description optional explanation of the OAuth client
     * @param personas unique names (qualifiedNames) of personas that should be linked to the client
     * @param role workspace role the client should be bound to
     * @param options to override default client settings
     * @return the created OAuth client
     * @throws AtlanException on any API communication issue
     */
    public OAuthClient create(
        String displayName, String description, Set<String> personas, String role, RequestOptions options)
        throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), endpoint);
        OAuthClientRequest ocr = new OAuthClientRequest(displayName, description, personas, role);
        if (options == null) {
            options = RequestOptions.from(client)
                .readTimeout(client.getReadTimeout() * 3)
                .build();
        }
        WrappedOAuthClient response =
            ApiResource.request(client, ApiResource.RequestMethod.POST, url, ocr, WrappedOAuthClient.class, options);
        if (response != null) {
            OAuthClient token = response.getToken();
            token.setRawJsonObject(response.getRawJsonObject());
            return token;
        }
        return null;
    }

    /**
     * Update an existing OAuth client with the provided settings.
     *
     * @param clientId unique identifier of the OAuth client
     * @param displayName human-readable name for the OAuth client
     * @param description optional explanation of the OAuth client
     * @return the updated OAuth client
     * @throws AtlanException on any API communication issue
     */
    public OAuthClient update(String clientId, String displayName, String description)
        throws AtlanException {
        return update(clientId, displayName, description, null);
    }

    /**
     * Update an existing OAuth client with the provided settings.
     *
     * @param clientId unique identifier of the OAuth client
     * @param displayName human-readable name for the OAuth client
     * @param description optional explanation of the OAuth client
     * @param options to override default client settings
     * @return the updated OAuth client
     * @throws AtlanException on any API communication issue
     */
    public OAuthClient update(
        String clientId, String displayName, String description, RequestOptions options)
        throws AtlanException {
        String url = String.format("%s%s/%s", getBaseUrl(), endpoint, clientId);
        OAuthClientRequest ocr = new OAuthClientRequest(displayName, description, null, null);
        WrappedOAuthClient response =
            ApiResource.request(client, ApiResource.RequestMethod.POST, url, ocr, WrappedOAuthClient.class, options);
        if (response != null) {
            OAuthClient token = response.getToken();
            token.setRawJsonObject(response.getRawJsonObject());
            return token;
        }
        return null;
    }

    /**
     * Delete (purge) the specified OAuth client.
     *
     * @param clientId unique identifier of the OAuth client to delete
     * @throws AtlanException on any API communication issue
     */
    public void purge(String clientId) throws AtlanException {
        purge(clientId, null);
    }

    /**
     * Delete (purge) the specified OAuth client.
     *
     * @param clientId unique identifier of the OAuth client to delete
     * @param options to override default client settings
     * @throws AtlanException on any API communication issue
     */
    public void purge(String clientId, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s/%s", getBaseUrl(), endpoint, clientId);
        ApiResource.request(client, ApiResource.RequestMethod.DELETE, url, "", options);
    }

    /**
     * Exchange client details for a (short-lived) bearer token.
     *
     * @param clientId unique identifier of the OAuth client
     * @param clientSecret secret for the OAuth client
     * @throws AtlanException on any API communication issue
     */
    public OAuthExchangeResponse exchange(String clientId, String clientSecret) throws AtlanException {
        return exchange(clientId, clientSecret, null);
    }

    /**
     * Exchange client details for a (short-lived) bearer token.
     *
     * @param clientId unique identifier of the OAuth client
     * @param clientSecret secret for the OAuth client
     * @param options to override default client settings
     * @throws AtlanException on any API communication issue
     */
    public OAuthExchangeResponse exchange(String clientId, String clientSecret, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), exchangeEndpoint);
        OAuthExchangeRequest oer = new OAuthExchangeRequest(clientId, clientSecret);
        return ApiResource.request(client, ApiResource.RequestMethod.POST, url, oer, OAuthExchangeResponse.class, options);
    }

    /**
     * Necessary for wrapping a request for a new OAuth client into an object that extends ApiResource for API interactions.
     */
    @Getter
    @EqualsAndHashCode(callSuper = false)
    @SuppressWarnings("serial")
    private static final class OAuthClientRequest extends ApiResource {
        private static final long serialVersionUID = 2L;

        /** Human-readable name provided when creating the client. */
        String displayName;

        /** Explanation of the client. */
        @JsonInclude(JsonInclude.Include.ALWAYS)
        String description;

        /** Unique names (qualifiedNames) of personas to associate with the client. */
        Set<String> personaQNs;

        /** Workspace role the client should be bound to. */
        String role;

        public OAuthClientRequest(String displayName, String description, Set<String> personaQNs, String role) {
            this.displayName = displayName;
            this.description = description == null ? "" : description;
            this.personaQNs = personaQNs;
            this.role = role;
        }
    }

    /**
     * Necessary for wrapping a request for a new bearer token into an object that extends ApiResource for API interactions.
     */
    @Getter
    @EqualsAndHashCode(callSuper = false)
    @SuppressWarnings("serial")
    private static final class OAuthExchangeRequest extends ApiResource {
        private static final long serialVersionUID = 2L;

        /** Human-readable name provided when creating the client. */
        String clientId;

        /** Explanation of the client. */
        String clientSecret;

        public OAuthExchangeRequest(String clientId, String clientSecret) {
            this.clientId = clientId;
            this.clientSecret = clientSecret;
        }
    }

    /**
     * Necessary for handling responses that are plain OAuthClients without any wrapping.
     */
    @Getter
    @JsonSerialize(using = WrappedOAuthClientSerializer.class)
    @JsonDeserialize(using = WrappedOAuthClientDeserializer.class)
    @EqualsAndHashCode(callSuper = false)
    private static final class WrappedOAuthClient extends ApiResource {
        private static final long serialVersionUID = 2L;

        OAuthClient token;

        public WrappedOAuthClient(OAuthClient token) {
            this.token = token;
        }
    }

    private static class WrappedOAuthClientDeserializer extends StdDeserializer<WrappedOAuthClient> {
        private static final long serialVersionUID = 2L;

        public WrappedOAuthClientDeserializer() {
            this(WrappedOAuthClient.class);
        }

        public WrappedOAuthClientDeserializer(Class<?> t) {
            super(t);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public WrappedOAuthClient deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            OAuthClient token = parser.getCodec().readValue(parser, new TypeReference<>() {});
            return new WrappedOAuthClient(token);
        }
    }

    private static class WrappedOAuthClientSerializer extends StdSerializer<WrappedOAuthClient> {
        private static final long serialVersionUID = 2L;

        private final transient AtlanClient client;

        @SuppressWarnings("UnusedMethod")
        public WrappedOAuthClientSerializer(AtlanClient client) {
            this(WrappedOAuthClient.class, client);
        }

        public WrappedOAuthClientSerializer(Class<WrappedOAuthClient> t, AtlanClient client) {
            super(t);
            this.client = client;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void serialize(WrappedOAuthClient wrappedToken, JsonGenerator gen, SerializerProvider sp)
            throws IOException, JsonProcessingException {
            OAuthClient token = wrappedToken.getToken();
            client.writeValue(gen, token);
        }
    }
}
