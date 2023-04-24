/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum PowerBIEndorsementType implements AtlanEnum {
    PROMOTED("Promoted"),
    CERTIFIED("Certified"),
;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    PowerBIEndorsementType(String value) {
        this.value = value;
    }

    public static PowerBIEndorsementType fromValue(String value) {
        for (PowerBIEndorsementType b : PowerBIEndorsementType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
