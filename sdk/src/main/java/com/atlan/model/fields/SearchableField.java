/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
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
    public Query hasAnyValue() {
        return ISearchable.hasAnyValue(getElasticFieldName());
    }

    /** {@inheritDoc} */
    @Override
    public Aggregation distinct() {
        return ISearchable.distinct(getElasticFieldName());
    }

    /** {@inheritDoc} */
    @Override
    public Aggregation count() {
        return ISearchable.count(getElasticFieldName());
    }

    /** {@inheritDoc} */
    @Override
    public Aggregation bucketBy(int size, boolean includeSourceValue) {
        if (includeSourceValue) {
            return ISearchable.bucketBy(getElasticFieldName(), size, getAtlanFieldName());
        } else {
            return ISearchable.bucketBy(getElasticFieldName(), size);
        }
    }

    /** {@inheritDoc} */
    @Override
    public SortOptions order(SortOrder order) {
        return ISearchable.order(getElasticFieldName(), order);
    }
}
