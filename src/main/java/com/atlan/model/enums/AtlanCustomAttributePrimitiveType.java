/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.enums;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

public enum AtlanCustomAttributePrimitiveType implements AtlanEnum {
    @SerializedName("string")
    STRING("string"),

    @SerializedName("int")
    INTEGER("int"),

    @SerializedName("float")
    DECIMAL("float"),

    @SerializedName("boolean")
    BOOLEAN("boolean"),

    @SerializedName("date")
    DATE("date"),

    @SerializedName("enum")
    OPTIONS("enum"),

    @SerializedName("users")
    USERS("users"),

    @SerializedName("groups")
    GROUPS("groups"),

    @SerializedName("url")
    URL("url"),

    @SerializedName("SQL")
    SQL("SQL");

    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanCustomAttributePrimitiveType(String value) {
        this.value = value;
    }

    public static AtlanCustomAttributePrimitiveType fromValue(String value) {
        for (AtlanCustomAttributePrimitiveType b : AtlanCustomAttributePrimitiveType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
