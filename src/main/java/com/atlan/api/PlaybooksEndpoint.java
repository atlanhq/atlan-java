package com.atlan.api;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.NestedSortValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NestedQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import com.atlan.exception.AtlanException;
import com.atlan.model.workflow.WorkflowSearchRequest;
import com.atlan.model.workflow.WorkflowSearchResponse;

/**
 * API endpoints for operating on Atlan's playbooks.
 */
public class PlaybooksEndpoint extends WorkflowsEndpoint {

    /**
     * List the requested number of most recently created playbooks.
     *
     * @param max maximum number of playbooks to retrieve
     * @return the most recently created playbooks
     * @throws AtlanException on any API communication issue
     */
    public static WorkflowSearchResponse list(int max) throws AtlanException {
        WorkflowSearchRequest request = WorkflowSearchRequest.builder()
            .from(0)
            .size(max)
            .query(BoolQuery.of(b -> b
                .filter(NestedQuery.of(n -> n
                    .path("metadata")
                    .query(TermQuery.of(t -> t
                        .field("metadata.annotations.package.argoproj.io/name.keyword")
                        .value("@atlan/playbook"))._toQuery()
                ))._toQuery()
            ))._toQuery())
            .sortOption(SortOptions.of(s -> s.field(FieldSort.of(f -> f.field("metadata.creationTimestamp").order(SortOrder.Desc).nested(NestedSortValue.of(n -> n.path("metadata")))))))
            .build();
        return search(request);
    }
}
