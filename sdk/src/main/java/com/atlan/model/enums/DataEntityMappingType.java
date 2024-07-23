/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.atlan.model.enums.AtlanEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import javax.annotation.processing.Generated;

@Generated(value="com.atlan.generators.ModelGeneratorV2")
public enum DataEntityMappingType implements AtlanEnum {
    LOGICAL_TO_CONCEPTUAL("Logical to Conceptual"),
    PHYSICAL_TO_LOGICAL("Physical to Logical"),
;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DataEntityMappingType(String value) {
        this.value = value;
    }

    public static DataEntityMappingType fromValue(String value) {
        for (DataEntityMappingType b : DataEntityMappingType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
