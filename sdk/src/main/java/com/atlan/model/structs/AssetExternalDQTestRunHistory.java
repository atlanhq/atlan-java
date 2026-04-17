/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import java.util.List;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about the External DQ Test runs and their results.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AssetExternalDQTestRunHistory extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AssetExternalDQTestRunHistory";

    /** Fixed typeName for AssetExternalDQTestRunHistory. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Unique identifier of this DQ Test run on the external DQ tool. */
    String assetExternalDQTestRunId;

    /** Start timestamp of the last DQ Test run for the asset on the external DQ tool. */
    Long assetExternalDQTestRunStartedAt;

    /** End timestamp of the last DQ Test run for the asset on the external DQ tool. */
    Long assetExternalDQTestRunEndedAt;

    /** Overall status of the last DQ Test run for the asset on the external DQ tool. */
    String assetExternalDQTestRunStatus;

    /** DQ score value of this specific test run on the external DQ tool. */
    String assetExternalDQTestRunScoreValue;

    /** DQ score type of this specific test run on the external DQ tool (e.g. numeric). */
    String assetExternalDQTestScoreType;

    /** Total number of rules evaluated in this DQ Test run for the asset on the external DQ tool. */
    Long assetExternalDQTestTotalRulesCount;

    /** Count of the rules passed in last DQ Test run for the asset on the external DQ tool. */
    Long assetExternalDQTestPassedRulesCount;

    /** Count of the rules failed in last DQ Test run for the asset on the external DQ tool. */
    Long assetExternalDQTestFailedRulesCount;

    /** Metric details of each DQ Test run for the asset on the external DQ tool. */
    AssetExternalDQTestMetric assetExternalDQTestMetricInfo;

    /** Detailed breakdown of the score by dimension for the DQ Test run for the asset on the external DQ tool. */
    @Singular
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    List<AssetExternalDQTestScoreDimension> assetExternalDQTestScoreDimensions;

    /** Detailed listing of all the rules evaluated in the DQ Test run for the asset on the external DQ tool. */
    @Singular
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    List<AssetExternalDQTestRule> assetExternalDQTestRules;

    /**
     * Quickly create a new AssetExternalDQTestRunHistory.
     * @param assetExternalDQTestRunId Unique identifier of this DQ Test run on the external DQ tool.
     * @param assetExternalDQTestRunStartedAt Start timestamp of the last DQ Test run for the asset on the external DQ tool.
     * @param assetExternalDQTestRunEndedAt End timestamp of the last DQ Test run for the asset on the external DQ tool.
     * @param assetExternalDQTestRunStatus Overall status of the last DQ Test run for the asset on the external DQ tool.
     * @param assetExternalDQTestRunScoreValue DQ score value of this specific test run on the external DQ tool.
     * @param assetExternalDQTestScoreType DQ score type of this specific test run on the external DQ tool (e.g. numeric).
     * @param assetExternalDQTestTotalRulesCount Total number of rules evaluated in this DQ Test run for the asset on the external DQ tool.
     * @param assetExternalDQTestPassedRulesCount Count of the rules passed in last DQ Test run for the asset on the external DQ tool.
     * @param assetExternalDQTestFailedRulesCount Count of the rules failed in last DQ Test run for the asset on the external DQ tool.
     * @param assetExternalDQTestMetricInfo Metric details of each DQ Test run for the asset on the external DQ tool.
     * @param assetExternalDQTestScoreDimensions Detailed breakdown of the score by dimension for the DQ Test run for the asset on the external DQ tool.
     * @param assetExternalDQTestRules Detailed listing of all the rules evaluated in the DQ Test run for the asset on the external DQ tool.
     * @return a AssetExternalDQTestRunHistory with the provided information
     */
    public static AssetExternalDQTestRunHistory of(
            String assetExternalDQTestRunId,
            Long assetExternalDQTestRunStartedAt,
            Long assetExternalDQTestRunEndedAt,
            String assetExternalDQTestRunStatus,
            String assetExternalDQTestRunScoreValue,
            String assetExternalDQTestScoreType,
            Long assetExternalDQTestTotalRulesCount,
            Long assetExternalDQTestPassedRulesCount,
            Long assetExternalDQTestFailedRulesCount,
            AssetExternalDQTestMetric assetExternalDQTestMetricInfo,
            List<AssetExternalDQTestScoreDimension> assetExternalDQTestScoreDimensions,
            List<AssetExternalDQTestRule> assetExternalDQTestRules) {
        return AssetExternalDQTestRunHistory.builder()
                .assetExternalDQTestRunId(assetExternalDQTestRunId)
                .assetExternalDQTestRunStartedAt(assetExternalDQTestRunStartedAt)
                .assetExternalDQTestRunEndedAt(assetExternalDQTestRunEndedAt)
                .assetExternalDQTestRunStatus(assetExternalDQTestRunStatus)
                .assetExternalDQTestRunScoreValue(assetExternalDQTestRunScoreValue)
                .assetExternalDQTestScoreType(assetExternalDQTestScoreType)
                .assetExternalDQTestTotalRulesCount(assetExternalDQTestTotalRulesCount)
                .assetExternalDQTestPassedRulesCount(assetExternalDQTestPassedRulesCount)
                .assetExternalDQTestFailedRulesCount(assetExternalDQTestFailedRulesCount)
                .assetExternalDQTestMetricInfo(assetExternalDQTestMetricInfo)
                .assetExternalDQTestScoreDimensions(assetExternalDQTestScoreDimensions)
                .assetExternalDQTestRules(assetExternalDQTestRules)
                .build();
    }

    public abstract static class AssetExternalDQTestRunHistoryBuilder<
                    C extends AssetExternalDQTestRunHistory, B extends AssetExternalDQTestRunHistoryBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
