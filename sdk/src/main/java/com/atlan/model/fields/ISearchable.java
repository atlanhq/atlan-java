/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.search.SourceConfig;
import co.elastic.clients.util.NamedValue;
import java.util.List;
import java.util.Map;

public interface ISearchable {
    String EMBEDDED_SOURCE_VALUE = "sourceValue";

    /**
     * Returns a query that will only match assets that have some non-null, non-empty value
     * (no matter what actual value) for the field.
     *
     * @return a query that will only match assets that have some non-null, non-empty value
     *         (no matter what actual value) for the field
     */
    Query hasAnyValue();

    /**
     * Returns a query that will only match assets that have some non-null, non-empty value
     * (no matter what actual value) for the field.
     *
     * @param field name of the field to search
     * @return a query that will only match assets that have some non-null, non-empty value
     *         (no matter what actual value) for the field
     */
    static Query hasAnyValue(final String field) {
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
     * Returns criteria to calculate the approximate number of distinct values in the field across
     * all results. Note that this de-duplicates values, but is an <a href="https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-metrics-cardinality-aggregation.html">approximation</a>.
     *
     * @param precision threshold for precision of the count
     * @return criteria to calculate the approximate number of distinct values in a field across the results
     */
    Aggregation distinct(int precision);

    /**
     * Returns criteria to calculate the approximate number of distinct values in a field across
     * all results. Note that this de-duplicates values, but is an <a href="https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-metrics-cardinality-aggregation.html">approximation</a>.
     *
     * @param field for which to count distinct values
     * @param precision threshold for precision of the count
     * @return criteria to calculate the approximate number of distinct values in a field across the results
     */
    static Aggregation distinct(final String field, final int precision) {
        return Aggregation.of(a -> a.cardinality(c -> c.field(field).precisionThreshold(precision)));
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
     * Note: this will only return details for the top 10 buckets with the most results in them.
     *
     * @return criteria to bucket results by the provided field
     */
    default Aggregation bucketBy() {
        return bucketBy(10);
    }

    /**
     * Return criteria to bucket results based on the provided field.
     * Note: this will only return details for the top 10 buckets with the most results in them.
     *
     * @param field by which to bucket the results
     * @return criteria to bucket results by the provided field
     */
    static Aggregation bucketBy(final String field) {
        return bucketBy(field, 10);
    }

    /**
     * Return criteria to bucket results based on the field.
     *
     * @param size the number of buckets to include results across.
     * @return criteria to bucket results by the provided field, across a maximum number of buckets defined by the provided size
     */
    default Aggregation bucketBy(int size) {
        return bucketBy(size, false);
    }

    /**
     * Return criteria to bucket results based on the provided field.
     *
     * @param field by which to bucket the results
     * @param size the number of buckets to include results across.
     * @return criteria to bucket results by the provided field, across a maximum number of buckets defined by the provided size
     */
    static Aggregation bucketBy(final String field, final int size) {
        return bucketBy(field, size, null);
    }

    /**
     * Return criteria to bucket results based on the provided field.
     *
     * @param size the number of buckets to include results across.
     * @param includeSourceValue if true, include the actual source value of this attribute for each bucket will be included in the result
     * @return criteria to bucket results by the provided field, across a maximum number of buckets defined by the provided size
     */
    Aggregation bucketBy(int size, boolean includeSourceValue);

    /**
     * Return criteria to bucket results based on the provided field.
     *
     * @param field by which to bucket the results
     * @param size the number of buckets to include results across
     * @param sourceAttribute if non-null, the actual source value of this attribute for the bucket will be included in the result
     * @return criteria to bucket results by the provided field, across a maximum number of buckets defined by the provided size
     */
    static Aggregation bucketBy(final String field, final int size, final String sourceAttribute) {
        if (sourceAttribute != null && !sourceAttribute.isEmpty()) {
            return bucketBy(field, size, Map.of(EMBEDDED_SOURCE_VALUE, topHits(sourceAttribute)), null);
        } else {
            return Aggregation.of(a -> a.terms(t -> t.field(field).size(size)));
        }
    }

    /**
     * Return criteria to bucket results based on the provided field.
     *
     * @param size the number of buckets to include results across.
     * @param nested a map of nested aggregations, from arbitrary string key to aggregation
     * @param order (optional) named value from aggregation key (string) to sort order
     * @return criteria to bucket results by the provided field, across a maximum number of buckets, with nested (sub)aggregations
     */
    Aggregation bucketBy(int size, Map<String, Aggregation> nested, List<NamedValue<SortOrder>> order);

    /**
     * Return criteria to bucket results based on the provided field, including nested
     * aggregations and (optionally) sorting the top-level buckets by one of the nested
     * aggregation's results.
     *
     * @param field by which to bucket the results
     * @param size the number of buckets to include results across
     * @param nested a map of nested aggregations, from arbitrary string key to aggregation
     * @param order (optional) named value from aggregation key (string) to sort order
     * @return criteria to bucket results by the provided field, across a maximum number of buckets, with nested (sub)aggregations
     */
    static Aggregation bucketBy(
            final String field,
            final int size,
            final Map<String, Aggregation> nested,
            final List<NamedValue<SortOrder>> order) {
        if (nested != null) {
            if (order != null) {
                return Aggregation.of(a ->
                        a.terms(t -> t.field(field).size(size).order(order)).aggregations(nested));
            } else {
                return Aggregation.of(
                        a -> a.terms(t -> t.field(field).size(size)).aggregations(nested));
            }
        } else {
            return Aggregation.of(a -> a.terms(t -> t.field(field).size(size)));
        }
    }

    /**
     * Return criteria to give the top hits for values in the provided field.
     *
     * @param field for which to give the top values
     * @return criteria to give the top hits for values in the provided field
     */
    private static Aggregation topHits(final String field) {
        return Aggregation.of(
                h -> h.topHits(t -> t.size(1).source(SourceConfig.of(c -> c.filter(f -> f.includes(field))))));
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
