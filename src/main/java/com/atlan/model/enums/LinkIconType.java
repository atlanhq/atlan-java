/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum LinkIconType implements AtlanEnum {
    IMAGE("image"),
    EMOJI("emoji");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    LinkIconType(String value) {
        this.value = value;
    }

    public static LinkIconType fromValue(String value) {
        for (LinkIconType b : LinkIconType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
