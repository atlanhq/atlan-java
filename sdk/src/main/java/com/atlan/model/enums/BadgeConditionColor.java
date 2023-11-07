/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum BadgeConditionColor implements AtlanEnum {
    GREEN("#047960"),
    YELLOW("#F7B43D"),
    RED("#BF1B1B"),
    GREY("#525C73");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    BadgeConditionColor(String value) {
        this.value = value;
    }

    public static BadgeConditionColor fromValue(String value) {
        for (BadgeConditionColor b : BadgeConditionColor.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
