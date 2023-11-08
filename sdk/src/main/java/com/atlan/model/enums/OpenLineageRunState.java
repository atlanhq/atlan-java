/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum OpenLineageRunState implements AtlanEnum {
    START("START"),
    RUNNING("RUNNING"),
    COMPLETE("COMPLETE"),
    ABORT("ABORT"),
    FAIL("FAIL"),
    OTHER("OTHER"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    OpenLineageRunState(String value) {
        this.value = value;
    }

    public static OpenLineageRunState fromValue(String value) {
        for (OpenLineageRunState b : OpenLineageRunState.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
