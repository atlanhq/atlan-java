/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum AtlanAppWorkflowStatus implements AtlanEnum {
    DRAFT("DRAFT"),
    PUBLISHED("PUBLISHED"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanAppWorkflowStatus(String value) {
        this.value = value;
    }

    public static AtlanAppWorkflowStatus fromValue(String value) {
        for (AtlanAppWorkflowStatus b : AtlanAppWorkflowStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
