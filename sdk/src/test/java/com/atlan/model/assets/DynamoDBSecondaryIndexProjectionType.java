/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum DynamoDBSecondaryIndexProjectionType implements AtlanEnum {
    KEYS_ONLY("KEYS_ONLY"),
    INCLUDE("INCLUDE"),
    ALL("ALL"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DynamoDBSecondaryIndexProjectionType(String value) {
        this.value = value;
    }

    public static DynamoDBSecondaryIndexProjectionType fromValue(String value) {
        for (DynamoDBSecondaryIndexProjectionType b : DynamoDBSecondaryIndexProjectionType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
