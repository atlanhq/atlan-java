/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AtlanLineageDirection implements AtlanEnum {
    UPSTREAM("INPUT"),
    DOWNSTREAM("OUTPUT"),
    BOTH("BOTH");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanLineageDirection(String value) {
        this.value = value;
    }

    public static AtlanLineageDirection fromValue(String value) {
        for (AtlanLineageDirection b : AtlanLineageDirection.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
