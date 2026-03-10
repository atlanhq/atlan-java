/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum AtlasGlossaryType implements AtlanEnum {
    KNOWLEDGE_HUB("KNOWLEDGE_HUB"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlasGlossaryType(String value) {
        this.value = value;
    }

    public static AtlasGlossaryType fromValue(String value) {
        for (AtlasGlossaryType b : AtlasGlossaryType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
