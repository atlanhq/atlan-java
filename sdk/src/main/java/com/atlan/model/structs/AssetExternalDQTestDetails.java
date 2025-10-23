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
    String assetExternalDQTestName;

    /** Identifier of the DQ Test entity in the external DQ tool. */
    String assetExternalDQTestId;

    /** Description of the DQ Test entity in the external DQ tool. */
    String assetExternalDQTestDescription;

    /** Schedule descriptor of the DQ Test entity in the external DQ tool. */
    String assetExternalDQTestScheduleType;

    /** Status of the last run of the DQ Test entity in the external DQ tool. */
    String assetExternalDQTestLastRunStatus;

    /** Detailed listing of all the runs of the DQ Test entity in the external DQ tool. */
    @Singular
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    List<AssetExternalDQTestRunHistory> assetExternalDQTestRuns;

    /**
     * Quickly create a new AssetExternalDQTestDetails.
     * @param assetExternalDQTestName Name of the DQ Test entity in the external DQ tool.
     * @param assetExternalDQTestId Identifier of the DQ Test entity in the external DQ tool.
     * @param assetExternalDQTestDescription Description of the DQ Test entity in the external DQ tool.
     * @param assetExternalDQTestScheduleType Schedule descriptor of the DQ Test entity in the external DQ tool.
     * @param assetExternalDQTestLastRunStatus Status of the last run of the DQ Test entity in the external DQ tool.
     * @param assetExternalDQTestRuns Detailed listing of all the runs of the DQ Test entity in the external DQ tool.
     * @return a AssetExternalDQTestDetails with the provided information
     */
    public static AssetExternalDQTestDetails of(
            String assetExternalDQTestName,
            String assetExternalDQTestId,
            String assetExternalDQTestDescription,
            String assetExternalDQTestScheduleType,
            String assetExternalDQTestLastRunStatus,
            List<AssetExternalDQTestRunHistory> assetExternalDQTestRuns) {
        return AssetExternalDQTestDetails.builder()
                .assetExternalDQTestName(assetExternalDQTestName)
                .assetExternalDQTestId(assetExternalDQTestId)
                .assetExternalDQTestDescription(assetExternalDQTestDescription)
                .assetExternalDQTestScheduleType(assetExternalDQTestScheduleType)
                .assetExternalDQTestLastRunStatus(assetExternalDQTestLastRunStatus)
                .assetExternalDQTestRuns(assetExternalDQTestRuns)
                .build();
    }

    public abstract static class AssetExternalDQTestDetailsBuilder<
                    C extends AssetExternalDQTestDetails, B extends AssetExternalDQTestDetailsBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
