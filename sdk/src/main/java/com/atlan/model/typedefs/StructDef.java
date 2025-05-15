/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.typedefs;

import com.atlan.model.enums.AtlanTypeCategory;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Structural definition of an object that can be reused in other type definitions.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StructDef extends TypeDef {
    private static final long serialVersionUID = 2L;

    /** Fixed category for struct typedefs. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    AtlanTypeCategory category = AtlanTypeCategory.STRUCT;

    /**
     * Build up a struct definition from the provided parameters and default settings for all other parameters.
     * NOTE: INTERNAL USE ONLY.
     *
     * @param name name of the struct definition
     * @param attributes definitions for each attribute within the struct definition
     * @return a builder for a struct definition
     */
    public static StructDefBuilder<?, ?> creator(String name, List<AttributeDef> attributes) {
        return StructDef.builder()
                .name(name)
                .serviceType("custom_extension")
                .typeVersion("1.0")
                .attributeDefs(attributes);
    }

    public abstract static class StructDefBuilder<C extends StructDef, B extends StructDefBuilder<C, B>>
            extends TypeDef.TypeDefBuilder<C, B> {}
}
