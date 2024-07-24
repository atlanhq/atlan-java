/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum DataAttributeMappingType implements AtlanEnum {
    LOGICAL_TO_CONCEPTUAL("Logical to Conceptual"),
    PHYSICAL_TO_LOGICAL("Physical to Logical"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DataAttributeMappingType(String value) {
        this.value = value;
    }

    public static DataAttributeMappingType fromValue(String value) {
        for (DataAttributeMappingType b : DataAttributeMappingType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
