/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.search;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.util.NamedValue;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.enums.UTMTags;
import com.atlan.model.search.aggregates.AssetViews;
import com.atlan.model.search.aggregates.UserViews;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

/**
 * Search abstraction mechanism, to simplify the most common searches against Atlan's
 * log of past searches (removing the need to understand the guts of Elastic).
 */
@SuperBuilder(builderMethodName = "_internal")
@SuppressWarnings("cast")
public class SearchLog extends CompoundQuery {

    public static final List<String> EXCLUDE_USERS = List.of("support", "atlansupport");

    public static final Query VIEWED = FluentSearch._internal()
            .whereSome(SearchLogEntry.UTM_TAGS.eq(UTMTags.UI_PROFILE))
            .whereSome(SearchLogEntry.UTM_TAGS.eq(UTMTags.UI_SIDEBAR))
            .minSomes(1)
            .build()
            .toQuery();

    public static final Query SEARCHED = FluentSearch._internal()
            .whereSome(SearchLogEntry.UTM_TAGS.eq(UTMTags.UI_MAIN_LIST))
            .whereSome(SearchLogEntry.UTM_TAGS.eq(UTMTags.UI_SEARCHBAR))
            .minSomes(1)
            .build()
            .toQuery();

    /**
     * Build a search log request against the provided Atlan tenant.
     *
     * @param client connectivity to an Atlan tenant
     * @return the start of a search log request against the tenant
     */
    public static SearchLogBuilder<?, ?> builder(AtlanClient client) {
        return _internal().client(client);
    }

    /** Criteria by which to sort the results. */
    @Singular
    List<SortOptions> sorts;

    /**
     * Aggregations to run against the results of the search.
     * You provide any key you want to the map (you'll use it to look at the results of a specific aggregation).
     */
    @Singular("aggregate")
    Map<String, Aggregation> aggregations;

    /** Number of results to retrieve per underlying API request. */
    @Builder.Default
    Integer pageSize = IndexSearchDSL.DEFAULT_PAGE_SIZE;

    /**
     * Start building a search log request for the views of assets.
     *
     * @param client connectivity to the Atlan tenant
     * @return a request builder pre-configured with these criteria
     */
    public static SearchLogBuilder<?, ?> views(AtlanClient client) {
        return views(client, null);
    }

    /**
     * Start building a search log request for the views of assets.
     *
     * @param client connectivity to the Atlan tenant
     * @param excludeUsers list of usernames to exclude from the results
     * @return a request builder pre-configured with these criteria
     */
    public static SearchLogBuilder<?, ?> views(AtlanClient client, List<String> excludeUsers) {
        return views(client, -1, -1, excludeUsers);
    }

    /**
     * Start building a search log request for the views of assets.
     *
     * @param client connectivity to the Atlan tenant
     * @param from timestamp after which to look for any asset view activity (inclusive)
     * @param to timestamp up through which to look for any asset view activity (inclusive)
     * @return a request builder pre-configured with these criteria
     */
    public static SearchLogBuilder<?, ?> views(AtlanClient client, long from, long to) {
        return views(client, from, to, null);
    }

    /**
     * Start building a search log request for the views of assets.
     *
     * @param client connectivity to the Atlan tenant
     * @param from timestamp after which to look for any asset view activity (inclusive)
     * @param to timestamp up through which to look for any asset view activity (inclusive)
     * @param excludeUsers list of usernames to exclude from the results
     * @return a request builder pre-configured with these criteria
     */
    public static SearchLogBuilder<?, ?> views(AtlanClient client, long from, long to, List<String> excludeUsers) {
        List<String> exclusion = new ArrayList<>(EXCLUDE_USERS);
        if (excludeUsers != null) {
            exclusion.addAll(excludeUsers);
        }
        SearchLogBuilder<?, ?> builder = builder(client)
                .where(SearchLogEntry.UTM_TAGS.eq(UTMTags.ACTION_ASSET_VIEWED))
                .where(VIEWED)
                .whereNot(SearchLogEntry.USER.in(exclusion))
                .whereNot(SearchLogEntry.USER.startsWith("service-account-"));
        if (from > 0 && to > 0) {
            builder.where(_internal()
                    .whereSome(SearchLogEntry.LOGGED_AT.between(from, to))
                    .whereSome(SearchLogEntry.SEARCHED_AT.between(from, to))
                    .minSomes(1)
                    .build()
                    .toQuery());
        } else if (from > 0) {
            builder.where(_internal()
                    .whereSome(SearchLogEntry.LOGGED_AT.gte(from))
                    .whereSome(SearchLogEntry.SEARCHED_AT.gte(from))
                    .minSomes(1)
                    .build()
                    .toQuery());
        } else if (to > 0) {
            builder.where(_internal()
                    .whereSome(SearchLogEntry.LOGGED_AT.lte(to))
                    .whereSome(SearchLogEntry.SEARCHED_AT.lte(to))
                    .minSomes(1)
                    .build()
                    .toQuery());
        }
        return builder;
    }

