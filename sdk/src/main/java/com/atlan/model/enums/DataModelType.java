/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.atlan.model.enums.AtlanEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import javax.annotation.processing.Generated;

@Generated(value="com.atlan.generators.ModelGeneratorV2")
public enum DataModelType implements AtlanEnum {
    CONCEPTUAL_DATA_MODEL("Conceptual Data Model"),
    LOGICAL_DATA_MODEL("Logical Data Model"),
    PHYSICAL_DATA_MODEL("Physical Data Model"),
;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DataModelType(String value) {
        this.value = value;
    }

    public static DataModelType fromValue(String value) {
        for (DataModelType b : DataModelType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
