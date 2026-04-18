/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Breakdown of the DQ Score by dimensions for External DQ Test runs.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AssetExternalDQTestScoreDimension extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AssetExternalDQTestScoreDimension";

    /** Fixed typeName for AssetExternalDQTestScoreDimension. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Name of the dimension associated with the DQ Test run on the external DQ tool. */
    String assetExternalDQTestScoreDimensionName;

    /** Description of the dimension associated with the DQ Test run on the external DQ tool. */
    String assetExternalDQTestScoreDimensionDescription;

    /** DQ score value for the given dimension associated with the DQ Test run on the external DQ tool. */
    String assetExternalDQTestScoreDimensionScoreValue;

    /** DQ score type for the given dimension associated with the DQ Test run on the external DQ tool. */
    String assetExternalDQTestScoreDimensionScoreType;

    /**
     * Quickly create a new AssetExternalDQTestScoreDimension.
     * @param assetExternalDQTestScoreDimensionName Name of the dimension associated with the DQ Test run on the external DQ tool.
     * @param assetExternalDQTestScoreDimensionDescription Description of the dimension associated with the DQ Test run on the external DQ tool.
     * @param assetExternalDQTestScoreDimensionScoreValue DQ score value for the given dimension associated with the DQ Test run on the external DQ tool.
     * @param assetExternalDQTestScoreDimensionScoreType DQ score type for the given dimension associated with the DQ Test run on the external DQ tool.
     * @return a AssetExternalDQTestScoreDimension with the provided information
     */
    public static AssetExternalDQTestScoreDimension of(
            String assetExternalDQTestScoreDimensionName,
            String assetExternalDQTestScoreDimensionDescription,
            String assetExternalDQTestScoreDimensionScoreValue,
            String assetExternalDQTestScoreDimensionScoreType) {
        return AssetExternalDQTestScoreDimension.builder()
                .assetExternalDQTestScoreDimensionName(assetExternalDQTestScoreDimensionName)
                .assetExternalDQTestScoreDimensionDescription(assetExternalDQTestScoreDimensionDescription)
                .assetExternalDQTestScoreDimensionScoreValue(assetExternalDQTestScoreDimensionScoreValue)
                .assetExternalDQTestScoreDimensionScoreType(assetExternalDQTestScoreDimensionScoreType)
                .build();
    }

    public abstract static class AssetExternalDQTestScoreDimensionBuilder<
                    C extends AssetExternalDQTestScoreDimension,
                    B extends AssetExternalDQTestScoreDimensionBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
