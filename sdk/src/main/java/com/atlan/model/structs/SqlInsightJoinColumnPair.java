/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * A column mapping within a join pattern, pairing a source column to a joined column.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class SqlInsightJoinColumnPair extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SqlInsightJoinColumnPair";

    /** Fixed typeName for SqlInsightJoinColumnPair. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Qualified name of the source column in this join pair. */
    String sqlInsightJoinColumnPairSourceColumnQualifiedName;

    /** Qualified name of the joined column in this join pair. */
    String sqlInsightJoinColumnPairJoinedColumnQualifiedName;

    /**
     * Quickly create a new SqlInsightJoinColumnPair.
     * @param sqlInsightJoinColumnPairSourceColumnQualifiedName Qualified name of the source column in this join pair.
     * @param sqlInsightJoinColumnPairJoinedColumnQualifiedName Qualified name of the joined column in this join pair.
     * @return a SqlInsightJoinColumnPair with the provided information
     */
    public static SqlInsightJoinColumnPair of(
            String sqlInsightJoinColumnPairSourceColumnQualifiedName,
            String sqlInsightJoinColumnPairJoinedColumnQualifiedName) {
        return SqlInsightJoinColumnPair.builder()
                .sqlInsightJoinColumnPairSourceColumnQualifiedName(sqlInsightJoinColumnPairSourceColumnQualifiedName)
                .sqlInsightJoinColumnPairJoinedColumnQualifiedName(sqlInsightJoinColumnPairJoinedColumnQualifiedName)
                .build();
    }

    public abstract static class SqlInsightJoinColumnPairBuilder<
                    C extends SqlInsightJoinColumnPair, B extends SqlInsightJoinColumnPairBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
