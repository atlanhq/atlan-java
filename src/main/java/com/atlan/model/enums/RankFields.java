/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import lombok.Getter;

public enum RankFields implements AtlanSearchableField {
    /** TBC */
    POPULARITY_SCORE("popularityScore.rank_feature"),
    /** TBC */
    VIEW_SCORE("viewScore.rank_feature"),
    ;

    @Getter(onMethod_ = {@Override})
    private final String indexedFieldName;

    RankFields(String indexedFieldName) {
        this.indexedFieldName = indexedFieldName;
    }
}
