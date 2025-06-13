/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.net;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import com.atlan.AtlanClient;
import com.atlan.exception.*;
import com.atlan.model.core.AtlanError;
import com.atlan.model.core.AtlanEventStreamResponseInterface;
import com.atlan.model.core.AtlanResponseInterface;
import com.atlan.serde.Serde;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class that wraps the API request and response handling, such as detecting errors from specific response codes.
 */
public class LiveAtlanResponseGetter implements AtlanResponseGetter {
    private final HttpClient httpClient;

    /**
     * Initializes a new instance of the {@link LiveAtlanResponseGetter} class with default
     * parameters.
     */
    public LiveAtlanResponseGetter() {
        this(null);
    }

    /**
     * Initializes a new instance of the {@link LiveAtlanResponseGetter} class.
     *
     * @param httpClient the HTTP client to use
     */
    public LiveAtlanResponseGetter(HttpClient httpClient) {
        this.httpClient = (httpClient != null) ? httpClient : buildDefaultHttpClient();
    }

    /** {@inheritDoc} */
    @Override
    public void request(
            AtlanClient client,
            ApiResource.RequestMethod method,
            String url,
            String body,
            RequestOptions options,
            String requestId)
            throws AtlanException {
        AtlanRequest request = new AtlanRequest(client, method, url, body, options, requestId);
        request(request);
    }

    /**
     * Send a request to Atlan, when no response is expected.
     *
     * @param request bundle of all information about the request that should be sent
     * @throws AtlanException on any API issues
     */
    private void request(AtlanRequest request) throws AtlanException {
        AtlanResponse response = httpClient.requestWithRetries(request);
        int responseCode = response.code();
        if (responseCode < 200 || responseCode >= 300) {
            handleApiError(response);
        }
    }

    /** {@inheritDoc} */
    @Override
    public <T extends AtlanResponseInterface> T request(
            AtlanClient client,
            ApiResource.RequestMethod method,
            String url,
            String body,
            Class<T> clazz,
            RequestOptions options,
            String requestId)
            throws AtlanException {
        AtlanRequest request = new AtlanRequest(client, method, url, body, options, requestId);
        return request(request, clazz);
    }

    /** {@inheritDoc} */
    @Override
    public String requestPlainText(
            AtlanClient client,
            ApiResource.RequestMethod method,
            String url,
            String body,
            RequestOptions options,
            String requestId)
            throws AtlanException {
        AtlanRequest request = new AtlanRequest(client, method, url, body, options, requestId);
        return requestPlainText(request);
    }

    /** {@inheritDoc} */
    @Override
    public <T extends AtlanEventStreamResponseInterface> T requestStream(
            AtlanClient client,
            ApiResource.RequestMethod method,
            String url,
            String body,
            Class<T> clazz,
            RequestOptions options,
            String requestId)
            throws AtlanException {
        AtlanRequest request = new AtlanRequest(client, method, url, body, options, requestId, "text/event-stream");
        return requestStream(request, clazz);
    }

    /** {@inheritDoc} */
    @Override
    public <T extends AtlanResponseInterface> T request(
            AtlanClient client,
            ApiResource.RequestMethod method,
            String url,
            InputStream upload,
            String filename,
            Class<T> clazz,
            Map<String, String> extras,
            RequestOptions options,
            String requestId)
            throws AtlanException {
        AtlanRequest request = new AtlanRequest(client, method, url, upload, filename, extras, options, requestId);
        return request(request, clazz);
    }

    /** {@inheritDoc} */
    @Override
    public <T extends AtlanResponseInterface> T request(
            AtlanClient client,
            ApiResource.RequestMethod method,
            String url,
            Map<String, Object> map,
            Class<T> clazz,
            RequestOptions options,
            String requestId)
            throws AtlanException {
        AtlanRequest request = new AtlanRequest(client, method, url, map, options, requestId);
        return request(request, clazz);
    }

