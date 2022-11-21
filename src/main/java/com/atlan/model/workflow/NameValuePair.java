/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.workflow;

import com.atlan.model.core.AtlanObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class NameValuePair extends AtlanObject {
    private static final long serialVersionUID = 2L;

    String name;
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
}
