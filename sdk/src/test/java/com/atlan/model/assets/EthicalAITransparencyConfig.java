/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum EthicalAITransparencyConfig implements AtlanEnum {
    LIMITED_DISCLOSURE("LIMITED_DISCLOSURE"),
    PARTIAL_DISCLOSURE("PARTIAL_DISCLOSURE"),
    FULL_DISCLOSURE("FULL_DISCLOSURE"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    EthicalAITransparencyConfig(String value) {
        this.value = value;
    }

    public static EthicalAITransparencyConfig fromValue(String value) {
        for (EthicalAITransparencyConfig b : EthicalAITransparencyConfig.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
