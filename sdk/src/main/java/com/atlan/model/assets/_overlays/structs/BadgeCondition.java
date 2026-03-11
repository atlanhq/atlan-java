// IMPORT: import com.atlan.model.enums.BadgeComparisonOperator;
// IMPORT: import com.atlan.model.enums.BadgeConditionColor;
// TYPE_OVERRIDE: badgeConditionOperator=BadgeComparisonOperator

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
