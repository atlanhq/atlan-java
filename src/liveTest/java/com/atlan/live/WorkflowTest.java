package com.atlan.live;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import com.atlan.api.WorkflowsEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.*;
import com.atlan.model.enums.AtlanWorkflowPhase;
import com.atlan.model.responses.WorkflowResponse;
import com.atlan.model.workflow.Workflow;
import com.atlan.model.workflow.WorkflowSearchRequest;
import com.atlan.model.workflow.WorkflowSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

@Test(groups = {"workflow"})
public class WorkflowTest extends AtlanLiveTest {

    private static final Logger log = LoggerFactory.getLogger(WorkflowTest.class);

    private static String workflowName = null;

    @Test(
            groups = {"purge.connection"},
            dependsOnGroups = {"purge.lineage"},
            alwaysRun = true)
    void purgeConnection() {
        try {
            Workflow deleteWorkflow = Packages.getConnectionDelete(S3AssetTest.connectionQame, true);
            WorkflowResponse response = deleteWorkflow.run();
            assertNotNull(response);
            workflowName = response.getMetadata().getName();
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to delete a connection.");
        }
    }

    @Test(
            groups = {"workflow.status"},
            dependsOnGroups = {"purge.connection"})
    void monitorStatus() {
        try {
            AtlanWorkflowPhase status = null;
            do {
                final WorkflowSearchResult runDetails = WorkflowSearchRequest.findLatestRun(workflowName);
                if (runDetails != null) {
                    status = runDetails.getStatus();
                }
                log.info("Workflow status: {}", status);
                Thread.sleep(5000);
            } while (status != null && status != AtlanWorkflowPhase.SUCCESS);
        } catch (AtlanException | InterruptedException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to monitor deletion workflow.");
        }
    }

    @Test(
            groups = {"workflow.archive"},
            dependsOnGroups = {"workflow.status"})
    void archiveWorkflowRun() {
        try {
            WorkflowsEndpoint.archive(workflowName);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to archive the workflow run.");
        }
    }
}
