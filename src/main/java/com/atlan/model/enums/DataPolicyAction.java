/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum DataPolicyAction implements AtlanEnum {
    SELECT("select");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DataPolicyAction(String value) {
        this.value = value;
    }

    public static DataPolicyAction fromValue(String value) {
        for (DataPolicyAction b : DataPolicyAction.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
