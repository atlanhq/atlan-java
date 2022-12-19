/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import com.atlan.model.core.AtlanObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Captures the results of a single bucket within an aggregation.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AggregationBucketDetails extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /**
     * Key of the field representing this bucket of aggregate results, as a string.
     * For example, when the key is a date (numeric) this will be something like "2015-01-01".
     */
    @JsonProperty("key_as_string")
    String keyAsString;

    /**
     * Key of the field representing this bucket of aggregate results.
     * This could be a string, a number (in the case of dates), or an array
     * (in the case of multi-term aggregations).
     */
    Object key;

    /** Number of results that fit within this bucket of the aggregation. */
    @JsonProperty("doc_count")
    Long docCount;

    /** TBC */
    @JsonProperty("max_matching_length")
    Long maxMatchingLength;

    /** End of a range (date, geo, IP, etc), as a number or string. */
    Object to;

    /** End of a range, as a string. */
    @JsonProperty("to_as_string")
    String toAsString;

    /** Start of a range (date, geo, IP, etc), as a number or string. */
    Object from;

    /** Start of a range, as a string. */
    @JsonProperty("from_as_string")
    String fromAsString;
}
