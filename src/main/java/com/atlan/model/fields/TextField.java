/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class TextField extends SearchableField implements ITextSearchable {
    public TextField(String atlan, String keyword) {
        super(atlan, keyword);
    }

    /** {@inheritDoc} */
    @Override
    public String getTextFieldName() {
        return getElasticFieldName();
    }

    /** {@inheritDoc} */
    @Override
    public Query match(String value) {
        return ITextSearchable.match(getTextFieldName(), value);
    }
}
