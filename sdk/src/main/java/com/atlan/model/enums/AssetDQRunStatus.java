/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum AssetDQRunStatus implements AtlanEnum {
    SUCCESSFUL("SUCCESSFUL"),
    FAILURE("FAILURE"),
    IN_PROGRESS("IN_PROGRESS"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    AssetDQRunStatus(String value) {
        this.value = value;
    }

    public static AssetDQRunStatus fromValue(String value) {
        for (AssetDQRunStatus b : AssetDQRunStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
