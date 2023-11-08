/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AtlanCustomAttributeCardinality implements AtlanEnum {
    SINGLE("SINGLE"),
    SET("SET");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanCustomAttributeCardinality(String value) {
        this.value = value;
    }

    public static AtlanCustomAttributeCardinality fromValue(String value) {
        for (AtlanCustomAttributeCardinality b : AtlanCustomAttributeCardinality.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
