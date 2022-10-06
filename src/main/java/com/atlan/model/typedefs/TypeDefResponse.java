/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.typedefs;

import com.atlan.net.ApiResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Wrapper used for both requests and responses for type definitions.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TypeDefResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    /** List of enumeration type definitions. */
    List<TypeDef> enumDefs;

    /** List of struct type definitions. */
    List<TypeDef> structDefs;

    /** List of classification type definitions. */
    List<ClassificationDef> classificationDefs;

    /** List of entity type definitions. */
    List<EntityDef> entityDefs;

    /** List of relationship type definitions. */
    List<TypeDef> relationshipDefs;

    /** List of custom metadata type definitions. */
    @JsonProperty("businessMetadataDefs")
    List<CustomMetadataDef> customMetadataDefs;
}
