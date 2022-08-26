package com.atlan.api;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.WorkflowJ;
import com.atlan.model.admin.WorkflowSearchRequestJ;
import com.atlan.model.responses.WorkflowResponseJ;
import com.atlan.model.responses.WorkflowSearchResponseJ;
import com.atlan.net.ApiResourceJ;

public class WorkflowsEndpointJ {

    private static final String workflows_endpoint = "/api/service/workflows";
    private static final String runs_endpoint = "/api/service/runs";
    private static final String search_endpoint = runs_endpoint + "/indexsearch";

    /**
     * Run the provided workflow.
     * @param workflow details of the workflow to run
     * @return details of the workflow run
     * @throws AtlanException on any API communication issue
     */
    public static WorkflowResponseJ run(WorkflowJ workflow) throws AtlanException {
        String url = String.format("%s%s", Atlan.getBaseUrl(), String.format("%s?submit=true", workflows_endpoint));
        return ApiResourceJ.request(ApiResourceJ.RequestMethod.POST, url, workflow, WorkflowResponseJ.class, null);
    }

    /**
     * Archive (delete) the provided workflow.
     * @param workflowName the workflow to delete
     * @throws AtlanException on any API communication issue
     */
    public static void archive(String workflowName) throws AtlanException {
        String url = String.format(
                "%s%s", Atlan.getBaseUrl(), String.format("%s/%s/archive", workflows_endpoint, workflowName));
        ApiResourceJ.request(ApiResourceJ.RequestMethod.POST, url, "", null, null);
    }

    /**
     * Search for workflows that meet the provided criteria.
     * @param request criteria by which to find workflows
     * @return the matching workflows
     * @throws AtlanException on any API communication issue
     */
    public static WorkflowSearchResponseJ search(WorkflowSearchRequestJ request) throws AtlanException {
        String url = String.format("%s%s", Atlan.getBaseUrl(), search_endpoint);
        return ApiResourceJ.request(ApiResourceJ.RequestMethod.POST, url, request, WorkflowSearchResponseJ.class, null);
    }
}
