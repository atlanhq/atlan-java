/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum DataQualityRuleADStatus implements AtlanEnum {
    TRAINING("TRAINING"),
    ACTIVE("ACTIVE"),
    ERROR("ERROR"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DataQualityRuleADStatus(String value) {
        this.value = value;
    }

    public static DataQualityRuleADStatus fromValue(String value) {
        for (DataQualityRuleADStatus b : DataQualityRuleADStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
