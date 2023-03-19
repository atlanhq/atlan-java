/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.typedefs;

import com.atlan.model.enums.AtlanTypeCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Structural definition of an entity.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
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
}
