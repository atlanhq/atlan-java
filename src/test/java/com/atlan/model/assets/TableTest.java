/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import static org.testng.Assert.*;

import com.atlan.model.enums.*;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.*;
import org.testng.annotations.Test;

public class TableTest {

    private static final Table full = Table.builder()
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
                    LineageProcess.refByGuid("333e79a7-a822-4123-8c8c-1fb7ef6403c3"),
                    LineageProcess.refByGuid("2ff25ff8-51ac-4703-b8ca-68231feadb38")))
            .outputFromProcesses(Set.of(
                    LineageProcess.refByGuid("d0c8ccfb-065c-4d48-8a7f-acec947e4ce3"),
                    LineageProcess.refByGuid("ced06ae5-1ead-4abe-98be-281eeab8cad4")))
            .queryCount(-1666680909191730079L)
            .queryUserCount(6356379793764929150L)
            // .queryUserMap(Map.of("key1", 123456L, "key2", 654321L))
            .queryCountUpdatedAt(501458149465324825L)
            .databaseName("databaseName")
            .databaseQualifiedName("databaseQualifiedName")
            .schemaName("schemaName")
            .schemaQualifiedName("schemaQualifiedName")
            .tableName("tableName")
            .tableQualifiedName("tableQualifiedName")
            .viewName("viewName")
            .viewQualifiedName("viewQualifiedName")
            .isProfiled(false)
            .lastProfiledAt(-7630635268113772927L)
            .dbtModels(Set.of(
                    DbtModel.refByGuid("61425cf2-c8f7-4535-92d6-b21104622ce5"),
                    DbtModel.refByGuid("8ebbaebf-77be-4da4-9b57-49266a8de67f")))
            .dbtSources(Set.of(
                    DbtSource.refByGuid("30926af5-0e42-4fba-b388-8ebd629088c5"),
                    DbtSource.refByGuid("f64e9a69-d8af-45fa-967d-c383842d8263")))
            .sqlDbtModels(Set.of(
                    DbtModel.refByGuid("b989a2ab-598b-4634-85ac-42c42e5158c5"),
                    DbtModel.refByGuid("aca6cf21-25d2-4398-b065-35c0086b5dd5")))
            .sqlDBTSources(Set.of(
                    DbtSource.refByGuid("d8dd4080-3eaf-4b79-ae3a-e131cc0af79b"),
                    DbtSource.refByGuid("2a73ff06-a188-4ae6-bad4-901addb2d0a1")))
            .columnCount(3634627257025430943L)
            .rowCount(5983706554036988052L)
            .sizeBytes(8207880686149652699L)
            .alias("alias")
            .isTemporary(false)
            .isQueryPreview(false)
            .queryPreviewConfig(Map.of("key1", "value1", "key2", "value2"))
            .externalLocation("externalLocation")
            .externalLocationRegion("externalLocationRegion")
            .externalLocationFormat("externalLocationFormat")
            .isPartitioned(false)
            .partitionStrategy("partitionStrategy")
            .partitionCount(-6842808470179792012L)
            .partitionList("partitionList")
            .partitions(Set.of(
                    TablePartition.refByGuid("62e7cdca-9ea8-434c-af0c-6cf005272319"),
                    TablePartition.refByGuid("02afb9fc-38ef-4f69-b898-969ff5177798")))
            .columns(Set.of(
                    Column.refByGuid("e2790fc5-5b4b-4310-b652-f4f685b20a2d"),
                    Column.refByGuid("c494e69b-e909-40c6-9297-6b3339fd4420")))
            .queries(Set.of(
                    AtlanQuery.refByGuid("31ec001c-a081-4ee5-8be2-b7067e46e922"),
                    AtlanQuery.refByGuid("e4265d70-b64e-476b-a95c-f0ca36dee676")))
            .schema(Schema.refByGuid("f7bbe6f1-775d-46d9-bd20-7294637f8d23"))
            .build();
    private static Table frodo;
    private static String serialized;

    @Test(groups = {"Table.builderEquivalency"})
    void builderEquivalency() {
        assertEquals(full.toBuilder().build(), full);
    }

    @Test(
            groups = {"Table.serialize"},
            dependsOnGroups = {"Table.builderEquivalency"})
    void serialization() {
        assertNotNull(full);
        serialized = full.toJson();
        assertNotNull(serialized);
    }

    @Test(
            groups = {"Table.deserialize"},
            dependsOnGroups = {"Table.serialize"})
    void deserialization() throws JsonProcessingException {
        assertNotNull(serialized);
        frodo = Serde.mapper.readValue(serialized, Table.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"Table.equivalency"},
            dependsOnGroups = {"Table.serialize", "Table.deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson();
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
    }

    @Test(
            groups = {"Table.equivalency"},
            dependsOnGroups = {"Table.serialize", "Table.deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
