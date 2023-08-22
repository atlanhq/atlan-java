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
public enum StemmedFields implements AtlanSearchableField {
    /** Title for the asset. */
    DATA_STUDIO_ASSET_TITLE("dataStudioAssetTitle.stemmed"),
    /** Human-readable name of the asset. */
    NAME("name.stemmed"),
    /** Username of the user who last changed the collection. */
    PRESET_DASHBOARD_CHANGED_BY_NAME("presetDashboardChangedByName.stemmed"),
    /** Name of the data source for the dataset. */
    PRESET_DATASET_DATASOURCE_NAME("presetDatasetDatasourceName.stemmed"),
    ;

    @Getter(onMethod_ = {@Override})
    private final String indexedFieldName;

    StemmedFields(String indexedFieldName) {
        this.indexedFieldName = indexedFieldName;
    }
}
