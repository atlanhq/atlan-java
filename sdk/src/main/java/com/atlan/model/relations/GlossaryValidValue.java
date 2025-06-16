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
 * Terms that represent valid values for another, for example: 'red', 'blue', 'green' could all be valid values for a term 'color'.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class GlossaryValidValue extends RelationshipAttributes {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AtlasGlossaryValidValue";

    /** Fixed typeName for GlossaryValidValues. */
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

    /** Terms that represent valid values for another, for example: 'red', 'blue', 'green' could all be valid values for a term 'color'. */
    @Generated(value = "com.atlan.generators.ModelGeneratorV2")
    @Getter
    @SuperBuilder(toBuilder = true, builderMethodName = "_internal")
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class ValidValue extends GlossaryTerm {
        private static final long serialVersionUID = 2L;

        /** Fixed typeName for GlossaryValidValue. */
        @Getter(onMethod_ = {@Override})
        @Builder.Default
        String relationshipType = GlossaryValidValue.TYPE_NAME;

        /** Relationship attributes specific to GlossaryValidValue. */
        GlossaryValidValue relationshipAttributes;
    }

    /** Terms that represent valid values for another, for example: 'red', 'blue', 'green' could all be valid values for a term 'color'. */
    @Generated(value = "com.atlan.generators.ModelGeneratorV2")
    @Getter
    @SuperBuilder(toBuilder = true, builderMethodName = "_internal")
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class ValidValuesFor extends GlossaryTerm {
        private static final long serialVersionUID = 2L;

        /** Fixed typeName for GlossaryValidValue. */
        @Getter(onMethod_ = {@Override})
        @Builder.Default
        String relationshipType = GlossaryValidValue.TYPE_NAME;

        /** Relationship attributes specific to GlossaryValidValue. */
        GlossaryValidValue relationshipAttributes;
    }

    public abstract static class GlossaryValidValueBuilder<
                    C extends GlossaryValidValue, B extends GlossaryValidValueBuilder<C, B>>
            extends RelationshipAttributes.RelationshipAttributesBuilder<C, B> {

        /**
         * Build the GlossaryValidValue relationship (with attributes) into a related object.
         *
         * @param related the related asset to which to build the detailed relationship
         * @param semantic to use for saving the relationship
         * @return a detailed Atlan relationship that conforms to the necessary interface for a related asset
         * @throws InvalidRequestException if the asset provided is without a GUID or qualifiedName
         */
        public IGlossaryTerm validValue(IGlossaryTerm related, Reference.SaveSemantic semantic)
                throws InvalidRequestException {
            GlossaryValidValue attributes = build();
            if (related.getGuid() != null && !related.getGuid().isBlank()) {
                return ValidValue._internal()
                        .guid(related.getGuid())
                        .relationshipAttributes(attributes)
                        .semantic(semantic)
                        .build();
            } else {
                return ValidValue._internal()
                        .uniqueAttributes(UniqueAttributes.builder()
                                .qualifiedName(related.getQualifiedName())
                                .build())
                        .relationshipAttributes(attributes)
                        .semantic(semantic)
                        .build();
            }
        }

        /**
         * Build the GlossaryValidValue relationship (with attributes) into a related object.
         *
         * @param related the related asset to which to build the detailed relationship
         * @param semantic to use for saving the relationship
         * @return a detailed Atlan relationship that conforms to the necessary interface for a related asset
         * @throws InvalidRequestException if the asset provided is without a GUID or qualifiedName
         */
        public IGlossaryTerm validValuesFor(IGlossaryTerm related, Reference.SaveSemantic semantic)
                throws InvalidRequestException {
            GlossaryValidValue attributes = build();
            if (related.getGuid() != null && !related.getGuid().isBlank()) {
                return ValidValuesFor._internal()
                        .guid(related.getGuid())
                        .relationshipAttributes(attributes)
                        .semantic(semantic)
                        .build();
            } else {
                return ValidValuesFor._internal()
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
