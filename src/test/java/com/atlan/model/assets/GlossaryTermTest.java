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
                    GlossaryTerm.refByGuid("768dabdb-d86d-4467-a662-00a26511178e"),
                    GlossaryTerm.refByGuid("d1ef4aba-f6a4-4a1f-825b-9f4447304ac4")))
            .validValuesFor(Set.of(
                    GlossaryTerm.refByGuid("d3c46a26-c330-42be-b332-cbff51bc289d"),
                    GlossaryTerm.refByGuid("170cfd83-670c-456e-8630-23bb41d9115a")))
            .synonyms(Set.of(
                    GlossaryTerm.refByGuid("d6a986fe-5387-4d54-9404-e6157f7827d4"),
                    GlossaryTerm.refByGuid("62ad288e-9275-418d-ab68-a85cd820b5af")))
            .replacedBy(Set.of(
                    GlossaryTerm.refByGuid("3401c927-b26a-4573-9c0f-d54797695fa2"),
                    GlossaryTerm.refByGuid("581c775c-8844-4653-9dbe-3ce675e376b5")))
            .validValues(Set.of(
                    GlossaryTerm.refByGuid("04c9246a-e2b9-41c9-95d2-a730c82fe7ef"),
                    GlossaryTerm.refByGuid("b967f3bf-4676-4354-8e11-3aed131a2456")))
            .replacementTerms(Set.of(
                    GlossaryTerm.refByGuid("0502e3cf-6881-43ec-ad29-6ad70328e338"),
                    GlossaryTerm.refByGuid("a641b56b-9b8b-4a93-a038-73541c82c9f4")))
            .seeAlso(Set.of(
                    GlossaryTerm.refByGuid("67d9da6b-9384-4b48-8601-5649c50b188e"),
                    GlossaryTerm.refByGuid("2d7813f8-ee37-4e74-956d-26b3542989bb")))
            .translatedTerms(Set.of(
                    GlossaryTerm.refByGuid("c122fc34-f8ee-42f8-9029-d665515fac37"),
                    GlossaryTerm.refByGuid("5d919e83-65ab-4421-8951-1b179e69f5ab")))
            .isA(Set.of(
                    GlossaryTerm.refByGuid("d0a97cd0-599b-40e8-b624-93a37d8718a8"),
                    GlossaryTerm.refByGuid("49da6099-60c8-4106-9036-7769de31cf33")))
            .anchor(Glossary.refByGuid("b9d692be-4099-43b4-a53b-66802c41678c"))
            .antonyms(Set.of(
                    GlossaryTerm.refByGuid("eceee315-4c37-49a5-ab8f-dd8dccdf5037"),
                    GlossaryTerm.refByGuid("f6583af2-50d3-40b8-810f-794a06af6de5")))
            .assignedEntities(Set.of(
                    Table.refByGuid("e4cc28c4-0dd5-4af1-b3c2-77c472a44834"),
                    Table.refByGuid("fbde6d82-2383-4147-a59f-7b9af839fb16")))
            .categories(Set.of(
                    GlossaryCategory.refByGuid("3fa25ad7-b35e-4003-9935-1296409a3cf4"),
                    GlossaryCategory.refByGuid("f1b0ec47-d915-4aa6-986c-3ead0a3546aa")))
            .classifies(Set.of(
                    GlossaryTerm.refByGuid("f70d07a1-b6a9-4b02-b050-29a638e581a9"),
                    GlossaryTerm.refByGuid("ba4cdbeb-ac85-43a3-a836-70b1055d8cf5")))
            .preferredToTerms(Set.of(
                    GlossaryTerm.refByGuid("7c9cec94-704e-490e-a484-deff8645aa5d"),
                    GlossaryTerm.refByGuid("24ced105-53d8-45a4-a1ff-c99b400cf8f1")))
            .preferredTerms(Set.of(
                    GlossaryTerm.refByGuid("4f0c30b2-a57e-438f-b558-714d58064e0b"),
                    GlossaryTerm.refByGuid("63099314-f6b7-4e8b-81a7-0700b8188659")))
            .build();
    private static GlossaryTerm frodo;
    private static String serialized;

    @Test(groups = {"GlossaryTerm.builderEquivalency"})
    void builderEquivalency() {
        assertEquals(full.toBuilder().build(), full);
    }

    @Test(
            groups = {"GlossaryTerm.serialize"},
            dependsOnGroups = {"GlossaryTerm.builderEquivalency"})
    void serialization() {
        assertNotNull(full);
        serialized = full.toJson();
        assertNotNull(serialized);
    }

    @Test(
            groups = {"GlossaryTerm.deserialize"},
            dependsOnGroups = {"GlossaryTerm.serialize"})
    void deserialization() throws JsonProcessingException {
        assertNotNull(serialized);
        frodo = Serde.mapper.readValue(serialized, GlossaryTerm.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"GlossaryTerm.equivalency"},
            dependsOnGroups = {"GlossaryTerm.serialize", "GlossaryTerm.deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson();
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
    }

    @Test(
            groups = {"GlossaryTerm.equivalency"},
            dependsOnGroups = {"GlossaryTerm.serialize", "GlossaryTerm.deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
