/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.discovery;

/**
 * Represents any field in Atlan that can be used for discovery by truthiness.
 */
public class BooleanFilterField extends DiscoveryFilterField {

    /**
     * Default constructor
     * @param field name of the field to filter by (singular)
     */
    public BooleanFilterField(String field) {
        super(field);
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that exactly equals
     * the provided boolean value.
     *
     * @param value the value (boolean) to check the field's value is exactly equal to
     * @return a query that will only match assets whose value for the field is exactly equal to the boolean value provided
     */
    public DiscoveryFilter eq(boolean value) {
        return build("boolean", value);
    }
}
