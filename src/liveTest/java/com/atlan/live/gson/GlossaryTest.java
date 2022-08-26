package com.atlan.live;

import static org.testng.Assert.*;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import com.atlan.exception.AtlanException;
import com.atlan.model.*;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.relations.GuidReference;
import com.atlan.model.relations.Reference;
import com.atlan.model.responses.EntityMutationResponse;
import com.atlan.model.responses.IndexSearchResponse;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.testng.annotations.Test;

@Test(groups = {"glossary"})
public class GlossaryTest extends AtlanLiveTest {

    public static final String GLOSSARY_NAME = "JavaClient Test Glossary";
    public static final String CATEGORY_NAME = "JavaClient Test Category";
    public static final String TERM_NAME1 = "JavaClient Test Term1";
    public static final String TERM_NAME2 = "JavaClient Test Term2";

    public static String glossaryGuid = null;
    public static String glossaryQame = null;

    private static String categoryGuid = null;
    private static String categoryQame = null;

    public static String termGuid1 = null;
    public static String termQame1 = null;
    public static String termGuid2 = null;
    public static String termQame2 = null;

    @Test(groups = {"create.glossary"})
    void createGlossary() {
        Glossary glossary = Glossary.creator(GLOSSARY_NAME).build();
        try {
            EntityMutationResponse response = glossary.upsert();
            assertNotNull(response);
            assertEquals(response.getDeletedEntities().size(), 0);
            assertEquals(response.getUpdatedEntities().size(), 0);
            List<Entity> entities = response.getCreatedEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 1);
            Entity one = entities.get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), Glossary.TYPE_NAME);
            assertTrue(one instanceof Glossary);
            glossary = (Glossary) one;
            glossaryGuid = glossary.getGuid();
            assertNotNull(glossaryGuid);
            glossaryQame = glossary.getQualifiedName();
            assertNotNull(glossaryQame);
            assertEquals(glossary.getName(), GLOSSARY_NAME);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception: " + e.getMessage());
        }
    }

    @Test(
            groups = {"create.category"},
            dependsOnGroups = {"create.glossary"})
    void createCategory() {
        GlossaryCategory category =
                GlossaryCategory.creator(CATEGORY_NAME, glossaryGuid, null).build();
        try {
            EntityMutationResponse response = category.upsert();
            assertNotNull(response);
            assertEquals(response.getDeletedEntities().size(), 0);
            validateGlossaryUpdate(response.getUpdatedEntities());
            List<Entity> entities = response.getCreatedEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 1);
            Entity one = entities.get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), GlossaryCategory.TYPE_NAME);
            assertTrue(one instanceof GlossaryCategory);
            category = (GlossaryCategory) one;
            categoryGuid = category.getGuid();
            assertNotNull(categoryGuid);
            categoryQame = category.getQualifiedName();
            assertNotNull(categoryQame);
            assertEquals(category.getName(), CATEGORY_NAME);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception: " + e.getMessage());
        }
    }

    @Test(
            groups = {"create.term"},
            dependsOnGroups = {"create.glossary"})
    void createTerm1() {
        GlossaryTerm term = GlossaryTerm.creator(TERM_NAME1, glossaryGuid, null).build();
        try {
            EntityMutationResponse response = term.upsert();
            assertNotNull(response);
            assertEquals(response.getDeletedEntities().size(), 0);
            validateGlossaryUpdate(response.getUpdatedEntities());
            List<Entity> entities = response.getCreatedEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 1);
            Entity one = entities.get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), GlossaryTerm.TYPE_NAME);
            assertTrue(one instanceof GlossaryTerm);
            term = (GlossaryTerm) one;
            termGuid1 = term.getGuid();
            assertNotNull(termGuid1);
            assertNotNull(term.getAttributes());
            termQame1 = term.getQualifiedName();
            assertNotNull(termQame1);
            assertEquals(term.getName(), TERM_NAME1);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception: " + e.getMessage());
        }
    }

    @Test(
            groups = {"create.term"},
            dependsOnGroups = {"create.glossary", "create.category"})
    void createTerm2() {
        GlossaryTerm term = GlossaryTerm.creator(TERM_NAME2, glossaryGuid, null).build();
        try {
            EntityMutationResponse response = term.upsert();
            assertNotNull(response);
            assertEquals(response.getDeletedEntities().size(), 0);
            validateGlossaryUpdate(response.getUpdatedEntities());
            List<Entity> entities = response.getCreatedEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 1);
            Entity one = entities.get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), GlossaryTerm.TYPE_NAME);
            assertTrue(one instanceof GlossaryTerm);
            term = (GlossaryTerm) one;
            termGuid2 = term.getGuid();
            assertNotNull(termGuid2);
            assertNotNull(term.getAttributes());
            termQame2 = term.getQualifiedName();
            assertNotNull(termQame2);
            assertEquals(term.getName(), TERM_NAME2);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception: " + e.getMessage());
        }
    }

    @Test(
            groups = {"read.glossary"},
            dependsOnGroups = {"create.glossary", "create.category", "create.term"})
    void readGlossary() {
        try {
            Entity one = Entity.retrieveFull(glossaryGuid);
            assertNotNull(one);
            assertEquals(one.getTypeName(), Glossary.TYPE_NAME);
            assertTrue(one instanceof Glossary);
            Glossary glossary = (Glossary) one;
            assertEquals(glossary.getGuid(), glossaryGuid);
            assertEquals(glossary.getQualifiedName(), glossaryQame);
            assertEquals(glossary.getName(), GLOSSARY_NAME);
            assertNotNull(glossary.getTerms());
            assertEquals(glossary.getTerms().size(), 2);
            Set<String> types =
                    glossary.getTerms().stream().map(Reference::getTypeName).collect(Collectors.toSet());
            assertNotNull(types);
            assertEquals(types.size(), 1);
            assertTrue(types.contains(GlossaryTerm.TYPE_NAME));
            Set<String> guids =
                    glossary.getTerms().stream().map(Reference::getGuid).collect(Collectors.toSet());
            assertNotNull(guids);
            assertEquals(guids.size(), 2);
            assertTrue(guids.contains(termGuid1));
            assertTrue(guids.contains(termGuid2));
            assertNotNull(glossary.getCategories());
            assertEquals(glossary.getCategories().size(), 1);
            types = glossary.getCategories().stream()
                    .map(Reference::getTypeName)
                    .collect(Collectors.toSet());
            assertTrue(types.contains(GlossaryCategory.TYPE_NAME));
            guids = glossary.getCategories().stream().map(Reference::getGuid).collect(Collectors.toSet());
            assertTrue(guids.contains(categoryGuid));
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception: " + e.getMessage());
        }
    }

    @Test(
            groups = {"read.category"},
            dependsOnGroups = {"create.category"})
    void readCategory() {
        try {
            Entity one = Entity.retrieveFull(categoryGuid);
            assertNotNull(one);
            assertEquals(one.getTypeName(), GlossaryCategory.TYPE_NAME);
            assertTrue(one instanceof GlossaryCategory);
            GlossaryCategory category = (GlossaryCategory) one;
            assertEquals(category.getGuid(), categoryGuid);
            assertEquals(category.getQualifiedName(), categoryQame);
            assertEquals(category.getName(), CATEGORY_NAME);
            assertNotNull(category.getAnchor());
            assertEquals(category.getAnchor().getTypeName(), Glossary.TYPE_NAME);
            assertEquals(category.getAnchor().getGuid(), glossaryGuid);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception: " + e.getMessage());
        }
    }

    @Test(
            groups = {"read.term"},
            dependsOnGroups = {"create.term"})
    void readTerm() {
        try {
            Entity one = Entity.retrieveFull(termGuid1);
            assertNotNull(one);
            assertEquals(one.getTypeName(), GlossaryTerm.TYPE_NAME);
            assertTrue(one instanceof GlossaryTerm);
            GlossaryTerm term = (GlossaryTerm) one;
            assertEquals(term.getGuid(), termGuid1);
            assertEquals(term.getQualifiedName(), termQame1);
            assertEquals(term.getName(), TERM_NAME1);
            assertNotNull(term.getAnchor());
            assertEquals(term.getAnchor().getTypeName(), Glossary.TYPE_NAME);
            assertEquals(term.getAnchor().getGuid(), glossaryGuid);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception: " + e.getMessage());
        }
    }

    @Test(
            groups = {"update.glossary"},
            dependsOnGroups = {"read.glossary"})
    void updateGlossary() {
        Glossary glossary = Glossary.updater(glossaryGuid, GLOSSARY_NAME).build();
        glossary = glossary.toBuilder()
                .certificateStatus(AtlanCertificateStatus.VERIFIED)
                .announcementType(AtlanAnnouncementType.INFORMATION)
                .announcementTitle(ANNOUNCEMENT_TITLE)
                .announcementMessage(ANNOUNCEMENT_MESSAGE)
                .build();
        try {
            EntityMutationResponse response = glossary.upsert();
            assertNotNull(response);
            assertEquals(response.getDeletedEntities().size(), 0);
            assertEquals(response.getCreatedEntities().size(), 0);
            List<Entity> entities = response.getUpdatedEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 1);
            Entity one = entities.get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), Glossary.TYPE_NAME);
            assertTrue(one instanceof Glossary);
            glossary = (Glossary) one;
            assertEquals(glossary.getGuid(), glossaryGuid);
            assertEquals(glossary.getQualifiedName(), glossaryQame);
            assertEquals(glossary.getName(), GLOSSARY_NAME);
            assertEquals(glossary.getCertificateStatus(), AtlanCertificateStatus.VERIFIED);
            assertEquals(glossary.getAnnouncementType(), AtlanAnnouncementType.INFORMATION);
            assertEquals(glossary.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
            assertEquals(glossary.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception: " + e.getMessage());
        }
    }

    @Test(
            groups = {"update.category"},
            dependsOnGroups = {"create.category"})
    void updateCategory() {
        GlossaryCategory category = GlossaryCategory.updater(categoryQame, CATEGORY_NAME, glossaryGuid)
                .build();
        category = category.toBuilder()
                .certificateStatus(AtlanCertificateStatus.DRAFT)
                .announcementType(AtlanAnnouncementType.WARNING)
                .announcementTitle(ANNOUNCEMENT_TITLE)
                .announcementMessage(ANNOUNCEMENT_MESSAGE)
                .build();
        try {
            EntityMutationResponse response = category.upsert();
            assertNotNull(response);
            assertEquals(response.getDeletedEntities().size(), 0);
            assertEquals(response.getCreatedEntities().size(), 0);
            List<Entity> entities = response.getUpdatedEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 1);
            Entity one = entities.get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), GlossaryCategory.TYPE_NAME);
            assertTrue(one instanceof GlossaryCategory);
            category = (GlossaryCategory) one;
            assertEquals(category.getGuid(), categoryGuid);
            assertEquals(category.getQualifiedName(), categoryQame);
            assertEquals(category.getName(), CATEGORY_NAME);
            assertEquals(category.getCertificateStatus(), AtlanCertificateStatus.DRAFT);
            assertEquals(category.getAnnouncementType(), AtlanAnnouncementType.WARNING);
            assertEquals(category.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
            assertEquals(category.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception: " + e.getMessage());
        }
    }

    @Test(
            groups = {"update.category.attributes"},
            dependsOnGroups = {"update.category"})
    void removeCategoryAttributes() {
        GlossaryCategory category2 = GlossaryCategory.updater(categoryQame, CATEGORY_NAME, glossaryGuid)
                .build();
        category2.removeAnnouncement();
        try {
            EntityMutationResponse response = category2.upsert();
            assertNotNull(response);
            assertEquals(response.getDeletedEntities().size(), 0);
            assertEquals(response.getCreatedEntities().size(), 0);
            List<Entity> entities = response.getUpdatedEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 1);
            Entity one = entities.get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), GlossaryCategory.TYPE_NAME);
            assertTrue(one instanceof GlossaryCategory);
            category2 = (GlossaryCategory) one;
            assertEquals(category2.getGuid(), categoryGuid);
            assertEquals(category2.getQualifiedName(), categoryQame);
            assertEquals(category2.getName(), CATEGORY_NAME);
            assertEquals(category2.getCertificateStatus(), AtlanCertificateStatus.DRAFT);
            assertNull(category2.getAnnouncementType());
            assertNull(category2.getAnnouncementTitle());
            assertNull(category2.getAnnouncementMessage());
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception: " + e.getMessage());
        }
    }

    @Test(
            groups = {"update.term"},
            dependsOnGroups = {"create.term", "create.category"})
    void updateTerm() {
        GlossaryTerm term = GlossaryTerm.updater(termQame1, TERM_NAME1, glossaryGuid)
                .announcementType(AtlanAnnouncementType.ISSUE)
                .announcementTitle(ANNOUNCEMENT_TITLE)
                .announcementMessage(ANNOUNCEMENT_MESSAGE)
                .category(GuidReference.to(GlossaryCategory.TYPE_NAME, categoryGuid))
                .build();
        try {
            EntityMutationResponse response = term.upsert();
            assertNotNull(response);
            assertEquals(response.getDeletedEntities().size(), 0);
            assertEquals(response.getCreatedEntities().size(), 0);
            List<Entity> entities = response.getUpdatedEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 2);
            Entity one = entities.get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), GlossaryTerm.TYPE_NAME);
            assertTrue(one instanceof GlossaryTerm);
            term = (GlossaryTerm) one;
            assertEquals(term.getGuid(), termGuid1);
            assertEquals(term.getQualifiedName(), termQame1);
            assertEquals(term.getName(), TERM_NAME1);
            assertEquals(term.getAnnouncementType(), AtlanAnnouncementType.ISSUE);
            assertEquals(term.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
            assertEquals(term.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
            one = entities.get(1);
            assertNotNull(one);
            assertEquals(one.getTypeName(), GlossaryCategory.TYPE_NAME);
            assertTrue(one instanceof GlossaryCategory);
            GlossaryCategory category = (GlossaryCategory) one;
            assertEquals(category.getGuid(), categoryGuid);
            assertEquals(category.getQualifiedName(), categoryQame);
            assertEquals(category.getName(), CATEGORY_NAME);
            term = GlossaryTerm.updateCertificate(
                    termQame1, TERM_NAME1, glossaryGuid, AtlanCertificateStatus.DEPRECATED, null);
            assertNotNull(term);
            assertEquals(term.getCertificateStatus(), AtlanCertificateStatus.DEPRECATED);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception: " + e.getMessage());
        }
    }

    @Test(
            groups = {"search.term"},
            dependsOnGroups = {"update.term"})
    void searchTerms() {
        try {
            Query byState =
                    TermQuery.of(t -> t.field("__state").value("ACTIVE"))._toQuery();

            Query byType = TermQuery.of(t -> t.field("__typeName.keyword").value(GlossaryTerm.TYPE_NAME))
                    ._toQuery();

            Query byName =
                    TermQuery.of(t -> t.field("name.keyword").value(TERM_NAME1))._toQuery();

            Query combined =
                    BoolQuery.of(b -> b.must(byState).must(byType).must(byName))._toQuery();

            IndexSearchRequest index = IndexSearchRequest.builder()
                    .dsl(IndexSearchDSL.builder()
                            .from(0)
                            .size(100)
                            .query(combined)
                            .build())
                    .attributes(Collections.singletonList("anchor"))
                    .relationAttributes(Collections.singletonList("certificateStatus"))
                    .build();

            IndexSearchResponse response = index.search();

            assertNotNull(response);
            assertEquals(response.getApproximateCount().longValue(), 1L);
            List<Entity> entities = response.getEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 1);
            Entity one = entities.get(0);
            assertTrue(one instanceof GlossaryTerm);
            GlossaryTerm term = (GlossaryTerm) one;
            assertEquals(term.getGuid(), termGuid1);
            assertEquals(term.getQualifiedName(), termQame1);
            assertNotNull(term.getAnchor());
            assertEquals(term.getAnchor().getTypeName(), Glossary.TYPE_NAME);
            assertEquals(term.getAnchor().getGuid(), glossaryGuid);
            // TODO: test embedded relationship attributes that were requested
            //  ... this probably needs a more complex entity structure than
            //  just the basic references defined in Entity (?)
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception: " + e.getMessage());
        }
    }

    @Test(
            groups = {"purge.term"},
            dependsOnGroups = {"create.*", "update.*", "read.*", "search.*", "link.*", "unlink.*"},
            alwaysRun = true)
    void purgeTerm() {
        try {
            EntityMutationResponse response = GlossaryTerm.purge(termGuid1);
            assertNotNull(response);
            assertEquals(response.getCreatedEntities().size(), 0);
            assertEquals(response.getUpdatedEntities().size(), 0);
            List<Entity> entities = response.getDeletedEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 1);
            Entity one = entities.get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), GlossaryTerm.TYPE_NAME);
            assertTrue(one instanceof GlossaryTerm);
            GlossaryTerm term = (GlossaryTerm) one;
            assertEquals(term.getGuid(), termGuid1);
            assertEquals(term.getQualifiedName(), termQame1);
            assertEquals(term.getName(), TERM_NAME1);
            assertEquals(term.getCertificateStatus(), AtlanCertificateStatus.DEPRECATED);
            assertEquals(term.getAnnouncementType(), AtlanAnnouncementType.ISSUE);
            assertEquals(term.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
            assertEquals(term.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
            assertEquals(term.getStatus(), AtlanStatus.DELETED);
            assertEquals(term.getDeleteHandler(), "HARD");
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception: " + e.getMessage());
        }
    }

    @Test(
            groups = {"purge.category"},
            dependsOnGroups = {"purge.term"},
            alwaysRun = true)
    void purgeCategory() {
        try {
            EntityMutationResponse response = GlossaryCategory.purge(categoryGuid);
            assertNotNull(response);
            assertEquals(response.getCreatedEntities().size(), 0);
            assertEquals(response.getUpdatedEntities().size(), 0);
            List<Entity> entities = response.getDeletedEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 1);
            Entity one = entities.get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), GlossaryCategory.TYPE_NAME);
            assertTrue(one instanceof GlossaryCategory);
            GlossaryCategory category = (GlossaryCategory) one;
            assertEquals(category.getGuid(), categoryGuid);
            assertEquals(category.getQualifiedName(), categoryQame);
            assertEquals(category.getName(), CATEGORY_NAME);
            assertEquals(category.getCertificateStatus(), AtlanCertificateStatus.DRAFT);
            assertNull(category.getAnnouncementType());
            assertNull(category.getAnnouncementTitle());
            assertNull(category.getAnnouncementMessage());
            assertEquals(category.getStatus(), AtlanStatus.DELETED);
            assertEquals(category.getDeleteHandler(), "HARD");
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception: " + e.getMessage());
        }
    }

    @Test(
            groups = {"purge.glossary"},
            dependsOnGroups = {"purge.category"},
            alwaysRun = true)
    void purgeGlossary() {
        try {
            EntityMutationResponse response = Glossary.purge(glossaryGuid);
            assertNotNull(response);
            assertEquals(response.getCreatedEntities().size(), 0);
            assertEquals(response.getUpdatedEntities().size(), 0);
            List<Entity> entities = response.getDeletedEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 1);
            Entity one = entities.get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), Glossary.TYPE_NAME);
            assertTrue(one instanceof Glossary);
            Glossary glossary = (Glossary) one;
            assertEquals(glossary.getGuid(), glossaryGuid);
            assertEquals(glossary.getQualifiedName(), glossaryQame);
            assertEquals(glossary.getName(), GLOSSARY_NAME);
            assertEquals(glossary.getCertificateStatus(), AtlanCertificateStatus.VERIFIED);
            assertEquals(glossary.getAnnouncementType(), AtlanAnnouncementType.INFORMATION);
            assertEquals(glossary.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
            assertEquals(glossary.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
            assertEquals(glossary.getStatus(), AtlanStatus.DELETED);
            assertEquals(glossary.getDeleteHandler(), "HARD");
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception: " + e.getMessage());
        }
    }

    private void validateGlossaryUpdate(List<Entity> entities) {
        assertNotNull(entities);
        assertEquals(entities.size(), 1);
        Entity one = entities.get(0);
        assertNotNull(one);
        assertEquals(one.getTypeName(), Glossary.TYPE_NAME);
        assertTrue(one instanceof Glossary);
        Glossary glossary = (Glossary) one;
        assertEquals(glossary.getGuid(), glossaryGuid);
        assertEquals(glossary.getQualifiedName(), glossaryQame);
    }
}
