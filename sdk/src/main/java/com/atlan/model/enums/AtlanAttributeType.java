/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AtlanAttributeType implements AtlanEnum {
    STRING("string"),
    LONG("long"),
    DECIMAL("float"),
    BOOLEAN("boolean"),
    DATE("date"),
    MAP_STRINGS("map<string,string>"),
    ENUM("enum"),
    STRUCT("struct");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanAttributeType(String value) {
        this.value = value;
    }

    public static AtlanAttributeType fromValue(String value) {
        for (AtlanAttributeType b : AtlanAttributeType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
