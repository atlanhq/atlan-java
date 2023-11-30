/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.net;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import com.atlan.Atlan;
import com.atlan.AtlanClient;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

/**
 * Class to encapsulate all the options that can be overridden on individual API calls.
 * For the moment, only {@link #getDefault()} is used (behind-the-scenes), but this would provide the foundation
 * to open up per-request variations of things like the maximum number of retries to allow.
 */
@Builder(toBuilder = true)
@Getter
@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("cast")
public class RequestOptions {
    private final int connectTimeout;
    private final int readTimeout;

    private final int maxNetworkRetries;
    private final Proxy connectionProxy;
    private final PasswordAuthentication proxyCredential;

    @Singular
    private final Map<String, List<String>> extraHeaders;

    /**
     * Returns a default set of request options, using the global settings of the SDK.
     *
     * @return default request options
     */
    public static RequestOptions getDefault() {
        return RequestOptions.builder()
                .connectTimeout(Atlan.DEFAULT_CONNECT_TIMEOUT)
                .readTimeout(Atlan.DEFAULT_READ_TIMEOUT)
                .maxNetworkRetries(Atlan.DEFAULT_NETWORK_RETRIES)
                .extraHeaders(Atlan.EXTRA_HEADERS)
                .build();
    }

    /**
     * Returns a new set of request options, initialized from the values set in the provided client.
     *
     * @param client from which to initialize the options
     * @return a new set of request options
     */
    public static RequestOptionsBuilder from(AtlanClient client) {
        return RequestOptions.builder()
                .connectTimeout(client.getConnectTimeout())
                .readTimeout(client.getReadTimeout())
                .maxNetworkRetries(client.getMaxNetworkRetries())
                .connectionProxy(client.getConnectionProxy())
                .proxyCredential(client.getProxyCredential())
                .extraHeaders(client.getExtraHeaders());
    }
}
