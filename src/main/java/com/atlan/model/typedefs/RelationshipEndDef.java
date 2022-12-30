/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.typedefs;

import com.atlan.model.enums.AtlanCustomAttributeCardinality;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Defines the characteristics of the structure of one end of a relationship.
 */
@Getter
@Setter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
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
}