    /**
     * Start building a search log request for searches run against assets.
     *
     * @param client connectivity to the Atlan tenant
     * @return a request builder pre-configured with these criteria
     */
    public static SearchLogBuilder<?, ?> searches(AtlanClient client) {
        return searches(client, null);
    }

    /**
     * Start building a search log request for searches run against assets.
     *
     * @param client connectivity to the Atlan tenant
     * @param excludeUsers list of usernames to exclude from the results
     * @return a request builder pre-configured with these criteria
     */
    public static SearchLogBuilder<?, ?> searches(AtlanClient client, List<String> excludeUsers) {
        return searches(client, -1, -1, excludeUsers);
    }

    /**
     * Start building a search log request for searches run against assets.
     *
     * @param client connectivity to the Atlan tenant
     * @param from timestamp after which to look for any search activity (inclusive)
     * @param to timestamp up through which to look for any search activity (inclusive)
     * @return a request builder pre-configured with these criteria
     */
    public static SearchLogBuilder<?, ?> searches(AtlanClient client, long from, long to) {
        return searches(client, from, to, null);
    }

    /**
     * Start building a search log request for searches run against assets.
     *
     * @param client connectivity to the Atlan tenant
     * @param from timestamp after which to look for any search activity (inclusive)
     * @param to timestamp up through which to look for any search activity (inclusive)
     * @param excludeUsers list of usernames to exclude from the results
     * @return a request builder pre-configured with these criteria
     */
    public static SearchLogBuilder<?, ?> searches(AtlanClient client, long from, long to, List<String> excludeUsers) {
        List<String> exclusion = new ArrayList<>(EXCLUDE_USERS);
        if (excludeUsers != null) {
            exclusion.addAll(excludeUsers);
        }
        SearchLogBuilder<?, ?> builder = builder(client)
                .where(SearchLogEntry.UTM_TAGS.eq(UTMTags.ACTION_SEARCHED))
                .where(SEARCHED)
                .whereNot(SearchLogEntry.USER.in(exclusion))
                .whereNot(SearchLogEntry.USER.startsWith("service-account-"));
        if (from > 0 && to > 0) {
            builder.where(_internal()
                    .whereSome(SearchLogEntry.LOGGED_AT.between(from, to))
                    .whereSome(SearchLogEntry.SEARCHED_AT.between(from, to))
                    .minSomes(1)
                    .build()
                    .toQuery());
        } else if (from > 0) {
            builder.where(_internal()
                    .whereSome(SearchLogEntry.LOGGED_AT.gte(from))
                    .whereSome(SearchLogEntry.SEARCHED_AT.gte(from))
                    .minSomes(1)
                    .build()
                    .toQuery());
        } else if (to > 0) {
            builder.where(_internal()
                    .whereSome(SearchLogEntry.LOGGED_AT.lte(to))
                    .whereSome(SearchLogEntry.SEARCHED_AT.lte(to))
                    .minSomes(1)
                    .build()
                    .toQuery());
        }
        return builder;
    }

    /**
     * Start building a search log request for the views of an asset, by its GUID.
     *
     * @param client connectivity to the Atlan tenant
     * @param guid unique identifier of the asset for which to retrieve the view history
     * @return a request builder pre-configured with these criteria
     */
    public static SearchLogBuilder<?, ?> viewsByGuid(AtlanClient client, String guid) {
        return viewsByGuid(client, guid, null);
    }

