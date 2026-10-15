/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum WorkflowNotificationAction implements AtlanEnum {
    EDIT_CREDENTIALS("EDIT_CREDENTIALS"),
    RECHECK_NOW("RECHECK_NOW"),
    PAUSE_RUNS("PAUSE_RUNS"),
    REMOVE_SCHEDULE("REMOVE_SCHEDULE"),
    RAISE_TICKET("RAISE_TICKET"),
    DISMISS("DISMISS"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    WorkflowNotificationAction(String value) {
        this.value = value;
    }

    public static WorkflowNotificationAction fromValue(String value) {
        for (WorkflowNotificationAction b : WorkflowNotificationAction.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
