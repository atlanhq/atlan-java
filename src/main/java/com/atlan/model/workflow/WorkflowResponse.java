/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.workflow;

import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanWorkflowPhase;
import com.atlan.net.ApiResource;
import com.atlan.net.HttpClient;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;

@Data
@EqualsAndHashCode(callSuper = false)
public class WorkflowResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    WorkflowMetadata metadata;
    WorkflowSpec spec;
    List<Object> payload;

    /**
     * Monitor the status of the workflow's run, blocking until it has completed.
     *
     * @param log through which to log status information (INFO-level)
     * @return the status at completion, or null if the workflow was not even run
     * @throws AtlanException on any errors running the workflow
     * @throws InterruptedException on any interruption of the busy wait loop
     */
    public AtlanWorkflowPhase monitorStatus(Logger log) throws AtlanException, InterruptedException {
        if (getMetadata() != null && getMetadata().getName() != null) {
            // If the workflow name is null, it wasn't run (idempotent skip)
            // ... so we can also skip searching for its status
            String workflowName = getMetadata().getName();
            AtlanWorkflowPhase status = null;
            do {
                final WorkflowSearchResult runDetails = WorkflowSearchRequest.findLatestRun(workflowName);
                if (runDetails != null) {
                    status = runDetails.getStatus();
                }
                log.info("Workflow status: {}", status);
                // Fix a value here so that we go to the high-end of the wait duration,
                // but still apply a jitter each time
                Thread.sleep(HttpClient.waitTime(5).toMillis());
            } while (status != AtlanWorkflowPhase.SUCCESS
                && status != AtlanWorkflowPhase.ERROR
                && status != AtlanWorkflowPhase.FAILED);
            return status;
        } else {
            log.info("... skipping workflow monitoring — nothing to monitor.");
            return null;
        }
    }
}
