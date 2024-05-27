/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum WorkflowType implements AtlanEnum {
    DATA_ACCESS("DATA_ACCESS"),
    POLICY("POLICY"),
    CHANGE_MANAGEMENT("CHANGE_MANAGEMENT"),
    PUBLICATION_MANAGEMENT("PUBLICATION_MANAGEMENT"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    WorkflowType(String value) {
        this.value = value;
    }

    public static WorkflowType fromValue(String value) {
        for (WorkflowType b : WorkflowType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
