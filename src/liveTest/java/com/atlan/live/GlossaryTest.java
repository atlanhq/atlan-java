/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.api.EntityBulkEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.Glossary;
import com.atlan.model.assets.GlossaryCategory;
import com.atlan.model.assets.GlossaryTerm;
import com.atlan.model.core.Entity;
import com.atlan.model.core.EntityMutationResponse;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanDeleteType;
import com.atlan.model.enums.AtlanStatus;
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

    private static final String TRAVERSE_GLOSSARY_NAME = "JavaClient Traversable";
    private static String traverseGlossaryGuid = null;
    private static String traverseGlossaryQame = null;
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
            assertNotEquals(glossaryQame, GLOSSARY_NAME);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception: " + e.getMessage());
        }
        glossary = Glossary.creator(TRAVERSE_GLOSSARY_NAME).build();
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
            traverseGlossaryGuid = glossary.getGuid();
            assertNotNull(traverseGlossaryGuid);
            traverseGlossaryQame = glossary.getQualifiedName();
            assertNotNull(traverseGlossaryQame);
            assertEquals(glossary.getName(), TRAVERSE_GLOSSARY_NAME);
            assertNotEquals(traverseGlossaryQame, TRAVERSE_GLOSSARY_NAME);
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
            groups = {"create.hierarchy"},
            dependsOnGroups = {"create.category"})
    void createHierarchy() {
        try {

            GlossaryCategory top1 =
                    GlossaryCategory.creator("top1", traverseGlossaryGuid, null).build();
            GlossaryCategory top2 =
                    GlossaryCategory.creator("top2", traverseGlossaryGuid, null).build();
            EntityMutationResponse response = EntityBulkEndpoint.upsert(List.of(top1, top2), false, false);
            assertNotNull(response);
            assertEquals(response.getDeletedEntities().size(), 0);
            assertEquals(response.getCreatedEntities().size(), 2);
            List<Entity> entities = response.getCreatedEntities();
            for (Entity created : entities) {
                if (created instanceof GlossaryCategory) {
                    String name = ((GlossaryCategory) created).getName();
                    if (name.equals("top1")) {
                        top1Guid = created.getGuid();
                    } else if (name.equals("top2")) {
                        top2Guid = created.getGuid();
                    }
                }
            }

            GlossaryCategory mid1a = GlossaryCategory.creator("mid1a", traverseGlossaryGuid, null)
                    .parentCategory(GlossaryCategory.refByGuid(top1Guid))
                    .build();
            GlossaryCategory mid1b = GlossaryCategory.creator("mid1b", traverseGlossaryGuid, null)
                    .parentCategory(GlossaryCategory.refByGuid(top1Guid))
                    .build();
            GlossaryCategory mid2a = GlossaryCategory.creator("mid2a", traverseGlossaryGuid, null)
                    .parentCategory(GlossaryCategory.refByGuid(top2Guid))
                    .build();
            GlossaryCategory mid2b = GlossaryCategory.creator("mid2b", traverseGlossaryGuid, null)
                    .parentCategory(GlossaryCategory.refByGuid(top2Guid))
                    .build();
            response = EntityBulkEndpoint.upsert(List.of(mid1a, mid1b, mid2a, mid2b), false, false);
            assertNotNull(response);
            assertEquals(response.getDeletedEntities().size(), 0);
            assertEquals(response.getCreatedEntities().size(), 4);
            entities = response.getCreatedEntities();
            for (Entity created : entities) {
                if (created instanceof GlossaryCategory) {
                    String name = ((GlossaryCategory) created).getName();
                    if (name.equals("mid1a")) {
                        mid1aGuid = created.getGuid();
                    } else if (name.equals("mid1b")) {
                        mid1bGuid = created.getGuid();
                    } else if (name.equals("mid2a")) {
                        mid2aGuid = created.getGuid();
                    } else if (name.equals("mid2b")) {
                        mid2bGuid = created.getGuid();
                    }
                }
            }

            GlossaryCategory leaf1aa = GlossaryCategory.creator("leaf1aa", traverseGlossaryGuid, null)
                    .parentCategory(GlossaryCategory.refByGuid(mid1aGuid))
                    .build();
            GlossaryCategory leaf1ab = GlossaryCategory.creator("leaf1ab", traverseGlossaryGuid, null)
                    .parentCategory(GlossaryCategory.refByGuid(mid1aGuid))
                    .build();
            GlossaryCategory leaf1ba = GlossaryCategory.creator("leaf1ba", traverseGlossaryGuid, null)
                    .parentCategory(GlossaryCategory.refByGuid(mid1bGuid))
                    .build();
            GlossaryCategory leaf1bb = GlossaryCategory.creator("leaf1bb", traverseGlossaryGuid, null)
                    .parentCategory(GlossaryCategory.refByGuid(mid1bGuid))
                    .build();
            GlossaryCategory leaf2aa = GlossaryCategory.creator("leaf2aa", traverseGlossaryGuid, null)
                    .parentCategory(GlossaryCategory.refByGuid(mid2aGuid))
                    .build();
            GlossaryCategory leaf2ab = GlossaryCategory.creator("leaf2ab", traverseGlossaryGuid, null)
                    .parentCategory(GlossaryCategory.refByGuid(mid2aGuid))
                    .build();
            GlossaryCategory leaf2ba = GlossaryCategory.creator("leaf2ba", traverseGlossaryGuid, null)
                    .parentCategory(GlossaryCategory.refByGuid(mid2bGuid))
                    .build();
            GlossaryCategory leaf2bb = GlossaryCategory.creator("leaf2bb", traverseGlossaryGuid, null)
                    .parentCategory(GlossaryCategory.refByGuid(mid2bGuid))
                    .build();
            response = EntityBulkEndpoint.upsert(
                    List.of(leaf1aa, leaf1ab, leaf1ba, leaf1bb, leaf2aa, leaf2ab, leaf2ba, leaf2bb), false, false);
            assertNotNull(response);
            assertEquals(response.getDeletedEntities().size(), 0);
            assertEquals(response.getCreatedEntities().size(), 8);
            entities = response.getCreatedEntities();
            for (Entity created : entities) {
                if (created instanceof GlossaryCategory) {
                    String name = ((GlossaryCategory) created).getName();
                    if (name.equals("leaf1aa")) {
                        leaf1aaGuid = created.getGuid();
                    } else if (name.equals("leaf1ab")) {
                        leaf1abGuid = created.getGuid();
                    } else if (name.equals("leaf1ba")) {
                        leaf1baGuid = created.getGuid();
                    } else if (name.equals("leaf1bb")) {
                        leaf1bbGuid = created.getGuid();
                    } else if (name.equals("leaf2aa")) {
                        leaf2aaGuid = created.getGuid();
                    } else if (name.equals("leaf2ab")) {
                        leaf2abGuid = created.getGuid();
                    } else if (name.equals("leaf2ba")) {
                        leaf2baGuid = created.getGuid();
                    } else if (name.equals("leaf2bb")) {
                        leaf2bbGuid = created.getGuid();
                    }
                }
            }

        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception: " + e.getMessage());
        }
    }

    @Test(
            groups = {"read.hierarchy"},
            dependsOnGroups = {"create.hierarchy"})
    void traverseHierarchy() {
        try {
            Glossary glossary = Glossary.findByName(TRAVERSE_GLOSSARY_NAME, null);
            assertNotNull(glossary);
            Glossary.CategoryHierarchy tree = glossary.getHierarchy();
            assertNotNull(tree);
            List<GlossaryCategory> dfs = tree.depthFirst();
            assertNotNull(dfs);
            assertEquals(dfs.size(), 14);
            List<String> names = dfs.stream().map(GlossaryCategory::getName).collect(Collectors.toList());
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
            List<GlossaryCategory> bfs = tree.breadthFirst();
            assertNotNull(bfs);
            assertEquals(bfs.size(), 14);
            names = bfs.stream().map(Asset::getName).collect(Collectors.toList());
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
            List<GlossaryCategory> root = tree.getRootCategories();
            assertNotNull(root);
            assertEquals(root.size(), 2);
            names = root.stream().map(Asset::getName).collect(Collectors.toList());
            assertTrue(names.get(0).startsWith("top"));
            assertTrue(names.get(1).startsWith("top"));
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
            Glossary glossary = Glossary.retrieveByGuid(glossaryGuid);
            assertNotNull(glossary);
            assertTrue(glossary.isComplete());
            assertEquals(glossary.getGuid(), glossaryGuid);
            assertEquals(glossary.getQualifiedName(), glossaryQame);
            assertEquals(glossary.getName(), GLOSSARY_NAME);
            assertNotNull(glossary.getTerms());
            assertEquals(glossary.getTerms().size(), 2);
            Set<String> types =
                    glossary.getTerms().stream().map(GlossaryTerm::getTypeName).collect(Collectors.toSet());
            assertNotNull(types);
            assertEquals(types.size(), 1);
            assertTrue(types.contains(GlossaryTerm.TYPE_NAME));
            Set<String> guids =
                    glossary.getTerms().stream().map(GlossaryTerm::getGuid).collect(Collectors.toSet());
            assertNotNull(guids);
            assertEquals(guids.size(), 2);
            assertTrue(guids.contains(termGuid1));
            assertTrue(guids.contains(termGuid2));
            assertNotNull(glossary.getCategories());
            assertEquals(glossary.getCategories().size(), 1);
            types = glossary.getCategories().stream()
                    .map(GlossaryCategory::getTypeName)
                    .collect(Collectors.toSet());
            assertTrue(types.contains(GlossaryCategory.TYPE_NAME));
            guids = glossary.getCategories().stream()
                    .map(GlossaryCategory::getGuid)
                    .collect(Collectors.toSet());
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
            GlossaryCategory category = GlossaryCategory.retrieveByGuid(categoryGuid);
            assertNotNull(category);
            assertTrue(category.isComplete());
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
            GlossaryTerm term = GlossaryTerm.retrieveByGuid(termGuid1);
            assertNotNull(term);
            assertTrue(term.isComplete());
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
                .category(GlossaryCategory.refByGuid(categoryGuid))
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
            groups = {"purge.term.1"},
            dependsOnGroups = {"create.*", "update.*", "read.*", "search.*", "link.*", "unlink.*"},
            alwaysRun = true)
    void purgeTerm1() {
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
            groups = {"purge.term.2"},
            dependsOnGroups = {"create.*", "update.*", "read.*", "search.*", "link.*", "unlink.*"},
            alwaysRun = true)
    void purgeTerm2() {
        try {
            EntityMutationResponse response = GlossaryTerm.purge(termGuid2);
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
            assertEquals(term.getGuid(), termGuid2);
            assertEquals(term.getQualifiedName(), termQame2);
            assertEquals(term.getName(), TERM_NAME2);
            assertNull(term.getCertificateStatus());
            assertNull(term.getCertificateStatusMessage());
            assertNull(term.getAnnouncementType());
            assertNull(term.getAnnouncementTitle());
            assertNull(term.getAnnouncementMessage());
            assertEquals(term.getStatus(), AtlanStatus.DELETED);
            assertEquals(term.getDeleteHandler(), "HARD");
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception: " + e.getMessage());
        }
    }

    @Test(
            groups = {"purge.category"},
            dependsOnGroups = {"purge.term.*"},
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
            groups = {"purge.hierarchy"},
            dependsOnGroups = {"create.*", "update.*", "read.*", "search.*", "link.*", "unlink.*"},
            alwaysRun = true)
    void purgeHierarchy() {
        try {
            EntityMutationResponse response = EntityBulkEndpoint.delete(
                    List.of(
                            leaf1aaGuid,
                            leaf1abGuid,
                            leaf1baGuid,
                            leaf1bbGuid,
                            leaf2aaGuid,
                            leaf2abGuid,
                            leaf2baGuid,
                            leaf2bbGuid),
                    AtlanDeleteType.HARD);
            assertNotNull(response);
            assertEquals(response.getCreatedEntities().size(), 0);
            assertEquals(response.getUpdatedEntities().size(), 0);
            List<Entity> entities = response.getDeletedEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 8);

            response = EntityBulkEndpoint.delete(
                    List.of(mid1aGuid, mid1bGuid, mid2aGuid, mid2bGuid), AtlanDeleteType.HARD);
            assertNotNull(response);
            assertEquals(response.getCreatedEntities().size(), 0);
            assertEquals(response.getUpdatedEntities().size(), 0);
            entities = response.getDeletedEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 4);

            response = EntityBulkEndpoint.delete(List.of(top1Guid, top2Guid), AtlanDeleteType.HARD);
            assertNotNull(response);
            assertEquals(response.getCreatedEntities().size(), 0);
            assertEquals(response.getUpdatedEntities().size(), 0);
            entities = response.getDeletedEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 2);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception: " + e.getMessage());
        }
    }

    @Test(
            groups = {"purge.glossary"},
            dependsOnGroups = {"purge.category", "purge.hierarchy"},
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
            response = Glossary.purge(traverseGlossaryGuid);
            assertNotNull(response);
            assertEquals(response.getCreatedEntities().size(), 0);
            assertEquals(response.getUpdatedEntities().size(), 0);
            entities = response.getDeletedEntities();
            assertNotNull(entities);
            assertEquals(entities.size(), 1);
            one = entities.get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), Glossary.TYPE_NAME);
            assertTrue(one instanceof Glossary);
            glossary = (Glossary) one;
            assertEquals(glossary.getGuid(), traverseGlossaryGuid);
            assertEquals(glossary.getQualifiedName(), traverseGlossaryQame);
            assertEquals(glossary.getName(), TRAVERSE_GLOSSARY_NAME);
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
