/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.util;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.enums.*;
import com.atlan.model.search.AggregationMetricResult;
import com.atlan.model.search.AggregationResult;
import java.util.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

public class QueryFactory {

    private static final Query ACTIVE = have(KeywordFields.STATE).eq(AtlanStatus.ACTIVE.getValue());
    private static final Query ARCHIVED = have(KeywordFields.STATE).eq(AtlanStatus.DELETED.getValue());
    private static final Query WITH_LINEAGE = have(BooleanFields.HAS_LINEAGE).eq(true);

    /**
     * Returns a query that will only match active assets.
     *
     * @return a query that will only match active assets
     */
    public static Query beActive() {
        return ACTIVE;
    }

    /**
     * Returns a query that will only match soft-deleted (archived) assets.
     *
     * @return a query that will only match soft-deleted (archived) assets
     */
    public static Query beArchived() {
        return ARCHIVED;
    }

    /**
     * Returns a query that will only match assets with lineage.
     *
     * @return a query that will only match assets with lineage
     */
    public static Query haveLineage() {
        return WITH_LINEAGE;
    }

    /**
     * Returns a query that will only match assets of the type provided.
     *
     * @param typeName for assets to match
     * @return a query that will only match assets of the type provided
     */
    public static Query beOfType(String typeName) {
        return have(KeywordFields.TYPE_NAME).eq(typeName);
    }

    /**
     * Returns a query that will match all assets that are a subtype of the type provided.
     *
     * @param typeName of the supertype for assets to match
     * @return a query that will only match assets of a subtype of the type provided
     */
    public static Query haveSuperType(String typeName) {
        return have(KeywordFields.SUPER_TYPE_NAMES).eq(typeName);
    }

    /**
     * Returns a query that will match all assets that are one of the types provided.
     *
     * @param typeNames collection of types that assets need to match one of
     * @return a query that will only match assets that are one of the types provided
     */
    public static Query beOneOfTypes(Collection<String> typeNames) {
        return have(KeywordFields.TYPE_NAME).beOneOf(typeNames);
    }

    /**
     * Returns a query that will only match assets that have at least one of the Atlan tags
     * provided. This will match irrespective of the Atlan tag being directly applied to the
     * asset, or if it was propagated to the asset.
     *
     * @param atlanTagNames human-readable names of the Atlan tags
     * @return a query that will only match assets that have at least one of the Atlan tags provided
     * @throws AtlanException on any error communicating with the API to refresh the Atlan tag cache
     */
    public static Query beTaggedByAtLeastOneOf(Collection<String> atlanTagNames) throws AtlanException {
        return beTaggedByAtLeastOneOf(Atlan.getDefaultClient(), atlanTagNames);
    }

    /**
     * Returns a query that will only match assets that have at least one of the Atlan tags
     * provided. This will match irrespective of the Atlan tag being directly applied to the
     * asset, or if it was propagated to the asset.
     *
     * @param client connectivity to Atlan
     * @param atlanTagNames human-readable names of the Atlan tags
     * @return a query that will only match assets that have at least one of the Atlan tags provided
     * @throws AtlanException on any error communicating with the API to refresh the Atlan tag cache
     */
    public static Query beTaggedByAtLeastOneOf(AtlanClient client, Collection<String> atlanTagNames)
            throws AtlanException {
        List<String> values = new ArrayList<>();
        for (String name : atlanTagNames) {
            values.add(client.getAtlanTagCache().getIdForName(name));
        }
        return CompoundQuery.builder()
                .should(have(KeywordFields.TRAIT_NAMES).beOneOf(values)) // direct Atlan tags
                .should(have(KeywordFields.PROPAGATED_TRAIT_NAMES).beOneOf(values)) // propagated Atlan tags
                .minimum(1)
                .build()
                ._toQuery();
    }

