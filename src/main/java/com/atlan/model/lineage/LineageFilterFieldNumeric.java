/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.lineage;

import com.atlan.model.enums.AtlanComparisonOperator;
import com.atlan.model.fields.SearchableField;
import lombok.Getter;

/**
 * Class used to provide a proxy to building up a lineage filter with the appropriate
 * subset of conditions available, for numeric fields.
 */
@Getter
public class LineageFilterFieldNumeric extends LineageFilterField {

    public LineageFilterFieldNumeric(SearchableField field) {
        super(field);
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that is strictly less than
     * the provided value.
     *
     * @param value the value to check the field's value is strictly less than
     * @return a filter that will only match assets whose value for the field is strictly less than the value provided
     */
    public <T extends Number> LineageFilter lt(T value) {
        return build(AtlanComparisonOperator.LT, value.toString());
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that is strictly greater than
     * the provided value.
     *
     * @param value the value to check the field's value is strictly greater than
     * @return a filter that will only match assets whose value for the field is strictly greater than the value provided
     */
    public <T extends Number> LineageFilter gt(T value) {
        return build(AtlanComparisonOperator.GT, value.toString());
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that is less than or
     * equal to the provided value.
     *
     * @param value the value to check the field's value is less than or equal to
     * @return a filter that will only match assets whose value for the field is less than or equal to the value provided
     */
    public <T extends Number> LineageFilter lte(T value) {
        return build(AtlanComparisonOperator.LTE, value.toString());
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that is greater than or
     * equal to the provided value.
     *
     * @param value the value to check the field's value is greater than or equal to
     * @return a filter that will only match assets whose value for the field is greater than or equal to the value provided
     */
    public <T extends Number> LineageFilter gte(T value) {
        return build(AtlanComparisonOperator.GTE, value.toString());
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that is exactly
     * the provided value.
     *
     * @param value the value to check the field's value equals
     * @return a filter that will only match assets whose value for the field is exactly the value provided
     */
    public <T extends Number> LineageFilter eq(T value) {
        return build(AtlanComparisonOperator.EQ, value.toString());
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that is not exactly
     * the provided value.
     *
     * @param value the value to check the field's value does not equal
     * @return a filter that will only match assets whose value for the field is not exactly the value provided
     */
    public <T extends Number> LineageFilter neq(T value) {
        return build(AtlanComparisonOperator.NEQ, value.toString());
    }

    // TODO: TIME_RANGE
}
