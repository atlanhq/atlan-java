/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

/**
 * Captures the results from a bucket aggregation.
 */
@Getter
@Builder
@Jacksonized
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AggregationBucketResult extends AggregationResult {
    private static final long serialVersionUID = 2L;

    /**
     * Maximum number of missing documents when a bucket's count is very large and
     * may exceed the shard size of Elasticsearch. The larger this number, the more approximate
     * the values of the counts in the buckets.
     */
    @JsonProperty("doc_count_error_upper_bound")
    Long docCountErrorUpperBound;

    /**
     * When there are many unique terms, only the top terms are returned. This gives the sum
     * of all document counts across all buckets that are NOT part of the response.
     */
    @JsonProperty("sum_other_doc_count")
    Long sumOtherDocCount;

    /** List of the top buckets that resulted from the aggregation. */
    List<AggregationBucketDetails> buckets;
}
