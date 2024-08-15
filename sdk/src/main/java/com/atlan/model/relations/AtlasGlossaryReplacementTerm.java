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
 * TBC
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class AtlasGlossaryReplacementTerm extends RelationshipAttributes {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AtlasGlossaryReplacementTerm";

    /** Fixed typeName for AtlasGlossaryReplacementTerms. */
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

    /** TBC */
    @Generated(value = "com.atlan.generators.ModelGeneratorV2")
    @Getter
    @SuperBuilder(toBuilder = true, builderMethodName = "_internal")
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class Replacementterm extends GlossaryTerm {
        private static final long serialVersionUID = 2L;

        /** Fixed typeName for AtlasGlossaryReplacementTerm. */
        @Getter(onMethod_ = {@Override})
        @Builder.Default
        String relationshipType = AtlasGlossaryReplacementTerm.TYPE_NAME;

        /** Relationship attributes specific to AtlasGlossaryReplacementTerm. */
        AtlasGlossaryReplacementTerm relationshipAttributes;
    }

    /** TBC */
    @Generated(value = "com.atlan.generators.ModelGeneratorV2")
    @Getter
    @SuperBuilder(toBuilder = true, builderMethodName = "_internal")
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class Replacedby extends GlossaryTerm {
        private static final long serialVersionUID = 2L;

        /** Fixed typeName for AtlasGlossaryReplacementTerm. */
        @Getter(onMethod_ = {@Override})
        @Builder.Default
        String relationshipType = AtlasGlossaryReplacementTerm.TYPE_NAME;

        /** Relationship attributes specific to AtlasGlossaryReplacementTerm. */
        AtlasGlossaryReplacementTerm relationshipAttributes;
    }

    public abstract static class AtlasGlossaryReplacementTermBuilder<
                    C extends AtlasGlossaryReplacementTerm, B extends AtlasGlossaryReplacementTermBuilder<C, B>>
            extends RelationshipAttributes.RelationshipAttributesBuilder<C, B> {

        /**
         * Build the AtlasGlossaryReplacementTerm relationship (with attributes) into a related object.
         *
         * @param related the related asset to which to build the detailed relationship
         * @return a detailed Atlan relationship that conforms to the necessary interface for a related asset
         * @throws InvalidRequestException if the asset provided is without a GUID or qualifiedName
         */
        public IGlossaryTerm replacementTerm(IGlossaryTerm related) throws InvalidRequestException {
            AtlasGlossaryReplacementTerm attributes = build();
            if (related.getGuid() != null && !related.getGuid().isBlank()) {
                return Replacementterm._internal()
                        .guid(related.getGuid())
                        .relationshipAttributes(attributes)
                        .build();
            } else {
                return Replacementterm._internal()
                        .uniqueAttributes(UniqueAttributes.builder()
                                .qualifiedName(related.getQualifiedName())
                                .build())
                        .relationshipAttributes(attributes)
                        .build();
            }
        }

        /**
         * Build the AtlasGlossaryReplacementTerm relationship (with attributes) into a related object.
         *
         * @param related the related asset to which to build the detailed relationship
         * @return a detailed Atlan relationship that conforms to the necessary interface for a related asset
         * @throws InvalidRequestException if the asset provided is without a GUID or qualifiedName
         */
        public IGlossaryTerm replacedBy(IGlossaryTerm related) throws InvalidRequestException {
            AtlasGlossaryReplacementTerm attributes = build();
            if (related.getGuid() != null && !related.getGuid().isBlank()) {
                return Replacedby._internal()
                        .guid(related.getGuid())
                        .relationshipAttributes(attributes)
                        .build();
            } else {
                return Replacedby._internal()
                        .uniqueAttributes(UniqueAttributes.builder()
                                .qualifiedName(related.getQualifiedName())
                                .build())
                        .relationshipAttributes(attributes)
                        .build();
            }
        }
    }
}
