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
    Long assetV2ExternalDQTestRunStartedAt;

    /** End timestamp of the last DQ Test run for the asset on the external DQ tool. */
    Long assetV2ExternalDQTestRunEndedAt;

    /** Overall status of the last DQ Test run for the asset on the external DQ tool. */
    String assetV2ExternalDQTestRunStatus;

    /**
     * Quickly create a new AssetExternalDQTestRunHistory.
     * @param assetV2ExternalDQTestRunStartedAt Start timestamp of the last DQ Test run for the asset on the external DQ tool.
     * @param assetV2ExternalDQTestRunEndedAt End timestamp of the last DQ Test run for the asset on the external DQ tool.
     * @param assetV2ExternalDQTestRunStatus Overall status of the last DQ Test run for the asset on the external DQ tool.
     * @return a AssetExternalDQTestRunHistory with the provided information
     */
    public static AssetExternalDQTestRunHistory of(
            Long assetV2ExternalDQTestRunStartedAt,
            Long assetV2ExternalDQTestRunEndedAt,
            String assetV2ExternalDQTestRunStatus) {
        return AssetExternalDQTestRunHistory.builder()
                .assetV2ExternalDQTestRunStartedAt(assetV2ExternalDQTestRunStartedAt)
                .assetV2ExternalDQTestRunEndedAt(assetV2ExternalDQTestRunEndedAt)
                .assetV2ExternalDQTestRunStatus(assetV2ExternalDQTestRunStatus)
                .build();
    }

    public abstract static class AssetExternalDQTestRunHistoryBuilder<
                    C extends AssetExternalDQTestRunHistory, B extends AssetExternalDQTestRunHistoryBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
