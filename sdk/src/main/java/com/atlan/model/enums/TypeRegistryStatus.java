/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum TypeRegistryStatus implements AtlanEnum {
    DRAFT("DRAFT"),
    EXPERIMENTAL("EXPERIMENTAL"),
    PUBLISHED("PUBLISHED"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    TypeRegistryStatus(String value) {
        this.value = value;
    }

    public static TypeRegistryStatus fromValue(String value) {
        for (TypeRegistryStatus b : TypeRegistryStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
