/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum SqlInsightJoinType implements AtlanEnum {
    INNER("INNER"),
    LEFT("LEFT"),
    RIGHT("RIGHT"),
    FULL("FULL"),
    CROSS("CROSS"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    SqlInsightJoinType(String value) {
        this.value = value;
    }

    public static SqlInsightJoinType fromValue(String value) {
        for (SqlInsightJoinType b : SqlInsightJoinType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
