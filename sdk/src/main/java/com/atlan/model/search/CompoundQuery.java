/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.search;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.SpanOrQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.SpanQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.SpanTermQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.SpanWithinQuery;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.ITag;
import com.atlan.model.enums.AtlanStatus;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Builder;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Class to compose compound queries combining various conditions.
 * (Along with some static factory methods for some of the most common queries.)
 */
@SuperBuilder
@Slf4j
public abstract class CompoundQuery {

    /** Client through which to run the search. */
    AtlanClient client;

    /** Criteria that must be present on every search result. (Translated to filters.) */
    @Singular
    private List<Query> wheres;

    /** Criteria that must not be present on any search result. */
    @Singular
    private List<Query> whereNots;

    /**
     * A collection of criteria at least some of which should be present on each search result.
     * You can control "how many" of the criteria are a minimum for each search result to match
     * through the `minimum` parameter.
     * @see #minSomes
     */
    @Singular
    private List<Query> whereSomes;

    /** The minimum number of criteria in the "whereSomes" that must match on each search result. (Defaults to 1.) */
    @Builder.Default
    private int minSomes = 1;

    /**
     * Translate the Atlan compound query into an Elastic Query object.
     *
     * @return an Elastic Query object that represents the compound query
     */
    public Query toQuery() {
        return toQuery(true);
    }

    /**
     * Translate the Atlan compound query into an Elastic Query object, with an outer
     * bool query and inner filtered bool query (necessary for some UI elements).
     *
     * @return the Elastic Query object that represents the compound query
     */
    public Query toUnfilteredQuery() {
        return toQuery(false);
    }

    private Query toQuery(boolean filter) {
        BoolQuery.Builder builder = new BoolQuery.Builder();
        if (wheres != null && !wheres.isEmpty()) {
            if (filter) {
                builder.filter(wheres);
            } else {
                builder.must(wheres);
            }
        }
        if (whereNots != null && !whereNots.isEmpty()) {
            builder.mustNot(whereNots);
        }
        if (whereSomes != null && !whereSomes.isEmpty()) {
            builder.should(whereSomes).minimumShouldMatch("" + minSomes);
        }
        return builder.build()._toQuery();
    }

    private static final List<SpanQuery> SOURCE_SYNCED_LITTLE_ORS = List.of(
            SpanTermQuery.of(t -> t.field("__classificationsText.text").value("tagAttachmentKey"))
                    ._toSpanQuery(),
            SpanTermQuery.of(t -> t.field("__classificationsText.text").value("sourceTagName"))
                    ._toSpanQuery(),
            SpanTermQuery.of(t -> t.field("__classificationsText.text").value("sourceTagQualifiedName"))
                    ._toSpanQuery(),
            SpanTermQuery.of(t -> t.field("__classificationsText.text").value("sourceTagGuid"))
                    ._toSpanQuery(),
            SpanTermQuery.of(t -> t.field("__classificationsText.text").value("sourceTagConnectorName"))
                    ._toSpanQuery(),
            SpanTermQuery.of(t -> t.field("__classificationsText.text").value("isSourceTagSynced"))
                    ._toSpanQuery(),
            SpanTermQuery.of(t -> t.field("__classificationsText.text").value("sourceTagSyncTimestamp"))
                    ._toSpanQuery(),
            SpanTermQuery.of(t -> t.field("__classificationsText.text").value("sourceTagValue"))
                    ._toSpanQuery());

    public abstract static class CompoundQueryBuilder<C extends CompoundQuery, B extends CompoundQueryBuilder<C, B>> {

        /**
         * Returns a query that will only match assets that have at least one of the Atlan tags
         * provided. This will match irrespective of the Atlan tag being directly applied to the
         * asset, or if it was propagated to the asset.
         *
         * @param atlanTagNames human-readable names of the Atlan tags
         * @return a query that will only match assets that have at least one of the Atlan tags provided
         * @throws AtlanException on any error communicating with the API to refresh the Atlan tag cache
         */
        public B tagged(Collection<String> atlanTagNames) throws AtlanException {
            List<String> values = new ArrayList<>();
            for (String name : atlanTagNames) {
                values.add(client.getAtlanTagCache().getSidForName(name));
            }
            return whereSome(Asset.ATLAN_TAGS.in(values)) // direct Atlan tags
                    .whereSome(Asset.PROPAGATED_ATLAN_TAGS.in(values)) // propagated Atlan tags
                    .minSomes(1);
        }

        /**
         * Returns a query that will only match assets that have at least one Atlan tag assigned.
         *
         * @param directly when true, the asset must have at least one Atlan tag directly assigned (otherwise even propagated tags will suffice)
         * @return a query that will only match assets that have at least one Atlan tag directly assigned
         */
        public B tagged(boolean directly) {
            if (directly) {
                return where(Asset.ATLAN_TAGS.hasAnyValue());
            } else {
                return whereSome(Asset.ATLAN_TAGS.hasAnyValue())
                        .whereSome(Asset.PROPAGATED_ATLAN_TAGS.hasAnyValue())
                        .minSomes(1);
            }
        }

        /**
         * Returns a query that will match assets that have a specific value for the specified tag
         * (for source-synced tags).
         *
         * @param atlanTagName human-readable name of the Atlan tag
         * @param value the tag should have to match the query
         * @return a query that will only match assets that have a particular value assigned for the given Atlan tag
         * @throws AtlanException on any error communicating with the API to refresh the Atlan tag cache
         */
        public B taggedWithValue(String atlanTagName, String value) throws AtlanException {
            return taggedWithValue(atlanTagName, value, false);
        }

