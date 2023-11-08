/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum AuthPolicyType implements AtlanEnum {
    ALLOW("allow"),
    DENY("deny"),
    ALLOW_EXCEPTIONS("allowExceptions"),
    DENY_EXCEPTIONS("denyExceptions"),
    DATA_MASK("dataMask"),
    ROW_FILTER("rowFilter"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AuthPolicyType(String value) {
        this.value = value;
    }

    public static AuthPolicyType fromValue(String value) {
        for (AuthPolicyType b : AuthPolicyType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
