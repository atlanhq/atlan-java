/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class BooleanField extends SearchableField implements IBooleanSearchable {
    public BooleanField(String atlan, String elastic) {
        super(atlan, elastic);
    }

    /** {@inheritDoc} */
    @Override
    public String getBooleanFieldName() {
        return getElasticFieldName();
    }

    /** {@inheritDoc} */
    @Override
    public Query eq(boolean value) {
        return IBooleanSearchable.eq(getBooleanFieldName(), value);
    }
}
