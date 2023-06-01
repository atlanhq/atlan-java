/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum PersonaMetadataPolicyAction implements AtlanEnum {
    CREATE("entity-create"),
    READ("entity-read"),
    UPDATE("entity-update"),
    DELETE("entity-delete"),
    LINK_ASSETS("link-assets"),
    UPDATE_CUSTOM_METADATA("entity-update-business-metadata"),
    ADD_ATLAN_TAG("entity-add-classification"),
    REMOVE_ATLAN_TAG("entity-remove-classification"),
    ATTACH_TERMS("add-terms"),
    DETACH_TERMS("remove-terms");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    PersonaMetadataPolicyAction(String value) {
        this.value = value;
    }

    public static PersonaMetadataPolicyAction fromValue(String value) {
        for (PersonaMetadataPolicyAction b : PersonaMetadataPolicyAction.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
