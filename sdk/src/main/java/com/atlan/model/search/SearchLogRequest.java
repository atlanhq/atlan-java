/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanObject;
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

    /** TBC */
    String queryString;

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

    public abstract static class SearchLogRequestBuilder<
                    C extends SearchLogRequest, B extends SearchLogRequestBuilder<C, B>>
            extends AtlanObject.AtlanObjectBuilder<C, B> {}
}
