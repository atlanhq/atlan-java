/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum PersonaMetadataAction implements AtlanEnum, AtlanPolicyAction {
    CREATE("persona-api-create"),
    READ("persona-asset-read"),
    UPDATE("persona-asset-update"),
    DELETE("persona-api-delete"),
    UPDATE_CUSTOM_METADATA("persona-business-update-metadata"),
    ADD_ATLAN_TAG("persona-entity-add-classification"),
    UPDATE_ATLAN_TAG("persona-entity-update-classification"),
    REMOVE_ATLAN_TAG("persona-entity-remove-classification"),
    ATTACH_TERMS("persona-add-terms"),
    DETACH_TERMS("persona-remove-terms"),
    DQ_CREATE("persona-dq-create"),
    DQ_READ("persona-dq-read"),
    DQ_UPDATE("persona-dq-update"),
    DQ_DELETE("persona-dq-delete");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    PersonaMetadataAction(String value) {
        this.value = value;
    }

    public static PersonaMetadataAction fromValue(String value) {
        for (PersonaMetadataAction b : PersonaMetadataAction.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
