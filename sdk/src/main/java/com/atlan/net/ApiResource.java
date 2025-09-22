/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.net;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.core.AtlanResponseInterface;
import com.atlan.model.enums.AtlanEnum;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

/**
 * Base class for all response objects.
 */
@Slf4j
@ToString(callSuper = true)
public abstract class ApiResource extends AtlanObject implements AtlanResponseInterface {
    private static final long serialVersionUID = 2L;

    public static final Charset CHARSET = StandardCharsets.UTF_8;

    static final AtlanResponseGetter atlanResponseGetter = new LiveAtlanResponseGetter();

    @JsonIgnore
    private transient AtlanResponse lastResponse;

    /**
     * {@inheritDoc}
     */
    @Override
    @JsonIgnore
    public AtlanResponse getLastResponse() {
        return lastResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsonIgnore
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
    @Override
    @JsonIgnore
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
     * Creates the HTTP query string for a given map of parameters.
     *
     * @param params The map of parameters.
     * @return The query string.
     */
    public static String createQueryString(Map<String, Object> params) {
        if (params == null) {
            return "";
        }

        Collection<KeyValuePair<String, String>> flatParams = flattenParamsMap(params).stream()
                .filter(kvp -> kvp.getValue() instanceof String)
                .map(kvp -> new KeyValuePair<>(kvp.getKey(), (String) kvp.getValue()))
                .collect(Collectors.toList());
        return createQueryString(flatParams);
    }

    /**
     * Creates the HTTP query string for a collection of name/value tuples.
     *
     * @param nameValueCollection The collection of name/value tuples.
     * @return The query string.
     */
    public static String createQueryString(Collection<KeyValuePair<String, String>> nameValueCollection) {
        if (nameValueCollection == null) {
            return "";
        }

        return nameValueCollection.stream()
                .map(kvp -> String.format("%s=%s", urlEncode(kvp.getKey()), urlEncode(kvp.getValue())))
                .collect(Collectors.joining("&"));
    }

    /**
     * Returns a list of parameters for a given value. The value can be basically anything, as long as
     * it can be encoded in some way.
     *
     * @param value The value for which to create the list of parameters.
     * @param keyPrefix The key under which new keys should be nested, if any.
     * @return The list of parameters.
     */
    private static List<KeyValuePair<String, Object>> flattenParamsValue(Object value, String keyPrefix) {
        List<KeyValuePair<String, Object>> flatParams;

        if (value == null) {
            flatParams = singleParam(keyPrefix, "");
        } else if (value instanceof Map<?, ?>) {
            flatParams = flattenParamsMap((Map<?, ?>) value);
        } else if (value instanceof String) {
            flatParams = singleParam(keyPrefix, value);
        } else if (value instanceof Collection<?>) {
            flatParams = flattenParamsCollection((Collection<?>) value, keyPrefix);
        } else if (value instanceof AtlanEnum) {
            flatParams = singleParam(keyPrefix, ((AtlanEnum) value).getValue());
        } else {
            flatParams = singleParam(keyPrefix, value.toString());
        }

        return flatParams;
    }

    /**
     * Returns a list of parameters for a given map.
     *
     * @param map The map for which to create the list of parameters.
     * @return The list of parameters.
     */
    private static List<KeyValuePair<String, Object>> flattenParamsMap(Map<?, ?> map) {
        List<KeyValuePair<String, Object>> flatParams = new ArrayList<>();
        if (map == null) {
            return flatParams;
        }

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();
            flatParams.addAll(flattenParamsValue(value, key));
        }

        return flatParams;
    }

    /**
     * Returns a list of parameters for a given collection of objects. The parameter keys will be
     * indexed under the `keyPrefix` parameter. E.g. if the `keyPrefix` is `foo`, then the key for the
     * first element's will be `foo[0]`, etc.
     *
     * @param collection The collection for which to create the list of parameters.
     * @param keyPrefix The key under which new keys should be nested.
     * @return The list of parameters.
     */
    private static List<KeyValuePair<String, Object>> flattenParamsCollection(
            Collection<?> collection, String keyPrefix) {
        List<KeyValuePair<String, Object>> flatParams = new ArrayList<>();
        if (collection == null) {
            return flatParams;
        }

        for (Object value : collection) {
            String newPrefix = String.format("%s", keyPrefix);
            flatParams.addAll(flattenParamsValue(value, newPrefix));
        }

        return flatParams;
    }

