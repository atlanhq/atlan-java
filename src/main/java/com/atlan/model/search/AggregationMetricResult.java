/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Captures the results from a metric aggregation.
 */
@Getter
@Builder
@EqualsAndHashCode(callSuper = false)
public class AggregationMetricResult extends AggregationResult {
    private static final long serialVersionUID = 2L;

    /** Value of the requested metric aggregation. */
    Double value;
}
