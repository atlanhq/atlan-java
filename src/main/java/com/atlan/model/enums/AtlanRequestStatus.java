/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AtlanRequestStatus implements AtlanEnum {
    APPROVED("approved"),
    REJECTED("rejected"),
    PENDING("active");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanRequestStatus(String value) {
        this.value = value;
    }

    public static AtlanRequestStatus fromValue(String value) {
        for (AtlanRequestStatus b : AtlanRequestStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
