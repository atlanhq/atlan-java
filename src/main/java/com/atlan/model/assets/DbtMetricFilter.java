/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.core.AtlanObject;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about a dbt Metric Filter.
 */
@Getter
@Jacksonized
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class DbtMetricFilter extends AtlanObject {
    /** Unique name of the column the metric filter applies to. */
    String dbtMetricFilterColumnQualifiedName;

    /** TBC. */
    String dbtMetricFilterField;

    /** TBC. */
    String dbtMetricFilterOperator;

    /** TBC. */
    String dbtMetricFilterValue;
}
