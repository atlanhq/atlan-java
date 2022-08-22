package com.atlan.model;

import static org.testng.Assert.*;

import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.relations.Reference;
import com.atlan.model.serde.IndistinctAsset;
import com.atlan.net.ApiResource;
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
            .link(Reference.to("Resource", "linkGuid1"))
            .link(Reference.to("Resource", "linkGuid2"))
            .readme(Reference.to("Readme", "readmeGuid"))
            .meaning(Reference.to(GlossaryTerm.TYPE_NAME, "termGuid1"))
            .meaning(Reference.to(GlossaryTerm.TYPE_NAME, "termGuid2"))
            .anchor(Reference.to(Glossary.TYPE_NAME, "glossaryGuid"))
            .assignedEntity(Reference.to(IndistinctAsset.TYPE_NAME, "assetGuid1"))
            .assignedEntity(Reference.to(IndistinctAsset.TYPE_NAME, "assetGuid2"))
            .category(Reference.to(GlossaryCategory.TYPE_NAME, "categoryGuid1"))
            .category(Reference.to(GlossaryCategory.TYPE_NAME, "categoryGuid2"))
            .addToSeeAlso(Reference.to(GlossaryTerm.TYPE_NAME, "termGuid1"))
            .addToSeeAlso(Reference.to(GlossaryTerm.TYPE_NAME, "termGuid2"))
            .synonym(Reference.to(GlossaryTerm.TYPE_NAME, "termGuid1"))
            .synonym(Reference.to(GlossaryTerm.TYPE_NAME, "termGuid2"))
            .antonym(Reference.to(GlossaryTerm.TYPE_NAME, "termGuid1"))
            .antonym(Reference.to(GlossaryTerm.TYPE_NAME, "termGuid2"))
            .preferredTerm(Reference.to(GlossaryTerm.TYPE_NAME, "termGuid1"))
            .preferredTerm(Reference.to(GlossaryTerm.TYPE_NAME, "termGuid2"))
            .addToReplacedBy(Reference.to(GlossaryTerm.TYPE_NAME, "termGuid1"))
            .addToReplacedBy(Reference.to(GlossaryTerm.TYPE_NAME, "termGuid2"))
            .translatedTerm(Reference.to(GlossaryTerm.TYPE_NAME, "termGuid1"))
            .translatedTerm(Reference.to(GlossaryTerm.TYPE_NAME, "termGuid2"))
            .addToValidValuesFor(Reference.to(GlossaryTerm.TYPE_NAME, "termGuid1"))
            .addToValidValuesFor(Reference.to(GlossaryTerm.TYPE_NAME, "termGuid2"))
            .build();

    private static GlossaryTerm frodo;
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
    void deserialization() {
        assertNotNull(serialized);
        frodo = ApiResource.GSON.fromJson(serialized, GlossaryTerm.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"equivalency"},
            dependsOnGroups = {"serialize", "deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson();
        assertEquals(serialized, backAgain, "Serialization is not equivalent after serde loop,");
    }

    @Test(
            groups = {"equivalency"},
            dependsOnGroups = {"serialize", "deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(full, frodo, "Deserialization is not equivalent after serde loop,");
    }

    @Test
    void anchorLinkByGuid() {
        Reference anchorLink = Glossary.anchorLink("glossaryGuid", null);
        assertNotNull(anchorLink);
        assertEquals(anchorLink.getTypeName(), Glossary.TYPE_NAME);
        assertEquals(anchorLink.getGuid(), "glossaryGuid");
        assertNull(anchorLink.getUniqueAttributes());
    }

    @Test
    void anchorLinkByQualifiedName() {
        Reference anchorLink = Glossary.anchorLink(null, "glossaryQualifiedName");
        assertNotNull(anchorLink);
        assertEquals(anchorLink.getTypeName(), Glossary.TYPE_NAME);
        assertNotNull(anchorLink.getUniqueAttributes());
        assertEquals(anchorLink.getUniqueAttributes().getQualifiedName(), "glossaryQualifiedName");
        assertNull(anchorLink.getGuid());
    }

    @Test
    void anchorLinkByBoth() {
        Reference anchorLink = Glossary.anchorLink("glossaryGuid", "glossaryQualifiedName");
        assertNotNull(anchorLink);
        assertEquals(anchorLink.getTypeName(), Glossary.TYPE_NAME);
        assertEquals(anchorLink.getGuid(), "glossaryGuid");
        assertNull(anchorLink.getUniqueAttributes());
    }
}
