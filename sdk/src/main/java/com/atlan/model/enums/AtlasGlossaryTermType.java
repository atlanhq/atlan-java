/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum AtlasGlossaryTermType implements AtlanEnum {
    DOCUMENT("DOCUMENT"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlasGlossaryTermType(String value) {
        this.value = value;
    }

    public static AtlasGlossaryTermType fromValue(String value) {
        for (AtlasGlossaryTermType b : AtlasGlossaryTermType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
