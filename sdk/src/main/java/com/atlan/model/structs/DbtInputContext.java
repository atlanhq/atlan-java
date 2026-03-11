/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Context for inputs to a dbt process - used for metrics.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class DbtInputContext extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DbtInputContext";

    /** Fixed typeName for DbtInputContext. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Actual name of the input. */
    String dbtInputContextName;

    /** Qualified name of the input. */
    String dbtInputContextQualifiedName;

    /** Type of the input (measure or metric). */
    String dbtInputContextType;

    /** Alias of the input in expressions. */
    String dbtInputContextAlias;

    /** Filter applied to the input. */
    String dbtInputContextFilter;

    /** Time shift configuration for period-over-period comparisons. */
    String dbtInputContextOffsetWindow;

    /** Time grain boundary alignment for comparisons. */
    String dbtInputContextOffsetToGrain;

    /**
     * Quickly create a new DbtInputContext.
     * @param dbtInputContextName Actual name of the input.
     * @param dbtInputContextQualifiedName Qualified name of the input.
     * @param dbtInputContextType Type of the input (measure or metric).
     * @param dbtInputContextAlias Alias of the input in expressions.
     * @param dbtInputContextFilter Filter applied to the input.
     * @param dbtInputContextOffsetWindow Time shift configuration for period-over-period comparisons.
     * @param dbtInputContextOffsetToGrain Time grain boundary alignment for comparisons.
     * @return a DbtInputContext with the provided information
     */
    public static DbtInputContext of(
            String dbtInputContextName,
            String dbtInputContextQualifiedName,
            String dbtInputContextType,
            String dbtInputContextAlias,
            String dbtInputContextFilter,
            String dbtInputContextOffsetWindow,
            String dbtInputContextOffsetToGrain) {
        return DbtInputContext.builder()
                .dbtInputContextName(dbtInputContextName)
                .dbtInputContextQualifiedName(dbtInputContextQualifiedName)
                .dbtInputContextType(dbtInputContextType)
                .dbtInputContextAlias(dbtInputContextAlias)
                .dbtInputContextFilter(dbtInputContextFilter)
                .dbtInputContextOffsetWindow(dbtInputContextOffsetWindow)
                .dbtInputContextOffsetToGrain(dbtInputContextOffsetToGrain)
                .build();
    }

    public abstract static class DbtInputContextBuilder<
                    C extends DbtInputContext, B extends DbtInputContextBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
