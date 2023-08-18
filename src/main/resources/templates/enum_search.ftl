/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023- Atlan Pte. Ltd. */
package ${packageRoot}.enums;

import com.atlan.model.enums.AtlanSearchableField;
import lombok.Getter;
import javax.annotation.processing.Generated;

/**
 * Enumeration of all index fields.
 * @deprecated these are now enumerated as constants in the various asset interfaces, as instances of one of the following: {@link com.atlan.model.fields.BooleanField}, {@link com.atlan.model.fields.KeywordField}, {@link com.atlan.model.fields.KeywordTextField}, {@link com.atlan.model.fields.KeywordTextStemmedField}, {@link com.atlan.model.fields.NumericField}, {@link com.atlan.model.fields.NumericRankField}, {@link com.atlan.model.fields.RelationField}, or {@link com.atlan.model.fields.TextField}
 */
@Deprecated
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
