package com.atlan.api;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.model.IndexSearchRequestJ;
import com.atlan.model.responses.IndexSearchResponseJ;
import com.atlan.net.ApiResourceJ;

public class IndexSearchEndpointJ {

    private static final String endpoint = "/api/meta/search/indexsearch";

    public static IndexSearchResponseJ search(IndexSearchRequestJ request) throws AtlanException {
        String url = String.format("%s%s", Atlan.getBaseUrl(), endpoint);
        return ApiResourceJ.request(ApiResourceJ.RequestMethod.POST, url, request, IndexSearchResponseJ.class, null);
    }
}
