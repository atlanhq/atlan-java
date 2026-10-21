/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum NotificationState implements AtlanEnum {
    GENERATED("GENERATED"),
    DELIVERED("DELIVERED"),
    ACTIONED("ACTIONED"),
    RESOLVED("RESOLVED"),
    CLOSED("CLOSED"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    NotificationState(String value) {
        this.value = value;
    }

    public static NotificationState fromValue(String value) {
        for (NotificationState b : NotificationState.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
