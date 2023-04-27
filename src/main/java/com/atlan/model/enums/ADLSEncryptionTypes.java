/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum ADLSEncryptionTypes implements AtlanEnum {
    MICROSOFT_STORAGE("Microsoft.Storage"),
    MICROSOFT_KEYVAULT("Microsoft.Keyvault"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    ADLSEncryptionTypes(String value) {
        this.value = value;
    }

    public static ADLSEncryptionTypes fromValue(String value) {
        for (ADLSEncryptionTypes b : ADLSEncryptionTypes.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
