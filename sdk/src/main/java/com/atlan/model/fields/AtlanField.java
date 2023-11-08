/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

import lombok.Getter;

/**
 * Base enumeration of all attributes that exist in Atlan, so you do not have to remember their
 * exact spelling or capitalization.
 */
@Getter
public abstract class AtlanField {
    private final String atlanFieldName;

    public AtlanField(String atlan) {
        this.atlanFieldName = atlan;
    }
}
