/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AtlanWorkflowPhase implements AtlanEnum {
    SUCCESS("Succeeded"),
    RUNNING("Running"),
    FAILED("Failed"),
    ERROR("Error"),
    PENDING("Pending");

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanWorkflowPhase(String value) {
        this.value = value;
    }

    public static AtlanWorkflowPhase fromValue(String value) {
        for (AtlanWorkflowPhase b : AtlanWorkflowPhase.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
