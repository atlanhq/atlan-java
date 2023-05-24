/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.atlan.model.enums.BadgeComparisonOperator;
import com.atlan.model.enums.BadgeConditionColor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about a condition used in coloring a custom metadata badge in Atlan.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class BadgeCondition extends AtlanStruct {

    public static final String TYPE_NAME = "BadgeCondition";

    /** Fixed typeName for BadgeCondition. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /**
     * Quickly create a new BadgeCondition.
     * @param badgeConditionOperator Comparison operator to use when comparing a custom metadata attribute's value.
     * @param badgeConditionValue Value against which to compare the custom metadata attribute's content.
     * @param badgeConditionColorhex Color (in RGB hex, with a # prefix) to use when custom metadata attribute's content matches the value through the comparison operator.
     * @return a BadgeCondition with the provided information
     */
    public static BadgeCondition of(
            BadgeComparisonOperator badgeConditionOperator, String badgeConditionValue, String badgeConditionColorhex) {
        return BadgeCondition.builder()
                .badgeConditionOperator(badgeConditionOperator)
                .badgeConditionValue(badgeConditionValue)
                .badgeConditionColorhex(badgeConditionColorhex)
                .build();
    }

    /** Comparison operator to use when comparing a custom metadata attribute's value. */
    BadgeComparisonOperator badgeConditionOperator;

    /** Value against which to compare the custom metadata attribute's content. */
    String badgeConditionValue;

    /** Color (in RGB hex, with a # prefix) to use when custom metadata attribute's content matches the value through the comparison operator. */
    String badgeConditionColorhex;

    /**
     * Build a new condition for a badge.
     *
     * @param operator the comparison operator for the condition
     * @param value the value to match against the comparison operator
     * @param color the color to use when a value matches (from the standard colors available in the UI)
     * @return the badge condition with these criteria
     */
    public static BadgeCondition of(BadgeComparisonOperator operator, String value, BadgeConditionColor color) {
        return BadgeCondition.builder()
                .badgeConditionOperator(operator)
                .badgeConditionValue(value)
                .badgeConditionColorhex(color.getValue())
                .build();
    }
}
