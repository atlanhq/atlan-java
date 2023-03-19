/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import java.util.UUID;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class ClassificationRequest extends AtlanRequest {

    public static final String REQUEST_TYPE = "attach_classification";
    public static final String SOURCE_TYPE = "atlas";

    /** Fixed requestType for classifications. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String requestType = REQUEST_TYPE;

    /** Fixed sourceType for classifications. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String sourceType = SOURCE_TYPE;

    /** Details of the requested classification. */
    ClassificationPayload payload;

    /**
     * Create a new request to attach a classification to an asset.
     *
     * @param assetGuid unique identifier (GUID) of the asset to classify
     * @param assetQualifiedName qualifiedName of the asset to classify
     * @param assetType type of the asset to classify
     * @param classificationDetails details of the requested classification
     * @return a builder for the request with these details
     */
    public static ClassificationRequestBuilder<?, ?> creator(
            String assetGuid,
            String assetQualifiedName,
            String assetType,
            ClassificationPayload classificationDetails) {
        return ClassificationRequest.builder()
                .id(UUID.randomUUID().toString())
                .destinationGuid(assetGuid)
                .destinationQualifiedName(assetQualifiedName)
                .entityType(assetType)
                .payload(classificationDetails);
    }
}
