/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum DataProductSensitivity implements AtlanEnum {
    PUBLIC("Public"),
    INTERNAL("Internal"),
    CONFIDENTIAL("Confidential"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DataProductSensitivity(String value) {
        this.value = value;
    }

    public static DataProductSensitivity fromValue(String value) {
        for (DataProductSensitivity b : DataProductSensitivity.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
