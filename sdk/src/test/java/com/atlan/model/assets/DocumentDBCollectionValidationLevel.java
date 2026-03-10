/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum DocumentDBCollectionValidationLevel implements AtlanEnum {
    OFF("OFF"),
    STRICT("STRICT"),
    MODERATE("MODERATE"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DocumentDBCollectionValidationLevel(String value) {
        this.value = value;
    }

    public static DocumentDBCollectionValidationLevel fromValue(String value) {
        for (DocumentDBCollectionValidationLevel b : DocumentDBCollectionValidationLevel.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
