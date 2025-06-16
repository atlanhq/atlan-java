/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.model.relations;

import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.GlossaryCategory;
import com.atlan.model.assets.GlossaryTerm;
import com.atlan.model.assets.IGlossaryCategory;
import com.atlan.model.assets.IGlossaryTerm;
import com.atlan.model.enums.AtlasGlossaryTermRelationshipStatus;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.processing.Generated;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Organizes terms into categories. A term may be linked with many categories and a category may have many terms linked to it. This relationship may connect terms and categories both in the same glossary or in different glossaries.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class GlossaryTermCategorization extends RelationshipAttributes {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AtlasGlossaryTermCategorization";

    /** Fixed typeName for GlossaryTermCategorizations. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    String description;

    /** TBC */
    AtlasGlossaryTermRelationshipStatus status;

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getAll() {
        Map<String, Object> map = new HashMap<>();
        if (description != null) {
            map.put("description", description);
        }
        if (status != null) {
            map.put("status", status);
        }
        return map;
    }

    /** Organizes terms into categories. A term may be linked with many categories and a category may have many terms linked to it. This relationship may connect terms and categories both in the same glossary or in different glossaries. */
    @Generated(value = "com.atlan.generators.ModelGeneratorV2")
    @Getter
    @SuperBuilder(toBuilder = true, builderMethodName = "_internal")
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class Category extends GlossaryCategory {
        private static final long serialVersionUID = 2L;

        /** Fixed typeName for GlossaryTermCategorization. */
        @Getter(onMethod_ = {@Override})
        @Builder.Default
        String relationshipType = GlossaryTermCategorization.TYPE_NAME;

        /** Relationship attributes specific to GlossaryTermCategorization. */
        GlossaryTermCategorization relationshipAttributes;
    }

    /** Organizes terms into categories. A term may be linked with many categories and a category may have many terms linked to it. This relationship may connect terms and categories both in the same glossary or in different glossaries. */
    @Generated(value = "com.atlan.generators.ModelGeneratorV2")
    @Getter
    @SuperBuilder(toBuilder = true, builderMethodName = "_internal")
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class Term extends GlossaryTerm {
        private static final long serialVersionUID = 2L;

        /** Fixed typeName for GlossaryTermCategorization. */
        @Getter(onMethod_ = {@Override})
        @Builder.Default
        String relationshipType = GlossaryTermCategorization.TYPE_NAME;

        /** Relationship attributes specific to GlossaryTermCategorization. */
        GlossaryTermCategorization relationshipAttributes;
    }

    public abstract static class GlossaryTermCategorizationBuilder<
                    C extends GlossaryTermCategorization, B extends GlossaryTermCategorizationBuilder<C, B>>
            extends RelationshipAttributes.RelationshipAttributesBuilder<C, B> {

        /**
         * Build the GlossaryTermCategorization relationship (with attributes) into a related object.
         *
         * @param related the related asset to which to build the detailed relationship
         * @param semantic to use for saving the relationship
         * @return a detailed Atlan relationship that conforms to the necessary interface for a related asset
         * @throws InvalidRequestException if the asset provided is without a GUID or qualifiedName
         */
        public IGlossaryCategory category(IGlossaryCategory related, Reference.SaveSemantic semantic)
                throws InvalidRequestException {
            GlossaryTermCategorization attributes = build();
            if (related.getGuid() != null && !related.getGuid().isBlank()) {
                return Category._internal()
                        .guid(related.getGuid())
                        .relationshipAttributes(attributes)
                        .semantic(semantic)
                        .build();
            } else {
                return Category._internal()
                        .uniqueAttributes(UniqueAttributes.builder()
                                .qualifiedName(related.getQualifiedName())
                                .build())
                        .relationshipAttributes(attributes)
                        .semantic(semantic)
                        .build();
            }
        }

        /**
         * Build the GlossaryTermCategorization relationship (with attributes) into a related object.
         *
         * @param related the related asset to which to build the detailed relationship
         * @param semantic to use for saving the relationship
         * @return a detailed Atlan relationship that conforms to the necessary interface for a related asset
         * @throws InvalidRequestException if the asset provided is without a GUID or qualifiedName
         */
        public IGlossaryTerm term(IGlossaryTerm related, Reference.SaveSemantic semantic)
                throws InvalidRequestException {
            GlossaryTermCategorization attributes = build();
            if (related.getGuid() != null && !related.getGuid().isBlank()) {
                return Term._internal()
                        .guid(related.getGuid())
                        .relationshipAttributes(attributes)
                        .semantic(semantic)
                        .build();
            } else {
                return Term._internal()
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
