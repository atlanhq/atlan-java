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
    String assetV2ExternalDQScoreDimensionName;

    /** Description of the dimension associated with the DQ Test definition on the external DQ tool. */
    String assetV2ExternalDQScoreDimensionDescription;

    /** DQ score value for the given dimension associated with the DQ Test definition on the external DQ tool. */
    String assetV2ExternalDQScoreDimensionScoreValue;

    /** DQ score type for the given dimension associated with the DQ Test definition on the external DQ tool. */
    String assetV2ExternalDQScoreDimensionScoreType;

    /**
     * Quickly create a new AssetExternalDQScoreBreakdownByDimension.
     * @param assetV2ExternalDQScoreDimensionName Name of the dimension associated with the DQ Test definition on the external DQ tool.
     * @param assetV2ExternalDQScoreDimensionDescription Description of the dimension associated with the DQ Test definition on the external DQ tool.
     * @param assetV2ExternalDQScoreDimensionScoreValue DQ score value for the given dimension associated with the DQ Test definition on the external DQ tool.
     * @param assetV2ExternalDQScoreDimensionScoreType DQ score type for the given dimension associated with the DQ Test definition on the external DQ tool.
     * @return a AssetExternalDQScoreBreakdownByDimension with the provided information
     */
    public static AssetExternalDQScoreBreakdownByDimension of(
            String assetV2ExternalDQScoreDimensionName,
            String assetV2ExternalDQScoreDimensionDescription,
            String assetV2ExternalDQScoreDimensionScoreValue,
            String assetV2ExternalDQScoreDimensionScoreType) {
        return AssetExternalDQScoreBreakdownByDimension.builder()
                .assetV2ExternalDQScoreDimensionName(assetV2ExternalDQScoreDimensionName)
                .assetV2ExternalDQScoreDimensionDescription(assetV2ExternalDQScoreDimensionDescription)
                .assetV2ExternalDQScoreDimensionScoreValue(assetV2ExternalDQScoreDimensionScoreValue)
                .assetV2ExternalDQScoreDimensionScoreType(assetV2ExternalDQScoreDimensionScoreType)
                .build();
    }

    public abstract static class AssetExternalDQScoreBreakdownByDimensionBuilder<
                    C extends AssetExternalDQScoreBreakdownByDimension,
                    B extends AssetExternalDQScoreBreakdownByDimensionBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
