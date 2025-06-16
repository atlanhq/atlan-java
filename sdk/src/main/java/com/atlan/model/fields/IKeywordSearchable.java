/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.ScriptLanguage;
import co.elastic.clients.elasticsearch._types.ScriptSource;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import com.atlan.model.enums.AtlanEnum;
import com.atlan.model.enums.ElasticRegexOperator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface IKeywordSearchable {
    /**
     * Returns the name of the keyword field index for this attribute in Elastic.
     *
     * @return the field name for the keyword index on this attribute
     */
    String getKeywordFieldName();

    /**
     * Returns a query that will match all assets whose provided field has a value that starts with
     * the provided value. Note that this is a case-sensitive match.
     *
     * @param value the value (prefix) to check the field's value starts with (case-sensitive)
     * @return a query that will only match assets whose value for the field starts with the value provided
     */
    default Query startsWith(String value) {
        return startsWith(value, false);
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that starts with
     * the provided value. Note that this can also be a case-insensitive match.
     *
     * @param value the value (prefix) to check the field's value starts with
     * @param caseInsensitive if true will match the value irrespective of case, otherwise will be a case-sensitive match
     * @return a query that will only match assets whose value for the field starts with the value provided
     */
    Query startsWith(String value, boolean caseInsensitive);

    /**
     * Returns a query that will match all assets whose provided field has a value that starts with
     * the provided value. Note that this can also be a case-insensitive match.
     *
     * @param field name of the field to search
     * @param value the value (prefix) to check the field's value starts with (case-sensitive)
     * @param caseInsensitive if true will match the value irrespective of case, otherwise will be a case-sensitive match
     * @return a query that will only match assets whose value for the field starts with the value provided
     */
    static Query startsWith(final String field, final String value, final boolean caseInsensitive) {
        return PrefixQuery.of(t -> t.field(field).value(value).caseInsensitive(caseInsensitive))
                ._toQuery();
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that exactly equals
     * the provided enumerated value.
     *
     * @param value the value (enumerated) to check the field's value is exactly equal to
     * @return a query that will only match assets whose value for the field is exactly equal to the enumerated value provided
     */
    Query eq(AtlanEnum value);

    /**
     * Returns a query that will match all assets whose provided field has a value that exactly equals
     * the provided enumerated value.
     *
     * @param field name of the field to search
     * @param value the value (enumerated) to check the field's value is exactly equal to
     * @return a query that will only match assets whose value for the field is exactly equal to the enumerated value provided
     */
    static Query eq(final String field, final AtlanEnum value) {
        return TermQuery.of(t -> t.field(field).value(value.getValue()))._toQuery();
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that exactly equals
     * the provided string value.
     *
     * @param value the value (string) to check the field's value is exactly equal to
     * @return a query that will only match assets whose value for the field is exactly equal to the string value provided
     */
    default Query eq(String value) {
        return eq(value, false);
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that exactly equals
     * the provided string value. Note that this can also be modified to ignore case when doing the exact match.
     *
     * @param value the value (string) to check the field's value is exactly equal to
     * @param caseInsensitive if true will match the value irrespective of case, otherwise will be a case-sensitive match
     * @return a query that will only match assets whose value for the field is exactly equal to the string value provided (optionally case-insensitive)
     */
    Query eq(String value, boolean caseInsensitive);

    /**
     * Returns a query that will match all assets whose provided field has a value that exactly equals
     * the provided string value. Note that this can also be modified to ignore case when doing the exact match.
     *
     * @param field name of the field to search
     * @param value the value (string) to check the field's value is exactly equal to
     * @param caseInsensitive if true will match the value irrespective of case, otherwise will be a case-sensitive match
     * @return a query that will only match assets whose value for the field is exactly equal to the string value provided (optionally case-insensitive)
     */
    static Query eq(final String field, final String value, final boolean caseInsensitive) {
        return TermQuery.of(t -> t.field(field).value(value).caseInsensitive(caseInsensitive))
                ._toQuery();
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that exactly equals
     * at least one of the provided string values.
     *
     * @param values the values (strings) to check the field's value is exactly equal to
     * @return a query that will only match assets whose value for the field is exactly equal to at least one of the string values provided
     */
    Query in(Collection<String> values);

    /**
     * Returns a query that will match all assets whose provided field has a value that exactly equals
     * at least one of the provided string values.
     *
     * @param field name of the field to search
     * @param values the values (strings) to check the field's value is exactly equal to
     * @return a query that will only match assets whose value for the field is exactly equal to at least one of the string values provided
     * @see #in(Collection)
     */
    static Query in(final String field, final Collection<String> values) {
        List<FieldValue> list = new ArrayList<>();
        for (String value : values) {
            list.add(FieldValue.of(value));
        }
        return TermsQuery.of(t -> t.field(field).terms(TermsQueryField.of(f -> f.value(list))))
                ._toQuery();
    }

    /**
     * Returns a query that will match all assets whose provided field has multiple values that exactly equal
     * a specified number of the provided string values.
     *
     * @param values the values (strings) to check the field's values are exactly equal to
     * @param minMustMatch minimum number of values from the provided list that must be contained in the field's values
     * @return a query that will only match assets whose values for the field are exactly equal to a minimal number of the string values provided
     */
    Query in(List<String> values, int minMustMatch);

    /**
     * Returns a query that will match all assets whose provided field has multiple values that exactly equal
     * a specified number of the provided string values.
     *
     * @param field name of the field to search
     * @param values the values (strings) to check the field's values are exactly equal to
     * @param minMustMatch minimum number of values from the provided list that must be contained in the field's values
     * @return a query that will only match assets whose values for the field are exactly equal to a minimal number of the string values provided
     * @see #in(Collection)
     */
    static Query in(final String field, final List<String> values, final int minMustMatch) {
        List<FieldValue> vals = values.stream().map(FieldValue::of).collect(Collectors.toList());
        return TermsSetQuery.of(
                        t -> t.field(field).terms(vals).minimumShouldMatchScript(s -> s.lang(ScriptLanguage.Painless)
                                .source(ScriptSource.of(f -> f.scriptString("" + minMustMatch)))))
                ._toQuery();
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that matches the
     * wildcard expression provided. This is similar to regex matching, but only allows * (match zero or
     * more characters) and ? (match any single character) operators.
     *
     * @param value the value (containing either * or ?) to match against the field's values
     * @return a query that will only match assets whose value for the field matches the wildcard expression provided
     */
    default Query wildcard(String value) {
        return wildcard(value, false);
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that matches the
     * wildcard expression provided. This is similar to regex matching, but only allows * (match zero or
     * more characters) and ? (match any single character) operators.
     *
     * @param value the value (containing either * or ?) to match against the field's values
     * @param caseInsensitive if true will match the value irrespective of case, otherwise will be a case-sensitive match
     * @return a query that will only match assets whose value for the field matches the wildcard expression provided
     */
    Query wildcard(String value, boolean caseInsensitive);

    /**
     * Returns a query that will match all assets whose provided field has a value that matches the
     * wildcard expression provided. This is similar to regex matching, but only allows * (match zero or
     * more characters) and ? (match any single character) operators.
     *
     * @param field name of the field to search
     * @param value the value (containing either * or ?) to match against the field's values
     * @param caseInsensitive if true will match the value irrespective of case, otherwise will be a case-sensitive match
     * @return a query that will only match assets whose value for the field matches the wildcard expression provided
     * @see #wildcard(String, boolean)
     */
    static Query wildcard(final String field, final String value, final boolean caseInsensitive) {
        return WildcardQuery.of(w -> w.field(field).value(value).caseInsensitive(caseInsensitive))
                ._toQuery();
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that matches the
     * regular expression provided.
     *
     * @param regexp regular expression to match values against
     * @return a query that will only match assets whose value for the field matches the regular expression provided
     */
    default Query regex(String regexp) {
        return regex(regexp, false);
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that matches the
     * regular expression provided.
     *
     * @param regexp regular expression to match values against
     * @param caseInsensitive if true will match the value irrespective of case, otherwise will be a case-sensitive match
     * @return a query that will only match assets whose value for the field matches the regular expression provided
     */
    default Query regex(String regexp, boolean caseInsensitive) {
        return regex(regexp, null, caseInsensitive);
    }

    /**
     * Returns a query that will match all assets whose provided field has a value that matches the
     * regular expression provided.
     *
     * @param regexp regular expression to match values against
     * @param flags optional set of operators to enable in the regular expression engine
     * @param caseInsensitive if true will match the value irrespective of case, otherwise will be a case-sensitive match
     * @return a query that will only match assets whose value for the field matches the regular expression provided
     */
    Query regex(String regexp, Collection<ElasticRegexOperator> flags, boolean caseInsensitive);

    /**
     * Returns a query that will match all assets whose provided field has a value that matches the
     * regular expression provided.
     *
     * @param field name of the field to search
     * @param regexp regular expression to match values against
     * @param flags optional set of operators to enable in the regular expression engine
     * @param caseInsensitive if true will match the value irrespective of case, otherwise will be a case-sensitive match
     * @return a query that will only match assets whose value for the field matches the regular expression provided
     * @see #regex(String, Collection, boolean)
     */
    static Query regex(
            final String field,
            final String regexp,
            final Collection<ElasticRegexOperator> flags,
            final boolean caseInsensitive) {
        final String flagsToUse;
        if (flags != null && !flags.isEmpty()) {
            flagsToUse = flags.stream().map(ElasticRegexOperator::getValue).collect(Collectors.joining("|"));
        } else {
            flagsToUse = "";
        }
        return RegexpQuery.of(r -> r.field(field)
                        .value(regexp)
                        .caseInsensitive(caseInsensitive)
                        .flags(flagsToUse))
                ._toQuery();
    }
}
