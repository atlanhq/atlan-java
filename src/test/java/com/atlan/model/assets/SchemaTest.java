/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import static org.testng.Assert.*;

import com.atlan.model.enums.*;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.*;
import org.testng.annotations.Test;

public class SchemaTest {

    private static final Schema full = Schema.builder()
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
                    LineageProcess.refByGuid("354d17c4-0fa9-4275-ad98-a3edbc88834d"),
                    LineageProcess.refByGuid("e41fa6a4-30f9-417f-ba98-536cea1acda4")))
            .outputFromProcesses(Set.of(
                    LineageProcess.refByGuid("71d4299d-be82-47d1-b2dc-b7decae9c178"),
                    LineageProcess.refByGuid("c106b92b-8668-4526-8fab-2379d6f059b0")))
            .queryCount(-5323718724479795273L)
            .queryUserCount(5407834279798490699L)
            // .queryUserMap(Map.of("key1", 123456L, "key2", 654321L))
            .queryCountUpdatedAt(-4441334462722413579L)
            .databaseName("databaseName")
            .databaseQualifiedName("databaseQualifiedName")
            .schemaName("schemaName")
            .schemaQualifiedName("schemaQualifiedName")
            .tableName("tableName")
            .tableQualifiedName("tableQualifiedName")
            .viewName("viewName")
            .viewQualifiedName("viewQualifiedName")
            .isProfiled(true)
            .lastProfiledAt(-6087788523547470388L)
            .dbtModels(Set.of(
                    DbtModel.refByGuid("b084e656-c86b-46dc-b08e-ef5ef7dc756b"),
                    DbtModel.refByGuid("d9d3a616-08b4-4057-9a22-514b5bf4aafc")))
            .dbtSources(Set.of(
                    DbtSource.refByGuid("5e8608b0-8df3-4bcf-91f3-0cc25d1cc94b"),
                    DbtSource.refByGuid("292e133a-b74b-4e1d-af7b-90d0610b8bb5")))
            .sqlDbtModels(Set.of(
                    DbtModel.refByGuid("31d1fff9-16a0-42b8-87ad-dd371eba1eb6"),
                    DbtModel.refByGuid("c8dc81be-4b9e-43c1-8ad0-abfed48d7f52")))
            .sqlDBTSources(Set.of(
                    DbtSource.refByGuid("ae33915c-6302-4b45-a737-d71630048d0b"),
                    DbtSource.refByGuid("286a66a5-b855-464c-b252-d7462a35183e")))
            .tableCount(-1415459775)
            .viewCount(-999102117)
            .materializedViews(Set.of(
                    MaterializedView.refByGuid("1ddef7dd-d75b-45bf-af28-9cca8e661148"),
                    MaterializedView.refByGuid("4ce31441-25d5-48ec-82f6-e32cbd70156c")))
            .tables(Set.of(
                    Table.refByGuid("a4c69475-848f-4ee1-b6ca-bf8ca810d280"),
                    Table.refByGuid("b4bf7f05-a710-4174-9474-487734cac8a1")))
            .database(Database.refByGuid("61b6ee55-20b5-44fb-970d-7be65366e658"))
            .snowflakePipes(Set.of(
                    SnowflakePipe.refByGuid("90ccae23-2a6f-4d93-82e4-2d99d0b011b2"),
                    SnowflakePipe.refByGuid("e73037c0-5ed2-4e0a-93ea-67db8b8435fe")))
            .snowflakeStreams(Set.of(
                    SnowflakeStream.refByGuid("efaee0e1-c1cf-4a22-bba2-8047737d4c5d"),
                    SnowflakeStream.refByGuid("7dc9e730-1552-4830-ba2a-daa2044f8cdc")))
            .procedures(Set.of(
                    Procedure.refByGuid("2db45640-96c9-4476-baeb-77ec3b2f2268"),
                    Procedure.refByGuid("3a11ab0b-4c9d-4c41-ab7d-a37272945ab8")))
            .views(Set.of(
                    View.refByGuid("b2aee7eb-dc80-4499-b91e-c1d99de2a1d8"),
                    View.refByGuid("063a9f90-1262-4cb2-b729-917df8856514")))
            .build();
    private static Schema frodo;
    private static String serialized;

    @Test(groups = {"Schema.builderEquivalency"})
    void builderEquivalency() {
        assertEquals(full.toBuilder().build(), full);
    }

    @Test(
            groups = {"Schema.serialize"},
            dependsOnGroups = {"Schema.builderEquivalency"})
    void serialization() {
        assertNotNull(full);
        serialized = full.toJson();
        assertNotNull(serialized);
    }

    @Test(
            groups = {"Schema.deserialize"},
            dependsOnGroups = {"Schema.serialize"})
    void deserialization() throws JsonProcessingException {
        assertNotNull(serialized);
        frodo = Serde.mapper.readValue(serialized, Schema.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"Schema.equivalency"},
            dependsOnGroups = {"Schema.serialize", "Schema.deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson();
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
    }

    @Test(
            groups = {"Schema.equivalency"},
            dependsOnGroups = {"Schema.serialize", "Schema.deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
