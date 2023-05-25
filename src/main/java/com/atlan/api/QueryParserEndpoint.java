/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.exception.AtlanException;
import com.atlan.model.admin.ParsedQuery;
import com.atlan.model.admin.QueryParserRequest;
import com.atlan.net.ApiResource;

/**
 * API endpoints for parsing SQL queries.
 */
public class QueryParserEndpoint extends HekaEndpoint {

    private static final String endpoint = "/query/parse";

    /**
     * Parses the provided query to describe its component parts.
     *
     * @param request query to parse and configuration options
     * @return parsed explanation of the query
     * @throws AtlanException on any issues with API communication
     */
    public static ParsedQuery parseQuery(QueryParserRequest request) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), endpoint);
        return ApiResource.request(ApiResource.RequestMethod.POST, url, request, ParsedQuery.class, null);
    }
}
