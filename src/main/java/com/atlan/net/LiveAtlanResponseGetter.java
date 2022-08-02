/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.net;

import com.atlan.exception.*;
import com.atlan.model.core.AtlanError;
import com.atlan.model.core.AtlanResponseInterface;
import com.google.gson.JsonSyntaxException;

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

    @Override
    public <T extends AtlanResponseInterface> T request(
            ApiResource.RequestMethod method, String url, String body, Class<T> clazz, RequestOptions options)
            throws AtlanException {
        AtlanRequest request = new AtlanRequest(method, url, body, options);
        AtlanResponse response = httpClient.requestWithRetries(request);

        int responseCode = response.code();
        String responseBody = response.body();
        String requestId = response.requestId();

        if (responseCode < 200 || responseCode >= 300) {
            handleApiError(response);
        }

        T resource = null;
        try {
            resource = ApiResource.GSON.fromJson(responseBody, clazz);
        } catch (JsonSyntaxException e) {
            raiseMalformedJsonError(responseBody, responseCode, requestId, e);
        }

        resource.setLastResponse(response);

        return resource;
    }

    private static HttpClient buildDefaultHttpClient() {
        return new HttpURLConnectionClient();
    }

    private static void raiseMalformedJsonError(String responseBody, int responseCode, String requestId, Throwable e)
            throws ApiException {
        String details = e == null ? "none" : e.getMessage();
        throw new ApiException(
                String.format(
                        "Invalid response object from API: %s. (HTTP response code was %d). Additional details: %s.",
                        responseBody, responseCode, details),
                requestId,
                null,
                responseCode,
                e);
    }

    private static void handleApiError(AtlanResponse response) throws AtlanException {
        AtlanError error = null;
        AtlanException exception = null;

        // Check for a 500 response first -- if found, we won't have a JSON body to parse,
        // so preemptively exit with an invalid request exception.
        int rc = response.code();
        if (rc == 500) {
            throw new InvalidRequestException(response.body(), null, null, null, rc, null);
        }

        try {
            error = ApiResource.GSON.fromJson(response.body(), AtlanError.class);
        } catch (JsonSyntaxException e) {
            raiseMalformedJsonError(response.body(), response.code(), response.requestId(), e);
        }
        if (error == null) {
            raiseMalformedJsonError(response.body(), response.code(), response.requestId(), null);
        }

        switch (response.code()) {
            case 400:
                exception = new InvalidRequestException(
                        error.getErrorMessage(),
                        null,
                        response.requestId(),
                        error.getErrorCode(),
                        response.code(),
                        null);
                break;
            case 404:
                exception = new NotFoundException(
                        error.getErrorMessage(), response.requestId(), error.getErrorCode(), response.code(), null);
                break;
            case 401:
                exception = new AuthenticationException(
                        error.getErrorMessage(), response.requestId(), error.getErrorCode(), response.code());
                break;
            case 403:
                exception = new PermissionException(
                        error.getErrorMessage(), response.requestId(), error.getErrorCode(), response.code());
                break;
            case 409:
                exception = new ConflictException(
                        error.getErrorMessage(), response.requestId(), error.getErrorCode(), response.code(), null);
                break;
            case 429:
                exception = new RateLimitException(
                        error.getErrorMessage(),
                        null,
                        response.requestId(),
                        error.getErrorCode(),
                        response.code(),
                        null);
                break;
            default:
                exception = new ApiException(
                        error.getErrorMessage(), response.requestId(), error.getErrorCode(), response.code(), null);
                break;
        }

        exception.setAtlanError(error);

        throw exception;
    }
}
