/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum IncidentSeverity implements AtlanEnum {
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    IncidentSeverity(String value) {
        this.value = value;
    }

    public static IncidentSeverity fromValue(String value) {
        for (IncidentSeverity b : IncidentSeverity.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
