/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.lineage;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanComparisonOperator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Class used to define how to filter the assets to fetch when retrieving lineage.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class EntityFilter extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Name of the attribute on which filtering should be applied. */
    String attributeName;

    /**
     * Comparison that should be used when checking {@link #attributeName}'s value against
     * the provided {@link #attributeValue}.
     */
    AtlanComparisonOperator operator;

    /** Value that {@link #attributeName}'s value should be compared against. */
    String attributeValue;
}
