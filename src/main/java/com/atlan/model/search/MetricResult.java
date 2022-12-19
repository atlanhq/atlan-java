/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Captures the results from a metric aggregation.
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class MetricResult extends AggregationResult {
    private static final long serialVersionUID = 2L;

    /** Value of the requested metric aggregation. */
    Double value;
}
