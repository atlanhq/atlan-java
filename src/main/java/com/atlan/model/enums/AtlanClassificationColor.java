/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AtlanClassificationColor implements AtlanEnum {
    GREEN("Green"),
    YELLOW("Yellow"),
    RED("Red");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanClassificationColor(String value) {
        this.value = value;
    }

    public static AtlanClassificationColor fromValue(String value) {
        for (AtlanClassificationColor b : AtlanClassificationColor.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
