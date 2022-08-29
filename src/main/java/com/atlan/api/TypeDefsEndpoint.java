/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.responses.TypeDefResponse;
import com.atlan.model.typedefs.*;
import com.atlan.net.ApiResource;
import java.util.List;

public class TypeDefsEndpoint {

    private static final String endpoint = "/api/meta/types/typedefs";
    private static final String endpoint_singular = "/api/meta/types/typedef";

    /**
     * Retrieves a list of the type definitions in Atlan.
     * @param category of type definitions to retrieve
     * @return the requested list of type definitions
     * @throws AtlanException on any API communication issue
     */
    public static TypeDefResponse getTypeDefs(AtlanTypeCategory category) throws AtlanException {
        String url = String.format(
                "%s%s",
                Atlan.getBaseUrl(),
                String.format("%s?type=%s", endpoint, category.getValue().toLowerCase()));
        return ApiResource.request(ApiResource.RequestMethod.GET, url, "", TypeDefResponse.class, null);
    }

    /**
     * Create a new type definition in Atlan.
     * @param typeDef to create
     * @return the resulting type definition that was created
     * @throws AtlanException on any API communication issue
     */
    public static TypeDefResponse createTypeDef(TypeDef typeDef) throws AtlanException {
        TypeDefResponse wrapper = new TypeDefResponse();
        if (typeDef != null) {
            switch (typeDef.getCategory()) {
                case CLASSIFICATION:
                    wrapper.setClassificationDefs(List.of((ClassificationDef) typeDef));
                    break;
                case BUSINESS_METADATA:
                    wrapper.setBusinessMetadataDefs(List.of((CustomMetadataDef) typeDef));
                    break;
                default:
                    throw new InvalidRequestException(
                            "Unable to create new type definitions of category: " + typeDef.getCategory(),
                            "category",
                            "ATLAN-CLIENT-400-010",
                            400,
                            null);
            }
            String url = String.format("%s%s", Atlan.getBaseUrl(), endpoint);
            return ApiResource.request(ApiResource.RequestMethod.POST, url, wrapper, TypeDefResponse.class, null);
        }
        // If there was no typedef provided, just return an empty response (noop)
        return wrapper;
    }

    /**
     * Delete the type definition.
     * @param internalName the internal hashed-string name of the type definition
     * @throws AtlanException on any API communication issue
     */
    public static void purgeTypeDef(String internalName) throws AtlanException {
        String url =
                String.format("%s%s", Atlan.getBaseUrl(), String.format("%s/name/%s", endpoint_singular, internalName));
        ApiResource.request(ApiResource.RequestMethod.DELETE, url, "", null, null);
    }
}
