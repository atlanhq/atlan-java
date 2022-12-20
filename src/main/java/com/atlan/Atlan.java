/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import com.atlan.exception.ApiConnectionException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Primary point of configuration for the SDK. In particular, use these methods to set up the SDK to run in your
 * environment:
 *
 * - {@link #setBaseUrl(String)} to define your Atlan URL ({@code https://tenant.atlan.com})
 * - {@link #setApiToken(String)} to define the Atlan API token the SDK should use
 * - {@link #setMaxNetworkRetries(int)} to set the maximum number of retries the SDK should attempt when it hits a problem (default: 10)
 */
public abstract class Atlan {
    public static final int DEFAULT_CONNECT_TIMEOUT = 30 * 1000;
    public static final int DEFAULT_READ_TIMEOUT = 80 * 1000;

    public static final String VERSION = "0.1.0-SNAPSHOT";

    public static volatile String clientId;
    public static volatile boolean enableTelemetry = true;
    public static volatile String partnerId;

    // Note that URLConnection reserves the value of 0 to mean "infinite
    // timeout", so we use -1 here to represent an unset value which should
    // fall back to a default.
    private static volatile int connectTimeout = -1;
    private static volatile int readTimeout = -1;

    private static volatile int maxNetworkRetries = 10;

    private static volatile String apiToken = null;
    private static volatile String apiBase = null;
    private static volatile Proxy connectionProxy = null;
    private static volatile PasswordAuthentication proxyCredential = null;

    private static volatile Map<String, String> appInfo = null;

    /** Set the base URL for your tenant of Atlan. */
    public static void setBaseUrl(final String baseURL) {
        apiBase = baseURL;
    }

    /**
     * Retrieve the base URL for your tenant of Atlan.
     * @throws ApiConnectionException if no base URL has been defined
     */
    public static String getBaseUrl() throws ApiConnectionException {
        if (apiBase == null) {
            throw new ApiConnectionException(
                    "No base URL is configured. You must first use Atlan.setBaseUrl() before making an API call.");
        } else {
            return apiBase;
        }
    }

    /** Retrieve the base URL for your tenant of Atlan. */
    public static String getBaseUrlSafe() {
        return apiBase;
    }

    /** Set the API token to use for authenticating API calls. */
    public static void setApiToken(final String token) {
        apiToken = token;
    }

    /** Retrieve the API token to use for authenticating API calls. */
    public static String getApiToken() {
        return apiToken;
    }

    /**
     * Set proxy to tunnel all Atlan connections.
     *
     * @param proxy proxy host and port setting
     */
    public static void setConnectionProxy(final Proxy proxy) {
        connectionProxy = proxy;
    }

    /**
     * Returns the proxy to tunnel all Atlan connections.
     *
     * @return proxy
     */
    public static Proxy getConnectionProxy() {
        return connectionProxy;
    }

    /**
     * Returns the connection timeout.
     *
     * @return timeout value in milliseconds
     */
    public static int getConnectTimeout() {
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
    public static void setConnectTimeout(final int timeout) {
        connectTimeout = timeout;
    }

    /**
     * Returns the read timeout.
     *
     * @return timeout value in milliseconds
     */
    public static int getReadTimeout() {
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
    public static void setReadTimeout(final int timeout) {
        readTimeout = timeout;
    }

    /**
     * Returns the maximum number of times requests will be retried.
     *
     * @return the maximum number of times requests will be retried
     */
    public static int getMaxNetworkRetries() {
        return maxNetworkRetries;
    }

    /**
     * Sets the maximum number of times requests will be retried.
     *
     * @param numRetries the maximum number of times requests will be retried
     */
    public static void setMaxNetworkRetries(final int numRetries) {
        maxNetworkRetries = numRetries;
    }

    /**
     * Provide credential for proxy authorization if required.
     *
     * @param auth proxy required userName and password
     */
    public static void setProxyCredential(final PasswordAuthentication auth) {
        proxyCredential = auth;
    }

    /**
     * Returns the credential to use for proxy authorization if required.
     *
     * @return the credential to use for proxy authorization
     */
    public static PasswordAuthentication getProxyCredential() {
        return proxyCredential;
    }

    /**
     * Sets information about your application. The information is passed along to Atlan.
     *
     * @param name Name of your application (e.g. "MyAwesomeApp")
     */
    public static void setAppInfo(String name) {
        setAppInfo(name, null, null, null);
    }

    /**
     * Sets information about your application. The information is passed along to Atlan.
     *
     * @param name Name of your application (e.g. "MyAwesomeApp")
     * @param version Version of your application (e.g. "1.2.34")
     */
    public static void setAppInfo(String name, String version) {
        setAppInfo(name, version, null, null);
    }

    /**
     * Sets information about your application. The information is passed along to Atlan.
     *
     * @param name Name of your application (e.g. "MyAwesomeApp")
     * @param version Version of your application (e.g. "1.2.34")
     * @param url Website for your application (e.g. "https://myawesomeapp.info")
     */
    public static void setAppInfo(String name, String version, String url) {
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
    public static void setAppInfo(String name, String version, String url, String partnerId) {
        if (appInfo == null) {
            appInfo = new HashMap<String, String>();
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
    public static Map<String, String> getAppInfo() {
        return appInfo;
    }
}
