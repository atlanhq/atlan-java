/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum ElasticRegexOperator implements AtlanEnum {
    ALL("ALL"),
    COMPLEMENT("COMPLEMENT"),
    EMPTY("EMPTY"),
    INTERVAL("INTERVAL"),
    INTERSECTION("INTERSECTION"),
    ANYSTRING("ANYSTRING"),
    NONE("NONE");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    ElasticRegexOperator(String value) {
        this.value = value;
    }

    public static ElasticRegexOperator fromValue(String value) {
        for (ElasticRegexOperator b : ElasticRegexOperator.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
