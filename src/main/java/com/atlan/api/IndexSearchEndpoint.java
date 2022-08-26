/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.model.IndexSearchRequest;
import com.atlan.model.responses.IndexSearchResponse;
import com.atlan.net.ApiResource;

public class IndexSearchEndpoint {

    private static final String endpoint = "/api/meta/search/indexsearch";

    public static IndexSearchResponse search(IndexSearchRequest request) throws AtlanException {
        String url = String.format("%s%s", Atlan.getBaseUrl(), endpoint);
        return ApiResource.request(ApiResource.RequestMethod.POST, url, request, IndexSearchResponse.class, null);
    }
}
