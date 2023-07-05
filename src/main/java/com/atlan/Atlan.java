/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import com.atlan.exception.ErrorCode;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Primary point of configuration for the SDK to one or more tenants.
 */
public abstract class Atlan {
    public static final int DEFAULT_CONNECT_TIMEOUT = 30 * 1000;
    public static final int DEFAULT_READ_TIMEOUT = 120 * 1000;
    public static final int DEFAULT_NETWORK_RETRIES = 10;

    public static final String VERSION = "0.8.2-SNAPSHOT";

    public static volatile boolean enableTelemetry = true;

    private static final Map<String, AtlanClient> clientCache = new ConcurrentHashMap<>();

    private static AtlanClient defaultClient = null;

    /**
     * Retrieve the Atlan client configured for the provided tenant URL.
     *
     * @param baseURL base URL of the tenant for which to configure a client
     */
    public static AtlanClient getInstance(final String baseURL) {
        if (baseURL == null) {
            throw new IllegalStateException(
                    ErrorCode.NO_BASE_URL.getMessageDefinition().getErrorMessage());
        }
        String prepped = baseURL;
        if (baseURL.endsWith("/")) {
            prepped = baseURL.substring(0, baseURL.lastIndexOf("/"));
        }
        if (!clientCache.containsKey(prepped)) {
            clientCache.put(baseURL, new AtlanClient(prepped));
        }
        return clientCache.get(baseURL);
    }

    /**
     * Set the base URL for the default client. This must be called BEFORE setApiToken.
     *
     * @param baseURL base URL of the tenant to use as the default client
     * @see #setApiToken(String)
     */
    public static void setBaseUrl(final String baseURL) {
        defaultClient = getInstance(baseURL);
    }

    /**
     * Set the API token for the default client. This can only be called AFTER setBaseUrl.
     *
     * @param apiToken for the default client
     * @see #setBaseUrl(String)
     */
    public static void setApiToken(final String apiToken) {
        if (defaultClient == null) {
            throw new IllegalStateException(
                    ErrorCode.NO_BASE_URL.getMessageDefinition().getErrorMessage());
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
            throw new IllegalStateException(
                    ErrorCode.NO_BASE_URL.getMessageDefinition().getErrorMessage());
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
