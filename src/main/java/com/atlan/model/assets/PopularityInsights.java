/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.SourceCostUnitType;
import com.atlan.serde.PopularityInsightsDeserializer;
import com.atlan.serde.PopularityInsightsSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Detailed information about an asset's usage or popularity.
 */
@Getter
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@JsonDeserialize(using = PopularityInsightsDeserializer.class)
@JsonSerialize(using = PopularityInsightsSerializer.class)
public class PopularityInsights extends AtlanObject {

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
}
