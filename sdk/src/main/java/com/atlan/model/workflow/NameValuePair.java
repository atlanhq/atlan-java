/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.workflow;

import com.atlan.model.core.AtlanObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Definition of arbitrary name-value pairs.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class NameValuePair extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Name or key of the pair. */
    String name;

    /** Value for the pair. */
    Object value;

    /**
     * Build a NameValuePair from the provided name and value.
     *
     * @param name for the pair
     * @param value for the name
     * @return the NameValuePair
     */
    public static NameValuePair of(String name, String value) {
        return NameValuePair.builder().name(name).value(value).build();
    }

    /**
     * Build a NameValuePair from the provided name and value.
     *
     * @param name for the pair
     * @param value for the name
     * @return the NameValuePair
     */
    public static NameValuePair of(String name, boolean value) {
        return NameValuePair.builder().name(name).value(value).build();
    }
}
