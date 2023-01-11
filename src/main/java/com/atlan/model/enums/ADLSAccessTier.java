/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum ADLSAccessTier implements AtlanEnum {
    COOL("Cool"),
    HOT("Hot"),
    ARCHIVE("Archive");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    ADLSAccessTier(String value) {
        this.value = value;
    }

    public static ADLSAccessTier fromValue(String value) {
        for (ADLSAccessTier b : ADLSAccessTier.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
