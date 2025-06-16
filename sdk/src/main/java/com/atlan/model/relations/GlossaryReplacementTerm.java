/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.model.relations;

import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.GlossaryTerm;
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
 * Indicates term(s) must be used instead of another. This is stronger version of the PreferredTerm.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class GlossaryReplacementTerm extends RelationshipAttributes {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AtlasGlossaryReplacementTerm";

    /** Fixed typeName for GlossaryReplacementTerms. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    String description;

    /** TBC */
    String expression;

    /** TBC */
    String source;

    /** TBC */
    AtlasGlossaryTermRelationshipStatus status;

    /** TBC */
    String steward;

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getAll() {
        Map<String, Object> map = new HashMap<>();
        if (description != null) {
            map.put("description", description);
        }
        if (expression != null) {
            map.put("expression", expression);
        }
        if (source != null) {
            map.put("source", source);
        }
        if (status != null) {
            map.put("status", status);
        }
        if (steward != null) {
            map.put("steward", steward);
        }
        return map;
    }

    /** Indicates term(s) must be used instead of another. This is stronger version of the PreferredTerm. */
    @Generated(value = "com.atlan.generators.ModelGeneratorV2")
    @Getter
    @SuperBuilder(toBuilder = true, builderMethodName = "_internal")
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class ReplacementTerm extends GlossaryTerm {
        private static final long serialVersionUID = 2L;

        /** Fixed typeName for GlossaryReplacementTerm. */
        @Getter(onMethod_ = {@Override})
        @Builder.Default
        String relationshipType = GlossaryReplacementTerm.TYPE_NAME;

        /** Relationship attributes specific to GlossaryReplacementTerm. */
        GlossaryReplacementTerm relationshipAttributes;
    }

    /** Indicates term(s) must be used instead of another. This is stronger version of the PreferredTerm. */
    @Generated(value = "com.atlan.generators.ModelGeneratorV2")
    @Getter
    @SuperBuilder(toBuilder = true, builderMethodName = "_internal")
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class ReplacedBy extends GlossaryTerm {
        private static final long serialVersionUID = 2L;

        /** Fixed typeName for GlossaryReplacementTerm. */
        @Getter(onMethod_ = {@Override})
        @Builder.Default
        String relationshipType = GlossaryReplacementTerm.TYPE_NAME;

        /** Relationship attributes specific to GlossaryReplacementTerm. */
        GlossaryReplacementTerm relationshipAttributes;
    }

    public abstract static class GlossaryReplacementTermBuilder<
                    C extends GlossaryReplacementTerm, B extends GlossaryReplacementTermBuilder<C, B>>
            extends RelationshipAttributes.RelationshipAttributesBuilder<C, B> {

        /**
         * Build the GlossaryReplacementTerm relationship (with attributes) into a related object.
         *
         * @param related the related asset to which to build the detailed relationship
         * @param semantic to use for saving the relationship
         * @return a detailed Atlan relationship that conforms to the necessary interface for a related asset
         * @throws InvalidRequestException if the asset provided is without a GUID or qualifiedName
         */
        public IGlossaryTerm replacementTerm(IGlossaryTerm related, Reference.SaveSemantic semantic)
                throws InvalidRequestException {
            GlossaryReplacementTerm attributes = build();
            if (related.getGuid() != null && !related.getGuid().isBlank()) {
                return ReplacementTerm._internal()
                        .guid(related.getGuid())
                        .relationshipAttributes(attributes)
                        .semantic(semantic)
                        .build();
            } else {
                return ReplacementTerm._internal()
                        .uniqueAttributes(UniqueAttributes.builder()
                                .qualifiedName(related.getQualifiedName())
                                .build())
                        .relationshipAttributes(attributes)
                        .semantic(semantic)
                        .build();
            }
        }

        /**
         * Build the GlossaryReplacementTerm relationship (with attributes) into a related object.
         *
         * @param related the related asset to which to build the detailed relationship
         * @param semantic to use for saving the relationship
         * @return a detailed Atlan relationship that conforms to the necessary interface for a related asset
         * @throws InvalidRequestException if the asset provided is without a GUID or qualifiedName
         */
        public IGlossaryTerm replacedBy(IGlossaryTerm related, Reference.SaveSemantic semantic)
                throws InvalidRequestException {
            GlossaryReplacementTerm attributes = build();
            if (related.getGuid() != null && !related.getGuid().isBlank()) {
                return ReplacedBy._internal()
                        .guid(related.getGuid())
                        .relationshipAttributes(attributes)
                        .semantic(semantic)
                        .build();
            } else {
                return ReplacedBy._internal()
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
