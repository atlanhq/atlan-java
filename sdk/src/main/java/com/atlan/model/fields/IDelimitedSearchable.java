/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public interface IDelimitedSearchable {
    /**
     * Returns the name of the delimited text field index for this attribute in Elastic.
     *
     * @return the field name for the delimited text index on this attribute
     */
    String getDelimitedFieldName();

    /**
     * Returns a query that will textually match the provided value against the field. This
     * analyzes the provided value according to the same analysis carried out on the field
     * (for example, tokenization and stemming).
     *
     * @param value the string value to match against
     * @return a query that will only match assets whose analyzed value for the field matches the value provided (which will also be analyzed)
     */
    Query matchDelimited(String value);

    /**
     * Returns a query that will textually match the provided value against the field. This
     * analyzes the provided value according to the same analysis carried out on the field
     * (for example, tokenization and stemming).
     *
     * @param field name of the field to search
     * @param value the string value to match against
     * @return a query that will only match assets whose analyzed value for the field matches the value provided (which will also be analyzed)
     */
    static Query matchDelimited(final String field, final String value) {
        return MatchQuery.of(m -> m.field(field).query(value))._toQuery();
    }
}
