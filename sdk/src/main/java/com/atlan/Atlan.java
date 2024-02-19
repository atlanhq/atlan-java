/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import com.atlan.exception.ErrorCode;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Primary point of configuration for the SDK to one or more tenants.
 */
public abstract class Atlan {
    public static final int DEFAULT_CONNECT_TIMEOUT = 30 * 1000;
    public static final int DEFAULT_READ_TIMEOUT = 120 * 1000;
    public static final int DEFAULT_NETWORK_RETRIES = 5;

    public static final String VERSION = "1.10.8";

    // Note: these are set here so that they can be overridden,
    // i.e. when using the SDK in a workflow setting
    public static final Map<String, List<String>> EXTRA_HEADERS =
            Map.ofEntries(Map.entry("x-atlan-agent", List.of("sdk")), Map.entry("x-atlan-agent-id", List.of("java")));

    public static volatile boolean enableTelemetry = true;

    private static final Map<String, AtlanClient> clientCache = new ConcurrentHashMap<>();
    private static final String INVALID_CLIENT_MSG =
            ErrorCode.NO_BASE_URL.getMessageDefinition().getErrorId() + " "
                    + ErrorCode.NO_BASE_URL.getMessageDefinition().getErrorMessage() + " "
                    + ErrorCode.NO_BASE_URL.getMessageDefinition().getUserAction();

    private static AtlanClient defaultClient = null;

    /**
     * Retrieve the Atlan client configured for the provided tenant URL.
     *
     * @param baseURL base URL of the tenant for which to configure a client
     */
    public static AtlanClient getClient(final String baseURL) {
        return getClient(baseURL, null);
    }

    /**
     * Retrieve the Atlan client configured for the provided tenant URL, with a given unique name.
     * Note: generally we would advise having only a single client per tenant URL, but this method
     * is provided for any rare case where you might want to have multiple clients against a single
     * tenant. Note that this increases resource usage:
     * - Information for that tenant will be cached independently in every client (increased heap)
     * - Cache refreshes will be done independently in every client (increased latency for operations)
     *
     * @param baseURL base URL of the tenant for which to configure a client
     * @param name a unique name for this client
     */
    public static AtlanClient getClient(final String baseURL, final String name) {
        if (baseURL == null) {
            throw new IllegalStateException(INVALID_CLIENT_MSG);
        }
        String prepped = prepURL(baseURL);
        String key = getClientId(prepped, name);
        if (!clientCache.containsKey(key)) {
            clientCache.put(key, new AtlanClient(prepped));
        }
        return clientCache.get(key);
    }

    /**
     * Remove a configured Atlan client given the URL and (optional) unique name.
     *
     * @param baseURL base URL of the tenant for which the client is configured
     * @param name (optional) unique name given to the client when it was configured
     */
    public static void removeClient(final String baseURL, final String name) {
        String key = getClientId(prepURL(baseURL), name);
        clientCache.remove(key);
    }

    private static String prepURL(final String baseURL) {
        String prepped = baseURL;
        if (baseURL.endsWith("/")) {
            prepped = baseURL.substring(0, baseURL.lastIndexOf("/"));
        }
        return prepped;
    }

    private static String getClientId(final String baseURL, final String name) {
        String key = baseURL;
        if (name != null && !name.isEmpty()) {
            key = baseURL + "#" + key;
        }
        return key;
    }

    /**
     * Set the base URL for the default client. This must be called BEFORE setApiToken.
     *
     * @param baseURL base URL of the tenant to use as the default client
     * @see #setApiToken(String)
     */
    public static void setBaseUrl(final String baseURL) {
        defaultClient = getClient(baseURL);
    }

    /**
     * Set the API token for the default client. This can only be called AFTER setBaseUrl.
     *
     * @param apiToken for the default client
     * @see #setBaseUrl(String)
     */
    public static void setApiToken(final String apiToken) {
        if (defaultClient == null) {
            throw new IllegalStateException(INVALID_CLIENT_MSG);
        }
        defaultClient.setApiToken(apiToken);
    }

    /**
     * Retrieve the default client that has been configured.
     *
     * @return the default client
     */
    public static AtlanClient getDefaultClient() {
        if (defaultClient == null) {
            throw new IllegalStateException(INVALID_CLIENT_MSG);
        }
        return defaultClient;
    }

    /**
     * Retrieve the base URL for the tenant of Atlan configured in the default client.
     *
     * @return the base URL for the tenant configured in the default client
     */
    public static String getBaseUrl() {
        return getDefaultClient().getBaseUrl();
    }

    /**
     * Returns information about your application configured in the default client.
     *
     * @return information about your application configured in the default client
     */
    public static Map<String, String> getAppInfo() {
        return getDefaultClient().getAppInfo();
    }

    /**
     * Returns the maximum number of times requests will be retried, as configured in the default client.
     *
     * @return the maximum number of times requests will be retried, as configured in the default client
     */
    public static int getMaxNetworkRetries() {
        return getDefaultClient().getMaxNetworkRetries();
    }

    /**
     * Sets the maximum number of times requests will be retried, in the default client.
     *
     * @param numRetries the maximum number of times requests will be retried in the default client
     */
    public static void setMaxNetworkRetries(final int numRetries) {
        getDefaultClient().setMaxNetworkRetries(numRetries);
    }
}
