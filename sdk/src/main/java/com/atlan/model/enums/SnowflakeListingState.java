/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum SnowflakeListingState implements AtlanEnum {
    PUBLISHED("PUBLISHED"),
    DRAFT("DRAFT"),
    UNPUBLISHED("UNPUBLISHED"),
    LIVE("LIVE"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    SnowflakeListingState(String value) {
        this.value = value;
    }

    public static SnowflakeListingState fromValue(String value) {
        for (SnowflakeListingState b : SnowflakeListingState.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
