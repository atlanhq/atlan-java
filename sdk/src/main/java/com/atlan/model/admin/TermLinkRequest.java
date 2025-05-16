/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.GlossaryTerm;
import java.util.ArrayList;
import java.util.List;
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
    private static final long serialVersionUID = 2L;

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
     * Create a new request to change an attribute's value.
     * Note that the asset must have at least its real (not placeholder) GUID and qualifiedName populated.
     *
     * @param asset against which to raise the request
     * @param term to link to the asset
     * @return a builder for the request with these details
     * @throws InvalidRequestException if any of the required details for the provided asset are missing
     */
    public static TermLinkRequestBuilder<?, ?> creator(Asset asset, GlossaryTerm term) throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (asset.getQualifiedName() == null || asset.getQualifiedName().isEmpty()) {
            missing.add("qualifiedName");
        }
        if (asset.getGuid() == null || asset.getGuid().isEmpty()) {
            missing.add("guid");
        }
        if (term.getQualifiedName() == null || term.getQualifiedName().isEmpty()) {
            missing.add("term::qualifiedName");
        }
        if (term.getGuid() == null || term.getGuid().isEmpty()) {
            missing.add("term::guid");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, asset.getTypeName(), String.join(",", missing));
        }
        return creator(
                asset.getGuid(),
                asset.getQualifiedName(),
                asset.getTypeName(),
                term.getGuid(),
                term.getQualifiedName());
    }

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

    public abstract static class TermLinkRequestBuilder<
                    C extends TermLinkRequest, B extends TermLinkRequestBuilder<C, B>>
            extends AtlanRequest.AtlanRequestBuilder<C, B> {}
}
