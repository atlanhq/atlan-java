/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.*;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.assets.*;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.*;
import com.atlan.model.relations.Reference;
import com.atlan.model.search.*;
import java.util.ArrayList;
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
    private static final String TERM_NAME3 = PREFIX + " Term3";
    private static final String TERM_NAME4 = PREFIX + " Term4";

    private static Glossary glossary = null;
    private static GlossaryCategory category = null;
    private static GlossaryTerm term1 = null;
    private static GlossaryTerm term2 = null;
    private static GlossaryTerm term3 = null;
    private static GlossaryTerm term4 = null;

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
        assertEquals(response.getMutation(glossary), AssetMutationResponse.MutationType.CREATED);
        Glossary result = response.getResult(glossary);
        assertNotNull(result);
        Glossary created = response.getCreatedAssets(Glossary.class).get(0);
        assertNotNull(created.getGuid());
        assertNotNull(created.getQualifiedName());
        assertEquals(created.getName(), name);
        assertNotEquals(created.getQualifiedName(), name);
        assertEquals(created, result);
        return created;
    }

    /**
     * Create a new glossary term with a unique name.
     *
     * @param client connectivity to the Atlan tenant in which to create the term
     * @param name to make the glossary term unique
     * @param glossary in which to create the term
     * @return the glossary term that was created
     * @throws AtlanException on any error creating or reading-back the glossary term
     */
    static GlossaryTerm createTerm(AtlanClient client, String name, Glossary glossary) throws AtlanException {
        assertThrows(
                NotFoundException.class,
                () -> GlossaryTerm.creator(name, glossary).build().updateMergingCM(client));
        GlossaryTerm term = GlossaryTerm.creator(name, glossary).build();
        AssetMutationResponse response = term.save(client);
        assertNotNull(response);
        assertEquals(response.getDeletedAssets().size(), 0);
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof Glossary);
        Glossary updated = response.getUpdatedAssets(Glossary.class).get(0);
        assertEquals(updated.getGuid(), glossary.getGuid());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertNotNull(one);
        assertTrue(one instanceof GlossaryTerm);
        term = (GlossaryTerm) one;
        assertNotNull(term.getGuid());
        assertNotNull(term.getQualifiedName());
        assertEquals(term.getName(), name);
        assertNotEquals(term.getQualifiedName(), name);
        return term.toBuilder().anchor(glossary.trimToReference()).build();
    }

    /**
     * Create a new category within the glossary.
     *
     * @param client connectivity to the Atlan tenant in which to create the term
     * @param name of the category to create
     * @param glossary in which to create the category
     * @return the created category
     * @throws AtlanException on any errors creating the category
     */
    static GlossaryCategory createCategory(AtlanClient client, String name, Glossary glossary) throws AtlanException {
        GlossaryCategory category = GlossaryCategory.creator(name, glossary).build();
        AssetMutationResponse response = category.save(client);
        assertNotNull(response);
        assertEquals(response.getCreatedAssets().size(), 1);
        assertEquals(response.getUpdatedAssets().size(), 1);
        assertEquals(response.getDeletedAssets().size(), 0);
        assertEquals(response.getUpdatedAssets().get(0).getGuid(), glossary.getGuid());
        Asset one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof GlossaryCategory);
        category = (GlossaryCategory) one;
        assertNotNull(category.getGuid());
        assertNotNull(category.getQualifiedName());
        assertEquals(category.getName(), name);
        assertNotEquals(category.getQualifiedName(), name);
        return category.toBuilder().anchor(glossary.trimToReference()).build();
    }

    /**
     * Delete (purge) the glossary with the provided GUID.
     *
     * @param client connectivity to the Atlan tenant in which to create the term
     * @param guid of the glossary to purge
     * @return the purged glossary
     * @throws AtlanException on any errors purging the glossary
     */
    static Glossary deleteGlossary(AtlanClient client, String guid) throws AtlanException {
        AssetMutationResponse response = Glossary.purge(client, guid).block();
        assertNotNull(response);
        assertEquals(response.getCreatedAssets().size(), 0);
        assertEquals(response.getUpdatedAssets().size(), 0);
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof Glossary);
        Glossary deletedGlossary = response.getDeletedAssets(Glossary.class).get(0);
        assertEquals(deletedGlossary.getGuid(), guid);
        assertEquals(deletedGlossary.getStatus(), AtlanStatus.DELETED);
        return deletedGlossary;
    }

    /**
     * Delete (purge) the category with the provided GUID.
     *
     * @param client connectivity to the Atlan tenant in which to create the term
     * @param guid of the category to purge
     * @return the purged category
     * @throws AtlanException on any errors purging the category
     */
    static GlossaryCategory deleteCategory(AtlanClient client, String guid) throws AtlanException {
        AssetMutationResponse response = GlossaryCategory.purge(client, guid).block();
        assertNotNull(response);
        assertEquals(response.getCreatedAssets().size(), 0);
        assertEquals(response.getUpdatedAssets().size(), 0);
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof GlossaryCategory);
        GlossaryCategory deletedCategory =
                response.getDeletedAssets(GlossaryCategory.class).get(0);
        assertEquals(deletedCategory.getGuid(), guid);
        assertEquals(deletedCategory.getStatus(), AtlanStatus.DELETED);
        return deletedCategory;
    }

    /**
     * Delete (purge) the term with the provided GUID.
     *
     * @param client connectivity to the Atlan tenant in which to create the term
     * @param guid of the term to purge
     * @return the purged term
     * @throws AtlanException on any errors purging the term
     */
    static GlossaryTerm deleteTerm(AtlanClient client, String guid) throws AtlanException {
        AssetMutationResponse response = GlossaryTerm.purge(client, guid).block();
        assertNotNull(response);
        assertEquals(response.getCreatedAssets().size(), 0);
        assertEquals(response.getUpdatedAssets().size(), 0);
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof GlossaryTerm);
        GlossaryTerm deletedTerm = response.getDeletedAssets(GlossaryTerm.class).get(0);
        assertEquals(deletedTerm.getGuid(), guid);
        assertEquals(deletedTerm.getStatus(), AtlanStatus.DELETED);
        return deletedTerm;
    }

    @Test(groups = {"glossary.create.glossary"})
    void createGlossary() throws AtlanException {
        glossary = createGlossary(client, GLOSSARY_NAME);
    }

    @Test(
            groups = {"glossary.create.hierarchy"},
            dependsOnGroups = {"glossary.create.glossary"})
    void createHierarchy() throws AtlanException {
        List<Asset> categories = new ArrayList<>();
        GlossaryCategory top1 =
                GlossaryCategory.creator("top1" + PREFIX, glossary).build();
        GlossaryCategory top2 =
                GlossaryCategory.creator("top2" + PREFIX, glossary).build();

        categories.add(top1);
        categories.add(top2);

        AssetMutationResponse response = client.assets.save(categories);
        assertNotNull(response);
        assertEquals(response.getDeletedAssets().size(), 0);
        assertEquals(response.getUpdatedAssets().size(), 1);
        assertEquals(response.getUpdatedAssets(Glossary.class).size(), 1);
        assertEquals(response.getUpdatedAssets(Glossary.class).get(0).getGuid(), glossary.getGuid());
        assertEquals(response.getCreatedAssets().size(), categories.size());
        List<GlossaryCategory> entities = response.getCreatedAssets(GlossaryCategory.class);
        assertEquals(entities.size(), categories.size());
        top1Guid = response.getAssignedGuid(top1);
        top2Guid = response.getAssignedGuid(top2);

        categories = new ArrayList<>();

        GlossaryCategory mid1a = GlossaryCategory.creator("mid1a" + PREFIX, glossary)
                .parentCategory(GlossaryCategory.refByGuid(top1Guid))
                .build();
        GlossaryCategory mid1b = GlossaryCategory.creator("mid1b" + PREFIX, glossary)
                .parentCategory(GlossaryCategory.refByGuid(top1Guid))
                .build();
        GlossaryCategory mid2a = GlossaryCategory.creator("mid2a" + PREFIX, glossary)
                .parentCategory(GlossaryCategory.refByGuid(top2Guid))
                .build();
        GlossaryCategory mid2b = GlossaryCategory.creator("mid2b" + PREFIX, glossary)
                .parentCategory(GlossaryCategory.refByGuid(top2Guid))
                .build();
        categories.add(mid1a);
        categories.add(mid1b);
        categories.add(mid2a);
        categories.add(mid2b);

        response = client.assets.save(categories);
        assertNotNull(response);
        assertEquals(response.getDeletedAssets().size(), 0);
        assertEquals(response.getUpdatedAssets().size(), 3);
        assertEquals(response.getUpdatedAssets(Glossary.class).size(), 1);
        assertEquals(response.getUpdatedAssets(Glossary.class).get(0).getGuid(), glossary.getGuid());
        assertEquals(response.getUpdatedAssets(GlossaryCategory.class).size(), 2);
        assertEquals(response.getCreatedAssets().size(), categories.size());
        entities = response.getCreatedAssets(GlossaryCategory.class);
        assertEquals(entities.size(), categories.size());
        mid1aGuid = response.getAssignedGuid(mid1a);
        mid1bGuid = response.getAssignedGuid(mid1b);
        mid2aGuid = response.getAssignedGuid(mid2a);
        mid2bGuid = response.getAssignedGuid(mid2b);

        categories = new ArrayList<>();

        categories.add(GlossaryCategory.creator("leaf1aa" + PREFIX, glossary)
                .parentCategory(GlossaryCategory.refByGuid(mid1aGuid))
                .build());
        categories.add(GlossaryCategory.creator("leaf1ab" + PREFIX, glossary)
                .parentCategory(GlossaryCategory.refByGuid(mid1aGuid))
                .build());
        categories.add(GlossaryCategory.creator("leaf1ba" + PREFIX, glossary)
                .parentCategory(GlossaryCategory.refByGuid(mid1bGuid))
                .build());
        categories.add(GlossaryCategory.creator("leaf1bb" + PREFIX, glossary)
                .parentCategory(GlossaryCategory.refByGuid(mid1bGuid))
                .build());
        categories.add(GlossaryCategory.creator("leaf2aa" + PREFIX, glossary)
                .parentCategory(GlossaryCategory.refByGuid(mid2aGuid))
                .build());
        categories.add(GlossaryCategory.creator("leaf2ab" + PREFIX, glossary)
                .parentCategory(GlossaryCategory.refByGuid(mid2aGuid))
                .build());
        categories.add(GlossaryCategory.creator("leaf2ba" + PREFIX, glossary)
                .parentCategory(GlossaryCategory.refByGuid(mid2bGuid))
                .build());
        categories.add(GlossaryCategory.creator("leaf2bb" + PREFIX, glossary)
                .parentCategory(GlossaryCategory.refByGuid(mid2bGuid))
                .build());

        response = client.assets.save(categories);
        assertNotNull(response);
        assertEquals(response.getDeletedAssets().size(), 0);
        assertEquals(response.getUpdatedAssets().size(), 5);
        assertEquals(response.getUpdatedAssets(Glossary.class).size(), 1);
        assertEquals(response.getUpdatedAssets(Glossary.class).get(0).getGuid(), glossary.getGuid());
        assertEquals(response.getUpdatedAssets(GlossaryCategory.class).size(), 4);
        assertEquals(response.getCreatedAssets().size(), categories.size());
        entities = response.getCreatedAssets(GlossaryCategory.class);
        assertEquals(entities.size(), categories.size());
        leaf1aaGuid = entities.get(0).getGuid();
        leaf1abGuid = entities.get(1).getGuid();
        leaf1baGuid = entities.get(2).getGuid();
        leaf1bbGuid = entities.get(3).getGuid();
        leaf2aaGuid = entities.get(4).getGuid();
        leaf2abGuid = entities.get(5).getGuid();
        leaf2baGuid = entities.get(6).getGuid();
        leaf2bbGuid = entities.get(7).getGuid();
    }

    @Test(
            groups = {"glossary.read.hierarchy"},
            dependsOnGroups = {"glossary.create.hierarchy"})
    void traverseHierarchy() throws AtlanException {
        Glossary glossary = Glossary.findByName(client, GLOSSARY_NAME);
        assertNotNull(glossary);
        Glossary.CategoryHierarchy tree = glossary.getHierarchy(client);
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
        term1 = createTerm(client, TERM_NAME1, glossary);
        assertEquals(term1.getName(), TERM_NAME1);
    }

    @Test(
            groups = {"glossary.create.term"},
            dependsOnGroups = {"glossary.create.glossary"})
    void createTerm2() throws AtlanException {
        term2 = createTerm(client, TERM_NAME2, glossary);
        assertEquals(term2.getName(), TERM_NAME2);
    }

    @Test(
            groups = {"glossary.create.term"},
            dependsOnGroups = {"glossary.create.glossary"})
    void createTerm3() throws AtlanException {
        term3 = createTerm(client, TERM_NAME3, glossary);
        assertEquals(term3.getName(), TERM_NAME3);
    }

    @Test(
            groups = {"glossary.create.term"},
            dependsOnGroups = {"glossary.create.glossary"})
    void createTerm4() throws AtlanException {
        term4 = createTerm(client, TERM_NAME4, glossary);
        assertEquals(term4.getName(), TERM_NAME4);
    }

    @Test(
            groups = {"glossary.read.glossary"},
            dependsOnGroups = {"glossary.create.glossary", "glossary.create.hierarchy", "glossary.create.term"})
    void readGlossary() throws AtlanException {
        Glossary g = Glossary.get(client, glossary.getGuid(), true);
        assertNotNull(g);
        assertTrue(g.isComplete());
        assertEquals(g.getGuid(), glossary.getGuid());
        assertEquals(g.getQualifiedName(), glossary.getQualifiedName());
        assertEquals(g.getName(), glossary.getName());
        Set<IGlossaryTerm> terms = g.getTerms();
        assertNotNull(terms);
        assertEquals(terms.size(), 4);
        Set<String> guids = terms.stream().map(IGlossaryTerm::getGuid).collect(Collectors.toSet());
        assertNotNull(guids);
        assertEquals(guids.size(), 4);
        assertTrue(guids.contains(term1.getGuid()));
        assertTrue(guids.contains(term2.getGuid()));
        assertTrue(guids.contains(term3.getGuid()));
        assertTrue(guids.contains(term4.getGuid()));
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
        GlossaryTerm term = GlossaryTerm.get(client, term1.getGuid(), true);
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
        AssetMutationResponse response = g.save(client);
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
                client, glossary.getQualifiedName(), GLOSSARY_NAME, CERTIFICATE_STATUS, CERTIFICATE_MESSAGE);
        assertEquals(g.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(g.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
    }

    @Test(
            groups = {"glossary.update.category"},
            dependsOnGroups = {"glossary.create.hierarchy"})
    void updateCategory() throws AtlanException {
        category = GlossaryCategory.get(client, leaf1baGuid, true);
        List<GlossaryCategory> list = GlossaryCategory.findByName(client, "leaf1ba" + PREFIX, GLOSSARY_NAME);
        assertNotNull(list);
        assertEquals(list.size(), 1);
        assertEquals(category.getGuid(), list.get(0).getGuid());
        GlossaryCategory toUpdate = GlossaryCategory.updater(
                        category.getQualifiedName(),
                        category.getName(),
                        category.getAnchor().getGuid())
                .announcementType(ANNOUNCEMENT_TYPE)
                .announcementTitle(ANNOUNCEMENT_TITLE)
                .announcementMessage(ANNOUNCEMENT_MESSAGE)
                .build();
        AssetMutationResponse response = toUpdate.save(client);
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
                client,
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
        AssetMutationResponse response = toUpdate.save(client);
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
        AssetMutationResponse response = term.updateMergingCM(client);
        assertNotNull(response);
        assertEquals(response.getDeletedAssets().size(), 0);
        assertEquals(response.getCreatedAssets().size(), 0);
        List<Asset> entities = response.getUpdatedAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 2);

        List<GlossaryTerm> terms = response.getUpdatedAssets(GlossaryTerm.class);
        assertNotNull(terms);
        assertEquals(terms.size(), 1);
        assertEquals(terms.get(0).getGuid(), term1.getGuid());
        GlossaryTerm result = response.getResult(term);
        assertNotNull(result);
        assertEquals(result.getGuid(), term1.getGuid());
        assertEquals(response.getMutation(term), AssetMutationResponse.MutationType.UPDATED);

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
                client,
                term1.getQualifiedName(),
                term1.getName(),
                glossary.getGuid(),
                CERTIFICATE_STATUS,
                CERTIFICATE_MESSAGE);
        assertNotNull(term);
        assertEquals(term.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(term.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
    }

    @Test(
            groups = {"glossary.search.term"},
            dependsOnGroups = {"glossary.update.term"})
    void searchTerms() throws AtlanException {

        IndexSearchRequest index = GlossaryTerm.select(client)
                .where(GlossaryTerm.NAME.eq(TERM_NAME1))
                .pageSize(100)
                .aggregate("type", Asset.TYPE_NAME.bucketBy())
                .includeOnResults(GlossaryTerm.ANCHOR)
                .includeOnRelations(Asset.CERTIFICATE_STATUS)
                .toRequest();

        IndexSearchResponse response = index.search(client);
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

    // TODO: Add tests for term-to-term relationships:
    //  - append
    //  - remove
    //  - replace
    @Test(
            groups = {"glossary.update.term.createRelationship"},
            dependsOnGroups = {"glossary.search.term"})
    void createRelationships() throws AtlanException {
        GlossaryTerm term = GlossaryTerm.updater(term1.getQualifiedName(), term1.getName(), glossary.getGuid())
                .seeAlsoOne(GlossaryTerm.refByGuid(term2.getGuid()))
                .seeAlsoOne(GlossaryTerm.refByGuid(term3.getGuid()))
                .build();
        AssetMutationResponse response = term.save(client);
        assertNotNull(response);
        GlossaryTerm result = GlossaryTerm.get(client, term1.getGuid(), true);
        assertNotNull(result);
        assertNotNull(result.getSeeAlso());
        assertEquals(result.getSeeAlso().size(), 2);
        Set<String> relatedGuids =
                result.getSeeAlso().stream().map(IGlossaryTerm::getGuid).collect(Collectors.toSet());
        assertEquals(relatedGuids.size(), 2);
        assertTrue(relatedGuids.contains(term2.getGuid()));
        assertTrue(relatedGuids.contains(term3.getGuid()));
    }

    @Test(
            groups = {"glossary.update.term.removeRelationship"},
            dependsOnGroups = {"glossary.update.term.createRelationship"})
    void removeRelationship() throws AtlanException {
        GlossaryTerm term = GlossaryTerm.updater(term1.getQualifiedName(), term1.getName(), glossary.getGuid())
                .seeAlsoOne(GlossaryTerm.refByGuid(term2.getGuid(), Reference.SaveSemantic.REMOVE))
                .build();
        AssetMutationResponse response = term.save(client);
        assertNotNull(response);
        GlossaryTerm result = GlossaryTerm.get(client, term1.getGuid(), true);
        assertNotNull(result);
        assertNotNull(result.getSeeAlso());
        List<IGlossaryTerm> activeRelationships = result.getSeeAlso().stream()
                .filter(r -> r.getRelationshipStatus() == AtlanStatus.ACTIVE)
                .toList();
        assertEquals(activeRelationships.size(), 1);
        assertEquals(activeRelationships.get(0).getGuid(), term3.getGuid());
    }

    @Test(
            groups = {"glossary.update.term.appendRelationship"},
            dependsOnGroups = {"glossary.update.term.removeRelationship"})
    void appendRelationship() throws AtlanException {
        GlossaryTerm term = GlossaryTerm.updater(term1.getQualifiedName(), term1.getName(), glossary.getGuid())
                .seeAlsoOne(GlossaryTerm.refByGuid(term4.getGuid(), Reference.SaveSemantic.APPEND))
                .build();
        AssetMutationResponse response = term.save(client);
        assertNotNull(response);
        GlossaryTerm result = GlossaryTerm.get(client, term1.getGuid(), true);
        assertNotNull(result);
        assertNotNull(result.getSeeAlso());
        Set<IGlossaryTerm> activeRelationships = result.getSeeAlso().stream()
                .filter(r -> r.getRelationshipStatus() == AtlanStatus.ACTIVE)
                .collect(Collectors.toSet());
        assertEquals(activeRelationships.size(), 2);
        Set<String> relatedGuids =
                activeRelationships.stream().map(IGlossaryTerm::getGuid).collect(Collectors.toSet());
        assertEquals(relatedGuids.size(), 2);
        assertTrue(relatedGuids.contains(term3.getGuid()));
        assertTrue(relatedGuids.contains(term4.getGuid()));
    }

    @Test(
            groups = {"glossary.update.term.appendRelationship2"},
            dependsOnGroups = {"glossary.update.term.appendRelationship"})
    void appendRelationshipAgain() throws AtlanException {
        GlossaryTerm term = GlossaryTerm.updater(term1.getQualifiedName(), term1.getName(), glossary.getGuid())
                .seeAlsoOne(GlossaryTerm.refByGuid(term4.getGuid(), Reference.SaveSemantic.APPEND))
                .build();
        AssetMutationResponse response = term.save(client);
        assertNotNull(response);
        GlossaryTerm result = GlossaryTerm.get(client, term1.getGuid(), true);
        assertNotNull(result);
        assertNotNull(result.getSeeAlso());
        Set<IGlossaryTerm> activeRelationships = result.getSeeAlso().stream()
                .filter(r -> r.getRelationshipStatus() == AtlanStatus.ACTIVE)
                .collect(Collectors.toSet());
        assertEquals(activeRelationships.size(), 2);
        Set<String> relatedGuids =
                activeRelationships.stream().map(IGlossaryTerm::getGuid).collect(Collectors.toSet());
        assertEquals(relatedGuids.size(), 2);
        assertTrue(relatedGuids.contains(term3.getGuid()));
        assertTrue(relatedGuids.contains(term4.getGuid()));
    }

    @Test(
            groups = {"glossary.update.term.removeRelationship2"},
            dependsOnGroups = {"glossary.update.term.appendRelationship2"})
    void removeUnrelatedRelationship() {
        GlossaryTerm term = GlossaryTerm.updater(term1.getQualifiedName(), term1.getName(), glossary.getGuid())
                .seeAlsoOne(GlossaryTerm.refByGuid(term2.getGuid(), Reference.SaveSemantic.REMOVE))
                .build();
        assertThrows(NotFoundException.class, () -> term.save(client));
    }

    @Test(
            groups = {"glossary.delete.term"},
            dependsOnGroups = {"glossary.create.*", "glossary.update.*", "glossary.read.*", "glossary.search.*"},
            alwaysRun = true)
    void deleteTerm1() throws AtlanException {
        AssetMutationResponse response =
                GlossaryTerm.delete(client, term1.getGuid()).block();
        assertNotNull(response);
        assertEquals(response.getCreatedAssets().size(), 0);
        assertEquals(response.getUpdatedAssets().size(), 0);
        List<Asset> entities = response.getDeletedAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 1);
        Asset one = entities.get(0);
        assertTrue(one instanceof GlossaryTerm);
        GlossaryTerm term = response.getDeletedAssets(GlossaryTerm.class).get(0);
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
        assertTrue(GlossaryTerm.restore(client, term1.getQualifiedName()));
        GlossaryTerm term = GlossaryTerm.get(client, term1.getQualifiedName());
        assertFalse(term.isComplete());
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
        GlossaryTerm term = deleteTerm(client, term1.getGuid());
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
        GlossaryTerm term = deleteTerm(client, term2.getGuid());
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
            groups = {"glossary.purge.term"},
            dependsOnGroups = {"glossary.restore.term"},
            alwaysRun = true)
    void purgeTerm3() throws AtlanException {
        GlossaryTerm term = deleteTerm(client, term3.getGuid());
        assertEquals(term.getQualifiedName(), term3.getQualifiedName());
        assertEquals(term.getName(), term3.getName());
        assertNull(term.getCertificateStatus());
        assertNull(term.getCertificateStatusMessage());
        assertNull(term.getAnnouncementType());
        assertNull(term.getAnnouncementTitle());
        assertNull(term.getAnnouncementMessage());
        assertEquals(term.getStatus(), AtlanStatus.DELETED);
        assertEquals(term.getDeleteHandler(), "PURGE");
    }

    @Test(
            groups = {"glossary.purge.term"},
            dependsOnGroups = {"glossary.restore.term"},
            alwaysRun = true)
    void purgeTerm4() throws AtlanException {
        GlossaryTerm term = deleteTerm(client, term4.getGuid());
        assertEquals(term.getQualifiedName(), term4.getQualifiedName());
        assertEquals(term.getName(), term4.getName());
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
        GlossaryCategory c = deleteCategory(client, category.getGuid());
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
        AssetMutationResponse response = client.assets
                .delete(
                        List.of(
                                leaf1aaGuid,
                                leaf1abGuid,
                                leaf1bbGuid,
                                leaf2aaGuid,
                                leaf2abGuid,
                                leaf2baGuid,
                                leaf2bbGuid),
                        AtlanDeleteType.PURGE)
                .block();
        assertNotNull(response);
        assertEquals(response.getCreatedAssets().size(), 0);
        assertEquals(response.getUpdatedAssets().size(), 0);
        List<Asset> entities = response.getDeletedAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 7);

        response = client.assets
                .delete(List.of(mid1aGuid, mid1bGuid, mid2aGuid, mid2bGuid), AtlanDeleteType.PURGE)
                .block();
        assertNotNull(response);
        assertEquals(response.getCreatedAssets().size(), 0);
        assertEquals(response.getUpdatedAssets().size(), 0);
        entities = response.getDeletedAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 4);

        response = client.assets
                .delete(List.of(top1Guid, top2Guid), AtlanDeleteType.PURGE)
                .block();
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
        Glossary g = deleteGlossary(client, glossary.getGuid());
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
