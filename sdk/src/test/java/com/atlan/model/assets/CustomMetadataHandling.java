/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * Custom metadata handling semantics.
 */
public enum CustomMetadataHandling implements AtlanEnum {
    IGNORE("ignore"),
    OVERWRITE("overwrite"),
    MERGE("merge"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    CustomMetadataHandling(String value) {
        this.value = value;
    }

    public static CustomMetadataHandling fromValue(String value) {
        for (CustomMetadataHandling b : CustomMetadataHandling.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