    /**
     * Returns a query that will only match assets that have at least one Atlan tag directly assigned.
     *
     * @return a query that will only match assets that have at least one Atlan tag directly assigned
     */
    public static Query beDirectlyTagged() {
        return have(KeywordFields.TRAIT_NAMES).present();
    }

    /**
     * Returns a query that will only match assets that have at least one term assigned.
     *
     * @return a query that will only match assets that have at least one term assigned
     */
    public static Query beAssignedATerm() {
        return have(KeywordFields.ASSIGNED_TERMS).present();
    }

    /**
     * Returns a query that will only match assets that have at least one of the terms assigned.
     *
     * @param termQualifiedNames the qualifiedNames of the terms
     * @return a query that will only match assets that have at least one of the terms assigned
     */
    public static Query beDefinedByAtLeastOneOf(Collection<String> termQualifiedNames) {
        return have(KeywordFields.ASSIGNED_TERMS).beOneOf(termQualifiedNames);
    }

    /**
     * Returns the start of a query against any field. You need to call one of the operations
     * on the returned FieldQuery object to actually complete building the query against the field.
     *
     * @param field to start the query against
     * @return a FieldQuery object that can be used to complete construction of the query
     */
    public static FieldQuery have(AtlanSearchableField field) {
        return new FieldQuery(field);
    }

    /**
     * Returns the start of a query against any custom metadata field. You need to call one of the
     * operations on the returned FieldQuery object to actually complete building the query against
     * the field.
     *
     * @param cmName name of the custom metadata set
     * @param cmAttributeName name of the custom metadata attribute within the set
     * @return a FieldQuery object that can be used to complete construction of the query
     * @throws AtlanException if there is any problem resolving the custom metadata, such as it not existing
     */
    public static FieldQuery haveCM(String cmName, String cmAttributeName) throws AtlanException {
        return haveCM(Atlan.getDefaultClient(), cmName, cmAttributeName);
    }

    /**
     * Returns the start of a query against any custom metadata field. You need to call one of the
     * operations on the returned FieldQuery object to actually complete building the query against
     * the field.
     *
     * @param client connectivity to Atlan
     * @param cmName name of the custom metadata set
     * @param cmAttributeName name of the custom metadata attribute within the set
     * @return a FieldQuery object that can be used to complete construction of the query
     * @throws AtlanException if there is any problem resolving the custom metadata, such as it not existing
     */
    public static FieldQuery haveCM(AtlanClient client, String cmName, String cmAttributeName) throws AtlanException {
        String attributeId = client.getCustomMetadataCache().getAttrIdForName(cmName, cmAttributeName);
        return new FieldQuery(
                SearchableCMField.builder().attributeId(attributeId).build());
    }

    /** Class to compose compound queries combining various conditions. */
    public static final class FieldQuery {

        private final AtlanSearchableField field;

        /** Only allow creation of these from within the query factory. */
        private FieldQuery(AtlanSearchableField field) {
            this.field = field;
        }

        /**
         * Returns a query that will match all assets whose provided field has a value that starts with
         * the provided value. Note that this is a case-sensitive match.
         *
         * @param value the value (prefix) to check the field's value starts with (case-sensitive)
         * @return a query that will only match assets whose value for the field starts with the value provided
         */
        public Query startingWith(String value) {
            return startingWith(value, false);
        }

        /**
         * Returns a query that will match all assets whose provided field has a value that starts with
         * the provided value. Note that this can also be a case-insensitive match.
         *
         * @param value the value (prefix) to check the field's value starts with (case-sensitive)
         * @param caseInsensitive if true will match the value irrespective of case, otherwise will be a case-sensitive match
         * @return a query that will only match assets whose value for the field starts with the value provided
         */
        public Query startingWith(String value, boolean caseInsensitive) {
            PrefixQuery.Builder builder =
                    new PrefixQuery.Builder().field(field.getIndexedFieldName()).value(value);
            if (caseInsensitive) {
                builder.caseInsensitive(true);
            }
            return builder.build()._toQuery();
        }

