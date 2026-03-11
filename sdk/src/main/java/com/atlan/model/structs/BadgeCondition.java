/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.atlan.model.enums.BadgeComparisonOperator;
import com.atlan.model.enums.BadgeConditionColor;
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
    BadgeComparisonOperator badgeConditionOperator;

    /** Value against which to compare the custom metadata attribute's content. */
    String badgeConditionValue;

    /** Color (in RGB hex, with a # prefix) to use when custom metadata attribute's content matches the value through the comparison operator. */
    String badgeConditionColorhex;

    /**
     * Build a new condition for a badge on a string-based custom metadata property (including options (enumerations)).
     * Note that this will wrap the value itself in double-quotes, as this is needed to properly set the value for the
     * badge. So for example if you set the value as {@code abc123} and you retrieve this value back from the badge
     * condition, you will receive back {@code "abc123"}.
     *
     * @param operator the comparison operator for the condition
     * @param value the value to match against the comparison operator
     * @param color the color to use when a value matches (from the standard colors available in the UI)
     * @return the badge condition with these criteria
     */
    public static BadgeCondition of(BadgeComparisonOperator operator, String value, BadgeConditionColor color) {
        return BadgeCondition.builder()
                .badgeConditionOperator(operator)
                .badgeConditionValue("\"" + value + "\"")
                .badgeConditionColorhex(color.getValue())
                .build();
    }

    /**
     * Build a new condition for a badge on a string-based custom metadata property (including options (enumerations)).
     * Note that this will wrap the value itself in double-quotes, as this is needed to properly set the value for the
     * badge. So for example if you set the value as {@code abc123} and you retrieve this value back from the badge
     * condition, you will receive back {@code "abc123"}.
     *
     * @param operator the comparison operator for the condition
     * @param value the value to match against the comparison operator
     * @param color the color to use when a value matches (any valid RGB hex string of the form {@code #ffffff})
     * @return the badge condition with these criteria
     */
    public static BadgeCondition of(BadgeComparisonOperator operator, String value, String color) {
        return BadgeCondition.builder()
                .badgeConditionOperator(operator)
                .badgeConditionValue("\"" + value + "\"")
                .badgeConditionColorhex(color)
                .build();
    }

    /**
     * Build a new condition for a badge on a number-based custom metadata property.
     *
     * @param operator the comparison operator for the condition
     * @param value the value to match against the comparison operator
     * @param color the color to use when a value matches (from the standard colors available in the UI)
     * @return the badge condition with these criteria
     */
    public static BadgeCondition of(BadgeComparisonOperator operator, Number value, BadgeConditionColor color) {
        return BadgeCondition.builder()
                .badgeConditionOperator(operator)
                .badgeConditionValue(value.toString())
                .badgeConditionColorhex(color.getValue())
                .build();
    }

    /**
     * Build a new condition for a badge on a number-based custom metadata property.
     *
     * @param operator the comparison operator for the condition
     * @param value the value to match against the comparison operator
     * @param color the color to use when a value matches (any valid RGB hex string of the form {@code #ffffff})
     * @return the badge condition with these criteria
     */
    public static BadgeCondition of(BadgeComparisonOperator operator, Number value, String color) {
        return BadgeCondition.builder()
                .badgeConditionOperator(operator)
                .badgeConditionValue(value.toString())
                .badgeConditionColorhex(color)
                .build();
    }

    /**
     * Build a new condition for a badge on a boolean-based custom metadata property.
     *
     * @param operator the comparison operator for the condition
     * @param value the value to match against the comparison operator
     * @param color the color to use when a value matches (from the standard colors available in the UI)
     * @return the badge condition with these criteria
     */
    public static BadgeCondition of(BadgeComparisonOperator operator, boolean value, BadgeConditionColor color) {
        return BadgeCondition.builder()
                .badgeConditionOperator(operator)
                .badgeConditionValue(Boolean.toString(value))
                .badgeConditionColorhex(color.getValue())
                .build();
    }

    /**
     * Build a new condition for a badge on a boolean-based custom metadata property.
     *
     * @param operator the comparison operator for the condition
     * @param value the value to match against the comparison operator
     * @param color the color to use when a value matches (any valid RGB hex string of the form {@code #ffffff})
     * @return the badge condition with these criteria
     */
    public static BadgeCondition of(BadgeComparisonOperator operator, boolean value, String color) {
        return BadgeCondition.builder()
                .badgeConditionOperator(operator)
                .badgeConditionValue(Boolean.toString(value))
                .badgeConditionColorhex(color)
                .build();
    }

    public abstract static class BadgeConditionBuilder<C extends BadgeCondition, B extends BadgeConditionBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
