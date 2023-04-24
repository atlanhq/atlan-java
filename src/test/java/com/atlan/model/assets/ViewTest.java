/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import static org.testng.Assert.*;

import com.atlan.model.enums.*;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.*;
import org.testng.annotations.Test;

public class ViewTest {

    private static final View full = View.builder()
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
                    LineageProcess.refByGuid("80339b0a-f6c7-4161-8765-7932c00eeac9"),
                    LineageProcess.refByGuid("b5c929ff-61e4-4ae3-83d5-cfb788228d26")))
            .outputFromProcesses(Set.of(
                    LineageProcess.refByGuid("2828bb75-6e1e-453c-bfab-50b22de1f39e"),
                    LineageProcess.refByGuid("a953d393-6919-4f6f-afbe-39e42042a646")))
            .queryCount(-1641703763835198047L)
            .queryUserCount(4815776398797624151L)
            // .queryUserMap(Map.of("key1", 123456L, "key2", 654321L))
            .queryCountUpdatedAt(-6404118960467428466L)
            .databaseName("databaseName")
            .databaseQualifiedName("databaseQualifiedName")
            .schemaName("schemaName")
            .schemaQualifiedName("schemaQualifiedName")
            .tableName("tableName")
            .tableQualifiedName("tableQualifiedName")
            .viewName("viewName")
            .viewQualifiedName("viewQualifiedName")
            .isProfiled(false)
            .lastProfiledAt(7939414877924706611L)
            .dbtModels(Set.of(
                    DbtModel.refByGuid("d7aa6d97-4a86-45f9-9128-a83d708a108a"),
                    DbtModel.refByGuid("ea29cc7d-996b-4477-a862-3e69159f2bda")))
            .dbtSources(Set.of(
                    DbtSource.refByGuid("1bdd53ef-294a-4376-872e-a0894c7e9966"),
                    DbtSource.refByGuid("ea870e0e-83e6-4283-8dd2-417844ae204c")))
            .sqlDbtModels(Set.of(
                    DbtModel.refByGuid("9252152b-1be5-45e5-b7e4-4f0ad6770402"),
                    DbtModel.refByGuid("3f9e00a2-5e6c-45f6-be04-4e6c886d3b9d")))
            .sqlDBTSources(Set.of(
                    DbtSource.refByGuid("f8c43782-2261-4b9c-b1db-bc26986ff512"),
                    DbtSource.refByGuid("f77e9430-89ef-4644-abd1-4cc2243c1367")))
            .columnCount(5087786187880220986L)
            .rowCount(-8086441261525702018L)
            .sizeBytes(-5995543225876134370L)
            .isQueryPreview(false)
            .queryPreviewConfig(Map.of("key1", "value1", "key2", "value2"))
            .alias("alias")
            .isTemporary(true)
            .definition("definition")
            .columns(Set.of(
                    Column.refByGuid("c5fb756a-1e34-453c-8e76-1aeaf5daedaa"),
                    Column.refByGuid("20385053-c557-44b4-9ab0-5d7237e56ddc")))
            .queries(Set.of(
                    AtlanQuery.refByGuid("52b53dab-d8f5-49c3-ad0e-54e0c58ef3b5"),
                    AtlanQuery.refByGuid("382a6726-59eb-45ce-8718-bc6d86543665")))
            .schema(Schema.refByGuid("7ae15d57-e516-4f37-aff2-8619ea22c43b"))
            .build();
    private static View frodo;
    private static String serialized;

    @Test(groups = {"View.builderEquivalency"})
    void builderEquivalency() {
        assertEquals(full.toBuilder().build(), full);
    }

    @Test(
            groups = {"View.serialize"},
            dependsOnGroups = {"View.builderEquivalency"})
    void serialization() {
        assertNotNull(full);
        serialized = full.toJson();
        assertNotNull(serialized);
    }

    @Test(
            groups = {"View.deserialize"},
            dependsOnGroups = {"View.serialize"})
    void deserialization() throws JsonProcessingException {
        assertNotNull(serialized);
        frodo = Serde.mapper.readValue(serialized, View.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"View.equivalency"},
            dependsOnGroups = {"View.serialize", "View.deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson();
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
    }

    @Test(
            groups = {"View.equivalency"},
            dependsOnGroups = {"View.serialize", "View.deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
