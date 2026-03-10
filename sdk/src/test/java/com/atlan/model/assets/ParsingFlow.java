/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum ParsingFlow implements AtlanEnum {
    GUDUSOFT("GUDUSOFT_FLOW"),
    EXPLAIN_CALL("EXPLAIN_CALL_FLOW"),
    CALCITE("CALCITE_FLOW"),
    JSQL("JSQL_FLOW"),
    NONE("NO_PARSING"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    ParsingFlow(String value) {
        this.value = value;
    }

    public static ParsingFlow fromValue(String value) {
        for (ParsingFlow b : ParsingFlow.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
