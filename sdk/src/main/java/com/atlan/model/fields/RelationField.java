/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

/**
 * Represents any field used to capture a relationship in Atlan, which is not inherently
 * searchable.
 */
public class RelationField extends AtlanField {

    /**
     * Default constructor.
     *
     * @param atlan name of the attribute in the metastore
     */
    public RelationField(String atlan) {
        super(atlan);
    }
}
