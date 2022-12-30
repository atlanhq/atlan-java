/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.typedefs;

import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.enums.PropagateTags;
import com.atlan.model.enums.RelationshipCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Structural definition of an object that can be reused in other type definitions.
 */
@Getter
@Setter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class StructDef extends TypeDef {
    private static final long serialVersionUID = 2L;

    /** Fixed category for struct typedefs. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    AtlanTypeCategory category = AtlanTypeCategory.STRUCT;
}
