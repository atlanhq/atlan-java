/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import com.atlan.api.*;
import com.atlan.cache.*;
import com.atlan.serde.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import java.io.IOException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration for the SDK against a particular Atlan tenant.
 */
public class AtlanClient {
    public static final int DEFAULT_CONNECT_TIMEOUT = 30 * 1000;
    public static final int DEFAULT_READ_TIMEOUT = 120 * 1000;
    public static final String DELETED_AUDIT_OBJECT = "(DELETED)";

    // Note that URLConnection reserves the value of 0 to mean "infinite
    // timeout", so we use -1 here to represent an unset value which should
    // fall back to a default.
    private volatile int connectTimeout = -1;
    private volatile int readTimeout = -1;

    private volatile int maxNetworkRetries = 10;

    private final String apiBase;
    private final boolean internalAccess;
    private volatile String apiToken;
    private volatile Proxy connectionProxy = null;
    private volatile PasswordAuthentication proxyCredential = null;

    private volatile Map<String, String> appInfo = null;

    private final AtlanTagCache atlanTagCache;
    private final CustomMetadataCache customMetadataCache;
    private final EnumCache enumCache;
    private final GroupCache groupCache;
    private final RoleCache roleCache;
    private final UserCache userCache;

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

    /** Endpoint with operations to manage playbooks. */
    public final PlaybooksEndpoint playbooks;

    /** Endpoint with operations to manage logs. */
    public final LogsEndpoint logs;

    /** Endpoint with operations to manage images. */
    public final ImagesEndpoint images;

    /** Endpoint with operations to manage assets. */
    public final AssetEndpoint assets;

    /** Endpoint with operations to manage requests. */
    public final RequestsEndpoint requests;

