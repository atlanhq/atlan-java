/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.*;

public interface ISearchable {
    /**
     * Returns a query that will only match assets that have some non-null, non-empty value
     * (no matter what actual value) for the field.
     *
     * @return a query that will only match assets that have some non-null, non-empty value
     *         (no matter what actual value) for the field
     */
    Query exists();

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

    /**
     * Returns criteria to calculate the approximate number of distinct values in the field across
     * all results. Note that this de-duplicates values, but is an <a href="https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-metrics-cardinality-aggregation.html">approximation</a>.
     *
     * @return criteria to calculate the approximate number of distinct values in a field across the results
     */
    Aggregation distinct();

    /**
     * Returns criteria to calculate the approximate number of distinct values in a field across
     * all results. Note that this de-duplicates values, but is an <a href="https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-metrics-cardinality-aggregation.html">approximation</a>.
     *
     * @param field for which to count distinct values
     * @return criteria to calculate the approximate number of distinct values in a field across the results
     */
    static Aggregation distinct(final String field) {
        return Aggregation.of(a -> a.cardinality(c -> c.field(field)));
    }

    /**
     * Return criteria to calculate the number of values in the field across all results.
     * Note that this does not de-duplicate.
     *
     * @return criteria to calculate the number of values in the provided field across the results
     */
    Aggregation count();

    /**
     * Return criteria to calculate the number of values in a field across all results.
     * Note that this does not de-duplicate.
     *
     * @param field for which to count values
     * @return criteria to calculate the number of values in the provided field across the results
     */
    static Aggregation count(final String field) {
        return Aggregation.of(a -> a.valueCount(v -> v.field(field)));
    }

    /**
     * Return criteria to bucket results based on the field.
     *
     * @return criteria to bucket results by the provided field
     */
    Aggregation bucketBy();

    /**
     * Return criteria to bucket results based on the provided field.
     *
     * @param field by which to bucket the results
     * @return criteria to bucket results by the provided field
     */
    static Aggregation bucketBy(final String field) {
        return Aggregation.of(a -> a.terms(t -> t.field(field)));
    }

    /**
     * Return a condition to sort results by the field, in the order specified.
     *
     * @param order the order in which to sort the results based on the values of the field
     * @return sort condition for the provided field, in the order specified
     */
    SortOptions order(SortOrder order);

    /**
     * Return a condition to sort results by the provided field, in the specified order.
     *
     * @param field by which to sort the results
     * @param order in which to sort the results
     * @return sort condition for the provided field, in the specified order
     */
    static SortOptions order(final String field, SortOrder order) {
        return SortOptions.of(s -> s.field(FieldSort.of(f -> f.field(field).order(order))));
    }
}
