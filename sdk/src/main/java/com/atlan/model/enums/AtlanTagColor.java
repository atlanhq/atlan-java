/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AtlanTagColor implements AtlanEnum {
    GREEN("Green"),
    YELLOW("Yellow"),
    RED("Red"),
    GRAY("Gray");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanTagColor(String value) {
        this.value = value;
    }

    public static AtlanTagColor fromValue(String value) {
        for (AtlanTagColor b : AtlanTagColor.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
