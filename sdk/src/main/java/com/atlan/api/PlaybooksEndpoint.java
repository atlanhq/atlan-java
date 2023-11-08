/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.api;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.NestedSortValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NestedQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.workflow.WorkflowSearchRequest;
import com.atlan.model.workflow.WorkflowSearchResponse;
import com.atlan.net.RequestOptions;

/**
 * API endpoints for operating on Atlan's playbooks.
 */
public class PlaybooksEndpoint extends WorkflowsEndpoint {

    public PlaybooksEndpoint(AtlanClient client) {
        super(client);
    }

    /**
     * List the requested number of most recently created playbooks.
     *
     * @param max maximum number of playbooks to retrieve
     * @return the most recently created playbooks
     * @throws AtlanException on any API communication issue
     */
    public WorkflowSearchResponse list(int max) throws AtlanException {
        return list(max, null);
    }

    /**
     * List the requested number of most recently created playbooks.
     *
     * @param max maximum number of playbooks to retrieve
     * @param options to override default client settings
     * @return the most recently created playbooks
     * @throws AtlanException on any API communication issue
     */
    public WorkflowSearchResponse list(int max, RequestOptions options) throws AtlanException {
        WorkflowSearchRequest request = WorkflowSearchRequest.builder()
                .from(0)
                .size(max)
                .query(BoolQuery.of(b -> b.filter(NestedQuery.of(n -> n.path("metadata")
                                        .query(TermQuery.of(t -> t.field(
                                                                "metadata.annotations.package.argoproj.io/name.keyword")
                                                        .value("@atlan/playbook"))
                                                ._toQuery()))
                                ._toQuery()))
                        ._toQuery())
                .sortOption(SortOptions.of(s -> s.field(FieldSort.of(f -> f.field("metadata.creationTimestamp")
                        .order(SortOrder.Desc)
                        .nested(NestedSortValue.of(n -> n.path("metadata")))))))
                .build();
        return search(request, options);
    }
}