    /**
     * Start building a search log request for the views of an asset, by its GUID.
     *
     * @param client connectivity to the Atlan tenant
     * @param guid unique identifier of the asset for which to retrieve the view history
     * @param excludeUsers list of usernames to exclude from the results
     * @return a request builder pre-configured with these criteria
     */
    public static SearchLogBuilder<?, ?> viewsByGuid(AtlanClient client, String guid, List<String> excludeUsers) {
        List<String> exclusion = new ArrayList<>(EXCLUDE_USERS);
        if (excludeUsers != null) {
            exclusion.addAll(excludeUsers);
        }
        return builder(client)
                .where(SearchLogEntry.UTM_TAGS.eq(UTMTags.ACTION_ASSET_VIEWED))
                .where(SearchLogEntry.ENTITY_ID.eq(guid))
                .where(VIEWED)
                .whereNot(SearchLogEntry.USER.in(exclusion));
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
        SearchLogRequest request = viewsByGuid(client, guid, excludeUsers)
                .aggregate("uniqueUsers", byUser)
                .aggregate("totalDistinctUsers", SearchLogEntry.USER.distinct(1000))
                .pageSize(0) // Do not need the detailed results, only the aggregates
                .toRequest();
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
        SearchLogRequest request = views(client, excludeUsers)
                .aggregate("uniqueAssets", byGuid)
                .aggregate("totalDistinctUsers", SearchLogEntry.USER.distinct(1000))
                .pageSize(1) // Do not need the detailed results, only the aggregates
                .toRequest();
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
     * Translate the Atlan search log into an Atlan search log request.
     *
     * @return an Atlan search log request that encapsulates the requested criteria
     */
    public SearchLogRequest toRequest() {
        return _requestBuilder().build();
    }

    /**
     * Return the total number of search log entries that will match the supplied criteria,
     * using the most minimal query possible (retrieves minimal data).
     *
     * @return the count of search log entries that will match the supplied criteria
     * @throws AtlanException on any issues interacting with the Atlan APIs
     */
    public long count() throws AtlanException {
        if (client == null) {
            throw new InvalidRequestException(ErrorCode.NO_ATLAN_CLIENT);
        }
        // As long as there is a client, build the search request for just a single result (with count)
        // and then just return the count
        SearchLogRequest request = SearchLogRequest.builder(
                        _dsl().size(1).clearAggregations().build())
                .build();
        return request.search(client).getApproximateCount();
    }

    /**
     * Run the search to retrieve search log entries that match the supplied criteria.
     *
     * @return a stream of search log entries that match the specified criteria, lazily-fetched
     * @throws AtlanException on any issues interacting with the Atlan APIs
     */
    public Stream<SearchLogEntry> stream() throws AtlanException {
        return stream(false);
    }

    /**
     * Run the search to retrieve search log entries that match the supplied criteria.
     *
     * @param parallel if true, returns a parallel stream
     * @return a stream of search log entries that match the specified criteria, lazily-fetched
     * @throws AtlanException on any issues interacting with the Atlan APIs
     */
    public Stream<SearchLogEntry> stream(boolean parallel) throws AtlanException {
        if (client == null) {
            throw new InvalidRequestException(ErrorCode.NO_ATLAN_CLIENT);
        }
        if (parallel) {
            return toRequest().search(client).parallelStream();
        } else {
            return toRequest().search(client).stream();
        }
    }

    /**
     * Run the search to retrieve search log entries that match the supplied criteria, using a
     * parallel stream (multiple pages are retrieved in parallel for improved throughput).
     *
     * @return a stream of search log entries that match the specified criteria, lazily-fetched
     * @throws AtlanException on any issues interacting with the Atlan APIs
     */
    public Stream<SearchLogEntry> parallelStream() throws AtlanException {
        return stream(true);
    }

    /**
     * Run the search to retrieve search log entries that match the supplied criteria, using a
     * stream specifically meant for streaming large numbers of results (10,000 or more).
     * Note: this will apply its own sorting algorithm, so any sort order you have specified
     * may be ignored.
     *
     * @return a stream of search log entries that match the specified criteria, lazily-fetched
     * @throws AtlanException on any issues interacting with the Atlan APIs
     */
    public Stream<SearchLogEntry> bulkStream() throws AtlanException {
        if (!SearchLogResponse.presortedByTimestamp(sorts)) {
            sorts = SearchLogResponse.sortByTimestampFirst(sorts);
        }
        return toRequest().search(client).bulkStream();
    }

    /**
     * Translate the Atlan search log search into an Atlan search DSL builder.
     *
     * @return an Atlan search DSL builder that encapsulates the search log search
     */
    protected IndexSearchDSL.IndexSearchDSLBuilder<?, ?> _dsl() {
        return IndexSearchDSL.builder(toQuery());
    }

    /**
     * Translate the Atlan search log search into an Atlan search log request builder.
     *
     * @return an Atlan search log request builder that encapsulates the search
     */
    protected SearchLogRequest.SearchLogRequestBuilder<?, ?> _requestBuilder() {
        IndexSearchDSL.IndexSearchDSLBuilder<?, ?> dsl = _dsl();
        if (pageSize != null) {
            dsl.size(pageSize);
        }
        if (sorts != null) {
            dsl.sort(sorts);
        }
        if (aggregations != null) {
            dsl.aggregations(aggregations);
        }
        return SearchLogRequest.builder(dsl.build());
    }

    public abstract static class SearchLogBuilder<C extends SearchLog, B extends SearchLogBuilder<C, B>>
            extends CompoundQueryBuilder<C, B> {

        /**
         * Translate the Atlan search log search into an Atlan search log request builder.
         *
         * @return an Atlan search log request builder that encapsulates the search
         */
        public SearchLogRequest.SearchLogRequestBuilder<?, ?> toRequestBuilder() {
            return build()._requestBuilder();
        }

        /**
         * Translate the Atlan search log search into an Atlan search log request.
         *
         * @return an Atlan search log request that encapsulates the search
         */
        public SearchLogRequest toRequest() {
            return build().toRequest();
        }

        /**
         * Return the total number of search log entries that will match the supplied criteria,
         * using the most minimal query possible (retrieves minimal data).
         *
         * @return the count of search log entries that will match the supplied criteria
         * @throws AtlanException on any issues interacting with the Atlan APIs
         */
        public long count() throws AtlanException {
            return build().count();
        }

        /**
         * Run the search log search to retrieve search log entries that match the supplied criteria.
         *
         * @return a stream of search log entries that match the specified criteria, lazily-fetched
         * @throws AtlanException on any issues interacting with the Atlan APIs
         */
        public Stream<SearchLogEntry> stream() throws AtlanException {
            return build().stream();
        }

        /**
         * Run the search log search to retrieve search log entries that match the supplied criteria.
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
