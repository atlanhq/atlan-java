/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Breakdown of the DQ Score by dimensions associated with the External DQ Test definition.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AssetExternalDQScoreBreakdownByDimension extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AssetExternalDQScoreBreakdownByDimension";

    /** Fixed typeName for AssetExternalDQScoreBreakdownByDimension. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Name of the dimension associated with the DQ Test definition on the external DQ tool. */
    String assetExternalDQScoreDimensionName;

    /** Description of the dimension associated with the DQ Test definition on the external DQ tool. */
    String assetExternalDQScoreDimensionDescription;

    /** DQ score value for the given dimension associated with the DQ Test definition on the external DQ tool. */
    String assetExternalDQScoreDimensionScoreValue;

    /** DQ score type for the given dimension associated with the DQ Test definition on the external DQ tool. */
    String assetExternalDQScoreDimensionScoreType;

    /**
     * Quickly create a new AssetExternalDQScoreBreakdownByDimension.
     * @param assetExternalDQScoreDimensionName Name of the dimension associated with the DQ Test definition on the external DQ tool.
     * @param assetExternalDQScoreDimensionDescription Description of the dimension associated with the DQ Test definition on the external DQ tool.
     * @param assetExternalDQScoreDimensionScoreValue DQ score value for the given dimension associated with the DQ Test definition on the external DQ tool.
     * @param assetExternalDQScoreDimensionScoreType DQ score type for the given dimension associated with the DQ Test definition on the external DQ tool.
     * @return a AssetExternalDQScoreBreakdownByDimension with the provided information
     */
    public static AssetExternalDQScoreBreakdownByDimension of(
            String assetExternalDQScoreDimensionName,
            String assetExternalDQScoreDimensionDescription,
            String assetExternalDQScoreDimensionScoreValue,
            String assetExternalDQScoreDimensionScoreType) {
        return AssetExternalDQScoreBreakdownByDimension.builder()
                .assetExternalDQScoreDimensionName(assetExternalDQScoreDimensionName)
                .assetExternalDQScoreDimensionDescription(assetExternalDQScoreDimensionDescription)
                .assetExternalDQScoreDimensionScoreValue(assetExternalDQScoreDimensionScoreValue)
                .assetExternalDQScoreDimensionScoreType(assetExternalDQScoreDimensionScoreType)
                .build();
    }

    public abstract static class AssetExternalDQScoreBreakdownByDimensionBuilder<
                    C extends AssetExternalDQScoreBreakdownByDimension,
                    B extends AssetExternalDQScoreBreakdownByDimensionBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
