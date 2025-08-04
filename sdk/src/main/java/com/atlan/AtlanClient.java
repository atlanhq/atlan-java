/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import com.atlan.api.*;
import com.atlan.cache.*;
import com.atlan.model.core.AtlanCloseable;
import com.atlan.serde.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import java.io.IOException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.Setter;

/**
 * Configuration for the SDK against a particular Atlan tenant.
 */
public class AtlanClient implements AtlanCloseable {
    public static final String DELETED_AUDIT_OBJECT = "(DELETED)";

    /** Timeout value that will be used for making new connections to the Atlan API (in milliseconds). */
    @Getter
    @Setter
    private volatile int connectTimeout = Atlan.DEFAULT_CONNECT_TIMEOUT;

    /**
     * Timeout value that will be used for reading a response from an API request (in milliseconds).
     * Note that this value should be set conservatively because some API requests can take time
     * and a short timeout increases the likelihood of causing a problem in the backend.
     */
    @Getter
    @Setter
    private volatile int readTimeout = Atlan.DEFAULT_READ_TIMEOUT;

    /** Maximum number of times requests will be retried. */
    @Getter
    @Setter
    private volatile int maxNetworkRetries = Atlan.DEFAULT_NETWORK_RETRIES;

    /** Extra headers to include on any requests made by this client. */
    @Getter
    @Setter
    private volatile Map<String, List<String>> extraHeaders;

    private final String apiBase;
    private final boolean internalAccess;
    private final boolean localAccess;

    /** API token to use for authenticating API calls. */
    @Getter
    @Setter
    private volatile String apiToken;

    /** Basic auth settings for running locally (only). */
    @Getter
    private volatile String basicAuth;

    /** Unique identifier (GUID) of the user this client impersonates. */
    @Getter
    @Setter
    private volatile String userId = null;

    /** Proxy to tunnel all Atlan connections. */
    @Getter
    @Setter
    private volatile Proxy connectionProxy = null;

    /** Credential for proxy authorization if required. */
    @Getter
    @Setter
    private volatile PasswordAuthentication proxyCredential = null;

    /** Information about your application. */
    @Getter
    private volatile Map<String, String> appInfo = null;

    /** Cache of Atlan tags specific to this client. */
    @Getter
    private final AtlanTagCache atlanTagCache;

    /** Cache of custom metadata structures specific to this client. */
    @Getter
    private final CustomMetadataCache customMetadataCache;

    /** Cache of enums specific to this client. */
    @Getter
    private final EnumCache enumCache;

    /** Cache of groups specific to this client. */
    @Getter
    private final GroupCache groupCache;

    /** Cache of workspace roles specific to this client. */
    @Getter
    private final RoleCache roleCache;

    /** Cache of users specific to this client. */
    @Getter
    private final UserCache userCache;

    /** Cache of connections specific to this client. */
    @Getter
    private final ConnectionCache connectionCache;

    /** Cache of source-synced tags specific to this client. */
    @Getter
    private final SourceTagCache sourceTagCache;

    private final ObjectMapper mapper;

    /** Endpoint with operations to manage type definitions. */
    public final TypeDefsEndpoint typeDefs;

    /** Endpoint with operations to manage workspace roles. */
    public final RolesEndpoint roles;

    /** Endpoint with operations to manage API tokens. */
    public final ApiTokensEndpoint apiTokens;

    /** Endpoint with operations to manage groups of users. */
    public final GroupsEndpoint groups;

    /** Endpoint with operations to manage users. */
    public final UsersEndpoint users;

    /** Endpoint with operations to manage workflows. */
    public final WorkflowsEndpoint workflows;

    /** Endpoint with operations to manage query parsing. */
    public final QueryParserEndpoint queryParser;

    /** Endpoint with operations to run SQL queries. */
    public final QueriesEndpoint queries;

    /** Endpoint with operations to manage playbooks. */
    public final PlaybooksEndpoint playbooks;

    /** Endpoint with operations to manage logs. */
    public final LogsEndpoint logs;

    /** Endpoint with operations to manage images. */
    public final ImagesEndpoint images;

    /** Endpoint with operations to manage files. */
    public final FilesEndpoint files;

    /** Endpoint with operations to manage assets. */
    public final AssetEndpoint assets;

    /** Endpoint with operations to manage requests. */
    public final RequestsEndpoint requests;

    /** Endpoint with operations to impersonate users. */
    public final ImpersonationEndpoint impersonate;

    /** Endpoint with operations to search details of past searches. */
    public final SearchLogEndpoint searchLog;

    /** Endpoint with operations to manage credentials (of connectors). */
    public final CredentialsEndpoint credentials;

    /** Endpoint with operations to manage background tasks. */
    public final TaskEndpoint tasks;

    /** Endpoint with operations to manage SSO configuration. */
    public final SSOEndpoint sso;

    /** Endpoint with operations to interact with OpenLineage. */
    public final OpenLineageEndpoint openLineage;

    /** Endpoint with operations to interact with data contracts. */
    public final ContractsEndpoint contracts;

