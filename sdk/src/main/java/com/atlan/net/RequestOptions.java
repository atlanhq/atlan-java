/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.net;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
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

    @Builder.Default
    private final boolean skipLogging = false;

    @Singular
    private final Map<String, List<String>> extraHeaders;

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

    public static class RequestOptionsBuilder {}
}
