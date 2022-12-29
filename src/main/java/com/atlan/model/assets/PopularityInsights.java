/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.SourceCostUnitType;
import java.util.Comparator;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about an asset's usage or popularity.
 */
@Data
@Jacksonized
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class PopularityInsights extends AtlanObject implements Comparable<PopularityInsights> {

    private static final Comparator<String> stringComparator = Comparator.nullsFirst(String::compareTo);
    private static final Comparator<Long> longComparator = Comparator.nullsFirst(Long::compareTo);
    private static final Comparator<Double> doubleComparator = Comparator.nullsFirst(Double::compareTo);
    private static final Comparator<PopularityInsights> popularityInsightsComparator = Comparator.comparing(
                    PopularityInsights::getRecordUser, stringComparator)
            .thenComparing(PopularityInsights::getRecordQuery, stringComparator)
            .thenComparing(PopularityInsights::getRecordWarehouse, stringComparator)
            .thenComparing(PopularityInsights::getRecordQueryDuration, longComparator)
            .thenComparing(PopularityInsights::getRecordQueryCount, longComparator)
            .thenComparing(PopularityInsights::getRecordTotalUserCount, longComparator)
            .thenComparing(PopularityInsights::getRecordComputeCost, doubleComparator)
            .thenComparing(PopularityInsights::getRecordMaxComputeCost, doubleComparator)
            .thenComparing(PopularityInsights::getRecordLastTimestamp, longComparator);

    /** Username or email of the user who ran the queries. */
    String recordUser;

    /** Query run at source. */
    String recordQuery;

    /** Duration for which the query ran at source. */
    Long recordQueryDuration;

    /** Number of queries run by the user. */
    Long recordQueryCount;

    /** Total number of users who ran queries. */
    Long recordTotalUserCount;

    /** Total compute cost for running all queries. */
    Double recordComputeCost;

    /** Maximum compute cost across all query runs. */
    Double recordMaxComputeCost;

    /** Unit of measure for recordComputeCost. */
    SourceCostUnitType recordComputeCostUnit;

    /** Timestamp of last operation or query run by the user. */
    Long recordLastTimestamp;

    /** Name of the warehouse on which the queries were run. */
    String recordWarehouse;

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(PopularityInsights o) {
        return popularityInsightsComparator.compare(this, o);
    }
}
