/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum AIDatasetType implements AtlanEnum {
    TRAINING("TRAINING"),
    TESTING("TESTING"),
    INFERENCE("INFERENCE"),
    VALIDATION("VALIDATION"),
    OUTPUT("OUTPUT"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AIDatasetType(String value) {
        this.value = value;
    }

    public static AIDatasetType fromValue(String value) {
        for (AIDatasetType b : AIDatasetType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
