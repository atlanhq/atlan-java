/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum ADLSObjectType implements AtlanEnum {
    BLOCK_BLOB("BlockBlob"),
    PAGE_BLOB("PageBlob"),
    APPEND_BLOB("AppendBlob"),
;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    ADLSObjectType(String value) {
        this.value = value;
    }

    public static ADLSObjectType fromValue(String value) {
        for (ADLSObjectType b : ADLSObjectType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
