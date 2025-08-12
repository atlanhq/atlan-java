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
    String assetV2ExternalDQSystemName;

    /** Logo URL for the external DQ tool. */
    String assetV2ExternalDQSourceLogo;

    /** URL on the external DQ tool with reference to this asset. */
    String assetV2ExternalDQSourceURL;

    /** Timestamp of the last metadata sync with the external DQ tool. */
    Long assetV2ExternalDQLastSyncRunAt;

    /** Name of the DQ Test entity in the external DQ tool. */
    String assetV2ExternalDQTestEntityName;

    /** Total number of DQ tests defined on the external DQ tool for this asset. */
    Integer assetV2ExternalDQTestTotalCount;

    /** Total number of DQ tests defined on the external DQ tool for this asset that were successful in their last run. */
    Integer assetV2ExternalDQTestLastRunSuccessCount;

    /** Total number of DQ tests defined on the external DQ tool for this asset that were not successful in their last run. */
    Integer assetV2ExternalDQTestLastRunFailureCount;

    /** DQ score value for the DQ Test for this asset on the external DQ tool. */
    String assetV2ExternalDQOverallScoreValue;

    /** DQ score type for the DQ Test for this asset on the external DQ tool. */
    String assetV2ExternalDQOverallScoreType;

    /** Detailed breakdown of the score by dimension for the DQ Test for this asset on the external DQ tool. */
    @Singular
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    List<AssetExternalDQScoreBreakdownByDimension> assetV2ExternalDQScoreDimensions;

    /** Detail of the DQ Test entities for this asset on the external DQ tool. */
    @Singular
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    List<AssetExternalDQTestDetails> assetV2ExternalDQTests;

    /**
     * Quickly create a new AssetExternalDQMetadata.
     * @param assetV2ExternalDQSystemName Name of the external DQ tool.
     * @param assetV2ExternalDQSourceLogo Logo URL for the external DQ tool.
     * @param assetV2ExternalDQSourceURL URL on the external DQ tool with reference to this asset.
     * @param assetV2ExternalDQLastSyncRunAt Timestamp of the last metadata sync with the external DQ tool.
     * @param assetV2ExternalDQTestEntityName Name of the DQ Test entity in the external DQ tool.
     * @param assetV2ExternalDQTestTotalCount Total number of DQ tests defined on the external DQ tool for this asset.
     * @param assetV2ExternalDQTestLastRunSuccessCount Total number of DQ tests defined on the external DQ tool for this asset that were successful in their last run.
     * @param assetV2ExternalDQTestLastRunFailureCount Total number of DQ tests defined on the external DQ tool for this asset that were not successful in their last run.
     * @param assetV2ExternalDQOverallScoreValue DQ score value for the DQ Test for this asset on the external DQ tool.
     * @param assetV2ExternalDQOverallScoreType DQ score type for the DQ Test for this asset on the external DQ tool.
     * @param assetV2ExternalDQScoreDimensions Detailed breakdown of the score by dimension for the DQ Test for this asset on the external DQ tool.
     * @param assetV2ExternalDQTests Detail of the DQ Test entities for this asset on the external DQ tool.
     * @return a AssetExternalDQMetadata with the provided information
     */
    public static AssetExternalDQMetadata of(
            String assetV2ExternalDQSystemName,
            String assetV2ExternalDQSourceLogo,
            String assetV2ExternalDQSourceURL,
            Long assetV2ExternalDQLastSyncRunAt,
            String assetV2ExternalDQTestEntityName,
            Integer assetV2ExternalDQTestTotalCount,
            Integer assetV2ExternalDQTestLastRunSuccessCount,
            Integer assetV2ExternalDQTestLastRunFailureCount,
            String assetV2ExternalDQOverallScoreValue,
            String assetV2ExternalDQOverallScoreType,
            List<AssetExternalDQScoreBreakdownByDimension> assetV2ExternalDQScoreDimensions,
            List<AssetExternalDQTestDetails> assetV2ExternalDQTests) {
        return AssetExternalDQMetadata.builder()
                .assetV2ExternalDQSystemName(assetV2ExternalDQSystemName)
                .assetV2ExternalDQSourceLogo(assetV2ExternalDQSourceLogo)
                .assetV2ExternalDQSourceURL(assetV2ExternalDQSourceURL)
                .assetV2ExternalDQLastSyncRunAt(assetV2ExternalDQLastSyncRunAt)
                .assetV2ExternalDQTestEntityName(assetV2ExternalDQTestEntityName)
                .assetV2ExternalDQTestTotalCount(assetV2ExternalDQTestTotalCount)
                .assetV2ExternalDQTestLastRunSuccessCount(assetV2ExternalDQTestLastRunSuccessCount)
                .assetV2ExternalDQTestLastRunFailureCount(assetV2ExternalDQTestLastRunFailureCount)
                .assetV2ExternalDQOverallScoreValue(assetV2ExternalDQOverallScoreValue)
                .assetV2ExternalDQOverallScoreType(assetV2ExternalDQOverallScoreType)
                .assetV2ExternalDQScoreDimensions(assetV2ExternalDQScoreDimensions)
                .assetV2ExternalDQTests(assetV2ExternalDQTests)
                .build();
    }

    public abstract static class AssetExternalDQMetadataBuilder<
                    C extends AssetExternalDQMetadata, B extends AssetExternalDQMetadataBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
