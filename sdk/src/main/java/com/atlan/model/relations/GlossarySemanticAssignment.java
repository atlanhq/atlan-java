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
 * Assigns meaning to an asset by linking the term that describes the meaning of the asset. The semantic assignment needs to be a controlled relationship when glossary definitions are used to provide classifications for the data assets and hence define how the data is to be governed.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class GlossarySemanticAssignment extends RelationshipAttributes {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AtlasGlossarySemanticAssignment";

    /** Fixed typeName for GlossarySemanticAssignments. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    Integer confidence;

    /** TBC */
    String createdBy;

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
        if (confidence != null) {
            map.put("confidence", confidence);
        }
        if (createdBy != null) {
            map.put("createdBy", createdBy);
        }
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

    /** Assigns meaning to an asset by linking the term that describes the meaning of the asset. The semantic assignment needs to be a controlled relationship when glossary definitions are used to provide classifications for the data assets and hence define how the data is to be governed. */
    @Generated(value = "com.atlan.generators.ModelGeneratorV2")
    @Getter
    @SuperBuilder(toBuilder = true, builderMethodName = "_internal")
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class AssignedTerm extends GlossaryTerm {
        private static final long serialVersionUID = 2L;

        /** Fixed typeName for GlossarySemanticAssignment. */
        @Getter(onMethod_ = {@Override})
        @Builder.Default
        String relationshipType = GlossarySemanticAssignment.TYPE_NAME;

        /** Relationship attributes specific to GlossarySemanticAssignment. */
        GlossarySemanticAssignment relationshipAttributes;
    }

    public abstract static class GlossarySemanticAssignmentBuilder<
                    C extends GlossarySemanticAssignment, B extends GlossarySemanticAssignmentBuilder<C, B>>
            extends RelationshipAttributes.RelationshipAttributesBuilder<C, B> {

        /**
         * Build the GlossarySemanticAssignment relationship (with attributes) into a related object.
         *
         * @param related the related asset to which to build the detailed relationship
         * @param semantic to use for saving the relationship
         * @return a detailed Atlan relationship that conforms to the necessary interface for a related asset
         * @throws InvalidRequestException if the asset provided is without a GUID or qualifiedName
         */
        public IGlossaryTerm assignedTerm(IGlossaryTerm related, Reference.SaveSemantic semantic)
                throws InvalidRequestException {
            GlossarySemanticAssignment attributes = build();
            if (related.getGuid() != null && !related.getGuid().isBlank()) {
                return AssignedTerm._internal()
                        .guid(related.getGuid())
                        .relationshipAttributes(attributes)
                        .semantic(semantic)
                        .build();
            } else {
                return AssignedTerm._internal()
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
