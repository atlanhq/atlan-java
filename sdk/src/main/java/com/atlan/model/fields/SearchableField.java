/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.util.NamedValue;
import java.util.List;
import java.util.Map;
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
    public Aggregation distinct(int precision) {
        return ISearchable.distinct(getElasticFieldName(), precision);
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
            if (this instanceof CustomMetadataField) {
                // Need to handle the hashed-string ID stuff for custom metadata fields
                return ISearchable.bucketBy(getElasticFieldName(), size, getElasticFieldName());
            } else {
                return ISearchable.bucketBy(getElasticFieldName(), size, getAtlanFieldName());
            }
        } else {
            return ISearchable.bucketBy(getElasticFieldName(), size);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Aggregation bucketBy(int size, Map<String, Aggregation> nested, List<NamedValue<SortOrder>> order) {
        return ISearchable.bucketBy(getElasticFieldName(), size, nested, order);
    }

    /** {@inheritDoc} */
    @Override
    public SortOptions order(SortOrder order) {
        return ISearchable.order(getElasticFieldName(), order);
    }
}
