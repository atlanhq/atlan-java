/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum AssetGroupingStrategySource implements AtlanEnum {
    SYSTEM("SYSTEM"),
    USER("USER"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AssetGroupingStrategySource(String value) {
        this.value = value;
    }

    public static AssetGroupingStrategySource fromValue(String value) {
        for (AssetGroupingStrategySource b : AssetGroupingStrategySource.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
