/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * Part of a Link asset that determines its uniqueness for idempotency.
 */
public enum LinkIdempotencyInvariant implements AtlanEnum {
    URL("url"),
    NAME("name"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    LinkIdempotencyInvariant(String value) {
        this.value = value;
    }

    public static LinkIdempotencyInvariant fromValue(String value) {
        for (LinkIdempotencyInvariant b : LinkIdempotencyInvariant.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
