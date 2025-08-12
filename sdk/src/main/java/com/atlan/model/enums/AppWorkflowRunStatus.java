/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum AppWorkflowRunStatus implements AtlanEnum {
    SKIPPED("Skipped"),
    PENDING("Pending"),
    RUNNING("Running"),
    SUCCEEDED("Succeeded"),
    FAILED("Failed"),
    ERROR("Error"),
    STOPPED("Stopped"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AppWorkflowRunStatus(String value) {
        this.value = value;
    }

    public static AppWorkflowRunStatus fromValue(String value) {
        for (AppWorkflowRunStatus b : AppWorkflowRunStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
