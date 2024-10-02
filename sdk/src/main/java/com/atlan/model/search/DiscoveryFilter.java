/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.search;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Class to compose a single filter criterion for use in a linkable query.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode
@Slf4j
public class DiscoveryFilter {

    /** Attribute (property) against which to filter. */
    String operand;

    /** Operator to use to compare the operand to the value. */
    String operator; // TODO: should be an enumeration

    /** Value to compare the operand against. */
    Object value;

    /** TBC */
    Boolean rawOptions;
}
