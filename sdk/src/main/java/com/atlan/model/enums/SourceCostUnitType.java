/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum SourceCostUnitType implements AtlanEnum {
    CREDITS("Credits"),
    BYTES("bytes"),
    SLOT_MS("slot-ms"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    SourceCostUnitType(String value) {
        this.value = value;
    }

    public static SourceCostUnitType fromValue(String value) {
        for (SourceCostUnitType b : SourceCostUnitType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
