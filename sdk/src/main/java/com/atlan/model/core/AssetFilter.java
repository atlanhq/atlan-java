/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.core;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.Asset;
import com.atlan.model.search.IndexSearchDSL;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.util.QueryFactory;
import java.util.List;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Singular;

/**
 * Search abstraction mechanism, to simplify the most common searches against Atlan
 * (removing the need to understand the guts of Elastic).
 * @deprecated replaced by {@link com.atlan.model.search.FluentSearch}
 */
@Deprecated
@Builder
public class AssetFilter {

    /** Client through which to retrieve the assets. */
    AtlanClient client;

    /** Filters to choose which assets to include in the results. */
    @Singular
    List<Query> filters;

    /** Criteria by which to choose which assets to exclude from the results. */
    @Singular
    List<Query> excludes;

    /** Criteria by which to sort the results. */
    @Singular
    List<SortOptions> sorts;

    /** Number of results to retrieve per underlying API request. */
    Integer batch;

    /** Attributes to retrieve for each asset. */
    @Singular
    List<String> attributes;

    /** Attributes to retrieve for each asset related to the assets in the results. */
    @Singular
    List<String> relationAttributes;

    public static class AssetFilterBuilder {

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
            QueryFactory.CompoundQuery.CompoundQueryBuilder query = QueryFactory.CompoundQuery.builder();
            if (filters != null) {
                query.musts(filters);
            }
            if (excludes != null) {
                query.mustNots(excludes);
            }
            IndexSearchDSL.IndexSearchDSLBuilder<?, ?> dsl =
                    IndexSearchDSL.builder(query.build()._toQuery()).size(1);
            IndexSearchRequest.IndexSearchRequestBuilder<?, ?> request = IndexSearchRequest.builder(dsl.build());
            return request.build().search(client).getApproximateCount();
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
            QueryFactory.CompoundQuery.CompoundQueryBuilder query = QueryFactory.CompoundQuery.builder();
            if (filters != null) {
                query.musts(filters);
            }
            if (excludes != null) {
                query.mustNots(excludes);
            }
            IndexSearchDSL.IndexSearchDSLBuilder<?, ?> dsl =
                    IndexSearchDSL.builder(query.build()._toQuery());
            if (sorts != null) {
                dsl.sort(sorts);
            }
            if (batch == null) {
                batch = 50;
            }
            dsl.size(batch);
            IndexSearchRequest.IndexSearchRequestBuilder<?, ?> request = IndexSearchRequest.builder(dsl.build());
            if (attributes != null) {
                request.attributes(attributes);
            }
            if (relationAttributes != null) {
                request.relationAttributes(relationAttributes);
            }
            if (parallel) {
                return request.build().search(client).parallelStream();
            } else {
                return request.build().search(client).stream();
            }
        }
    }
}
