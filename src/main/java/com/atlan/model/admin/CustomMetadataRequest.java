/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class CustomMetadataRequest extends AtlanRequest {

    public static final String REQUEST_TYPE = "bm_attribute";
    public static final String SOURCE_TYPE = "static";

    /** Fixed requestType for classifications. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String requestType = REQUEST_TYPE;

    /** Fixed sourceType for classifications. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String sourceType = SOURCE_TYPE;

    /** Custom metadata attributes and values that were requested. */
    CustomMetadataPayload payload;
}
