/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum DataQualityRuleThresholdUnit implements AtlanEnum {
    PERCENTAGE("PERCENTAGE"),
    SECONDS("SECONDS"),
    MINUTES("MINUTES"),
    HOURS("HOURS"),
    DAYS("DAYS"),
    WEEKS("WEEKS"),
    MONTHS("MONTHS"),
    YEARS("YEARS"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DataQualityRuleThresholdUnit(String value) {
        this.value = value;
    }

    public static DataQualityRuleThresholdUnit fromValue(String value) {
        for (DataQualityRuleThresholdUnit b : DataQualityRuleThresholdUnit.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
