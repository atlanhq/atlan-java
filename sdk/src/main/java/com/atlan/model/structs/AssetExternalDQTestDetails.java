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
 * Detailed information about the External DQ Tests associated with a given asset.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AssetExternalDQTestDetails extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AssetExternalDQTestDetails";

    /** Fixed typeName for AssetExternalDQTestDetails. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Name of the DQ Test entity in the external DQ tool. */
    String assetV2ExternalDQTestName;

    /** Identifier of the DQ Test entity in the external DQ tool. */
    String assetV2ExternalDQTestId;

    /** Description of the DQ Test entity in the external DQ tool. */
    String assetV2ExternalDQTestDescription;

    /** Schedule descriptor of the DQ Test entity in the external DQ tool. */
    String assetV2ExternalDQTestScheduleType;

    /** Status of the last run of the DQ Test entity in the external DQ tool. */
    String assetV2ExternalDQTestLastRunStatus;

    /** Detailed listing of all the runs of the DQ Test entity in the external DQ tool. */
    @Singular
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    List<AssetExternalDQTestRunHistory> assetV2ExternalDQTestRuns;

    /**
     * Quickly create a new AssetExternalDQTestDetails.
     * @param assetV2ExternalDQTestName Name of the DQ Test entity in the external DQ tool.
     * @param assetV2ExternalDQTestId Identifier of the DQ Test entity in the external DQ tool.
     * @param assetV2ExternalDQTestDescription Description of the DQ Test entity in the external DQ tool.
     * @param assetV2ExternalDQTestScheduleType Schedule descriptor of the DQ Test entity in the external DQ tool.
     * @param assetV2ExternalDQTestLastRunStatus Status of the last run of the DQ Test entity in the external DQ tool.
     * @param assetV2ExternalDQTestRuns Detailed listing of all the runs of the DQ Test entity in the external DQ tool.
     * @return a AssetExternalDQTestDetails with the provided information
     */
    public static AssetExternalDQTestDetails of(
            String assetV2ExternalDQTestName,
            String assetV2ExternalDQTestId,
            String assetV2ExternalDQTestDescription,
            String assetV2ExternalDQTestScheduleType,
            String assetV2ExternalDQTestLastRunStatus,
            List<AssetExternalDQTestRunHistory> assetV2ExternalDQTestRuns) {
        return AssetExternalDQTestDetails.builder()
                .assetV2ExternalDQTestName(assetV2ExternalDQTestName)
                .assetV2ExternalDQTestId(assetV2ExternalDQTestId)
                .assetV2ExternalDQTestDescription(assetV2ExternalDQTestDescription)
                .assetV2ExternalDQTestScheduleType(assetV2ExternalDQTestScheduleType)
                .assetV2ExternalDQTestLastRunStatus(assetV2ExternalDQTestLastRunStatus)
                .assetV2ExternalDQTestRuns(assetV2ExternalDQTestRuns)
                .build();
    }

    public abstract static class AssetExternalDQTestDetailsBuilder<
                    C extends AssetExternalDQTestDetails, B extends AssetExternalDQTestDetailsBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
