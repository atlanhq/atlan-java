/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.model.lineage.LineageFilterFieldString;

/**
 * Represents any field in Atlan that can only be searched using text-related search operations.
 */
public class TextField extends SearchableField implements ITextSearchable {

    @SuppressWarnings("this-escape")
    public final LineageFilterFieldString inLineage = new LineageFilterFieldString(this);

    /**
     * Default constructor.
     *
     * @param atlan name of the attribute in the metastore
     * @param text name of the text field in the search index
     */
    public TextField(String atlan, String text) {
        super(atlan, text);
    }

    /** {@inheritDoc} */
    @Override
    public String getTextFieldName() {
        return getElasticFieldName();
    }

    /** {@inheritDoc} */
    @Override
    public Query match(String value) {
        return ITextSearchable.match(getTextFieldName(), value);
    }
}
