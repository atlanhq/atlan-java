/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.lineage;

import com.atlan.model.enums.AtlanComparisonOperator;
import com.atlan.model.enums.AtlanEnum;
import com.atlan.model.fields.SearchableField;
import lombok.Getter;

/**
 * Class used to provide a proxy to building up a lineage filter with the appropriate
 * subset of conditions available, for string-searchable fields.
 */
@Getter
public class LineageFilterFieldString extends LineageFilterField {

    public LineageFilterFieldString(SearchableField field) {
        super(field);
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that is exactly
     * the provided value. Note that this is a case-sensitive match.
     *
     * @param value the value to check the field's value equals (case-sensitive)
     * @return a filter that will only match assets whose value for the field is exactly the value provided
     */
    public LineageFilter eq(String value) {
        return build(AtlanComparisonOperator.EQ, value);
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that is exactly
     * the provided value. Note that this is a case-sensitive match.
     *
     * @param value the value to check the field's value equals (case-sensitive)
     * @return a filter that will only match assets whose value for the field is exactly the value provided
     */
    public LineageFilter eq(AtlanEnum value) {
        return build(AtlanComparisonOperator.EQ, value.getValue());
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that is not exactly
     * the provided value. Note that this is a case-sensitive match.
     *
     * @param value the value to check the field's value does not equal (case-sensitive)
     * @return a filter that will only match assets whose value for the field is not exactly the value provided
     */
    public LineageFilter neq(String value) {
        return build(AtlanComparisonOperator.NEQ, value);
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that is not exactly
     * the provided value. Note that this is a case-sensitive match.
     *
     * @param value the value to check the field's value does not equal (case-sensitive)
     * @return a filter that will only match assets whose value for the field is not exactly the value provided
     */
    public LineageFilter neq(AtlanEnum value) {
        return build(AtlanComparisonOperator.NEQ, value.getValue());
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that starts with
     * the provided value. Note that this is a case-sensitive match.
     *
     * @param value the value (prefix) to check the field's value starts with (case-sensitive)
     * @return a filter that will only match assets whose value for the field starts with the value provided
     */
    public LineageFilter startsWith(String value) {
        return build(AtlanComparisonOperator.STARTS_WITH, value);
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that ends with
     * the provided value. Note that this is a case-sensitive match.
     *
     * @param value the value (suffix) to check the field's value ends with (case-sensitive)
     * @return a filter that will only match assets whose value for the field ends with the value provided
     */
    public LineageFilter endsWith(String value) {
        return build(AtlanComparisonOperator.ENDS_WITH, value);
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that contains
     * the provided value. Note that this is a case-sensitive match.
     *
     * @param value the value to check the field's value contains (case-sensitive)
     * @return a filter that will only match assets whose value for the field contains the value provided
     */
    public LineageFilter contains(String value) {
        return build(AtlanComparisonOperator.CONTAINS, value);
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that does not contain
     * the provided value. Note that this is a case-sensitive match.
     *
     * @param value the value to check the field's value does not contain (case-sensitive)
     * @return a filter that will only match assets whose value for the field does not contain the value provided
     */
    public LineageFilter doesNotContain(String value) {
        return build(AtlanComparisonOperator.NOT_CONTAINS, value);
    }

    // TODO: NOT_EMPTY, IN, LIKE, CONTAINS_ANY, CONTAINS_ALL
}
