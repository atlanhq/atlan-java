/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum TypeRegistryPropagationType implements AtlanEnum {
    NONE("NONE"),
    ONE_TO_TWO("ONE_TO_TWO"),
    TWO_TO_ONE("TWO_TO_ONE"),
    BOTH("BOTH"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    TypeRegistryPropagationType(String value) {
        this.value = value;
    }

    public static TypeRegistryPropagationType fromValue(String value) {
        for (TypeRegistryPropagationType b : TypeRegistryPropagationType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
