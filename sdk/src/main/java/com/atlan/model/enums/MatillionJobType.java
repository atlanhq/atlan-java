/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum MatillionJobType implements AtlanEnum {
    ORCHESTRATION("ORCHESTRATION"),
    TRANSFORMATION("TRANSFORMATION"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    MatillionJobType(String value) {
        this.value = value;
    }

    public static MatillionJobType fromValue(String value) {
        for (MatillionJobType b : MatillionJobType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
