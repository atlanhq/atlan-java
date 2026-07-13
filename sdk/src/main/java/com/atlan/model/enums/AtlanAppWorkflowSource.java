/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum AtlanAppWorkflowSource implements AtlanEnum {
    CONNECTOR("connector"),
    ENRICHMENT_STUDIO("enrichment_studio"),
    BACKGROUND_JOB("background_job"),
    SYSTEM_JOB("system_job"),
    UNKNOWN("unknown"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AtlanAppWorkflowSource(String value) {
        this.value = value;
    }

    public static AtlanAppWorkflowSource fromValue(String value) {
        for (AtlanAppWorkflowSource b : AtlanAppWorkflowSource.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
