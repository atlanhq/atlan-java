/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AtlanAnnouncementType implements AtlanEnum {
    INFORMATION("information"),
    WARNING("warning"),
    ISSUE("issue");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanAnnouncementType(String value) {
        this.value = value;
    }

    public static AtlanAnnouncementType fromValue(String value) {
        for (AtlanAnnouncementType b : AtlanAnnouncementType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
