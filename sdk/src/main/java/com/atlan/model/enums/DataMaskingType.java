/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum DataMaskingType implements AtlanEnum {
    SHOW_FIRST_4("MASK_SHOW_FIRST_4"),
    SHOW_LAST_4("MASK_SHOW_LAST_4"),
    HASH("MASK_HASH"),
    NULLIFY("MASK_NULL"),
    REDACT("MASK_REDACT");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DataMaskingType(String value) {
        this.value = value;
    }

    public static DataMaskingType fromValue(String value) {
        for (DataMaskingType b : DataMaskingType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
