/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum DremioParentAssetType implements AtlanEnum {
    SPACE("SPACE"),
    SOURCE("SOURCE"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DremioParentAssetType(String value) {
        this.value = value;
    }

    public static DremioParentAssetType fromValue(String value) {
        for (DremioParentAssetType b : DremioParentAssetType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
