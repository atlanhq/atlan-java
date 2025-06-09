/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AtlanCustomAttributePrimitiveType implements AtlanEnum {
    STRING("string"),
    INTEGER("int"),
    DECIMAL("float"),
    BOOLEAN("boolean"),
    DATE("date"),
    OPTIONS("enum"),
    USERS("users"),
    GROUPS("groups"),
    URL("url"),
    SQL("SQL"),
    LONG("long");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanCustomAttributePrimitiveType(String value) {
        this.value = value;
    }

    public static AtlanCustomAttributePrimitiveType fromValue(String value) {
        for (AtlanCustomAttributePrimitiveType b : AtlanCustomAttributePrimitiveType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
