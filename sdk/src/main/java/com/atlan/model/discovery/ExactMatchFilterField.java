/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.discovery;

import java.util.List;

/**
 * Represents any field in Atlan that can be used for discovery by either exactly matching (or not matching) a value.
 */
public class ExactMatchFilterField extends StrictEqualityFilterField {

    /**
     * Default constructor
     * @param field name of the field to filter by (singular)
     */
    public ExactMatchFilterField(String field) {
        super(field);
    }

    /**
     * Default constructor
     * @param fields names of the fields to filter by (multiple)
     */
    public ExactMatchFilterField(List<String> fields) {
        super(fields);
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
}
