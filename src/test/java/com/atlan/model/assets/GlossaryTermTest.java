/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import static org.testng.Assert.*;

import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.testng.annotations.Test;

public class GlossaryTermTest {

    private static final GlossaryTerm full = GlossaryTerm.builder()
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
            .viewerUser("viewerUser")
            .viewerGroup("viewerGroup")
            .isDiscoverable(true)
            .isEditable(true)
            .viewScore(123456.0)
            .popularityScore(123456.0)
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
            .meaning(GlossaryTerm.refByGuid("termGuid1"))
            .meaning(GlossaryTerm.refByGuid("termGuid2"))
            .anchor(Glossary.refByGuid("glossaryGuid"))
            .assignedEntity(Column.refByGuid("assetGuid1"))
            .assignedEntity(Table.refByQualifiedName("assetGuid2"))
            .category(GlossaryCategory.refByGuid("categoryGuid1"))
            .category(GlossaryCategory.refByGuid("categoryGuid2"))
            .addToSeeAlso(GlossaryTerm.refByGuid("termGuid1"))
            .addToSeeAlso(GlossaryTerm.refByGuid("termGuid2"))
            .synonym(GlossaryTerm.refByQualifiedName("termGuid1"))
            .synonym(GlossaryTerm.refByQualifiedName("termGuid2"))
            .antonym(GlossaryTerm.refByGuid("termGuid1"))
            .antonym(GlossaryTerm.refByQualifiedName("termGuid2"))
            .preferredTerm(GlossaryTerm.refByQualifiedName("termGuid1"))
            .preferredTerm(GlossaryTerm.refByGuid("termGuid2"))
            .addToReplacedBy(GlossaryTerm.refByGuid("termGuid1"))
            .addToReplacedBy(GlossaryTerm.refByQualifiedName("termGuid2"))
            .translatedTerm(GlossaryTerm.refByQualifiedName("termGuid1"))
            .translatedTerm(GlossaryTerm.refByGuid("termGuid2"))
            .addToValidValuesFor(GlossaryTerm.refByGuid("termGuid1"))
            .addToValidValuesFor(GlossaryTerm.refByQualifiedName("termGuid2"))
            .build();

    private static GlossaryTerm frodo;
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
        frodo = Serde.mapper.readValue(serialized, GlossaryTerm.class);
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
