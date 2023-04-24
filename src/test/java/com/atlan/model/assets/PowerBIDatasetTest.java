/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import static org.testng.Assert.*;

import com.atlan.model.enums.*;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.*;
import org.testng.annotations.Test;

public class PowerBIDatasetTest {

    private static final PowerBIDataset full = PowerBIDataset.builder()
            .guid("guid")
            .displayText("displayText")
            .status(AtlanStatus.ACTIVE)
            .createdBy("createdBy")
            .updatedBy("updatedBy")
            .createTime(123456789L)
            .updateTime(123456789L)
            .isIncomplete(false)
            .qualifiedName("qualifiedName")
            .name("name")
            .displayName("displayName")
            .description("description")
            .userDescription("userDescription")
            .tenantId("tenantId")
            .certificateStatus(CertificateStatus.VERIFIED)
            .certificateStatusMessage("certificateStatusMessage")
            .certificateUpdatedBy("certificateUpdatedBy")
            .certificateUpdatedAt(123456789L)
            .announcementTitle("announcementTitle")
            .announcementMessage("announcementMessage")
            .announcementUpdatedAt(123456789L)
            .announcementUpdatedBy("announcementUpdatedBy")
            .announcementType(AtlanAnnouncementType.INFORMATION)
            .ownerUser("ownerUser")
            .ownerGroup("ownerGroup")
            .adminUser("adminUser")
            .adminGroup("adminGroup")
            .adminRole("adminRole")
            .viewerUser("viewerUser")
            .viewerGroup("viewerGroup")
            .connectorType(AtlanConnectorType.PRESTO)
            .connectionName("connectionName")
            .connectionQualifiedName("connectionQualifiedName")
            .hasLineage(false)
            .isDiscoverable(true)
            .isEditable(true)
            .viewScore(123456.0)
            .popularityScore(123456.0)
            .sourceOwners("sourceOwners")
            .sourceURL("sourceURL")
            .sourceEmbedURL("sourceEmbedURL")
            .lastSyncWorkflowName("lastSyncWorkflowName")
            .lastSyncRunAt(123456789L)
            .lastSyncRun("lastSyncRun")
            .sourceCreatedBy("sourceCreatedBy")
            .sourceCreatedAt(123456789L)
            .sourceUpdatedAt(123456789L)
            .sourceUpdatedBy("sourceUpdatedBy")
            .dbtQualifiedName("dbtQualifiedName")
            .assetDbtAlias("assetDbtAlias")
            .assetDbtMeta("assetDbtMeta")
            .assetDbtUniqueId("assetDbtUniqueId")
            .assetDbtAccountName("assetDbtAccountName")
            .assetDbtProjectName("assetDbtProjectName")
            .assetDbtPackageName("assetDbtPackageName")
            .assetDbtJobName("assetDbtJobName")
            .assetDbtJobSchedule("assetDbtJobSchedule")
            .assetDbtJobStatus("assetDbtJobStatus")
            .assetDbtJobScheduleCronHumanized("assetDbtJobScheduleCronHumanized")
            .assetDbtJobLastRun(123456789L)
            .assetDbtJobLastRunUrl("assetDbtJobLastRunUrl")
            .assetDbtJobLastRunCreatedAt(123456789L)
            .assetDbtJobLastRunUpdatedAt(123456789L)
            .assetDbtJobLastRunDequedAt(123456789L)
            .assetDbtJobLastRunStartedAt(123456789L)
            .assetDbtJobLastRunTotalDuration("assetDbtJobLastRunTotalDuration")
            .assetDbtJobLastRunTotalDurationHumanized("assetDbtJobLastRunTotalDurationHumanized")
            .assetDbtJobLastRunQueuedDuration("assetDbtJobLastRunQueuedDuration")
            .assetDbtJobLastRunQueuedDurationHumanized("assetDbtJobLastRunQueuedDurationHumanized")
            .assetDbtJobLastRunRunDuration("assetDbtJobLastRunRunDuration")
            .assetDbtJobLastRunRunDurationHumanized("assetDbtJobLastRunRunDurationHumanized")
            .assetDbtJobLastRunGitBranch("assetDbtJobLastRunGitBranch")
            .assetDbtJobLastRunGitSha("assetDbtJobLastRunGitSha")
            .assetDbtJobLastRunStatusMessage("assetDbtJobLastRunStatusMessage")
            .assetDbtJobLastRunOwnerThreadId("assetDbtJobLastRunOwnerThreadId")
            .assetDbtJobLastRunExecutedByThreadId("assetDbtJobLastRunExecutedByThreadId")
            .assetDbtJobLastRunArtifactsSaved(true)
            .assetDbtJobLastRunArtifactS3Path("assetDbtJobLastRunArtifactS3Path")
            .assetDbtJobLastRunHasDocsGenerated(false)
            .assetDbtJobLastRunHasSourcesGenerated(true)
            .assetDbtJobLastRunNotificationsSent(false)
            .assetDbtJobNextRun(123456789L)
            .assetDbtJobNextRunHumanized("assetDbtJobNextRunHumanized")
            .assetDbtEnvironmentName("assetDbtEnvironmentName")
            .assetDbtEnvironmentDbtVersion("assetDbtEnvironmentDbtVersion")
            .assetDbtTag("assetDbtTag1")
            .assetDbtTag("assetDbtTag2")
            .assetDbtSemanticLayerProxyUrl("assetDbtSemanticLayerProxyUrl")
            .link(Link.refByGuid("linkGuid1"))
            .link(Link.refByGuid("linkGuid2"))
            .readme(Readme.refByGuid("readmeGuid"))
            .assignedTerm(GlossaryTerm.refByGuid("termGuid1"))
            .assignedTerm(GlossaryTerm.refByGuid("termGuid2"))
            .inputToProcesses(Set.of(
                    LineageProcess.refByGuid("2e4e03a6-46ea-4482-96a4-caef2cd06ecc"),
                    LineageProcess.refByGuid("d0444ab6-81c6-45a4-84d8-e099292c3f4e")))
            .outputFromProcesses(Set.of(
                    LineageProcess.refByGuid("afa89ed6-472a-4673-90bf-fed9e9a4f78e"),
                    LineageProcess.refByGuid("e86dbcd3-6924-4c0f-b6e4-cde81e7a9b4d")))
            .powerBIIsHidden(true)
            .powerBITableQualifiedName("powerBITableQualifiedName")
            .powerBIFormatString("powerBIFormatString")
            .powerBIEndorsement(PowerBIEndorsementType.PROMOTED)
            .workspaceQualifiedName("workspaceQualifiedName")
            .webUrl("webUrl")
            .reports(Set.of(
                    PowerBIReport.refByGuid("1d689381-44b9-497b-a4a3-dfb02d17f1af"),
                    PowerBIReport.refByGuid("9691f0e7-1ee3-4aa8-a7ab-e3823230a187")))
            .tiles(Set.of(
                    PowerBITile.refByGuid("d63398cf-a0d3-4bbc-8f15-2e6a4a178a8c"),
                    PowerBITile.refByGuid("6b7477b4-d737-450a-9051-d63b5edc5f63")))
            .workspace(PowerBIWorkspace.refByGuid("76b87649-b3b4-46b6-83c5-6f7b6f316441"))
            .tables(Set.of(
                    PowerBITable.refByGuid("cf5bbd58-c321-4da7-a6d7-9f9fd807a67a"),
                    PowerBITable.refByGuid("05d8b6f6-7b0e-424c-9702-bd09bfb0b222")))
            .datasources(Set.of(
                    PowerBIDatasource.refByGuid("f9570e8e-0ec5-41a5-9071-eead53d48a0c"),
                    PowerBIDatasource.refByGuid("55ef4c96-ec63-4d3a-81fa-08858864d91f")))
            .dataflows(Set.of(
                    PowerBIDataflow.refByGuid("56d6a2f9-b70e-4d29-b832-31af07d17a4c"),
                    PowerBIDataflow.refByGuid("6acd4165-4234-42ff-bb88-6af29bdf32ac")))
            .build();
    private static PowerBIDataset frodo;
    private static String serialized;

    @Test(groups = {"PowerBIDataset.builderEquivalency"})
    void builderEquivalency() {
        assertEquals(full.toBuilder().build(), full);
    }

    @Test(
            groups = {"PowerBIDataset.serialize"},
            dependsOnGroups = {"PowerBIDataset.builderEquivalency"})
    void serialization() {
        assertNotNull(full);
        serialized = full.toJson();
        assertNotNull(serialized);
    }

    @Test(
            groups = {"PowerBIDataset.deserialize"},
            dependsOnGroups = {"PowerBIDataset.serialize"})
    void deserialization() throws JsonProcessingException {
        assertNotNull(serialized);
        frodo = Serde.mapper.readValue(serialized, PowerBIDataset.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"PowerBIDataset.equivalency"},
            dependsOnGroups = {"PowerBIDataset.serialize", "PowerBIDataset.deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson();
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
    }

    @Test(
            groups = {"PowerBIDataset.equivalency"},
            dependsOnGroups = {"PowerBIDataset.serialize", "PowerBIDataset.deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
