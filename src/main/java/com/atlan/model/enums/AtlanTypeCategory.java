/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

public enum AtlanTypeCategory implements AtlanEnum {
    @SerializedName("ENUM")
    ENUM("ENUM"),

    @SerializedName("STRUCT")
    STRUCT("STRUCT"),

    @SerializedName("CLASSIFICATION")
    CLASSIFICATION("CLASSIFICATION"),

    @SerializedName("ENTITY")
    ENTITY("ENTITY"),

    @SerializedName("RELATIONSHIP")
    RELATIONSHIP("RELATIONSHIP"),

    @SerializedName("BUSINESS_METADATA")
    BUSINESS_METADATA("BUSINESS_METADATA");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanTypeCategory(String value) {
        this.value = value;
    }

    public static AtlanTypeCategory fromValue(String value) {
        for (AtlanTypeCategory b : AtlanTypeCategory.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
