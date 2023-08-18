/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Base class for any field in Atlan that can be searched.
 */
public class SearchableField extends AtlanField implements ISearchable {

    @Getter(AccessLevel.PROTECTED)
    private final String elasticFieldName;

    /**
     * Default constructor.
     *
     * @param atlan name of the attribute in the metastore
     * @param elastic name of the field in the search index
     */
    public SearchableField(String atlan, String elastic) {
        super(atlan);
        this.elasticFieldName = elastic;
    }

    /** {@inheritDoc} */
    @Override
    public Query exists() {
        return ISearchable.exists(getElasticFieldName());
    }
}
