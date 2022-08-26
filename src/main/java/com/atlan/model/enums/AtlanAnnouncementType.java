/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

public enum AtlanAnnouncementType implements AtlanEnum {
    @SerializedName("information")
    INFORMATION("information"),

    @SerializedName("warning")
    WARNING("warning"),

    @SerializedName("issue")
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
