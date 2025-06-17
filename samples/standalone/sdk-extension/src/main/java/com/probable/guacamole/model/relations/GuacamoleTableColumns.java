/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.probable.guacamole.model.relations;

import com.atlan.exception.InvalidRequestException;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.RelationshipAttributes;
import com.atlan.model.relations.UniqueAttributes;
import com.probable.guacamole.model.assets.GuacamoleColumn;
import com.probable.guacamole.model.assets.GuacamoleTable;
import com.probable.guacamole.model.assets.IGuacamoleColumn;
import com.probable.guacamole.model.assets.IGuacamoleTable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.processing.Generated;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Parent-child relationship between specialized table and its columns.
 */
@Generated(value = "com.probable.guacamole.generators.POJOGenerator")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class GuacamoleTableColumns extends RelationshipAttributes {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "GuacamoleTableColumns";

    /** Fixed typeName for GuacamoleTableColumnss. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Table partition within which the column exists. */
    String guacamolePartition;

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getAll() {
        Map<String, Object> map = new HashMap<>();
        if (guacamolePartition != null) {
            map.put("guacamolePartition", guacamolePartition);
        }
        return map;
    }

    /** Parent-child relationship between specialized table and its columns. */
    @Generated(value = "com.probable.guacamole.generators.POJOGenerator")
    @Getter
    @SuperBuilder(toBuilder = true, builderMethodName = "_internal")
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class GuacamoleTableReln extends GuacamoleTable {
        private static final long serialVersionUID = 2L;

        /** Fixed typeName for GuacamoleTableColumns. */
        @Getter(onMethod_ = {@Override})
        @Builder.Default
        String relationshipType = GuacamoleTableColumns.TYPE_NAME;

        /** Relationship attributes specific to GuacamoleTableColumns. */
        GuacamoleTableColumns relationshipAttributes;
    }

    /** Parent-child relationship between specialized table and its columns. */
    @Generated(value = "com.probable.guacamole.generators.POJOGenerator")
    @Getter
    @SuperBuilder(toBuilder = true, builderMethodName = "_internal")
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class GuacamoleColumnReln extends GuacamoleColumn {
        private static final long serialVersionUID = 2L;

        /** Fixed typeName for GuacamoleTableColumns. */
        @Getter(onMethod_ = {@Override})
        @Builder.Default
        String relationshipType = GuacamoleTableColumns.TYPE_NAME;

        /** Relationship attributes specific to GuacamoleTableColumns. */
        GuacamoleTableColumns relationshipAttributes;
    }

    public abstract static class GuacamoleTableColumnsBuilder<
                    C extends GuacamoleTableColumns, B extends GuacamoleTableColumnsBuilder<C, B>>
            extends RelationshipAttributes.RelationshipAttributesBuilder<C, B> {

        /**
         * Build the GuacamoleTableColumns relationship (with attributes) into a related object.
         *
         * @param related the related asset to which to build the detailed relationship
         * @param semantic to use for saving the relationship
         * @return a detailed Atlan relationship that conforms to the necessary interface for a related asset
         * @throws InvalidRequestException if the asset provided is without a GUID or qualifiedName
         */
        public IGuacamoleTable guacamoleTable(IGuacamoleTable related, Reference.SaveSemantic semantic)
                throws InvalidRequestException {
            GuacamoleTableColumns attributes = build();
            if (related.getGuid() != null && !related.getGuid().isBlank()) {
                return GuacamoleTableReln._internal()
                        .guid(related.getGuid())
                        .relationshipAttributes(attributes)
                        .semantic(semantic)
                        .build();
            } else {
                return GuacamoleTableReln._internal()
                        .uniqueAttributes(UniqueAttributes.builder()
                                .qualifiedName(related.getQualifiedName())
                                .build())
                        .relationshipAttributes(attributes)
                        .semantic(semantic)
                        .build();
            }
        }

        /**
         * Build the GuacamoleTableColumns relationship (with attributes) into a related object.
         *
         * @param related the related asset to which to build the detailed relationship
         * @param semantic to use for saving the relationship
         * @return a detailed Atlan relationship that conforms to the necessary interface for a related asset
         * @throws InvalidRequestException if the asset provided is without a GUID or qualifiedName
         */
        public IGuacamoleColumn guacamoleColumn(IGuacamoleColumn related, Reference.SaveSemantic semantic)
                throws InvalidRequestException {
            GuacamoleTableColumns attributes = build();
            if (related.getGuid() != null && !related.getGuid().isBlank()) {
                return GuacamoleColumnReln._internal()
                        .guid(related.getGuid())
                        .relationshipAttributes(attributes)
                        .semantic(semantic)
                        .build();
            } else {
                return GuacamoleColumnReln._internal()
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
