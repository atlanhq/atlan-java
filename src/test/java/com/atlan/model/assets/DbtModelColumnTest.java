/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import static org.testng.Assert.*;

import com.atlan.model.enums.*;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.*;
import org.testng.annotations.Test;

public class DbtModelColumnTest {

    private static final DbtModelColumn full = DbtModelColumn.builder()
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
                    LineageProcess.refByGuid("cab21b07-9bef-44da-88fe-36629c6a812b"),
                    LineageProcess.refByGuid("f383f36c-96b4-4d67-8abb-38a45a9aeb0d")))
            .outputFromProcesses(Set.of(
                    LineageProcess.refByGuid("0e240d2d-a979-48c5-93dc-f79d0cbf589b"),
                    LineageProcess.refByGuid("52f903e8-f4f5-4795-ba54-8469399ea6a1")))
            .dbtAlias("dbtAlias")
            .dbtMeta("dbtMeta")
            .dbtUniqueId("dbtUniqueId")
            .dbtAccountName("dbtAccountName")
            .dbtProjectName("dbtProjectName")
            .dbtPackageName("dbtPackageName")
            .dbtJobName("dbtJobName")
            .dbtJobSchedule("dbtJobSchedule")
            .dbtJobStatus("dbtJobStatus")
            .dbtJobScheduleCronHumanized("dbtJobScheduleCronHumanized")
            .dbtJobLastRun(-8406688296589677162L)
            .dbtJobNextRun(3772585979247133488L)
            .dbtJobNextRunHumanized("dbtJobNextRunHumanized")
            .dbtEnvironmentName("dbtEnvironmentName")
            .dbtEnvironmentDbtVersion("dbtEnvironmentDbtVersion")
            .dbtTags(Set.of("one", "two", "three"))
            .dbtConnectionContext("dbtConnectionContext")
            .dbtSemanticLayerProxyUrl("dbtSemanticLayerProxyUrl")
            .dbtModelQualifiedName("dbtModelQualifiedName")
            .dbtModelColumnDataType("dbtModelColumnDataType")
            .dbtModelColumnOrder(2061906394)
            .dbtModelColumnSqlColumns(Set.of(
                    Column.refByGuid("80c012c1-b18e-4ba5-afc7-a94470466e98"),
                    Column.refByGuid("9f1539c7-bd4b-4f94-9f39-fe4b4c0471b4")))
            .sqlColumn(Column.refByGuid("98d61f93-0f4f-4639-abec-d3395cc17b8b"))
            .dbtModel(DbtModel.refByGuid("c0199c89-49fb-4918-aab2-176f784e9fdc"))
            .build();
    private static DbtModelColumn frodo;
    private static String serialized;

    @Test(groups = {"DbtModelColumn.builderEquivalency"})
    void builderEquivalency() {
        assertEquals(full.toBuilder().build(), full);
    }

    @Test(
            groups = {"DbtModelColumn.serialize"},
            dependsOnGroups = {"DbtModelColumn.builderEquivalency"})
    void serialization() {
        assertNotNull(full);
        serialized = full.toJson();
        assertNotNull(serialized);
    }

    @Test(
            groups = {"DbtModelColumn.deserialize"},
            dependsOnGroups = {"DbtModelColumn.serialize"})
    void deserialization() throws JsonProcessingException {
        assertNotNull(serialized);
        frodo = Serde.mapper.readValue(serialized, DbtModelColumn.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"DbtModelColumn.equivalency"},
            dependsOnGroups = {"DbtModelColumn.serialize", "DbtModelColumn.deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson();
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
    }

    @Test(
            groups = {"DbtModelColumn.equivalency"},
            dependsOnGroups = {"DbtModelColumn.serialize", "DbtModelColumn.deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
