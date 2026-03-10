/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum DataProductVisibility implements AtlanEnum {
    PRIVATE("Private"),
    PROTECTED("Protected"),
    PUBLIC("Public"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DataProductVisibility(String value) {
        this.value = value;
    }

    public static DataProductVisibility fromValue(String value) {
        for (DataProductVisibility b : DataProductVisibility.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
