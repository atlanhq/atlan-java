package com.atlan.functional;

import static org.testng.Assert.*;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.exception.AtlanException;
import com.atlan.model.*;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.relations.Reference;
import com.atlan.model.responses.EntityMutationResponse;
import com.atlan.model.responses.IndexSearchResponse;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

@Slf4j
public class GlossaryTest extends AtlanLiveTest {

    public static final String GLOSSARY_NAME = "JavaClient Test Glossary";
    public static final String CATEGORY_NAME = "JavaClient Test Category";
    public static final String TERM_NAME = "JavaClient Test Term";

    private static String glossaryGuid = null;
    private static String glossaryQame = null;

    private static String categoryGuid = null;
    private static String categoryQame = null;

    private static String termGuid = null;
    private static String termQame = null;

    @Test(groups = {"glossary.create"})
    void createGlossary() {
        Glossary glossary = Glossary.toCreate(GLOSSARY_NAME);
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
            groups = {"category.create"},
            dependsOnGroups = {"glossary.create"})
    void createCategory() {
        GlossaryCategory category = GlossaryCategory.toCreate(CATEGORY_NAME, glossaryGuid, null);
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
            groups = {"term.create"},
            dependsOnGroups = {"glossary.create"})
    void createTerm() {
        GlossaryTerm term = GlossaryTerm.toCreate(TERM_NAME, glossaryGuid, null);
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
            termGuid = term.getGuid();
            assertNotNull(termGuid);
            assertNotNull(term.getAttributes());
            termQame = term.getQualifiedName();
            assertNotNull(termQame);
            assertEquals(term.getName(), TERM_NAME);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception: " + e.getMessage());
        }
    }

    @Test(
            groups = {"glossary.read"},
            dependsOnGroups = {"glossary.create", "category.create", "term.create"})
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
            assertEquals(glossary.getTerms().size(), 1);
            assertEquals(glossary.getTerms().get(0).getTypeName(), GlossaryTerm.TYPE_NAME);
            assertEquals(glossary.getTerms().get(0).getGuid(), termGuid);
            assertNotNull(glossary.getCategories());
            assertEquals(glossary.getCategories().size(), 1);
            assertEquals(glossary.getCategories().get(0).getTypeName(), GlossaryCategory.TYPE_NAME);
            assertEquals(glossary.getCategories().get(0).getGuid(), categoryGuid);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception: " + e.getMessage());
        }
    }

    @Test(
            groups = {"category.read"},
            dependsOnGroups = {"category.create"})
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
            groups = {"term.read"},
            dependsOnGroups = {"term.create"})
    void readTerm() {
        try {
            Entity one = Entity.retrieveFull(termGuid);
            assertNotNull(one);
            assertEquals(one.getTypeName(), GlossaryTerm.TYPE_NAME);
            assertTrue(one instanceof GlossaryTerm);
            GlossaryTerm term = (GlossaryTerm) one;
            assertEquals(term.getGuid(), termGuid);
            assertEquals(term.getQualifiedName(), termQame);
            assertEquals(term.getName(), TERM_NAME);
            assertNotNull(term.getAnchor());
            assertEquals(term.getAnchor().getTypeName(), Glossary.TYPE_NAME);
            assertEquals(term.getAnchor().getGuid(), glossaryGuid);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception: " + e.getMessage());
        }
    }

    @Test(
            groups = {"glossary.update"},
            dependsOnGroups = {"glossary.read"})
    void updateGlossary() {
        Glossary glossary = Glossary.toUpdate(glossaryGuid, GLOSSARY_NAME);
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
            groups = {"category.update"},
            dependsOnGroups = {"category.create"})
    void updateCategory() {
        GlossaryCategory category = GlossaryCategory.toUpdate(categoryQame, CATEGORY_NAME, glossaryGuid);
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
            groups = {"category.remove.attributes"},
            dependsOnGroups = {"category.update"})
    void removeCategoryAttributes() {
        GlossaryCategory category2 = GlossaryCategory.toUpdate(categoryQame, CATEGORY_NAME, glossaryGuid);
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
            groups = {"term.update"},
            dependsOnGroups = {"term.create", "category.create"})
    void updateTerm() {
        GlossaryTerm term = GlossaryTerm.toUpdate(termQame, TERM_NAME, glossaryGuid);
        term = term.toBuilder()
                .certificateStatus(AtlanCertificateStatus.DEPRECATED)
                .announcementType(AtlanAnnouncementType.ISSUE)
                .announcementTitle(ANNOUNCEMENT_TITLE)
                .announcementMessage(ANNOUNCEMENT_MESSAGE)
                .category(Reference.to(GlossaryCategory.TYPE_NAME, categoryGuid))
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
            assertEquals(term.getGuid(), termGuid);
            assertEquals(term.getQualifiedName(), termQame);
            assertEquals(term.getName(), TERM_NAME);
            assertEquals(term.getCertificateStatus(), AtlanCertificateStatus.DEPRECATED);
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
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception: " + e.getMessage());
        }
    }

    @Test(
            groups = {"term.search"},
            dependsOnGroups = {"term.update"})
    void searchTerms() {
        try {
            Query byState =
                    MatchQuery.of(m -> m.field("__state").query("ACTIVE"))._toQuery();

            Query byType = MatchQuery.of(m -> m.field("__typeName.keyword").query(GlossaryTerm.TYPE_NAME))
                    ._toQuery();

            Query byName =
                    MatchQuery.of(m -> m.field("name.keyword").query(TERM_NAME))._toQuery();

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
            assertEquals(term.getGuid(), termGuid);
            assertEquals(term.getQualifiedName(), termQame);
            assertNotNull(term.getAnchor());
            assertEquals(term.getAnchor().getTypeName(), Glossary.TYPE_NAME);
            assertEquals(term.getAnchor().getGuid(), glossaryGuid);
            // TODO: test embedded relationship attributes that were requested
            //  ... this probably needs a more complex entity structure than
            //  just the basic references defined in EntityX (?)
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception: " + e.getMessage());
        }
    }

    @Test(
            groups = {"term.purge"},
            dependsOnGroups = {"term.search"})
    void purgeTerm() {
        try {
            EntityMutationResponse response = GlossaryTerm.purge(termGuid);
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
            assertEquals(term.getGuid(), termGuid);
            assertEquals(term.getQualifiedName(), termQame);
            assertEquals(term.getName(), TERM_NAME);
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
            groups = {"category.purge"},
            dependsOnGroups = {"term.purge", "category.update", "category.remove.attributes"})
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
            groups = {"glossary.purge"},
            dependsOnGroups = {"category.purge"})
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
