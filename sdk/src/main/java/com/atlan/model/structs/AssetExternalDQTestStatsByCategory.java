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
 * Detailed information about the External DQ Tests associated with a given asset.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings({"cast", "serial"})
public class AssetExternalDQTestStatsByCategory extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AssetExternalDQTestStatsByCategory";

    /** Fixed typeName for AssetExternalDQTestStatsByCategory. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Statistics for external DQ tests grouped by category. */
    @Singular
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    Map<String, AssetExternalDQTestsByStatus> assetExternalDQTestsByStatus;

    /**
     * Quickly create a new AssetExternalDQTestStatsByCategory.
     * @param assetExternalDQTestsByStatus Statistics for external DQ tests grouped by category.
     * @return a AssetExternalDQTestStatsByCategory with the provided information
     */
    public static AssetExternalDQTestStatsByCategory of(
            Map<String, AssetExternalDQTestsByStatus> assetExternalDQTestsByStatus) {
        return AssetExternalDQTestStatsByCategory.builder()
                .assetExternalDQTestsByStatus(assetExternalDQTestsByStatus)
                .build();
    }

    public abstract static class AssetExternalDQTestStatsByCategoryBuilder<
                    C extends AssetExternalDQTestStatsByCategory,
                    B extends AssetExternalDQTestStatsByCategoryBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
