/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.model.responses.WorkflowResponse;
import com.atlan.model.responses.WorkflowSearchResponse;
import com.atlan.model.workflow.Workflow;
import com.atlan.model.workflow.WorkflowSearchRequest;
import com.atlan.net.ApiResource;

/**
 * API endpoints for operating on Atlan's workflows.
 */
public class WorkflowsEndpoint {

    private static final String workflows_endpoint = "/api/service/workflows";
    private static final String runs_endpoint = "/api/service/runs";
    private static final String search_endpoint = runs_endpoint + "/indexsearch";

    /**
     * Run the provided workflow.
     *
     * @param workflow details of the workflow to run
     * @return details of the workflow run
     * @throws AtlanException on any API communication issue
     */
    public static WorkflowResponse run(Workflow workflow) throws AtlanException {
        String url = String.format("%s%s", Atlan.getBaseUrl(), String.format("%s?submit=true", workflows_endpoint));
        return ApiResource.request(ApiResource.RequestMethod.POST, url, workflow, WorkflowResponse.class, null);
    }

    /**
     * Archive (delete) the provided workflow.
     *
     * @param workflowName the workflow to delete
     * @throws AtlanException on any API communication issue
     */
    public static void archive(String workflowName) throws AtlanException {
        String url = String.format(
                "%s%s", Atlan.getBaseUrl(), String.format("%s/%s/archive", workflows_endpoint, workflowName));
        ApiResource.request(ApiResource.RequestMethod.POST, url, "", null, null);
    }

    /**
     * Search for workflows that meet the provided criteria.
     *
     * @param request criteria by which to find workflows
     * @return the matching workflows
     * @throws AtlanException on any API communication issue
     */
    public static WorkflowSearchResponse search(WorkflowSearchRequest request) throws AtlanException {
        String url = String.format("%s%s", Atlan.getBaseUrl(), search_endpoint);
        return ApiResource.request(ApiResource.RequestMethod.POST, url, request, WorkflowSearchResponse.class, null);
    }
}
