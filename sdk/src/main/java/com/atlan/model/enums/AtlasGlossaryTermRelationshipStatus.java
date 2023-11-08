/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum AtlasGlossaryTermRelationshipStatus implements AtlanEnum {
    DRAFT("DRAFT"),
    ACTIVE("ACTIVE"),
    DEPRECATED("DEPRECATED"),
    OBSOLETE("OBSOLETE"),
    OTHER("OTHER"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlasGlossaryTermRelationshipStatus(String value) {
        this.value = value;
    }

    public static AtlasGlossaryTermRelationshipStatus fromValue(String value) {
        for (AtlasGlossaryTermRelationshipStatus b : AtlasGlossaryTermRelationshipStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
