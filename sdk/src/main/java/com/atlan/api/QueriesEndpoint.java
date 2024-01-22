/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.QueryRequest;
import com.atlan.model.admin.QueryResponse;
import com.atlan.net.ApiEventStreamResource;
import com.atlan.net.ApiResource;
import com.atlan.net.RequestOptions;

/**
 * API endpoints for running SQL queries.
 */
public class QueriesEndpoint extends HekaEndpoint {

    private static final String endpoint = "/query/stream";

    public QueriesEndpoint(AtlanClient client) {
        super(client);
    }

    /**
     * Runs the provided query and returns its results.
     *
     * @param request query to run
     * @return results of the query
     * @throws AtlanException on any issues with API communication
     */
    public QueryResponse stream(QueryRequest request) throws AtlanException {
        return stream(request, null);
    }

    /**
     * Runs the provided query and returns its results.
     *
     * @param request query to run
     * @param options to override default client settings
     * @return results of the query
     * @throws AtlanException on any issues with API communication
     */
    public QueryResponse stream(QueryRequest request, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), endpoint);
        return ApiEventStreamResource.request(
                client, ApiResource.RequestMethod.POST, url, request, QueryResponse.class, options);
    }
}
