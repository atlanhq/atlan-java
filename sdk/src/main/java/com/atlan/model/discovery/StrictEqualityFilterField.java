/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.discovery;

import java.util.List;

/**
 * Represents any field in Atlan that can be used for discovery only by exactly matching a value (positively).
 */
public class StrictEqualityFilterField extends DiscoveryFilterField {

    /**
     * Default constructor
     * @param field name of the field to filter by (singular)
     */
    public StrictEqualityFilterField(String field) {
        super(field);
    }

    /**
     * Default constructor
     * @param fields names of the fields to filter by (multiple)
     */
    public StrictEqualityFilterField(List<String> fields) {
        super(fields);
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that exactly equals
     * the provided value.
     *
     * @param value the value to check the field's value is exactly equal to
     * @return a query that will only match assets whose value for the field is exactly equal to the provided value
     */
    public DiscoveryFilter eq(String value) {
        return build("equals", value);
    }
}
