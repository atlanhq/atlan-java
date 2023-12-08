/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum DynamoDBStatus implements AtlanEnum {
    CREATING("CREATING"),
    UPDATING("UPDATING"),
    DELETING("DELETING"),
    ACTIVE("ACTIVE"),
    INACCESSIBLE_ENCRYPTION_CREDENTIALS("INACCESSIBLE_ENCRYPTION_CREDENTIALS"),
    ARCHIVING("ARCHIVING"),
    ARCHIVED("ARCHIVED"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DynamoDBStatus(String value) {
        this.value = value;
    }

    public static DynamoDBStatus fromValue(String value) {
        for (DynamoDBStatus b : DynamoDBStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
