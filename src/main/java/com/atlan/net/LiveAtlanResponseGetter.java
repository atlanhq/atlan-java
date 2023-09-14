/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.net;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import com.atlan.AtlanClient;
import com.atlan.exception.*;
import com.atlan.model.core.AtlanError;
import com.atlan.model.core.AtlanResponseInterface;
import com.atlan.serde.Serde;
import java.io.IOException;
import java.io.InputStream;
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

    /**
     * Makes a request to an Atlan's API.
     *
     * @param client connectivity to Atlan
     * @param method to use for the request
     * @param url of the endpoint (with all path and query parameters) for the request
     * @param body payload for the request, if any
     * @param clazz the expected response object type from the request
     * @param options any alternative options to use for the request, or null to use default options
     * @param requestId unique identifier (GUID) of a single request to Atlan
     * @return the response of the request
     * @param <T> the type of the response of the request
     * @throws AtlanException on any API interaction problem, indicating the type of problem encountered
     */
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

    /**
     * Makes a request to Atlan's API, to upload a file.
     *
     * @param client connectivity to Atlan
     * @param method to use for the request
     * @param url of the endpoint (with all path and query parameters) for the request
     * @param upload file to be uploaded
     * @param filename name of the file the InputStream is reading
     * @param clazz the expected response object type from the request
     * @param options any alternative options to use for the request, or null to use default options
     * @param requestId unique identifier (GUID) of a single request to Atlan
     * @return the response of the request
     * @param <T> the type of the response of the request
     * @throws AtlanException on any API interaction problems, indicating the type of problem encountered
     */
    @Override
    public <T extends AtlanResponseInterface> T request(
            AtlanClient client,
            ApiResource.RequestMethod method,
            String url,
            InputStream upload,
            String filename,
            Class<T> clazz,
            RequestOptions options,
            String requestId)
            throws AtlanException {
        AtlanRequest request = new AtlanRequest(client, method, url, upload, filename, options, requestId);
        return request(request, clazz);
    }

    /**
     * Makes a request to Atlan's API, to form-urlencode parameters.
     *
     * @param client connectivity to Atlan
     * @param method to use for the request
     * @param url of the endpoint (with all path and query parameters) for the request
     * @param map of key-value pairs to be form-urlencoded
     * @param clazz the expected response object type from the request
     * @param options any alternative options to use for the request, or null to use default options
     * @param requestId unique identifier (GUID) of a single request to Atlan
     * @return the response of the request
     * @param <T> the type of the response of the request
     * @throws AtlanException on any API interaction problems, indicating the type of problem encountered
     */
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
        // so preemptively exit with a generic ApiException pass-through.
        int rc = response.code();
        if (rc == 500) {
            throw new ApiException(
                    ErrorCode.ERROR_PASSTHROUGH, null, "" + rc, response.body() == null ? "" : response.body());
        }

        try {
            error = Serde.allInclusiveMapper.readValue(response.body(), AtlanError.class);
        } catch (IOException e) {
            raiseMalformedJsonError(response.body(), response.code(), e);
        }
        if (error == null) {
            raiseMalformedJsonError(response.body(), response.code(), null);
        }

        switch (response.code()) {
            case 400:
                exception = new InvalidRequestException(
                        ErrorCode.INVALID_REQUEST_PASSTHROUGH, error.findCode(), error.findMessage());
                break;
            case 404:
                exception =
                        new NotFoundException(ErrorCode.NOT_FOUND_PASSTHROUGH, error.findCode(), error.findMessage());
                break;
            case 401:
                exception = new AuthenticationException(
                        ErrorCode.AUTHENTICATION_PASSTHROUGH, error.findCode(), error.findMessage());
                break;
            case 403:
                exception = new PermissionException(
                        ErrorCode.PERMISSION_PASSTHROUGH, error.findCode(), error.findMessage());
                break;
            case 409:
                exception =
                        new ConflictException(ErrorCode.CONFLICT_PASSTHROUGH, error.findCode(), error.findMessage());
                break;
            case 429:
                // TODO: confirm that a 429 is raised rather than needing to check the X-RateLimit-Remaining-Minute
                //  header value of a response (if it is 0 then we are being rate-limited)
                exception =
                        new RateLimitException(ErrorCode.RATE_LIMIT_PASSTHROUGH, error.findCode(), error.findMessage());
                break;
            default:
                exception = new ApiException(ErrorCode.ERROR_PASSTHROUGH, null, error.findCode(), error.findMessage());
                break;
        }

        exception.setAtlanError(error);

        throw exception;
    }
}
