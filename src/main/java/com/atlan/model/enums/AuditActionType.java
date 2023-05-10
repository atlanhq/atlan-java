/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AuditActionType implements AtlanEnum {
    ENTITY_CREATE("ENTITY_CREATE"),
    ENTITY_UPDATE("ENTITY_UPDATE"),
    ENTITY_DELETE("ENTITY_DELETE"),
    CUSTOM_METADATA_UPDATE("BUSINESS_ATTRIBUTE_UPDATE"),
    CLASSIFICATION_ADD("CLASSIFICATION_ADD"),
    PROPAGATED_CLASSIFICATION_ADD("PROPAGATED_CLASSIFICATION_ADD"),
    CLASSIFICATION_DELETE("CLASSIFICATION_DELETE"),
    PROPAGATED_CLASSIFICATION_DELETE("PROPAGATED_CLASSIFICATION_DELETE");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AuditActionType(String value) {
        this.value = value;
    }

    public static AuditActionType fromValue(String value) {
        for (AuditActionType b : AuditActionType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
