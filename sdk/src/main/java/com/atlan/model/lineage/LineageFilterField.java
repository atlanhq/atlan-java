/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.lineage;

import com.atlan.model.enums.AtlanComparisonOperator;
import com.atlan.model.fields.SearchableField;
import lombok.Getter;

/**
 * Class used to provide a proxy to building up a lineage filter with the appropriate
 * subset of conditions available.
 */
@Getter
public class LineageFilterField {

    /** Field on which filtering should be applied. */
    protected final SearchableField field;

    public LineageFilterField(SearchableField field) {
        this.field = field;
    }

    /**
     * Returns a filter that will match all assets whose provided field has any value at all (non-null).
     *
     * @return a filter that will only match assets that have some (non-null) value for the field
     */
    public LineageFilter hasAnyValue() {
        return build(AtlanComparisonOperator.NOT_NULL, "");
    }

    /**
     * Returns a filter that will match all assets whose provided field has no value at all (is null).
     *
     * @return a filter that will only match assets that have no value at all for the field (null)
     */
    public LineageFilter hasNoValue() {
        return build(AtlanComparisonOperator.IS_NULL, "");
    }

    /**
     * Utility method to build up a lineage filter from provided conditions.
     *
     * @param op operator to compare the field and value
     * @param value to compare the field's value with
     * @return the lineage filter with the provided conditions
     */
    protected LineageFilter build(AtlanComparisonOperator op, String value) {
        return LineageFilter.builder().field(field).operator(op).value(value).build();
    }
}
