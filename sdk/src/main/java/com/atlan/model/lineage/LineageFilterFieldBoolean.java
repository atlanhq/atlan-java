/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.lineage;

import com.atlan.model.enums.AtlanComparisonOperator;
import com.atlan.model.fields.SearchableField;
import lombok.Getter;

/**
 * Class used to provide a proxy to building up a lineage filter with the appropriate
 * subset of conditions available, for boolean fields.
 */
@Getter
public class LineageFilterFieldBoolean extends LineageFilterField {

    public LineageFilterFieldBoolean(SearchableField field) {
        super(field);
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that is exactly
     * the provided value.
     *
     * @param value the value to check the field's value equals
     * @return a filter that will only match assets whose value for the field is exactly the value provided
     */
    public LineageFilter eq(Boolean value) {
        return build(AtlanComparisonOperator.EQ, value.toString());
    }

    /**
     * Returns a filter that will match all assets whose provided field has a value that is not exactly
     * the provided value.
     *
     * @param value the value to check the field's value does not equal
     * @return a filter that will only match assets whose value for the field is not exactly the value provided
     */
    public LineageFilter neq(Boolean value) {
        return build(AtlanComparisonOperator.NEQ, value.toString());
    }
}
