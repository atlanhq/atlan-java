/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AtlanTaskType implements AtlanEnum {
    CLASSIFICATION_PROPAGATION_ADD("CLASSIFICATION_PROPAGATION_ADD"),
    CLASSIFICATION_PROPAGATION_DELETE("CLASSIFICATION_PROPAGATION_DELETE"),
    CLASSIFICATION_ONLY_PROPAGATION_DELETE("CLASSIFICATION_ONLY_PROPAGATION_DELETE"),
    CLASSIFICATION_ONLY_PROPAGATION_DELETE_ON_HARD_DELETE("CLASSIFICATION_ONLY_PROPAGATION_DELETE_ON_HARD_DELETE"),
    CLASSIFICATION_REFRESH_PROPAGATION("CLASSIFICATION_REFRESH_PROPAGATION"),
    CLASSIFICATION_PROPAGATION_RELATIONSHIP_UPDATE("CLASSIFICATION_PROPAGATION_RELATIONSHIP_UPDATE"),
    UPDATE_ENTITY_MEANINGS_ON_TERM_UPDATE("UPDATE_ENTITY_MEANINGS_ON_TERM_UPDATE"),
    UPDATE_ENTITY_MEANINGS_ON_TERM_SOFT_DELETE("UPDATE_ENTITY_MEANINGS_ON_TERM_SOFT_DELETE"),
    UPDATE_ENTITY_MEANINGS_ON_TERM_HARD_DELETE("UPDATE_ENTITY_MEANINGS_ON_TERM_HARD_DELETE"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanTaskType(String value) {
        this.value = value;
    }

    public static AtlanTaskType fromValue(String value) {
        for (AtlanTaskType b : AtlanTaskType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
