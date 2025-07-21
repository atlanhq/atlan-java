/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * List of values to be considered valid for a given enumeration.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class TypeRegistryValidValue extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "TypeRegistryValidValue";

    /** Fixed typeName for TypeRegistryValidValue. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** A single valid value. */
    String typeRegistryValue;

    /** Unique numeric value assigned to this single valid value. */
    Long typeRegistryOrdinal;

    /** Explanatory definition of the meaning of the single valid value. */
    String typeRegistryDescription;

    /**
     * Quickly create a new TypeRegistryValidValue.
     * @param typeRegistryValue A single valid value.
     * @param typeRegistryOrdinal Unique numeric value assigned to this single valid value.
     * @param typeRegistryDescription Explanatory definition of the meaning of the single valid value.
     * @return a TypeRegistryValidValue with the provided information
     */
    public static TypeRegistryValidValue of(
            String typeRegistryValue, Long typeRegistryOrdinal, String typeRegistryDescription) {
        return TypeRegistryValidValue.builder()
                .typeRegistryValue(typeRegistryValue)
                .typeRegistryOrdinal(typeRegistryOrdinal)
                .typeRegistryDescription(typeRegistryDescription)
                .build();
    }

    public abstract static class TypeRegistryValidValueBuilder<
                    C extends TypeRegistryValidValue, B extends TypeRegistryValidValueBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
