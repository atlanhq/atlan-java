/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.discovery;

import java.util.Collection;

/**
 * Represents any field in Atlan that can be used for discovery by a predefined set of values.
 */
public class EnumFilterField extends DiscoveryFilterField {

    /**
     * Default constructor
     * @param field name of the field to filter by (singular)
     */
    public EnumFilterField(String field) {
        super(field);
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that exactly equals
     * one of the provided enumerated values.
     *
     * @param values the values to check the field's value is exactly equal to
     * @return a query that will only match assets whose value for the field is exactly equal to one of the provided values
     */
    public DiscoveryFilter in(Collection<String> values) {
        return build("equals", values);
    }
}
