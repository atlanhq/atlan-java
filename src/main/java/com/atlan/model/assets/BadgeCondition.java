/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.BadgeConditionColor;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about a badge condition.
 */
@Getter
@Jacksonized
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class BadgeCondition extends AtlanObject {
    /** Comparison operator to use when comparing a custom metadata attribute's value. */
    String badgeConditionOperator;

    /** Value against which to compare the custom metadata attribute's content. */
    String badgeConditionValue;

    /** Color (in RGB hex, with a # prefix) to use when custom metadata attribute's content matches the value through the comparison operator. */
    String badgeConditionColorhex;

    /**
     * Build a new condition for a badge.
     *
     * @param operator the comparison operator for the condition
     * @param value the value to match against the comparison operator
     * @param colorHex the color to use when a value matches
     * @return the badge condition with these criteria
     */
    public static BadgeCondition of(String operator, String value, String colorHex) {
        return BadgeCondition.builder()
                .badgeConditionOperator(operator)
                .badgeConditionValue(value)
                .badgeConditionColorhex(colorHex)
                .build();
    }

    /**
     * Build a new condition for a badge.
     *
     * @param operator the comparison operator for the condition
     * @param value the value to match against the comparison operator
     * @param color the color to use when a value matches (from the standard colors available in the UI)
     * @return the badge condition with these criteria
     */
    public static BadgeCondition of(String operator, String value, BadgeConditionColor color) {
        return BadgeCondition.builder()
                .badgeConditionOperator(operator)
                .badgeConditionValue(value)
                .badgeConditionColorhex(color.getValue())
                .build();
    }
}
