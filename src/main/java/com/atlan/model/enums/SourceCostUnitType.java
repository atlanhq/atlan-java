/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum SourceCostUnitType implements AtlanEnum {
    CREDITS("Credits"),
    BYTES("bytes"),
    SLOTS("slot-ms");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    SourceCostUnitType(String value) {
        this.value = value;
    }

    public static SourceCostUnitType fromValue(String value) {
        for (SourceCostUnitType b : SourceCostUnitType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
