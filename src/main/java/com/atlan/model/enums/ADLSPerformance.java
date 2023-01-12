/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum ADLSPerformance implements AtlanEnum {
    STANDARD("Standard"),
    PREMIUM("Premium");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    ADLSPerformance(String value) {
        this.value = value;
    }

    public static ADLSPerformance fromValue(String value) {
        for (ADLSPerformance b : ADLSPerformance.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
