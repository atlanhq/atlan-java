/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about an Azure tag.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AzureTag extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AzureTag";

    /** Fixed typeName for AzureTag. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Key of the Azure tag. */
    String azureTagKey;

    /** Value for the Azure tag. */
    String azureTagValue;

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

    public abstract static class AzureTagBuilder<C extends AzureTag, B extends AzureTagBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
