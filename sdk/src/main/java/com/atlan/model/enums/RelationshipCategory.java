/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * The Relationship category determines the style of relationship around containment and lifecycle. UML terminology is used for the values.
 *
 * ASSOCIATION is a relationship with no containment.
 * COMPOSITION and AGGREGATION are containment relationships.
 *
 * The difference being in the lifecycles of the container and its children. In the COMPOSITION case, the children cannot exist without the container. For AGGREGATION, the life cycles of the container and children are totally independent.
 */
public enum RelationshipCategory implements AtlanEnum {
    ASSOCIATION("ASSOCIATION"),
    AGGREGATION("AGGREGATION"),
    COMPOSITION("COMPOSITION");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    RelationshipCategory(String value) {
        this.value = value;
    }

    public static RelationshipCategory fromValue(String value) {
        for (RelationshipCategory b : RelationshipCategory.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
