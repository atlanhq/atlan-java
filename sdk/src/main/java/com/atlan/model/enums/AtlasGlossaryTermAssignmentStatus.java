/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum AtlasGlossaryTermAssignmentStatus implements AtlanEnum {
    DISCOVERED("DISCOVERED"),
    PROPOSED("PROPOSED"),
    IMPORTED("IMPORTED"),
    VALIDATED("VALIDATED"),
    DEPRECATED("DEPRECATED"),
    OBSOLETE("OBSOLETE"),
    OTHER("OTHER"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlasGlossaryTermAssignmentStatus(String value) {
        this.value = value;
    }

    public static AtlasGlossaryTermAssignmentStatus fromValue(String value) {
        for (AtlasGlossaryTermAssignmentStatus b : AtlasGlossaryTermAssignmentStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
