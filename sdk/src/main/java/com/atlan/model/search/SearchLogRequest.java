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
     * Start building a search log request for the last views of an asset, by its GUID.
     *
     * @param guid unique identifier of the asset for which to retrieve the view history
     * @param maxUsers number of unique users to retrieve
     * @return a request builder pre-configured with these criteria
     */
    public static SearchLogRequestBuilder<?, ?> viewersByGuid(String guid, int maxUsers) {
        return viewersByGuid(Atlan.getDefaultClient(), guid, maxUsers);
    }

    /**
     * Start building a search log request for the views of an asset, by its GUID.
     *
     * @param client connectivity to the Atlan tenant on which to search the log
     * @param guid unique identifier of the asset for which to retrieve the view history
     * @param maxUsers number of unique users to retrieve
     * @return a request builder pre-configured with these criteria
     */
    public static SearchLogRequestBuilder<?, ?> viewersByGuid(AtlanClient client, String guid, int maxUsers) {
        Query view = FluentSearch.builder(client)
                .whereSome(SearchLogEntry.UTM_TAGS.eq("ui_profile")) // TODO: enum these
                .whereSome(SearchLogEntry.UTM_TAGS.eq("ui_sidebar"))
                .minSomes(1)
                .build()
                .toQuery();
        Query viewersByGuid = FluentSearch.builder(client)
                .where(SearchLogEntry.UTM_TAGS.eq("action_asset_viewed")) // TODO: enum this
                .where(SearchLogEntry.ENTITY_ID.eq(guid))
                .where(view)
                .build()
                .toQuery();
        // TODO: Need a more general way to aggregate, so consumers know the keys for the
        //  aggregation results
        Aggregation byUser = SearchLogEntry.USER.bucketBy(
                maxUsers,
                Map.of("latestTimestamp", SearchLogEntry.SEARCHED_AT.max()),
                List.of(NamedValue.of("latestTimestamp", SortOrder.Desc)));
        IndexSearchDSL dsl = IndexSearchDSL.builder(viewersByGuid)
                .aggregation("uniqueUsers", byUser)
                .aggregation("totalDistinctUsers", SearchLogEntry.USER.distinct(1000))
                .build();
        return SearchLogRequest.builder(dsl);
    }

    /**
     * Run the search.
     *
     * @return the matching assets
     */
    public SearchLogResponse search() throws AtlanException {
        return search(Atlan.getDefaultClient());
    }

    /**
     * Run the search.
     *
     * @param client connectivity to the Atlan tenant on which to run the search
     * @return the matching assets
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
