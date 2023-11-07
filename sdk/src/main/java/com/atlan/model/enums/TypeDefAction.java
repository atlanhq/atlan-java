/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum TypeDefAction implements AtlanEnum, AtlanPolicyAction {
    CREATE("type-create"),
    READ("type-read"),
    UPDATE("type-update"),
    DELETE("type-delete"),
    ADD_LABEL("entity-add-label"),
    REMOVE_LABEL("entity-remove-label"),
    ADD_RELATIONSHIP("add-relationship"),
    UPDATE_RELATIONSHIP("update-relationship"),
    REMOVE_RELATIONSHIP("remove-relationship");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    TypeDefAction(String value) {
        this.value = value;
    }

    public static TypeDefAction fromValue(String value) {
        for (TypeDefAction b : TypeDefAction.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
