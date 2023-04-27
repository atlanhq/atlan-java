/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AtlasOperation implements AtlanEnum {
    OTHERS("OTHERS"),
    PURGE("PURGE"),
    EXPORT("EXPORT"),
    IMPORT("IMPORT"),
    IMPORT_DELETE_REPL("IMPORT_DELETE_REPL"),
    TYPE_DEF_CREATE("TYPE_DEF_CREATE"),
    TYPE_DEF_UPDATE("TYPE_DEF_UPDATE"),
    TYPE_DEF_DELETE("TYPE_DEF_DELETE"),
    SERVER_START("SERVER_START"),
    SERVER_STATE_ACTIVE("SERVER_STATE_ACTIVE"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlasOperation(String value) {
        this.value = value;
    }

    public static AtlasOperation fromValue(String value) {
        for (AtlasOperation b : AtlasOperation.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
