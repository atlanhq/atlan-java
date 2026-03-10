/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum FormFieldDimension implements AtlanEnum {
    SINGLE("SINGLE"),
    MULTI("MULTI"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    FormFieldDimension(String value) {
        this.value = value;
    }

    public static FormFieldDimension fromValue(String value) {
        for (FormFieldDimension b : FormFieldDimension.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
