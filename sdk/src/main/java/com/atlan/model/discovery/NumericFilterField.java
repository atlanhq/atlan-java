/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.discovery;

import lombok.Getter;

/**
 * Represents any field in Atlan that can be used for discovery by numerical comparison.
 */
@Getter
public class NumericFilterField extends DiscoveryFilterField {

    /**
     * Default constructor
     * @param field name of the field to filter by (singular)
     */
    public NumericFilterField(String field) {
        super(field);
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that exactly equals
     * the provided value.
     *
     * @param value the value to check the field's value is exactly equal to
     * @return a query that will only match assets whose value for the field is exactly equal to the provided value
     */
    public <T extends Number> DiscoveryFilter eq(T value) {
        return build("equals", value);
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that does not exactly equal
     * the provided value.
     *
     * @param value the value to check the field's value is NOT exactly equal to
     * @return a query that will only match assets whose value for the field is not exactly equal to the provided value
     */
    public DiscoveryFilter neq(String value) {
        return build("notEquals", value);
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that starts with
     * the provided value.
     *
     * @param value the value to check the field's value starts with
     * @return a query that will only match assets whose value for the field starts with the provided value
     */
    public DiscoveryFilter startsWith(String value) {
        return build("startsWith", value);
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that ends with
     * the provided value.
     *
     * @param value the value to check the field's value ends with
     * @return a query that will only match assets whose value for the field ends with the provided value
     */
    public DiscoveryFilter endsWith(String value) {
        return build("endsWith", value);
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that contains
     * the provided value.
     *
     * @param value the value to check the field's value contains
     * @return a query that will only match assets whose value for the field contains the provided value
     */
    public DiscoveryFilter contains(String value) {
        return build("contains", value);
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that does not contain
     * the provided value.
     *
     * @param value the value to check the field's value does NOT contain
     * @return a query that will only match assets whose value for the field does not contain the provided value
     */
    public DiscoveryFilter doesNotContain(String value) {
        return build("notContains", value);
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that matches the provided
     * regular expression pattern.
     *
     * @param value the regular expression to check the field's value matches
     * @return a query that will only match assets whose value for the field matches the provided regular expression
     */
    public DiscoveryFilter regex(String value) {
        return build("pattern", value);
    }
}
