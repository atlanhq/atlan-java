package com.atlan.api;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.model.responses.EntityResponse;
import com.atlan.net.ApiResource;

public class EntityGuidEndpoint {

    private static final String endpoint = "/api/meta/entity/guid/";

    /** Retrieves any entity by its GUID. */
    public static EntityResponse retrieve(String guid, boolean ignoreRelationships, boolean minExtInfo)
            throws AtlanException {
        String url = String.format(
                "%s%s",
                Atlan.getApiBase(),
                String.format(
                        "%s%s?ignoreRelationships=%s&minExtInfo=%s",
                        endpoint, ApiResource.urlEncodeId(guid), ignoreRelationships, minExtInfo));
        return ApiResource.request(ApiResource.RequestMethod.GET, url, "", EntityResponse.class, null);
    }
}
