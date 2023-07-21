/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.core;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.search.IndexSearchDSL;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.util.QueryFactory;
import java.util.List;
import lombok.Builder;
import lombok.Singular;

@Builder
public class AssetFilter {

    // TODO: Alternative nouns here that would better match the existing QueryFactory
    //  wording (beActive, have(...), etc)? Or better to change QueryFactory's wording?

    // TODO: Can we add an "update" method that does a seamless redirect to some AssetBatch()
    //  to mass-update the assets through chaining?

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
    @Builder.Default
    int batch = 50;

    /** Attributes to retrieve for each asset. */
    @Singular
    List<String> attributes;

    /** Attributes to retrieve for each asset related to the assets in the results. */
    @Singular
    List<String> relationAttributes;

    public static class AssetFilterBuilder {

        /**
         * Run the set of filters to retrieve assets that match the supplied criteria,
         * using the default Atlan client.
         *
         * @return the paged set of assets that match the supplied criteria
         * @throws AtlanException on any issues interacting with the Atlan APIs
         */
        public IndexSearchResponse go() throws AtlanException {
            return go(Atlan.getDefaultClient());
        }

        /**
         * Run the set of filters to retrieve assets that match the supplied criteria.
         *
         * @param client through which to run the asset retrieval
         * @return the paged set of assets that match the supplied criteria
         * @throws AtlanException on any issues interacting with the Atlan APIs
         */
        public IndexSearchResponse go(AtlanClient client) throws AtlanException {
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
            dsl.size(batch$value);
            IndexSearchRequest.IndexSearchRequestBuilder<?, ?> request = IndexSearchRequest.builder(dsl.build());
            if (attributes != null) {
                request.attributes(attributes);
            }
            if (relationAttributes != null) {
                request.relationAttributes(relationAttributes);
            }
            return request.build().search(client);
        }
    }
}
