/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AssetSidebarTab implements AtlanEnum {
    COLUMNS("Columns"),
    OBJECTS("Objects"),
    LINEAGE("Lineage"),
    RELATIONS("Relations"),
    ASSETS("Assets"),
    ACTIVITY("Activity"),
    RESOURCES("Resources"),
    QUERIES("Queries"),
    REQUEST("Request"),
    PROPERTY("Property");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AssetSidebarTab(String value) {
        this.value = value;
    }

    public static AssetSidebarTab fromValue(String value) {
        for (AssetSidebarTab b : AssetSidebarTab.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
