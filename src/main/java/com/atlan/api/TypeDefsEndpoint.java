/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.typedefs.*;
import com.atlan.model.typedefs.TypeDefResponse;
import com.atlan.net.ApiResource;
import com.atlan.util.StringUtils;
import java.util.List;

/**
 * API endpoints for operating on Atlan's type definitions (in simple terms the underlying metadata model).
 */
public class TypeDefsEndpoint extends AtlasEndpoint {

    private static final String endpoint = "/types/typedefs";
    private static final String endpoint_singular = "/types/typedef";

    /**
     * Retrieves a list of the type definitions in Atlan.
     *
     * @param category of type definitions to retrieve
     * @return the requested list of type definitions
     * @throws AtlanException on any API communication issue
     */
    public static TypeDefResponse getTypeDefs(AtlanTypeCategory category) throws AtlanException {
        String url = String.format(
                "%s%s",
                getBaseUrl(),
                String.format("%s?type=%s", endpoint, category.getValue().toLowerCase()));
        return ApiResource.request(ApiResource.RequestMethod.GET, url, "", TypeDefResponse.class, null);
    }

    /**
     * Create a new type definition in Atlan.
     * Note: only custom metadata, enumerations, and Atlan tag type definitions are currently supported.
     *
     * @param typeDef to create
     * @return the resulting type definition that was created
     * @throws AtlanException on any API communication issue
     */
    public static TypeDefResponse createTypeDef(TypeDef typeDef) throws AtlanException {
        TypeDefResponse response = null;
        if (typeDef != null) {
            switch (typeDef.getCategory()) {
                case ATLAN_TAG:
                case CUSTOM_METADATA:
                case ENUM:
                    response = createInternal(typeDef);
                    break;
                default:
                    throw new InvalidRequestException(
                            ErrorCode.UNABLE_TO_CREATE_TYPEDEF_CATEGORY,
                            typeDef.getCategory().getValue());
            }
        }
        return response;
    }

    /**
     * Create a new type definition in Atlan.
     * NOTE: INTERNAL USE ONLY. This will NOT work without specially-configured policies - use createTypeDef instead.
     *
     * @param typeDef to create
     * @return the resulting type definition that was created
     * @throws AtlanException on any API communication issue
     * @see #createTypeDef(TypeDef)
     */
    public static TypeDefResponse createInternal(TypeDef typeDef) throws AtlanException {
        TypeDefResponse.TypeDefResponseBuilder builder = TypeDefResponse.builder();
        if (typeDef != null) {
            switch (typeDef.getCategory()) {
                case ATLAN_TAG:
                    builder.atlanTagDefs(List.of((AtlanTagDef) typeDef));
                    break;
                case CUSTOM_METADATA:
                    builder.customMetadataDefs(List.of((CustomMetadataDef) typeDef));
                    break;
                case ENUM:
                    builder.enumDefs(List.of((EnumDef) typeDef));
                    break;
                case STRUCT:
                    builder.structDefs(List.of((StructDef) typeDef));
                    break;
                case ENTITY:
                    builder.entityDefs(List.of((EntityDef) typeDef));
                    break;
                case RELATIONSHIP:
                    builder.relationshipDefs(List.of((RelationshipDef) typeDef));
                    break;
            }
            return createInternal(builder);
        }
        // If there was no typedef provided, just return an empty response (noop)
        return builder.build();
    }

    private static TypeDefResponse createInternal(TypeDefResponse.TypeDefResponseBuilder builder)
            throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), endpoint);
        return ApiResource.request(ApiResource.RequestMethod.POST, url, builder.build(), TypeDefResponse.class, null);
    }

    /**
     * Update an existing type definition in Atlan.
     * Note: only custom metadata and Atlan tag type definitions are currently supported.
     *
     * @param typeDef to update
     * @return the resulting type definition that was updated
     * @throws AtlanException on any API communication issue
     */
    public static TypeDefResponse updateTypeDef(TypeDef typeDef) throws AtlanException {
        TypeDefResponse.TypeDefResponseBuilder builder = TypeDefResponse.builder();
        if (typeDef != null) {
            switch (typeDef.getCategory()) {
                case ATLAN_TAG:
                    builder.atlanTagDefs(List.of((AtlanTagDef) typeDef));
                    break;
                case CUSTOM_METADATA:
                    builder.customMetadataDefs(List.of((CustomMetadataDef) typeDef));
                    break;
                case ENUM:
                    builder.enumDefs(List.of((EnumDef) typeDef));
                    break;
                default:
                    throw new InvalidRequestException(
                            ErrorCode.UNABLE_TO_CREATE_TYPEDEF_CATEGORY,
                            typeDef.getCategory().getValue());
            }
            String url = String.format("%s%s", getBaseUrl(), endpoint);
            return ApiResource.request(
                    ApiResource.RequestMethod.PUT, url, builder.build(), TypeDefResponse.class, null);
        }
        // If there was no typedef provided, just return an empty response (noop)
        return builder.build();
    }

    /**
     * Delete the type definition.
     *
     * @param internalName the internal hashed-string name of the type definition
     * @throws AtlanException on any API communication issue
     */
    public static void purgeTypeDef(String internalName) throws AtlanException {
        String url = String.format(
                "%s%s",
                getBaseUrl(), String.format("%s/name/%s", endpoint_singular, StringUtils.encodeContent(internalName)));
        ApiResource.request(ApiResource.RequestMethod.DELETE, url, "", null, null);
    }
}
