package com.atlan.api;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.responses.TypeDefResponseJ;
import com.atlan.model.typedefs.*;
import com.atlan.net.ApiResourceJ;
import java.util.List;

public class TypeDefsEndpointJ {

    private static final String endpoint = "/api/meta/types/typedefs";
    private static final String endpoint_singular = "/api/meta/types/typedef";

    /**
     * Retrieves a list of the type definitions in Atlan.
     * @param category of type definitions to retrieve
     * @return the requested list of type definitions
     * @throws AtlanException on any API communication issue
     */
    public static TypeDefResponseJ getTypeDefs(AtlanTypeCategory category) throws AtlanException {
        String url = String.format(
                "%s%s",
                Atlan.getBaseUrl(),
                String.format("%s?type=%s", endpoint, category.getValue().toLowerCase()));
        return ApiResourceJ.request(ApiResourceJ.RequestMethod.GET, url, "", TypeDefResponseJ.class, null);
    }

    /**
     * Create a new type definition in Atlan.
     * @param typeDef to create
     * @return the resulting type definition that was created
     * @throws AtlanException on any API communication issue
     */
    public static TypeDefResponseJ createTypeDef(TypeDefJ typeDef) throws AtlanException {
        TypeDefResponseJ wrapper = new TypeDefResponseJ();
        if (typeDef != null) {
            switch (typeDef.getCategory()) {
                case CLASSIFICATION:
                    wrapper.setClassificationDefs(List.of((ClassificationDefJ) typeDef));
                    break;
                case BUSINESS_METADATA:
                    wrapper.setBusinessMetadataDefs(List.of((CustomMetadataDefJ) typeDef));
                    break;
                default:
                    throw new InvalidRequestException(
                            "Unable to create new type definitions of category: " + typeDef.getCategory(),
                            "category",
                            "",
                            "ATLAN-CLIENT-400-010",
                            400,
                            null);
            }
            String url = String.format("%s%s", Atlan.getBaseUrl(), endpoint);
            return ApiResourceJ.request(ApiResourceJ.RequestMethod.POST, url, wrapper, TypeDefResponseJ.class, null);
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
        ApiResourceJ.request(ApiResourceJ.RequestMethod.DELETE, url, "", null, null);
    }
}
