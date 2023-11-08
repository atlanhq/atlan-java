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
public class CompoundQuery {

    public static final Query ACTIVE = Asset.STATUS.eq(AtlanStatus.ACTIVE);
    public static final Query ARCHIVED = Asset.STATUS.eq(AtlanStatus.DELETED);
    public static final Query WITH_LINEAGE = Asset.HAS_LINEAGE.eq(true);
    public static final Query WITHOUT_LINEAGE =
            builder().whereNot(WITH_LINEAGE).build().toQuery();

    /**
     * Returns a query that will only match assets of the type provided.
     *
     * @param typeName for assets to match
     * @return a query that will only match assets of the type provided
     */
    public static Query assetType(String typeName) {
        return Asset.TYPE_NAME.eq(typeName);
    }

    /**
     * Returns a query that will only match assets that are one of the types provided.
     *
     * @param typeNames for assets to match
     * @return a query that will only match assets of the types provided
     */
    public static Query assetTypes(Collection<String> typeNames) {
        return Asset.TYPE_NAME.in(typeNames);
    }

    /**
     * Returns a query that will match all assets that are a subtype of the type provided.
     *
     * @param typeName of the supertype for assets to match
     * @return a query that will only match assets of a subtype of the type provided
     */
    public static Query superType(String typeName) {
        return Asset.SUPER_TYPE_NAMES.eq(typeName);
    }

    /**
     * Returns a query that will match all assets that are a subtype of one of the types provided.
     *
     * @param typeNames of the supertypes for assets to match
     * @return a query that will only match assets of a subtype of the types provided
     */
    public static Query superTypes(Collection<String> typeNames) {
        return Asset.SUPER_TYPE_NAMES.in(typeNames);
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
    public static Query tagged(AtlanClient client, Collection<String> atlanTagNames) throws AtlanException {
        List<String> values = new ArrayList<>();
        for (String name : atlanTagNames) {
            values.add(client.getAtlanTagCache().getIdForName(name));
        }
        return builder()
                .whereSome(Asset.ATLAN_TAGS.in(values)) // direct Atlan tags
                .whereSome(Asset.PROPAGATED_ATLAN_TAGS.in(values)) // propagated Atlan tags
                .minSomes(1)
                .build()
                .toQuery();
    }

    /**
     * Returns a query that will only match assets that have at least one Atlan tag assigned.
     *
     * @param directly when true, the asset must have at least one Atlan tag directly assigned (otherwise even propagated tags will suffice)
     * @return a query that will only match assets that have at least one Atlan tag directly assigned
     */
    public static Query tagged(boolean directly) {
        if (directly) {
            return Asset.ATLAN_TAGS.hasAnyValue();
        } else {
            return builder()
                    .whereSome(Asset.ATLAN_TAGS.hasAnyValue())
                    .whereSome(Asset.PROPAGATED_ATLAN_TAGS.hasAnyValue())
                    .minSomes(1)
                    .build()
                    .toQuery();
        }
    }

    /**
     * Returns a query that will only match assets that have at least one term assigned.
     *
     * @return a query that will only match assets that have at least one term assigned
     */
    public static Query assignedTerm() {
        return Asset.ASSIGNED_TERMS.hasAnyValue();
    }

    /**
     * Returns a query that will only match assets that have at least one of the terms assigned.
     *
     * @param termQualifiedNames the qualifiedNames of the terms
     * @return a query that will only match assets that have at least one of the terms assigned
     */
    public static Query assignedTerm(Collection<String> termQualifiedNames) {
        return Asset.ASSIGNED_TERMS.in(termQualifiedNames);
    }

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
        BoolQuery.Builder builder = new BoolQuery.Builder();
        if (wheres != null && !wheres.isEmpty()) {
            builder.filter(wheres);
        }
        if (whereNots != null && !whereNots.isEmpty()) {
            builder.mustNot(whereNots);
        }
        if (whereSomes != null && !whereSomes.isEmpty()) {
            builder.should(whereSomes).minimumShouldMatch("" + minSomes);
        }
        return builder.build()._toQuery();
    }
}
