package com.atlan.api;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.model.responses.EntityResponse;
import com.atlan.net.ApiResource;

public class EntityUniqueAttributesEndpoint {

    private static final String endpoint = "/api/meta/entity/uniqueAttribute/type/";

    /** Retrieves any entity by its qualifiedName. */
    public static EntityResponse retrieve(
            String typeName, String qualifiedName, boolean ignoreRelationships, boolean minExtInfo)
            throws AtlanException {
        String url = String.format(
                "%s%s",
                Atlan.getBaseUrl(),
                String.format(
                        "%s%s?attr:qualifiedName=%s&ignoreRelationships=%s&minExtInfo=%s",
                        endpoint,
                        ApiResource.urlEncodeId(typeName),
                        ApiResource.urlEncodeId(qualifiedName),
                        ignoreRelationships,
                        minExtInfo));
        return ApiResource.request(ApiResource.RequestMethod.GET, url, "", EntityResponse.class, null);
    }
}
