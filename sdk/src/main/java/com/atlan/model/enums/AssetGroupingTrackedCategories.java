/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum AssetGroupingTrackedCategories implements AtlanEnum {
    DESCRIPTION("description"),
    MEANINGS("meanings"),
    CLASSIFICATIONS("classifications"),
    README("readme"),
    OWNERS("owners"),
    CERTIFICATION("certification"),
    DATA_QUALITY_RULES("data-quality-rules"),
    SQL_INSIGHTS("sql-insights"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AssetGroupingTrackedCategories(String value) {
        this.value = value;
    }

    public static AssetGroupingTrackedCategories fromValue(String value) {
        for (AssetGroupingTrackedCategories b : AssetGroupingTrackedCategories.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
