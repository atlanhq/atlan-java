/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AssetFilterGroup implements AtlanEnum {
    TERMS("terms"),
    OWNERS("owners"),
    USAGE("usage"),
    TAGS("__traitNames"),
    PROPERTIES("properties"),
    DOCUMENTS("documents"),
    CERTIFICATE("certificateStatus");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AssetFilterGroup(String value) {
        this.value = value;
    }

    public static AssetFilterGroup fromValue(String value) {
        for (AssetFilterGroup b : AssetFilterGroup.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
