/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    /** Start timestamp of the last DQ Test run for the asset on the external DQ tool. */
    Long assetExternalDQTestRunStartedAt;

    /** End timestamp of the last DQ Test run for the asset on the external DQ tool. */
    Long assetExternalDQTestRunEndedAt;

    /** Overall status of the last DQ Test run for the asset on the external DQ tool. */
    String assetExternalDQTestRunStatus;

    /**
     * Quickly create a new AssetExternalDQTestRunHistory.
     * @param assetExternalDQTestRunStartedAt Start timestamp of the last DQ Test run for the asset on the external DQ tool.
     * @param assetExternalDQTestRunEndedAt End timestamp of the last DQ Test run for the asset on the external DQ tool.
     * @param assetExternalDQTestRunStatus Overall status of the last DQ Test run for the asset on the external DQ tool.
     * @return a AssetExternalDQTestRunHistory with the provided information
     */
    public static AssetExternalDQTestRunHistory of(
            Long assetExternalDQTestRunStartedAt,
            Long assetExternalDQTestRunEndedAt,
            String assetExternalDQTestRunStatus) {
        return AssetExternalDQTestRunHistory.builder()
                .assetExternalDQTestRunStartedAt(assetExternalDQTestRunStartedAt)
                .assetExternalDQTestRunEndedAt(assetExternalDQTestRunEndedAt)
                .assetExternalDQTestRunStatus(assetExternalDQTestRunStatus)
                .build();
    }

    public abstract static class AssetExternalDQTestRunHistoryBuilder<
                    C extends AssetExternalDQTestRunHistory, B extends AssetExternalDQTestRunHistoryBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
