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
public class AtlasGlossaryValidValue extends RelationshipAttributes {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AtlasGlossaryValidValue";

    /** Fixed typeName for AtlasGlossaryValidValues. */
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
    public static final class Validvalue extends GlossaryTerm {
        private static final long serialVersionUID = 2L;

        /** Fixed typeName for AtlasGlossaryValidValue. */
        @Getter(onMethod_ = {@Override})
        @Builder.Default
        String relationshipType = AtlasGlossaryValidValue.TYPE_NAME;

        /** Relationship attributes specific to AtlasGlossaryValidValue. */
        AtlasGlossaryValidValue relationshipAttributes;
    }

    /** TBC */
    @Generated(value = "com.atlan.generators.ModelGeneratorV2")
    @Getter
    @SuperBuilder(toBuilder = true, builderMethodName = "_internal")
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class Validvaluesfor extends GlossaryTerm {
        private static final long serialVersionUID = 2L;

        /** Fixed typeName for AtlasGlossaryValidValue. */
        @Getter(onMethod_ = {@Override})
        @Builder.Default
        String relationshipType = AtlasGlossaryValidValue.TYPE_NAME;

        /** Relationship attributes specific to AtlasGlossaryValidValue. */
        AtlasGlossaryValidValue relationshipAttributes;
    }

    public abstract static class AtlasGlossaryValidValueBuilder<
                    C extends AtlasGlossaryValidValue, B extends AtlasGlossaryValidValueBuilder<C, B>>
            extends RelationshipAttributes.RelationshipAttributesBuilder<C, B> {

        /**
         * Build the AtlasGlossaryValidValue relationship (with attributes) into a related object.
         *
         * @param related the related asset to which to build the detailed relationship
         * @return a detailed Atlan relationship that conforms to the necessary interface for a related asset
         * @throws InvalidRequestException if the asset provided is without a GUID or qualifiedName
         */
        public IGlossaryTerm validValue(IGlossaryTerm related) throws InvalidRequestException {
            AtlasGlossaryValidValue attributes = build();
            if (related.getGuid() != null && !related.getGuid().isBlank()) {
                return Validvalue._internal()
                        .guid(related.getGuid())
                        .relationshipAttributes(attributes)
                        .build();
            } else {
                return Validvalue._internal()
                        .uniqueAttributes(UniqueAttributes.builder()
                                .qualifiedName(related.getQualifiedName())
                                .build())
                        .relationshipAttributes(attributes)
                        .build();
            }
        }

        /**
         * Build the AtlasGlossaryValidValue relationship (with attributes) into a related object.
         *
         * @param related the related asset to which to build the detailed relationship
         * @return a detailed Atlan relationship that conforms to the necessary interface for a related asset
         * @throws InvalidRequestException if the asset provided is without a GUID or qualifiedName
         */
        public IGlossaryTerm validValuesFor(IGlossaryTerm related) throws InvalidRequestException {
            AtlasGlossaryValidValue attributes = build();
            if (related.getGuid() != null && !related.getGuid().isBlank()) {
                return Validvaluesfor._internal()
                        .guid(related.getGuid())
                        .relationshipAttributes(attributes)
                        .build();
            } else {
                return Validvaluesfor._internal()
                        .uniqueAttributes(UniqueAttributes.builder()
                                .qualifiedName(related.getQualifiedName())
                                .build())
                        .relationshipAttributes(attributes)
                        .build();
            }
        }
    }
}
