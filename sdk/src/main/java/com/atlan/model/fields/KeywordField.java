/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.model.enums.AtlanEnum;
import com.atlan.model.enums.ElasticRegexOperator;
import com.atlan.model.lineage.LineageFilterFieldString;
import java.util.Collection;
import java.util.List;

/**
 * Represents any field in Atlan that can be searched only by keyword (no text-analyzed fuzziness).
 */
public class KeywordField extends SearchableField implements IKeywordSearchable {

    @SuppressWarnings("this-escape")
    public final LineageFilterFieldString inLineage = new LineageFilterFieldString(this);

    /**
     * Default constructor.
     *
     * @param atlan name of the attribute in the metastore
     * @param keyword name of the keyword field in the search index
     */
    public KeywordField(String atlan, String keyword) {
        super(atlan, keyword);
    }

    /** {@inheritDoc} */
    @Override
    public String getKeywordFieldName() {
        return getElasticFieldName();
    }

    /** {@inheritDoc} */
    @Override
    public Query startsWith(String value, boolean caseInsensitive) {
        return IKeywordSearchable.startsWith(getKeywordFieldName(), value, caseInsensitive);
    }

    /** {@inheritDoc} */
    @Override
    public Query eq(AtlanEnum value) {
        return IKeywordSearchable.eq(getKeywordFieldName(), value);
    }

    /** {@inheritDoc} */
    @Override
    public Query eq(String value, boolean caseInsensitive) {
        return IKeywordSearchable.eq(getKeywordFieldName(), value, caseInsensitive);
    }

    /** {@inheritDoc} */
    @Override
    public Query in(Collection<String> values) {
        return IKeywordSearchable.in(getKeywordFieldName(), values);
    }

    /** {@inheritDoc} */
    @Override
    public Query in(List<String> values, int minMustMatch) {
        return IKeywordSearchable.in(getKeywordFieldName(), values, minMustMatch);
    }

    /** {@inheritDoc} */
    @Override
    public Query wildcard(String value, boolean caseInsensitive) {
        return IKeywordSearchable.wildcard(getKeywordFieldName(), value, caseInsensitive);
    }

    /** {@inheritDoc} */
    @Override
    public Query regex(String regexp, Collection<ElasticRegexOperator> flags, boolean caseInsensitive) {
        return IKeywordSearchable.regex(getKeywordFieldName(), regexp, flags, caseInsensitive);
    }
}
