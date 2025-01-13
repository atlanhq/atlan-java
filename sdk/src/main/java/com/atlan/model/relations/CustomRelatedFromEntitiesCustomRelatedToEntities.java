/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.model.relations;

import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.CustomEntity;
import com.atlan.model.assets.ICustomEntity;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.processing.Generated;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Inter-relationship between two custom assets.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class CustomRelatedFromEntitiesCustomRelatedToEntities extends RelationshipAttributes {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "custom_related_from_entities_custom_related_to_entities";

    /** Fixed typeName for CustomRelatedFromEntitiesCustomRelatedToEntitiess. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    String customEntityFromLabel;

    /** TBC */
    String customEntityToLabel;

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getAll() {
        Map<String, Object> map = new HashMap<>();
        if (customEntityFromLabel != null) {
            map.put("customEntityFromLabel", customEntityFromLabel);
        }
        if (customEntityToLabel != null) {
            map.put("customEntityToLabel", customEntityToLabel);
        }
        return map;
    }

    /** Inter-relationship between two custom assets. */
    @Generated(value = "com.atlan.generators.ModelGeneratorV2")
    @Getter
    @SuperBuilder(toBuilder = true, builderMethodName = "_internal")
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class CustomRelatedFromEntity extends CustomEntity {
        private static final long serialVersionUID = 2L;

        /** Fixed typeName for CustomRelatedFromEntitiesCustomRelatedToEntities. */
        @Getter(onMethod_ = {@Override})
        @Builder.Default
        String relationshipType = CustomRelatedFromEntitiesCustomRelatedToEntities.TYPE_NAME;

        /** Relationship attributes specific to CustomRelatedFromEntitiesCustomRelatedToEntities. */
        CustomRelatedFromEntitiesCustomRelatedToEntities relationshipAttributes;
    }

    /** Inter-relationship between two custom assets. */
    @Generated(value = "com.atlan.generators.ModelGeneratorV2")
    @Getter
    @SuperBuilder(toBuilder = true, builderMethodName = "_internal")
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class CustomRelatedToEntity extends CustomEntity {
        private static final long serialVersionUID = 2L;

        /** Fixed typeName for CustomRelatedFromEntitiesCustomRelatedToEntities. */
        @Getter(onMethod_ = {@Override})
        @Builder.Default
        String relationshipType = CustomRelatedFromEntitiesCustomRelatedToEntities.TYPE_NAME;

        /** Relationship attributes specific to CustomRelatedFromEntitiesCustomRelatedToEntities. */
        CustomRelatedFromEntitiesCustomRelatedToEntities relationshipAttributes;
    }

    public abstract static class CustomRelatedFromEntitiesCustomRelatedToEntitiesBuilder<
                    C extends CustomRelatedFromEntitiesCustomRelatedToEntities,
                    B extends CustomRelatedFromEntitiesCustomRelatedToEntitiesBuilder<C, B>>
            extends RelationshipAttributes.RelationshipAttributesBuilder<C, B> {

        /**
         * Build the CustomRelatedFromEntitiesCustomRelatedToEntities relationship (with attributes) into a related object.
         *
         * @param related the related asset to which to build the detailed relationship
         * @return a detailed Atlan relationship that conforms to the necessary interface for a related asset
         * @throws InvalidRequestException if the asset provided is without a GUID or qualifiedName
         */
        public ICustomEntity customRelatedFromEntity(ICustomEntity related) throws InvalidRequestException {
            CustomRelatedFromEntitiesCustomRelatedToEntities attributes = build();
            if (related.getGuid() != null && !related.getGuid().isBlank()) {
                return CustomRelatedFromEntity._internal()
                        .guid(related.getGuid())
                        .relationshipAttributes(attributes)
                        .build();
            } else {
                return CustomRelatedFromEntity._internal()
                        .uniqueAttributes(UniqueAttributes.builder()
                                .qualifiedName(related.getQualifiedName())
                                .build())
                        .relationshipAttributes(attributes)
                        .build();
            }
        }

        /**
         * Build the CustomRelatedFromEntitiesCustomRelatedToEntities relationship (with attributes) into a related object.
         *
         * @param related the related asset to which to build the detailed relationship
         * @return a detailed Atlan relationship that conforms to the necessary interface for a related asset
         * @throws InvalidRequestException if the asset provided is without a GUID or qualifiedName
         */
        public ICustomEntity customRelatedToEntity(ICustomEntity related) throws InvalidRequestException {
            CustomRelatedFromEntitiesCustomRelatedToEntities attributes = build();
            if (related.getGuid() != null && !related.getGuid().isBlank()) {
                return CustomRelatedToEntity._internal()
                        .guid(related.getGuid())
                        .relationshipAttributes(attributes)
                        .build();
            } else {
                return CustomRelatedToEntity._internal()
                        .uniqueAttributes(UniqueAttributes.builder()
                                .qualifiedName(related.getQualifiedName())
                                .build())
                        .relationshipAttributes(attributes)
                        .build();
            }
        }
    }
}
