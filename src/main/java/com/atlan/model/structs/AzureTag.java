/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.atlan.model.core.AtlanObject;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about an Azure tag.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class AzureTag extends AtlanObject {

    /**
     * Quickly create a new AzureTag.
     * @param azureTagKey Key of the Azure tag.
     * @param azureTagValue Value for the Azure tag.
     * @return a AzureTag with the provided information
     */
    public static AzureTag of(String azureTagKey, String azureTagValue) {
        return AzureTag.builder()
                .azureTagKey(azureTagKey)
                .azureTagValue(azureTagValue)
                .build();
    }

    /** Key of the Azure tag. */
    String azureTagKey;

    /** Value for the Azure tag. */
    String azureTagValue;
}
