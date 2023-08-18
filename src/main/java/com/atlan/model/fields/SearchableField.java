/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.exception.AtlanException;
import lombok.AccessLevel;
import lombok.Getter;

public class SearchableField implements ISearchable {

    @Getter
    private final String atlanFieldName;

    @Getter(AccessLevel.PROTECTED)
    private final String elasticFieldName;

    public SearchableField(String atlan, String elastic) {
        this.atlanFieldName = atlan;
        this.elasticFieldName = elastic;
    }

    /** {@inheritDoc} */
    @Override
    public Query exists() throws AtlanException {
        return ISearchable.exists(getElasticFieldName());
    }
}
