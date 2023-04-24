/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum ${className} implements AtlanEnum {
<#list values as value>
    ${value.enumeratedValue}("${value.actualValue}"),
</#list>
;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    ${className}(String value) {
        this.value = value;
    }

    public static ${className} fromValue(String value) {
        for (${className} b : ${className}.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
