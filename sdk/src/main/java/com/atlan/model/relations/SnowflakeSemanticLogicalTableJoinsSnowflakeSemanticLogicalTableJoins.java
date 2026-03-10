/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.model.relations;

import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.ISnowflakeSemanticLogicalTable;
import com.atlan.model.assets.SnowflakeSemanticLogicalTable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.processing.Generated;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Describing joins between semantic logical tables.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class SnowflakeSemanticLogicalTableJoinsSnowflakeSemanticLogicalTableJoins extends RelationshipAttributes {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "snowflake_semantic_logical_table_joins_snowflake_semantic_logical_table_joins";

    /** Fixed typeName for SnowflakeSemanticLogicalTableJoinsSnowflakeSemanticLogicalTableJoinss. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Columns in the left table used for the join. */
    String snowflakeJoinForeignKeys;

    /** Name of the semantic relationship between logical tables. */
    String snowflakeJoinName;

    /** Columns in the right table used for the join. */
    String snowflakeJoinRefKeys;

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getAll() {
        Map<String, Object> map = new HashMap<>();
        if (snowflakeJoinForeignKeys != null) {
            map.put("snowflakeJoinForeignKeys", snowflakeJoinForeignKeys);
        }
        if (snowflakeJoinName != null) {
            map.put("snowflakeJoinName", snowflakeJoinName);
        }
        if (snowflakeJoinRefKeys != null) {
            map.put("snowflakeJoinRefKeys", snowflakeJoinRefKeys);
        }
        return map;
    }

    /** Describing joins between semantic logical tables. */
    @Generated(value = "com.atlan.generators.ModelGeneratorV2")
    @Getter
    @SuperBuilder(toBuilder = true, builderMethodName = "_internal")
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class SnowflakeSemanticLogicalTableJoin extends SnowflakeSemanticLogicalTable {
        private static final long serialVersionUID = 2L;

        /** Fixed typeName for SnowflakeSemanticLogicalTableJoinsSnowflakeSemanticLogicalTableJoins. */
        @Getter(onMethod_ = {@Override})
        @Builder.Default
        String relationshipType = SnowflakeSemanticLogicalTableJoinsSnowflakeSemanticLogicalTableJoins.TYPE_NAME;

        /** Relationship attributes specific to SnowflakeSemanticLogicalTableJoinsSnowflakeSemanticLogicalTableJoins. */
        SnowflakeSemanticLogicalTableJoinsSnowflakeSemanticLogicalTableJoins relationshipAttributes;
    }

    public abstract static class SnowflakeSemanticLogicalTableJoinsSnowflakeSemanticLogicalTableJoinsBuilder<
                    C extends SnowflakeSemanticLogicalTableJoinsSnowflakeSemanticLogicalTableJoins,
                    B extends SnowflakeSemanticLogicalTableJoinsSnowflakeSemanticLogicalTableJoinsBuilder<C, B>>
            extends RelationshipAttributes.RelationshipAttributesBuilder<C, B> {

        /**
         * Build the SnowflakeSemanticLogicalTableJoinsSnowflakeSemanticLogicalTableJoins relationship (with attributes) into a related object.
         *
         * @param related the related asset to which to build the detailed relationship
         * @param semantic to use for saving the relationship
         * @return a detailed Atlan relationship that conforms to the necessary interface for a related asset
         * @throws InvalidRequestException if the asset provided is without a GUID or qualifiedName
         */
        public ISnowflakeSemanticLogicalTable snowflakeSemanticLogicalTableJoin(ISnowflakeSemanticLogicalTable related, Reference.SaveSemantic semantic)
                throws InvalidRequestException {
            SnowflakeSemanticLogicalTableJoinsSnowflakeSemanticLogicalTableJoins attributes = build();
            if (related.getGuid() != null && !related.getGuid().isBlank()) {
                return SnowflakeSemanticLogicalTableJoin._internal()
                        .guid(related.getGuid())
                        .relationshipAttributes(attributes)
                        .semantic(semantic)
                        .build();
            } else {
                return SnowflakeSemanticLogicalTableJoin._internal()
                        .typeName(related.getTypeName())
                        .uniqueAttributes(UniqueAttributes.builder()
                                .qualifiedName(related.getQualifiedName())
                                .build())
                        .relationshipAttributes(attributes)
                        .semantic(semantic)
                        .build();
            }
        }
    }
}
