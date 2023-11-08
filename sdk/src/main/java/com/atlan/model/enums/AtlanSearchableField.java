/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.enums;

/**
 * Interface implemented by all enums to get the actual string value required by the Atlan API.
 * @deprecated see {@link com.atlan.model.fields.SearchableField} instead
 */
@Deprecated
public interface AtlanSearchableField {
    String getIndexedFieldName();
}
