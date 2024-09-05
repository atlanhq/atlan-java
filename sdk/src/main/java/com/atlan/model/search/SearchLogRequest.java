/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.util.NamedValue;
import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.UTMTags;
import com.atlan.model.search.aggregates.AssetViews;
import com.atlan.model.search.aggregates.UserViews;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Class from which to configure and run a search against Atlan's log of past searches.
 * For a list of the attributes that can be searched, aggregated, sorted, etc see the
 * static constants in {@link SearchLogEntry}.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SearchLogRequest extends AtlanObject {
    private static final long serialVersionUID = 2L;

    public static final List<String> EXCLUDE_USERS = List.of("support", "atlansupport");

    public static final Query VIEWED = FluentSearch._internal()
            .whereSome(SearchLogEntry.UTM_TAGS.eq(UTMTags.UI_PROFILE))
            .whereSome(SearchLogEntry.UTM_TAGS.eq(UTMTags.UI_SIDEBAR))
            .minSomes(1)
            .build()
            .toQuery();

    /**
     * Build a search using the provided query and default options.
     *
     * @param query the query to use for the search
     * @return the search request, with default options
     */
    public static SearchLogRequestBuilder<?, ?> builder(Query query) {
        return builder(IndexSearchDSL.of(query));
    }

    /**
     * Build a search using the provided DSL and default options.
     *
     * @param dsl the query details to use for the search
     * @return the search request, with default options
     */
    public static SearchLogRequestBuilder<?, ?> builder(IndexSearchDSL dsl) {
        return SearchLogRequest._internal().dsl(dsl);
    }

    /** Parameters for the search itself. */
    IndexSearchDSL dsl;

    /**
     * Start building a search log request for the views of assets.
     *
     * @return a request builder pre-configured with these criteria
     */
    public static SearchLogRequestBuilder<?, ?> views() {
        return views(null);
    }

    /**
     * Start building a search log request for the views of assets.
     *
     * @param excludeUsers list of usernames to exclude from the results
     * @return a request builder pre-configured with these criteria
     */
    public static SearchLogRequestBuilder<?, ?> views(List<String> excludeUsers) {
        List<String> exclusion = new ArrayList<>(EXCLUDE_USERS);
        if (excludeUsers != null) {
            exclusion.addAll(excludeUsers);
        }
        Query viewedByGuid = FluentSearch._internal()
                .where(SearchLogEntry.UTM_TAGS.eq(UTMTags.ACTION_ASSET_VIEWED))
                .where(VIEWED)
                .whereNot(SearchLogEntry.USER.in(exclusion))
                .build()
                .toQuery();
        return SearchLogRequest.builder(viewedByGuid);
    }

    /**
     * Start building a search log request for the views of an asset, by its GUID.
     *
     * @param guid unique identifier of the asset for which to retrieve the view history
     * @return a request builder pre-configured with these criteria
     */
    public static SearchLogRequestBuilder<?, ?> viewsByGuid(String guid) {
        return viewsByGuid(guid, null);
    }

    /**
     * Start building a search log request for the views of an asset, by its GUID.
     *
     * @param guid unique identifier of the asset for which to retrieve the view history
     * @param excludeUsers list of usernames to exclude from the results
     * @return a request builder pre-configured with these criteria
     */
    public static SearchLogRequestBuilder<?, ?> viewsByGuid(String guid, List<String> excludeUsers) {
        List<String> exclusion = new ArrayList<>(EXCLUDE_USERS);
        if (excludeUsers != null) {
            exclusion.addAll(excludeUsers);
        }
        Query viewedByGuid = FluentSearch._internal()
                .where(SearchLogEntry.UTM_TAGS.eq(UTMTags.ACTION_ASSET_VIEWED))
                .where(SearchLogEntry.ENTITY_ID.eq(guid))
                .where(VIEWED)
                .whereNot(SearchLogEntry.USER.in(exclusion))
                .build()
                .toQuery();
        return SearchLogRequest.builder(viewedByGuid);
    }

    /**
     * Find the most recent viewers of the asset in Atlan.
     *
     * @param guid of the asset
     * @param maxUsers maximum number of recent users to consider
     * @return the list of users that most-recently viewed the asset, in descending order (most-recently viewed first)
     * @throws AtlanException on any issues interacting with the Atlan APIs
     */
    public static List<UserViews> mostRecentViewers(String guid, int maxUsers) throws AtlanException {
        return mostRecentViewers(guid, maxUsers, null);
    }

    /**
     * Find the most recent viewers of the asset in Atlan.
     *
     * @param guid of the asset
     * @param maxUsers maximum number of recent users to consider
     * @param excludeUsers list of usernames to exclude from the results
     * @return the list of users that most-recently viewed the asset, in descending order (most-recently viewed first)
     * @throws AtlanException on any issues interacting with the Atlan APIs
     */
    public static List<UserViews> mostRecentViewers(String guid, int maxUsers, List<String> excludeUsers)
            throws AtlanException {
        return mostRecentViewers(Atlan.getDefaultClient(), guid, maxUsers, excludeUsers);
    }

    /**
     * Find the most recent viewers of the asset in Atlan.
     *
     * @param client connectivity to the Atlan tenant on which to run the search
     * @param guid of the asset
     * @param maxUsers maximum number of recent users to consider
     * @return the list of users that most-recently viewed the asset, in descending order (most-recently viewed first)
     * @throws AtlanException on any issues interacting with the Atlan APIs
     */
    public static List<UserViews> mostRecentViewers(AtlanClient client, String guid, int maxUsers)
            throws AtlanException {
        return mostRecentViewers(client, guid, maxUsers, null);
    }

    /**
     * Find the most recent viewers of the asset in Atlan.
     *
     * @param client connectivity to the Atlan tenant on which to run the search
     * @param guid of the asset
     * @param maxUsers maximum number of recent users to consider
     * @param excludeUsers list of usernames to exclude from the results
     * @return the list of users that most-recently viewed the asset, in descending order (most-recently viewed first)
     * @throws AtlanException on any issues interacting with the Atlan APIs
     */
    public static List<UserViews> mostRecentViewers(
            AtlanClient client, String guid, int maxUsers, List<String> excludeUsers) throws AtlanException {
        List<UserViews> list = new ArrayList<>();
        Aggregation byUser = SearchLogEntry.USER.bucketBy(
                maxUsers,
                Map.of("latestTimestamp", SearchLogEntry.SEARCHED_AT.max()),
                List.of(NamedValue.of("latestTimestamp", SortOrder.Desc)));
        SearchLogRequest request = viewsByGuid(guid, excludeUsers)
                .aggregation("uniqueUsers", byUser)
                .aggregation("totalDistinctUsers", SearchLogEntry.USER.distinct(1000))
                .pageSize(0) // Do not need the detailed results, only the aggregates
                .build();
        SearchLogResponse response = request.search(client);
        AggregationBucketResult uniqueUsers =
                (AggregationBucketResult) response.getAggregations().get("uniqueUsers");
        for (AggregationBucketDetails details : uniqueUsers.getBuckets()) {
            list.add(UserViews.builder()
                    .username(details.key.toString())
                    .viewCount(details.docCount)
                    .mostRecentView(details.getNestedResults()
                            .get("latestTimestamp")
                            .getMetric()
                            .longValue())
                    .build());
        }
        return list;
    }

    /**
     * Find the most-viewed assets in Atlan.
     *
     * @param maxAssets maximum number of assets to consider
     * @param byDifferentUsers when true, will consider assets viewed by more users as more important than total view count, otherwise will consider total view count most important
     * @return the list of assets that are most-viewed, in descending order (most-viewed first)
     * @throws AtlanException on any issues interacting with the Atlan APIs
     */
    public static List<AssetViews> mostViewedAssets(int maxAssets, boolean byDifferentUsers) throws AtlanException {
        return mostViewedAssets(maxAssets, byDifferentUsers, null);
    }

    /**
     * Find the most-viewed assets in Atlan.
     *
     * @param maxAssets maximum number of assets to consider
     * @param byDifferentUsers when true, will consider assets viewed by more users as more important than total view count, otherwise will consider total view count most important
     * @param excludeUsers list of usernames to exclude from the results
     * @return the list of assets that are most-viewed, in descending order (most-viewed first)
     * @throws AtlanException on any issues interacting with the Atlan APIs
     */
    public static List<AssetViews> mostViewedAssets(int maxAssets, boolean byDifferentUsers, List<String> excludeUsers)
            throws AtlanException {
        return mostViewedAssets(Atlan.getDefaultClient(), maxAssets, byDifferentUsers, excludeUsers);
    }

    /**
     * Find the most-viewed assets in Atlan.
     *
     * @param client connectivity to the Atlan tenant on which to run the search
     * @param maxAssets maximum number of assets to consider
     * @param byDifferentUsers when true, will consider assets viewed by more users as more important than total view count, otherwise will consider total view count most important
     * @return the list of assets that are most-viewed, in descending order (most-viewed first)
     * @throws AtlanException on any issues interacting with the Atlan APIs
     */
    public static List<AssetViews> mostViewedAssets(AtlanClient client, int maxAssets, boolean byDifferentUsers)
            throws AtlanException {
        return mostViewedAssets(client, maxAssets, byDifferentUsers, null);
    }

    /**
     * Find the most-viewed assets in Atlan.
     *
     * @param client connectivity to the Atlan tenant on which to run the search
     * @param maxAssets maximum number of assets to consider
     * @param byDifferentUsers when true, will consider assets viewed by more users as more important than total view count, otherwise will consider total view count most important
     * @param excludeUsers list of usernames to exclude from the results
     * @return the list of assets that are most-viewed, in descending order (most-viewed first)
     * @throws AtlanException on any issues interacting with the Atlan APIs
     */
    public static List<AssetViews> mostViewedAssets(
            AtlanClient client, int maxAssets, boolean byDifferentUsers, List<String> excludeUsers)
            throws AtlanException {
        List<AssetViews> list = new ArrayList<>();
        List<NamedValue<SortOrder>> sort = null;
        if (byDifferentUsers) {
            sort = List.of(NamedValue.of("uniqueUsers", SortOrder.Desc));
        }
        Aggregation byGuid = SearchLogEntry.ENTITY_ID.bucketBy(
                maxAssets, Map.of("uniqueUsers", SearchLogEntry.USER.distinct(1000)), sort);
        SearchLogRequest request = views(excludeUsers)
                .aggregation("uniqueAssets", byGuid)
                .aggregation("totalDistinctUsers", SearchLogEntry.USER.distinct(1000))
                .pageSize(1) // Do not need the detailed results, only the aggregates
                .build();
        SearchLogResponse response = request.search(client);
        AggregationBucketResult uniqueAssets =
                (AggregationBucketResult) response.getAggregations().get("uniqueAssets");
        for (AggregationBucketDetails details : uniqueAssets.getBuckets()) {
            list.add(AssetViews.builder()
                    .guid(details.key.toString())
                    .totalViews(details.docCount)
                    .distinctUsers(details.getNestedResults()
                            .get("uniqueUsers")
                            .getMetric()
                            .longValue())
                    .build());
        }
        return list;
    }

    /**
     * Run the search.
     *
     * @return the matching assets
     * @throws AtlanException on any issues interacting with the Atlan APIs
     */
    public SearchLogResponse search() throws AtlanException {
        return search(Atlan.getDefaultClient());
    }

    /**
     * Run the search.
     *
     * @param client connectivity to the Atlan tenant on which to run the search
     * @return the matching assets
     * @throws AtlanException on any issues interacting with the Atlan APIs
     */
    public SearchLogResponse search(AtlanClient client) throws AtlanException {
        return client.searchLog.search(this);
    }

    /**
     * Return the total number of search log entries that will match the supplied criteria,
     * using the most minimal query possible (retrieves minimal data).
     *
     * @return the count of search log entries that will match the supplied criteria
     * @throws AtlanException on any issues interacting with the Atlan APIs
     */
    public long count() throws AtlanException {
        return count(Atlan.getDefaultClient());
    }

    /**
     * Return the total number of search log entries that will match the supplied criteria,
     * using the most minimal query possible (retrieves minimal data).
     *
     * @param client through which to run the request
     * @return the count of search log entries that will match the supplied criteria
     * @throws AtlanException on any issues interacting with the Atlan APIs
     */
    public long count(AtlanClient client) throws AtlanException {
        if (client == null) {
            throw new InvalidRequestException(ErrorCode.NO_ATLAN_CLIENT);
        }
        // As long as there is a client, build the search request for just a single result (with count)
        // and then just return the count
        SearchLogRequest request = SearchLogRequest.builder(
                        getDsl().toBuilder().size(1).clearAggregations().build())
                .build();
        return request.search(client).getApproximateCount();
    }

    /**
     * Run the search to retrieve entries that match the supplied criteria.
     *
     * @return a stream of search log entries that match the specified criteria, lazily-fetched
     * @throws AtlanException on any issues interacting with the Atlan APIs
     */
    public Stream<SearchLogEntry> stream() throws AtlanException {
        return stream(Atlan.getDefaultClient());
    }

    /**
     * Run the search to retrieve entries that match the supplied criteria.
     *
     * @param client through which to run the request
     * @return a stream of search log entries that match the specified criteria, lazily-fetched
     * @throws AtlanException on any issues interacting with the Atlan APIs
     */
    public Stream<SearchLogEntry> stream(AtlanClient client) throws AtlanException {
        return stream(client, false);
    }

    /**
     * Run the search to retrieve entries that match the supplied criteria.
     *
     * @param parallel if true, returns a parallel stream
     * @return a stream of search log entries that match the specified criteria, lazily-fetched
     * @throws AtlanException on any issues interacting with the Atlan APIs
     */
    public Stream<SearchLogEntry> stream(boolean parallel) throws AtlanException {
        return stream(Atlan.getDefaultClient(), parallel);
    }

    /**
     * Run the search to retrieve entries that match the supplied criteria.
     *
     * @param client through which to run the request
     * @param parallel if true, returns a parallel stream
     * @return a stream of search log entries that match the specified criteria, lazily-fetched
     * @throws AtlanException on any issues interacting with the Atlan APIs
     */
    public Stream<SearchLogEntry> stream(AtlanClient client, boolean parallel) throws AtlanException {
        if (parallel) {
            return search(client).parallelStream();
        } else {
            return search(client).stream();
        }
    }

    public abstract static class SearchLogRequestBuilder<
                    C extends SearchLogRequest, B extends SearchLogRequest.SearchLogRequestBuilder<C, B>>
            extends AtlanObject.AtlanObjectBuilder<C, B> {
        /**
         * Set the maximum number of results to retrieve per page of log entries.
         *
         * @param size number of results to retrieve per page
         * @return this search log request builder, with the updated size
         */
        public B pageSize(int size) {
            return this.dsl(dsl.toBuilder().size(size).build());
        }

        /**
         * Add a sort option to sort the resulting search log entries.
         *
         * @param option by which to sort the resulting entries
         * @return this search log request builder, with the updated sorting option(s)
         */
        public B sortBy(SortOptions option) {
            return this.dsl(dsl.toBuilder().sortOption(option).build());
        }

        /**
         * Add an aggregation on the search log entries.
         *
         * @param key arbitrary identifier for the aggregation results
         * @param aggregation the aggregation to add on top of the results
         * @return this search log request builder, with the additional aggregation(s)
         */
        public B aggregation(String key, Aggregation aggregation) {
            return this.dsl(dsl.toBuilder().aggregation(key, aggregation).build());
        }

        /**
         * Return the total number of assets that will match the supplied criteria,
         * using the most minimal query possible (retrieves minimal data).
         *
         * @return the count of assets that will match the supplied criteria
         * @throws AtlanException on any issues interacting with the Atlan APIs
         */
        public long count() throws AtlanException {
            return build().count();
        }

        /**
         * Run the search to retrieve entries that match the supplied criteria.
         *
         * @return a stream of search log entries that match the specified criteria, lazily-fetched
         * @throws AtlanException on any issues interacting with the Atlan APIs
         */
        public Stream<SearchLogEntry> stream() throws AtlanException {
            return build().stream();
        }

        /**
         * Run the search to retrieve entries that match the supplied criteria.
         *
         * @param parallel if true, returns a parallel stream
         * @return a stream of search log entries that match the specified criteria, lazily-fetched
         * @throws AtlanException on any issues interacting with the Atlan APIs
         */
        public Stream<SearchLogEntry> stream(boolean parallel) throws AtlanException {
            return build().stream(parallel);
        }
    }
}
