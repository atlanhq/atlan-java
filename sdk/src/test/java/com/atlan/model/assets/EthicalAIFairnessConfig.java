/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum EthicalAIFairnessConfig implements AtlanEnum {
    LOW_RISK("LOW_RISK"),
    MODERATE_RISK("MODERATE_RISK"),
    HIGH_RISK("HIGH_RISK"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    EthicalAIFairnessConfig(String value) {
        this.value = value;
    }

    public static EthicalAIFairnessConfig fromValue(String value) {
        for (EthicalAIFairnessConfig b : EthicalAIFairnessConfig.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
