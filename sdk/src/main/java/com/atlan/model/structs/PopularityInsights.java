/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.atlan.model.enums.SourceCostUnitType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about an asset's usage or popularity based on aggregated queries.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class PopularityInsights extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "PopularityInsights";

    /** Fixed typeName for PopularityInsights. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

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
     * Quickly create a new PopularityInsights.
     * @param recordUser Username or email of the user who ran the queries.
     * @param recordQuery Query run at source.
     * @param recordQueryDuration Duration for which the query ran at source.
     * @param recordQueryCount Number of queries run by the user.
     * @param recordTotalUserCount Total number of users who ran queries.
     * @param recordComputeCost Total compute cost for running all queries.
     * @param recordMaxComputeCost Maximum compute cost across all query runs.
     * @param recordComputeCostUnit Unit of measure for recordComputeCost.
     * @param recordLastTimestamp Timestamp of last operation or query run by the user.
     * @param recordWarehouse Name of the warehouse on which the queries were run.
     * @return a PopularityInsights with the provided information
     */
    public static PopularityInsights of(
            String recordUser,
            String recordQuery,
            Long recordQueryDuration,
            Long recordQueryCount,
            Long recordTotalUserCount,
            Double recordComputeCost,
            Double recordMaxComputeCost,
            SourceCostUnitType recordComputeCostUnit,
            Long recordLastTimestamp,
            String recordWarehouse) {
        return PopularityInsights.builder()
                .recordUser(recordUser)
                .recordQuery(recordQuery)
                .recordQueryDuration(recordQueryDuration)
                .recordQueryCount(recordQueryCount)
                .recordTotalUserCount(recordTotalUserCount)
                .recordComputeCost(recordComputeCost)
                .recordMaxComputeCost(recordMaxComputeCost)
                .recordComputeCostUnit(recordComputeCostUnit)
                .recordLastTimestamp(recordLastTimestamp)
                .recordWarehouse(recordWarehouse)
                .build();
    }

    public abstract static class PopularityInsightsBuilder<
                    C extends PopularityInsights, B extends PopularityInsightsBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
