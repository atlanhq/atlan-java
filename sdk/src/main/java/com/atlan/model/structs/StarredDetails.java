/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about the users who have starred an asset.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class StarredDetails extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "StarredDetails";

    /** Fixed typeName for StarredDetails. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Username of the user who starred the asset. */
    String assetStarredBy;

    /** Time at which the user starred the asset. */
    Long assetStarredAt;

    /**
     * Quickly create a new StarredDetails.
     * @param assetStarredBy Username of the user who starred the asset.
     * @param assetStarredAt Time at which the user starred the asset.
     * @return a StarredDetails with the provided information
     */
    public static StarredDetails of(String assetStarredBy, Long assetStarredAt) {
        return StarredDetails.builder()
                .assetStarredBy(assetStarredBy)
                .assetStarredAt(assetStarredAt)
                .build();
    }
}
