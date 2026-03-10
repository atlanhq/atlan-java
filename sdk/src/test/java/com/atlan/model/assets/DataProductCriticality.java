/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum DataProductCriticality implements AtlanEnum {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DataProductCriticality(String value) {
        this.value = value;
    }

    public static DataProductCriticality fromValue(String value) {
        for (DataProductCriticality b : DataProductCriticality.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
