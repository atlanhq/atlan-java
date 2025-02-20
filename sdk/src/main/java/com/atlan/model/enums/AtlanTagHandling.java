/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * Atlan tag association handling semantics.
 */
public enum AtlanTagHandling implements AtlanEnum {
    IGNORE("ignore"),
    APPEND("append"),
    REPLACE("replace"),
    REMOVE("remove"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanTagHandling(String value) {
        this.value = value;
    }

    public static AtlanTagHandling fromValue(String value) {
        for (AtlanTagHandling b : AtlanTagHandling.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
