/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum DataQualityRuleAlertPriority implements AtlanEnum {
    URGENT("URGENT"),
    NORMAL("NORMAL"),
    LOW("LOW"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DataQualityRuleAlertPriority(String value) {
        this.value = value;
    }

    public static DataQualityRuleAlertPriority fromValue(String value) {
        for (DataQualityRuleAlertPriority b : DataQualityRuleAlertPriority.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
