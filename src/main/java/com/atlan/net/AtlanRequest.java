/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.net;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import com.atlan.AtlanClient;
import com.atlan.exception.ApiConnectionException;
import com.atlan.exception.AtlanException;
import com.atlan.exception.AuthenticationException;
import com.atlan.exception.ErrorCode;
import com.atlan.util.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Accessors;

/** A request to Atlan's API. */
@Value
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Accessors(fluent = true)
public class AtlanRequest {
    /** Connectivity to the Atlan tenant. */
    AtlanClient client;

    /** The HTTP method for the request (GET, POST or DELETE). */
    ApiResource.RequestMethod method;

    /**
     * The URL for the request. If this is a GET or DELETE request, the URL also includes the request
     * parameters in its query string.
     */
    URL url;

    /**
     * The body of the request. For POST requests, this will be either a {@code application/json}
     * payload or a multi-part form upload (for files). For GET requests, this will be {@code null}.
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

    /** Unique identifier (GUID) of this request. */
    String requestId;

    /**
     * Initializes a new instance of the {@link AtlanRequest} class, used for the majority of requests.
     *
     * @param client connectivity to an Atlan tenant
     * @param method the HTTP method
     * @param url the URL of the request
     * @param body the body of the request
     * @param options the special modifiers of the request
     * @param requestId unique identifier (GUID) of a single request to Atlan
     * @throws AtlanException if the request cannot be initialized for any reason
     */
    public AtlanRequest(
            AtlanClient client,
            ApiResource.RequestMethod method,
            String url,
            String body,
            RequestOptions options,
            String requestId)
            throws AtlanException {
        try {
            this.client = client;
            this.body = body;
            this.options = (options != null) ? options : RequestOptions.getDefault();
            this.requestId = requestId;
            this.method = method;
            this.url = new URL(url);
            this.content = (body == null || body.length() == 0) ? null : HttpContent.buildJSONEncodedContent(body);
            this.headers = buildHeaders(true);
        } catch (IOException e) {
            throw new ApiConnectionException(ErrorCode.CONNECTION_ERROR, e, client.getBaseUrl());
        }
    }

    /**
     * Initializes a new instance of the {@link AtlanRequest} class, used specifically for uploading files (images).
     *
     * @param client connectivity to an Atlan tenant
     * @param method the HTTP method
     * @param url the URL of the request
     * @param file the file to be uploaded through the request
     * @param filename name of the file the InputStream is reading
     * @param options the special modifiers of the request
     * @param requestId unique identifier (GUID) of a single request to Atlan
     * @throws AtlanException if the request cannot be initialized for any reason
     */
    public AtlanRequest(
            AtlanClient client,
            ApiResource.RequestMethod method,
            String url,
            InputStream file,
            String filename,
            RequestOptions options,
            String requestId)
            throws AtlanException {
        try {
            this.client = client;
            this.body = null;
            this.options = (options != null) ? options : RequestOptions.getDefault();
            this.requestId = requestId;
            this.method = method;
            this.url = new URL(url);
            this.content =
                    HttpContent.buildMultipartFormDataContent(List.of(new KeyValuePair<>("file", file)), filename);
            this.headers = buildHeaders(true);
        } catch (IOException e) {
            throw new ApiConnectionException(ErrorCode.CONNECTION_ERROR, e, client.getBaseUrl());
        }
    }

    /**
     * Initializes a new instance of the {@link AtlanRequest} class, used specifically for exchanging form-urlencoded
     * content.
     *
     * @param client connectivity to an Atlan tenant
     * @param method the HTTP method
     * @param url the URL of the request
     * @param map of key-value pairs to be form-urlencoded
     * @param options the special modifiers of the request
     * @param requestId unique identifier (GUID) of a single request to Atlan
     * @throws AtlanException if the request cannot be initialized for any reason
     */
    public AtlanRequest(
            AtlanClient client,
            ApiResource.RequestMethod method,
            String url,
            Map<String, Object> map,
            RequestOptions options,
            String requestId)
            throws AtlanException {
        try {
            this.client = client;
            this.body = null;
            this.options = (options != null) ? options : RequestOptions.getDefault();
            this.requestId = requestId;
            this.method = method;
            this.url = new URL(url);
            this.content = FormEncoder.createHttpContent(map);
            this.headers = buildHeaders(false);
        } catch (IOException e) {
            throw new ApiConnectionException(ErrorCode.CONNECTION_ERROR, e, client.getBaseUrl());
        }
    }

    private HttpHeaders buildHeaders(boolean checkApiToken) throws AuthenticationException {
        Map<String, List<String>> headerMap = new HashMap<>();

        // Accept
        headerMap.put("Accept", Arrays.asList("application/json"));

        // Accept-Charset
        headerMap.put("Accept-Charset", Arrays.asList(ApiResource.CHARSET.name()));

        // Authorization
        String apiToken = client.getApiToken();
        if (checkApiToken) {
            if (apiToken == null) {
                throw new AuthenticationException(ErrorCode.NO_API_TOKEN);
            } else if (apiToken.isEmpty()) {
                throw new AuthenticationException(ErrorCode.EMPTY_API_TOKEN);
            } else if (StringUtils.containsWhitespace(apiToken)) {
                throw new AuthenticationException(ErrorCode.INVALID_API_TOKEN);
            }
        }
        headerMap.put("Authorization", Arrays.asList(String.format("Bearer %s", apiToken)));

        headerMap.put("X-Atlan-Request-Id", List.of(requestId));

        return HttpHeaders.of(headerMap);
    }
}
