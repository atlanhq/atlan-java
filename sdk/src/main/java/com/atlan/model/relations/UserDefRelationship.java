/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.model.relations;

import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.IAsset;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.processing.Generated;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * A generic relationship to hold relationship between any type of asset
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class UserDefRelationship extends RelationshipAttributes {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "UserDefRelationship";

    /** Fixed typeName for UserDefRelationships. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Name for the relationship when referring from endDef2 asset to endDef1 asset */
    String fromTypeLabel;

    /** Name for the relationship when referring from endDef1 asset to endDef2 asset */
    String toTypeLabel;

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getAll() {
        Map<String, Object> map = new HashMap<>();
        if (fromTypeLabel != null) {
            map.put("fromTypeLabel", fromTypeLabel);
        }
        if (toTypeLabel != null) {
            map.put("toTypeLabel", toTypeLabel);
        }
        return map;
    }

    /** A generic relationship to hold relationship between any type of asset */
    @Generated(value = "com.atlan.generators.ModelGeneratorV2")
    @Getter
    @SuperBuilder(toBuilder = true, builderMethodName = "_internal")
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class UserDefRelationshipFrom extends Asset {
        private static final long serialVersionUID = 2L;

        /** Fixed typeName for UserDefRelationship. */
        @Getter(onMethod_ = {@Override})
        @Builder.Default
        String relationshipType = UserDefRelationship.TYPE_NAME;

        /** Relationship attributes specific to UserDefRelationship. */
        UserDefRelationship relationshipAttributes;
    }

    /** A generic relationship to hold relationship between any type of asset */
    @Generated(value = "com.atlan.generators.ModelGeneratorV2")
    @Getter
    @SuperBuilder(toBuilder = true, builderMethodName = "_internal")
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class UserDefRelationshipTo extends Asset {
        private static final long serialVersionUID = 2L;

        /** Fixed typeName for UserDefRelationship. */
        @Getter(onMethod_ = {@Override})
        @Builder.Default
        String relationshipType = UserDefRelationship.TYPE_NAME;

        /** Relationship attributes specific to UserDefRelationship. */
        UserDefRelationship relationshipAttributes;
    }

    public abstract static class UserDefRelationshipBuilder<
                    C extends UserDefRelationship, B extends UserDefRelationshipBuilder<C, B>>
            extends RelationshipAttributes.RelationshipAttributesBuilder<C, B> {

        /**
         * Build the UserDefRelationship relationship (with attributes) into a related object.
         *
         * @param related the related asset to which to build the detailed relationship
         * @param semantic to use for saving the relationship
         * @return a detailed Atlan relationship that conforms to the necessary interface for a related asset
         * @throws InvalidRequestException if the asset provided is without a GUID or qualifiedName
         */
        public IAsset userDefRelationshipFrom(IAsset related, Reference.SaveSemantic semantic)
                throws InvalidRequestException {
            UserDefRelationship attributes = build();
            if (related.getGuid() != null && !related.getGuid().isBlank()) {
                return UserDefRelationshipFrom._internal()
                        .guid(related.getGuid())
                        .relationshipAttributes(attributes)
                        .semantic(semantic)
                        .build();
            } else {
                return UserDefRelationshipFrom._internal()
                        .typeName(related.getTypeName())
                        .uniqueAttributes(UniqueAttributes.builder()
                                .qualifiedName(related.getQualifiedName())
                                .build())
                        .relationshipAttributes(attributes)
                        .semantic(semantic)
                        .build();
            }
        }

        /**
         * Build the UserDefRelationship relationship (with attributes) into a related object.
         *
         * @param related the related asset to which to build the detailed relationship
         * @param semantic to use for saving the relationship
         * @return a detailed Atlan relationship that conforms to the necessary interface for a related asset
         * @throws InvalidRequestException if the asset provided is without a GUID or qualifiedName
         */
        public IAsset userDefRelationshipTo(IAsset related, Reference.SaveSemantic semantic)
                throws InvalidRequestException {
            UserDefRelationship attributes = build();
            if (related.getGuid() != null && !related.getGuid().isBlank()) {
                return UserDefRelationshipTo._internal()
                        .guid(related.getGuid())
                        .relationshipAttributes(attributes)
                        .semantic(semantic)
                        .build();
            } else {
                return UserDefRelationshipTo._internal()
                        .typeName(related.getTypeName())
                        .uniqueAttributes(UniqueAttributes.builder()
                                .qualifiedName(related.getQualifiedName())
                                .build())
                        .relationshipAttributes(attributes)
                        .build();
            }
        }
    }
}
