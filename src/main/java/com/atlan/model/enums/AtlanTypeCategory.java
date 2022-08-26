/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AtlanTypeCategory implements AtlanEnum {
    ENUM("ENUM"),
    STRUCT("STRUCT"),
    CLASSIFICATION("CLASSIFICATION"),
    ENTITY("ENTITY"),
    RELATIONSHIP("RELATIONSHIP"),
    BUSINESS_METADATA("BUSINESS_METADATA");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanTypeCategory(String value) {
        this.value = value;
    }

    public static AtlanTypeCategory fromValue(String value) {
        for (AtlanTypeCategory b : AtlanTypeCategory.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
