/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum EthicalAIPrivacyConfig implements AtlanEnum {
    PERSONAL_DATA("PERSONAL_DATA"),
    NO_PERSONAL_DATA("NO_PERSONAL_DATA"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    EthicalAIPrivacyConfig(String value) {
        this.value = value;
    }

    public static EthicalAIPrivacyConfig fromValue(String value) {
        for (EthicalAIPrivacyConfig b : EthicalAIPrivacyConfig.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
