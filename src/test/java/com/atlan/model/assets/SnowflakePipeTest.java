/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import static org.testng.Assert.*;

import com.atlan.model.enums.*;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.*;
import org.testng.annotations.Test;

public class SnowflakePipeTest {

    private static final SnowflakePipe full = SnowflakePipe.builder()
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
                    LineageProcess.refByGuid("5f3d8e02-14fd-4047-986c-8900c48cb5d1"),
                    LineageProcess.refByGuid("6ea69adf-0647-4a47-afa9-8d6a5acf9e14")))
            .outputFromProcesses(Set.of(
                    LineageProcess.refByGuid("9a3ef338-16e8-4f3b-8faf-d8de80ed4020"),
                    LineageProcess.refByGuid("d9910dd5-13d2-4b61-8065-0b943bb56b6f")))
            .queryCount(980828367166852340L)
            .queryUserCount(-4523751100975730859L)
            // .queryUserMap(Map.of("key1", 123456L, "key2", 654321L))
            .queryCountUpdatedAt(-3802346789333478547L)
            .databaseName("databaseName")
            .databaseQualifiedName("databaseQualifiedName")
            .schemaName("schemaName")
            .schemaQualifiedName("schemaQualifiedName")
            .tableName("tableName")
            .tableQualifiedName("tableQualifiedName")
            .viewName("viewName")
            .viewQualifiedName("viewQualifiedName")
            .isProfiled(false)
            .lastProfiledAt(6602170762858407790L)
            .dbtModels(Set.of(
                    DbtModel.refByGuid("d24dd6a5-8a97-40f0-ba6e-1459400918b4"),
                    DbtModel.refByGuid("34aea413-abf9-4bd3-ae79-cc181806f5fe")))
            .dbtSources(Set.of(
                    DbtSource.refByGuid("d4a7c22a-9bd4-401a-8000-218d41dea57e"),
                    DbtSource.refByGuid("4fda5321-4fc3-4f0b-afea-ea5326a46dee")))
            .sqlDbtModels(Set.of(
                    DbtModel.refByGuid("aefbc009-0bfe-49bf-a9e9-4d0d80775560"),
                    DbtModel.refByGuid("91712be4-cddf-4ba2-b6cc-5b432a203d10")))
            .sqlDBTSources(Set.of(
                    DbtSource.refByGuid("a774f9f1-7a58-4e7e-bdb5-8ac22bcde593"),
                    DbtSource.refByGuid("36421c8b-90f2-41b1-90ef-347fdeaf5b08")))
            .definition("definition")
            .snowflakePipeIsAutoIngestEnabled(true)
            .snowflakePipeNotificationChannelName("snowflakePipeNotificationChannelName")
            .schema(Schema.refByGuid("9b17fb5b-10a7-441d-a380-1ab25e02c534"))
            .build();
    private static SnowflakePipe frodo;
    private static String serialized;

    @Test(groups = {"SnowflakePipe.builderEquivalency"})
    void builderEquivalency() {
        assertEquals(full.toBuilder().build(), full);
    }

    @Test(
            groups = {"SnowflakePipe.serialize"},
            dependsOnGroups = {"SnowflakePipe.builderEquivalency"})
    void serialization() {
        assertNotNull(full);
        serialized = full.toJson();
        assertNotNull(serialized);
    }

    @Test(
            groups = {"SnowflakePipe.deserialize"},
            dependsOnGroups = {"SnowflakePipe.serialize"})
    void deserialization() throws JsonProcessingException {
        assertNotNull(serialized);
        frodo = Serde.mapper.readValue(serialized, SnowflakePipe.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"SnowflakePipe.equivalency"},
            dependsOnGroups = {"SnowflakePipe.serialize", "SnowflakePipe.deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson();
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
    }

    @Test(
            groups = {"SnowflakePipe.equivalency"},
            dependsOnGroups = {"SnowflakePipe.serialize", "SnowflakePipe.deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
