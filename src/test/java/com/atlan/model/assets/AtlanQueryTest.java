/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import static org.testng.Assert.*;

import com.atlan.model.enums.*;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.*;
import org.testng.annotations.Test;

public class AtlanQueryTest {

    private static final AtlanQuery full = AtlanQuery.builder()
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
                    LineageProcess.refByGuid("62643689-44ff-40ec-a58f-a1a8a6de09e1"),
                    LineageProcess.refByGuid("84fa3899-6ae1-4172-9cc9-84034785eba2")))
            .outputFromProcesses(Set.of(
                    LineageProcess.refByGuid("b7011b42-e0d5-4d86-9005-c01e774dfdfc"),
                    LineageProcess.refByGuid("98d6b502-522c-48d1-8eb4-6fb8ed3fc3da")))
            .queryCount(-5706166928549924885L)
            .queryUserCount(-7554215961707564365L)
            // .queryUserMap(Map.of("key1", 123456L, "key2", 654321L))
            .queryCountUpdatedAt(8201441841631989489L)
            .databaseName("databaseName")
            .databaseQualifiedName("databaseQualifiedName")
            .schemaName("schemaName")
            .schemaQualifiedName("schemaQualifiedName")
            .tableName("tableName")
            .tableQualifiedName("tableQualifiedName")
            .viewName("viewName")
            .viewQualifiedName("viewQualifiedName")
            .isProfiled(false)
            .lastProfiledAt(-6206043472338803859L)
            .dbtModels(Set.of(
                    DbtModel.refByGuid("10589e8b-3067-4ae2-bf27-5f825cf6b088"),
                    DbtModel.refByGuid("7174b7c6-c6b0-47a6-ab37-05ad1692d874")))
            .dbtSources(Set.of(
                    DbtSource.refByGuid("a4dc11ab-de49-4dd6-92f8-76f1204ee01b"),
                    DbtSource.refByGuid("26dbf273-cea8-4fd6-86cb-ab8744946de6")))
            .sqlDbtModels(Set.of(
                    DbtModel.refByGuid("bf19e1ca-777e-4d73-b411-af68829e3b24"),
                    DbtModel.refByGuid("60a5e2b8-5cb6-4527-afbc-7c698610c834")))
            .sqlDBTSources(Set.of(
                    DbtSource.refByGuid("3f1a5e9b-bb92-4e53-8ca1-7484e178f839"),
                    DbtSource.refByGuid("92be988a-cfa5-4f09-b3c4-28646f0a829d")))
            .rawQuery("rawQuery")
            .defaultSchemaQualifiedName("defaultSchemaQualifiedName")
            .defaultDatabaseQualifiedName("defaultDatabaseQualifiedName")
            .variablesSchemaBase64("variablesSchemaBase64")
            .isPrivate(true)
            .isSqlSnippet(true)
            .parentQualifiedName("parentQualifiedName")
            .collectionQualifiedName("collectionQualifiedName")
            .isVisualQuery(true)
            .visualBuilderSchemaBase64("visualBuilderSchemaBase64")
            .parent(Folder.refByGuid("fe3cbb73-bfa3-4bbd-a6bc-2732f3f99089"))
            .columns(Set.of(
                    Column.refByGuid("fcfb1f38-9b60-469e-ad66-87bd69cff7d8"),
                    Column.refByGuid("21d08a72-664d-4538-b74f-e3ee4ab4c3a6")))
            .tables(Set.of(
                    Table.refByGuid("b5a3b7d9-c79f-4e7c-a0bc-901367e29e91"),
                    Table.refByGuid("3c679f20-b4ae-4ad8-91d2-6646bc965eb8")))
            .views(Set.of(
                    View.refByGuid("aa7b546d-e6b4-4730-8523-0462819d0b30"),
                    View.refByGuid("2994dac5-8dbc-4fbf-a90e-761a32832368")))
            .build();
    private static AtlanQuery frodo;
    private static String serialized;

    @Test(groups = {"AtlanQuery.builderEquivalency"})
    void builderEquivalency() {
        assertEquals(full.toBuilder().build(), full);
    }

    @Test(
            groups = {"AtlanQuery.serialize"},
            dependsOnGroups = {"AtlanQuery.builderEquivalency"})
    void serialization() {
        assertNotNull(full);
        serialized = full.toJson();
        assertNotNull(serialized);
    }

    @Test(
            groups = {"AtlanQuery.deserialize"},
            dependsOnGroups = {"AtlanQuery.serialize"})
    void deserialization() throws JsonProcessingException {
        assertNotNull(serialized);
        frodo = Serde.mapper.readValue(serialized, AtlanQuery.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"AtlanQuery.equivalency"},
            dependsOnGroups = {"AtlanQuery.serialize", "AtlanQuery.deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson();
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
    }

    @Test(
            groups = {"AtlanQuery.equivalency"},
            dependsOnGroups = {"AtlanQuery.serialize", "AtlanQuery.deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
