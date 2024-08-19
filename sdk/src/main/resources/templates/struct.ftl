/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package ${packageRoot}.structs;

import com.atlan.model.structs.AtlanStruct;
<#if className == "BadgeCondition">
import com.atlan.model.enums.BadgeComparisonOperator;
import com.atlan.model.enums.BadgeConditionColor;
</#if>
<#list attributes as attribute>
<#if attribute.type.type == "ENUM">
import com.atlan.model.enums.${attribute.type.name};
<#elseif attribute.type.type == "STRUCT">
import com.atlan.model.structs.${attribute.type.name};
</#if>
</#list>
import java.util.Map;
import java.util.List;
import java.util.SortedSet;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import javax.annotation.processing.Generated;

/**
 * ${description}
 */
@Generated(value="${generatorName}")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
<#if mapContainers??>@SuppressWarnings("cast")</#if>
public class ${className} extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "${className}";

    /** Fixed typeName for ${className}. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

<#list attributes as attribute>
    /** ${attribute.description} */
    <#if attribute.type.container?has_content && attribute.type.container == "Map<">@Builder.Default<#elseif attribute.singular??>@Singular<#if attribute.singular?has_content>("${attribute.singular}")</#if></#if>
    <#if attribute.renamed != attribute.originalName>
    @JsonProperty("${attribute.originalName}")
    </#if>
    ${attribute.fullType} ${attribute.renamed}<#if attribute.type.container?has_content && attribute.type.container == "Map<"> = null</#if>;

</#list>
<#if className == "BadgeCondition">
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
<#else>
    /**
     * Quickly create a new ${className}.
<#list attributes as attribute>
     * @param ${attribute.renamed} ${attribute.description}
</#list>
     * @return a ${className} with the provided information
     */
    public static ${className} of(<#list attributes as attribute>${attribute.fullType} ${attribute.renamed}<#sep>, </#sep></#list>) {
        return ${className}.builder()
        <#list attributes as attribute>
            .${attribute.renamed}(${attribute.renamed})
        </#list>
            .build();
    }
</#if>
}
