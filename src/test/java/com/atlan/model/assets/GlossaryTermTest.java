/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import static org.testng.Assert.*;

import com.atlan.model.enums.*;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.*;
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
            .shortDescription("shortDescription")
            .longDescription("longDescription")
            .examples(Set.of("one", "two", "three"))
            .abbreviation("abbreviation")
            .usage("usage")
            .additionalAttributes(Map.of("key1", "value1", "key2", "value2"))
            .translationTerms(Set.of(
                    GlossaryTerm.refByGuid("244c0fa0-7009-4638-bedc-62fedfa61c84"),
                    GlossaryTerm.refByGuid("dfcfbbf9-39f7-46e7-af16-55699545811c")))
            .validValuesFor(Set.of(
                    GlossaryTerm.refByGuid("849bedaa-316f-4863-a4bc-68a288b1af64"),
                    GlossaryTerm.refByGuid("fb9450df-c84f-40a8-a39d-742a5ff62a3c")))
            .synonyms(Set.of(
                    GlossaryTerm.refByGuid("32c04fb4-17ee-4bef-9649-f7fab1590298"),
                    GlossaryTerm.refByGuid("6ed811b3-6e85-48c9-8af2-3180a6e67284")))
            .replacedBy(Set.of(
                    GlossaryTerm.refByGuid("76998367-863b-4298-9594-72197f88fa47"),
                    GlossaryTerm.refByGuid("ddd5802d-54a4-4314-95d2-92bc2a2c799b")))
            .validValues(Set.of(
                    GlossaryTerm.refByGuid("e6167b82-7fb6-44e4-8dc5-f154272a85c9"),
                    GlossaryTerm.refByGuid("0f05286f-0794-46a7-9368-9256110040f1")))
            .replacementTerms(Set.of(
                    GlossaryTerm.refByGuid("f0d8259c-1187-432a-bfdb-772150b9be1a"),
                    GlossaryTerm.refByGuid("93578971-8840-473c-87d6-f27e3d75943a")))
            .seeAlso(Set.of(
                    GlossaryTerm.refByGuid("93215dfb-c95e-408b-b228-646f68bb1f3c"),
                    GlossaryTerm.refByGuid("c982f232-9171-432e-8275-5a0509d92017")))
            .translatedTerms(Set.of(
                    GlossaryTerm.refByGuid("7abb83ce-7d5e-4232-ad78-0f6d6aff42b9"),
                    GlossaryTerm.refByGuid("f0398314-cb48-43ec-8913-9dd3d563ce4f")))
            .isA(Set.of(
                    GlossaryTerm.refByGuid("e8be26c7-6cbb-464a-9f58-771f21cbe5f9"),
                    GlossaryTerm.refByGuid("3545b150-0fa3-4a8a-8891-1844551e2711")))
            .anchor(Glossary.refByGuid("29266596-2bc1-4d60-993d-13b276d74b8e"))
            .antonyms(Set.of(
                    GlossaryTerm.refByGuid("40263463-3ec2-45b3-bce0-9b28389aae39"),
                    GlossaryTerm.refByGuid("956939a4-df89-469e-904b-68fea874b556")))
            .assignedEntities(Set.of(
                    Table.refByGuid("20a1abb0-0788-4de3-95a2-cf6e9967de8e"),
                    Table.refByGuid("1d1dbe78-1b56-4d99-9e3c-5d4de2b41557")))
            .categories(Set.of(
                    GlossaryCategory.refByGuid("dbf4c7ab-4cf3-4990-9978-24ae3e041bb6"),
                    GlossaryCategory.refByGuid("64340c31-56f3-4ed1-93f1-f4ccdc245bce")))
            .classifies(Set.of(
                    GlossaryTerm.refByGuid("bf87fc8d-22f7-4da5-9735-2760ee70eb93"),
                    GlossaryTerm.refByGuid("067a1c0b-35ee-4612-89ae-08f4cdf6d9ed")))
            .preferredToTerms(Set.of(
                    GlossaryTerm.refByGuid("7a86cb36-50c9-4482-9560-2653c99e349d"),
                    GlossaryTerm.refByGuid("5b8c47a3-aeb6-4a3d-a992-0c12adff19fd")))
            .preferredTerms(Set.of(
                    GlossaryTerm.refByGuid("bb67b17d-d914-49ae-a903-d1dfc51b24f0"),
                    GlossaryTerm.refByGuid("c2a3c5a7-5268-483f-9d60-2ae719de977f")))
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
