/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum DataQualitySourceSyncStatus implements AtlanEnum {
    SUCCESSFUL("SUCCESSFUL"),
    FAILURE("FAILURE"),
    IN_PROGRESS("IN_PROGRESS"),
    WAITING_FOR_SCHEDULE("WAITING_FOR_SCHEDULE"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DataQualitySourceSyncStatus(String value) {
        this.value = value;
    }

    public static DataQualitySourceSyncStatus fromValue(String value) {
        for (DataQualitySourceSyncStatus b : DataQualitySourceSyncStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
