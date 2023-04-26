/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import static org.testng.Assert.*;

import com.atlan.model.enums.*;
import com.atlan.model.structs.GoogleLabel;
import com.atlan.model.structs.GoogleTag;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.*;
import org.testng.annotations.Test;

public class GCSObjectTest {

    private static final GCSObject full = GCSObject.builder()
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
                    LineageProcess.refByGuid("7caee1f1-e9ee-4996-9b76-0474f7873ba2"),
                    LineageProcess.refByGuid("18654745-2bbe-4d79-8529-32841ff6d859")))
            .outputFromProcesses(Set.of(
                    LineageProcess.refByGuid("b53b430b-d8c4-4db2-8888-94d20c5120d3"),
                    LineageProcess.refByGuid("700d11f7-6f60-49e1-b9a6-259d455cf0d9")))
            .googleService("googleService")
            .googleProjectName("googleProjectName")
            .googleProjectId("googleProjectId")
            .googleProjectNumber(-5655569751307817193L)
            .googleLocation("googleLocation")
            .googleLocationType("googleLocationType")
            .googleLabel(GoogleLabel.of("key1", "value1"))
            .googleLabel(GoogleLabel.of("key2", "value2"))
            .googleTag(GoogleTag.of("key1", "value1"))
            .googleTag(GoogleTag.of("key2", "value2"))
            .gcsStorageClass("gcsStorageClass")
            .gcsEncryptionType("gcsEncryptionType")
            .gcsETag("gcsETag")
            .gcsRequesterPays(true)
            .gcsAccessControl("gcsAccessControl")
            .gcsMetaGenerationId(7301106230656543084L)
            .gcsBucketName("gcsBucketName")
            .gcsBucketQualifiedName("gcsBucketQualifiedName")
            .gcsObjectSize(-3860147200356214492L)
            .gcsObjectKey("gcsObjectKey")
            .gcsObjectMediaLink("gcsObjectMediaLink")
            .gcsObjectHoldType("gcsObjectHoldType")
            .gcsObjectGenerationId(1694763665417568460L)
            .gcsObjectCRC32CHash("gcsObjectCRC32CHash")
            .gcsObjectMD5Hash("gcsObjectMD5Hash")
            .gcsObjectDataLastModifiedTime(713780278613201016L)
            .gcsObjectContentType("gcsObjectContentType")
            .gcsObjectContentEncoding("gcsObjectContentEncoding")
            .gcsObjectContentDisposition("gcsObjectContentDisposition")
            .gcsObjectContentLanguage("gcsObjectContentLanguage")
            .gcsObjectRetentionExpirationDate(3613704574085639456L)
            .gcsBucket(GCSBucket.refByGuid("54ec88ca-5dc4-4d38-9bcc-6adf2dcf18b6"))
            .build();
    private static GCSObject frodo;
    private static String serialized;

    @Test(groups = {"GCSObject.builderEquivalency"})
    void builderEquivalency() {
        assertEquals(full.toBuilder().build(), full);
    }

    @Test(
            groups = {"GCSObject.serialize"},
            dependsOnGroups = {"GCSObject.builderEquivalency"})
    void serialization() {
        assertNotNull(full);
        serialized = full.toJson();
        assertNotNull(serialized);
    }

    @Test(
            groups = {"GCSObject.deserialize"},
            dependsOnGroups = {"GCSObject.serialize"})
    void deserialization() throws JsonProcessingException {
        assertNotNull(serialized);
        frodo = Serde.mapper.readValue(serialized, GCSObject.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"GCSObject.equivalency"},
            dependsOnGroups = {"GCSObject.serialize", "GCSObject.deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson();
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
    }

    @Test(
            groups = {"GCSObject.equivalency"},
            dependsOnGroups = {"GCSObject.serialize", "GCSObject.deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
