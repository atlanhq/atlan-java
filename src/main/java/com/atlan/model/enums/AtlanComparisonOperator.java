/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AtlanComparisonOperator implements AtlanEnum {
    CONTAINS("contains"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanComparisonOperator(String value) {
        this.value = value;
    }

    public static AtlanComparisonOperator fromValue(String value) {
        for (AtlanComparisonOperator b : AtlanComparisonOperator.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
