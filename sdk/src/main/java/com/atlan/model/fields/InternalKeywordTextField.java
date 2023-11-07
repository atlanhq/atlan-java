/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

import lombok.Getter;

/**
 * Represents any field in Atlan that can be searched by keyword or text-based search operations,
 * and can also be searched against a special internal field directly within Atlan.
 */
public class InternalKeywordTextField extends KeywordTextField implements IInternalSearchable {

    @Getter
    private final String internalFieldName;

    /**
     * Default constructor.
     *
     * @param atlan name of the attribute in the metastore
     * @param internal name of the internal searchable attribute in the metastore
     * @param keyword name of the keyword field in the search index
     * @param text name of the text field in the search index
     */
    public InternalKeywordTextField(String atlan, String internal, String keyword, String text) {
        super(atlan, keyword, text);
        this.internalFieldName = internal;
    }
}
