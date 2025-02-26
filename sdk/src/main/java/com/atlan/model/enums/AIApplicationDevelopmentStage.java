/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum AIApplicationDevelopmentStage implements AtlanEnum {
    PROPOSAL("PROPOSAL"),
    DEVELOPMENT("DEVELOPMENT"),
    PRODUCTION("PRODUCTION"),
    ARCHIVED("ARCHIVED"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AIApplicationDevelopmentStage(String value) {
        this.value = value;
    }

    public static AIApplicationDevelopmentStage fromValue(String value) {
        for (AIApplicationDevelopmentStage b : AIApplicationDevelopmentStage.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
