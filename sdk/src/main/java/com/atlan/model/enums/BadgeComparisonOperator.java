/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum BadgeComparisonOperator implements AtlanEnum {
    GT("gt"),
    GTE("gte"),
    LT("lt"),
    LTE("lte"),
    EQ("eq"),
    NEQ("neq");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    BadgeComparisonOperator(String value) {
        this.value = value;
    }

    public static BadgeComparisonOperator fromValue(String value) {
        for (BadgeComparisonOperator b : BadgeComparisonOperator.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