    /**
     * Creates a list containing a single parameter.
     *
     * @param key The parameter's key.
     * @param value The parameter's value.
     * @return A list containg the single parameter.
     */
    private static List<KeyValuePair<String, Object>> singleParam(String key, Object value) {
        List<KeyValuePair<String, Object>> flatParams = new ArrayList<>();
        flatParams.add(new KeyValuePair<>(key, value));
        return flatParams;
    }

    /**
     * Pass-through to the request-handling method after confirming that the provided payload is non-null,
     * for calls that do not expect a response.
     *
     * @param client connectivity to Atlan
     * @param method for the request
     * @param url of the request
     * @param payload to send in the request
     * @param options for sending the request (or null to use global defaults)
     * @throws AtlanException on any API interaction problem
     */
    public static void request(
            AtlanClient client,
            ApiResource.RequestMethod method,
            String url,
            AtlanObject payload,
            RequestOptions options)
            throws AtlanException {
        checkNullTypedParams(url, payload);
        request(client, method, url, payload.toJson(client), options);
    }

    /**
     * Pass-through the request to the request-handling method.
     * This method wraps debug-level logging lines around the request to show precisely what was constructed and sent
     * to Atlan. Since this method is used when no response is expected, no response will be logged.
     *
     * @param client connectivity to Atlan
     * @param method for the request
     * @param url of the request
     * @param body to send in the request, if any (to not send any use an empty string)
     * @param options for sending the request (or null to use global defaults)
     * @throws AtlanException on any API interaction problem
     */
    public static void request(
            AtlanClient client, ApiResource.RequestMethod method, String url, String body, RequestOptions options)
            throws AtlanException {
        // Create a unique ID for every request, and add it to the logging context and header
        String requestId = UUID.randomUUID().toString();
        injectTraceId(requestId);
        log.debug("({}) {} with: {}", method, url, body);
        ApiResource.atlanResponseGetter.request(client, method, url, body, options, requestId);
        // Ensure we reset the Atlan request ID, so we always have the context from the original
        // request that was made (even if it in turn triggered off other requests)
        injectTraceId(requestId);
        if (log.isDebugEnabled()) {
            log.debug(" ... empty response.");
        }
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
     * Pass-through to the request-handling method after confirming that the provided payload is non-null.
     * This uses special handling of the response, where the response is plain text rather than JSON.
     *
     * @param client connectivity to Atlan
     * @param method for the request
     * @param url of the request
     * @param payload to send in the request
     * @param options for sending the request (or null to use global defaults)
     * @return the response
     * @throws AtlanException on any API interaction problem
     */
    public static String requestPlainText(
            AtlanClient client,
            ApiResource.RequestMethod method,
            String url,
            AtlanObject payload,
            RequestOptions options)
            throws AtlanException {
        checkNullTypedParams(url, payload);
        return requestPlainText(client, method, url, payload.toJson(client), options);
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
        // Create a unique ID for every request, and add it to the logging context and header
        String requestId = UUID.randomUUID().toString();
        injectTraceId(requestId);
        log.debug("({}) {} with: {}", method, url, body);
        T response = ApiResource.atlanResponseGetter.request(client, method, url, body, clazz, options, requestId);
        // Ensure we reset the Atlan request ID, so we always have the context from the original
        // request that was made (even if it in turn triggered off other requests)
        injectTraceId(requestId);
        if (log.isDebugEnabled() && (options == null || !options.getSkipLogging())) {
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
        } else {
            log.debug(" ... response received -- skipped logging as requested.");
        }
        return response;
    }

    /**
     * Pass-through the request to the request-handling method.
     * This method wraps debug-level logging lines around the request to show precisely what was constructed and sent
     * to Atlan and precisely what was returned (prior to deserialization).
     * This handles the response as plain text rather than JSON.
     *
     * @param client connectivity to Atlan
     * @param method for the request
     * @param url of the request
     * @param body to send in the request, if any (to not send any use an empty string)
     * @param options for sending the request (or null to use global defaults)
     * @return the response
     * @throws AtlanException on any API interaction problem
     */
    public static String requestPlainText(
            AtlanClient client, ApiResource.RequestMethod method, String url, String body, RequestOptions options)
            throws AtlanException {
        // Create a unique ID for every request, and add it to the logging context and header
        String requestId = UUID.randomUUID().toString();
        injectTraceId(requestId);
        log.debug("({}) {} with: {}", method, url, body);
        String response =
                ApiResource.atlanResponseGetter.requestPlainText(client, method, url, body, options, requestId);
        // Ensure we reset the Atlan request ID, so we always have the context from the original
        // request that was made (even if it in turn triggered off other requests)
        injectTraceId(requestId);
        if (log.isDebugEnabled()) {
            if (response != null) {
                log.debug(" ... response: {}", response);
            } else {
                log.debug(" ... empty response.");
            }
        }
        return response;
    }

    /**
     * Pass-through the request to the request-handling method, for file uploads.
     * This method wraps debug-level logging lines around the request to show precisely what was constructed and sent
     * to Atlan and precisely what was returned (prior to deserialization).
     *
     * @param client connectivity to Atlan
     * @param method for the request
     * @param url of the request
     * @param payload binary input stream of the file contents
     * @param filename name of the file being streamed
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
            InputStream payload,
            String filename,
            Class<T> clazz,
            RequestOptions options)
            throws AtlanException {
        return request(client, method, url, payload, filename, clazz, null, options);
    }

    /**
     * Pass-through the request to the request-handling method, for file uploads.
     * This method wraps debug-level logging lines around the request to show precisely what was constructed and sent
     * to Atlan and precisely what was returned (prior to deserialization).
     *
     * @param client connectivity to Atlan
     * @param method for the request
     * @param url of the request
     * @param payload binary input stream of the file contents
     * @param filename name of the file being streamed
     * @param clazz defining the expected response type
     * @param extras additional form-encoded fields to send in the request
     * @param options for sending the request (or null to use global defaults)
     * @return the response
     * @param <T> the type of the response
     * @throws AtlanException on any API interaction problem
     */
    public static <T extends ApiResource> T request(
            AtlanClient client,
            ApiResource.RequestMethod method,
            String url,
            InputStream payload,
            String filename,
            Class<T> clazz,
            Map<String, String> extras,
            RequestOptions options)
            throws AtlanException {
        if (payload == null) {
            throw new IllegalArgumentException(String.format("Found null input stream for %s.", url));
        }
        // Create a unique ID for every request, and add it to the logging context and header
        String requestId = UUID.randomUUID().toString();
        injectTraceId(requestId);
        log.debug("({}) {} with: {}", method, url, filename);
        T response = ApiResource.atlanResponseGetter.request(
                client, method, url, payload, filename, clazz, extras, options, requestId);
        // Ensure we reset the Atlan request ID, so we always have the context from the original
        // request that was made (even if it in turn triggered off other requests)
        injectTraceId(requestId);
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
     * Pass-through the request to the request-handling method, for form-urlencoded endpoints.
     *
     * @param client connectivity to Atlan
     * @param method for the request
     * @param url of the request
     * @param map of key-value pairs to be form-urlencoded
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
            Map<String, Object> map,
            Class<T> clazz,
            RequestOptions options)
            throws AtlanException {
        if (map == null) {
            throw new IllegalArgumentException(String.format("Found null map of key-value pairs for %s.", url));
        }
        // Create a unique ID for every request, and add it to the logging context and header
        String requestId = UUID.randomUUID().toString();
        injectTraceId(requestId);
        T response = ApiResource.atlanResponseGetter.request(client, method, url, map, clazz, options, requestId);
        // Ensure we reset the Atlan request ID, so we always have the context from the original
        // request that was made (even if it in turn triggered off other requests)
        injectTraceId(requestId);
        if (log.isDebugEnabled()) {
            if (response != null) {
                if (Atlan.enableTelemetry) {
                    log.debug(
                            " ... response timing: {}",
                            response.getLastResponse().metrics());
                }
            } else {
                log.debug(" ... empty response.");
            }
        }
        return response;
    }

    public static void injectTraceId(String requestId) {
        MDC.put("X-Atlan-Request-Id", requestId);
        MDC.put("trace_id", requestId);
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
