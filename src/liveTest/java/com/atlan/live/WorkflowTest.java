/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import com.atlan.api.WorkflowsEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanWorkflowPhase;
import com.atlan.model.packages.ConnectionDelete;
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
    private static String workflowNamePreset = null;
    private static String workflowNameDataStudio = null;
    private static String workflowNameAPI = null;

    @Test(
            groups = {"purge.connection.s3"},
            dependsOnGroups = {"purge.lineage"},
            alwaysRun = true)
    void purgeConnectionS3() {
        try {
            Workflow deleteWorkflow = ConnectionDelete.creator(S3AssetTest.connectionQame, true);
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
        monitorStatus(workflowNameS3);
    }

    @Test(
            groups = {"workflow.archive.s3"},
            dependsOnGroups = {"workflow.status.s3"})
    void archiveWorkflowRunS3() {
        archiveWorkflowRun(workflowNameS3);
    }

    @Test(
            groups = {"purge.connection.data"},
            dependsOnGroups = {"purge.lineage"},
            alwaysRun = true)
    void purgeConnectionData() {
        try {
            Workflow deleteWorkflow = ConnectionDelete.creator(DataAssetTest.connectionQame, true);
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
        monitorStatus(workflowNameData);
    }

    @Test(
            groups = {"workflow.archive.data"},
            dependsOnGroups = {"workflow.status.data"})
    void archiveWorkflowRunData() {
        archiveWorkflowRun(workflowNameData);
    }

    @Test(
            groups = {"purge.connection.preset"},
            dependsOnGroups = {"purge.lineage"},
            alwaysRun = true)
    void purgeConnectionPreset() {
        try {
            Workflow deleteWorkflow = ConnectionDelete.creator(PresetAssetTest.connectionQame, true);
            WorkflowResponse response = deleteWorkflow.run();
            assertNotNull(response);
            workflowNamePreset = response.getMetadata().getName();
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to delete the Preset connection.");
        }
    }

    @Test(
            groups = {"workflow.status.preset"},
            dependsOnGroups = {"purge.connection.preset"})
    void monitorStatusPreset() {
        monitorStatus(workflowNamePreset);
    }

    @Test(
            groups = {"workflow.archive.preset"},
            dependsOnGroups = {"workflow.status.preset"})
    void archiveWorkflowRunPreset() {
        archiveWorkflowRun(workflowNamePreset);
    }

    @Test(
            groups = {"purge.connection.gds"},
            dependsOnGroups = {"purge.lineage"},
            alwaysRun = true)
    void purgeConnectionGDS() {
        try {
            Workflow deleteWorkflow = ConnectionDelete.creator(DataStudioAssetTest.connectionQame, true);
            WorkflowResponse response = deleteWorkflow.run();
            assertNotNull(response);
            workflowNameDataStudio = response.getMetadata().getName();
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to delete the Data Studio connection.");
        }
    }

    @Test(
            groups = {"workflow.status.gds"},
            dependsOnGroups = {"purge.connection.gds"})
    void monitorStatusGDS() {
        monitorStatus(workflowNameDataStudio);
    }

    @Test(
            groups = {"workflow.archive.gds"},
            dependsOnGroups = {"workflow.status.gds"})
    void archiveWorkflowRunGDS() {
        archiveWorkflowRun(workflowNameDataStudio);
    }

    @Test(
            groups = {"purge.connection.api"},
            dependsOnGroups = {"purge.lineage"},
            alwaysRun = true)
    void purgeConnectionAPI() {
        try {
            Workflow deleteWorkflow = ConnectionDelete.creator(APIAssetTest.connectionQame, true);
            WorkflowResponse response = deleteWorkflow.run();
            assertNotNull(response);
            workflowNameAPI = response.getMetadata().getName();
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to delete the API connection.");
        }
    }

    @Test(
            groups = {"workflow.status.api"},
            dependsOnGroups = {"purge.connection.api"})
    void monitorStatusAPI() {
        monitorStatus(workflowNameAPI);
    }

    @Test(
            groups = {"workflow.archive.api"},
            dependsOnGroups = {"workflow.status.api"})
    void archiveWorkflowRunAPI() {
        archiveWorkflowRun(workflowNameAPI);
    }

    private void monitorStatus(String workflowName) {
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
            assertNull(e, "Unexpected exception while trying to monitor " + workflowName + " deletion workflow.");
        }
    }

    private void archiveWorkflowRun(String workflowName) {
        try {
            WorkflowsEndpoint.archive(workflowName);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to archive the " + workflowName + " workflow run.");
        }
    }
}
