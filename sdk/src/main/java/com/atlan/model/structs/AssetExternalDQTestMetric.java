/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about the External DQ Test metric.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AssetExternalDQTestMetric extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AssetExternalDQTestMetric";

    /** Fixed typeName for AssetExternalDQTestMetric. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Metric value generated in the particular run for the asset on the external DQ tool. */
    String assetExternalDQTestMetricObservedValue;

    /** Represents the upper threshold of acceptable metric values in the particular run for the asset on the external DQ tool based on historical trends. */
    String assetExternalDQTestMetricUpperBound;

    /** Represents the lower threshold of acceptable metric values in the particular run for the asset on the external DQ tool based on historical trends. */
    String assetExternalDQTestMetricLowerBound;

    /**
     * Quickly create a new AssetExternalDQTestMetric.
     * @param assetExternalDQTestMetricObservedValue Metric value generated in the particular run for the asset on the external DQ tool.
     * @param assetExternalDQTestMetricUpperBound Represents the upper threshold of acceptable metric values in the particular run for the asset on the external DQ tool based on historical trends.
     * @param assetExternalDQTestMetricLowerBound Represents the lower threshold of acceptable metric values in the particular run for the asset on the external DQ tool based on historical trends.
     * @return a AssetExternalDQTestMetric with the provided information
     */
    public static AssetExternalDQTestMetric of(
            String assetExternalDQTestMetricObservedValue,
            String assetExternalDQTestMetricUpperBound,
            String assetExternalDQTestMetricLowerBound) {
        return AssetExternalDQTestMetric.builder()
                .assetExternalDQTestMetricObservedValue(assetExternalDQTestMetricObservedValue)
                .assetExternalDQTestMetricUpperBound(assetExternalDQTestMetricUpperBound)
                .assetExternalDQTestMetricLowerBound(assetExternalDQTestMetricLowerBound)
                .build();
    }

    public abstract static class AssetExternalDQTestMetricBuilder<
                    C extends AssetExternalDQTestMetric, B extends AssetExternalDQTestMetricBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
