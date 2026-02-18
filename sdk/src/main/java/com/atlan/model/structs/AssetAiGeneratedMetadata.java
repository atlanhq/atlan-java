/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * AI-generated metadata suggestions for an asset, including descriptions, tags, and other enrichments.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AssetAiGeneratedMetadata extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AssetAiGeneratedMetadata";

    /** Fixed typeName for AssetAiGeneratedMetadata. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** AI-generated description suggestion. */
    AssetAiGeneratedField assetDescription;

    /**
     * Quickly create a new AssetAiGeneratedMetadata.
     * @param assetDescription AI-generated description suggestion.
     * @return an AssetAiGeneratedMetadata with the provided information
     */
    public static AssetAiGeneratedMetadata of(AssetAiGeneratedField assetDescription) {
        return AssetAiGeneratedMetadata.builder()
                .assetDescription(assetDescription)
                .build();
    }

    public abstract static class AssetAiGeneratedMetadataBuilder<
                    C extends AssetAiGeneratedMetadata, B extends AssetAiGeneratedMetadataBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