    /**
     * Instantiate a new client — this should only be called by the Atlan factory, hence package-private.
     * @param baseURL of the tenant
     */
    AtlanClient(final String baseURL) {
        if (baseURL.equals("INTERNAL")) {
            apiBase = null;
            internalAccess = true;
        } else {
            internalAccess = false;
            if (baseURL.endsWith("/")) {
                apiBase = baseURL.substring(0, baseURL.lastIndexOf("/"));
            } else {
                apiBase = baseURL;
            }
        }
        mapper = Serde.createMapper(this);
        typeDefs = new TypeDefsEndpoint(this);
        roles = new RolesEndpoint(this);
        apiTokens = new ApiTokensEndpoint(this);
        groups = new GroupsEndpoint(this);
        users = new UsersEndpoint(this);
        workflows = new WorkflowsEndpoint(this);
        queryParser = new QueryParserEndpoint(this);
        playbooks = new PlaybooksEndpoint(this);
        logs = new LogsEndpoint(this);
        images = new ImagesEndpoint(this);
        assets = new AssetEndpoint(this);
        requests = new RequestsEndpoint(this);
        atlanTagCache = new AtlanTagCache(typeDefs);
        customMetadataCache = new CustomMetadataCache(typeDefs);
        enumCache = new EnumCache(typeDefs);
        groupCache = new GroupCache();
        roleCache = new RoleCache(roles);
        userCache = new UserCache();
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
     * Serialize an object through the provided {@link JsonGenerator}.
     * @param g JSON generator through which to serialize the object
     * @param value object to serialize
     * @throws IOException on any errors doing the serialization
     */
    public void writeValue(JsonGenerator g, Object value) throws IOException {
        mapper.writeValue(g, value);
    }

    /**
     * Retrieve the cache of Atlan tags specific to this client.
     * @return the cache of Atlan tags specific to this client.
     */
    public AtlanTagCache getAtlanTagCache() {
        return atlanTagCache;
    }

    /**
     * Retrieve the cache of custom metadata structures specific to this client.
     * @return the cache of custom metadata structures specific to this client.
     */
    public CustomMetadataCache getCustomMetadataCache() {
        return customMetadataCache;
    }

    /**
     * Retrieve the cache of enums specific to this client.
     * @return the cache of enums specific to this client.
     */
    public EnumCache getEnumCache() {
        return enumCache;
    }

    /**
     * Retrieve the cache of groups specific to this client.
     * @return the cache of groups specific to this client.
     */
    public GroupCache getGroupCache() {
        return groupCache;
    }

    /**
     * Retrieve the cache of workspace roles specific to this client.
     * @return the cache of workspace roles specific to this client.
     */
    public RoleCache getRoleCache() {
        return roleCache;
    }

    /**
     * Retrieve the cache of users specific to this client.
     * @return the cache of users specific to this client.
     */
    public UserCache getUserCache() {
        return userCache;
    }

    /**
     * Indicates whether the SDK is configured for cluster-internal access (true) or external access (false).
     * @return boolean indicating whether the SDK is configured for cluster-internal access (true) or not (false)
     */
    public boolean isInternal() {
        return internalAccess;
    }

    /** Retrieve the base URL for the tenant of Atlan configured in this client. */
    public String getBaseUrl() {
        return apiBase;
    }

    /** Set the API token to use for authenticating API calls. */
    public void setApiToken(final String token) {
        apiToken = token;
    }

    /** Retrieve the API token to use for authenticating API calls. */
    public String getApiToken() {
        return apiToken;
    }

    /**
     * Set proxy to tunnel all Atlan connections.
     *
     * @param proxy proxy host and port setting
     */
    public void setConnectionProxy(final Proxy proxy) {
        connectionProxy = proxy;
    }

    /**
     * Returns the proxy to tunnel all Atlan connections.
     *
     * @return proxy
     */
    public Proxy getConnectionProxy() {
        return connectionProxy;
    }

    /**
     * Returns the connection timeout.
     *
     * @return timeout value in milliseconds
     */
    public int getConnectTimeout() {
        if (connectTimeout == -1) {
            return DEFAULT_CONNECT_TIMEOUT;
        }
        return connectTimeout;
    }

    /**
     * Sets the timeout value that will be used for making new connections to the Atlan API (in
     * milliseconds).
     *
     * @param timeout timeout value in milliseconds
     */
    public void setConnectTimeout(final int timeout) {
        connectTimeout = timeout;
    }

    /**
     * Returns the read timeout.
     *
     * @return timeout value in milliseconds
     */
    public int getReadTimeout() {
        if (readTimeout == -1) {
            return DEFAULT_READ_TIMEOUT;
        }
        return readTimeout;
    }

    /**
     * Sets the timeout value that will be used when reading data from an established connection to
     * the Atlan API (in milliseconds).
     *
     * <p>Note that this value should be set conservatively because some API requests can take time
     * and a short timeout increases the likelihood of causing a problem in the backend.
     *
     * @param timeout timeout value in milliseconds
     */
    public void setReadTimeout(final int timeout) {
        readTimeout = timeout;
    }

    /**
     * Returns the maximum number of times requests will be retried.
     *
     * @return the maximum number of times requests will be retried
     */
    public int getMaxNetworkRetries() {
        return maxNetworkRetries;
    }

    /**
     * Sets the maximum number of times requests will be retried.
     *
     * @param numRetries the maximum number of times requests will be retried
     */
    public void setMaxNetworkRetries(final int numRetries) {
        maxNetworkRetries = numRetries;
    }

    /**
     * Provide credential for proxy authorization if required.
     *
     * @param auth proxy required userName and password
     */
    public void setProxyCredential(final PasswordAuthentication auth) {
        proxyCredential = auth;
    }

    /**
     * Returns the credential to use for proxy authorization if required.
     *
     * @return the credential to use for proxy authorization
     */
    public PasswordAuthentication getProxyCredential() {
        return proxyCredential;
    }

    /**
     * Sets information about your application. The information is passed along to Atlan.
     *
     * @param name Name of your application (e.g. "MyAwesomeApp")
     */
    public void setAppInfo(String name) {
        setAppInfo(name, null, null, null);
    }

    /**
     * Sets information about your application. The information is passed along to Atlan.
     *
     * @param name Name of your application (e.g. "MyAwesomeApp")
     * @param version Version of your application (e.g. "1.2.34")
     */
    public void setAppInfo(String name, String version) {
        setAppInfo(name, version, null, null);
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

    /**
     * Returns information about your application.
     *
     * @return information about your application
     */
    public Map<String, String> getAppInfo() {
        return appInfo;
    }
}
