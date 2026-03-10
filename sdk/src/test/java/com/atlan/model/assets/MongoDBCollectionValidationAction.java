/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum MongoDBCollectionValidationAction implements AtlanEnum {
    ERROR("ERROR"),
    WARN("WARN"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    MongoDBCollectionValidationAction(String value) {
        this.value = value;
    }

    public static MongoDBCollectionValidationAction fromValue(String value) {
        for (MongoDBCollectionValidationAction b : MongoDBCollectionValidationAction.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
