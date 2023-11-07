/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum MaskingType implements AtlanEnum {
    SHOW_FIRST_4("heka:MASK_SHOW_FIRST_4"),
    SHOW_LAST_4("heka:MASK_SHOW_LAST_4"),
    HASH("heka:MASK_HASH"),
    NULLIFY("heka:MASK_NULL"),
    REDACT("heka:MASK_REDACT");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    MaskingType(String value) {
        this.value = value;
    }

    public static MaskingType fromValue(String value) {
        for (MaskingType b : MaskingType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
