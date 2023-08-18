/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanEnum;
import java.util.Collection;

public class KeywordTextStemmedField extends SearchableField implements IKeywordSearchable, ITextSearchable, IStemmedSearchable {

    private final String textFieldName;
    private final String stemmedFieldName;

    public KeywordTextStemmedField(String atlan, String keyword, String text, String stemmed) {
        super(atlan, keyword);
        this.textFieldName = text;
        this.stemmedFieldName = stemmed;
    }

    /** {@inheritDoc} */
    @Override
    public String getKeywordFieldName() {
        return getElasticFieldName();
    }

    /** {@inheritDoc} */
    @Override
    public String getTextFieldName() {
        return textFieldName;
    }

    /** {@inheritDoc} */
    @Override
    public String getStemmedFieldName() {
        return stemmedFieldName;
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
    public Query match(String value) {
        return ITextSearchable.match(getTextFieldName(), value);
    }

    /** {@inheritDoc} */
    @Override
    public Query matchStemmed(String value) {
        return IStemmedSearchable.matchStemmed(getStemmedFieldName(), value);
    }
}
