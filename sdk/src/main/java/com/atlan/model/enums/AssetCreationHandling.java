/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * Asset creation vs update semantics.
 */
public enum AssetCreationHandling implements AtlanEnum {
    FULL("upsert"),
    PARTIAL("partial"),
    NONE("update"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AssetCreationHandling(String value) {
        this.value = value;
    }

    public static AssetCreationHandling fromValue(String value) {
        for (AssetCreationHandling b : AssetCreationHandling.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
