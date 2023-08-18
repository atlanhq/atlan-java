/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.search;

import co.elastic.clients.elasticsearch._types.SortOptions;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.Asset;
import com.atlan.model.fields.AtlanField;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

/**
 * Search abstraction mechanism, to simplify the most common searches against Atlan
 * (removing the need to understand the guts of Elastic).
 */
@SuperBuilder(builderMethodName = "_internal")
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

    /** Client through which to retrieve the assets. */
    AtlanClient client;

    /** Criteria by which to sort the results. */
    @Singular
    List<SortOptions> sorts;

    /** Number of results to retrieve per underlying API request. */
    Integer pageSize;

    /** Attributes to retrieve for each asset. */
    @Singular("includeOnResults")
    List<AtlanField> includesOnResults;

    /** Attributes to retrieve for each asset related to the assets in the results. */
    @Singular("includeOnRelations")
    List<AtlanField> includesOnRelations;

    public abstract static class FluentSearchBuilder<C extends FluentSearch, B extends FluentSearchBuilder<C, B>>
            extends CompoundQueryBuilder<C, B> {

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
            IndexSearchRequest request = toRequest(1).build();
            return request.search(client).getApproximateCount();
        }

        /**
         * Run the set of filters to retrieve assets that match the supplied criteria.
         *
         * @return a stream of assets that match the specified criteria, lazily-fetched
         * @throws AtlanException on any issues interacting with the Atlan APIs
         */
        public Stream<Asset> stream() throws AtlanException {
            return stream(false);
        }

        /**
         * Run the set of filters to retrieve assets that match the supplied criteria.
         *
         * @param parallel if true, returns a parallel stream
         * @return a stream of assets that match the specified criteria, lazily-fetched
         * @throws AtlanException on any issues interacting with the Atlan APIs
         */
        public Stream<Asset> stream(boolean parallel) throws AtlanException {
            if (client == null) {
                throw new InvalidRequestException(ErrorCode.NO_ATLAN_CLIENT);
            }
            if (pageSize == null) {
                pageSize = 50;
            }
            IndexSearchRequest.IndexSearchRequestBuilder<?, ?> request = toRequest(pageSize, sorts);
            if (includesOnResults != null) {
                request.attributes(includesOnResults.stream()
                        .map(AtlanField::getAtlanFieldName)
                        .collect(Collectors.toList()));
            }
            if (includesOnRelations != null) {
                request.relationAttributes(includesOnRelations.stream()
                        .map(AtlanField::getAtlanFieldName)
                        .collect(Collectors.toList()));
            }
            if (parallel) {
                return request.build().search(client).parallelStream();
            } else {
                return request.build().search(client).stream();
            }
        }
    }
}
