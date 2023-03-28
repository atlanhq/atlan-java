/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.model.assets.*;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.AtlanStatus;
import java.util.SortedSet;
import org.testng.annotations.Test;

/**
 * Tests all aspects of linked resources (links and READMEs).
 */
public class ResourceTest extends AtlanLiveTest {

    private static final String PREFIX = AtlanLiveTest.PREFIX + "Resource";

    private static final String LINK_TITLE = PREFIX;
    private static final String LINK_URL = "https://www.atlan.com";
    private static final String README_CONTENT =
            "<h1>This is a test</h1><h2>With some headings</h2><p>And some normal content.</p>";

    private static Glossary glossary = null;
    private static GlossaryTerm term = null;
    private static Link link = null;
    private static Readme readme = null;

    @Test(groups = {"resources.create.term"})
    void createTerm() throws AtlanException {
        glossary = GlossaryTest.createGlossary(PREFIX);
        term = GlossaryTest.createTerm(PREFIX, glossary.getGuid());
    }

    @Test(
            groups = {"resources.create.readme"},
            dependsOnGroups = {"resources.create.term"})
    void addReadme() throws AtlanException {
        Readme toCreate = Readme.creator(GlossaryTerm.refByGuid(term.getGuid()), term.getName(), README_CONTENT)
                .build();
        AssetMutationResponse response = toCreate.upsert();
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getCreatedAssets().size(), 1);
        Asset one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof Readme);
        readme = (Readme) one;
        assertNotNull(readme.getGuid());
        assertNotNull(readme.getQualifiedName());
        assertEquals(readme.getDescription(), README_CONTENT);
        assertEquals(response.getUpdatedAssets().size(), 1);
        one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof GlossaryTerm);
        GlossaryTerm t = (GlossaryTerm) one;
        assertEquals(t.getGuid(), term.getGuid());
        assertEquals(t.getQualifiedName(), term.getQualifiedName());
    }

    @Test(
            groups = {"resources.create.link"},
            dependsOnGroups = {"resources.create.term"})
    void addLink() throws AtlanException {
        Link toCreate = Link.creator(GlossaryTerm.refByGuid(term.getGuid()), LINK_TITLE, LINK_URL)
                .build();
        AssetMutationResponse response = toCreate.upsert();
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getCreatedAssets().size(), 1);
        Asset one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof Link);
        link = (Link) one;
        assertNotNull(link.getGuid());
        assertNotNull(link.getQualifiedName());
        assertEquals(link.getName(), LINK_TITLE);
        assertEquals(link.getLink(), LINK_URL);
        assertEquals(response.getUpdatedAssets().size(), 1);
        one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof GlossaryTerm);
        GlossaryTerm t = (GlossaryTerm) one;
        assertEquals(t.getGuid(), term.getGuid());
        assertEquals(t.getQualifiedName(), term.getQualifiedName());
    }

    @Test(
            groups = {"resources.read.term"},
            dependsOnGroups = {"resources.create.*"})
    void retrieveTerm() throws AtlanException {
        GlossaryTerm t = GlossaryTerm.retrieveByQualifiedName(term.getQualifiedName());
        assertNotNull(t);
        assertTrue(t.isComplete());
        Readme r = t.getReadme();
        assertNotNull(r);
        assertEquals(r.getGuid(), readme.getGuid());
        SortedSet<Link> links = t.getLinks();
        assertNotNull(links);
        assertEquals(links.size(), 1);
        assertEquals(links.first().getGuid(), link.getGuid());
    }

    @Test(
            groups = {"resources.purge.readme"},
            dependsOnGroups = {"resources.read.term"},
            alwaysRun = true)
    void purgeReadme() throws AtlanException {
        AssetMutationResponse response = Readme.purge(readme.getGuid());
        assertNotNull(response);
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof Readme);
        Readme r = (Readme) one;
        assertEquals(r.getGuid(), readme.getGuid());
        assertEquals(r.getQualifiedName(), readme.getQualifiedName());
        assertEquals(r.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"resources.purge.link"},
            dependsOnGroups = {"resources.read.term"},
            alwaysRun = true)
    void purgeLink() throws AtlanException {
        AssetMutationResponse response = Link.purge(link.getGuid());
        assertNotNull(response);
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof Link);
        Link l = (Link) one;
        assertEquals(l.getGuid(), link.getGuid());
        assertEquals(l.getQualifiedName(), link.getQualifiedName());
        assertEquals(l.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"resources.purge.term"},
            dependsOnGroups = {
                "resources.create.*",
                "resources.read.*",
                "resources.purge.readme",
                "resources.purge.link"
            },
            alwaysRun = true)
    void purgeTerm() throws AtlanException {
        GlossaryTest.deleteTerm(term.getGuid());
        GlossaryTest.deleteGlossary(glossary.getGuid());
    }
}
