/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.exception.AtlanException;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.net.ApiResource;

/**
 * API endpoints for searching against Atlan's Elasticsearch index for all entities.
 */
public class IndexSearchEndpoint extends AtlasEndpoint {

    private static final String endpoint = "/search/indexsearch";

    /**
     * Run the requested search.
     *
     * @param request detailing the search query, parameters, and so on to run
     * @return the results of the search
     * @throws AtlanException on any API interaction problems
     */
    public static IndexSearchResponse search(IndexSearchRequest request) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), endpoint);
        return ApiResource.request(ApiResource.RequestMethod.POST, url, request, IndexSearchResponse.class, null);
    }
}
