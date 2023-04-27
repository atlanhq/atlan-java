/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum IconType implements AtlanEnum {
    IMAGE("image"),
    EMOJI("emoji"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    IconType(String value) {
        this.value = value;
    }

    public static IconType fromValue(String value) {
        for (IconType b : IconType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
