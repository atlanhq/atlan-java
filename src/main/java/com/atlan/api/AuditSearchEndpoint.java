/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.exception.AtlanException;
import com.atlan.model.search.AuditSearchRequest;
import com.atlan.model.search.AuditSearchResponse;
import com.atlan.net.ApiResource;

/**
 * API endpoints for searching against Atlan's Elasticsearch index for entity changes (activity log).
 */
public class AuditSearchEndpoint extends AtlasEndpoint {

    private static final String endpoint = "/entity/auditSearch";

    /**
     * Run the requested search.
     *
     * @param request detailing the search query, parameters, and so on to run
     * @return the results of the search
     * @throws AtlanException on any API interaction problems
     */
    public static AuditSearchResponse search(AuditSearchRequest request) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), endpoint);
        return ApiResource.request(ApiResource.RequestMethod.POST, url, request, AuditSearchResponse.class, null);
    }
}