    /** Client-aware asset deserializer. */
    @Getter
    private final AssetDeserializer assetDeserializer;

    /** Client-aware relationship attributes deserializer. */
    @Getter
    private final RelationshipAttributesDeserializer relationshipAttributesDeserializer;

    /** Client-aware custom metadata deserializer. */
    @Getter
    private final CustomMetadataAuditDeserializer customMetadataAuditDeserializer;

    /** Client-aware Atlan tag deserializer. */
    @Getter
    private final AtlanTagDeserializer atlanTagDeserializer;

    /**
     * Instantiate a new client.
     * This will take the URL of the tenant from the environment variable {@code ATLAN_BASE_URL}
     * and the API token for accessing the tenant from the environment variable {@code ATLAN_API_KEY}.
     */
    public AtlanClient() {
        this(System.getenv("ATLAN_BASE_URL"));
    }

    /**
     * Instantiate a new client.
     * This will take the API token for accessing the tenant from the environment variable {@code ATLAN_API_KEY}.
     *
     * @param baseURL of the tenant, including {@code https://}
     */
    public AtlanClient(final String baseURL) {
        this(baseURL, System.getenv("ATLAN_API_KEY"));
    }

    /**
     * Instantiate a new client.
     *
     * @param baseURL of the tenant, including {@code https://}
     * @param apiToken API token to use for accessing the tenant
     */
    @SuppressWarnings("this-escape")
    public AtlanClient(final String baseURL, final String apiToken) {
        extraHeaders = new ConcurrentHashMap<>();
        extraHeaders.putAll(Atlan.EXTRA_HEADERS);
        if (baseURL == null) {
            throw new IllegalStateException(Atlan.INVALID_CLIENT_MSG);
        } else if (baseURL.isEmpty()) {
            throw new IllegalStateException(Atlan.BLANK_CLIENT_MSG);
        } else if (baseURL.equals("INTERNAL")) {
            apiBase = null;
            internalAccess = true;
            localAccess = false;
        } else if (baseURL.equals("LOCAL")) {
            internalAccess = false;
            localAccess = true;
            apiBase = "http://localhost:21000";
            this.basicAuth = apiToken;
        } else {
            internalAccess = false;
            localAccess = false;
            apiBase = Atlan.prepURL(baseURL);
        }
        this.apiToken = apiToken;
        mapper = Serde.createMapper(this);
        typeDefs = new TypeDefsEndpoint(this);
        roles = new RolesEndpoint(this);
        apiTokens = new ApiTokensEndpoint(this);
        groups = new GroupsEndpoint(this);
        users = new UsersEndpoint(this);
        workflows = new WorkflowsEndpoint(this);
        queryParser = new QueryParserEndpoint(this);
        queries = new QueriesEndpoint(this);
        playbooks = new PlaybooksEndpoint(this);
        logs = new LogsEndpoint(this);
        images = new ImagesEndpoint(this);
        files = new FilesEndpoint(this);
        assets = new AssetEndpoint(this);
        requests = new RequestsEndpoint(this);
        impersonate = new ImpersonationEndpoint(this);
        searchLog = new SearchLogEndpoint(this);
        credentials = new CredentialsEndpoint(this);
        tasks = new TaskEndpoint(this);
        sso = new SSOEndpoint(this);
        openLineage = new OpenLineageEndpoint(this);
        contracts = new ContractsEndpoint(this);
        atlanTagCache = new AtlanTagCache(this);
        customMetadataCache = new CustomMetadataCache(this);
        enumCache = new EnumCache(typeDefs);
        groupCache = new GroupCache(this);
        roleCache = new RoleCache(this);
        userCache = new UserCache(this);
        connectionCache = new ConnectionCache(this);
        sourceTagCache = new SourceTagCache(this);
        assetDeserializer = new AssetDeserializer(this);
        relationshipAttributesDeserializer = new RelationshipAttributesDeserializer(this);
        customMetadataAuditDeserializer = new CustomMetadataAuditDeserializer(this);
        atlanTagDeserializer = new AtlanTagDeserializer(this);
    }

    /**
     * Deserialize a string value into an object.
     * @param value the value to deserialize
     * @param clazz the expected object type of the deserialization
     * @return the deserialized object
     * @param <T> type of the deserialized object
     * @throws IOException on any errors doing the deserialization
     */
    public <T> T readValue(String value, Class<T> clazz) throws IOException {
        return mapper.readValue(value, clazz);
    }

    /**
     * Deserialize a byte-array value into an object.
     * @param value the value to deserialize
     * @param clazz the expected object type of the deserialization
     * @return the deserialized object
     * @param <T> type of the deserialized object
     * @throws IOException on any errors doing the deserialization
     */
    public <T> T readValue(byte[] value, Class<T> clazz) throws IOException {
        return mapper.readValue(value, clazz);
    }

