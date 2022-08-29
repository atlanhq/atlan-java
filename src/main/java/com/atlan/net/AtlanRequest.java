/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.net;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import com.atlan.Atlan;
import com.atlan.exception.ApiConnectionException;
import com.atlan.exception.AtlanException;
import com.atlan.exception.AuthenticationException;
import com.atlan.util.StringUtils;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Accessors;

/** A request to Atlan's API. */
@Value
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class AtlanRequest {
    /** The HTTP method for the request (GET, POST or DELETE). */
    ApiResource.RequestMethod method;

    /**
     * The URL for the request. If this is a GET or DELETE request, the URL also includes the request
     * parameters in its query string.
     */
    URL url;

    /**
     * The body of the request. For POST requests, this will be either a {@code application/json}
     * payload. For GET requests, this will be {@code null}.
     */
    HttpContent content;

    /**
     * The HTTP headers of the request ({@code Authorization}, {@code Atlan-Version}, {@code
     * Atlan-Account}, {@code Idempotency-Key}...).
     */
    HttpHeaders headers;

    /** The body of the request (as an unmodifiable JSON string). */
    String body;

    /** The special modifiers of the request. */
    RequestOptions options;

    /**
     * Initializes a new instance of the {@link AtlanRequest} class.
     *
     * @param method the HTTP method
     * @param url the URL of the request
     * @param body the body of the request
     * @param options the special modifiers of the request
     * @throws AtlanException if the request cannot be initialized for any reason
     */
    public AtlanRequest(ApiResource.RequestMethod method, String url, String body, RequestOptions options)
            throws AtlanException {
        try {
            this.body = body;
            this.options = (options != null) ? options : RequestOptions.getDefault();
            this.method = method;
            this.url = new URL(url);
            this.content = (body == null || body.length() == 0) ? null : HttpContent.buildJSONEncodedContent(body);
            this.headers = buildHeaders(method, this.options);
        } catch (IOException e) {
            throw new ApiConnectionException(
                    String.format(
                            "IOException during API request to Atlan (%s): %s "
                                    + "Please check your internet connection and try again. If this problem persists,"
                                    + "you should check Atlan's availability via a browser,"
                                    + " or let us know at support@atlan.com.",
                            Atlan.getBaseUrl(), e.getMessage()),
                    e);
        }
    }

    /**
     * Returns a new {@link AtlanRequest} instance with an additional header.
     *
     * @param name the additional header's name
     * @param value the additional header's value
     * @return the new {@link AtlanRequest} instance
     */
    public AtlanRequest withAdditionalHeader(String name, String value) {
        return new AtlanRequest(
                this.method,
                this.url,
                this.content,
                this.headers.withAdditionalHeader(name, value),
                this.body,
                this.options);
    }

    private static HttpHeaders buildHeaders(ApiResource.RequestMethod method, RequestOptions options)
            throws AuthenticationException {
        Map<String, List<String>> headerMap = new HashMap<String, List<String>>();

        // Accept
        headerMap.put("Accept", Arrays.asList("application/json"));

        // Accept-Charset
        headerMap.put("Accept-Charset", Arrays.asList(ApiResource.CHARSET.name()));

        // Authorization
        String apiKey = options.getApiKey();
        if (apiKey == null) {
            throw new AuthenticationException(
                    "No API key provided. Set your API key using `Atlan.apiKey = \"<API-KEY>\"`. You can "
                            + "generate API keys from the Atlan Dashboard. See "
                            + "https://atlan.com/docs/api/authentication for details or contact support at "
                            + "https://support.atlan.com/email if you have any questions.",
                    null,
                    null,
                    0);
        } else if (apiKey.isEmpty()) {
            throw new AuthenticationException(
                    "Your API key is invalid, as it is an empty string. You can double-check your API key "
                            + "from the Atlan Dashboard. See "
                            + "https://atlan.com/docs/api/authentication for details or contact support at "
                            + "https://support.atlan.com/email if you have any questions.",
                    null,
                    null,
                    0);
        } else if (StringUtils.containsWhitespace(apiKey)) {
            throw new AuthenticationException(
                    "Your API key is invalid, as it contains whitespace. You can double-check your API key "
                            + "from the Atlan Dashboard. See "
                            + "https://atlan.com/docs/api/authentication for details or contact support at "
                            + "https://support.atlan.com/email if you have any questions.",
                    null,
                    null,
                    0);
        }
        headerMap.put("Authorization", Arrays.asList(String.format("Bearer %s", apiKey)));

        // Atlan-Version
        if (options.getAtlanVersion() != null) {
            headerMap.put("Atlan-Version", Arrays.asList(options.getAtlanVersion()));
        } else {
            throw new IllegalStateException("`atlanVersion` value must be set.");
        }

        // Atlan-Account
        if (options.getAtlanAccount() != null) {
            headerMap.put("Atlan-Account", Arrays.asList(options.getAtlanAccount()));
        }

        // Idempotency-Key
        if (options.getIdempotencyKey() != null) {
            headerMap.put("Idempotency-Key", Arrays.asList(options.getIdempotencyKey()));
        } else if (method == ApiResource.RequestMethod.POST) {
            headerMap.put("Idempotency-Key", Arrays.asList(UUID.randomUUID().toString()));
        }

        return HttpHeaders.of(headerMap);
    }
}
