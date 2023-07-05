/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.net;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import com.atlan.Atlan;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import lombok.EqualsAndHashCode;

/**
 * Class to encapsulate all the options that can be overridden on individual API calls.
 * For the moment, only {@link #getDefault()} is used (behind-the-scenes), but this would provide the foundation
 * to open up per-request variations of things like the maximum number of retries to allow.
 */
@EqualsAndHashCode(callSuper = false)
public class RequestOptions {
    private final int connectTimeout;
    private final int readTimeout;

    private final int maxNetworkRetries;
    private final Proxy connectionProxy;
    private final PasswordAuthentication proxyCredential;

    /**
     * Returns a default set of request options, using the global settings of the SDK.
     *
     * @return default request options
     */
    public static RequestOptions getDefault() {
        return new RequestOptions(
                Atlan.DEFAULT_CONNECT_TIMEOUT, Atlan.DEFAULT_READ_TIMEOUT, Atlan.DEFAULT_NETWORK_RETRIES, null, null);
    }

    private RequestOptions(
            int connectTimeout,
            int readTimeout,
            int maxNetworkRetries,
            Proxy connectionProxy,
            PasswordAuthentication proxyCredential) {
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.maxNetworkRetries = maxNetworkRetries;
        this.connectionProxy = connectionProxy;
        this.proxyCredential = proxyCredential;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getMaxNetworkRetries() {
        return maxNetworkRetries;
    }

    public Proxy getConnectionProxy() {
        return connectionProxy;
    }

    public PasswordAuthentication getProxyCredential() {
        return proxyCredential;
    }

    public static RequestOptionsBuilder builder() {
        return new RequestOptionsBuilder();
    }

    public static final class RequestOptionsBuilder {
        private int connectTimeout;
        private int readTimeout;
        private int maxNetworkRetries;
        private Proxy connectionProxy;
        private PasswordAuthentication proxyCredential;

        /**
         * Constructs a request options builder with the global parameters (API key and client ID) as
         * default values.
         */
        public RequestOptionsBuilder() {
            this.connectTimeout = Atlan.DEFAULT_CONNECT_TIMEOUT;
            this.readTimeout = Atlan.DEFAULT_READ_TIMEOUT;
            this.maxNetworkRetries = Atlan.DEFAULT_NETWORK_RETRIES;
            this.connectionProxy = null;
            this.proxyCredential = null;
        }

        public int getConnectTimeout() {
            return connectTimeout;
        }

        /**
         * Sets the timeout value that will be used for making new connections to the Atlan API (in
         * milliseconds).
         *
         * @param timeout timeout value in milliseconds
         */
        public RequestOptionsBuilder setConnectTimeout(int timeout) {
            this.connectTimeout = timeout;
            return this;
        }

        public int getReadTimeout() {
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
        public RequestOptionsBuilder setReadTimeout(int timeout) {
            this.readTimeout = timeout;
            return this;
        }

        public int getMaxNetworkRetries() {
            return maxNetworkRetries;
        }

        /**
         * Sets the maximum number of times the request will be retried in the event of a failure.
         *
         * @param maxNetworkRetries the number of times to retry the request
         */
        public RequestOptionsBuilder setMaxNetworkRetries(int maxNetworkRetries) {
            this.maxNetworkRetries = maxNetworkRetries;
            return this;
        }

        public Proxy getConnectionProxy() {
            return connectionProxy;
        }

        public RequestOptionsBuilder setConnectionProxy(Proxy connectionProxy) {
            this.connectionProxy = connectionProxy;
            return this;
        }

        public PasswordAuthentication getProxyCredential() {
            return proxyCredential;
        }

        public RequestOptionsBuilder setProxyCredential(PasswordAuthentication proxyCredential) {
            this.proxyCredential = proxyCredential;
            return this;
        }

        /** Constructs a {@link RequestOptions} with the specified values. */
        public RequestOptions build() {
            return new RequestOptions(connectTimeout, readTimeout, maxNetworkRetries, connectionProxy, proxyCredential);
        }
    }
}
