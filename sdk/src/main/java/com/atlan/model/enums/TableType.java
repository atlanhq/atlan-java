/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum TableType implements AtlanEnum {
    TEMPORARY("TEMPORARY"),
    ICEBERG("ICEBERG"),
    KUDU("KUDU"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    TableType(String value) {
        this.value = value;
    }

    public static TableType fromValue(String value) {
        for (TableType b : TableType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
