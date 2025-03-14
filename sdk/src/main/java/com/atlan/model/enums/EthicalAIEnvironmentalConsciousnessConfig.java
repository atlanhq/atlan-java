/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum EthicalAIEnvironmentalConsciousnessConfig implements AtlanEnum {
    LOW_RISK("LOW_RISK"),
    MEDIUM_RISK("MEDIUM_RISK"),
    HIGH_RISK("HIGH_RISK"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    EthicalAIEnvironmentalConsciousnessConfig(String value) {
        this.value = value;
    }

    public static EthicalAIEnvironmentalConsciousnessConfig fromValue(String value) {
        for (EthicalAIEnvironmentalConsciousnessConfig b : EthicalAIEnvironmentalConsciousnessConfig.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
