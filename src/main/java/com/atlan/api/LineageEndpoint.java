/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.model.lineage.LineageRequest;
import com.atlan.model.lineage.LineageResponse;
import com.atlan.net.ApiResource;

/**
 * API endpoints for retrieving lineage from Atlan.
 */
public class LineageEndpoint {

    private static final String endpoint = "/api/meta/lineage/getlineage";

    /**
     * Run the requested search.
     *
     * @param request detailing the search query, parameters, and so on to run
     * @return the results of the search
     * @throws AtlanException on any API interaction problems
     */
    public static LineageResponse fetch(LineageRequest request) throws AtlanException {
        String url = String.format("%s%s", Atlan.getBaseUrl(), endpoint);
        return ApiResource.request(ApiResource.RequestMethod.POST, url, request, LineageResponse.class, null);
    }
}
