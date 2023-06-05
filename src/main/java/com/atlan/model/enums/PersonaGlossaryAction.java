/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum PersonaGlossaryAction implements AtlanEnum, AtlanPolicyAction {
    CREATE("persona-glossary-create"),
    READ("persona-glossary-read"),
    UPDATE("persona-glossary-update"),
    DELETE("persona-glossary-delete"),
    UPDATE_CUSTOM_METADATA("persona-glossary-update-custom-metadata"),
    ADD_ATLAN_TAG("persona-glossary-add-classifications"),
    UPDATE_ATLAN_TAG("persona-glossary-update-classifications"),
    REMOVE_ATLAN_TAG("persona-glossary-delete-classifications");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    PersonaGlossaryAction(String value) {
        this.value = value;
    }

    public static PersonaGlossaryAction fromValue(String value) {
        for (PersonaGlossaryAction b : PersonaGlossaryAction.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
