package com.atlan.model;

import static org.testng.Assert.*;

import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.relations.ReferenceJ;
import com.atlan.net.AtlanObjectJ;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.testng.annotations.Test;

public class GlossaryJTest {

    // TODO: classifications, meaningNames, customMetadataSets

    private static final GlossaryJ full = GlossaryJ.builder()
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
            .readme(ReferenceJ.to("Readme", "readmeGuid"))
            .meaning(ReferenceJ.to(GlossaryTerm.TYPE_NAME, "termGuid1"))
            .meaning(ReferenceJ.to(GlossaryTerm.TYPE_NAME, "termGuid2"))
            .term(ReferenceJ.to(GlossaryTerm.TYPE_NAME, "termGuid1"))
            .term(ReferenceJ.to(GlossaryTerm.TYPE_NAME, "termGuid2"))
            .category(ReferenceJ.to(GlossaryCategory.TYPE_NAME, "categoryGuid1"))
            .category(ReferenceJ.to(GlossaryCategory.TYPE_NAME, "categoryGuid2"))
            .build();

    private static GlossaryJ frodo;
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
        frodo = AtlanObjectJ.mapper.readValue(serialized, GlossaryJ.class);
        assertNotNull(frodo);
    }

    @Test(
            groups = {"equivalency"},
            dependsOnGroups = {"serialize", "deserialize"})
    void serializedEquivalency() {
        assertNotNull(serialized);
        assertNotNull(frodo);
        String backAgain = frodo.toJson();
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop.");
    }

    @Test(
            groups = {"equivalency"},
            dependsOnGroups = {"serialize", "deserialize"})
    void deserializedEquivalency() {
        assertNotNull(full);
        assertNotNull(frodo);
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop.");
    }

    @Test
    void anchorLinkByGuid() {
        ReferenceJ anchorLink = GlossaryJ.anchorLink("glossaryGuid", null);
        assertNotNull(anchorLink);
        assertEquals(anchorLink.getTypeName(), GlossaryJ.TYPE_NAME);
        assertEquals(anchorLink.getGuid(), "glossaryGuid");
        assertNull(anchorLink.getUniqueAttributes());
    }

    @Test
    void anchorLinkByQualifiedName() {
        ReferenceJ anchorLink = GlossaryJ.anchorLink(null, "glossaryQualifiedName");
        assertNotNull(anchorLink);
        assertEquals(anchorLink.getTypeName(), GlossaryJ.TYPE_NAME);
        assertNotNull(anchorLink.getUniqueAttributes());
        assertEquals(anchorLink.getUniqueAttributes().getQualifiedName(), "glossaryQualifiedName");
        assertNull(anchorLink.getGuid());
    }

    @Test
    void anchorLinkByBoth() {
        ReferenceJ anchorLink = GlossaryJ.anchorLink("glossaryGuid", "glossaryQualifiedName");
        assertNotNull(anchorLink);
        assertEquals(anchorLink.getTypeName(), GlossaryJ.TYPE_NAME);
        assertEquals(anchorLink.getGuid(), "glossaryGuid");
        assertNull(anchorLink.getUniqueAttributes());
    }
}