    /**
     * Deserialize a string value into an object.
     * @param value the value to deserialize
     * @param typeRef the expected object type of the deserialization
     * @return the deserialized object
     * @param <T> type of the deserialized object
     * @throws IOException on any errors doing the deserialization
     */
    public <T> T readValue(String value, TypeReference<T> typeRef) throws IOException {
        return mapper.readValue(value, typeRef);
    }

    /**
     * Deserialize a string value into an object.
     * @param value the value to deserialize
     * @param typeRef the expected object type of the deserialization
     * @return the deserialized object
     * @param <T> type of the deserialized object
     * @throws IOException on any errors doing the deserialization
     */
    public <T> T readValue(byte[] value, TypeReference<T> typeRef) throws IOException {
        return mapper.readValue(value, typeRef);
    }

    /**
     * Converts from a JSON representation into an object.
     * @param value the JSON representation
     * @param typeReference the expected object type of the deserialization
     * @return the deserialized object
     * @param <T> type of the deserialized object
     * @throws IllegalArgumentException if conversion fails
     */
    public <T> T convertValue(JsonNode value, TypeReference<T> typeReference) throws IllegalArgumentException {
        return mapper.convertValue(value, typeReference);
    }

    /**
     * Converts from a JSON representation into an object.
     * @param value the JSON representation
     * @param clazz the expected object type of the deserialization
     * @return the deserialized object
     * @param <T> type of the deserialized object
     * @throws IllegalArgumentException if conversion fails
     */
    public <T> T convertValue(JsonNode value, Class<T> clazz) throws IllegalArgumentException {
        return mapper.convertValue(value, clazz);
    }

    /**
     * Serialize an object into a JSON string.
     * @param value the object to serialize
     * @return a string giving the JSON representing the object
     * @param <T> type of the object
     * @throws IOException on any errors doing the serialization
     */
    public <T> String writeValueAsString(T value) throws IOException {
        return mapper.writeValueAsString(value);
    }

    /**
     * Serialize an object into a JSON byte-array.
     * @param value the object to serialize
     * @return a byte-array giving the JSON representing the object
     * @param <T> type of the object
     * @throws IOException on any errors doing the serialization
     */
    public <T> byte[] writeValueAsBytes(T value) throws IOException {
        return mapper.writeValueAsBytes(value);
    }

    /**
     * Serialize an object through the provided {@link JsonGenerator}.
     * @param g JSON generator through which to serialize the object
     * @param value object to serialize
     * @throws IOException on any errors doing the serialization
     */
    public void writeValue(JsonGenerator g, Object value) throws IOException {
        mapper.writeValue(g, value);
    }

    /**
     * Indicates whether the SDK is configured for cluster-internal access (true) or external access (false).
     * @return boolean indicating whether the SDK is configured for cluster-internal access (true) or not (false)
     */
    public boolean isInternal() {
        return internalAccess;
    }

    /**
     * Indicates whether the SDK is configured to run against a locally-running development instance (true) or not (false).
     * @return boolean indicating whether the SDK is configured for locally-running development instance (true) or not (false)
     */
    public boolean isLocal() {
        return localAccess;
    }

    /** Retrieve the base URL for the tenant of Atlan configured in this client. */
    public String getBaseUrl() {
        return apiBase;
    }

    /**
     * Sets information about your application. The information is passed along to Atlan.
     *
     * @param name Name of your application (e.g. "MyAwesomeApp")
     */
    public void setAppInfo(String name) {
        setAppInfo(name, null);
    }

    /**
     * Sets information about your application. The information is passed along to Atlan.
     *
     * @param name Name of your application (e.g. "MyAwesomeApp")
     * @param version Version of your application (e.g. "1.2.34")
     */
    public void setAppInfo(String name, String version) {
        setAppInfo(name, version, null);
    }

    /**
     * Sets information about your application. The information is passed along to Atlan.
     *
     * @param name Name of your application (e.g. "MyAwesomeApp")
     * @param version Version of your application (e.g. "1.2.34")
     * @param url Website for your application (e.g. "https://myawesomeapp.info")
     */
    public void setAppInfo(String name, String version, String url) {
        setAppInfo(name, version, url, null);
    }

    /**
     * Sets information about your application. The information is passed along to Atlan.
     *
     * @param name Name of your application (e.g. "MyAwesomeApp")
     * @param version Version of your application (e.g. "1.2.34")
     * @param url Website for your application (e.g. "https://myawesomeapp.info")
     * @param partnerId Your Atlan Partner ID (e.g. "pp_partner_1234")
     */
    public void setAppInfo(String name, String version, String url, String partnerId) {
        if (appInfo == null) {
            appInfo = new HashMap<>();
        }
        appInfo.put("name", name);
        appInfo.put("version", version);
        appInfo.put("url", url);
        appInfo.put("partner_id", partnerId);
    }

    /** {@inheritDoc} */
    @Override
    public void close() {
        AtlanCloseable.close(atlanTagCache);
        AtlanCloseable.close(customMetadataCache);
        AtlanCloseable.close(userCache);
        AtlanCloseable.close(groupCache);
        AtlanCloseable.close(roleCache);
    }
}
