/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum DMCardinalityType implements AtlanEnum {
    ONE_TO_ONE("ONE-TO-ONE"),
    ONE_TO_MANY("ONE-TO-MANY"),
    MANY_TO_ONE("MANY-TO-ONE"),
    MANY_TO_MANY("MANY-TO-MANY"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DMCardinalityType(String value) {
        this.value = value;
    }

    public static DMCardinalityType fromValue(String value) {
        for (DMCardinalityType b : DMCardinalityType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
