package com.atlan.model.admin;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.NestedSortValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NestedQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import com.atlan.api.WorkflowsEndpointJ;
import com.atlan.exception.AtlanException;
import com.atlan.model.IndexSearchDSLJ;
import com.atlan.model.responses.WorkflowSearchResponseJ;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class WorkflowSearchRequestJ extends IndexSearchDSLJ {
    private static final long serialVersionUID = 2L;

    /** Run the search. */
    public WorkflowSearchResponseJ search() throws AtlanException {
        return WorkflowsEndpointJ.search(this);
    }

    /** Find the latest run of a given workflow. */
    public static WorkflowSearchResultJ findLatestRun(String workflowName) throws AtlanException {

        SortOptions sort = SortOptions.of(s -> s.field(FieldSort.of(f -> f.field("metadata.creationTimestamp")
                .order(SortOrder.Desc)
                .nested(NestedSortValue.of(v -> v.path("metadata"))))));

        Query term = TermQuery.of(
                        t -> t.field("spec.workflowTemplateRef.name.keyword").value(workflowName))
                ._toQuery();

        Query nested = NestedQuery.of(n -> n.path("spec").query(term))._toQuery();

        Query query = BoolQuery.of(b -> b.filter(nested))._toQuery();

        WorkflowSearchRequestJ request = WorkflowSearchRequestJ.builder()
                .from(0)
                .size(10)
                .sortOption(sort)
                .query(query)
                .build();

        WorkflowSearchResponseJ response = WorkflowsEndpointJ.search(request);
        if (response != null) {
            List<WorkflowSearchResultJ> results = response.getHits().getHits();
            if (results != null && !results.isEmpty()) {
                return results.get(0);
            }
        }
        return null;
    }
}
