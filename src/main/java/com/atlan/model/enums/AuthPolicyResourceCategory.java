/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AuthPolicyResourceCategory implements AtlanEnum {
    ENTITY("ENTITY"),
    RELATIONSHIP("RELATIONSHIP"),
    TAG("TAG"),
    CUSTOM("CUSTOM"),
    TYPEDEFS("TYPEDEFS"),
    ADMIN("ADMIN"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AuthPolicyResourceCategory(String value) {
        this.value = value;
    }

    public static AuthPolicyResourceCategory fromValue(String value) {
        for (AuthPolicyResourceCategory b : AuthPolicyResourceCategory.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
