/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.core.AtlanObject;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about an Azure tag.
 */
@Data
@Jacksonized
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AzureTag extends AtlanObject {
    /**
     * Quickly create a new Azure tag.
     * @param key key of the tag
     * @param value value of the tag
     * @return an Azure tag with the provided key and value
     */
    public static AzureTag of(String key, String value) {
        return AzureTag.builder().azureTagKey(key).azureTagValue(value).build();
    }

    /** Key of the tag. */
    String azureTagKey;

    /** Value of the tag. */
    String azureTagValue;
}
