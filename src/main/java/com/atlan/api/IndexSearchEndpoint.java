/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
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
        if (request.getDsl().getSort() == null || request.getDsl().getSort().isEmpty()) {
            // If no sort has been provided, explicitly sort by _doc for consistency of paging
            // operations
            request = request.toBuilder()
                    .dsl(request.getDsl().toBuilder()
                            .sortOption(SortOptions.of(
                                    s -> s.field(f -> f.field("_doc").order(SortOrder.Asc))))
                            .build())
                    .build();
        }
        return ApiResource.request(ApiResource.RequestMethod.POST, url, request, IndexSearchResponse.class, null);
    }
}
