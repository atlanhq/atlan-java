/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum DataProductLineageStatus implements AtlanEnum {
    COMPLETED("Completed"),
    IN_PROGRESS("InProgress"),
    PENDING("Pending"),
    FAILED("Failed"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DataProductLineageStatus(String value) {
        this.value = value;
    }

    public static DataProductLineageStatus fromValue(String value) {
        for (DataProductLineageStatus b : DataProductLineageStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
