/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum DatabricksVolumeType implements AtlanEnum {
    MANAGED("MANAGED"),
    EXTERNAL("EXTERNAL"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DatabricksVolumeType(String value) {
        this.value = value;
    }

    public static DatabricksVolumeType fromValue(String value) {
        for (DatabricksVolumeType b : DatabricksVolumeType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
