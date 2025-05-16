/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information representing a column value and it's frequency.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class ColumnValueFrequencyMap extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ColumnValueFrequencyMap";

    /** Fixed typeName for ColumnValueFrequencyMap. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Sample value for the column. */
    String columnValue;

    /** Frequency of the sample value in the column. */
    Long columnValueFrequency;

    /**
     * Quickly create a new ColumnValueFrequencyMap.
     * @param columnValue Sample value for the column.
     * @param columnValueFrequency Frequency of the sample value in the column.
     * @return a ColumnValueFrequencyMap with the provided information
     */
    public static ColumnValueFrequencyMap of(String columnValue, Long columnValueFrequency) {
        return ColumnValueFrequencyMap.builder()
                .columnValue(columnValue)
                .columnValueFrequency(columnValueFrequency)
                .build();
    }

    public abstract static class ColumnValueFrequencyMapBuilder<
                    C extends ColumnValueFrequencyMap, B extends ColumnValueFrequencyMapBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
