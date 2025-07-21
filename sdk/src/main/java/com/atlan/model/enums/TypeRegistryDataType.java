/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum TypeRegistryDataType implements AtlanEnum {
    STRING("string"),
    INT("int"),
    LONG("long"),
    FLOAT("float"),
    BOOLEAN("boolean"),
    DATE("date"),
    EXTENDED("extended"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    TypeRegistryDataType(String value) {
        this.value = value;
    }

    public static TypeRegistryDataType fromValue(String value) {
        for (TypeRegistryDataType b : TypeRegistryDataType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
