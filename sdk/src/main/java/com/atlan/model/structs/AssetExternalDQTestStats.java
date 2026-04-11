/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import java.util.Map;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed statistics for the external data quality (DQ) tests associated with a given asset.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings({"cast", "serial"})
public class AssetExternalDQTestStats extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AssetExternalDQTestStats";

    /** Fixed typeName for AssetExternalDQTestStats. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** The timestamp when the external DQ tests were last assessed for this asset. */
    Long assetExternalDQTestLastAssessedAt;

    /** A map of data quality statistics grouped by category for the external DQ tests. */
    @Singular
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    Map<String, AssetExternalDQTestStatsByCategory> assetExternalDQTestStatsByCategory;

    /**
     * Quickly create a new AssetExternalDQTestStats.
     * @param assetExternalDQTestLastAssessedAt The timestamp when the external DQ tests were last assessed for this asset.
     * @param assetExternalDQTestStatsByCategory A map of data quality statistics grouped by category for the external DQ tests.
     * @return a AssetExternalDQTestStats with the provided information
     */
    public static AssetExternalDQTestStats of(
            Long assetExternalDQTestLastAssessedAt,
            Map<String, AssetExternalDQTestStatsByCategory> assetExternalDQTestStatsByCategory) {
        return AssetExternalDQTestStats.builder()
                .assetExternalDQTestLastAssessedAt(assetExternalDQTestLastAssessedAt)
                .assetExternalDQTestStatsByCategory(assetExternalDQTestStatsByCategory)
                .build();
    }

    public abstract static class AssetExternalDQTestStatsBuilder<
                    C extends AssetExternalDQTestStats, B extends AssetExternalDQTestStatsBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
