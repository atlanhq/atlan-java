package com.atlan.api;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.model.responses.TypeDefResponse;
import com.atlan.net.ApiResource;

public class TypeDefsEndpoint {

    private static final String endpoint = "/api/meta/types/typedefs";

    /**
     * Retrieves a list of the type definitions in Atlan.
     * @param category of type definitions to retrieve
     */
    public static TypeDefResponse getTypeDefs(String category) throws AtlanException {
        String url = String.format("%s%s", Atlan.getBaseUrl(), String.format("%s?type=%s", endpoint, category));
        return ApiResource.request(ApiResource.RequestMethod.GET, url, "", TypeDefResponse.class, null);
    }
}
