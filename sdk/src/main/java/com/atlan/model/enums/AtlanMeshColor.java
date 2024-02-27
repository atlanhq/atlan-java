/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AtlanMeshColor implements AtlanEnum {
    INDIGO("#3940E1"),
    VIOLET("#7F83EC"),
    MAGENTA("#F34D77"),
    PINK("#F78BA7"),
    BLUE("#0CD1FA"),
    GREEN("#00A680"),
    YELLOW("#FFB119"),
    GREY("#525C73"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanMeshColor(String value) {
        this.value = value;
    }

    public static AtlanMeshColor fromValue(String value) {
        for (AtlanMeshColor b : AtlanMeshColor.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
