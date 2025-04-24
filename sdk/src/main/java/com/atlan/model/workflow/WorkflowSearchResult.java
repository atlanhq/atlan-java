/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.workflow;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanWorkflowPhase;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Encapsulation of a single search result for workflows.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class WorkflowSearchResult extends AtlanObject {
    private static final long serialVersionUID = 2L;

    String _index;
    String _type;
    String _id;
    Object _seq_no;
    Object _primary_term;
    List<Object> sort;
    WorkflowSearchResultDetail _source;

    /** Retrieve the status of the workflow run. */
    public AtlanWorkflowPhase getStatus() {
        if (_source != null) {
            WorkflowSearchResultStatus status = _source.getStatus();
            if (status != null) {
                return status.getPhase();
            }
        }
        return null;
    }

    /**
     * Re-run this workflow.
     *
     * @param client connectivity to the Atlan tenant on which to rerun the workflow
     * @return details of the workflow run
     * @throws AtlanException on any API communication issue
     */
    public WorkflowRunResponse rerun(AtlanClient client) throws AtlanException {
        return rerun(client, false);
    }

    /**
     * Re-run this workflow.
     *
     * @param client connectivity to the Atlan tenant on which to rerun the workflow
     * @param idempotent if true, the workflow will only be rerun if it is not already currently running
     * @return details of the workflow run (if idempotent, will return details of the already-running workflow)
     * @throws AtlanException on any API communication issue
     */
    public WorkflowRunResponse rerun(AtlanClient client, boolean idempotent) throws AtlanException {
        if (_source != null) {
            if (idempotent) {
                String name = _source.getSpec().getWorkflowTemplateRef().get("name");
                try {
                    Thread.sleep(10000);
                    WorkflowSearchResult running = WorkflowSearchRequest.findCurrentRun(client, name);
                    if (running != null) {
                        WorkflowRunResponse response = new WorkflowRunResponse();
                        response.client = client;
                        response.metadata = running._source.getMetadata();
                        response.spec = running._source.getSpec();
                        response.status = running._source.status;
                        return response;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            return client.workflows.run(_source);
        }
        return null;
    }
}
