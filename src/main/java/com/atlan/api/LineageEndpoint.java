/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.exception.AtlanException;
import com.atlan.model.lineage.LineageListRequest;
import com.atlan.model.lineage.LineageListResponse;
import com.atlan.model.lineage.LineageRequest;
import com.atlan.model.lineage.LineageResponse;
import com.atlan.net.ApiResource;

/**
 * API endpoints for retrieving lineage from Atlan.
 */
public class LineageEndpoint extends AtlasEndpoint {

    private static final String endpoint = "/lineage/getlineage";
    private static final String list_endpoint = "/lineage/list";

    /**
     * Run the requested search.
     *
     * @param request detailing the search query, parameters, and so on to run
     * @return the results of the search
     * @throws AtlanException on any API interaction problems
     */
    public static LineageResponse fetch(LineageRequest request) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), endpoint);
        return ApiResource.request(ApiResource.RequestMethod.POST, url, request, LineageResponse.class, null);
    }

    /**
     * Retrieve lineage using the higher-performance "list" API.
     *
     * @param request detailing the lineage to retrieve
     * @return the results of the lineage
     * @throws AtlanException on any API interaction problems
     */
    public static LineageListResponse fetch(LineageListRequest request) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), list_endpoint);
        return ApiResource.request(ApiResource.RequestMethod.POST, url, request, LineageListResponse.class, null);
    }
}
