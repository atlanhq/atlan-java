/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.atlan.model.core.AtlanObject;
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
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * ${description}
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class ${className} extends AtlanObject {

    /**
     * Quickly create a new ${className}.
<#list attributes as attribute>
     * @param ${attribute.renamed} ${attribute.description}
</#list>
     * @return a ${className} with the provided information
     */
    public static ${className} of(<#list attributes as attribute>${attribute.fullType} ${attribute.renamed}<#sep>, </#list>) {
        return ${className}.builder()
        <#list attributes as attribute>
            .${attribute.renamed}(${attribute.renamed})
        </#list>
            .build();
    }

<#list attributes as attribute>
    /** ${attribute.description} */
    <#if attribute.renamed != attribute.originalName>
    @JsonProperty("${attribute.originalName}")
    </#if>
    ${attribute.fullType} ${attribute.renamed};

</#list>
<#if className == "BadgeCondition">
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
</#if>
}
