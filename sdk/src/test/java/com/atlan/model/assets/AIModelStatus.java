/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum AIModelStatus implements AtlanEnum {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AIModelStatus(String value) {
        this.value = value;
    }

    public static AIModelStatus fromValue(String value) {
        for (AIModelStatus b : AIModelStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
