/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Count of external DQ tests for a specific status.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AssetExternalDQTestsByStatus extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AssetExternalDQTestsByStatus";

    /** Fixed typeName for AssetExternalDQTestsByStatus. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** The number of external DQ tests associated with this status. */
    Integer assetExternalDQTestCountForStatus;

    /**
     * Quickly create a new AssetExternalDQTestsByStatus.
     * @param assetExternalDQTestCountForStatus The number of external DQ tests associated with this status.
     * @return a AssetExternalDQTestsByStatus with the provided information
     */
    public static AssetExternalDQTestsByStatus of(Integer assetExternalDQTestCountForStatus) {
        return AssetExternalDQTestsByStatus.builder()
                .assetExternalDQTestCountForStatus(assetExternalDQTestCountForStatus)
                .build();
    }

    public abstract static class AssetExternalDQTestsByStatusBuilder<
                    C extends AssetExternalDQTestsByStatus, B extends AssetExternalDQTestsByStatusBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
