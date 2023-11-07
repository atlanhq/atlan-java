/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum LogoType implements AtlanEnum {
    EMOJI("emoji"),
    IMAGE("image"),
    ICON("icon"),
    NONE(""),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    LogoType(String value) {
        this.value = value;
    }

    public static LogoType fromValue(String value) {
        for (LogoType b : LogoType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
