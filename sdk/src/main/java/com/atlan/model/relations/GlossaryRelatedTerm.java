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
 * Links terms that may also be of interest. It is like a 'see also' link in a dictionary.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class GlossaryRelatedTerm extends RelationshipAttributes {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AtlasGlossaryRelatedTerm";

    /** Fixed typeName for GlossaryRelatedTerms. */
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

    /** Links terms that may also be of interest. It is like a 'see also' link in a dictionary. */
    @Generated(value = "com.atlan.generators.ModelGeneratorV2")
    @Getter
    @SuperBuilder(toBuilder = true, builderMethodName = "_internal")
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class SeeAlso extends GlossaryTerm {
        private static final long serialVersionUID = 2L;

        /** Fixed typeName for GlossaryRelatedTerm. */
        @Getter(onMethod_ = {@Override})
        @Builder.Default
        String relationshipType = GlossaryRelatedTerm.TYPE_NAME;

        /** Relationship attributes specific to GlossaryRelatedTerm. */
        GlossaryRelatedTerm relationshipAttributes;
    }

    public abstract static class GlossaryRelatedTermBuilder<
                    C extends GlossaryRelatedTerm, B extends GlossaryRelatedTermBuilder<C, B>>
            extends RelationshipAttributes.RelationshipAttributesBuilder<C, B> {

        /**
         * Build the GlossaryRelatedTerm relationship (with attributes) into a related object.
         *
         * @param related the related asset to which to build the detailed relationship
         * @param semantic to use for saving the relationship
         * @return a detailed Atlan relationship that conforms to the necessary interface for a related asset
         * @throws InvalidRequestException if the asset provided is without a GUID or qualifiedName
         */
        public IGlossaryTerm seeAlso(IGlossaryTerm related, Reference.SaveSemantic semantic)
                throws InvalidRequestException {
            GlossaryRelatedTerm attributes = build();
            if (related.getGuid() != null && !related.getGuid().isBlank()) {
                return SeeAlso._internal()
                        .guid(related.getGuid())
                        .relationshipAttributes(attributes)
                        .semantic(semantic)
                        .build();
            } else {
                return SeeAlso._internal()
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
