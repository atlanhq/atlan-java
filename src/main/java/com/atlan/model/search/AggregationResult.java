/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import com.atlan.model.core.AtlanObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Captures the results from a single aggregation.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AggregationResult extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** TBC */
    @JsonProperty("doc_count_error_upper_bound")
    Long docCountErrorUpperBound;

    /** TBC */
    @JsonProperty("sum_other_doc_count")
    Long sumOtherDocCount;

    /** List of buckets that resulted from the aggregation. */
    List<AggregationBucket> buckets;
}
