/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum EthicalAIReliabilityAndSafetyConfig implements AtlanEnum {
    LOW("LOW"),
    MODERATE("MODERATE"),
    HIGH("HIGH"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    EthicalAIReliabilityAndSafetyConfig(String value) {
        this.value = value;
    }

    public static EthicalAIReliabilityAndSafetyConfig fromValue(String value) {
        for (EthicalAIReliabilityAndSafetyConfig b : EthicalAIReliabilityAndSafetyConfig.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
