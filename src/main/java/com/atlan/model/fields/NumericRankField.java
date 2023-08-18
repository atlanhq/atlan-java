/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class NumericRankField extends SearchableField implements INumericallySearchable, IRankSearchable {

    private final String rankFieldName;

    public NumericRankField(String atlan, String elastic, String rank) {
        super(atlan, elastic);
        this.rankFieldName = rank;
    }

    /** {@inheritDoc} */
    @Override
    public String getRankFieldName() {
        return rankFieldName;
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
}
