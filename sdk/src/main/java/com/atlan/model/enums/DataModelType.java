/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum DataModelType implements AtlanEnum {
    CONCEPTUAL("CONCEPTUAL"),
    LOGICAL("LOGICAL"),
    PHYSICAL("PHYSICAL"),
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
