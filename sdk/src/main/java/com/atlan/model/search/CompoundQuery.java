/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.search;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.Asset;
import com.atlan.model.enums.AtlanStatus;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Builder;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

/**
 * Class to compose compound queries combining various conditions.
 * (Along with some static factory methods for some of the most common queries.)
 */
@SuperBuilder
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
                values.add(client.getAtlanTagCache().getIdForName(name));
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
