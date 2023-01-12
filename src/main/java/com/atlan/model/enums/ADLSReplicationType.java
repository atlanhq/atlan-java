/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum ADLSReplicationType implements AtlanEnum {
    LOCALLY_REDUNDANT("LRS"),
    ZONE_REDUNDANT("ZRS"),
    GEO_REDUNDANT("GRS"),
    GEO_ZONE_REDUNDANT("GZRS"),
    READ_ACCESS_GEO_REDUNDANT("RA-GRS");

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
