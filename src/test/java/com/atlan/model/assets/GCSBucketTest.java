/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import static org.testng.Assert.*;

import com.atlan.model.enums.*;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.*;
import org.testng.annotations.Test;

public class GCSBucketTest {

    private static final GCSBucket full = GCSBucket.builder()
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
                    LineageProcess.refByGuid("ccd54212-c0b4-48bd-b0b5-abb312348f30"),
                    LineageProcess.refByGuid("ef097a62-8357-4c82-8ae1-348242a0df60")))
            .outputFromProcesses(Set.of(
                    LineageProcess.refByGuid("53eaad0c-1933-45f1-894c-bd298de988b9"),
                    LineageProcess.refByGuid("9dfc3016-9562-42ca-b803-ee1a507ed75c")))
            .googleService("googleService")
            .googleProjectName("googleProjectName")
            .googleProjectId("googleProjectId")
            .googleProjectNumber(-3506689685592332841L)
            .googleLocation("googleLocation")
            .googleLocationType("googleLocationType")
            .googleLabel(GoogleLabel.of("key1", "value1"))
            .googleLabel(GoogleLabel.of("key2", "value2"))
            .googleTag(GoogleTag.of("key1", "value1"))
            .googleTag(GoogleTag.of("key2", "value2"))
            .gcsStorageClass("gcsStorageClass")
            .gcsEncryptionType("gcsEncryptionType")
            .gcsETag("gcsETag")
            .gcsRequesterPays(false)
            .gcsAccessControl("gcsAccessControl")
            .gcsMetaGenerationId(-7025758879641529795L)
            .gcsObjectCount(1968059271833439451L)
            .gcsBucketVersioningEnabled(true)
            .gcsBucketRetentionLocked(false)
            .gcsBucketRetentionPeriod(-3257670569505228872L)
            .gcsBucketRetentionEffectiveTime(1854036854448567033L)
            .gcsBucketLifecycleRules("gcsBucketLifecycleRules")
            .gcsBucketRetentionPolicy("gcsBucketRetentionPolicy")
            .gcsObjects(Set.of(
                    GCSObject.refByGuid("bfdb5033-e686-49fc-b498-c3a0ff8bc78d"),
                    GCSObject.refByGuid("1904b2ea-75b1-45d0-ad8f-73f8fc6cf9fc")))
            .build();
    private static GCSBucket frodo;
    private static String serialized;

    @Test(groups = {"GCSBucket.builderEquivalency"})
    void builderEquivalency() {
        assertEquals(full.toBuilder().build(), full);
    }

    @Test(
            groups = {"GCSBucket.serialize"},
            dependsOnGroups = {"GCSBucket.builderEquivalency"})
    void serialization() {
        assertNotNull(full);
        serialized = full.toJson();
        assertNotNull(serialized);
    }

    @Test(
            groups = {"GCSBucket.deserialize"},
            dependsOnGroups = {"GCSBucket.serialize"})
    void deserialization() throws JsonProcessingException {
        assertNotNull(serialized);
        frodo = Serde.mapper.readValue(serialized, GCSBucket.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"GCSBucket.equivalency"},
            dependsOnGroups = {"GCSBucket.serialize", "GCSBucket.deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson();
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
    }

    @Test(
            groups = {"GCSBucket.equivalency"},
            dependsOnGroups = {"GCSBucket.serialize", "GCSBucket.deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
