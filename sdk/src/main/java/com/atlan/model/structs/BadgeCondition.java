/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about a condition used in coloring a custom metadata badge in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class BadgeCondition extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "BadgeCondition";

    /** Fixed typeName for BadgeCondition. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Comparison operator to use when comparing a custom metadata attribute's value. */
    String badgeConditionOperator;

    /** Value against which to compare the custom metadata attribute's content. */
    String badgeConditionValue;

    /** Color (in RGB hex, with a # prefix) to use when custom metadata attribute's content matches the value through the comparison operator. */
    String badgeConditionColorhex;

    /**
     * Quickly create a new BadgeCondition.
     * @param badgeConditionOperator Comparison operator to use when comparing a custom metadata attribute's value.
     * @param badgeConditionValue Value against which to compare the custom metadata attribute's content.
     * @param badgeConditionColorhex Color (in RGB hex, with a # prefix) to use when custom metadata attribute's content matches the value through the comparison operator.
     * @return a BadgeCondition with the provided information
     */
    public static BadgeCondition of(
            String badgeConditionOperator, String badgeConditionValue, String badgeConditionColorhex) {
        return BadgeCondition.builder()
                .badgeConditionOperator(badgeConditionOperator)
                .badgeConditionValue(badgeConditionValue)
                .badgeConditionColorhex(badgeConditionColorhex)
                .build();
    }

    public abstract static class BadgeConditionBuilder<C extends BadgeCondition, B extends BadgeConditionBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
