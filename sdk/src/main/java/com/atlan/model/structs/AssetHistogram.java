/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import java.util.List;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information representing a histogram of values for an asset.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AssetHistogram extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AssetHistogram";

    /** Fixed typeName for AssetHistogram. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Boundaries of the histogram. */
    @Singular
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    List<Double> assetHistogramBoundaries;

    /** Frequencies of the histogram. */
    @Singular
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    List<Double> assetHistogramFrequencies;

    /**
     * Quickly create a new AssetHistogram.
     * @param assetHistogramBoundaries Boundaries of the histogram.
     * @param assetHistogramFrequencies Frequencies of the histogram.
     * @return a AssetHistogram with the provided information
     */
    public static AssetHistogram of(List<Double> assetHistogramBoundaries, List<Double> assetHistogramFrequencies) {
        return AssetHistogram.builder()
                .assetHistogramBoundaries(assetHistogramBoundaries)
                .assetHistogramFrequencies(assetHistogramFrequencies)
                .build();
    }

    public abstract static class AssetHistogramBuilder<C extends AssetHistogram, B extends AssetHistogramBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
