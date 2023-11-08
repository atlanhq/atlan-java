/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import javax.annotation.processing.Generated;
import lombok.Getter;

/**
 * Enumeration of all index fields.
 * @deprecated these are now enumerated as constants in the various asset interfaces, as instances of one of the following: {@link com.atlan.model.fields.BooleanField}, {@link com.atlan.model.fields.KeywordField}, {@link com.atlan.model.fields.KeywordTextField}, {@link com.atlan.model.fields.KeywordTextStemmedField}, {@link com.atlan.model.fields.NumericField}, {@link com.atlan.model.fields.NumericRankField}, {@link com.atlan.model.fields.RelationField}, or {@link com.atlan.model.fields.TextField}
 */
@Deprecated
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
public enum RankFields implements AtlanSearchableField {
    /** Popularity score for this asset. */
    POPULARITY_SCORE("popularityScore.rank_feature"),
    /** View score for this asset. */
    VIEW_SCORE("viewScore.rank_feature"),
    ;

    @Getter(onMethod_ = {@Override})
    private final String indexedFieldName;

    RankFields(String indexedFieldName) {
        this.indexedFieldName = indexedFieldName;
    }
}
