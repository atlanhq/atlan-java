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
     * Note that this variation of the method will not log any activity, but will simply block
     * until the workflow completes.
     *
     * @return the status at completion, or null if the workflow was not even run
     * @throws AtlanException on any errors running the workflow
     * @throws InterruptedException on any interruption of the busy wait loop
     */
    public AtlanWorkflowPhase monitorStatus() throws AtlanException, InterruptedException {
        return monitorStatus(null);
    }

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
            String name = getMetadata().getName();
            AtlanWorkflowPhase status = null;
            do {
                // Fix a value here so that we go to the high-end of the wait duration,
                // but still apply a jitter each time
                // (Moved to the start of the loop to give a brief startup delay to avoid
                //  any false-positives on retrieving the latest run and picking up a previous
                //  run.)
                Thread.sleep(HttpClient.waitTime(5).toMillis());
                final WorkflowSearchResult runDetails = getRunDetails(name);
                if (runDetails != null) {
                    status = runDetails.getStatus();
                }
                if (log != null) {
                    log.info("Workflow status: {}", status);
                }
            } while (status != AtlanWorkflowPhase.SUCCESS
                    && status != AtlanWorkflowPhase.ERROR
                    && status != AtlanWorkflowPhase.FAILED);
            return status;
        } else {
            if (log != null) {
                log.info("... skipping workflow monitoring — nothing to monitor.");
            }
            return null;
        }
    }

    /**
     * Retrieve the workflow run details.
     *
     * @param name of the workflow template
     * @return the details of the workflow run
     * @throws AtlanException on any API errors searching for the workflow run
     */
    protected WorkflowSearchResult getRunDetails(String name) throws AtlanException {
        return WorkflowSearchRequest.findLatestRun(name);
    }
}
