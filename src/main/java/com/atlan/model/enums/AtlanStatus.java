/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AtlanStatus implements AtlanEnum {
    ACTIVE("ACTIVE"),
    DELETED("DELETED");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanStatus(String value) {
        this.value = value;
    }

    public static AtlanStatus fromValue(String value) {
        for (AtlanStatus b : AtlanStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
