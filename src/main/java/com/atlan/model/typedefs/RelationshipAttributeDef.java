/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.typedefs;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Defines the structure of a single relationship attribute for a type definition in Atlan.
 */
@Getter
@Setter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class RelationshipAttributeDef extends AttributeDef {
    private static final long serialVersionUID = 2L;

    /** Unused. */
    Boolean isLegacyAttribute;

    /** Name of the relationship type. */
    String relationshipTypeName;
}
