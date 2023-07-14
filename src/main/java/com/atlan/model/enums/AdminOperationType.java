/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * Source (keycloak): server-spi-private/src/main/java/org/keycloak/events/admin/OperationType.java
 */
public enum AdminOperationType implements AtlanEnum {
    CREATE("CREATE"),
    UPDATE("UPDATE"),
    DELETE("DELETE"),
    ACTION("ACTION"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AdminOperationType(String value) {
        this.value = value;
    }

    public static AdminOperationType fromValue(String value) {
        for (AdminOperationType b : AdminOperationType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
