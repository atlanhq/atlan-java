/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum EthicalAIAccountabilityConfig implements AtlanEnum {
    HAS_OWNER("HAS_OWNER"),
    SUBJECT_TO_HUMAN_OVERSIGHT("SUBJECT_TO_HUMAN_OVERSIGHT"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    EthicalAIAccountabilityConfig(String value) {
        this.value = value;
    }

    public static EthicalAIAccountabilityConfig fromValue(String value) {
        for (EthicalAIAccountabilityConfig b : EthicalAIAccountabilityConfig.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
