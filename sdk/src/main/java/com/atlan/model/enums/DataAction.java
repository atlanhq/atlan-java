/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum DataAction implements AtlanEnum, AtlanPolicyAction {
    SELECT("select");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DataAction(String value) {
        this.value = value;
    }

    public static DataAction fromValue(String value) {
        for (DataAction b : DataAction.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
