/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum QuickSightDatasetFieldType implements AtlanEnum {
    STRING("STRING"),
    INTEGER("INTEGER"),
    DECIMAL("DECIMAL"),
    DATETIME("DATETIME"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    QuickSightDatasetFieldType(String value) {
        this.value = value;
    }

    public static QuickSightDatasetFieldType fromValue(String value) {
        for (QuickSightDatasetFieldType b : QuickSightDatasetFieldType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
