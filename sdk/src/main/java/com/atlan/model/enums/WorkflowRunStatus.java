/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum WorkflowRunStatus implements AtlanEnum {
    PENDING("PENDING"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    SUCCESS("SUCCESS"),
    FAILURE("FAILURE"),
    WITHDRAWN("WITHDRAWN"),
    EXPIRED("EXPIRED"),
    TERMINATED("TERMINATED"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    WorkflowRunStatus(String value) {
        this.value = value;
    }

    public static WorkflowRunStatus fromValue(String value) {
        for (WorkflowRunStatus b : WorkflowRunStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
