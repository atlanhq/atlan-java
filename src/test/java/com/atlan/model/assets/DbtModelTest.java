/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import static org.testng.Assert.*;

import com.atlan.model.enums.*;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.*;
import org.testng.annotations.Test;

public class DbtModelTest {

    private static final DbtModel full = DbtModel.builder()
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
            .certificateStatus(AtlanCertificateStatus.VERIFIED)
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
                    LineageProcess.refByGuid("4cede9b5-6fa0-4491-90a0-68843c297fac"),
                    LineageProcess.refByGuid("c26bfca6-50aa-4686-80e4-eecc133bc954")))
            .outputFromProcesses(Set.of(
                    LineageProcess.refByGuid("ec56dbbe-c0d5-4fe2-93c5-ada82f4b33a1"),
                    LineageProcess.refByGuid("aa13c90e-3f7e-46ba-90c1-bc3c338f25e7")))
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
            .dbtJobLastRun(1289137827582205892L)
            .dbtJobNextRun(6295028506991248184L)
            .dbtJobNextRunHumanized("dbtJobNextRunHumanized")
            .dbtEnvironmentName("dbtEnvironmentName")
            .dbtEnvironmentDbtVersion("dbtEnvironmentDbtVersion")
            .dbtTags(Set.of("one", "two", "three"))
            .dbtConnectionContext("dbtConnectionContext")
            .dbtSemanticLayerProxyUrl("dbtSemanticLayerProxyUrl")
            .dbtStatus("dbtStatus")
            .dbtError("dbtError")
            .dbtRawSQL("dbtRawSQL")
            .dbtCompiledSQL("dbtCompiledSQL")
            .dbtStats("dbtStats")
            .dbtMaterializationType("dbtMaterializationType")
            .dbtModelCompileStartedAt(1350881232421499713L)
            .dbtModelCompileCompletedAt(-3291654264385444318L)
            .dbtModelExecuteStartedAt(3757515381108401438L)
            .dbtModelExecuteCompletedAt(4744315584930698429L)
            .dbtModelExecutionTime(0.5078299653918605)
            .dbtModelRunGeneratedAt(-6642250286287884L)
            .dbtModelRunElapsedTime(0.05105799380588616)
            .dbtMetrics(Set.of(
                    DbtMetric.refByGuid("9166c944-4237-4810-8cc5-c896fc584304"),
                    DbtMetric.refByGuid("6eedff39-1112-4ca0-b6ae-e4a47f416555")))
            .dbtModelColumns(Set.of(
                    DbtModelColumn.refByGuid("c4a8caac-33c8-4e60-b54d-bd1c2649c27a"),
                    DbtModelColumn.refByGuid("c3744b50-51dd-40a4-a200-e74da1f5f14e")))
            .primarySqlAsset(Table.refByGuid("779cce78-3d9a-479a-9efe-b61b4b95c5ef"))
            .build();
    private static DbtModel frodo;
    private static String serialized;

    @Test(groups = {"builderEquivalency"})
    void builderEquivalency() {
        assertEquals(full.toBuilder().build(), full);
    }

    @Test(
            groups = {"serialize"},
            dependsOnGroups = {"builderEquivalency"})
    void serialization() {
        assertNotNull(full);
        serialized = full.toJson();
        assertNotNull(serialized);
    }

    @Test(
            groups = {"deserialize"},
            dependsOnGroups = {"serialize"})
    void deserialization() throws JsonProcessingException {
        assertNotNull(serialized);
        frodo = Serde.mapper.readValue(serialized, DbtModel.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"equivalency"},
            dependsOnGroups = {"serialize", "deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson();
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
    }

    @Test(
            groups = {"equivalency"},
            dependsOnGroups = {"serialize", "deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
