/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.atlan.model.enums.AtlanEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import javax.annotation.processing.Generated;

@Generated(value="com.atlan.generators.ModelGeneratorV2")
public enum DataGlossary implements AtlanEnum {
    DATA_CONCEPTS("Data Concepts"),
    DATA_ELEMENTS("Data Elements"),
    KYC_TERMS("KYC Terms"),
;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DataGlossary(String value) {
        this.value = value;
    }

    public static DataGlossary fromValue(String value) {
        for (DataGlossary b : DataGlossary.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
