/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.atlan.model.enums.AtlanEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import javax.annotation.processing.Generated;

@Generated(value="com.atlan.generators.ModelGeneratorV2")
public enum RelationshipType implements AtlanEnum {
    ASSOCIATION("Association"),
    GENERALIZATION("Generalization"),
    SUB_TYPE("SubType"),
;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    RelationshipType(String value) {
        this.value = value;
    }

    public static RelationshipType fromValue(String value) {
        for (RelationshipType b : RelationshipType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
