/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.atlan.model.enums.SqlInsightJoinCardinality;
import com.atlan.model.enums.SqlInsightJoinType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import java.util.List;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * A join declared in a Snowflake semantic view, from the logical table holding the foreign keys to the logical table it references.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class SnowflakeSemanticJoin extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SnowflakeSemanticJoin";

    /** Fixed typeName for SnowflakeSemanticJoin. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Name of the relationship in the semantic view. */
    String snowflakeSemanticJoinName;

    /** Unique name of the referenced logical table. */
    String snowflakeSemanticJoinTargetQualifiedName;

    /** Simple name of the referenced logical table. */
    String snowflakeSemanticJoinTargetName;

    /** Column pairs that form the join condition, when it is a set of column equalities. */
    @Singular
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    List<SnowflakeSemanticJoinKey> snowflakeSemanticJoinKeys;

    /** SQL expression that forms the join condition, when it is not a set of column equalities. */
    String snowflakeSemanticJoinExpression;

    /** Type of the join, when the source declares one. */
    SqlInsightJoinType snowflakeSemanticJoinType;

    /** Cardinality of the join, when the source declares one. */
    SqlInsightJoinCardinality snowflakeSemanticJoinCardinality;

    /**
     * Quickly create a new SnowflakeSemanticJoin.
     * @param snowflakeSemanticJoinName Name of the relationship in the semantic view.
     * @param snowflakeSemanticJoinTargetQualifiedName Unique name of the referenced logical table.
     * @param snowflakeSemanticJoinTargetName Simple name of the referenced logical table.
     * @param snowflakeSemanticJoinKeys Column pairs that form the join condition, when it is a set of column equalities.
     * @param snowflakeSemanticJoinExpression SQL expression that forms the join condition, when it is not a set of column equalities.
     * @param snowflakeSemanticJoinType Type of the join, when the source declares one.
     * @param snowflakeSemanticJoinCardinality Cardinality of the join, when the source declares one.
     * @return a SnowflakeSemanticJoin with the provided information
     */
    public static SnowflakeSemanticJoin of(
            String snowflakeSemanticJoinName,
            String snowflakeSemanticJoinTargetQualifiedName,
            String snowflakeSemanticJoinTargetName,
            List<SnowflakeSemanticJoinKey> snowflakeSemanticJoinKeys,
            String snowflakeSemanticJoinExpression,
            SqlInsightJoinType snowflakeSemanticJoinType,
            SqlInsightJoinCardinality snowflakeSemanticJoinCardinality) {
        return SnowflakeSemanticJoin.builder()
                .snowflakeSemanticJoinName(snowflakeSemanticJoinName)
                .snowflakeSemanticJoinTargetQualifiedName(snowflakeSemanticJoinTargetQualifiedName)
                .snowflakeSemanticJoinTargetName(snowflakeSemanticJoinTargetName)
                .snowflakeSemanticJoinKeys(snowflakeSemanticJoinKeys)
                .snowflakeSemanticJoinExpression(snowflakeSemanticJoinExpression)
                .snowflakeSemanticJoinType(snowflakeSemanticJoinType)
                .snowflakeSemanticJoinCardinality(snowflakeSemanticJoinCardinality)
                .build();
    }

    public abstract static class SnowflakeSemanticJoinBuilder<
                    C extends SnowflakeSemanticJoin, B extends SnowflakeSemanticJoinBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
