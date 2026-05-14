/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum AgentType implements AtlanEnum {
    SYSTEM("SYSTEM"),
    CUSTOM("CUSTOM"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AgentType(String value) {
        this.value = value;
    }

    public static AgentType fromValue(String value) {
        for (AgentType b : AgentType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
