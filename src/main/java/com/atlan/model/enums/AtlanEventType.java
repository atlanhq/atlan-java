/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AtlanEventType implements AtlanEnum {
    ASSET_CREATE("ENTITY_CREATE"),
    ASSET_UPDATE("ENTITY_UPDATE"),
    ASSET_DELETE("ENTITY_DELETE"),
    CUSTOM_METADATA_UPDATE("BUSINESS_ATTRIBUTE_UPDATE"),
    CLASSIFICATION_ADD("CLASSIFICATION_ADD"),
    CLASSIFICATION_DELETE("CLASSIFICATION_DELETE");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanEventType(String value) {
        this.value = value;
    }

    public static AtlanEventType fromValue(String value) {
        for (AtlanEventType b : AtlanEventType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
