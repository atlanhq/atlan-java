/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.workflow;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanWorkflowPhase;
import com.atlan.net.ApiResource;
import com.atlan.net.HttpClient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.event.Level;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class WorkflowResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    /** Connectivity to the Atlan tenant where the workflow request was run. */
    @Setter
    @JsonIgnore
    AtlanClient client;

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
     * @param log through which to log status information (INFO-level, by default)
     * @return the status at completion, or null if the workflow was not even run
     * @throws AtlanException on any errors running the workflow
     * @throws InterruptedException on any interruption of the busy wait loop
     */
    public AtlanWorkflowPhase monitorStatus(Logger log) throws AtlanException, InterruptedException {
        return monitorStatus(log, Level.INFO);
    }

    /**
     * Monitor the status of the workflow's run, blocking until it has completed.
     *
     * @param log through which to log status information (INFO-level)
     * @param level through which to log the status information
     * @return the status at completion, or null if the workflow was not even run
     * @throws AtlanException on any errors running the workflow
     * @throws InterruptedException on any interruption of the busy wait loop
     */
    public AtlanWorkflowPhase monitorStatus(Logger log, Level level) throws AtlanException, InterruptedException {
        return monitorStatus(log, level, Long.MAX_VALUE);
    }

    /**
     * Monitor the status of the workflow's run, blocking until it has completed.
     *
     * @param log through which to log status information (INFO-level)
     * @param level through which to log the status information
     * @param maxWaitTime maximum time to block (in seconds), after which to stop monitoring and return
     * @return the status at completion, or null if the workflow was not even run (or has not yet completed when returning)
     * @throws AtlanException on any errors running the workflow
     * @throws InterruptedException on any interruption of the busy wait loop
     */
    public AtlanWorkflowPhase monitorStatus(Logger log, Level level, long maxWaitTime)
            throws AtlanException, InterruptedException {
        long start = System.currentTimeMillis();
        long elapsed;
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
                    log.atLevel(level).log("Workflow {}: {}", name, status);
                }
                elapsed = (System.currentTimeMillis() - start) / 1000;
            } while (status != AtlanWorkflowPhase.SUCCESS
                    && status != AtlanWorkflowPhase.ERROR
                    && status != AtlanWorkflowPhase.FAILED
                    && elapsed <= maxWaitTime);
            if (log != null) {
                log.atLevel(level).log("Workflow {}: {}", name, status);
            }
            return status;
        } else {
            if (log != null) {
                log.atLevel(level).log("Skipping workflow monitoring — nothing to monitor.");
            }
            return null;
        }
    }

    /**
     * Stop this workflow.
     * Note: the result will be returned immediately (async), so you may need to further poll
     * until the workflow is actually stopped.
     *
     * @throws AtlanException on any API errors stopping the workflow run
     * @return the result of the stop command, or null if there was no running workflow to stop
     */
    public WorkflowRunResponse stop() throws AtlanException {
        WorkflowSearchResult latest = getRunDetails(getMetadata().getName());
        if (latest != null && latest.get_id() != null) {
            return client.workflows.stop(latest.get_id(), null);
        }
        return null;
    }

    /**
     * Retrieve the workflow run details.
     *
     * @param name of the workflow template
     * @return the details of the workflow run
     * @throws AtlanException on any API errors searching for the workflow run
     */
    protected WorkflowSearchResult getRunDetails(String name) throws AtlanException {
        return WorkflowSearchRequest.findLatestRun(client, name);
    }
}
