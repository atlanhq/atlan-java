/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about a dbt Metric Filter.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class DbtMetricFilter extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DbtMetricFilter";

    /** Fixed typeName for DbtMetricFilter. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Unique name of the column the metric filter applies to. */
    String dbtMetricFilterColumnQualifiedName;

    /** TBC */
    String dbtMetricFilterField;

    /** TBC */
    String dbtMetricFilterOperator;

    /** TBC */
    String dbtMetricFilterValue;

    /**
     * Quickly create a new DbtMetricFilter.
     * @param dbtMetricFilterColumnQualifiedName Unique name of the column the metric filter applies to.
     * @param dbtMetricFilterField TBC
     * @param dbtMetricFilterOperator TBC
     * @param dbtMetricFilterValue TBC
     * @return a DbtMetricFilter with the provided information
     */
    public static DbtMetricFilter of(
            String dbtMetricFilterColumnQualifiedName,
            String dbtMetricFilterField,
            String dbtMetricFilterOperator,
            String dbtMetricFilterValue) {
        return DbtMetricFilter.builder()
                .dbtMetricFilterColumnQualifiedName(dbtMetricFilterColumnQualifiedName)
                .dbtMetricFilterField(dbtMetricFilterField)
                .dbtMetricFilterOperator(dbtMetricFilterOperator)
                .dbtMetricFilterValue(dbtMetricFilterValue)
                .build();
    }

    public abstract static class DbtMetricFilterBuilder<
                    C extends DbtMetricFilter, B extends DbtMetricFilterBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
