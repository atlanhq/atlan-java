/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AuthPolicyCategory implements AtlanEnum {
    BOOTSTRAP("bootstrap"),
    PERSONA("persona"),
    PURPOSE("purpose"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AuthPolicyCategory(String value) {
        this.value = value;
    }

    public static AuthPolicyCategory fromValue(String value) {
        for (AuthPolicyCategory b : AuthPolicyCategory.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
