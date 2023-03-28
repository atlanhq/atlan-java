/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum QuickSightFolderType implements AtlanEnum {
    SHARED("SHARED");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    QuickSightFolderType(String value) {
        this.value = value;
    }

    public static QuickSightFolderType fromValue(String value) {
        for (QuickSightFolderType b : QuickSightFolderType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
