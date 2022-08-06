package com.atlan.functional;

import static org.testng.Assert.*;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.exception.AtlanException;
import com.atlan.model.*;
import com.atlan.model.core.EntityX;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanDeleteType;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.relations.Reference;
import com.atlan.model.responses.EntityXMutationResponse;
import com.atlan.model.responses.EntityXResponse;
import com.atlan.model.responses.IndexSearchResponseX;
import com.atlan.model.responses.MutatedXEntities;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

@Slf4j
public class GlossaryXTest extends BaseAtlanTest {

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
        GlossaryX glossary = GlossaryX.createRequest(GLOSSARY_NAME);
        try {
            EntityXMutationResponse response = EntityX.create(glossary);
            assertNotNull(response);
            MutatedXEntities mutatedEntities = response.getMutatedEntities();
            assertNotNull(mutatedEntities);
            assertNull(mutatedEntities.getDELETE());
            assertNull(mutatedEntities.getUPDATE());
            List<EntityX> entities = mutatedEntities.getCREATE();
            assertNotNull(entities);
            assertEquals(entities.size(), 1);
            EntityX one = entities.get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), GlossaryX.TYPE_NAME);
            assertTrue(one instanceof GlossaryX);
            glossary = (GlossaryX) one;
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
        GlossaryCategoryX category = GlossaryCategoryX.createRequest(CATEGORY_NAME, glossaryGuid, null);
        try {
            EntityXMutationResponse response = EntityX.create(category);
            assertNotNull(response);
            MutatedXEntities mutatedEntities = response.getMutatedEntities();
            assertNotNull(mutatedEntities);
            assertNull(mutatedEntities.getDELETE());
            validateGlossaryUpdate(mutatedEntities.getUPDATE());
            List<EntityX> entities = mutatedEntities.getCREATE();
            assertNotNull(entities);
            assertEquals(entities.size(), 1);
            EntityX one = entities.get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), GlossaryCategoryX.TYPE_NAME);
            assertTrue(one instanceof GlossaryCategoryX);
            category = (GlossaryCategoryX) one;
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
        GlossaryTermX term = GlossaryTermX.createRequest(TERM_NAME, glossaryGuid, null);
        try {
            EntityXMutationResponse response = EntityX.create(term);
            assertNotNull(response);
            MutatedXEntities mutatedEntities = response.getMutatedEntities();
            assertNotNull(mutatedEntities);
            assertNull(mutatedEntities.getDELETE());
            validateGlossaryUpdate(mutatedEntities.getUPDATE());
            List<EntityX> entities = mutatedEntities.getCREATE();
            assertNotNull(entities);
            assertEquals(entities.size(), 1);
            EntityX one = entities.get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), GlossaryTermX.TYPE_NAME);
            assertTrue(one instanceof GlossaryTermX);
            term = (GlossaryTermX) one;
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
            EntityXResponse response = EntityX.retrieve(glossaryGuid);
            assertNotNull(response);
            EntityX one = response.getEntity();
            assertNotNull(one);
            assertEquals(one.getTypeName(), GlossaryX.TYPE_NAME);
            assertTrue(one instanceof GlossaryX);
            GlossaryX glossary = (GlossaryX) one;
            assertEquals(glossary.getGuid(), glossaryGuid);
            assertEquals(glossary.getQualifiedName(), glossaryQame);
            assertEquals(glossary.getName(), GLOSSARY_NAME);
            assertNotNull(glossary.getTerms());
            assertEquals(glossary.getTerms().size(), 1);
            assertEquals(glossary.getTerms().get(0).getTypeName(), GlossaryTermX.TYPE_NAME);
            assertEquals(glossary.getTerms().get(0).getGuid(), termGuid);
            assertNotNull(glossary.getCategories());
            assertEquals(glossary.getCategories().size(), 1);
            assertEquals(glossary.getCategories().get(0).getTypeName(), GlossaryCategoryX.TYPE_NAME);
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
            EntityXResponse response = EntityX.retrieve(categoryGuid);
            assertNotNull(response);
            EntityX one = response.getEntity();
            assertNotNull(one);
            assertEquals(one.getTypeName(), GlossaryCategoryX.TYPE_NAME);
            assertTrue(one instanceof GlossaryCategoryX);
            GlossaryCategoryX category = (GlossaryCategoryX) one;
            assertEquals(category.getGuid(), categoryGuid);
            assertEquals(category.getQualifiedName(), categoryQame);
            assertEquals(category.getName(), CATEGORY_NAME);
            assertNotNull(category.getAnchor());
            assertEquals(category.getAnchor().getTypeName(), GlossaryX.TYPE_NAME);
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
            EntityXResponse response = EntityX.retrieve(termGuid);
            assertNotNull(response);
            EntityX one = response.getEntity();
            assertNotNull(one);
            assertEquals(one.getTypeName(), GlossaryTermX.TYPE_NAME);
            assertTrue(one instanceof GlossaryTermX);
            GlossaryTermX term = (GlossaryTermX) one;
            assertEquals(term.getGuid(), termGuid);
            assertEquals(term.getQualifiedName(), termQame);
            assertEquals(term.getName(), TERM_NAME);
            assertNotNull(term.getAnchor());
            assertEquals(term.getAnchor().getTypeName(), GlossaryX.TYPE_NAME);
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
        GlossaryX glossary = GlossaryX.updateRequest(glossaryGuid, GLOSSARY_NAME);
        glossary = glossary.toBuilder()
                .certificateStatus(AtlanCertificateStatus.VERIFIED)
                .announcementType(AtlanAnnouncementType.INFORMATION)
                .announcementTitle(ANNOUNCEMENT_TITLE)
                .announcementMessage(ANNOUNCEMENT_MESSAGE)
                .build();
        try {
            EntityXMutationResponse response = EntityX.update(glossary);
            assertNotNull(response);
            MutatedXEntities mutatedEntities = response.getMutatedEntities();
            assertNotNull(mutatedEntities);
            assertNull(mutatedEntities.getDELETE());
            assertNull(mutatedEntities.getCREATE());
            List<EntityX> entities = mutatedEntities.getUPDATE();
            assertNotNull(entities);
            assertEquals(entities.size(), 1);
            EntityX one = entities.get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), GlossaryX.TYPE_NAME);
            assertTrue(one instanceof GlossaryX);
            glossary = (GlossaryX) one;
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
        GlossaryCategoryX category = GlossaryCategoryX.updateRequest(categoryQame, CATEGORY_NAME, glossaryGuid);
        category = category.toBuilder()
                .certificateStatus(AtlanCertificateStatus.DRAFT)
                .announcementType(AtlanAnnouncementType.WARNING)
                .announcementTitle(ANNOUNCEMENT_TITLE)
                .announcementMessage(ANNOUNCEMENT_MESSAGE)
                .build();
        try {
            EntityXMutationResponse response = EntityX.update(category);
            assertNotNull(response);
            MutatedXEntities mutatedEntities = response.getMutatedEntities();
            assertNotNull(mutatedEntities);
            assertNull(mutatedEntities.getDELETE());
            assertNull(mutatedEntities.getCREATE());
            List<EntityX> entities = mutatedEntities.getUPDATE();
            assertNotNull(entities);
            assertEquals(entities.size(), 1);
            EntityX one = entities.get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), GlossaryCategoryX.TYPE_NAME);
            assertTrue(one instanceof GlossaryCategoryX);
            category = (GlossaryCategoryX) one;
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
        GlossaryCategoryX category2 = GlossaryCategoryX.updateRequest(categoryQame, CATEGORY_NAME, glossaryGuid);
        category2.removeAnnouncement();
        try {
            EntityXMutationResponse response = EntityX.update(category2);
            assertNotNull(response);
            MutatedXEntities mutatedEntities = response.getMutatedEntities();
            assertNotNull(mutatedEntities);
            assertNull(mutatedEntities.getDELETE());
            assertNull(mutatedEntities.getCREATE());
            List<EntityX> entities = mutatedEntities.getUPDATE();
            assertNotNull(entities);
            assertEquals(entities.size(), 1);
            EntityX one = entities.get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), GlossaryCategoryX.TYPE_NAME);
            assertTrue(one instanceof GlossaryCategoryX);
            category2 = (GlossaryCategoryX) one;
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
        GlossaryTermX term = GlossaryTermX.updateRequest(termQame, TERM_NAME, glossaryGuid);
        term = term.toBuilder()
                .certificateStatus(AtlanCertificateStatus.DEPRECATED)
                .announcementType(AtlanAnnouncementType.ISSUE)
                .announcementTitle(ANNOUNCEMENT_TITLE)
                .announcementMessage(ANNOUNCEMENT_MESSAGE)
                .category(Reference.to(GlossaryCategoryX.TYPE_NAME, categoryGuid))
                .build();
        try {
            EntityXMutationResponse response = EntityX.update(term);
            assertNotNull(response);
            MutatedXEntities mutatedEntities = response.getMutatedEntities();
            assertNotNull(mutatedEntities);
            assertNull(mutatedEntities.getDELETE());
            assertNull(mutatedEntities.getCREATE());
            List<EntityX> entities = mutatedEntities.getUPDATE();
            assertNotNull(entities);
            assertEquals(entities.size(), 2);
            EntityX one = entities.get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), GlossaryTermX.TYPE_NAME);
            assertTrue(one instanceof GlossaryTermX);
            term = (GlossaryTermX) one;
            assertEquals(term.getGuid(), termGuid);
            assertEquals(term.getQualifiedName(), termQame);
            assertEquals(term.getName(), TERM_NAME);
            assertEquals(term.getCertificateStatus(), AtlanCertificateStatus.DEPRECATED);
            assertEquals(term.getAnnouncementType(), AtlanAnnouncementType.ISSUE);
            assertEquals(term.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
            assertEquals(term.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
            one = entities.get(1);
            assertNotNull(one);
            assertEquals(one.getTypeName(), GlossaryCategoryX.TYPE_NAME);
            assertTrue(one instanceof GlossaryCategoryX);
            GlossaryCategoryX category = (GlossaryCategoryX) one;
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

            Query byType = MatchQuery.of(m -> m.field("__typeName.keyword").query(GlossaryTermX.TYPE_NAME))
                    ._toQuery();

            Query byName =
                    MatchQuery.of(m -> m.field("name.keyword").query(TERM_NAME))._toQuery();

            Query combined =
                    BoolQuery.of(b -> b.must(byState).must(byType).must(byName))._toQuery();

            IndexSearchX index = IndexSearchX.builder()
                    .dsl(IndexSearchDSL.builder()
                            .from(0)
                            .size(100)
                            .query(combined)
                            .build())
                    .attributes(Collections.singletonList("anchor"))
                    .relationAttributes(Collections.singletonList("certificateStatus"))
                    .build();

            IndexSearchResponseX response = index.search();

            assertNotNull(response);
            assertEquals(response.getApproximateCount().longValue(), 1L);
            List<EntityX> entities = response.getEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 1);
            EntityX one = entities.get(0);
            assertTrue(one instanceof GlossaryTermX);
            GlossaryTermX term = (GlossaryTermX) one;
            assertEquals(term.getGuid(), termGuid);
            assertEquals(term.getQualifiedName(), termQame);
            assertNotNull(term.getAnchor());
            assertEquals(term.getAnchor().getTypeName(), GlossaryX.TYPE_NAME);
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
            groups = {"term.delete"},
            dependsOnGroups = {"term.search"})
    void deleteTerm() {
        try {
            EntityXMutationResponse response = EntityX.delete(termGuid, AtlanDeleteType.HARD);
            assertNotNull(response);
            MutatedXEntities mutatedEntities = response.getMutatedEntities();
            assertNotNull(mutatedEntities);
            assertNull(mutatedEntities.getCREATE());
            assertNull(mutatedEntities.getUPDATE());
            List<EntityX> entities = mutatedEntities.getDELETE();
            assertNotNull(entities);
            assertEquals(entities.size(), 1);
            EntityX one = entities.get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), GlossaryTermX.TYPE_NAME);
            assertTrue(one instanceof GlossaryTermX);
            GlossaryTermX term = (GlossaryTermX) one;
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
            groups = {"category.delete"},
            dependsOnGroups = {"term.delete", "category.update", "category.remove.attributes"})
    void deleteCategory() {
        try {
            EntityXMutationResponse response = EntityX.delete(categoryGuid, AtlanDeleteType.HARD);
            assertNotNull(response);
            MutatedXEntities mutatedEntities = response.getMutatedEntities();
            assertNotNull(mutatedEntities);
            assertNull(mutatedEntities.getCREATE());
            assertNull(mutatedEntities.getUPDATE());
            List<EntityX> entities = mutatedEntities.getDELETE();
            assertNotNull(entities);
            assertEquals(entities.size(), 1);
            EntityX one = entities.get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), GlossaryCategoryX.TYPE_NAME);
            assertTrue(one instanceof GlossaryCategoryX);
            GlossaryCategoryX category = (GlossaryCategoryX) one;
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
            groups = {"glossary.delete"},
            dependsOnGroups = {"category.delete"})
    void deleteGlossary() {
        try {
            EntityXMutationResponse response = EntityX.delete(glossaryGuid, AtlanDeleteType.HARD);
            assertNotNull(response);
            MutatedXEntities mutatedEntities = response.getMutatedEntities();
            assertNotNull(mutatedEntities);
            assertNull(mutatedEntities.getCREATE());
            assertNull(mutatedEntities.getUPDATE());
            List<EntityX> entities = mutatedEntities.getDELETE();
            assertNotNull(entities);
            assertEquals(entities.size(), 1);
            EntityX one = entities.get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), GlossaryX.TYPE_NAME);
            assertTrue(one instanceof GlossaryX);
            GlossaryX glossary = (GlossaryX) one;
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

    private void validateGlossaryUpdate(List<EntityX> entities) {
        assertNotNull(entities);
        assertEquals(entities.size(), 1);
        EntityX one = entities.get(0);
        assertNotNull(one);
        assertEquals(one.getTypeName(), GlossaryX.TYPE_NAME);
        assertTrue(one instanceof GlossaryX);
        GlossaryX glossary = (GlossaryX) one;
        assertEquals(glossary.getGuid(), glossaryGuid);
        assertEquals(glossary.getQualifiedName(), glossaryQame);
    }
}
