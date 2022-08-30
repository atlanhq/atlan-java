/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanComparisonOperator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Class used to define how to filter the assets to fetch when retrieving lineage.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class EntityFilter extends AtlanObject {

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
