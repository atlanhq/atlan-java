/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum HekaFlow implements AtlanEnum {
    BYPASS("BYPASS_FLOW"),
    REWRITE("REWRITE_FLOW"),
    PASSTHROUGH("PASSTHROUGH_FLOW"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    HekaFlow(String value) {
        this.value = value;
    }

    public static HekaFlow fromValue(String value) {
        for (HekaFlow b : HekaFlow.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