        /**
         * Returns a query that will match all assets whose provided field has a value that exactly equals
         * the provided enumerated value.
         *
         * @param value the value (enumerated) to check the field's value is exactly equal to
         * @return a query that will only match assets whose value for the field is exactly equal to the enumerated value provided
         */
        public Query eq(AtlanEnum value) {
            return TermQuery.of(t -> t.field(field.getIndexedFieldName()).value(value.getValue()))
                    ._toQuery();
        }

        /**
         * Returns a query that will match all assets whose provided field has a value that exactly equals
         * the provided boolean value.
         *
         * @param value the value (boolean) to check the field's value is exactly equal to
         * @return a query that will only match assets whose value for the field is exactly equal to the boolean value provided
         */
        public Query eq(boolean value) {
            return TermQuery.of(t -> t.field(field.getIndexedFieldName()).value(value))
                    ._toQuery();
        }

        /**
         * Returns a query that will match all assets whose provided field has a value that exactly equals
         * the provided string value.
         *
         * @param value the value (string) to check the field's value is exactly equal to
         * @return a query that will only match assets whose value for the field is exactly equal to the string value provided
         */
        public Query eq(String value) {
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
        public Query eq(String value, boolean caseInsensitive) {
            TermQuery.Builder builder =
                    new TermQuery.Builder().field(field.getIndexedFieldName()).value(value);
            if (caseInsensitive) {
                builder.caseInsensitive(true);
            }
            return builder.build()._toQuery();
        }

        /**
         * Returns a query that will match all assets whose provided field has a value that exactly equals
         * at least one of the provided string values.
         *
         * @param values the values (strings) to check the field's value is exactly equal to
         * @return a query that will only match assets whose value for the field is exactly equal to at least one of the string values provided
         */
        public Query beOneOf(Collection<String> values) {
            List<FieldValue> list = new ArrayList<>();
            for (String value : values) {
                list.add(FieldValue.of(value));
            }
            return TermsQuery.of(
                            t -> t.field(field.getIndexedFieldName()).terms(TermsQueryField.of(f -> f.value(list))))
                    ._toQuery();
        }

        /**
         * Returns a query that will match all assets whose provided field has a value that exactly
         * matches the provided numeric value.
         *
         * @param value the numeric value to exactly match
         * @return a query that will only match assets whose value for the field is exactly the numeric value provided
         * @param <T> numeric values
         */
        public <T extends Number> Query eq(T value) {
            return TermQuery.of(f -> f.field(field.getIndexedFieldName()).value(FieldValue.of(JsonData.of(value))))
                    ._toQuery();
        }

        /**
         * Returns a query that will match all assets whose provided field has a value that is strictly greater
         * than the provided numeric value.
         *
         * @param value the numeric value to compare against
         * @return a query that will only match assets whose value for the field is strictly greater than the numeric value provided
         * @param <T> numeric values
         */
        public <T extends Number> Query gt(T value) {
            return RangeQuery.of(r -> r.field(field.getIndexedFieldName()).gt(JsonData.of(value)))
                    ._toQuery();
        }

        /**
         * Returns a query that will match all assets whose provided field has a value that is greater
         * than or equal to the provided numeric value.
         *
         * @param value the numeric value to compare against
         * @return a query that will only match assets whose value for the field is greater than or equal to the numeric value provided
         * @param <T> numeric values
         */
        public <T extends Number> Query gte(T value) {
            return RangeQuery.of(r -> r.field(field.getIndexedFieldName()).gte(JsonData.of(value)))
                    ._toQuery();
        }

        /**
         * Returns a query that will match all assets whose provided field has a value that is strictly less
         * than the provided numeric value.
         *
         * @param value the numeric value to compare against
         * @return a query that will only match assets whose value for the field is strictly less than the numeric value provided
         * @param <T> numeric values
         */
        public <T extends Number> Query lt(T value) {
            return RangeQuery.of(r -> r.field(field.getIndexedFieldName()).lt(JsonData.of(value)))
                    ._toQuery();
        }

        /**
         * Returns a query that will match all assets whose provided field has a value that is less
         * than or equal to the provided numeric value.
         *
         * @param value the numeric value to compare against
         * @return a query that will only match assets whose value for the field is less than or equal to the numeric value provided
         * @param <T> numeric values
         */
        public <T extends Number> Query lte(T value) {
            return RangeQuery.of(r -> r.field(field.getIndexedFieldName()).lte(JsonData.of(value)))
                    ._toQuery();
        }

        /**
         * Returns a query that will match all assets whose provided field has a value between the
         * minimum and maximum specified values, inclusive.
         *
         * @param min minimum value of the field that will match (inclusive)
         * @param max maximum value of the field that will match (inclusive)
         * @return a query that will only match assets whose value for the field is between the min and max (both inclusive)
         * @param <T> numeric values
         */
        public <T extends Number> Query between(T min, T max) {
            return RangeQuery.of(r -> r.field(field.getIndexedFieldName())
                            .gte(JsonData.of(min))
                            .lte(JsonData.of(max)))
                    ._toQuery();
        }

        /**
         * Returns a query that will only match assets that have some non-null, non-empty value
         * (no matter what actual value) for the field.
         *
         * @return a query that will only match assets that have some non-null, non-empty value
         *         (no matter what actual value) for the field
         */
        public Query present() {
            return ExistsQuery.of(t -> t.field(field.getIndexedFieldName()))._toQuery();
        }

        /**
         * Returns a query that will textually match the provided value against the field. This
         * analyzes the provided value according to the same analysis carried out on the field
         * (for example, tokenization, stemming, and so on).
         *
         * @param value the string value to match against
         * @return a query that will only match assets whose analyzed value for the field matches the value provided (which will also be analyzed)
         */
        public Query match(String value) {
            return MatchQuery.of(m -> m.field(field.getIndexedFieldName()).query(value))
                    ._toQuery();
        }
    }

