/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.search;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.Asset;
import com.atlan.model.fields.AtlanField;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

/**
 * Search abstraction mechanism, to simplify the most common searches against Atlan
 * (removing the need to understand the guts of Elastic).
 */
@SuperBuilder(builderMethodName = "_internal")
@SuppressWarnings("cast")
public class FluentSearch extends CompoundQuery {

    /**
     * Build a fluent search against the provided Atlan tenant.
     *
     * @param client connectivity to an Atlan tenant
     * @return the start of a fluent search against the tenant
     */
    public static FluentSearchBuilder<?, ?> builder(AtlanClient client) {
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
    Integer pageSize;

    /** Attributes to retrieve for each asset. */
    @Singular("includeOnResults")
    List<AtlanField> includesOnResults;

    /** Attributes to retrieve for each asset (for internal use, unchecked!). */
    @Singular("_includeOnResults")
    List<String> _includesOnResults;

    /** Attributes to retrieve for each asset related to the assets in the results. */
    @Singular("includeOnRelations")
    List<AtlanField> includesOnRelations;

    /** Attributes to retrieve for each asset related to the assets in the results (for internal use, unchecked!). */
    @Singular("_includeOnRelations")
    List<String> _includesOnRelations;

    /** Whether to include relationship attributes on each relationship in the results. */
    Boolean includeRelationshipAttributes;

    /** Qualified name of a persona through which to restrict the results. */
    String restrictByPersona;

    /** Qualified name of a purpose through which to restrict the results. */
    String restrictByPurpose;

    /**
     * Translate the Atlan fluent search into an Atlan search request.
     *
     * @return an Atlan search request that encapsulates the fluent search
     */
    public IndexSearchRequest toRequest() {
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
        IndexSearchRequest.IndexSearchRequestBuilder<?, ?> rb =
                IndexSearchRequest.builder(_dsl().size(1).clearAggregations().build());
        if (restrictByPersona != null && !restrictByPersona.isEmpty()) {
            rb.persona(restrictByPersona);
        }
        if (restrictByPurpose != null && !restrictByPurpose.isEmpty()) {
            rb.purpose(restrictByPurpose);
        }
        return rb.build().search(client).getApproximateCount();
    }

    /**
     * Run the fluent search to retrieve assets that match the supplied criteria.
     *
     * @return a stream of assets that match the specified criteria, lazily-fetched
     * @throws AtlanException on any issues interacting with the Atlan APIs
     */
    public Stream<Asset> stream() throws AtlanException {
        return stream(false);
    }

    /**
     * Run the fluent search to retrieve assets that match the supplied criteria.
     * Note: if the number of results exceeds the predefined threshold (100,000 assets)
     * this will be automatically converted into a bulkStream().
     *
     * @param parallel if true, returns a parallel stream
     * @return a stream of assets that match the specified criteria, lazily-fetched
     * @throws AtlanException on any issues interacting with the Atlan APIs
     */
    public Stream<Asset> stream(boolean parallel) throws AtlanException {
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
     * Run the fluent search to retrieve assets that match the supplied criteria, using a
     * parallel stream (multiple pages are retrieved in parallel for improved throughput).
     * Note: if the number of results exceeds the predefined threshold (100,000 assets)
     * this will be automatically converted into a bulkStream().
     *
     * @return a stream of assets that match the specified criteria, lazily-fetched
     * @throws AtlanException on any issues interacting with the Atlan APIs
     */
    public Stream<Asset> parallelStream() throws AtlanException {
        return stream(true);
    }

    /**
     * Run the fluent search to retrieve assets that match the supplied criteria, using a
     * stream specifically meant for streaming large numbers of results (100,000's or more).
     * Note: this will apply its own sorting algorithm, so any sort order you have specified
     * may be ignored.
     *
     * @return a stream of assets that match the specified criteria, lazily-fetched
     * @throws AtlanException on any issues interacting with the Atlan APIs
     */
    public Stream<Asset> bulkStream() throws AtlanException {
        if (!IndexSearchResponse.presortedByTimestamp(sorts)) {
            sorts = IndexSearchResponse.sortByTimestampFirst(sorts);
        }
        return toRequest().search(client).bulkStream();
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
    protected IndexSearchRequest.IndexSearchRequestBuilder<?, ?> _requestBuilder() {
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
        IndexSearchRequest.IndexSearchRequestBuilder<?, ?> request = IndexSearchRequest.builder(dsl.build());
        if (_includesOnResults != null) {
            request.attributes(_includesOnResults);
        }
        if (includesOnResults != null) {
            request.attributes(includesOnResults.stream()
                    .map(AtlanField::getAtlanFieldName)
                    .collect(Collectors.toList()));
        }
        if (_includesOnRelations != null) {
            request.relationAttributes(_includesOnRelations);
        }
        if (includesOnRelations != null) {
            request.relationAttributes(includesOnRelations.stream()
                    .map(AtlanField::getAtlanFieldName)
                    .collect(Collectors.toList()));
        }
        if (includeRelationshipAttributes != null) {
            request.includeRelationshipAttributes(includeRelationshipAttributes);
        }
        if (restrictByPersona != null && !restrictByPersona.isEmpty()) {
            request.persona(restrictByPersona);
        }
        if (restrictByPurpose != null && !restrictByPurpose.isEmpty()) {
            request.purpose(restrictByPurpose);
        }
        return request;
    }

    public abstract static class FluentSearchBuilder<C extends FluentSearch, B extends FluentSearchBuilder<C, B>>
            extends CompoundQueryBuilder<C, B> {

        /**
         * Translate the Atlan fluent search into an Atlan search request builder.
         *
         * @return an Atlan search request builder that encapsulates the fluent search
         */
        public IndexSearchRequest.IndexSearchRequestBuilder<?, ?> toRequestBuilder() {
            return build()._requestBuilder();
        }

        /**
         * Translate the Atlan fluent search into an Atlan search request.
         *
         * @return an Atlan search request that encapsulates the fluent search
         */
        public IndexSearchRequest toRequest() {
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
        public Stream<Asset> stream() throws AtlanException {
            return build().stream();
        }

        /**
         * Run the fluent search to retrieve assets that match the supplied criteria.
         *
         * @param parallel if true, returns a parallel stream
         * @return a stream of assets that match the specified criteria, lazily-fetched
         * @throws AtlanException on any issues interacting with the Atlan APIs
         */
        public Stream<Asset> stream(boolean parallel) throws AtlanException {
            return build().stream(parallel);
        }
    }
}
