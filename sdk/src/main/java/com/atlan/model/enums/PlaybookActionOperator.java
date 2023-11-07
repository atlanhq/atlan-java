/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum PlaybookActionOperator implements AtlanEnum {
    ADD("add"),
    UPDATE("update"),
    REMOVE("remove"),
    REPLACE("replace");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    PlaybookActionOperator(String value) {
        this.value = value;
    }

    public static PlaybookActionOperator fromValue(String value) {
        for (PlaybookActionOperator b : PlaybookActionOperator.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
