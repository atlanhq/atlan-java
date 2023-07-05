/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.workflow.*;
import com.atlan.net.ApiResource;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * API endpoints for operating on Atlan's workflows.
 */
public class WorkflowsEndpoint extends HeraclesEndpoint {

    private static final String workflows_endpoint = "/workflows";
    private static final String workflows_endpoint_run_existing = workflows_endpoint + "/submit";
    private static final String workflows_search_endpoint = workflows_endpoint + "/indexsearch";
    private static final String runs_endpoint = "/runs";
    private static final String runs_search_endpoint = runs_endpoint + "/indexsearch";

    private final AtlanClient client;

    public WorkflowsEndpoint(AtlanClient client) {
        this.client = client;
    }

    /**
     * Run the provided workflow.
     *
     * @param workflow details of the workflow to run
     * @return details of the workflow run
     * @throws AtlanException on any API communication issue
     */
    public WorkflowResponse run(Workflow workflow) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(client), String.format("%s?submit=true", workflows_endpoint));
        return ApiResource.request(client, ApiResource.RequestMethod.POST, url, workflow, WorkflowResponse.class, null);
    }

    /**
     * Run the provided pre-existing workflow.
     *
     * @param workflow details of the pre-existing workflow to re-run
     * @return details of the workflow run
     * @throws AtlanException on any API communication issue
     */
    public WorkflowRunResponse run(WorkflowSearchResultDetail workflow) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(client), workflows_endpoint_run_existing);
        ReRunRequest request = ReRunRequest.builder()
                .namespace(workflow.getMetadata().getNamespace())
                .resourceName(workflow.getMetadata().getName())
                .build();
        return ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, request, WorkflowRunResponse.class, null);
    }

    /**
     * Archive (delete) the provided workflow.
     *
     * @param workflowName the workflow to delete
     * @throws AtlanException on any API communication issue
     */
    public void archive(String workflowName) throws AtlanException {
        String url = String.format(
                "%s%s", getBaseUrl(client), String.format("%s/%s/archive", workflows_endpoint, workflowName));
        ApiResource.request(client, ApiResource.RequestMethod.POST, url, "", null, null);
    }

    /**
     * Search for workflow runs that meet the provided criteria.
     *
     * @param request criteria by which to find workflow runs
     * @return the matching workflow runs
     * @throws AtlanException on any API communication issue
     */
    public WorkflowSearchResponse searchRuns(WorkflowSearchRequest request) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(client), runs_search_endpoint);
        return ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, request, WorkflowSearchResponse.class, null);
    }

    /**
     * Search for workflows that meet the provided criteria.
     *
     * @param request criteria by which to find workflows
     * @return the matching workflows
     * @throws AtlanException on any API communication issue
     */
    public WorkflowSearchResponse search(WorkflowSearchRequest request) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(client), workflows_search_endpoint);
        return ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, request, WorkflowSearchResponse.class, null);
    }

    /**
     * Request class for re-running a pre-existing workflow.
     */
    @Getter
    @SuperBuilder
    @EqualsAndHashCode(callSuper = false)
    static class ReRunRequest extends AtlanObject {
        /** Namespace of the workflow. */
        @Builder.Default
        String namespace = "default";

        /** Resource type of the workflow. */
        @Builder.Default
        String resourceKind = "WorkflowTemplate";

        /** Name of the workflow. */
        String resourceName;
    }
}
