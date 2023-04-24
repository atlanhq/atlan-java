/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum GoogleDatastudioAssetType implements AtlanEnum {
    REPORT("REPORT"),
    DATA_SOURCE("DATA_SOURCE"),
;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    GoogleDatastudioAssetType(String value) {
        this.value = value;
    }

    public static GoogleDatastudioAssetType fromValue(String value) {
        for (GoogleDatastudioAssetType b : GoogleDatastudioAssetType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
