/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023- Atlan Pte. Ltd. */
package ${packageRoot}.enums;

import com.atlan.model.enums.AtlanSearchableField;
import lombok.Getter;
import javax.annotation.processing.Generated;

@Generated(value="${generatorName}")
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
