/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about a distribution of values.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Histogram extends AtlanStruct {

    public static final String TYPE_NAME = "Histogram";

    /** Fixed typeName for Histogram. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    List<Double> boundaries;

    /** TBC */
    List<Double> frequencies;

    /**
     * Quickly create a new Histogram.
     * @param boundaries TBC
     * @param frequencies TBC
     * @return a Histogram with the provided information
     */
    public static Histogram of(List<Double> boundaries, List<Double> frequencies) {
        return Histogram.builder()
                .boundaries(boundaries)
                .frequencies(frequencies)
                .build();
    }
}
