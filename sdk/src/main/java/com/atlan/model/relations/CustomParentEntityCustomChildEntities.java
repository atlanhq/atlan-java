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
 * Containment relationship between two custom entities.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class CustomParentEntityCustomChildEntities extends RelationshipAttributes {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "custom_parent_entity_custom_child_entities";

    /** Fixed typeName for CustomParentEntityCustomChildEntitiess. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    String customEntityChildrenLabel;

    /** TBC */
    String customEntityParentLabel;

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getAll() {
        Map<String, Object> map = new HashMap<>();
        if (customEntityChildrenLabel != null) {
            map.put("customEntityChildrenLabel", customEntityChildrenLabel);
        }
        if (customEntityParentLabel != null) {
            map.put("customEntityParentLabel", customEntityParentLabel);
        }
        return map;
    }

    /** Containment relationship between two custom entities. */
    @Generated(value = "com.atlan.generators.ModelGeneratorV2")
    @Getter
    @SuperBuilder(toBuilder = true, builderMethodName = "_internal")
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class CustomParentEntity extends CustomEntity {
        private static final long serialVersionUID = 2L;

        /** Fixed typeName for CustomParentEntityCustomChildEntities. */
        @Getter(onMethod_ = {@Override})
        @Builder.Default
        String relationshipType = CustomParentEntityCustomChildEntities.TYPE_NAME;

        /** Relationship attributes specific to CustomParentEntityCustomChildEntities. */
        CustomParentEntityCustomChildEntities relationshipAttributes;
    }

    /** Containment relationship between two custom entities. */
    @Generated(value = "com.atlan.generators.ModelGeneratorV2")
    @Getter
    @SuperBuilder(toBuilder = true, builderMethodName = "_internal")
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class CustomChildEntity extends CustomEntity {
        private static final long serialVersionUID = 2L;

        /** Fixed typeName for CustomParentEntityCustomChildEntities. */
        @Getter(onMethod_ = {@Override})
        @Builder.Default
        String relationshipType = CustomParentEntityCustomChildEntities.TYPE_NAME;

        /** Relationship attributes specific to CustomParentEntityCustomChildEntities. */
        CustomParentEntityCustomChildEntities relationshipAttributes;
    }

    public abstract static class CustomParentEntityCustomChildEntitiesBuilder<
                    C extends CustomParentEntityCustomChildEntities,
                    B extends CustomParentEntityCustomChildEntitiesBuilder<C, B>>
            extends RelationshipAttributes.RelationshipAttributesBuilder<C, B> {

        /**
         * Build the CustomParentEntityCustomChildEntities relationship (with attributes) into a related object.
         *
         * @param related the related asset to which to build the detailed relationship
         * @return a detailed Atlan relationship that conforms to the necessary interface for a related asset
         * @throws InvalidRequestException if the asset provided is without a GUID or qualifiedName
         */
        public ICustomEntity customParentEntity(ICustomEntity related) throws InvalidRequestException {
            CustomParentEntityCustomChildEntities attributes = build();
            if (related.getGuid() != null && !related.getGuid().isBlank()) {
                return CustomParentEntity._internal()
                        .guid(related.getGuid())
                        .relationshipAttributes(attributes)
                        .build();
            } else {
                return CustomParentEntity._internal()
                        .uniqueAttributes(UniqueAttributes.builder()
                                .qualifiedName(related.getQualifiedName())
                                .build())
                        .relationshipAttributes(attributes)
                        .build();
            }
        }

        /**
         * Build the CustomParentEntityCustomChildEntities relationship (with attributes) into a related object.
         *
         * @param related the related asset to which to build the detailed relationship
         * @return a detailed Atlan relationship that conforms to the necessary interface for a related asset
         * @throws InvalidRequestException if the asset provided is without a GUID or qualifiedName
         */
        public ICustomEntity customChildEntity(ICustomEntity related) throws InvalidRequestException {
            CustomParentEntityCustomChildEntities attributes = build();
            if (related.getGuid() != null && !related.getGuid().isBlank()) {
                return CustomChildEntity._internal()
                        .guid(related.getGuid())
                        .relationshipAttributes(attributes)
                        .build();
            } else {
                return CustomChildEntity._internal()
                        .uniqueAttributes(UniqueAttributes.builder()
                                .qualifiedName(related.getQualifiedName())
                                .build())
                        .relationshipAttributes(attributes)
                        .build();
            }
        }
    }
}
