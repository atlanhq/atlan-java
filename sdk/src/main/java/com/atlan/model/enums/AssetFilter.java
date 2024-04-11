/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AssetFilter implements AtlanEnum {
    TERMS("terms"),
    OWNERS("owners"),
    USAGE("usage"),
    TAGS("__traitNames"),
    PROPERTIES("properties"),
    CERTIFICATE("certificateStatus");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AssetFilter(String value) {
        this.value = value;
    }

    public static AssetFilter fromValue(String value) {
        for (AssetFilter b : AssetFilter.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
