/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.net;

/* Based on original code from https://github.com/stripe/stripe-java (under MIT license) */
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanResponseInterface;
import java.io.InputStream;
import java.util.Map;

/**
 * Interface through which API interaction wrapping is handled.
 */
public interface AtlanResponseGetter {

    /**
     * Send a request to Atlan API, when no response is expected.
     *
     * @param client connectivity details for the tenant to which to send the request
     * @param method type of request
     * @param url endpoint for the request
     * @param body payload to send as the body of the request
     * @param options any one-off options for this specific request
     * @param requestId unique identifier for this specific request
     * @throws AtlanException on any API interaction problem, indicating the type of problem encountered
     */
    void request(
            AtlanClient client,
            ApiResource.RequestMethod method,
            String url,
            String body,
            RequestOptions options,
            String requestId)
            throws AtlanException;

    /**
     * Send a request to an Atlan API, when a response is expected.
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
    <T extends AtlanResponseInterface> T request(
            AtlanClient client,
            ApiResource.RequestMethod method,
            String url,
            String body,
            Class<T> clazz,
            RequestOptions options,
            String requestId)
            throws AtlanException;

    /**
     * Send a request to an Atlan API, to upload a file.
     *
     * @param client connectivity to Atlan
     * @param method to use for the rqeuest
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
    default <T extends AtlanResponseInterface> T request(
            AtlanClient client,
            ApiResource.RequestMethod method,
            String url,
            InputStream upload,
            String filename,
            Class<T> clazz,
            RequestOptions options,
            String requestId)
            throws AtlanException {
        return request(client, method, url, upload, filename, clazz, null, options, requestId);
    }

    /**
     * Send a request to an Atlan API, to upload a file.
     *
     * @param client connectivity to Atlan
     * @param method to use for the request
     * @param url of the endpoint (with all path and query parameters) for the request
     * @param upload file to be uploaded
     * @param filename name of the file the InputStream is reading
     * @param clazz the expected response object type from the request
     * @param extras (optional) additional form-encoded parameters to send
     * @param options any alternative options to use for the request, or null to use default options
     * @param requestId unique identifier (GUID) of a single request to Atlan
     * @return the response of the request
     * @param <T> the type of the response of the request
     * @throws AtlanException on any API interaction problems, indicating the type of problem encountered
     */
    <T extends AtlanResponseInterface> T request(
            AtlanClient client,
            ApiResource.RequestMethod method,
            String url,
            InputStream upload,
            String filename,
            Class<T> clazz,
            Map<String, String> extras,
            RequestOptions options,
            String requestId)
            throws AtlanException;

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
    <T extends AtlanResponseInterface> T request(
            AtlanClient client,
            ApiResource.RequestMethod method,
            String url,
            Map<String, Object> map,
            Class<T> clazz,
            RequestOptions options,
            String requestId)
            throws AtlanException;
}
