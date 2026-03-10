/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum DataQualityRuleTemplateMetricValueType implements AtlanEnum {
    ABSOLUTE("ABSOLUTE"),
    PERCENTAGE("PERCENTAGE"),
    TIME("TIME"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DataQualityRuleTemplateMetricValueType(String value) {
        this.value = value;
    }

    public static DataQualityRuleTemplateMetricValueType fromValue(String value) {
        for (DataQualityRuleTemplateMetricValueType b : DataQualityRuleTemplateMetricValueType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
