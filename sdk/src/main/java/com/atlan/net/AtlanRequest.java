/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
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
import java.nio.charset.StandardCharsets;
import java.util.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/** A request to Atlan's API. */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
@Accessors(fluent = true)
@SuppressWarnings("serial")
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

    /** Whether the API token needs to be confirmed. */
    boolean checkApiToken;

    /** MIME type of content that should be accepted. */
    String acceptType;

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
        this(client, method, url, body, options, requestId, "application/json");
    }

    /**
     * Initializes a new instance of the {@link AtlanRequest} class, used for the majority of requests.
     *
     * @param client connectivity to an Atlan tenant
     * @param method the HTTP method
     * @param url the URL of the request
     * @param body the body of the request
     * @param options the special modifiers of the request
     * @param requestId unique identifier (GUID) of a single request to Atlan
     * @param acceptType mime-type for the content accepted in the response to this query
     * @throws AtlanException if the request cannot be initialized for any reason
     */
    public AtlanRequest(
            AtlanClient client,
            ApiResource.RequestMethod method,
            String url,
            String body,
            RequestOptions options,
            String requestId,
            String acceptType)
            throws AtlanException {
        try {
            this.client = client;
            this.body = body;
            this.options =
                    (options != null) ? options : RequestOptions.from(client).build();
            this.requestId = requestId;
            this.method = method;
            this.url = new URL(url);
            this.content = (body == null || body.isEmpty()) ? null : HttpContent.buildJSONEncodedContent(body);
            this.checkApiToken = true;
            this.acceptType = acceptType;
            this.headers = buildHeaders(true, options, acceptType);
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
     * @param extras (optional) additional form-encoded parameters to send
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
            Map<String, String> extras,
            RequestOptions options,
            String requestId)
            throws AtlanException {
        try {
            this.client = client;
            this.body = null;
            this.options =
                    (options != null) ? options : RequestOptions.from(client).build();
            this.requestId = requestId;
            this.method = method;
            this.url = new URL(url);
            List<KeyValuePair<String, Object>> parameters = new ArrayList<>();
            parameters.add(new KeyValuePair<>("file", file));
            if (extras != null) {
                for (Map.Entry<String, String> entry : extras.entrySet()) {
                    parameters.add(new KeyValuePair<>(entry.getKey(), entry.getValue()));
                }
            }
            this.content = HttpContent.buildMultipartFormDataContent(parameters, filename);
            this.checkApiToken = true;
            this.acceptType = "application/json";
            this.headers = buildHeaders(true, options, acceptType);
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
            this.options =
                    (options != null) ? options : RequestOptions.from(client).build();
            this.requestId = requestId;
            this.method = method;
            this.url = new URL(url);
            this.content = FormEncoder.createHttpContent(map);
            this.checkApiToken = false;
            this.acceptType = "application/json";
            this.headers = buildHeaders(false, options, acceptType);
        } catch (IOException e) {
            throw new ApiConnectionException(ErrorCode.CONNECTION_ERROR, e, client.getBaseUrl());
        }
    }

    /**
     * Rebuild the headers for the request, i.e. to include any revised token details.
     *
     * @throws AuthenticationException on any issue rebuilding the headers for the request
     */
    public void rebuildHeaders() throws AuthenticationException {
        headers = buildHeaders(checkApiToken, options(), acceptType);
    }

    private HttpHeaders buildHeaders(boolean checkApiToken, RequestOptions provided, String acceptType)
            throws AuthenticationException {
        Map<String, List<String>> headerMap = new HashMap<>();

        // Request-Id + any custom headers (do these first, so they cannot clobber auth, etc)
        headerMap.put("X-Atlan-Request-Id", List.of(requestId));
        if (client.getExtraHeaders() != null) {
            headerMap.putAll(client.getExtraHeaders());
        }
        // Note: we will NOT use the member 'options' here, as we only want these to
        // override any set against the client itself if they are actually provided.
        // (Any defaults should not clobber any extras placed on the client level.)
        if (provided != null && provided.getExtraHeaders() != null) {
            headerMap.putAll(provided.getExtraHeaders());
        }

        // Accept
        headerMap.put("Accept", List.of(acceptType));

        // Accept-Charset
        headerMap.put("Accept-Charset", List.of(ApiResource.CHARSET.name()));

        // Authorization
        if (client.isLocal()) {
            String encodedCreds =
                    Base64.getEncoder().encodeToString(client.getBasicAuth().getBytes(StandardCharsets.UTF_8));
            headerMap.put("Authorization", List.of(String.format("Basic %s", encodedCreds)));
        } else {
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
            headerMap.put("Authorization", List.of(String.format("Bearer %s", apiToken)));
        }

        return HttpHeaders.of(headerMap);
    }
}
