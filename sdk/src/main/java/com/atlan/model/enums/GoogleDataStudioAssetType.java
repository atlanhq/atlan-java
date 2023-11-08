/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum GoogleDataStudioAssetType implements AtlanEnum {
    REPORT("REPORT"),
    DATA_SOURCE("DATA_SOURCE"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    GoogleDataStudioAssetType(String value) {
        this.value = value;
    }

    public static GoogleDataStudioAssetType fromValue(String value) {
        for (GoogleDataStudioAssetType b : GoogleDataStudioAssetType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
