/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.typedefs;

import com.atlan.net.ApiResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

/**
 * Wrapper used for both requests and responses for type definitions.
 */
@Getter
@Jacksonized
@Builder
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class TypeDefResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    /** List of enumeration type definitions. */
    @Singular
    List<EnumDef> enumDefs;

    /** List of struct type definitions. */
    @Singular
    List<StructDef> structDefs;

    /** List of Atlan tag type definitions. */
    @Singular
    @JsonProperty("classificationDefs")
    List<AtlanTagDef> atlanTagDefs;

    /** List of entity type definitions. */
    @Singular
    List<EntityDef> entityDefs;

    /** List of relationship type definitions. */
    @Singular
    List<RelationshipDef> relationshipDefs;

    /** List of custom metadata type definitions. */
    @Singular
    @JsonProperty("businessMetadataDefs")
    List<CustomMetadataDef> customMetadataDefs;
}