    /** Class to compose compound queries combining various conditions. */
    @Builder
    public static final class CompoundQuery {

        /** Criteria that must be present on every search result. (Translated to filters.) */
        @Singular
        private List<Query> musts;

        /** Criteria that must not be present on any search result. */
        @Singular
        private List<Query> mustNots;

        /**
         * A collection of criteria at least some of which should be present on each search result.
         * You can control "how many" of the criteria are a minimum for each search result to match
         * through the `minimum` parameter.
         * @see #minimum
         */
        @Singular
        private List<Query> shoulds;

        /** The minimum number of criteria in the "shoulds" that must match on each search result. (Defaults to 1.) */
        @Builder.Default
        private int minimum = 1;

        /**
         * Translate the Atlan compound query into an Elastic Query object.
         * @return an Elastic Query object that represents the compound query
         */
        public Query _toQuery() {
            BoolQuery.Builder builder = new BoolQuery.Builder();
            if (musts != null && !musts.isEmpty()) {
                builder.filter(musts);
            }
            if (mustNots != null && !mustNots.isEmpty()) {
                builder.mustNot(mustNots);
            }
            if (shoulds != null && !shoulds.isEmpty()) {
                builder.should(shoulds).minimumShouldMatch("" + minimum);
            }
            return builder.build()._toQuery();
        }
    }

    /** Class to quickly compose aggregation criteria. */
    public static final class Aggregate {

        /**
         * Returns criteria to calculate the approximate number of distinct values in a field across
         * all results. Note that this de-duplicates values, but is an <a href="https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-metrics-cardinality-aggregation.html">approximation</a>.
         *
         * @param field for which to count distinct values
         * @return criteria to calculate the approximate number of distinct values in a field across the results
         */
        public static Aggregation distinct(AtlanSearchableField field) {
            return Aggregation.of(a -> a.cardinality(c -> c.field(field.getIndexedFieldName())));
        }

