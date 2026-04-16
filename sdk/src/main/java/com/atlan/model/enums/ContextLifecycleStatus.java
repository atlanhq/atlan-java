/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum ContextLifecycleStatus implements AtlanEnum {
    DRAFT("DRAFT"),
    ACTIVE("ACTIVE"),
    DEPRECATED("DEPRECATED"),
    ARCHIVED("ARCHIVED"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    ContextLifecycleStatus(String value) {
        this.value = value;
    }

    public static ContextLifecycleStatus fromValue(String value) {
        for (ContextLifecycleStatus b : ContextLifecycleStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
