/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.model.IndexSearchRequest;
import com.atlan.model.responses.IndexSearchResponse;
import com.atlan.net.ApiResource;

/**
 * API endpoints for searching against Atlan's Elasticsearch index for all entities.
 */
public class IndexSearchEndpoint {

    private static final String endpoint = "/api/meta/search/indexsearch";

    /**
     * Run the requested search.
     *
     * @param request detailing the search query, parameters, and so on to run
     * @return the results of the search
     * @throws AtlanException on any API interaction problems
     */
    public static IndexSearchResponse search(IndexSearchRequest request) throws AtlanException {
        String url = String.format("%s%s", Atlan.getBaseUrl(), endpoint);
        return ApiResource.request(ApiResource.RequestMethod.POST, url, request, IndexSearchResponse.class, null);
    }
}
