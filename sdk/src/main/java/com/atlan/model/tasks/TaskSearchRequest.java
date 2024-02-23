/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.tasks;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.search.IndexSearchDSL;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Class from which to configure and run a search against Atlan's task queue.
 */
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TaskSearchRequest extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /**
     * Build a search using the provided query and default options.
     *
     * @param query the query to use for the search
     * @return the search request, with default options
     */
    public static TaskSearchRequestBuilder<?, ?> builder(Query query) {
        return builder(IndexSearchDSL.of(query));
    }

    /**
     * Build a search using the provided DSL and default options.
     *
     * @param dsl the query details to use for the search
     * @return the search request, with default options
     */
    public static TaskSearchRequestBuilder<?, ?> builder(IndexSearchDSL dsl) {
        return TaskSearchRequest._internal().dsl(dsl);
    }

    /** Parameters for the search itself. */
    IndexSearchDSL dsl;

    /**
     * Run the search.
     *
     * @return the matching task queue records
     */
    public TaskSearchResponse search() throws AtlanException {
        return search(Atlan.getDefaultClient());
    }

    /**
     * Run the search.
     *
     * @param client connectivity to the Atlan tenant on which to search the task queue
     * @return the matching task queue records
     */
    public TaskSearchResponse search(AtlanClient client) throws AtlanException {
        return client.tasks.search(this);
    }
}
