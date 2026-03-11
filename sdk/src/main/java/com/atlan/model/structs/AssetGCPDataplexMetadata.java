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
 * Various business metadata elements captured by Dataplex for GCP service-level objects.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings({"cast", "serial"})
public class AssetGCPDataplexMetadata extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AssetGCPDataplexMetadata";

    /** Fixed typeName for AssetGCPDataplexMetadata. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Time (epoch) at which the last dq rule ran. */
    Long assetGCPDataplexLastSyncRunAt;

    /** Dataplex Aspects-related metadata on the asset in question. */
    @Singular
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    Map<String, AssetGCPDataplexAspectMetadata> assetGCPDataplexAspectDetails;

    /**
     * Quickly create a new AssetGCPDataplexMetadata.
     * @param assetGCPDataplexLastSyncRunAt Time (epoch) at which the last dq rule ran.
     * @param assetGCPDataplexAspectDetails Dataplex Aspects-related metadata on the asset in question.
     * @return a AssetGCPDataplexMetadata with the provided information
     */
    public static AssetGCPDataplexMetadata of(
            Long assetGCPDataplexLastSyncRunAt,
            Map<String, AssetGCPDataplexAspectMetadata> assetGCPDataplexAspectDetails) {
        return AssetGCPDataplexMetadata.builder()
                .assetGCPDataplexLastSyncRunAt(assetGCPDataplexLastSyncRunAt)
                .assetGCPDataplexAspectDetails(assetGCPDataplexAspectDetails)
                .build();
    }

    public abstract static class AssetGCPDataplexMetadataBuilder<
                    C extends AssetGCPDataplexMetadata, B extends AssetGCPDataplexMetadataBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
