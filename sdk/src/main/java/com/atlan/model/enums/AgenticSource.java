/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum AgenticSource implements AtlanEnum {
    CONTEXT_STUDIO("context_studio"),
    ENRICHMENT_STUDIO("enrichment_studio"),
    MARKETPLACE("marketplace"),
    AUTOMATION_ENGINE("automation_engine"),
    UNKNOWN("unknown"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AgenticSource(String value) {
        this.value = value;
    }

    public static AgenticSource fromValue(String value) {
        for (AgenticSource b : AgenticSource.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
