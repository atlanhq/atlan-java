/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.discovery;

import com.atlan.model.enums.AtlanComparisonOperator;
import lombok.Getter;

import java.util.List;

/**
 * Class used to provide a proxy to building up a linkable query filter with the appropriate
 * subset of conditions available for a given field (property).
 */
@Getter
public class DiscoveryFilterField {

    /** Field on which filtering should be applied. */
    protected final List<String> fields;

    /** Singular field on which to index the filter map. */
    protected final String filerKey;

    /**
     * Default constructor
     * @param field name of the field to filter by (singular)
     */
    public DiscoveryFilterField(String field) {
        this.filerKey = field;
        this.fields = List.of(field);
    }

    /**
     * Default constructor
     * @param fields names of the fields to filter by (multiple)
     */
    public DiscoveryFilterField(List<String> fields) {
        this.filerKey = fields.get(0);
        this.fields = fields;
    }

    /**
     * Returns a filter that will match all assets whose provided field has any value at all (non-null).
     *
     * @return a filter that will only match assets that have some (non-null) value for the field
     */
    public DiscoveryFilter hasAnyValue() {
        return build("isNotNull", "");
    }

    /**
     * Returns a filter that will match all assets whose provided field has no value at all (is null).
     *
     * @return a filter that will only match assets that have no value at all for the field (null)
     */
    public DiscoveryFilter hasNoValue() {
        return build("isNull", "");
    }

    /**
     * Utility method to build up a lineage filter from provided conditions.
     *
     * @param op operator to compare the field and value
     * @param value to compare the field's value with
     * @return the lineage filter with the provided conditions
     */
    protected DiscoveryFilter build(String op, Object value) {
        return DiscoveryFilter._internal()
            .filterKey(filerKey)
            .operand(fields.size() > 1 ? fields : fields.get(0))
            .operator(op)
            .value(value)
            .build();
    }
}
