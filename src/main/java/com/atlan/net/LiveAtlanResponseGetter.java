/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.net;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import com.atlan.exception.*;
import com.atlan.model.core.AtlanError;
import com.atlan.model.core.AtlanResponseInterface;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;

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

    /**
     * Makes a request to an Atlan's API.
     *
     * @param method to use for the request
     * @param url of the endpoint (with all path and query parameters) for the request
     * @param body payload for the request, if any
     * @param clazz the expected response object type from the request
     * @param options any alternative options to use for the request, or null to use default options
     * @return the response of the request
     * @param <T> the type of the response of the request
     * @throws AtlanException on any API interaction problem, indicating the type of problem encountered
     */
    @Override
    public <T extends AtlanResponseInterface> T request(
            ApiResource.RequestMethod method, String url, String body, Class<T> clazz, RequestOptions options)
            throws AtlanException {
        AtlanRequest request = new AtlanRequest(method, url, body, options);
        AtlanResponse response = httpClient.requestWithRetries(request);

        int responseCode = response.code();
        String responseBody = response.body();

        if (responseCode < 200 || responseCode >= 300) {
            handleApiError(response);
        }

        T resource = null;
        if (clazz != null) {
            try {
                resource = Serde.mapper.readValue(responseBody, clazz);
            } catch (JsonProcessingException e) {
                raiseMalformedJsonError(responseBody, responseCode, e);
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
        String details = e == null ? "none" : e.getMessage();
        throw new ApiException(ErrorCode.JSON_ERROR, e, responseBody, "" + responseCode, details);
    }

    /**
     * Detect specific exceptions based primarily on the response code received from Atlan.
     *
     * @param response received from an API call
     * @throws AtlanException a more specific exception, based on the details of that response
     */
    private static void handleApiError(AtlanResponse response) throws AtlanException {
        AtlanError error = null;
        AtlanException exception = null;

        // Check for a 500 response first -- if found, we won't have a JSON body to parse,
        // so preemptively exit with an invalid request exception.
        int rc = response.code();
        if (rc == 500) {
            throw new InvalidRequestException(ExceptionMessageDefinition.builder()
                    .errorMessage(response.body())
                    .httpErrorCode(rc)
                    .build());
        }

        try {
            error = Serde.mapper.readValue(response.body(), AtlanError.class);
        } catch (JsonProcessingException e) {
            raiseMalformedJsonError(response.body(), response.code(), e);
        }
        if (error == null) {
            raiseMalformedJsonError(response.body(), response.code(), null);
        }

        switch (response.code()) {
            case 400:
                exception = new InvalidRequestException(ErrorCode.INVALID_REQUEST_PASSTHROUGH, error.getErrorCode(), error.getErrorMessage());
                break;
            case 404:
                exception = new NotFoundException(ErrorCode.NOT_FOUND_PASSTHROUGH, error.getErrorCode(), error.getErrorMessage());
                break;
            case 401:
                exception = new AuthenticationException(ErrorCode.AUTHENTICATION_PASSTHROUGH, error.getErrorCode(), error.getErrorMessage());
                break;
            case 403:
                exception = new PermissionException(ErrorCode.PERMISSION_PASSTHROUGH, error.getErrorCode(), error.getErrorMessage());
                break;
            case 409:
                exception = new ConflictException(ErrorCode.CONFLICT_PASSTHROUGH, error.getErrorCode(), error.getErrorMessage());
                break;
            case 429:
                // TODO: confirm that a 429 is raised rather than needing to check the X-RateLimit-Remaining-Minute
                //  header value of a response (if it is 0 then we are being rate-limited)
                exception = new RateLimitException(ErrorCode.RATE_LIMIT_PASSTHROUGH, error.getErrorCode(), error.getErrorMessage());
                break;
            default:
                exception = new ApiException(ErrorCode.ERROR_PASSTHROUGH, null, error.getErrorCode(), error.getErrorMessage());
                break;
        }

        exception.setAtlanError(error);

        throw exception;
    }
}
