/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum ADLSReplicationType implements AtlanEnum {
    LRS("LRS"),
    ZRS("ZRS"),
    GRS("GRS"),
    GZRS("GZRS"),
    RA_GRS("RA-GRS"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    ADLSReplicationType(String value) {
        this.value = value;
    }

    public static ADLSReplicationType fromValue(String value) {
        for (ADLSReplicationType b : ADLSReplicationType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
