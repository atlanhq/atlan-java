/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum BigqueryRoutineType implements AtlanEnum {
    SP("SP"),
    UDF("UDF"),
    TVF("TVF"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    BigqueryRoutineType(String value) {
        this.value = value;
    }

    public static BigqueryRoutineType fromValue(String value) {
        for (BigqueryRoutineType b : BigqueryRoutineType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
