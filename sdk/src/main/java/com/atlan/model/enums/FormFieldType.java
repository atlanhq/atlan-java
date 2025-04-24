/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum FormFieldType implements AtlanEnum {
    INT("INT"),
    STRING("STRING"),
    DATE("DATE"),
    BOOLEAN("BOOLEAN"),
    LONG("LONG"),
    JSON("JSON"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    FormFieldType(String value) {
        this.value = value;
    }

    public static FormFieldType fromValue(String value) {
        for (FormFieldType b : FormFieldType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
