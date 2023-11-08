/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.workflow.*;
import com.atlan.net.ApiResource;
import com.atlan.net.RequestOptions;
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

    public WorkflowsEndpoint(AtlanClient client) {
        super(client);
    }

    /**
     * Run the provided workflow.
     *
     * @param workflow details of the workflow to run
     * @return details of the workflow run
     * @throws AtlanException on any API communication issue
     */
    public WorkflowResponse run(Workflow workflow) throws AtlanException {
        return run(workflow, null);
    }

    /**
     * Run the provided workflow.
     *
     * @param workflow details of the workflow to run
     * @param options to override default client settings
     * @return details of the workflow run
     * @throws AtlanException on any API communication issue
     */
    public WorkflowResponse run(Workflow workflow, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), String.format("%s?submit=true", workflows_endpoint));
        WorkflowResponse response = ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, workflow, WorkflowResponse.class, options);
        response.setClient(client);
        return response;
    }

    /**
     * Run the provided pre-existing workflow.
     *
     * @param workflow details of the pre-existing workflow to re-run
     * @return details of the workflow run
     * @throws AtlanException on any API communication issue
     */
    public WorkflowRunResponse run(WorkflowSearchResultDetail workflow) throws AtlanException {
        return run(workflow, null);
    }

    /**
     * Run the provided pre-existing workflow.
     *
     * @param workflow details of the pre-existing workflow to re-run
     * @param options to override default client settings
     * @return details of the workflow run
     * @throws AtlanException on any API communication issue
     */
    public WorkflowRunResponse run(WorkflowSearchResultDetail workflow, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), workflows_endpoint_run_existing);
        ReRunRequest request = ReRunRequest.builder()
                .namespace(workflow.getMetadata().getNamespace())
                .resourceName(workflow.getMetadata().getName())
                .build();
        WorkflowRunResponse response = ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, request, WorkflowRunResponse.class, options);
        response.setClient(client);
        return response;
    }

    /**
     * Archive (delete) the provided workflow.
     *
     * @param workflowName the workflow to delete
     * @throws AtlanException on any API communication issue
     */
    public void archive(String workflowName) throws AtlanException {
        archive(workflowName, null);
    }

    /**
     * Archive (delete) the provided workflow.
     *
     * @param workflowName the workflow to delete
     * @param options to override default client settings
     * @throws AtlanException on any API communication issue
     */
    public void archive(String workflowName, RequestOptions options) throws AtlanException {
        String url =
                String.format("%s%s", getBaseUrl(), String.format("%s/%s/archive", workflows_endpoint, workflowName));
        ApiResource.request(client, ApiResource.RequestMethod.POST, url, "", null, options);
    }

    /**
     * Search for workflow runs that meet the provided criteria.
     *
     * @param request criteria by which to find workflow runs
     * @return the matching workflow runs
     * @throws AtlanException on any API communication issue
     */
    public WorkflowSearchResponse searchRuns(WorkflowSearchRequest request) throws AtlanException {
        return searchRuns(request, null);
    }

    /**
     * Search for workflow runs that meet the provided criteria.
     *
     * @param request criteria by which to find workflow runs
     * @param options to override default client settings
     * @return the matching workflow runs
     * @throws AtlanException on any API communication issue
     */
    public WorkflowSearchResponse searchRuns(WorkflowSearchRequest request, RequestOptions options)
            throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), runs_search_endpoint);
        return ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, request, WorkflowSearchResponse.class, options);
    }

    /**
     * Search for workflows that meet the provided criteria.
     *
     * @param request criteria by which to find workflows
     * @return the matching workflows
     * @throws AtlanException on any API communication issue
     */
    public WorkflowSearchResponse search(WorkflowSearchRequest request) throws AtlanException {
        return search(request, null);
    }

    /**
     * Search for workflows that meet the provided criteria.
     *
     * @param request criteria by which to find workflows
     * @param options to override default client settings
     * @return the matching workflows
     * @throws AtlanException on any API communication issue
     */
    public WorkflowSearchResponse search(WorkflowSearchRequest request, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), workflows_search_endpoint);
        return ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, request, WorkflowSearchResponse.class, options);
    }

    /**
     * Request class for re-running a pre-existing workflow.
     */
    @Getter
    @SuperBuilder
    @EqualsAndHashCode(callSuper = false)
    static class ReRunRequest extends AtlanObject {
        private static final long serialVersionUID = 2L;

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
