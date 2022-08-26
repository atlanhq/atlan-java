package com.atlan.model;

import static org.testng.Assert.*;

import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.relations.ReferenceJ;
import com.atlan.model.serde.IndistinctAssetJ;
import com.atlan.net.ApiResourceJ;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.testng.annotations.Test;

public class GlossaryTermJTest {

    private static final GlossaryTermJ full = GlossaryTermJ.builder()
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
            .connectorName("connectorName")
            .connectionName("connectionName")
            .connectionQualifiedName("connectionQualifiedName")
            .__hasLineage(false)
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
            .link(ReferenceJ.to("Resource", "linkGuid1"))
            .link(ReferenceJ.to("Resource", "linkGuid2"))
            .readme(ReferenceJ.to(ReadmeJ.TYPE_NAME, "readmeGuid"))
            .meaning(ReferenceJ.to(GlossaryTermJ.TYPE_NAME, "termGuid1"))
            .meaning(ReferenceJ.to(GlossaryTermJ.TYPE_NAME, "termGuid2"))
            .anchor(ReferenceJ.to(Glossary.TYPE_NAME, "glossaryGuid"))
            .assignedEntity(ReferenceJ.to(IndistinctAssetJ.TYPE_NAME, "assetGuid1"))
            .assignedEntity(ReferenceJ.to(IndistinctAssetJ.TYPE_NAME, "assetGuid2"))
            .category(ReferenceJ.to(GlossaryCategory.TYPE_NAME, "categoryGuid1"))
            .category(ReferenceJ.to(GlossaryCategory.TYPE_NAME, "categoryGuid2"))
            .addToSeeAlso(ReferenceJ.to(GlossaryTermJ.TYPE_NAME, "termGuid1"))
            .addToSeeAlso(ReferenceJ.to(GlossaryTermJ.TYPE_NAME, "termGuid2"))
            .synonym(ReferenceJ.to(GlossaryTermJ.TYPE_NAME, "termGuid1"))
            .synonym(ReferenceJ.to(GlossaryTermJ.TYPE_NAME, "termGuid2"))
            .antonym(ReferenceJ.to(GlossaryTermJ.TYPE_NAME, "termGuid1"))
            .antonym(ReferenceJ.to(GlossaryTermJ.TYPE_NAME, "termGuid2"))
            .preferredTerm(ReferenceJ.to(GlossaryTermJ.TYPE_NAME, "termGuid1"))
            .preferredTerm(ReferenceJ.to(GlossaryTermJ.TYPE_NAME, "termGuid2"))
            .addToReplacedBy(ReferenceJ.to(GlossaryTermJ.TYPE_NAME, "termGuid1"))
            .addToReplacedBy(ReferenceJ.to(GlossaryTermJ.TYPE_NAME, "termGuid2"))
            .translatedTerm(ReferenceJ.to(GlossaryTermJ.TYPE_NAME, "termGuid1"))
            .translatedTerm(ReferenceJ.to(GlossaryTermJ.TYPE_NAME, "termGuid2"))
            .addToValidValuesFor(ReferenceJ.to(GlossaryTermJ.TYPE_NAME, "termGuid1"))
            .addToValidValuesFor(ReferenceJ.to(GlossaryTermJ.TYPE_NAME, "termGuid2"))
            .build();

    private static GlossaryTermJ frodo;
    private static String serialized;

    @Test(groups = {"serialize"})
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
        frodo = ApiResourceJ.mapper.readValue(serialized, GlossaryTermJ.class);
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
