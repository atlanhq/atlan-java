/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum AtlasGlossaryCategoryType implements AtlanEnum {
    DOCUMENT_FOLDER("DOCUMENT_FOLDER"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlasGlossaryCategoryType(String value) {
        this.value = value;
    }

    public static AtlasGlossaryCategoryType fromValue(String value) {
        for (AtlasGlossaryCategoryType b : AtlasGlossaryCategoryType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
