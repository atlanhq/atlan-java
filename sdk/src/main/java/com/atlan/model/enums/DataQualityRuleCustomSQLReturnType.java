/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum DataQualityRuleCustomSQLReturnType implements AtlanEnum {
    ROW_COUNT("ROW_COUNT"),
    NUMERIC_VALUE("NUMERIC_VALUE"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DataQualityRuleCustomSQLReturnType(String value) {
        this.value = value;
    }

    public static DataQualityRuleCustomSQLReturnType fromValue(String value) {
        for (DataQualityRuleCustomSQLReturnType b : DataQualityRuleCustomSQLReturnType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
