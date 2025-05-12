/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import com.atlan.exception.ErrorCode;
import java.util.List;
import java.util.Map;

/**
 * Primary point of configuration for the SDK to one or more tenants.
 */
public abstract class Atlan {
    public static final int DEFAULT_CONNECT_TIMEOUT = 30 * 1000;
    public static final int DEFAULT_READ_TIMEOUT = 120 * 1000;
    public static final int DEFAULT_NETWORK_RETRIES = 5;

    public static final String VERSION = BuildInfo.VERSION;

    // Note: these are set here so that they can be overridden,
    // i.e. when using the SDK in a workflow setting
    public static final Map<String, List<String>> EXTRA_HEADERS =
            Map.ofEntries(Map.entry("x-atlan-agent", List.of("sdk")), Map.entry("x-atlan-agent-id", List.of("java")));

    public static volatile boolean enableTelemetry = true;

    static final String INVALID_CLIENT_MSG =
            ErrorCode.NO_BASE_URL.getMessageDefinition().getErrorId() + " "
                    + ErrorCode.NO_BASE_URL.getMessageDefinition().getErrorMessage() + " "
                    + ErrorCode.NO_BASE_URL.getMessageDefinition().getUserAction();

    static final String BLANK_CLIENT_MSG =
            ErrorCode.BLANK_BASE_URL.getMessageDefinition().getErrorId() + " "
                    + ErrorCode.BLANK_BASE_URL.getMessageDefinition().getErrorMessage() + " "
                    + ErrorCode.BLANK_BASE_URL.getMessageDefinition().getUserAction();

    static String prepURL(final String baseURL) {
        String prepped = baseURL;
        if (baseURL.endsWith("/")) {
            prepped = baseURL.substring(0, baseURL.lastIndexOf("/"));
        }
        return prepped;
    }
}
