/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum QuickSightAnalysisStatus implements AtlanEnum {
    CREATION_IN_PROGRESS("CREATION_IN_PROGRESS"),
    CREATION_SUCCESSFUL("CREATION_SUCCESSFUL"),
    CREATION_FAILED("CREATION_FAILED"),
    UPDATE_IN_PROGRESS("UPDATE_IN_PROGRESS"),
    UPDATE_SUCCESSFUL("UPDATE_SUCCESSFUL"),
    UPDATE_FAILED("UPDATE_FAILED"),
    DELETED("DELETED"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    QuickSightAnalysisStatus(String value) {
        this.value = value;
    }

    public static QuickSightAnalysisStatus fromValue(String value) {
        for (QuickSightAnalysisStatus b : QuickSightAnalysisStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
