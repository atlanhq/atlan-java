/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum PurposeMetadataPolicyAction implements AtlanEnum {
    CREATE("entity-create"),
    READ("entity-read"),
    UPDATE("entity-update"),
    DELETE("entity-delete"),
    UPDATE_CUSTOM_METADATA("entity-update-business-metadata"),
    ADD_ATLAN_TAG("entity-add-classification"),
    REMOVE_ATLAN_TAG("entity-remove-classification");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    PurposeMetadataPolicyAction(String value) {
        this.value = value;
    }

    public static PurposeMetadataPolicyAction fromValue(String value) {
        for (PurposeMetadataPolicyAction b : PurposeMetadataPolicyAction.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
