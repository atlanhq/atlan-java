/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import static org.testng.Assert.*;

import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.relations.Reference;
import com.atlan.serde.Serde;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.testng.annotations.Test;

public class GlossaryTest {

    // TODO: classifications, meaningNames, customMetadataSets

    private static final Glossary full = Glossary.builder()
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
            .link(Reference.to("Resource", "linkGuid1"))
            .link(Reference.to("Resource", "linkGuid2"))
            .readme(Reference.to("Readme", "readmeGuid"))
            .meaning(Reference.to(GlossaryTerm.TYPE_NAME, "termGuid1"))
            .meaning(Reference.to(GlossaryTerm.TYPE_NAME, "termGuid2"))
            .term(Reference.to(GlossaryTerm.TYPE_NAME, "termGuid1"))
            .term(Reference.to(GlossaryTerm.TYPE_NAME, "termGuid2"))
            .category(Reference.to(GlossaryCategory.TYPE_NAME, "categoryGuid1"))
            .category(Reference.to(GlossaryCategory.TYPE_NAME, "categoryGuid2"))
            .build();

    private static Glossary frodo;
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
        frodo = Serde.mapper.readValue(serialized, Glossary.class);
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
