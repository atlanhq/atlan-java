package com.atlan.live.jackson;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import com.atlan.api.WorkflowsEndpointJ;
import com.atlan.exception.AtlanException;
import com.atlan.live.AtlanLiveTest;
import com.atlan.model.admin.*;
import com.atlan.model.enums.AtlanWorkflowPhase;
import com.atlan.model.responses.WorkflowResponseJ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

@Test(groups = {"workflow"})
public class WorkflowJTest extends AtlanLiveTest {

    private static final Logger log = LoggerFactory.getLogger(WorkflowJTest.class);

    private static String workflowName = null;

    @Test(
            groups = {"purge.connection"},
            dependsOnGroups = {"purge.lineage"},
            alwaysRun = true)
    void purgeConnection() {
        try {
            WorkflowJ deleteWorkflow = PackagesJ.getConnectionDelete(S3AssetJTest.connectionQame, true);
            WorkflowResponseJ response = deleteWorkflow.run();
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
                final WorkflowSearchResultJ runDetails = WorkflowSearchRequestJ.findLatestRun(workflowName);
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
            WorkflowsEndpointJ.archive(workflowName);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to archive the workflow run.");
        }
    }
}
