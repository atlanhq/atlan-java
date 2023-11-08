/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

public interface IInternalSearchable {
    /**
     * Returns the name of the internal field for this attribute in Atlan.
     *
     * @return the field name for the internal (searchable) field in Atlan for this attribute
     */
    String getInternalFieldName();
}
