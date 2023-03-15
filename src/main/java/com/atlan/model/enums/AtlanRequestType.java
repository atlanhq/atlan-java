/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AtlanRequestType implements AtlanEnum {
    ATTRIBUTE("attribute"),
    TERM_LINK("term_link"),
    CLASSIFICATION("attach_classification"),
    CUSTOM_METADATA("bm_attribute");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanRequestType(String value) {
        this.value = value;
    }

    public static AtlanRequestType fromValue(String value) {
        for (AtlanRequestType b : AtlanRequestType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
