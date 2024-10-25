/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum FivetranConnectorStatus implements AtlanEnum {
    SUCCESSFUL("SUCCESSFUL"),
    FAILURE("FAILURE"),
    FAILURE_WITH_TASK("FAILURE_WITH_TASK"),
    RESCHEDULED("RESCHEDULED"),
    NO_STATUS("NO_STATUS"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    FivetranConnectorStatus(String value) {
        this.value = value;
    }

    public static FivetranConnectorStatus fromValue(String value) {
        for (FivetranConnectorStatus b : FivetranConnectorStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
