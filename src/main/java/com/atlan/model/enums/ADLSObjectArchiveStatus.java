/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum ADLSObjectArchiveStatus implements AtlanEnum {
    REHYDRATE_PENDING_TO_HOT("rehydrate-pending-to-hot"),
    REHYDRATE_PENDING_TO_COOL("rehydrate-pending-to-cool"),
;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    ADLSObjectArchiveStatus(String value) {
        this.value = value;
    }

    public static ADLSObjectArchiveStatus fromValue(String value) {
        for (ADLSObjectArchiveStatus b : ADLSObjectArchiveStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
