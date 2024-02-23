/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.tasks;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.search.IndexSearchDSL;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Singular;

/**
 * Search abstraction mechanism, to simplify the most common searches against Atlan task queue
 * (removing the need to understand the guts of Elastic).
 */
@Builder(builderMethodName = "_internal")
@SuppressWarnings("cast")
public class FluentTasks {

    /**
     * Build a fluent search against the provided Atlan tenant.
     *
     * @param client connectivity to an Atlan tenant
     * @return the start of a fluent search against the tenant
     */
    public static FluentTasksBuilder builder(AtlanClient client) {
        return _internal().client(client);
    }

    /** Client through which to retrieve the assets. */
    AtlanClient client;

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
    Integer pageSize;

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

    /**
     * Translate the Atlan fluent search into an Atlan search request.
     *
     * @return an Atlan search request that encapsulates the fluent search
     */
    public TaskSearchRequest toRequest() {
        return _requestBuilder().build();
    }

    /**
     * Return the total number of assets that will match the supplied criteria,
     * using the most minimal query possible (retrieves minimal data).
     *
     * @return the count of assets that will match the supplied criteria
     * @throws AtlanException on any issues interacting with the Atlan APIs
     */
    public long count() throws AtlanException {
        if (client == null) {
            throw new InvalidRequestException(ErrorCode.NO_ATLAN_CLIENT);
        }
        // As long as there is a client, build the search request for just a single result (with count)
        // and then just return the count
        TaskSearchRequest request = TaskSearchRequest.builder(
                        _dsl().size(1).clearAggregations().build())
                .build();
        return request.search(client).getApproximateCount();
    }

    /**
     * Run the fluent search to retrieve assets that match the supplied criteria.
     *
     * @return a stream of assets that match the specified criteria, lazily-fetched
     * @throws AtlanException on any issues interacting with the Atlan APIs
     */
    public Stream<AtlanTask> stream() throws AtlanException {
        return stream(false);
    }

    /**
     * Run the fluent search to retrieve assets that match the supplied criteria.
     *
     * @param parallel if true, returns a parallel stream
     * @return a stream of assets that match the specified criteria, lazily-fetched
     * @throws AtlanException on any issues interacting with the Atlan APIs
     */
    public Stream<AtlanTask> stream(boolean parallel) throws AtlanException {
        if (client == null) {
            throw new InvalidRequestException(ErrorCode.NO_ATLAN_CLIENT);
        }
        TaskSearchRequest request = toRequest();
        if (parallel) {
            return request.search(client).parallelStream();
        } else {
            return request.search(client).stream();
        }
    }

    /**
     * Translate the Atlan fluent search into an Atlan search DSL builder.
     *
     * @return an Atlan search DSL builder that encapsulates the fluent search
     */
    protected IndexSearchDSL.IndexSearchDSLBuilder<?, ?> _dsl() {
        return IndexSearchDSL.builder(toQuery());
    }

    /**
     * Translate the Atlan fluent search into an Atlan search request builder.
     *
     * @return an Atlan search request builder that encapsulates the fluent search
     */
    protected TaskSearchRequest.TaskSearchRequestBuilder<?, ?> _requestBuilder() {
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
        return TaskSearchRequest.builder(dsl.build());
    }

    public static class FluentTasksBuilder {

        /**
         * Translate the Atlan fluent search into an Atlan search request builder.
         *
         * @return an Atlan search request builder that encapsulates the fluent search
         */
        public TaskSearchRequest.TaskSearchRequestBuilder<?, ?> toRequestBuilder() {
            return build()._requestBuilder();
        }

        /**
         * Translate the Atlan fluent search into an Atlan search request.
         *
         * @return an Atlan search request that encapsulates the fluent search
         */
        public TaskSearchRequest toRequest() {
            return build().toRequest();
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
         * Run the fluent search to retrieve assets that match the supplied criteria.
         *
         * @return a stream of assets that match the specified criteria, lazily-fetched
         * @throws AtlanException on any issues interacting with the Atlan APIs
         */
        public Stream<AtlanTask> stream() throws AtlanException {
            return build().stream();
        }

        /**
         * Run the fluent search to retrieve assets that match the supplied criteria.
         *
         * @param parallel if true, returns a parallel stream
         * @return a stream of assets that match the specified criteria, lazily-fetched
         * @throws AtlanException on any issues interacting with the Atlan APIs
         */
        public Stream<AtlanTask> stream(boolean parallel) throws AtlanException {
            return build().stream(parallel);
        }
    }
}
