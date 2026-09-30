/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum ProcessLineageDerivation implements AtlanEnum {
    STATIC("STATIC"),
    RUNTIME("RUNTIME"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    ProcessLineageDerivation(String value) {
        this.value = value;
    }

    public static ProcessLineageDerivation fromValue(String value) {
        for (ProcessLineageDerivation b : ProcessLineageDerivation.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
