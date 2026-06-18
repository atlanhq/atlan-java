/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Metadata about the provider of an asset's summary.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class AssetSummaryProvider extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AssetSummaryProvider";

    /** Fixed typeName for AssetSummaryProvider. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Identifier for the provider; drives which renderer and icon the frontend picks for the summary card. */
    String assetSummaryProviderName;

    /** Optional deep link back to the asset's summary in the provider's UI; rendered on the asset summary page as a 'Show in {name}' button. */
    String assetSummaryProviderUrl;

    /**
     * Quickly create a new AssetSummaryProvider.
     * @param assetSummaryProviderName Identifier for the provider; drives which renderer and icon the frontend picks for the summary card.
     * @param assetSummaryProviderUrl Optional deep link back to the asset's summary in the provider's UI; rendered on the asset summary page as a 'Show in {name}' button.
     * @return a AssetSummaryProvider with the provided information
     */
    public static AssetSummaryProvider of(String assetSummaryProviderName, String assetSummaryProviderUrl) {
        return AssetSummaryProvider.builder()
                .assetSummaryProviderName(assetSummaryProviderName)
                .assetSummaryProviderUrl(assetSummaryProviderUrl)
                .build();
    }

    public abstract static class AssetSummaryProviderBuilder<
                    C extends AssetSummaryProvider, B extends AssetSummaryProviderBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
