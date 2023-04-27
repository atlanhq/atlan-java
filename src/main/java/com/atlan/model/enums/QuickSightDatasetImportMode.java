/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum QuickSightDatasetImportMode implements AtlanEnum {
    SPICE("SPICE"),
    DIRECT_QUERY("DIRECT_QUERY"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    QuickSightDatasetImportMode(String value) {
        this.value = value;
    }

    public static QuickSightDatasetImportMode fromValue(String value) {
        for (QuickSightDatasetImportMode b : QuickSightDatasetImportMode.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
