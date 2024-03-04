/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum DomoCardType implements AtlanEnum {
    DOC("DOC"),
    DOC_CARD("DOC CARD"),
    CHART("CHART"),
    DRILL_VIEW("DRILL VIEW"),
    NOTEBOOK("NOTEBOOK"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DomoCardType(String value) {
        this.value = value;
    }

    public static DomoCardType fromValue(String value) {
        for (DomoCardType b : DomoCardType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
