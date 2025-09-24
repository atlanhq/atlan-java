/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum DataQualityScheduleType implements AtlanEnum {
    ON_DATA_CHANGE("ON_DATA_CHANGE"),
    CRON("CRON"),
    NOT_SCHEDULED("NOT_SCHEDULED"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DataQualityScheduleType(String value) {
        this.value = value;
    }

    public static DataQualityScheduleType fromValue(String value) {
        for (DataQualityScheduleType b : DataQualityScheduleType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
