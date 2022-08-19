package com.atlan.api;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.Classification;
import com.atlan.model.core.Entity;
import com.atlan.model.core.SingleEntityRequest;
import com.atlan.model.responses.EntityMutationResponse;
import com.atlan.net.ApiResource;
import java.util.List;

public class EntityUpdateEndpoint {

    private static final String endpoint_attr = "/api/meta/entity/uniqueAttribute/type";

    /**
     * Updates any simple attributes provided. Note that this only supports adding or updating
     * the values of these attributes — it is not possible to REMOVE (null) attributes through
     * this endpoint.
     *
     * @param typeName type of asset to be updated
     * @param qualifiedName qualifiedName of the asset to be updated
     * @param value the entity containing only the attributes to be updated
     */
    public static EntityMutationResponse updateAttributes(String typeName, String qualifiedName, Entity value)
            throws AtlanException {
        String url = String.format(
                "%s%s",
                Atlan.getBaseUrl(),
                String.format(
                        "%s/%s?attr:qualifiedName=%s", endpoint_attr, typeName, ApiResource.urlEncode(qualifiedName)));
        SingleEntityRequest seq = SingleEntityRequest.builder().entity(value).build();
        return ApiResource.request(ApiResource.RequestMethod.PUT, url, seq, EntityMutationResponse.class, null);
    }

    /**
     * Updates any classifications. Note that this only supports adding or updating
     * the values of these classifications — it is not possible to REMOVE (null) classifications through
     * this endpoint.
     *
     * @param typeName type of asset to be updated
     * @param qualifiedName qualifiedName of the asset to be updated
     * @param classifications the list of classifications to add / update on the entity
     */
    public static void updateClassifications(
            String typeName, String qualifiedName, List<Classification> classifications) throws AtlanException {
        String url = String.format(
                "%s%s",
                Atlan.getBaseUrl(),
                String.format(
                        "%s/%s/classifications?attr:qualifiedName=%s",
                        endpoint_attr, typeName, ApiResource.urlEncode(qualifiedName)));
        System.out.println(url);
        // ApiResource.request(ApiResource.RequestMethod.PUT, url, classifications, EntityMutationResponse.class, null);
    }
}
