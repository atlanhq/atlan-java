/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AtlanConnectionCategory implements AtlanEnum {
    WAREHOUSE("warehouse"),
    BI("bi"),
    OBJECT_STORE("ObjectStore");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanConnectionCategory(String value) {
        this.value = value;
    }

    public static AtlanConnectionCategory fromValue(String value) {
        for (AtlanConnectionCategory b : AtlanConnectionCategory.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
