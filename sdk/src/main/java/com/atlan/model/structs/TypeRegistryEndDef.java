/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Definition of one end of a relationship in the metamodel.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class TypeRegistryEndDef extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "TypeRegistryEndDef";

    /** Fixed typeName for TypeRegistryEndDef. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Surrogate key (GUID) of the asset type at this end of the relationship. */
    String typeRegistryType;

    /** How this end of the relationship is labeled or referenced, from the other end of the relationship. */
    String typeRegistryReferredToAs;

    /** Explanatory definition of how this end of the relationship is used or what it represents. */
    String typeRegistryDescription;

    /**
     * Quickly create a new TypeRegistryEndDef.
     * @param typeRegistryType Surrogate key (GUID) of the asset type at this end of the relationship.
     * @param typeRegistryReferredToAs How this end of the relationship is labeled or referenced, from the other end of the relationship.
     * @param typeRegistryDescription Explanatory definition of how this end of the relationship is used or what it represents.
     * @return a TypeRegistryEndDef with the provided information
     */
    public static TypeRegistryEndDef of(
            String typeRegistryType, String typeRegistryReferredToAs, String typeRegistryDescription) {
        return TypeRegistryEndDef.builder()
                .typeRegistryType(typeRegistryType)
                .typeRegistryReferredToAs(typeRegistryReferredToAs)
                .typeRegistryDescription(typeRegistryDescription)
                .build();
    }

    public abstract static class TypeRegistryEndDefBuilder<
                    C extends TypeRegistryEndDef, B extends TypeRegistryEndDefBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
