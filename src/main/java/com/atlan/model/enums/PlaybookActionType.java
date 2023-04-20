/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum PlaybookActionType implements AtlanEnum {
    METADATA_UPDATE("metadata-update"),
    BULK_CLASSIFICATION("bulk-classification");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    PlaybookActionType(String value) {
        this.value = value;
    }

    public static PlaybookActionType fromValue(String value) {
        for (PlaybookActionType b : PlaybookActionType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
