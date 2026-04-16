/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.processing.Generated;
import lombok.Getter;

@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum DataMeshDatasetType implements AtlanEnum {
    RAW("Raw"),
    REFINED("Refined"),
    AGGREGATED("Aggregated"),
    ;

    @JsonValue
    @Getter(onMethod_ = {@Override})
    private final String value;

    DataMeshDatasetType(String value) {
        this.value = value;
    }

    public static DataMeshDatasetType fromValue(String value) {
        for (DataMeshDatasetType b : DataMeshDatasetType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
