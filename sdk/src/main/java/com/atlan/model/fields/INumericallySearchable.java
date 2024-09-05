/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.json.JsonData;

public interface INumericallySearchable {
    /**
     * Returns the name of the numeric field index for this attribute in Elastic.
     *
     * @return the field name for the numeric index on this attribute
     */
    String getNumericFieldName();

    /**
     * Returns a query that will match all assets whose provided field has a value that exactly
     * matches the provided numeric value.
     *
     * @param value the numeric value to exactly match
     * @return a query that will only match assets whose value for the field is exactly the numeric value provided
     * @param <T> numeric values
     */
    <T extends Number> Query eq(T value);

    /**
     * Returns a query that will match all assets whose provided field has a value that exactly
     * matches the provided numeric value.
     *
     * @param field name of the field to search
     * @param value the numeric value to exactly match
     * @return a query that will only match assets whose value for the field is exactly the numeric value provided
     * @param <T> numeric values
     */
    static <T extends Number> Query eq(final String field, final T value) {
        return TermQuery.of(f -> f.field(field).value(FieldValue.of(JsonData.of(value))))
                ._toQuery();
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that is strictly greater
     * than the provided numeric value.
     *
     * @param value the numeric value to compare against
     * @return a query that will only match assets whose value for the field is strictly greater than the numeric value provided
     * @param <T> numeric values
     */
    <T extends Number> Query gt(T value);

    /**
     * Returns a query that will match all assets whose provided field has a value that is strictly greater
     * than the provided numeric value.
     *
     * @param field name of the field to search
     * @param value the numeric value to compare against
     * @return a query that will only match assets whose value for the field is strictly greater than the numeric value provided
     * @param <T> numeric values
     */
    static <T extends Number> Query gt(final String field, final T value) {
        return RangeQuery.of(r -> r.untyped(u -> u.field(field).gt(JsonData.of(value))))
                ._toQuery();
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that is greater
     * than or equal to the provided numeric value.
     *
     * @param value the numeric value to compare against
     * @return a query that will only match assets whose value for the field is greater than or equal to the numeric value provided
     * @param <T> numeric values
     */
    <T extends Number> Query gte(T value);

    /**
     * Returns a query that will match all assets whose provided field has a value that is greater
     * than or equal to the provided numeric value.
     *
     * @param field name of the field to search
     * @param value the numeric value to compare against
     * @return a query that will only match assets whose value for the field is greater than or equal to the numeric value provided
     * @param <T> numeric values
     */
    static <T extends Number> Query gte(final String field, final T value) {
        return RangeQuery.of(r -> r.untyped(u -> u.field(field).gte(JsonData.of(value))))
                ._toQuery();
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that is strictly less
     * than the provided numeric value.
     *
     * @param value the numeric value to compare against
     * @return a query that will only match assets whose value for the field is strictly less than the numeric value provided
     * @param <T> numeric values
     */
    <T extends Number> Query lt(T value);

    /**
     * Returns a query that will match all assets whose provided field has a value that is strictly less
     * than the provided numeric value.
     *
     * @param field name of the field to search
     * @param value the numeric value to compare against
     * @return a query that will only match assets whose value for the field is strictly less than the numeric value provided
     * @param <T> numeric values
     */
    static <T extends Number> Query lt(final String field, final T value) {
        return RangeQuery.of(r -> r.untyped(u -> u.field(field).lt(JsonData.of(value))))
                ._toQuery();
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that is less
     * than or equal to the provided numeric value.
     *
     * @param value the numeric value to compare against
     * @return a query that will only match assets whose value for the field is less than or equal to the numeric value provided
     * @param <T> numeric values
     */
    <T extends Number> Query lte(T value);

    /**
     * Returns a query that will match all assets whose provided field has a value that is less
     * than or equal to the provided numeric value.
     *
     * @param field name of the field to search
     * @param value the numeric value to compare against
     * @return a query that will only match assets whose value for the field is less than or equal to the numeric value provided
     * @param <T> numeric values
     */
    static <T extends Number> Query lte(final String field, final T value) {
        return RangeQuery.of(r -> r.untyped(u -> u.field(field).lte(JsonData.of(value))))
                ._toQuery();
    }

    /**
     * Returns a query that will match all assets whose provided field has a value between the
     * minimum and maximum specified values, inclusive.
     *
     * @param min minimum value of the field that will match (inclusive)
     * @param max maximum value of the field that will match (inclusive)
     * @return a query that will only match assets whose value for the field is between the min and max (both inclusive)
     * @param <T> numeric values
     */
    <T extends Number> Query between(T min, T max);

    /**
     * Returns a query that will match all assets whose provided field has a value between the
     * minimum and maximum specified values, inclusive.
     *
     * @param field name of the field to search
     * @param min minimum value of the field that will match (inclusive)
     * @param max maximum value of the field that will match (inclusive)
     * @return a query that will only match assets whose value for the field is between the min and max (both inclusive)
     * @param <T> numeric values
     */
    static <T extends Number> Query between(final String field, final T min, final T max) {
        return RangeQuery.of(
                        r -> r.untyped(u -> u.field(field).gte(JsonData.of(min)).lte(JsonData.of(max))))
                ._toQuery();
    }

    /**
     * Return criteria to calculate a sum of the values of the field across all results.
     *
     * @return criteria to calculate the sum of the values of the provided field across the results
     */
    Aggregation sum();

    /**
     * Return criteria to calculate a sum of the values of the provided field across all
     * results.
     *
     * @param field for which to find the sum of values
     * @return criteria to calculate the sum of the values of the provided field across the results
     */
    static Aggregation sum(final String field) {
        return Aggregation.of(a -> a.sum(s -> s.field(field)));
    }

    /**
     * Return criteria to calculate the average value of the field across all results.
     *
     * @return criteria to calculate the average value of the provided field across the results
     */
    Aggregation avg();

    /**
     * Return criteria to calculate the average value of the provided field across all
     * results.
     *
     * @param field for which to find the average value
     * @return criteria to calculate the average value of the provided field across the results
     */
    static Aggregation avg(final String field) {
        return Aggregation.of(a -> a.avg(s -> s.field(field)));
    }

    /**
     * Return criteria to calculate the minimum value of the field across all results.
     *
     * @return criteria to calculate the minimum value of the provided field across the results
     */
    Aggregation min();

    /**
     * Return criteria to calculate the minimum value of the provided field across all
     * results.
     *
     * @param field for which to find the minimum value
     * @return criteria to calculate the minimum value of the provided field across the results
     */
    static Aggregation min(final String field) {
        return Aggregation.of(a -> a.min(s -> s.field(field)));
    }

    /**
     * Return criteria to calculate the maximum value of the field across all results.
     *
     * @return criteria to calculate the maximum value of the provided field across the results
     */
    Aggregation max();

    /**
     * Return criteria to calculate the maximum value of the provided field across all
     * results.
     *
     * @param field for which to find the maximum value
     * @return criteria to calculate the maximum value of the provided field across the results
     */
    static Aggregation max(final String field) {
        return Aggregation.of(a -> a.max(s -> s.field(field)));
    }
}
