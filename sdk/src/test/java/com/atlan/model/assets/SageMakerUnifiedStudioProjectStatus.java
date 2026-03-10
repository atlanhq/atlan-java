/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum SageMakerUnifiedStudioProjectStatus implements AtlanEnum {
    ACTIVE("ACTIVE"),
    DELETING("DELETING"),
    DELETE_FAILED("DELETE_FAILED"),
    UPDATING("UPDATING"),
    UPDATE_FAILED("UPDATE_FAILED"),
    MOVING("MOVING"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    SageMakerUnifiedStudioProjectStatus(String value) {
        this.value = value;
    }

    public static SageMakerUnifiedStudioProjectStatus fromValue(String value) {
        for (SageMakerUnifiedStudioProjectStatus b : SageMakerUnifiedStudioProjectStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
