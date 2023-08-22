/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

public interface IRankSearchable {
    /**
     * Returns the name of the numeric rank field index for this attribute in Elastic.
     *
     * @return the field name for the numeric rank index on this attribute
     */
    String getRankFieldName();
}
