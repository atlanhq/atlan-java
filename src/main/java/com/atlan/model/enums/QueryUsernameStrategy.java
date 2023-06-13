/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum QueryUsernameStrategy implements AtlanEnum {
    CONNECTION_USERNAME("connectionUsername"),
    ATLAN_USERNAME("atlanUsername"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    QueryUsernameStrategy(String value) {
        this.value = value;
    }

    public static QueryUsernameStrategy fromValue(String value) {
        for (QueryUsernameStrategy b : QueryUsernameStrategy.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
