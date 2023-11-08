/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.model.core.CustomMetadataAttributes;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Captures the details of custom metadata in a request.
 */
@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class CustomMetadataPayload extends AtlanRequestPayload {
    private static final long serialVersionUID = 2L;

    /** Custom metadata properties and their values. */
    CustomMetadataAttributes attributes;
}
