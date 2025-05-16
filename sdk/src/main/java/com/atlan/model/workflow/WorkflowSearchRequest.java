/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.workflow;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.NestedSortValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanPackageType;
import com.atlan.model.enums.AtlanWorkflowPhase;
import com.atlan.model.search.IndexSearchDSL;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WorkflowSearchRequest extends IndexSearchDSL {
    private static final long serialVersionUID = 2L;

    /**
     * Run the search.
     *
     * @param client connectivity to the Atlan tenant on which to run the search
     * @return results from running the search
     * @throws AtlanException on any API communication issue
     */
    public WorkflowSearchResponse search(AtlanClient client) throws AtlanException {
        return client.workflows.searchRuns(this);
    }

    /**
     * Find the latest run of a given workflow.
     *
     * @param client connectivity to the Atlan tenant on which to find the latest run of the workflow
     * @param workflowName name of the workflow for which to find the latest run
     * @return the singular result giving the latest run of the workflow
     * @throws AtlanException on any API communication issue
     */
    public static WorkflowSearchResult findLatestRun(AtlanClient client, String workflowName) throws AtlanException {

        SortOptions sort = SortOptions.of(s -> s.field(FieldSort.of(f -> f.field("metadata.creationTimestamp")
                .order(SortOrder.Desc)
                .nested(NestedSortValue.of(v -> v.path("metadata"))))));

        Query term = TermQuery.of(
                        t -> t.field("spec.workflowTemplateRef.name.keyword").value(workflowName))
                ._toQuery();

        Query nested = NestedQuery.of(n -> n.path("spec").query(term))._toQuery();

        Query query = BoolQuery.of(b -> b.filter(nested))._toQuery();

        WorkflowSearchRequest request = WorkflowSearchRequest.builder()
                .from(0)
                .size(10)
                .sortOption(sort)
                .query(query)
                .build();

        WorkflowSearchResponse response = client.workflows.searchRuns(request);
        if (response != null) {
            List<WorkflowSearchResult> results = response.getHits().getHits();
            if (results != null && !results.isEmpty()) {
                return results.get(0);
            }
        }
        return null;
    }

    /**
     * Find the most current, still-running run of a given workflow.
     *
     * @param client connectivity to the Atlan tenant on which to find the current run of the workflow
     * @param workflowName name of the workflow for which to find the current run
     * @return the singular result giving the latest currently-running run of the workflow, or null if it is not currently running
     * @throws AtlanException on any API communication issue
     */
    public static WorkflowSearchResult findCurrentRun(AtlanClient client, String workflowName) throws AtlanException {

        SortOptions sort = SortOptions.of(s -> s.field(FieldSort.of(f -> f.field("metadata.creationTimestamp")
                .order(SortOrder.Desc)
                .nested(NestedSortValue.of(v -> v.path("metadata"))))));

        Query name = TermQuery.of(
                        t -> t.field("spec.workflowTemplateRef.name.keyword").value(workflowName))
                ._toQuery();

        Query byName = NestedQuery.of(n -> n.path("spec").query(name))._toQuery();

        Query query = BoolQuery.of(b -> b.filter(byName))._toQuery();

        WorkflowSearchRequest request = WorkflowSearchRequest.builder()
                .from(0)
                .size(50)
                .sortOption(sort)
                .query(query)
                .build();

        WorkflowSearchResponse response = client.workflows.searchRuns(request);
        if (response != null) {
            List<WorkflowSearchResult> results = response.getHits().getHits();
            if (results != null && !results.isEmpty()) {
                for (WorkflowSearchResult result : results) {
                    if (result.getStatus() == AtlanWorkflowPhase.RUNNING
                            || result.getStatus() == AtlanWorkflowPhase.PENDING) {
                        return result;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Find a specific run of a given workflow.
     *
     * @param client connectivity to the Atlan tenant on which to find a specific run of the workflow
     * @param workflowRunName name of the specific workflow run to find
     * @return the singular result giving the specific run of the workflow
     * @throws AtlanException on any API communication issue
     */
    public static WorkflowSearchResult findRunByName(AtlanClient client, String workflowRunName) throws AtlanException {
        SortOptions sort = SortOptions.of(s -> s.field(FieldSort.of(f -> f.field("metadata.creationTimestamp")
                .order(SortOrder.Desc)
                .nested(NestedSortValue.of(v -> v.path("metadata"))))));

        Query term = TermQuery.of(t -> t.field("metadata.name.keyword").value(workflowRunName))
                ._toQuery();

        Query nested = NestedQuery.of(n -> n.path("metadata").query(term))._toQuery();

        Query query = BoolQuery.of(b -> b.filter(nested))._toQuery();

        WorkflowSearchRequest request = WorkflowSearchRequest.builder()
                .from(0)
                .size(10)
                .sortOption(sort)
                .query(query)
                .build();

        WorkflowSearchResponse response = client.workflows.searchRuns(request);
        if (response != null) {
            List<WorkflowSearchResult> results = response.getHits().getHits();
            if (results != null && !results.isEmpty()) {
                return results.get(0);
            }
        }
        return null;
    }

    /**
     * Find workflows based on their type.
     *
     * @param client connectivity to the Atlan tenant on which to find the workflows
     * @param type of the workflow
     * @param maxResults the maximum number of results to retrieve
     * @return the list of workflows of the provided type, with the most-recently created first
     * @throws AtlanException on any API communication issue
     */
    public static List<WorkflowSearchResult> findByType(AtlanClient client, AtlanPackageType type, int maxResults)
            throws AtlanException {
        String regExBuilder = type.getValue().replace("-", "[-]") + "[-][0-9]{10}";
        SortOptions sort = SortOptions.of(s -> s.field(FieldSort.of(f -> f.field("metadata.creationTimestamp")
                .order(SortOrder.Desc)
                .nested(NestedSortValue.of(v -> v.path("metadata"))))));

        Query term = RegexpQuery.of(t -> t.field("metadata.name.keyword").value(regExBuilder))
                ._toQuery();

        Query nested = NestedQuery.of(n -> n.path("metadata").query(term))._toQuery();

        Query query = BoolQuery.of(b -> b.filter(nested))._toQuery();

        WorkflowSearchRequest request = WorkflowSearchRequest.builder()
                .from(0)
                .size(maxResults)
                .sortOption(sort)
                .query(query)
                .build();

        WorkflowSearchResponse response = client.workflows.search(request);
        if (response != null && response.getHits() != null) {
            return response.getHits().getHits();
        }
        return null;
    }

    /**
     * Find a workflow based on is ID (e.g. {@code atlan-snowflake-miner-1714638976}).
     * Note: only workflows that have been run will be found.
     *
     * @param client connectivity to the Atlan tenant on which to find the workflow
     * @param id unique identifier of the specific workflow to find
     * @return singular result containing the workflow, or null if not found
     * @throws AtlanException on any API communication issue
     */
    public static WorkflowSearchResult findById(AtlanClient client, String id) throws AtlanException {
        Query term =
                TermQuery.of(t -> t.field("metadata.name.keyword").value(id))._toQuery();
        Query nested = NestedQuery.of(n -> n.path("metadata").query(term))._toQuery();
        Query query = BoolQuery.of(b -> b.filter(nested))._toQuery();
        WorkflowSearchRequest request =
                WorkflowSearchRequest.builder().from(0).size(2).query(query).build();
        WorkflowSearchResponse response = client.workflows.search(request);
        if (response != null
                && response.getHits() != null
                && !response.getHits().getHits().isEmpty()) {
            return response.getHits().getHits().get(0);
        }
        return null;
    }

    public abstract static class WorkflowSearchRequestBuilder<
                    C extends WorkflowSearchRequest, B extends WorkflowSearchRequestBuilder<C, B>>
            extends IndexSearchDSL.IndexSearchDSLBuilder<C, B> {}
}
