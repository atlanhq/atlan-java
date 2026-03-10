/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AtlanSSO implements AtlanEnum {
    GOOGLE("google"),
    AZURE_AD("azure"),
    OKTA("okta"),
    JUMPCLOUD("jumpcloud"),
    ONELOGIN("onelogin");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanSSO(String value) {
        this.value = value;
    }

    public static AtlanSSO fromValue(String value) {
        for (AtlanSSO b : AtlanSSO.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
