/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum DataQualityRuleStatus implements AtlanEnum {
    ACTIVE("ACTIVE"),
    SUSPENDED("SUSPENDED"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DataQualityRuleStatus(String value) {
        this.value = value;
    }

    public static DataQualityRuleStatus fromValue(String value) {
        for (DataQualityRuleStatus b : DataQualityRuleStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
