/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum ADLSStorageKind implements AtlanEnum {
    BLOB("BlobStorage"),
    BLOCK_BLOB("BlockBlobStorage"),
    FILE("FileStorage"),
    STORAGE("Storage"),
    STORAGE_V2("StorageV2");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    ADLSStorageKind(String value) {
        this.value = value;
    }

    public static ADLSStorageKind fromValue(String value) {
        for (ADLSStorageKind b : ADLSStorageKind.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
