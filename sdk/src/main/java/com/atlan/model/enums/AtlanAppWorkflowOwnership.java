/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum AtlanAppWorkflowOwnership implements AtlanEnum {
    SYSTEM("SYSTEM"),
    USER("USER"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanAppWorkflowOwnership(String value) {
        this.value = value;
    }

    public static AtlanAppWorkflowOwnership fromValue(String value) {
        for (AtlanAppWorkflowOwnership b : AtlanAppWorkflowOwnership.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
