/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import com.atlan.model.lineage.LineageFilterFieldBoolean;

public interface IBooleanSearchable {
    /**
     * Returns the name of the boolean field index for this attribute in Elastic.
     *
     * @return the field name for the boolean index on this attribute
     */
    String getBooleanFieldName();

    /**
     * Create a filter for this attribute for lineage.
     *
     * @return a lineage filter for this boolean attribute
     */
    LineageFilterFieldBoolean filterForLineage();

    /**
     * Returns a query that will match all assets whose provided field has a value that exactly equals
     * the provided boolean value.
     *
     * @param value the value (boolean) to check the field's value is exactly equal to
     * @return a query that will only match assets whose value for the field is exactly equal to the boolean value provided
     */
    Query eq(boolean value);

    /**
     * Returns a query that will match all assets whose provided field has a value that exactly equals
     * the provided boolean value.
     *
     * @param field name of the field to search
     * @param value the value (boolean) to check the field's value is exactly equal to
     * @return a query that will only match assets whose value for the field is exactly equal to the boolean value provided
     */
    static Query eq(final String field, final boolean value) {
        return TermQuery.of(t -> t.field(field).value(value))._toQuery();
    }
}
