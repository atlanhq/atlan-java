/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import static org.testng.Assert.*;

import com.atlan.model.enums.*;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.*;
import org.testng.annotations.Test;

public class ColumnTest {

    private static final Column full = Column.builder()
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
                    LineageProcess.refByGuid("dedbd185-c593-417d-b9a3-ad301d9ae28b"),
                    LineageProcess.refByGuid("705d96f4-bdb6-4792-8dfe-8dc4ca3d2c23")))
            .outputFromProcesses(Set.of(
                    LineageProcess.refByGuid("3653bfe0-98cf-45e2-8b08-1b9c0cd71779"),
                    LineageProcess.refByGuid("a8095e02-bccc-49b1-a1c7-6339a47aca8c")))
            .queryCount(-479597481647961611L)
            .queryUserCount(-2988747526011003636L)
            // .queryUserMap(Map.of("key1", 123456L, "key2", 654321L))
            .queryCountUpdatedAt(-5039324225773727914L)
            .databaseName("databaseName")
            .databaseQualifiedName("databaseQualifiedName")
            .schemaName("schemaName")
            .schemaQualifiedName("schemaQualifiedName")
            .tableName("tableName")
            .tableQualifiedName("tableQualifiedName")
            .viewName("viewName")
            .viewQualifiedName("viewQualifiedName")
            .isProfiled(false)
            .lastProfiledAt(-5771720575992588146L)
            .dbtModels(Set.of(
                    DbtModel.refByGuid("0926ac88-035a-49dd-bd21-3811d8ea5632"),
                    DbtModel.refByGuid("8de12dac-d107-40cb-a016-96180f589f35")))
            .dbtSources(Set.of(
                    DbtSource.refByGuid("5fcd1a23-82e1-4806-898b-6b06d37def32"),
                    DbtSource.refByGuid("c9ae8415-f617-4ea4-8367-5cd3248d1437")))
            .sqlDbtModels(Set.of(
                    DbtModel.refByGuid("6a3558b5-8c99-4efd-b6b6-fdff85c03cc8"),
                    DbtModel.refByGuid("965ef52e-949e-480c-b33a-604c279a6713")))
            .sqlDBTSources(Set.of(
                    DbtSource.refByGuid("9397638e-d8b2-4c02-b29f-dd9df1fa3cf1"),
                    DbtSource.refByGuid("16bfc6bd-dbe2-4244-9c02-4743c2c5a0d1")))
            .dataType("dataType")
            .subDataType("subDataType")
            .order(-1173797698)
            .isPartition(true)
            .partitionOrder(1872689210)
            .isClustered(false)
            .isPrimary(true)
            .isForeign(true)
            .isIndexed(true)
            .isSort(false)
            .isDist(true)
            .isPinned(false)
            .pinnedBy("pinnedBy")
            .pinnedAt(8503031643014693118L)
            .precision(1539352978)
            .defaultValue("defaultValue")
            .isNullable(false)
            .numericScale(0.582608550386072)
            .maxLength(-5646133551701489137L)
            .validations(Map.of("key1", "value1", "key2", "value2"))
            .columnDistinctValuesCount(-24042661)
            .columnDistinctValuesCountLong(2669818931470942023L)
            .columnMax(0.01975962720296809)
            .columnMin(0.7486168150999852)
            .columnMean(0.4515604923196431)
            .columnSum(0.11733529178404278)
            .columnMedian(0.5961294189316886)
            .columnStandardDeviation(0.7969916970650953)
            .columnUniqueValuesCount(-1905861526)
            .columnUniqueValuesCountLong(-5040224698173352841L)
            .columnAverage(0.9515575477808735)
            .columnAverageLength(0.991220369187816)
            .columnDuplicateValuesCount(1837877026)
            .columnDuplicateValuesCountLong(4242826882034749086L)
            .columnMaximumStringLength(94891903)
            .columnMaxs(new TreeSet<>(Set.of("one", "two", "three")))
            .columnMinimumStringLength(799057885)
            .columnMins(new TreeSet<>(Set.of("one", "two", "three")))
            .columnMissingValuesCount(-153429342)
            .columnMissingValuesCountLong(-1328122288840817853L)
            .columnMissingValuesPercentage(0.10901799354522435)
            .columnUniquenessPercentage(0.9283062847428295)
            .columnVariance(0.02620209349747904)
            .materializedView(MaterializedView.refByGuid("057c3961-3a26-4151-8606-f795a0a33908"))
            .queries(Set.of(
                    AtlanQuery.refByGuid("f8d94b51-3a06-476c-bdc8-c5bde05ed20b"),
                    AtlanQuery.refByGuid("12e19c7a-1780-4aff-b67f-04a8b627f995")))
            .metricTimestamps(Set.of(
                    DbtMetric.refByGuid("42225557-294d-43bb-8135-933b01cf5c5e"),
                    DbtMetric.refByGuid("4df80de8-d6d5-43ae-aaee-bac97c0be675")))
            .dbtMetrics(Set.of(
                    DbtMetric.refByGuid("71e01620-24e9-4af9-9e41-4d0b51f7222c"),
                    DbtMetric.refByGuid("523a5888-a4bc-4fe7-ac0e-0e1b6201a0c3")))
            .view(View.refByGuid("fe8633a7-b0c6-445e-82b4-ae3c09cd3c09"))
            .tablePartition(TablePartition.refByGuid("2d907b57-beff-48c9-b5d1-ec56e04a69a0"))
            .dataQualityMetricDimensions(Set.of(
                    DbtMetric.refByGuid("4862d999-351f-4093-9912-d320f283cf98"),
                    DbtMetric.refByGuid("dfc55a21-8473-4963-ad2b-f38ca6121680")))
            .dbtModelColumns(Set.of(
                    DbtModelColumn.refByGuid("a9b8e691-40f4-4353-bac2-49bd59863660"),
                    DbtModelColumn.refByGuid("26907d82-e9c8-493b-aeac-8386b4846d41")))
            .table(Table.refByGuid("23ea4240-1918-4135-9126-0810c52e3a38"))
            .columnDbtModelColumns(Set.of(
                    DbtModelColumn.refByGuid("c5c2540b-768f-4da3-81b7-a26fa3e6688d"),
                    DbtModelColumn.refByGuid("bbbc9b84-3b50-4f1a-aebb-3b10d1aaa88e")))
            .build();
    private static Column frodo;
    private static String serialized;

    @Test(groups = {"Column.builderEquivalency"})
    void builderEquivalency() {
        assertEquals(full.toBuilder().build(), full);
    }

    @Test(
            groups = {"Column.serialize"},
            dependsOnGroups = {"Column.builderEquivalency"})
    void serialization() {
        assertNotNull(full);
        serialized = full.toJson();
        assertNotNull(serialized);
    }

    @Test(
            groups = {"Column.deserialize"},
            dependsOnGroups = {"Column.serialize"})
    void deserialization() throws JsonProcessingException {
        assertNotNull(serialized);
        frodo = Serde.mapper.readValue(serialized, Column.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"Column.equivalency"},
            dependsOnGroups = {"Column.serialize", "Column.deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson();
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
    }

    @Test(
            groups = {"Column.equivalency"},
            dependsOnGroups = {"Column.serialize", "Column.deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
