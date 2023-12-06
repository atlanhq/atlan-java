/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.api;

import co.elastic.clients.elasticsearch._types.SortOrder;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.search.*;
import com.atlan.net.ApiResource;
import com.atlan.net.RequestOptions;
import lombok.extern.slf4j.Slf4j;

/**
 * API endpoints for operating on assets.
 */
@Slf4j
public class SearchLogEndpoint extends AtlasEndpoint {

    private static final String search_endpoint = "/search/searchlog";

    public SearchLogEndpoint(AtlanClient client) {
        super(client);
    }

    /**
     * Run the requested search against the search log.
     *
     * @param request detailing the search query, parameters, and so on to run
     * @return the results of the search
     * @throws AtlanException on any API interaction problems
     */
    public SearchLogResponse search(SearchLogRequest request) throws AtlanException {
        return search(request, null);
    }

    /**
     * Run the requested search against the search log.
     *
     * @param request detailing the search query, parameters, and so on to run
     * @param options to override default client settings
     * @return the results of the search
     * @throws AtlanException on any API interaction problems
     */
    public SearchLogResponse search(SearchLogRequest request, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), search_endpoint);
        if (request.getDsl().getSort() == null || request.getDsl().getSort().isEmpty()) {
            // If no sort has been provided, explicitly sort by time of the search for consistency of paging
            // (there is not a guaranteed-unique key in a search log entry, but if we sort by timestamp in
            // ascending order then earlier pages should never have additional entries - at least not until
            // there is full bi-temporal support in the search index, or time machines are invented...
            request = request.toBuilder()
                    .dsl(request.getDsl().toBuilder()
                            .sortOption(SearchLogEntry.SEARCHED_AT.order(SortOrder.Asc))
                            .build())
                    .build();
        }
        SearchLogResponse response = ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, request, SearchLogResponse.class, options);
        response.setClient(client);
        return response;
    }
}
