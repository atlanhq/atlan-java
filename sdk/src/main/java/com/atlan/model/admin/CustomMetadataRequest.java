/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CustomMetadataRequest extends AtlanRequest {
    private static final long serialVersionUID = 2L;

    public static final String REQUEST_TYPE = "bm_attribute";
    public static final String SOURCE_TYPE = "static";

    /** Fixed requestType for custom metadata. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String requestType = REQUEST_TYPE;

    /** Fixed sourceType for custom metadata. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String sourceType = SOURCE_TYPE;

    /** Custom metadata attributes and values that were requested. */
    CustomMetadataPayload payload;

    public abstract static class CustomMetadataRequestBuilder<C extends CustomMetadataRequest, B extends CustomMetadataRequestBuilder<C, B>>
        extends AtlanRequest.AtlanRequestBuilder<C, B> {}
}
