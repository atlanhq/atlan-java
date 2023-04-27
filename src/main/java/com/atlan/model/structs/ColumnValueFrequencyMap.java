/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.atlan.model.core.AtlanObject;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about frequently occuring values for a column.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class ColumnValueFrequencyMap extends AtlanObject {

    /**
     * Quickly create a new ColumnValueFrequencyMap.
     * @param columnValue Value for the column.
     * @param columnValueFrequency Number of records that contain the value.
     * @return a ColumnValueFrequencyMap with the provided information
     */
    public static ColumnValueFrequencyMap of(String columnValue, Long columnValueFrequency) {
        return ColumnValueFrequencyMap.builder()
                .columnValue(columnValue)
                .columnValueFrequency(columnValueFrequency)
                .build();
    }

    /** Value for the column. */
    String columnValue;

    /** Number of records that contain the value. */
    Long columnValueFrequency;
}
