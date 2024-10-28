/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum CustomTemperatureType implements AtlanEnum {
    COLD("COLD"),
    HOT("HOT"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    CustomTemperatureType(String value) {
        this.value = value;
    }

    public static CustomTemperatureType fromValue(String value) {
        for (CustomTemperatureType b : CustomTemperatureType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
