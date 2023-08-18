/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;

public interface ISearchable {
    /**
     * Returns a query that will only match assets that have some non-null, non-empty value
     * (no matter what actual value) for the field.
     *
     * @return a query that will only match assets that have some non-null, non-empty value
     *         (no matter what actual value) for the field
     * @throws InvalidRequestException if this query does not make sense to run on the field
     * @throws AtlanException on any API communication issue
     */
    Query exists() throws AtlanException;

    /**
     * Returns a query that will only match assets that have some non-null, non-empty value
     * (no matter what actual value) for the field.
     *
     * @param field name of the field to search
     * @return a query that will only match assets that have some non-null, non-empty value
     *         (no matter what actual value) for the field
     */
    static Query exists(final String field) {
        return ExistsQuery.of(e -> e.field(field))._toQuery();
    }
}
