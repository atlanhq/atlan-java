/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum AssetGroupingStrategyType implements AtlanEnum {
    DSL("DSL"),
    SQL("SQL"),
    AE_DAG("AE_DAG"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AssetGroupingStrategyType(String value) {
        this.value = value;
    }

    public static AssetGroupingStrategyType fromValue(String value) {
        for (AssetGroupingStrategyType b : AssetGroupingStrategyType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
