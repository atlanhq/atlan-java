/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import static org.testng.Assert.*;

import com.atlan.model.enums.*;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.*;
import org.testng.annotations.Test;

public class TablePartitionTest {

    private static final TablePartition full = TablePartition.builder()
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
                    LineageProcess.refByGuid("b2db57dc-3091-4eee-a5d2-dc9e9b03f010"),
                    LineageProcess.refByGuid("ece9e0d6-ec11-420f-850a-3c25fd8eac77")))
            .outputFromProcesses(Set.of(
                    LineageProcess.refByGuid("5badd0be-4414-4220-8cd0-ae52f895b7b0"),
                    LineageProcess.refByGuid("0718f0ac-9649-41cb-998e-1380344b8c12")))
            .queryCount(-3918158359311912933L)
            .queryUserCount(3907496560807504625L)
            // .queryUserMap(Map.of("key1", 123456L, "key2", 654321L))
            .queryCountUpdatedAt(-6311641284624351125L)
            .databaseName("databaseName")
            .databaseQualifiedName("databaseQualifiedName")
            .schemaName("schemaName")
            .schemaQualifiedName("schemaQualifiedName")
            .tableName("tableName")
            .tableQualifiedName("tableQualifiedName")
            .viewName("viewName")
            .viewQualifiedName("viewQualifiedName")
            .isProfiled(false)
            .lastProfiledAt(4380342658431213479L)
            .dbtModels(Set.of(
                    DbtModel.refByGuid("fbf1b354-2291-44d7-bdd1-b0aeabc4dc3e"),
                    DbtModel.refByGuid("8e15f461-2a96-4177-8725-1a64717232a3")))
            .dbtSources(Set.of(
                    DbtSource.refByGuid("ba2c8d70-381a-4678-9f4a-c73dd278094a"),
                    DbtSource.refByGuid("713ba2ed-c4dd-4f17-ae49-697bf80efafb")))
            .sqlDbtModels(Set.of(
                    DbtModel.refByGuid("2c72dce5-6320-4d2a-924b-be3f069201ad"),
                    DbtModel.refByGuid("8ac1163f-9a2a-491e-b599-d722911f396e")))
            .sqlDBTSources(Set.of(
                    DbtSource.refByGuid("f09f1585-65af-47d8-adf9-bd7e437754dd"),
                    DbtSource.refByGuid("c37e0e91-5c0f-4771-a445-7f7ac2cb7893")))
            .constraint("constraint")
            .columnCount(-9001792185068200817L)
            .rowCount(-1048303578309653054L)
            .sizeBytes(7138041396881024384L)
            .alias("alias")
            .isTemporary(true)
            .isQueryPreview(true)
            .queryPreviewConfig(Map.of("key1", "value1", "key2", "value2"))
            .externalLocation("externalLocation")
            .externalLocationRegion("externalLocationRegion")
            .externalLocationFormat("externalLocationFormat")
            .isPartitioned(true)
            .partitionStrategy("partitionStrategy")
            .partitionCount(-2014085021728451831L)
            .partitionList("partitionList")
            .columns(Set.of(
                    Column.refByGuid("9463ecf7-a87c-4a92-a4e6-008826c0d488"),
                    Column.refByGuid("995c1648-a84f-4226-a360-32705ddbe1b4")))
            .parentTable(Table.refByGuid("9fe29782-bd58-4b46-ab5e-d8fc4c169c49"))
            .build();
    private static TablePartition frodo;
    private static String serialized;

    @Test(groups = {"TablePartition.builderEquivalency"})
    void builderEquivalency() {
        assertEquals(full.toBuilder().build(), full);
    }

    @Test(
            groups = {"TablePartition.serialize"},
            dependsOnGroups = {"TablePartition.builderEquivalency"})
    void serialization() {
        assertNotNull(full);
        serialized = full.toJson();
        assertNotNull(serialized);
    }

    @Test(
            groups = {"TablePartition.deserialize"},
            dependsOnGroups = {"TablePartition.serialize"})
    void deserialization() throws JsonProcessingException {
        assertNotNull(serialized);
        frodo = Serde.mapper.readValue(serialized, TablePartition.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"TablePartition.equivalency"},
            dependsOnGroups = {"TablePartition.serialize", "TablePartition.deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson();
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
    }

    @Test(
            groups = {"TablePartition.equivalency"},
            dependsOnGroups = {"TablePartition.serialize", "TablePartition.deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
