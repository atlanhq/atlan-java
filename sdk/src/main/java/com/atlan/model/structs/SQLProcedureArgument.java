/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Information about a procedure argument.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class SQLProcedureArgument extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SQLProcedureArgument";

    /** Fixed typeName for SQLProcedureArgument. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Name of the argument. */
    String sqlArgumentName;

    /** Data type of the argument. */
    String sqlArgumentType;

    /**
     * Quickly create a new SQLProcedureArgument.
     * @param sqlArgumentName Name of the argument.
     * @param sqlArgumentType Data type of the argument.
     * @return a SQLProcedureArgument with the provided information
     */
    public static SQLProcedureArgument of(String sqlArgumentName, String sqlArgumentType) {
        return SQLProcedureArgument.builder()
                .sqlArgumentName(sqlArgumentName)
                .sqlArgumentType(sqlArgumentType)
                .build();
    }

    public abstract static class SQLProcedureArgumentBuilder<
                    C extends SQLProcedureArgument, B extends SQLProcedureArgumentBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
