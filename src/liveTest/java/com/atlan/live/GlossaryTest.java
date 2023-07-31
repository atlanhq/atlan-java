/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static com.atlan.util.QueryFactory.*;
import static org.testng.Assert.*;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.*;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.*;
import com.atlan.model.search.AggregationBucketResult;
import com.atlan.model.search.IndexSearchDSL;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.testng.annotations.Test;

/**
 * Tests all aspects of glossaries and related assets (categories and terms).
 */
public class GlossaryTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("GLS");

    private static final String GLOSSARY_NAME = PREFIX + " Traversable";
    private static final String TERM_NAME1 = PREFIX + " Term1";
    private static final String TERM_NAME2 = PREFIX + " Term2";

    private static Glossary glossary = null;
    private static GlossaryCategory category = null;
    private static GlossaryTerm term1 = null;
    private static GlossaryTerm term2 = null;

    private static String top1Guid = null;
    private static String top2Guid = null;
    private static String mid1aGuid = null;
    private static String mid1bGuid = null;
    private static String mid2aGuid = null;
    private static String mid2bGuid = null;
    private static String leaf1aaGuid = null;
    private static String leaf1abGuid = null;
    private static String leaf1baGuid = null;
    private static String leaf1bbGuid = null;
    private static String leaf2aaGuid = null;
    private static String leaf2abGuid = null;
    private static String leaf2baGuid = null;
    private static String leaf2bbGuid = null;

    /**
     * Create a new glossary with a unique name.
     *
     * @param name to make the glossary unique
     * @return the glossary that was created
     * @throws AtlanException on any error creating or reading-back the glossary
     */
    static Glossary createGlossary(String name) throws AtlanException {
        return createGlossary(Atlan.getDefaultClient(), name);
    }

    /**
     * Create a new glossary with a unique name.
     *
     * @param client connectivity to the Atlan tenant in which to create the glossary
     * @param name to make the glossary unique
     * @return the glossary that was created
     * @throws AtlanException on any error creating or reading-back the glossary
     */
    static Glossary createGlossary(AtlanClient client, String name) throws AtlanException {
        Glossary glossary = Glossary.creator(name).build();
        AssetMutationResponse response = glossary.save(client);
        assertNotNull(response);
        assertEquals(response.getDeletedAssets().size(), 0);
        assertEquals(response.getUpdatedAssets().size(), 0);
        assertEquals(response.getCreatedAssets().size(), 1);
        Asset one = response.getCreatedAssets().get(0);
        assertNotNull(one);
        assertTrue(one instanceof Glossary);
        glossary = (Glossary) one;
        assertNotNull(glossary.getGuid());
        assertNotNull(glossary.getQualifiedName());
        assertEquals(glossary.getName(), name);
        assertNotEquals(glossary.getQualifiedName(), name);
        return glossary;
    }

    /**
     * Create multiple categories at the same time, from the details provided.
     *
     * @param names of the categories to create
     * @param glossaryId GUID of the glossary in which to create the categories
     * @param parentCategoryId GUID of the parent in which to create the categories (or null if they should be root-level categories)
     * @return the list of categories that were created, in the same order as in the names provided
     * @throws AtlanException on any error creating or reading-back the categories
     */
    static List<GlossaryCategory> createCategories(List<String> names, String glossaryId, String parentCategoryId)
            throws AtlanException {
        List<Asset> toCreate = new ArrayList<>();
        for (String name : names) {
            GlossaryCategory one;
            if (parentCategoryId == null) {
                one = GlossaryCategory.creator(name, glossaryId, null).build();
            } else {
                one = GlossaryCategory.creator(name, glossaryId, null)
                        .parentCategory(GlossaryCategory.refByGuid(parentCategoryId))
                        .build();
            }
            toCreate.add(one);
        }
        AssetMutationResponse response = Atlan.getDefaultClient().assets.save(toCreate, false);
        assertNotNull(response);
        assertEquals(response.getDeletedAssets().size(), 0);
        assertEquals(response.getCreatedAssets().size(), names.size());
        List<Asset> entities = response.getCreatedAssets();
        List<GlossaryCategory> toReturn = new ArrayList<>(names.size());
        for (Asset created : entities) {
            if (created instanceof GlossaryCategory) {
                GlossaryCategory one = (GlossaryCategory) created;
                String name = one.getName();
                int index = names.indexOf(name);
                toReturn.add(index, one);
            }
        }
        return toReturn;
    }

    /**
     * Create a new glossary term with a unique name.
     *
     * @param name to make the glossary term unique
     * @param glossaryId GUID of the glossary in which to create the term
     * @return the glossary term that was created
     * @throws AtlanException on any error creating or reading-back the glossary term
     */
    static GlossaryTerm createTerm(String name, String glossaryId) throws AtlanException {
        return createTerm(Atlan.getDefaultClient(), name, glossaryId);
    }

    /**
     * Create a new glossary term with a unique name.
     *
     * @param client connectivity to the Atlan tenant in which to create the term
     * @param name to make the glossary term unique
     * @param glossaryId GUID of the glossary in which to create the term
     * @return the glossary term that was created
     * @throws AtlanException on any error creating or reading-back the glossary term
     */
    static GlossaryTerm createTerm(AtlanClient client, String name, String glossaryId) throws AtlanException {
        GlossaryTerm term = GlossaryTerm.creator(name, glossaryId, null).build();
        AssetMutationResponse response = term.save(client);
        assertNotNull(response);
        assertEquals(response.getDeletedAssets().size(), 0);
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof Glossary);
        Glossary glossary = (Glossary) one;
        assertEquals(glossary.getGuid(), glossaryId);
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertNotNull(one);
        assertTrue(one instanceof GlossaryTerm);
        term = (GlossaryTerm) one;
        assertNotNull(term.getGuid());
        assertNotNull(term.getQualifiedName());
        assertEquals(term.getName(), name);
        assertNotEquals(term.getQualifiedName(), name);
        return term;
    }

    /**
     * Delete (purge) the glossary with the provided GUID.
     *
     * @param guid of the glossary to purge
     * @return the purged glossary
     * @throws AtlanException on any errors purging the glossary
     */
    static Glossary deleteGlossary(String guid) throws AtlanException {
        AssetMutationResponse response = Glossary.purge(guid);
        assertNotNull(response);
        assertEquals(response.getCreatedAssets().size(), 0);
        assertEquals(response.getUpdatedAssets().size(), 0);
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof Glossary);
        Glossary deletedGlossary = (Glossary) one;
        assertEquals(deletedGlossary.getGuid(), guid);
        assertEquals(deletedGlossary.getStatus(), AtlanStatus.DELETED);
        return deletedGlossary;
    }

    /**
     * Delete (purge) the category with the provided GUID.
     *
     * @param guid of the category to purge
     * @return the purged category
     * @throws AtlanException on any errors purging the category
     */
    static GlossaryCategory deleteCategory(String guid) throws AtlanException {
        AssetMutationResponse response = GlossaryCategory.purge(guid);
        assertNotNull(response);
        assertEquals(response.getCreatedAssets().size(), 0);
        assertEquals(response.getUpdatedAssets().size(), 0);
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof GlossaryCategory);
        GlossaryCategory deletedCategory = (GlossaryCategory) one;
        assertEquals(deletedCategory.getGuid(), guid);
        assertEquals(deletedCategory.getStatus(), AtlanStatus.DELETED);
        return deletedCategory;
    }

    /**
     * Delete (purge) the term with the provided GUID.
     *
     * @param guid of the term to purge
     * @return the purged term
     * @throws AtlanException on any errors purging the term
     */
    static GlossaryTerm deleteTerm(String guid) throws AtlanException {
        AssetMutationResponse response = GlossaryTerm.purge(guid);
        assertNotNull(response);
        assertEquals(response.getCreatedAssets().size(), 0);
        assertEquals(response.getUpdatedAssets().size(), 0);
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof GlossaryTerm);
        GlossaryTerm deletedTerm = (GlossaryTerm) one;
        assertEquals(deletedTerm.getGuid(), guid);
        assertEquals(deletedTerm.getStatus(), AtlanStatus.DELETED);
        return deletedTerm;
    }

    @Test(groups = {"glossary.create.glossary"})
    void createGlossary() throws AtlanException {
        glossary = createGlossary(GLOSSARY_NAME);
    }

    @Test(
            groups = {"glossary.create.hierarchy"},
            dependsOnGroups = {"glossary.create.glossary"})
    void createHierarchy() throws AtlanException {

        List<String> topNames = List.of("top1" + PREFIX, "top2" + PREFIX);

        List<GlossaryCategory> tops = createCategories(topNames, glossary.getGuid(), null);
        assertEquals(tops.size(), 2);
        top1Guid = tops.get(0).getGuid();
        top2Guid = tops.get(1).getGuid();

        List<String> midNames = List.of("mid1a" + PREFIX, "mid1b" + PREFIX);

        List<GlossaryCategory> mids = createCategories(midNames, glossary.getGuid(), top1Guid);
        assertEquals(mids.size(), 2);
        mid1aGuid = mids.get(0).getGuid();
        mid1bGuid = mids.get(1).getGuid();

        midNames = List.of("mid2a" + PREFIX, "mid2b" + PREFIX);

        mids = createCategories(midNames, glossary.getGuid(), top2Guid);
        assertEquals(mids.size(), 2);
        mid2aGuid = mids.get(0).getGuid();
        mid2bGuid = mids.get(1).getGuid();

        List<String> leafNames = List.of("leaf1aa" + PREFIX, "leaf1ab" + PREFIX);

        List<GlossaryCategory> leaves = createCategories(leafNames, glossary.getGuid(), mid1aGuid);
        assertEquals(leaves.size(), 2);
        leaf1aaGuid = leaves.get(0).getGuid();
        leaf1abGuid = leaves.get(1).getGuid();

        leafNames = List.of("leaf1ba" + PREFIX, "leaf1bb" + PREFIX);

        leaves = createCategories(leafNames, glossary.getGuid(), mid1bGuid);
        assertEquals(leaves.size(), 2);
        leaf1baGuid = leaves.get(0).getGuid();
        leaf1bbGuid = leaves.get(1).getGuid();

        leafNames = List.of("leaf2aa" + PREFIX, "leaf2ab" + PREFIX);

        leaves = createCategories(leafNames, glossary.getGuid(), mid2aGuid);
        assertEquals(leaves.size(), 2);
        leaf2aaGuid = leaves.get(0).getGuid();
        leaf2abGuid = leaves.get(1).getGuid();

        leafNames = List.of("leaf2ba" + PREFIX, "leaf2bb" + PREFIX);

        leaves = createCategories(leafNames, glossary.getGuid(), mid2bGuid);
        assertEquals(leaves.size(), 2);
        leaf2baGuid = leaves.get(0).getGuid();
        leaf2bbGuid = leaves.get(1).getGuid();
    }

    @Test(
            groups = {"glossary.read.hierarchy"},
            dependsOnGroups = {"glossary.create.hierarchy"})
    void traverseHierarchy() throws AtlanException {
        Glossary glossary = Glossary.findByName(GLOSSARY_NAME, null);
        assertNotNull(glossary);
        Glossary.CategoryHierarchy tree = glossary.getHierarchy();
        assertNotNull(tree);
        List<IGlossaryCategory> dfs = tree.depthFirst();
        assertNotNull(dfs);
        assertEquals(dfs.size(), 14);
        List<String> names = dfs.stream().map(IGlossaryCategory::getName).collect(Collectors.toList());
        assertTrue(names.get(0).startsWith("top"));
        assertTrue(names.get(1).startsWith("mid"));
        assertTrue(names.get(2).startsWith("leaf"));
        assertTrue(names.get(3).startsWith("leaf"));
        assertTrue(names.get(4).startsWith("mid"));
        assertTrue(names.get(5).startsWith("leaf"));
        assertTrue(names.get(6).startsWith("leaf"));
        assertTrue(names.get(7).startsWith("top"));
        assertTrue(names.get(8).startsWith("mid"));
        assertTrue(names.get(9).startsWith("leaf"));
        assertTrue(names.get(10).startsWith("leaf"));
        assertTrue(names.get(11).startsWith("mid"));
        assertTrue(names.get(12).startsWith("leaf"));
        assertTrue(names.get(13).startsWith("leaf"));
        List<IGlossaryCategory> bfs = tree.breadthFirst();
        assertNotNull(bfs);
        assertEquals(bfs.size(), 14);
        names = bfs.stream().map(IGlossaryCategory::getName).collect(Collectors.toList());
        assertTrue(names.get(0).startsWith("top"));
        assertTrue(names.get(1).startsWith("top"));
        assertTrue(names.get(2).startsWith("mid"));
        assertTrue(names.get(3).startsWith("mid"));
        assertTrue(names.get(4).startsWith("mid"));
        assertTrue(names.get(5).startsWith("mid"));
        assertTrue(names.get(6).startsWith("leaf"));
        assertTrue(names.get(7).startsWith("leaf"));
        assertTrue(names.get(8).startsWith("leaf"));
        assertTrue(names.get(9).startsWith("leaf"));
        assertTrue(names.get(10).startsWith("leaf"));
        assertTrue(names.get(11).startsWith("leaf"));
        assertTrue(names.get(12).startsWith("leaf"));
        assertTrue(names.get(13).startsWith("leaf"));
        List<IGlossaryCategory> root = tree.getRootCategories();
        assertNotNull(root);
        assertEquals(root.size(), 2);
        names = root.stream().map(IGlossaryCategory::getName).collect(Collectors.toList());
        assertTrue(names.get(0).startsWith("top"));
        assertTrue(names.get(1).startsWith("top"));
    }

    @Test(
            groups = {"glossary.create.term"},
            dependsOnGroups = {"glossary.create.glossary"})
    void createTerm1() throws AtlanException {
        term1 = createTerm(TERM_NAME1, glossary.getGuid());
        assertEquals(term1.getName(), TERM_NAME1);
    }

    @Test(
            groups = {"glossary.create.term"},
            dependsOnGroups = {"glossary.create.glossary"})
    void createTerm2() throws AtlanException {
        term2 = createTerm(TERM_NAME2, glossary.getGuid());
        assertEquals(term2.getName(), TERM_NAME2);
    }

    @Test(
            groups = {"glossary.read.glossary"},
            dependsOnGroups = {"glossary.create.glossary", "glossary.create.hierarchy", "glossary.create.term"})
    void readGlossary() throws AtlanException {
        Glossary g = Glossary.get(glossary.getGuid());
        assertNotNull(g);
        assertTrue(g.isComplete());
        assertEquals(g.getGuid(), glossary.getGuid());
        assertEquals(g.getQualifiedName(), glossary.getQualifiedName());
        assertEquals(g.getName(), glossary.getName());
        Set<IGlossaryTerm> terms = g.getTerms();
        assertNotNull(terms);
        assertEquals(terms.size(), 2);
        Set<String> guids = terms.stream().map(IGlossaryTerm::getGuid).collect(Collectors.toSet());
        assertNotNull(guids);
        assertEquals(guids.size(), 2);
        assertTrue(guids.contains(term1.getGuid()));
        assertTrue(guids.contains(term2.getGuid()));
        Set<IGlossaryCategory> categories = g.getCategories();
        assertNotNull(categories);
        assertEquals(categories.size(), 14);
        guids = categories.stream().map(IGlossaryCategory::getGuid).collect(Collectors.toSet());
        assertEquals(guids.size(), 14);
    }

    @Test(
            groups = {"glossary.read.term"},
            dependsOnGroups = {"glossary.create.term"})
    void readTerm() throws AtlanException {
        GlossaryTerm term = GlossaryTerm.get(term1.getGuid());
        assertNotNull(term);
        assertTrue(term.isComplete());
        assertEquals(term.getGuid(), term1.getGuid());
        assertEquals(term.getQualifiedName(), term1.getQualifiedName());
        assertEquals(term.getName(), term1.getName());
        assertNotNull(term.getAnchor());
        assertEquals(term.getAnchor().getGuid(), glossary.getGuid());
    }

    @Test(
            groups = {"glossary.update.glossary"},
            dependsOnGroups = {"glossary.read.glossary"})
    void updateGlossary() throws AtlanException {
        Glossary g = Glossary.updater(glossary.getGuid(), GLOSSARY_NAME)
                .announcementType(ANNOUNCEMENT_TYPE)
                .announcementTitle(ANNOUNCEMENT_TITLE)
                .announcementMessage(ANNOUNCEMENT_MESSAGE)
                .build();
        AssetMutationResponse response = g.save();
        Asset one = validateSingleUpdate(response);
        assertTrue(one instanceof Glossary);
        g = (Glossary) one;
        assertEquals(g.getGuid(), glossary.getGuid());
        assertEquals(g.getQualifiedName(), glossary.getQualifiedName());
        assertEquals(g.getName(), glossary.getName());
        assertEquals(g.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(g.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(g.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        g = Glossary.updateCertificate(
                glossary.getQualifiedName(), GLOSSARY_NAME, CERTIFICATE_STATUS, CERTIFICATE_MESSAGE);
        assertEquals(g.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(g.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
    }

    @Test(
            groups = {"glossary.update.category"},
            dependsOnGroups = {"glossary.create.hierarchy"})
    void updateCategory() throws AtlanException {
        category = GlossaryCategory.get(leaf1baGuid);
        GlossaryCategory toUpdate = GlossaryCategory.updater(
                        category.getQualifiedName(),
                        category.getName(),
                        category.getAnchor().getGuid())
                .announcementType(ANNOUNCEMENT_TYPE)
                .announcementTitle(ANNOUNCEMENT_TITLE)
                .announcementMessage(ANNOUNCEMENT_MESSAGE)
                .build();
        AssetMutationResponse response = toUpdate.save();
        Asset one = validateSingleUpdate(response);
        assertTrue(one instanceof GlossaryCategory);
        GlossaryCategory c = (GlossaryCategory) one;
        assertEquals(c.getGuid(), category.getGuid());
        assertEquals(c.getQualifiedName(), category.getQualifiedName());
        assertEquals(c.getName(), category.getName());
        assertEquals(c.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(c.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(c.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        c = GlossaryCategory.updateCertificate(
                category.getQualifiedName(),
                category.getName(),
                glossary.getGuid(),
                CERTIFICATE_STATUS,
                CERTIFICATE_MESSAGE);
        assertEquals(c.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(c.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
    }

    @Test(
            groups = {"glossary.update.category.attributes"},
            dependsOnGroups = {"glossary.update.category"})
    void removeCategoryAttributes() throws AtlanException {
        GlossaryCategory toUpdate = GlossaryCategory.updater(
                        category.getQualifiedName(),
                        category.getName(),
                        category.getAnchor().getGuid())
                .removeAnnouncement()
                .build();
        AssetMutationResponse response = toUpdate.save();
        Asset one = validateSingleUpdate(response);
        assertTrue(one instanceof GlossaryCategory);
        GlossaryCategory c = (GlossaryCategory) one;
        assertEquals(c.getGuid(), category.getGuid());
        assertEquals(c.getQualifiedName(), category.getQualifiedName());
        assertEquals(c.getName(), category.getName());
        assertEquals(c.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(c.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        assertNull(c.getAnnouncementType());
        assertNull(c.getAnnouncementTitle());
        assertNull(c.getAnnouncementMessage());
    }

    @Test(
            groups = {"glossary.update.term"},
            dependsOnGroups = {"glossary.create.term", "glossary.create.hierarchy"})
    void updateTerm() throws AtlanException {
        GlossaryTerm term = GlossaryTerm.updater(term1.getQualifiedName(), term1.getName(), glossary.getGuid())
                .announcementType(ANNOUNCEMENT_TYPE)
                .announcementTitle(ANNOUNCEMENT_TITLE)
                .announcementMessage(ANNOUNCEMENT_MESSAGE)
                .category(GlossaryCategory.refByGuid(category.getGuid()))
                .build();
        AssetMutationResponse response = term.save();
        assertNotNull(response);
        assertEquals(response.getDeletedAssets().size(), 0);
        assertEquals(response.getCreatedAssets().size(), 0);
        List<Asset> entities = response.getUpdatedAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 2);

        Asset one = entities.get(0);
        assertTrue(one instanceof GlossaryTerm);
        term = (GlossaryTerm) one;
        assertEquals(term.getGuid(), term1.getGuid());
        assertEquals(term.getQualifiedName(), term1.getQualifiedName());
        assertEquals(term.getName(), term1.getName());
        assertEquals(term.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(term.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(term.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);

        one = entities.get(1);
        assertTrue(one instanceof GlossaryCategory);
        GlossaryCategory c = (GlossaryCategory) one;
        assertEquals(c.getGuid(), category.getGuid());
        assertEquals(c.getQualifiedName(), category.getQualifiedName());
        assertEquals(c.getName(), category.getName());
        term = GlossaryTerm.updateCertificate(
                term1.getQualifiedName(), term1.getName(), glossary.getGuid(), CERTIFICATE_STATUS, CERTIFICATE_MESSAGE);
        assertNotNull(term);
        assertEquals(term.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(term.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
    }

    @Test(
            groups = {"glossary.search.term"},
            dependsOnGroups = {"glossary.update.term"})
    void searchTerms() throws AtlanException {
        Query combined = CompoundQuery.builder()
                .must(beActive())
                .must(beOfType(GlossaryTerm.TYPE_NAME))
                .must(have(KeywordFields.NAME).eq(TERM_NAME1))
                .build()
                ._toQuery();

        IndexSearchRequest index = IndexSearchRequest.builder(IndexSearchDSL.builder(combined)
                        .size(100)
                        .aggregation("type", Aggregate.bucketBy(KeywordFields.TYPE_NAME))
                        .build())
                .attributes(Collections.singletonList("anchor"))
                .relationAttributes(Collections.singletonList("certificateStatus"))
                .build();

        IndexSearchResponse response = index.search();
        assertNotNull(response);
        assertNotNull(response.getAggregations());
        assertEquals(response.getAggregations().size(), 1);
        assertTrue(response.getAggregations().get("type") instanceof AggregationBucketResult);
        assertEquals(
                ((AggregationBucketResult) response.getAggregations().get("type"))
                        .getBuckets()
                        .size(),
                1);

        assertEquals(response.getApproximateCount().longValue(), 1L);
        List<Asset> entities = response.getAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 1);
        Asset one = entities.get(0);
        assertTrue(one instanceof GlossaryTerm);
        assertFalse(one.isComplete());
        GlossaryTerm term = (GlossaryTerm) one;
        assertEquals(term.getGuid(), term1.getGuid());
        assertEquals(term.getQualifiedName(), term1.getQualifiedName());
        assertNotNull(term.getAnchor());
        assertEquals(term.getAnchor().getTypeName(), Glossary.TYPE_NAME);
        assertEquals(term.getAnchor().getGuid(), glossary.getGuid());
    }

    @Test(
            groups = {"glossary.delete.term"},
            dependsOnGroups = {"glossary.create.*", "glossary.update.*", "glossary.read.*", "glossary.search.*"},
            alwaysRun = true)
    void deleteTerm1() throws AtlanException {
        AssetMutationResponse response = GlossaryTerm.delete(term1.getGuid());
        assertNotNull(response);
        assertEquals(response.getCreatedAssets().size(), 0);
        assertEquals(response.getUpdatedAssets().size(), 0);
        List<Asset> entities = response.getDeletedAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 1);
        Asset one = entities.get(0);
        assertTrue(one instanceof GlossaryTerm);
        GlossaryTerm term = (GlossaryTerm) one;
        assertEquals(term.getGuid(), term1.getGuid());
        assertEquals(term.getQualifiedName(), term1.getQualifiedName());
        assertEquals(term.getName(), term1.getName());
        assertEquals(term.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(term.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        assertEquals(term.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(term.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(term.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        assertEquals(term.getStatus(), AtlanStatus.DELETED);
        assertEquals(term.getDeleteHandler(), "SOFT");
    }

    @Test(
            groups = {"glossary.restore.term"},
            dependsOnGroups = {"glossary.delete.term"},
            alwaysRun = true)
    void restoreTerm1() throws AtlanException {
        assertTrue(GlossaryTerm.restore(term1.getQualifiedName()));
        GlossaryTerm term = GlossaryTerm.get(term1.getQualifiedName());
        assertNotNull(term);
        assertEquals(term.getGuid(), term1.getGuid());
        assertEquals(term.getQualifiedName(), term1.getQualifiedName());
        assertEquals(term.getName(), term1.getName());
        assertEquals(term.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(term.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        assertEquals(term.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(term.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(term.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        assertEquals(term.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"glossary.purge.term"},
            dependsOnGroups = {"glossary.restore.term"},
            alwaysRun = true)
    void purgeTerm1() throws AtlanException {
        GlossaryTerm term = deleteTerm(term1.getGuid());
        assertEquals(term.getQualifiedName(), term1.getQualifiedName());
        assertEquals(term.getName(), term1.getName());
        assertEquals(term.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(term.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        assertEquals(term.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(term.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(term.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        assertEquals(term.getStatus(), AtlanStatus.DELETED);
        assertEquals(term.getDeleteHandler(), "PURGE");
    }

    @Test(
            groups = {"glossary.purge.term"},
            dependsOnGroups = {"glossary.restore.term"},
            alwaysRun = true)
    void purgeTerm2() throws AtlanException {
        GlossaryTerm term = deleteTerm(term2.getGuid());
        assertEquals(term.getQualifiedName(), term2.getQualifiedName());
        assertEquals(term.getName(), term2.getName());
        assertNull(term.getCertificateStatus());
        assertNull(term.getCertificateStatusMessage());
        assertNull(term.getAnnouncementType());
        assertNull(term.getAnnouncementTitle());
        assertNull(term.getAnnouncementMessage());
        assertEquals(term.getStatus(), AtlanStatus.DELETED);
        assertEquals(term.getDeleteHandler(), "PURGE");
    }

    @Test(
            groups = {"glossary.purge.category"},
            dependsOnGroups = {"glossary.purge.term"},
            alwaysRun = true)
    void purgeCategory() throws AtlanException {
        GlossaryCategory c = deleteCategory(category.getGuid());
        assertEquals(c.getQualifiedName(), category.getQualifiedName());
        assertEquals(c.getName(), category.getName());
        assertEquals(c.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(c.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        assertNull(c.getAnnouncementType());
        assertNull(c.getAnnouncementTitle());
        assertNull(c.getAnnouncementMessage());
        assertEquals(c.getStatus(), AtlanStatus.DELETED);
        assertEquals(c.getDeleteHandler(), "PURGE");
    }

    @Test(
            groups = {"glossary.purge.hierarchy"},
            dependsOnGroups = {"glossary.purge.category", "glossary.purge.term"},
            alwaysRun = true)
    void purgeHierarchy() throws AtlanException {
        AssetMutationResponse response = Atlan.getDefaultClient()
                .assets
                .delete(
                        List.of(
                                leaf1aaGuid,
                                leaf1abGuid,
                                leaf1bbGuid,
                                leaf2aaGuid,
                                leaf2abGuid,
                                leaf2baGuid,
                                leaf2bbGuid),
                        AtlanDeleteType.PURGE);
        assertNotNull(response);
        assertEquals(response.getCreatedAssets().size(), 0);
        assertEquals(response.getUpdatedAssets().size(), 0);
        List<Asset> entities = response.getDeletedAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 7);

        response = Atlan.getDefaultClient()
                .assets
                .delete(List.of(mid1aGuid, mid1bGuid, mid2aGuid, mid2bGuid), AtlanDeleteType.PURGE);
        assertNotNull(response);
        assertEquals(response.getCreatedAssets().size(), 0);
        assertEquals(response.getUpdatedAssets().size(), 0);
        entities = response.getDeletedAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 4);

        response = Atlan.getDefaultClient().assets.delete(List.of(top1Guid, top2Guid), AtlanDeleteType.PURGE);
        assertNotNull(response);
        assertEquals(response.getCreatedAssets().size(), 0);
        assertEquals(response.getUpdatedAssets().size(), 0);
        entities = response.getDeletedAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 2);
    }

    @Test(
            groups = {"glossary.purge.glossary"},
            dependsOnGroups = {"glossary.purge.category", "glossary.purge.hierarchy"},
            alwaysRun = true)
    void purgeGlossary() throws AtlanException {
        Glossary g = deleteGlossary(glossary.getGuid());
        assertEquals(g.getQualifiedName(), glossary.getQualifiedName());
        assertEquals(g.getName(), glossary.getName());
        assertEquals(g.getCertificateStatus(), CertificateStatus.VERIFIED);
        assertEquals(g.getAnnouncementType(), AtlanAnnouncementType.INFORMATION);
        assertEquals(g.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(g.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        assertEquals(g.getStatus(), AtlanStatus.DELETED);
        assertEquals(g.getDeleteHandler(), "PURGE");
    }
}
