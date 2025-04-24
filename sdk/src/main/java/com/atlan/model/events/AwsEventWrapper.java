/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.events;

import com.atlan.model.core.AtlanObject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Details that wrap events when sent through AWS (Lambda).
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AwsEventWrapper extends AtlanObject {
    private static final long serialVersionUID = 2L;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public AwsEventWrapper(
            @JsonProperty("version") String version,
            @JsonProperty("routeKey") String routeKey,
            @JsonProperty("rawPath") String rawPath,
            @JsonProperty("rawQueryString") String rawQueryString,
            @JsonProperty("headers") Map<String, String> headers,
            @JsonProperty("requestContext") AwsRequestContext requestContext,
            @JsonProperty("body") String body,
            @JsonProperty("isBase64Encoded") boolean isBase64Encoded) {
        this.version = version;
        this.routeKey = routeKey;
        this.rawPath = rawPath;
        this.rawQueryString = rawQueryString;
        this.headers = headers;
        this.requestContext = requestContext;
        this.body = body;
        this.isBase64Encoded = isBase64Encoded;
    }

    /** TBC */
    final String version;

    /** TBC */
    final String routeKey;

    /** TBC */
    final String rawPath;

    /** TBC */
    final String rawQueryString;

    /** Headers that were used when sending the event through to the Lambda URL. */
    final Map<String, String> headers;

    /** TBC */
    final AwsRequestContext requestContext;

    /** Actual contents of the event that was sent by Atlan. */
    final String body;

    /** Whether the contents are base64-encoded (true) or plain text (false). */
    final boolean isBase64Encoded;

    @Getter
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    @SuppressWarnings("serial")
    public static final class AwsRequestContext extends AtlanObject {
        private static final long serialVersionUID = 2L;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public AwsRequestContext(
                @JsonProperty("accountId") String accountId,
                @JsonProperty("apiId") String apiId,
                @JsonProperty("domainName") String domainName,
                @JsonProperty("domainPrefix") String domainPrefix,
                @JsonProperty("http") Map<String, String> http,
                @JsonProperty("requestId") String requestId,
                @JsonProperty("routeKey") String routeKey,
                @JsonProperty("stage") String stage,
                @JsonProperty("time") String time,
                @JsonProperty("timeEpoch") Long timeEpoch) {
            this.accountId = accountId;
            this.apiId = apiId;
            this.domainName = domainName;
            this.domainPrefix = domainPrefix;
            this.http = http;
            this.requestId = requestId;
            this.routeKey = routeKey;
            this.stage = stage;
            this.time = time;
            this.timeEpoch = timeEpoch;
        }

        /** Account from which the request originated. */
        final String accountId;

        /** TBC */
        final String apiId;

        /** TBC */
        final String domainName;

        /** TBC */
        final String domainPrefix;

        /** TBC */
        final Map<String, String> http;

        /** TBC */
        final String requestId;

        /** TBC */
        final String routeKey;

        /** TBC */
        final String stage;

        /** Time at which the event was received, as a formatted string. */
        final String time;

        /** Time at which the event was received, epoch-based, in milliseconds. */
        final Long timeEpoch;
    }
}
