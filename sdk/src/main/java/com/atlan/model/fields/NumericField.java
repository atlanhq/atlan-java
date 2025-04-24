/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.model.lineage.LineageFilterFieldNumeric;

/**
 * Represents any field in Atlan that can be searched using only numeric search operations.
 */
public class NumericField extends SearchableField implements INumericallySearchable {

    @SuppressWarnings("this-escape")
    public final LineageFilterFieldNumeric inLineage = new LineageFilterFieldNumeric(this);

    /**
     * Default constructor.
     *
     * @param atlan name of the attribute in the metastore
     * @param numeric name of the numeric field in the search index
     */
    public NumericField(String atlan, String numeric) {
        super(atlan, numeric);
    }

    /** {@inheritDoc} */
    @Override
    public String getNumericFieldName() {
        return getElasticFieldName();
    }

    /** {@inheritDoc} */
    @Override
    public <T extends Number> Query eq(T value) {
        return INumericallySearchable.eq(getNumericFieldName(), value);
    }

    /** {@inheritDoc} */
    @Override
    public <T extends Number> Query gt(T value) {
        return INumericallySearchable.gt(getNumericFieldName(), value);
    }

    /** {@inheritDoc} */
    @Override
    public <T extends Number> Query gte(T value) {
        return INumericallySearchable.gte(getNumericFieldName(), value);
    }

    /** {@inheritDoc} */
    @Override
    public <T extends Number> Query lt(T value) {
        return INumericallySearchable.lt(getNumericFieldName(), value);
    }

    /** {@inheritDoc} */
    @Override
    public <T extends Number> Query lte(T value) {
        return INumericallySearchable.lte(getNumericFieldName(), value);
    }

    /** {@inheritDoc} */
    @Override
    public <T extends Number> Query between(T min, T max) {
        return INumericallySearchable.between(getNumericFieldName(), min, max);
    }

    /** {@inheritDoc} */
    @Override
    public Aggregation sum() {
        return INumericallySearchable.sum(getNumericFieldName());
    }

    /** {@inheritDoc} */
    @Override
    public Aggregation avg() {
        return INumericallySearchable.avg(getNumericFieldName());
    }

    /** {@inheritDoc} */
    @Override
    public Aggregation min() {
        return INumericallySearchable.min(getNumericFieldName());
    }

    /** {@inheritDoc} */
    @Override
    public Aggregation max() {
        return INumericallySearchable.max(getNumericFieldName());
    }
}
