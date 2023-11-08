/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum ADLSLeaseStatus implements AtlanEnum {
    LOCKED("Locked"),
    UNLOCKED("Unlocked"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    ADLSLeaseStatus(String value) {
        this.value = value;
    }

    public static ADLSLeaseStatus fromValue(String value) {
        for (ADLSLeaseStatus b : ADLSLeaseStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