    /**
     * Makes a request to Atlan's API.
     *
     * @param request bundled details of the request to make
     * @param clazz the expected response object type from the request
     * @return the response of the request
     * @param <T> the type of the response of the request
     * @throws AtlanException on any API interaction problem, indicating the type of problem encountered
     */
    private <T extends AtlanResponseInterface> T request(AtlanRequest request, Class<T> clazz) throws AtlanException {
        AtlanResponse response = httpClient.requestWithRetries(request);

        int responseCode = response.code();
        String responseBody = response.body();

        if (responseCode < 200 || responseCode >= 300) {
            handleApiError(response);
        }

        T resource = null;
        if (clazz != null) {
            try {
                resource = request.client().readValue(responseBody, clazz);
            } catch (IOException e) {
                raiseMalformedJsonError(responseBody, responseCode, e);
            }
        }

        // Null check necessary for empty responses
        if (resource != null) {
            resource.setLastResponse(response);
        }

        return resource;
    }

    /**
     * Makes a request to Atlan's API.
     *
     * @param request bundled details of the request to make
     * @return the response of the request
     * @throws AtlanException on any API interaction problem, indicating the type of problem encountered
     */
    private String requestPlainText(AtlanRequest request) throws AtlanException {
        AtlanResponse response = httpClient.requestWithRetries(request);

        int responseCode = response.code();
        String responseBody = response.body();

        if (responseCode < 200 || responseCode >= 300) {
            handleApiError(responseCode, responseBody);
        }

        return responseBody;
    }

    /**
     * Makes a request to Atlan's API.
     *
     * @param request bundled details of the request to make
     * @param clazz the expected response object type from the request
     * @return the response of the request
     * @param <T> the type of the response of the request
     * @throws AtlanException on any API interaction problem, indicating the type of problem encountered
     */
    private <T extends AtlanEventStreamResponseInterface> T requestStream(AtlanRequest request, Class<T> clazz)
            throws AtlanException {
        AtlanEventStreamResponse response = httpClient.requestEventStream(request);

        int responseCode = response.code();
        List<String> responseBody = response.body();

        if (responseCode < 200 || responseCode >= 300) {
            handleApiError(response);
        }

        T resource = null;
        if (clazz != null) {
            try {
                List<T> events = new ArrayList<>();
                for (String eventBody : responseBody) {
                    if (eventBody.startsWith("data: ")) {
                        // Trim off the "data: " prefix from the event before attempting to deserialize
                        events.add(request.client().readValue(eventBody.substring(6), clazz));
                    }
                }
                resource = clazz.getConstructor(List.class).newInstance(events);
            } catch (IOException e) {
                raiseMalformedJsonError(responseBody.toString(), responseCode, e);
            } catch (NoSuchMethodException
                    | IllegalAccessException
                    | InstantiationException
                    | InvocationTargetException e) {
                throw new LogicException(ErrorCode.UNABLE_TO_DESERIALIZE, responseBody.toString());
            }
        }

        // Null check necessary for empty responses
        if (resource != null) {
            resource.setLastResponse(response);
        }

        return resource;
    }

    private static HttpClient buildDefaultHttpClient() {
        return new HttpURLConnectionClient();
    }

    /**
     * If the response does not contain valid JSON, there was probably a pretty significant problem, so this raises
     * an ApiException in such cases.
     *
     * @param responseBody body of the response
     * @param responseCode numeric code of the response
     * @param e cause of the error, if any
     * @throws ApiException indicating that the response object was invalid (not valid JSON)
     */
    private static void raiseMalformedJsonError(String responseBody, int responseCode, Throwable e)
            throws ApiException {
        throw new ApiException(ErrorCode.ERROR_PASSTHROUGH, e, "" + responseCode, responseBody, "");
    }

