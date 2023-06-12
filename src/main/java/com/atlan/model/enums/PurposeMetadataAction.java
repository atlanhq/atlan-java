/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum PurposeMetadataAction implements AtlanEnum, AtlanPolicyAction {
    CREATE("entity-create"),
    READ("entity-read"),
    UPDATE("entity-update"),
    DELETE("entity-delete"),
    UPDATE_CUSTOM_METADATA("entity-update-business-metadata"),
    ADD_ATLAN_TAG("entity-add-classification"),
    READ_ATLAN_TAG("entity-read-classification"),
    UPDATE_ATLAN_TAG("entity-update-classification"),
    REMOVE_ATLAN_TAG("entity-remove-classification");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    PurposeMetadataAction(String value) {
        this.value = value;
    }

    public static PurposeMetadataAction fromValue(String value) {
        for (PurposeMetadataAction b : PurposeMetadataAction.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
