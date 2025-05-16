/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
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
public class AggregationHitsResult extends AggregationResult {
    private static final long serialVersionUID = 2L;

    /**
     * Details of the hits requested.
     */
    Hits hits;

    @Getter
    @SuppressWarnings("serial")
    public static final class Hits {
        Stats total;

        @JsonProperty("max_score")
        Double maxScore;

        List<Details> hits;
    }

    @Getter
    public static final class Stats {
        /** Number of search results that matched the hit value. */
        Long value;

        /** Comparison operation used to determine whether the values match. */
        String relation;
    }

    @Getter
    @SuppressWarnings("serial")
    public static final class Details {
        /** Elastic index against which the hits were found. */
        @JsonProperty("_index")
        String index;

        /** TBC */
        @JsonProperty("_type")
        String type;

        /** TBC */
        @JsonProperty("_id")
        String id;

        /** TBC */
        @JsonProperty("_score")
        String score;

        /** Actual values for the mapped field(s). */
        @JsonProperty("_source")
        Map<String, Object> source;
    }

    public static class AggregationHitsResultBuilder {}
}
