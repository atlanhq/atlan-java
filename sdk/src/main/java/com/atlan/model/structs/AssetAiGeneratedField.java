/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about an AI-generated field value, including confidence and model details.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AssetAiGeneratedField extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AssetAiGeneratedField";

    /** Fixed typeName for AssetAiGeneratedField. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** The AI-generated value. */
    String assetFieldValue;

    /** Epoch ms when the value was generated. */
    Long assetGeneratedAt;

    /** Confidence score (0.0 to 1.0). */
    Double assetConfidence;

    /** Identifier of the AI model used. */
    String assetModel;

    /**
     * Quickly create a new AssetAiGeneratedField.
     * @param assetFieldValue The AI-generated value.
     * @param assetGeneratedAt Epoch ms when the value was generated.
     * @param assetConfidence Confidence score (0.0 to 1.0).
     * @param assetModel Identifier of the AI model used.
     * @return an AssetAiGeneratedField with the provided information
     */
    public static AssetAiGeneratedField of(
            String assetFieldValue, Long assetGeneratedAt, Double assetConfidence, String assetModel) {
        return AssetAiGeneratedField.builder()
                .assetFieldValue(assetFieldValue)
                .assetGeneratedAt(assetGeneratedAt)
                .assetConfidence(assetConfidence)
                .assetModel(assetModel)
                .build();
    }

    public abstract static class AssetAiGeneratedFieldBuilder<
                    C extends AssetAiGeneratedField, B extends AssetAiGeneratedFieldBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
