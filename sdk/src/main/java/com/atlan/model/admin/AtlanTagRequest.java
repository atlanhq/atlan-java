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
public class AtlanTagRequest extends AtlanRequest {
    private static final long serialVersionUID = 2L;

    public static final String REQUEST_TYPE = "attach_classification";
    public static final String SOURCE_TYPE = "atlas";

    /** Fixed requestType for Atlan tags. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String requestType = REQUEST_TYPE;

    /** Fixed sourceType for Atlan tags. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String sourceType = SOURCE_TYPE;

    /** Details of the requested Atlan tag. */
    AtlanTagPayload payload;

    /**
     * Create a new request to attach an Atlan tag to an asset.
     * Note that the asset must have at least its real (not placeholder) GUID and qualifiedName populated.
     *
     * @param asset against which to raise the request
     * @param atlanTagDetails details of the requested Atlan tag
     * @return a builder for the request with these details
     * @throws InvalidRequestException if any of the required details for the provided asset are missing
     */
    public static AtlanTagRequestBuilder<?, ?> creator(Asset asset, AtlanTagPayload atlanTagDetails)
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
        return creator(asset.getGuid(), asset.getQualifiedName(), asset.getTypeName(), atlanTagDetails);
    }

    /**
     * Create a new request to attach an Atlan tag to an asset.
     *
     * @param assetGuid unique identifier (GUID) of the asset to classify
     * @param assetQualifiedName qualifiedName of the asset to classify
     * @param assetType type of the asset to classify
     * @param atlanTagDetails details of the requested Atlan tag
     * @return a builder for the request with these details
     */
    public static AtlanTagRequestBuilder<?, ?> creator(
            String assetGuid, String assetQualifiedName, String assetType, AtlanTagPayload atlanTagDetails) {
        return AtlanTagRequest.builder()
                .id(UUID.randomUUID().toString())
                .destinationGuid(assetGuid)
                .destinationQualifiedName(assetQualifiedName)
                .entityType(assetType)
                .payload(atlanTagDetails);
    }

    public abstract static class AtlanTagRequestBuilder<C extends AtlanTagRequest, B extends AtlanTagRequestBuilder<C, B>>
        extends AtlanRequest.AtlanRequestBuilder<C, B> {}
}
