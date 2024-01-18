/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.workflow;

import com.atlan.exception.AtlanException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class WorkflowRunResponse extends WorkflowResponse {
    private static final long serialVersionUID = 2L;

    WorkflowSearchResultStatus status;

    /**
     * Retrieve the workflow run details.
     *
     * @param name of the specific workflow run
     * @return the details of the workflow run
     * @throws AtlanException on any API errors searching for the workflow run
     */
    @Override
    protected WorkflowSearchResult getRunDetails(String name) throws AtlanException {
        return WorkflowSearchRequest.findRunByName(client, name);
    }

    /**
     * Stop this workflow run.
     * Note: the result will be returned immediately (async), so you may need to further poll
     * until the workflow is actually stopped.
     *
     * @throws AtlanException on any API errors stopping the workflow run
     * @return the result of the stop command
     */
    public WorkflowRunResponse stop() throws AtlanException {
        return client.workflows.stop(getMetadata().getName(), null);
    }
}
