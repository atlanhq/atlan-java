/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AtlanDeleteType implements AtlanEnum {
    HARD("HARD"),
    SOFT("SOFT"),
    DEFAULT("DEFAULT");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanDeleteType(String value) {
        this.value = value;
    }

    public static AtlanDeleteType fromValue(String value) {
        for (AtlanDeleteType b : AtlanDeleteType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
