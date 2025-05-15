/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.typedefs;

import com.atlan.model.enums.AtlanTypeCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
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
@JsonPropertyOrder({"name", "description", "typeVersion", "serviceType", "superTypes", "attributeDefs"})
@SuppressWarnings("serial")
public class EntityDef extends TypeDef {
    private static final long serialVersionUID = 2L;

    /** Fixed category for entity typedefs. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    AtlanTypeCategory category = AtlanTypeCategory.ENTITY;

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
     * Build up an entity definition from the provided parameters and default settings for all other parameters.
     * NOTE: INTERNAL USE ONLY.
     *
     * @param name name of the entity definition
     * @param attributes definitions for each attribute within the entity definition
     * @return a builder for an entity definition
     */
    public static EntityDefBuilder<?, ?> creator(String name, List<AttributeDef> attributes) {
        return creator(name, attributes, null);
    }

    /**
     * Build up an entity definition from the provided parameters and default settings for all other parameters.
     * NOTE: INTERNAL USE ONLY.
     *
     * @param name name of the entity definition
     * @param attributes definitions for each attribute within the entity definition
     * @param superTypes names of the types that this entity definition should inherit from
     * @return a builder for an entity definition
     */
    public static EntityDefBuilder<?, ?> creator(String name, List<AttributeDef> attributes, List<String> superTypes) {
        if (superTypes == null) {
            superTypes = new ArrayList<>();
        }
        if (superTypes.isEmpty()) {
            superTypes.add("Asset");
        }
        return EntityDef.builder()
                .name(name)
                .serviceType("custom_extension")
                .typeVersion("1.0")
                .attributeDefs(attributes)
                .superTypes(superTypes)
                .category(null);
    }

    public abstract static class EntityDefBuilder<C extends EntityDef, B extends EntityDefBuilder<C, B>>
        extends TypeDef.TypeDefBuilder<C, B> {}
}
