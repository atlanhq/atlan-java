/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Information about a procedure's return type.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class SQLProcedureReturn extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SQLProcedureReturn";

    /** Fixed typeName for SQLProcedureReturn. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Data type of the return value. */
    String sqlReturnType;

    /** Maximum length in characters for string return values. */
    Long sqlReturnCharacterMaximumLength;

    /** Maximum length in bytes for string return values. */
    Long sqlReturnCharacterOctetLength;

    /** Numeric precision for numeric return values. */
    Long sqlReturnNumericPrecision;

    /** Radix of precision for numeric return values. */
    Long sqlReturnNumericPrecisionRadix;

    /**
     * Quickly create a new SQLProcedureReturn.
     * @param sqlReturnType Data type of the return value.
     * @param sqlReturnCharacterMaximumLength Maximum length in characters for string return values.
     * @param sqlReturnCharacterOctetLength Maximum length in bytes for string return values.
     * @param sqlReturnNumericPrecision Numeric precision for numeric return values.
     * @param sqlReturnNumericPrecisionRadix Radix of precision for numeric return values.
     * @return a SQLProcedureReturn with the provided information
     */
    public static SQLProcedureReturn of(
            String sqlReturnType,
            Long sqlReturnCharacterMaximumLength,
            Long sqlReturnCharacterOctetLength,
            Long sqlReturnNumericPrecision,
            Long sqlReturnNumericPrecisionRadix) {
        return SQLProcedureReturn.builder()
                .sqlReturnType(sqlReturnType)
                .sqlReturnCharacterMaximumLength(sqlReturnCharacterMaximumLength)
                .sqlReturnCharacterOctetLength(sqlReturnCharacterOctetLength)
                .sqlReturnNumericPrecision(sqlReturnNumericPrecision)
                .sqlReturnNumericPrecisionRadix(sqlReturnNumericPrecisionRadix)
                .build();
    }

    public abstract static class SQLProcedureReturnBuilder<
                    C extends SQLProcedureReturn, B extends SQLProcedureReturnBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
