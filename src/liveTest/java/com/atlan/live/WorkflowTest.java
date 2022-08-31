/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import com.atlan.api.WorkflowsEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.*;
import com.atlan.model.enums.AtlanWorkflowPhase;
import com.atlan.model.workflow.Workflow;
import com.atlan.model.workflow.WorkflowResponse;
import com.atlan.model.workflow.WorkflowSearchRequest;
import com.atlan.model.workflow.WorkflowSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

@Test(groups = {"workflow"})
public class WorkflowTest extends AtlanLiveTest {

    private static final Logger log = LoggerFactory.getLogger(WorkflowTest.class);

    private static String workflowNameS3 = null;
    private static String workflowNameData = null;

    @Test(
            groups = {"purge.connection.s3"},
            dependsOnGroups = {"purge.lineage"},
            alwaysRun = true)
    void purgeConnectionS3() {
        try {
            Workflow deleteWorkflow = Packages.getConnectionDelete(S3AssetTest.connectionQame, true);
            WorkflowResponse response = deleteWorkflow.run();
            assertNotNull(response);
            workflowNameS3 = response.getMetadata().getName();
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to delete the S3 connection.");
        }
    }

    @Test(
            groups = {"workflow.status.s3"},
            dependsOnGroups = {"purge.connection.s3"})
    void monitorStatusS3() {
        try {
            AtlanWorkflowPhase status = null;
            do {
                final WorkflowSearchResult runDetails = WorkflowSearchRequest.findLatestRun(workflowNameS3);
                if (runDetails != null) {
                    status = runDetails.getStatus();
                }
                log.info("Workflow status: {}", status);
                Thread.sleep(5000);
            } while (status != null && status != AtlanWorkflowPhase.SUCCESS);
        } catch (AtlanException | InterruptedException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to monitor S3 deletion workflow.");
        }
    }

    @Test(
            groups = {"workflow.archive.s3"},
            dependsOnGroups = {"workflow.status.s3"})
    void archiveWorkflowRunS3() {
        try {
            WorkflowsEndpoint.archive(workflowNameS3);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to archive the S3 workflow run.");
        }
    }

    @Test(
            groups = {"purge.connection.data"},
            dependsOnGroups = {"purge.lineage"},
            alwaysRun = true)
    void purgeConnectionData() {
        try {
            Workflow deleteWorkflow = Packages.getConnectionDelete(DataAssetTest.connectionQame, true);
            WorkflowResponse response = deleteWorkflow.run();
            assertNotNull(response);
            workflowNameData = response.getMetadata().getName();
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to delete the data connection.");
        }
    }

    @Test(
            groups = {"workflow.status.data"},
            dependsOnGroups = {"purge.connection.data"})
    void monitorStatusData() {
        try {
            AtlanWorkflowPhase status = null;
            do {
                final WorkflowSearchResult runDetails = WorkflowSearchRequest.findLatestRun(workflowNameData);
                if (runDetails != null) {
                    status = runDetails.getStatus();
                }
                log.info("Workflow status: {}", status);
                Thread.sleep(5000);
            } while (status != null && status != AtlanWorkflowPhase.SUCCESS);
        } catch (AtlanException | InterruptedException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to monitor data deletion workflow.");
        }
    }

    @Test(
            groups = {"workflow.archive.data"},
            dependsOnGroups = {"workflow.status.data"})
    void archiveWorkflowRunData() {
        try {
            WorkflowsEndpoint.archive(workflowNameData);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to archive the data workflow run.");
        }
    }
}
