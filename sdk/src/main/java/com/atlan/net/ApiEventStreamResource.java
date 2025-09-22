/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.net;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanEventStreamResponseInterface;
import com.atlan.model.core.AtlanObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.*;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for all event-stream response objects.
 */
@Slf4j
@ToString(callSuper = true)
public abstract class ApiEventStreamResource extends AtlanObject implements AtlanEventStreamResponseInterface {
    private static final long serialVersionUID = 2L;

    @JsonIgnore
    private transient AtlanEventStreamResponse lastResponse;

    /**
     * {@inheritDoc}
     */
    @Override
    @JsonIgnore
    public AtlanEventStreamResponse getLastResponse() {
        return lastResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsonIgnore
    public void setLastResponse(AtlanEventStreamResponse response) {
        this.lastResponse = response;
    }

    /**
     * Pass-through to the request-handling method after confirming that the provided payload is non-null.
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
    public static <T extends ApiEventStreamResource> T request(
            AtlanClient client,
            ApiResource.RequestMethod method,
            String url,
            AtlanObject payload,
            Class<T> clazz,
            RequestOptions options)
            throws AtlanException {
        ApiResource.checkNullTypedParams(url, payload);
        return request(client, method, url, payload.toJson(client), clazz, options);
    }

    /**
     * Pass-through the request to the request-handling method.
     * This method wraps debug-level logging lines around the request to show precisely what was constructed and sent
     * to Atlan. What was returned in the response is explicitly not logged to avoid any sensitive information
     * possibly being logged.
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
    public static <T extends ApiEventStreamResource> T request(
            AtlanClient client,
            ApiResource.RequestMethod method,
            String url,
            String body,
            Class<T> clazz,
            RequestOptions options)
            throws AtlanException {
        // Create a unique ID for every request, and add it to the logging context and header
        String requestId = UUID.randomUUID().toString();
        ApiResource.injectTraceId(requestId);
        log.debug("({}) {} with: {}", method, url, body);
        T response =
                ApiResource.atlanResponseGetter.requestStream(client, method, url, body, clazz, options, requestId);
        // Ensure we reset the Atlan request ID, so we always have the context from the original
        // request that was made (even if it in turn triggered off other requests)
        ApiResource.injectTraceId(requestId);
        return response;
    }
}