    /**
     * Detect specific exceptions based primarily on the response code received from Atlan.
     *
     * @param response received from an API call
     * @throws AtlanException a more specific exception, based on the details of that response
     */
    private static void handleApiError(AtlanResponse response) throws AtlanException {
        // Attempt to parse the error into an AtlanError object, but if that fails, fallback
        // to just using the raw message body (wasn't JSON to begin with)
        AtlanError error = null;
        try {
            error = Serde.allInclusiveMapper.readValue(response.body(), AtlanError.class);
        } catch (IOException e) {
            // If the response cannot be parsed, let's set it as plain text
            error = new AtlanError();
            error.setCode((long) response.code());
            error.setErrorMessage(response.body());
        }
        if (error == null) {
            raiseMalformedJsonError(response.body(), response.code(), null);
        }

        raiseError(response.code(), error);
    }

    /**
     * Detect specific exceptions based primarily on the response code received from Atlan.
     *
     * @param code numeric response code
     * @param body of the response received from an API call
     * @throws AtlanException a more specific exception, based on the details of that response
     */
    private static void handleApiError(int code, String body) throws AtlanException {

        // Check for a 500 response first -- if found, we won't have a JSON body to parse,
        // so preemptively exit with a generic ApiException pass-through.
        if (code >= 500) {
            raiseMalformedJsonError(body, code, null);
        }

        AtlanError error = new AtlanError();
        error.setCode((long) code);
        error.setErrorMessage(body);
        raiseError(code, error);
    }

    /**
     * Detect specific exceptions based primarily on the response code received from Atlan.
     *
     * @param response received from an API call
     * @throws AtlanException a more specific exception, based on the details of that response
     */
    private static void handleApiError(AtlanEventStreamResponse response) throws AtlanException {
        AtlanError error = null;

        // Attempt to parse the error into an AtlanError object, but if that fails, fallback
        // to just using the raw message body (wasn't JSON to begin with)
        try {
            error = Serde.allInclusiveMapper.readValue(response.body().get(0), AtlanError.class);
        } catch (IOException e) {
            raiseMalformedJsonError(response.body().get(0), response.code(), e);
        }
        if (error == null) {
            raiseMalformedJsonError(response.body().toString(), response.code(), null);
        }

        raiseError(response.code(), error);
    }

    /**
     * Raise an Atlan-specific exception based on the response code.
     *
     * @param code numeric response code received from an API call
     * @param error error details parsed from the response
     * @throws AtlanException a more specific exception, based on the details of the response
     */
    private static void raiseError(int code, AtlanError error) throws AtlanException {
        AtlanException exception;
        switch (code) {
            case 400:
                exception = new InvalidRequestException(
                        ErrorCode.INVALID_REQUEST_PASSTHROUGH,
                        error.findCode(),
                        error.findMessage(),
                        error.renderCauses());
                break;
            case 404:
                exception = new NotFoundException(
                        ErrorCode.NOT_FOUND_PASSTHROUGH, error.findCode(), error.findMessage(), error.renderCauses());
                break;
            case 401:
                exception = new AuthenticationException(
                        ErrorCode.AUTHENTICATION_PASSTHROUGH,
                        error.findCode(),
                        error.findMessage(),
                        error.renderCauses());
                break;
            case 403:
                exception = new PermissionException(
                        ErrorCode.PERMISSION_PASSTHROUGH, error.findCode(), error.findMessage(), error.renderCauses());
                break;
            case 409:
                exception = new ConflictException(
                        ErrorCode.CONFLICT_PASSTHROUGH, error.findCode(), error.findMessage(), error.renderCauses());
                break;
            case 423:
                exception = new LockedException(
                        ErrorCode.LOCK_PASSTHROUGH, error.findCode(), error.findMessage(), error.renderCauses());
                break;
            case 429:
                exception = new RateLimitException(
                        ErrorCode.RATE_LIMIT_PASSTHROUGH, error.findCode(), error.findMessage(), error.renderCauses());
                break;
            default:
                exception = new ApiException(
                        ErrorCode.ERROR_PASSTHROUGH, null, error.findCode(), error.findMessage(), error.renderCauses());
                break;
        }

        exception.setAtlanError(error);

        throw exception;
    }
}
