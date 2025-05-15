/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search.aggregates;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Captures a specific aggregate result of views of an asset by a specific user.
 */
@Getter
@Builder
@EqualsAndHashCode
public class UserViews {
    /** Name of the user who viewed the asset. */
    String username;

    /** Number of times the user viewed the asset. */
    Long viewCount;

    /** When the user most recently viewed the asset (epoch-style), in milliseconds. */
    Long mostRecentView;

    public static class UserViewsBuilder {}
}
