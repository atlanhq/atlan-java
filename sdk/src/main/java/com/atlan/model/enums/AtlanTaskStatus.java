/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AtlanTaskStatus implements AtlanEnum {
    PENDING("PENDING"),
    IN_PROGRESS("IN_PROGRESS"),
    COMPLETE("COMPLETE"),
    FAILED("FAILED"),
    DELETED("DELETED"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanTaskStatus(String value) {
        this.value = value;
    }

    public static AtlanTaskStatus fromValue(String value) {
        for (AtlanTaskStatus b : AtlanTaskStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
