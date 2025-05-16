/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search.aggregates;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Captures a specific aggregate result of assets and the views on that asset.
 */
@Getter
@Builder
@EqualsAndHashCode
public class AssetViews {
    /** GUID of the asset that was viewed. */
    String guid;

    /** Number of times the asset has been viewed (in total). */
    Long totalViews;

    /** Number of distinct users that have viewed the asset. */
    Long distinctUsers;

    public static class AssetViewsBuilder {}
}
