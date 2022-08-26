/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

public enum AtlanCustomAttributeCardinality implements AtlanEnum {
    @SerializedName("SINGLE")
    SINGLE("SINGLE"),

    @SerializedName("SET")
    SET("SET");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanCustomAttributeCardinality(String value) {
        this.value = value;
    }

    public static AtlanCustomAttributeCardinality fromValue(String value) {
        for (AtlanCustomAttributeCardinality b : AtlanCustomAttributeCardinality.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
