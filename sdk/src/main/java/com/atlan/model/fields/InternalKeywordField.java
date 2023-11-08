/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

import lombok.Getter;

/**
 * Represents any field in Atlan that can be searched only by keyword (no text-analyzed fuzziness),
 * and can also be searched against a special internal field directly within Atlan.
 */
public class InternalKeywordField extends KeywordField implements IInternalSearchable {

    @Getter
    private final String internalFieldName;

    /**
     * Default constructor.
     *
     * @param atlan name of the attribute in the metastore
     * @param internal name of the internal searchable attribute in the metastore
     * @param keyword name of the keyword field in the search index
     */
    public InternalKeywordField(String atlan, String internal, String keyword) {
        super(atlan, keyword);
        this.internalFieldName = internal;
    }
}
