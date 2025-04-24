/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.lineage;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanComparisonOperator;
import com.atlan.model.fields.SearchableField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Class used to define how to filter assets and relationships when fetching lineage.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class LineageFilter extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Field on which filtering should be applied. */
    SearchableField field;

    /**
     * Comparison that should be used when checking {@link #field}'s value against
     * the provided {@link #value}.
     */
    AtlanComparisonOperator operator;

    /** Value that {@link #field}'s value should be compared against. */
    String value;
}
