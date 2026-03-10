/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum DataProductStatus implements AtlanEnum {
    ACTIVE("Active"),
    SUNSET("Sunset"),
    ARCHIVED("Archived"),
    DRAFT("Draft"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DataProductStatus(String value) {
        this.value = value;
    }

    public static DataProductStatus fromValue(String value) {
        for (DataProductStatus b : DataProductStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