        /**
         * Returns a query that will match assets that have a specific value for the specified tag
         * (for source-synced tags).
         *
         * @param atlanTagName human-readable name of the Atlan tag
         * @param value the tag should have to match the query
         * @param directly when true, the asset must have the tag and value directly assigned (otherwise even propagated tags with the value will suffice)
         * @return a query that will only match assets that have a particular value assigned for the given Atlan tag
         * @throws AtlanException on any error communicating with the API to refresh the Atlan tag cache
         */
        public B taggedWithValue(String atlanTagName, String value, boolean directly) throws AtlanException {
            String tagId = client.getAtlanTagCache().getSidForName(atlanTagName);
            List<Asset> syncedTags = client.assets.select().where(ITag.MAPPED_ATLAN_TAG_NAME.eq(tagId)).stream()
                    .toList();
            String syncedTagQN;
            if (syncedTags.size() > 1) {
                syncedTagQN = syncedTags.get(0).getQualifiedName();
                log.warn(
                        "Multiple mapped source-synced tags found for tag {} -- using only the first: {}",
                        atlanTagName,
                        syncedTagQN);
            } else if (!syncedTags.isEmpty()) {
                syncedTagQN = syncedTags.get(0).getQualifiedName();
            } else {
                syncedTagQN = "NON_EXISTENT";
            }
            return taggedWithValue(atlanTagName, syncedTagQN, value, directly);
        }

        /**
         * Returns a query that will match assets that have a specific value for the specified tag
         * (for source-synced tags).
         *
         * @param atlanTagName human-readable name of the Atlan tag
         * @param sourceTagQualifiedName qualifiedName of the source tag to match (when there are multiple)
         * @param value the tag should have to match the query
         * @param directly when true, the asset must have the tag and value directly assigned (otherwise even propagated tags with the value will suffice)
         * @return a query that will only match assets that have a particular value assigned for the given Atlan tag
         * @throws AtlanException on any error communicating with the API to refresh the Atlan tag cache
         */
        public B taggedWithValue(String atlanTagName, String sourceTagQualifiedName, String value, boolean directly)
                throws AtlanException {
            String tagId = client.getAtlanTagCache().getSidForName(atlanTagName);
            List<SpanQuery> littleSpans = new ArrayList<>();
            littleSpans.add(
                    SpanTermQuery.of(t -> t.field("__classificationsText.text").value("tagAttachmentValue"))
                            ._toSpanQuery());
            for (String token : value.split(" ")) {
                littleSpans.add(SpanTermQuery.of(
                                t -> t.field("__classificationsText.text").value(token))
                        ._toSpanQuery());
            }
            littleSpans.add(
                    SpanOrQuery.of(o -> o.clauses(SOURCE_SYNCED_LITTLE_ORS))._toSpanQuery());
            List<SpanQuery> bigSpans = new ArrayList<>();
            bigSpans.add(
                    SpanTermQuery.of(t -> t.field("__classificationsText.text").value(tagId))
                            ._toSpanQuery());
            bigSpans.add(
                    SpanTermQuery.of(t -> t.field("__classificationsText.text").value(sourceTagQualifiedName))
                            ._toSpanQuery());
            Query span = SpanWithinQuery.of(s -> s.little(l -> l.spanNear(
                                    n -> n.clauses(littleSpans).slop(0).inOrder(true)))
                            .big(b -> b.spanNear(
                                    n -> n.clauses(bigSpans).slop(10000000).inOrder(true))))
                    ._toQuery();
            if (directly) {
                return where(Asset.ATLAN_TAGS.eq(tagId)).where(span);
            } else {
                return whereSome(Asset.ATLAN_TAGS.eq(tagId))
                        .whereSome(Asset.PROPAGATED_ATLAN_TAGS.eq(tagId))
                        .minSomes(1)
                        .where(span);
            }
        }

        /**
         * Adds a condition that matches only active assets.
         * Note: this is mutually-exclusive with {@link #archived()} -- if you want both, specify neither.
         *
         * @return the search builder with a condition that will only match assets that are active
         */
        public B active() {
            return where(Asset.STATUS.eq(AtlanStatus.ACTIVE));
        }

        /**
         * Adds a condition that matches only archived assets.
         * Note: this is mutually-exclusive with {@link #active()} -- if you want both, specify neither.
         *
         * @return the search builder with a condition that will only match assets that are active
         */
        public B archived() {
            return where(Asset.STATUS.eq(AtlanStatus.DELETED));
        }

        /**
         * Adds a condition that matches only assets with lineage.
         * Note: this is mutually-exclusive with {@link #withoutLineage()} -- if you want both, specify neither.
         *
         * @return the search builder with a condition that will only match assets that have lineage
         */
        public B withLineage() {
            return where(Asset.HAS_LINEAGE.eq(true));
        }

        /**
         * Adds a condition that matches only assets without lineage.
         * Note: this is mutually-exclusive with {@link #withLineage()} -- if you want both, specify neither.
         *
         * @return the search builder with a condition that will only match assets that do NOT have lineage
         */
        public B withoutLineage() {
            return whereNot(withLineage().build().toQuery());
        }
    }
}
