/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.model.lineage.LineageFilterFieldBoolean;

/**
 * Represents any field in Atlan that can be searched only by truthiness.
 */
public class BooleanField extends SearchableField implements IBooleanSearchable {

    @SuppressWarnings("this-escape")
    public final LineageFilterFieldBoolean inLineage = new LineageFilterFieldBoolean(this);

    /**
     * Default constructor.
     *
     * @param atlan name of the attribute in the metastore
     * @param bool name of the boolean field in the search index
     */
    public BooleanField(String atlan, String bool) {
        super(atlan, bool);
    }

    /** {@inheritDoc} */
    @Override
    public String getBooleanFieldName() {
        return getElasticFieldName();
    }

    /** {@inheritDoc} */
    @Override
    public Query eq(boolean value) {
        return IBooleanSearchable.eq(getBooleanFieldName(), value);
    }
}
