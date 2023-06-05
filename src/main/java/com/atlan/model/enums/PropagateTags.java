/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * PropagateTags indicates whether tags should propagate across the relationship instance.
 *
 * Tags can propagate:
 *
 * NONE - not at all
 * ONE_TO_TWO - from end 1 to 2
 * TWO_TO_ONE - from end 2 to 1
 * BOTH - both ways
 *
 * Care needs to be taken when specifying. The use cases we are aware of where this flag is useful:
 *
 * - propagating confidentiality Atlan tags from a table to columns - ONE_TO_TWO could be used here
 * - propagating Atlan tags around Glossary synonyms - BOTH could be used here.
 *
 * There is an expectation that further enhancements will allow more granular control of tag propagation and will address how to resolve conflicts.
 */
public enum PropagateTags implements AtlanEnum {
    NONE("NONE"),
    ONE_TO_TWO("ONE_TO_TWO"),
    TWO_TO_ONE("TWO_TO_ONE"),
    BOTH("BOTH");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    PropagateTags(String value) {
        this.value = value;
    }

    public static PropagateTags fromValue(String value) {
        for (PropagateTags b : PropagateTags.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
