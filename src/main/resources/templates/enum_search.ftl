/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023- Atlan Pte. Ltd. */
package com.atlan.model.enums;

import lombok.Getter;

public enum ${className} implements AtlanSearchableField {
<#list fields as field>
    /** ${field.description} */
    ${field.enumName}("${field.searchFieldName}"),
</#list>
    ;

    @Getter(onMethod_ = {@Override})
    private final String indexedFieldName;

    ${className}(String indexedFieldName) {
        this.indexedFieldName = indexedFieldName;
    }
}
