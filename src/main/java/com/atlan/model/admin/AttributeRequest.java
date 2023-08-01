/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import java.util.UUID;
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
}
