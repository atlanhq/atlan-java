/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.typedefs;

import com.atlan.model.enums.AtlanCustomAttributeCardinality;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Defines the characteristics of the structure of one end of a relationship.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class RelationshipEndDef extends AttributeDef {
    private static final long serialVersionUID = 2L;

    /** Name of the attribute that represents this end of the relationship. */
    String name;

    /** Description of the relationship from this end of it. */
    String description;

    /** Name of the type definition (entity type) for the other end of the relationship. */
    String type;

    /** Whether this end of the relationship is a container ("owns" the other end's assets). */
    Boolean isContainer;

    /** Unused. */
    Boolean isLegacyAttribute;

    /** Cardinality of this end of the relationship. */
    AtlanCustomAttributeCardinality cardinality;

    /**
     * Builds the minimal object necessary to construct a relationship end definition.
     * NOTE: INTERNAL USE ONLY.
     *
     * @param name of the attribute that represents this end of the relationship
     * @param type name of the entity definition for the other end of the relationship
     * @param isContainer whether this end of the relationship "owns" the other end's assets
     * @param cardinality of this end of the relationship
     * @return builder containing minimal information necessary to define a new relationship end
     */
    public static RelationshipEndDefBuilder<?, ?> creator(
            String name, String type, boolean isContainer, AtlanCustomAttributeCardinality cardinality) {
        return RelationshipEndDef.builder()
                .name(name)
                .type(type)
                .isContainer(isContainer)
                .cardinality(cardinality);
    }
}
