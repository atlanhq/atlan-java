/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.typedefs;

import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.enums.PropagateTags;
import com.atlan.model.enums.RelationshipCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Structural definition of an entity.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonPropertyOrder({
    "name", "description", "typeVersion", "relationshipCategory", "relationshipLabel",
    "serviceType", "endDef1", "endDef2", "propagateTags", "relationshipAttributeDefs"
})
@SuppressWarnings("serial")
public class RelationshipDef extends TypeDef {
    private static final long serialVersionUID = 2L;

    /** Fixed category for entity typedefs. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    AtlanTypeCategory category = AtlanTypeCategory.RELATIONSHIP;

    /** TBC */
    String relationshipLabel;

    /** Style of relationship in regard to containment and lifecycle. */
    @Builder.Default
    RelationshipCategory relationshipCategory = RelationshipCategory.ASSOCIATION;

    /** Whether Atlan tags should propagate through this relationship, and if so in which direction(s). */
    @Builder.Default
    PropagateTags propagateTags = PropagateTags.NONE;

    /** Definition for the first endpoint of the relationship. */
    RelationshipEndDef endDef1;

    /** Definition for the second endpoint of the relationship. */
    RelationshipEndDef endDef2;

    /** Relationships that can exist for the entity. */
    List<RelationshipAttributeDef> relationshipAttributeDefs;

    /** Subtypes of this entity type. */
    List<String> subTypes;

    /** Supertypes of this entity type. */
    List<String> superTypes;

    /** Unused. */
    @JsonIgnore
    Map<String, Object> businessAttributeDefs;

    /**
     * Build up a relationship definition from the provided parameters and default settings for all other parameters.
     * NOTE: INTERNAL USE ONLY.
     *
     * @param name name of the relationship definition
     * @param end1 definition of the first endpoint of the relationship
     * @param end2 definition of the second endpoint of the relationship
     * @return a builder for a relationship definition
     */
    public static RelationshipDefBuilder<?, ?> creator(String name, RelationshipEndDef end1, RelationshipEndDef end2) {
        return creator(name, end1, end2, Collections.emptyList());
    }

    /**
     * Build up a relationship definition from the provided parameters and default settings for all other parameters.
     * NOTE: INTERNAL USE ONLY.
     *
     * @param name name of the relationship definition
     * @param end1 definition of the first endpoint of the relationship
     * @param end2 definition of the second endpoint of the relationship
     * @param attributes definitions for each attribute within the relationship definition
     * @return a builder for a relationship definition
     */
    public static RelationshipDefBuilder<?, ?> creator(
            String name, RelationshipEndDef end1, RelationshipEndDef end2, List<AttributeDef> attributes) {
        RelationshipCategory categoryToUse = RelationshipCategory.ASSOCIATION;
        if (end1.isContainer || end2.isContainer) {
            categoryToUse = RelationshipCategory.AGGREGATION;
        }
        return RelationshipDef.builder()
                .name(name)
                .serviceType("custom_extension")
                .typeVersion("1.0")
                .endDef1(end1)
                .endDef2(end2)
                .relationshipLabel("__" + end1.type + "." + end1.name)
                .attributeDefs(attributes)
                .relationshipCategory(categoryToUse)
                .category(null);
    }

    public abstract static class RelationshipDefBuilder<
                    C extends RelationshipDef, B extends RelationshipDefBuilder<C, B>>
            extends TypeDef.TypeDefBuilder<C, B> {}
}