        /**
         * Return criteria to calculate the number of values in a field across all results.
         * Note that this does not de-duplicate.
         *
         * @param field for which to count values
         * @return criteria to calculate the number of values in the provided field across the results
         */
        public static Aggregation count(AtlanSearchableField field) {
            return Aggregation.of(a -> a.valueCount(v -> v.field(field.getIndexedFieldName())));
        }

        /**
         * Return criteria to calculate a sum of the values of the provided field across all
         * results.
         *
         * @param field for which to find the sum of values
         * @return criteria to calculate the sum of the values of the provided field across the results
         */
        public static Aggregation sum(AtlanSearchableField field) {
            return Aggregation.of(a -> a.sum(s -> s.field(field.getIndexedFieldName())));
        }

        /**
         * Return criteria to calculate the average value of the provided field across all
         * results.
         *
         * @param field for which to find the average value
         * @return criteria to calculate the average value of the provided field across the results
         */
        public static Aggregation avg(AtlanSearchableField field) {
            return Aggregation.of(a -> a.avg(s -> s.field(field.getIndexedFieldName())));
        }

        /**
         * Return criteria to calculate the minimum value of the provided field across all
         * results.
         *
         * @param field for which to find the minimum value
         * @return criteria to calculate the minimum value of the provided field across the results
         */
        public static Aggregation min(AtlanSearchableField field) {
            return Aggregation.of(a -> a.min(s -> s.field(field.getIndexedFieldName())));
        }

        /**
         * Return criteria to calculate the maximum value of the provided field across all
         * results.
         *
         * @param field for which to find the maximum value
         * @return criteria to calculate the maximum value of the provided field across the results
         */
        public static Aggregation max(AtlanSearchableField field) {
            return Aggregation.of(a -> a.max(s -> s.field(field.getIndexedFieldName())));
        }

        /**
         * Return criteria to bucket results based on the provided field.
         *
         * @param field by which to bucket the results
         * @return criteria to bucket results by the provided field
         */
        public static Aggregation bucketBy(AtlanSearchableField field) {
            return Aggregation.of(a -> a.terms(t -> t.field(field.getIndexedFieldName())));
        }
    }

    /**
     * Retrieve the numeric value from the provided aggregation result.
     *
     * @param result the aggregation result from which to retrieve the numeric metric
     * @return the numeric result for the aggregation, or 0.0 in case the aggregation is empty
     * @throws InvalidRequestException if the provided aggregation result is not a metric
     */
    public static double getAggregationMetric(AggregationResult result) throws InvalidRequestException {
        if (result instanceof AggregationMetricResult) {
            return ((AggregationMetricResult) result).getValue();
        } else if (result == null) {
            return 0.0;
        } else {
            throw new InvalidRequestException(ErrorCode.NOT_AGGREGATION_METRIC);
        }
    }

    /** Class to quickly compose a sorting condition. */
    @Builder
    @Getter
    public static final class Sort {

        /**
         * Return a condition to sort results by the provided field, in descending order.
         *
         * @param field by which to sort the results
         * @return sort condition for the provided field, in descending order
         */
        public static SortOptions by(AtlanSearchableField field) {
            return by(field, SortOrder.Desc);
        }

        /**
         * Return a condition to sort results by the provided field, in the specified order.
         *
         * @param field by which to sort the results
         * @param order in which to sort the results
         * @return sort condition for hte provided field, in the specified order
         */
        public static SortOptions by(AtlanSearchableField field, SortOrder order) {
            return SortOptions.of(s -> s.field(
                    FieldSort.of(f -> f.field(field.getIndexedFieldName()).order(order))));
        }
    }

    /**
     * Local class to encapsulate custom metadata fields into a searchable interface.
     * (Should not be used outside this factory, hence private.)
     */
    @Builder
    private static final class SearchableCMField implements AtlanSearchableField {

        private String attributeId;

        @Override
        public String getIndexedFieldName() {
            return attributeId;
        }
    }
}
