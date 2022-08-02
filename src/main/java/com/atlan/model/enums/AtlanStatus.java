/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.enums;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

public enum AtlanStatus implements AtlanEnum {
    @SerializedName("ACTIVE")
    ACTIVE("ACTIVE"),

    @SerializedName("DELETED")
    DELETED("DELETED");

    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanStatus(String value) {
        this.value = value;
    }
}
