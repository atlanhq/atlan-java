/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.discovery;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Class to compose a single filter criterion for use in a linkable query.
 */
@Getter
@SuperBuilder(builderMethodName = "_internal")
@EqualsAndHashCode
@Slf4j
public class DiscoveryFilter {

    /** Singular key by which this criterion should be indexed in the filter map. */
    @JsonIgnore
    String filterKey;

    /** Attribute(s) (properties) against which to filter. */
    Object operand;

    /** Operator to use to compare the operand to the value. */
    String operator; // TODO: should be an enumeration

    /** Value to compare the operand against. */
    Object value;

    /** TBC */
    Boolean rawOptions;
}
