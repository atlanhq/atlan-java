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
                    LineageProcess.refByGuid("f76bb2c6-2968-476f-b33d-fe0b4a75e980"),
                    LineageProcess.refByGuid("aaedd884-c653-49c7-8143-540a36ba66b6")))
            .outputFromProcesses(Set.of(
                    LineageProcess.refByGuid("2492a268-8761-49b8-ba53-7e208cff3dcd"),
                    LineageProcess.refByGuid("e8d27461-2f24-49e5-bc5a-f0fe0b5eeb32")))
            .queryCount(-3022545005103052920L)
            .queryUserCount(-5308765275880160862L)
            // .queryUserMap(Map.of("key1", 123456L, "key2", 654321L))
            .queryCountUpdatedAt(3464630707843017083L)
            .databaseName("databaseName")
            .databaseQualifiedName("databaseQualifiedName")
            .schemaName("schemaName")
            .schemaQualifiedName("schemaQualifiedName")
            .tableName("tableName")
            .tableQualifiedName("tableQualifiedName")
            .viewName("viewName")
            .viewQualifiedName("viewQualifiedName")
            .isProfiled(false)
            .lastProfiledAt(-2153163356971756433L)
            .dbtModels(Set.of(
                    DbtModel.refByGuid("a70efc0e-a17f-4b5d-9e35-88f10fff9fcd"),
                    DbtModel.refByGuid("2abeec3e-69c5-48ed-b067-187bab851285")))
            .dbtSources(Set.of(
                    DbtSource.refByGuid("172d726a-70ed-4074-a9b1-0f58c8c76709"),
                    DbtSource.refByGuid("0d9495b0-98aa-4656-990c-8abba34dfcc8")))
            .dataType("dataType")
            .subDataType("subDataType")
            .order(-1966902301)
            .isPartition(false)
            .partitionOrder(592110913)
            .isClustered(false)
            .isPrimary(true)
            .isForeign(true)
            .isIndexed(true)
            .isSort(true)
            .isDist(false)
            .isPinned(true)
            .pinnedBy("pinnedBy")
            .pinnedAt(3844993251426572894L)
            .precision(-1475323273)
            .defaultValue("defaultValue")
            .isNullable(false)
            .numericScale(0.11822494766105718)
            .maxLength(3539946568009515085L)
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
                    AtlanQuery.refByGuid("2c5b2b2d-018b-4c06-96d3-000d45414df6"),
                    AtlanQuery.refByGuid("6aae2b13-9670-4c52-8ace-1e0d1ca3bf3b")))
            .metricTimestamps(Set.of(
                    DbtMetric.refByGuid("4855f267-bc65-467e-b7a8-62cb54fa1a10"),
                    DbtMetric.refByGuid("d4683d16-b53a-44e7-86e3-3d26badcf4b7")))
            .dbtMetrics(Set.of(
                    DbtMetric.refByGuid("4d17bc7b-dad5-4c3e-8895-f5981d3ef81f"),
                    DbtMetric.refByGuid("e108cade-398d-4d49-a34e-baea44cbbe6d")))
            .view(View.refByGuid("3932639b-588b-406b-8b1f-0973fd93d8b4"))
            .tablePartition(TablePartition.refByGuid("93a45ab9-874f-4825-ab4c-d60b33c79f0f"))
            .dataQualityMetricDimensions(Set.of(
                    DbtMetric.refByGuid("987adbda-32c4-4f17-a773-89c8944bb702"),
                    DbtMetric.refByGuid("d7114c8c-a113-42d8-a6b3-792d44cf1e89")))
            .dbtModelColumns(Set.of(
                    DbtModelColumn.refByGuid("0542e16e-8ca0-48b8-bd33-d2f2b7380d4c"),
                    DbtModelColumn.refByGuid("d4c7db64-6f49-4f84-b895-e5f411f747ab")))
            .table(Table.refByGuid("544e0b61-9091-4645-bbe3-bec40303d9b3"))
            .build();
    private static Column frodo;
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
        frodo = Serde.mapper.readValue(serialized, Column.class);
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
