/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum DataQualityResult implements AtlanEnum {
    PASS("PASS"),
    FAIL("FAIL"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DataQualityResult(String value) {
        this.value = value;
    }

    public static DataQualityResult fromValue(String value) {
        for (DataQualityResult b : DataQualityResult.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
