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
 * Detailed information about the External DQ system where rules are run against a given asset.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AssetExternalDQMetadata extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AssetExternalDQMetadata";

    /** Fixed typeName for AssetExternalDQMetadata. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Name of the external DQ tool. */
    String assetExternalDQSystemName;

    /** Logo URL for the external DQ tool. */
    String assetExternalDQSourceLogo;

    /** URL on the external DQ tool with reference to this asset. */
    String assetExternalDQSourceURL;

    /** Timestamp of the last metadata sync with the external DQ tool. */
    Long assetExternalDQLastSyncRunAt;

    /** Name of the DQ Test entity in the external DQ tool. */
    String assetExternalDQTestEntityName;

    /** Total number of DQ tests defined on the external DQ tool for this asset. */
    Integer assetExternalDQTestTotalCount;

    /** Total number of DQ tests defined on the external DQ tool for this asset that were successful in their last run. */
    Integer assetExternalDQTestLastRunSuccessCount;

    /** Total number of DQ tests defined on the external DQ tool for this asset that were not successful in their last run. */
    Integer assetExternalDQTestLastRunFailureCount;

    /** DQ score value for the DQ Test for this asset on the external DQ tool. */
    String assetExternalDQOverallScoreValue;

    /** DQ score type for the DQ Test for this asset on the external DQ tool. */
    String assetExternalDQOverallScoreType;

    /** Detailed breakdown of the score by dimension for the DQ Test for this asset on the external DQ tool. */
    @Singular
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    List<AssetExternalDQScoreBreakdownByDimension> assetExternalDQScoreDimensions;

    /** Detail of the DQ Test entities for this asset on the external DQ tool. */
    @Singular
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    List<AssetExternalDQTestDetails> assetExternalDQTests;

    /**
     * Quickly create a new AssetExternalDQMetadata.
     * @param assetExternalDQSystemName Name of the external DQ tool.
     * @param assetExternalDQSourceLogo Logo URL for the external DQ tool.
     * @param assetExternalDQSourceURL URL on the external DQ tool with reference to this asset.
     * @param assetExternalDQLastSyncRunAt Timestamp of the last metadata sync with the external DQ tool.
     * @param assetExternalDQTestEntityName Name of the DQ Test entity in the external DQ tool.
     * @param assetExternalDQTestTotalCount Total number of DQ tests defined on the external DQ tool for this asset.
     * @param assetExternalDQTestLastRunSuccessCount Total number of DQ tests defined on the external DQ tool for this asset that were successful in their last run.
     * @param assetExternalDQTestLastRunFailureCount Total number of DQ tests defined on the external DQ tool for this asset that were not successful in their last run.
     * @param assetExternalDQOverallScoreValue DQ score value for the DQ Test for this asset on the external DQ tool.
     * @param assetExternalDQOverallScoreType DQ score type for the DQ Test for this asset on the external DQ tool.
     * @param assetExternalDQScoreDimensions Detailed breakdown of the score by dimension for the DQ Test for this asset on the external DQ tool.
     * @param assetExternalDQTests Detail of the DQ Test entities for this asset on the external DQ tool.
     * @return a AssetExternalDQMetadata with the provided information
     */
    public static AssetExternalDQMetadata of(
            String assetExternalDQSystemName,
            String assetExternalDQSourceLogo,
            String assetExternalDQSourceURL,
            Long assetExternalDQLastSyncRunAt,
            String assetExternalDQTestEntityName,
            Integer assetExternalDQTestTotalCount,
            Integer assetExternalDQTestLastRunSuccessCount,
            Integer assetExternalDQTestLastRunFailureCount,
            String assetExternalDQOverallScoreValue,
            String assetExternalDQOverallScoreType,
            List<AssetExternalDQScoreBreakdownByDimension> assetExternalDQScoreDimensions,
            List<AssetExternalDQTestDetails> assetExternalDQTests) {
        return AssetExternalDQMetadata.builder()
                .assetExternalDQSystemName(assetExternalDQSystemName)
                .assetExternalDQSourceLogo(assetExternalDQSourceLogo)
                .assetExternalDQSourceURL(assetExternalDQSourceURL)
                .assetExternalDQLastSyncRunAt(assetExternalDQLastSyncRunAt)
                .assetExternalDQTestEntityName(assetExternalDQTestEntityName)
                .assetExternalDQTestTotalCount(assetExternalDQTestTotalCount)
                .assetExternalDQTestLastRunSuccessCount(assetExternalDQTestLastRunSuccessCount)
                .assetExternalDQTestLastRunFailureCount(assetExternalDQTestLastRunFailureCount)
                .assetExternalDQOverallScoreValue(assetExternalDQOverallScoreValue)
                .assetExternalDQOverallScoreType(assetExternalDQOverallScoreType)
                .assetExternalDQScoreDimensions(assetExternalDQScoreDimensions)
                .assetExternalDQTests(assetExternalDQTests)
                .build();
    }

    public abstract static class AssetExternalDQMetadataBuilder<
                    C extends AssetExternalDQMetadata, B extends AssetExternalDQMetadataBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
