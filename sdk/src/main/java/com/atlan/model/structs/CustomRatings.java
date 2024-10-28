/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Ratings for an asset from the source system.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CustomRatings extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "CustomRatings";

    /** Fixed typeName for CustomRatings. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Username of the user who left the rating. */
    String customRatingFrom;

    /** Numeric score for the rating left by the user. */
    Long customRatingOf;

    /**
     * Quickly create a new CustomRatings.
     * @param customRatingFrom Username of the user who left the rating.
     * @param customRatingOf Numeric score for the rating left by the user.
     * @return a CustomRatings with the provided information
     */
    public static CustomRatings of(String customRatingFrom, Long customRatingOf) {
        return CustomRatings.builder()
                .customRatingFrom(customRatingFrom)
                .customRatingOf(customRatingOf)
                .build();
    }
}
