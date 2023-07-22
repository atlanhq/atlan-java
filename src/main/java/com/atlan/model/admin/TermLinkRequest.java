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
public class TermLinkRequest extends AtlanRequest {

    public static final String REQUEST_TYPE = "term_link";
    public static final String SOURCE_TYPE = "atlas";

    /** Fixed requestType for term assignments. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String requestType = REQUEST_TYPE;

    /** Fixed sourceType for term assignments. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String sourceType = SOURCE_TYPE;

    /**
     * Create a new request to link a term to an asset.
     *
     * @param assetGuid unique identifier (GUID) of the asset to link to the term
     * @param assetQualifiedName qualifiedName of the asset to link to the term
     * @param assetType type of the asset to link to the term
     * @param termGuid unique identifier (GUID) of the term to link to the asset
     * @param termQualifiedName qualifiedName of the term to link to the asset
     * @return a builder for the request with these details
     */
    public static TermLinkRequestBuilder<?, ?> creator(
            String assetGuid, String assetQualifiedName, String assetType, String termGuid, String termQualifiedName) {
        return TermLinkRequest.builder()
                .id(UUID.randomUUID().toString())
                .destinationGuid(assetGuid)
                .destinationQualifiedName(assetQualifiedName)
                .entityType(assetType)
                .sourceGuid(termGuid)
                .sourceQualifiedName(termQualifiedName);
    }
}
