/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum ADLSLeaseState implements AtlanEnum {
    AVAILABLE("Available"),
    LEASED("Leased"),
    EXPIRED("Expired"),
    BREAKING("Breaking"),
    BROKEN("Broken"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    ADLSLeaseState(String value) {
        this.value = value;
    }

    public static ADLSLeaseState fromValue(String value) {
        for (ADLSLeaseState b : ADLSLeaseState.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
