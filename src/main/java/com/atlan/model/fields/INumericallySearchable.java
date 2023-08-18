/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

import co.elastic.clients.elasticsearch._types.FieldValue;
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
        return RangeQuery.of(r -> r.field(field).gt(JsonData.of(value)))._toQuery();
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
        return RangeQuery.of(r -> r.field(field).gte(JsonData.of(value)))._toQuery();
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
        return RangeQuery.of(r -> r.field(field).lt(JsonData.of(value)))._toQuery();
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
        return RangeQuery.of(r -> r.field(field).lte(JsonData.of(value)))._toQuery();
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
        return RangeQuery.of(r -> r.field(field).gte(JsonData.of(min)).lte(JsonData.of(max)))
                ._toQuery();
    }
}
