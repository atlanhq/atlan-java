/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum DataPolicyType implements AtlanEnum {
    ACCESS("access"),
    MASKING("masking");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DataPolicyType(String value) {
        this.value = value;
    }

    public static DataPolicyType fromValue(String value) {
        for (DataPolicyType b : DataPolicyType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
