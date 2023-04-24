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
                    LineageProcess.refByGuid("20e1f41d-f246-40fc-b9f2-7700a84d13a1"),
                    LineageProcess.refByGuid("da56ecf3-a925-4ab5-878d-d0599e8db260")))
            .outputFromProcesses(Set.of(
                    LineageProcess.refByGuid("35ddc0c0-18f9-43d9-ab4b-b296e1e452e5"),
                    LineageProcess.refByGuid("04aaf67d-ce45-4770-bbcc-0f9cc9807bd0")))
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
            .dbtJobLastRun(-6796933137269949035L)
            .dbtJobNextRun(-7331715196206200523L)
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
            .dbtModelCompileStartedAt(-1318569586905225725L)
            .dbtModelCompileCompletedAt(-3545998527247369642L)
            .dbtModelExecuteStartedAt(8095444791227889306L)
            .dbtModelExecuteCompletedAt(-5910915299582223764L)
            .dbtModelExecutionTime(0.35820156127988867)
            .dbtModelRunGeneratedAt(9088070212838596768L)
            .dbtModelRunElapsedTime(0.8507109372592024)
            .dbtMetrics(Set.of(
                    DbtMetric.refByGuid("0d9ccd83-f096-4824-a522-d979150361f8"),
                    DbtMetric.refByGuid("cbb0ecfc-97fa-42c3-ad34-fc3fd4afba12")))
            .dbtModelSqlAssets(Set.of(
                    Table.refByGuid("968cfc6c-7a04-4569-a1e0-96cf44bacd90"),
                    Table.refByGuid("1d4d2b99-91f5-4b53-97af-fd406044cda4")))
            .dbtModelColumns(Set.of(
                    DbtModelColumn.refByGuid("2476f9e3-40cf-4ab7-850d-1e8e651bf19b"),
                    DbtModelColumn.refByGuid("7528afe2-0291-4e26-bfd3-704248ae58e8")))
            .primarySqlAsset(Table.refByGuid("17f3503d-37b6-4228-af4d-d344af729ee0"))
            .build();
    private static DbtModel frodo;
    private static String serialized;

    @Test(groups = {"DbtModel.builderEquivalency"})
    void builderEquivalency() {
        assertEquals(full.toBuilder().build(), full);
    }

    @Test(
            groups = {"DbtModel.serialize"},
            dependsOnGroups = {"DbtModel.builderEquivalency"})
    void serialization() {
        assertNotNull(full);
        serialized = full.toJson();
        assertNotNull(serialized);
    }

    @Test(
            groups = {"DbtModel.deserialize"},
            dependsOnGroups = {"DbtModel.serialize"})
    void deserialization() throws JsonProcessingException {
        assertNotNull(serialized);
        frodo = Serde.mapper.readValue(serialized, DbtModel.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"DbtModel.equivalency"},
            dependsOnGroups = {"DbtModel.serialize", "DbtModel.deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson();
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
    }

    @Test(
            groups = {"DbtModel.equivalency"},
            dependsOnGroups = {"DbtModel.serialize", "DbtModel.deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
