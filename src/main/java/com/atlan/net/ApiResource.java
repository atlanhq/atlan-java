/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.net;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.core.AtlanResponseInterface;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for all response objects.
 */
@Slf4j
public abstract class ApiResource extends AtlanObject implements AtlanResponseInterface {
    public static final Charset CHARSET = StandardCharsets.UTF_8;

    private static final AtlanResponseGetter atlanResponseGetter = new LiveAtlanResponseGetter();

    private transient AtlanResponse lastResponse;

    private transient JsonNode rawJsonObject;

    /**
     * {@inheritDoc}
     */
    @Override
    public AtlanResponse getLastResponse() {
        return lastResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLastResponse(AtlanResponse response) {
        this.lastResponse = response;
    }

    /**
     * Returns the raw JsonNode exposed by the Jackson library. This can be used to access properties
     * that are not directly exposed by Atlan's Java library.
     *
     * <p>Note: You should always prefer using the standard property accessors whenever possible.
     * Because this method exposes Jackson's underlying API, it is not considered fully stable. Atlan's
     * Java library might move off Jackson in the future and this method would be removed or change
     * significantly.</p>
     *
     * @return The raw JsonNode.
     */
    public JsonNode getRawJsonObject() {
        // Lazily initialize this the first time the getter is called.
        if ((this.rawJsonObject == null) && (this.getLastResponse() != null)) {
            try {
                this.rawJsonObject =
                        Serde.allInclusiveMapper.readTree(this.getLastResponse().body());
            } catch (JsonProcessingException e) {
                log.error(
                        "Unable to parse raw JSON tree — invalid JSON? {}",
                        this.getLastResponse().body(),
                        e);
            }
        }
        return this.rawJsonObject;
    }

    /** HTTP methods that can be used in API requests. */
    public enum RequestMethod {
        GET,
        POST,
        PUT,
        DELETE
    }

    /** URL-encodes a string. */
    public static String urlEncode(String str) {
        // Preserve original behavior that passing null for an object id will lead
        // to us actually making a request to /v1/foo/null
        if (str == null) {
            return null;
        }

        // Don't use strict form encoding by changing the square bracket control
        // characters back to their literals. This is fine by the server, and
        // makes these parameter strings easier to read.
        return URLEncoder.encode(str, CHARSET).replaceAll("%5B", "[").replaceAll("%5D", "]");
    }

    /** URL-encode a string ID in url path formatting. */
    public static String urlEncodeId(String id) throws InvalidRequestException {
        if (id == null) {
            throw new InvalidRequestException(ErrorCode.NOTHING_TO_ENCODE);
        }
        return urlEncode(id);
    }

    /**
     * Pass-through to the request-handling method after confirming thatthe provided payload is non-null.
     *
     * @param client connectivity to Atlan
     * @param method for the request
     * @param url of the request
     * @param payload to send in the request
     * @param clazz defining the expected response type
     * @param options for sending the request (or null to use global defaults)
     * @return the response
     * @param <T> the type of the response
     * @throws AtlanException on any API interaction problem
     */
    public static <T extends ApiResource> T request(
            AtlanClient client,
            ApiResource.RequestMethod method,
            String url,
            AtlanObject payload,
            Class<T> clazz,
            RequestOptions options)
            throws AtlanException {
        checkNullTypedParams(url, payload);
        return request(client, method, url, payload.toJson(client), clazz, options);
    }

    /**
     * Pass-through the request to the request-handling method.
     * This method wraps debug-level logging lines around the request to show precisely what was constructed and sent
     * to Atlan and precisely what was returned (prior to deserialization).
     *
     * @param client connectivity to Atlan
     * @param method for the request
     * @param url of the request
     * @param body to send in the request, if any (to not send any use an empty string)
     * @param clazz defining the expected response type
     * @param options for sending the request (or null to use global defaults)
     * @return the response
     * @param <T> the type of the response
     * @throws AtlanException on any API interaction problem
     */
    public static <T extends ApiResource> T request(
            AtlanClient client,
            ApiResource.RequestMethod method,
            String url,
            String body,
            Class<T> clazz,
            RequestOptions options)
            throws AtlanException {
        log.debug("({}) {} with: {}", method, url, body);
        T response = ApiResource.atlanResponseGetter.request(client, method, url, body, clazz, options);
        if (log.isDebugEnabled()) {
            if (response != null) {
                if (Atlan.enableTelemetry) {
                    log.debug(
                            " ... response ({}): {}",
                            response.getLastResponse().metrics(),
                            response.getRawJsonObject());
                } else {
                    log.debug(" ... response: {}", response.getRawJsonObject());
                }
            } else {
                log.debug(" ... empty response.");
            }
        }
        return response;
    }

    public static <T extends ApiResource> T request(
            AtlanClient client,
            ApiResource.RequestMethod method,
            String url,
            InputStream payload,
            String filename,
            Class<T> clazz,
            RequestOptions options)
            throws AtlanException {
        if (payload == null) {
            throw new IllegalArgumentException(String.format("Found null input stream for %s.", url));
        }
        log.debug("({}) {} with: {}", method, url, filename);
        T response = ApiResource.atlanResponseGetter.request(client, method, url, payload, filename, clazz, options);
        if (log.isDebugEnabled()) {
            if (response != null) {
                if (Atlan.enableTelemetry) {
                    log.debug(
                            " ... response ({}): {}",
                            response.getLastResponse().metrics(),
                            response.getRawJsonObject());
                } else {
                    log.debug(" ... response: {}", response.getRawJsonObject());
                }
            } else {
                log.debug(" ... empty response.");
            }
        }
        return response;
    }

    /**
     * Invalidate null typed parameters.
     *
     * @param url request url associated with the given parameters.
     * @param params typed parameters to check for null value.
     */
    public static void checkNullTypedParams(String url, AtlanObject params) {
        if (params == null) {
            throw new IllegalArgumentException(String.format(
                    "Found null params for %s. "
                            + "Please pass empty params using param builder via `builder().build()` instead.",
                    url));
        }
    }
}
