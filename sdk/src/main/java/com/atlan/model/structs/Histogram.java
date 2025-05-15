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
 * Detailed information representing a histogram of values.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class Histogram extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Histogram";

    /** Fixed typeName for Histogram. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Boundaries of the histogram. */
    @Singular
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    List<Double> boundaries;

    /** Frequencies of the histogram. */
    @Singular
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    List<Double> frequencies;

    /**
     * Quickly create a new Histogram.
     * @param boundaries Boundaries of the histogram.
     * @param frequencies Frequencies of the histogram.
     * @return a Histogram with the provided information
     */
    public static Histogram of(List<Double> boundaries, List<Double> frequencies) {
        return Histogram.builder()
                .boundaries(boundaries)
                .frequencies(frequencies)
                .build();
    }

    public abstract static class HistogramBuilder<C extends Histogram, B extends HistogramBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
