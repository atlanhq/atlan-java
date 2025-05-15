/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.Asset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.search.IndexSearchRequest;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AttributeRequest extends AtlanRequest {
    private static final long serialVersionUID = 2L;

    public static final String REQUEST_TYPE = "attribute";
    public static final String SOURCE_TYPE = "static";

    /** Fixed requestType for attributes. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String requestType = REQUEST_TYPE;

    /** Fixed sourceType for attributes. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String sourceType = SOURCE_TYPE;

    /**
     * Create a new request to change an attribute's value.
     * Note that the asset must have at least its real (not placeholder) GUID and qualifiedName populated.
     *
     * @param asset against which to raise the request
     * @param attribute name of the attribute to change
     * @param value new value for the attribute
     * @return a builder for the request with these details
     * @throws InvalidRequestException if any of the required details for the provided asset are missing
     */
    public static AttributeRequestBuilder<?, ?> creator(Asset asset, String attribute, String value)
            throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (asset.getQualifiedName() == null || asset.getQualifiedName().isEmpty()) {
            missing.add("qualifiedName");
        }
        if (asset.getGuid() == null || asset.getGuid().isEmpty()) {
            missing.add("guid");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, asset.getTypeName(), String.join(",", missing));
        }
        return creator(asset.getGuid(), asset.getQualifiedName(), asset.getTypeName(), attribute, value);
    }

    /**
     * Create a new request to change an attribute's value.
     *
     * @param assetGuid unique identifier (GUID) of the asset to change
     * @param assetQualifiedName qualifiedName of the asset to change
     * @param assetType type of the asset to change
     * @param attribute name of the attribute to change
     * @param value new value for the attribute
     * @return a builder for the request with these details
     */
    public static AttributeRequestBuilder<?, ?> creator(
            String assetGuid, String assetQualifiedName, String assetType, String attribute, String value) {
        return AttributeRequest.builder()
                .id(UUID.randomUUID().toString())
                .destinationAttribute(attribute)
                .destinationGuid(assetGuid)
                .destinationQualifiedName(assetQualifiedName)
                .entityType(assetType)
                .destinationValue(value);
    }

    public abstract static class AttributeRequestBuilder<C extends AttributeRequest, B extends AttributeRequestBuilder<C, B>>
        extends AtlanRequest.AtlanRequestBuilder<C, B> {}
}
