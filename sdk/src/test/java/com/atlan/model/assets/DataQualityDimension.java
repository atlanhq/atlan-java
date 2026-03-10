/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum DataQualityDimension implements AtlanEnum {
    COMPLETENESS("completeness"),
    TIMELINESS("timeliness"),
    ACCURACY("accuracy"),
    CONSISTENCY("consistency"),
    UNIQUENESS("uniqueness"),
    VALIDITY("validity"),
    VOLUME("volume"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DataQualityDimension(String value) {
        this.value = value;
    }

    public static DataQualityDimension fromValue(String value) {
        for (DataQualityDimension b : DataQualityDimension.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
