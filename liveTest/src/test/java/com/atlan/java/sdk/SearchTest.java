/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.*;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.Glossary;
import com.atlan.model.assets.GlossaryCategory;
import com.atlan.model.assets.GlossaryTerm;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.net.HttpClient;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.testng.annotations.Test;

/**
 * Tests various searches.
 * Note: since the search index is only eventually consistent with the metastore, there can be slight
 * delays between the data being persisted and it being searchable. As a result, there are retry loops
 * here in the tests to allow for that eventual consistency.
 */
public class SearchTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("SRCH");

    private static Glossary glossary = null;
    private static GlossaryCategory category1 = null;
    private static GlossaryCategory category2 = null;
    private static GlossaryCategory category3 = null;
    private static GlossaryTerm term1 = null;
    private static GlossaryTerm term2 = null;
    private static GlossaryTerm term3 = null;
    private static GlossaryTerm term4 = null;
    private static GlossaryTerm term5 = null;

    @Test(groups = {"search.create.terms"})
    void createAssets() throws AtlanException {
        glossary = GlossaryTest.createGlossary(PREFIX);
        category1 = GlossaryTest.createCategory(PREFIX + "1", glossary);
        category2 = GlossaryTest.createCategory(PREFIX + "2", glossary);
        category3 = GlossaryTest.createCategory(PREFIX + "3", glossary);
        term1 = GlossaryTest.createTerm(PREFIX + "1", glossary);
        term2 = GlossaryTest.createTerm(PREFIX + "2", glossary);
        term3 = GlossaryTest.createTerm(PREFIX + "3", glossary);
        term4 = GlossaryTest.createTerm(PREFIX + "4", glossary);
        term5 = GlossaryTest.createTerm(PREFIX + "5", glossary);
    }

    @Test(
            groups = {"search.create.links"},
            dependsOnGroups = {"search.create.terms"})
    void linkAssets() throws AtlanException {
        AssetMutationResponse response = term1.trimToRequired()
                .category(category1.trimToReference())
                .build()
                .save();
        assertNotNull(response);
        assertEquals(response.getUpdatedAssets().size(), 2);
        assertEquals(response.getUpdatedAssets(GlossaryTerm.class).size(), 1);
        assertEquals(response.getUpdatedAssets(GlossaryCategory.class).size(), 1);
        response = term2.trimToRequired()
                .category(category1.trimToReference())
                .category(category2.trimToReference())
                .build()
                .save();
        assertNotNull(response);
        assertEquals(response.getUpdatedAssets().size(), 3);
        assertEquals(response.getUpdatedAssets(GlossaryTerm.class).size(), 1);
        assertEquals(response.getUpdatedAssets(GlossaryCategory.class).size(), 2);
        response = term3.trimToRequired()
                .category(category1.trimToReference())
                .category(category2.trimToReference())
                .category(category3.trimToReference())
                .build()
                .save();
        assertNotNull(response);
        assertEquals(response.getUpdatedAssets().size(), 4);
        assertEquals(response.getUpdatedAssets(GlossaryTerm.class).size(), 1);
        assertEquals(response.getUpdatedAssets(GlossaryCategory.class).size(), 3);
    }

    @Test(
            groups = {"search.search.consistent"},
            dependsOnGroups = {"search.create.links"})
    void waitForConsistency() throws AtlanException, InterruptedException {
        IndexSearchRequest index = GlossaryTerm.select()
                .where(GlossaryTerm.ANCHOR.eq(glossary.getQualifiedName()))
                .where(GlossaryTerm.UPDATE_TIME.gt(term5.getCreateTime()))
                .toRequest();

        IndexSearchResponse response = index.search();

        int count = 0;
        while (response.getApproximateCount() < 3L && count < Atlan.getMaxNetworkRetries()) {
            Thread.sleep(HttpClient.waitTime(count).toMillis());
            response = index.search();
            count++;
        }

        assertEquals(response.getApproximateCount(), 3);
    }

    @Test(
            groups = {"search.search.terms_set"},
            dependsOnGroups = {"search.search.consistent"})
    void termsSet() throws AtlanException {
        Set<String> guids = GlossaryTerm.select()
                .where(GlossaryTerm.ANCHOR.eq(glossary.getQualifiedName()))
                .where(GlossaryTerm.CATEGORIES.in(
                        List.of(category1.getQualifiedName(), category2.getQualifiedName()), 2))
                .stream()
                .map(Asset::getGuid)
                .collect(Collectors.toSet());
        assertNotNull(guids);
        assertEquals(guids.size(), 2);
        assertTrue(guids.contains(term2.getGuid()));
        assertTrue(guids.contains(term3.getGuid()));
    }

    @Test(
            groups = {"search.search.wildcard"},
            dependsOnGroups = {"search.search.consistent"})
    void wildcard() throws AtlanException {
        String orgTermName = term4.getName();
        String toSearch = orgTermName.substring(0, orgTermName.length() - 2) + "?4";
        Set<String> guids = GlossaryTerm.select()
                .where(GlossaryTerm.ANCHOR.eq(glossary.getQualifiedName()))
                .where(GlossaryTerm.NAME.wildcard(toSearch))
                .stream()
                .map(Asset::getGuid)
                .collect(Collectors.toSet());
        assertNotNull(guids);
        assertEquals(guids.size(), 1);
        assertTrue(guids.contains(term4.getGuid()));
        toSearch = orgTermName.substring(0, orgTermName.length() - 4) + "*4";
        guids = GlossaryTerm.select()
                .where(GlossaryTerm.ANCHOR.eq(glossary.getQualifiedName()))
                .where(GlossaryTerm.NAME.wildcard(toSearch))
                .stream()
                .map(Asset::getGuid)
                .collect(Collectors.toSet());
        assertNotNull(guids);
        assertEquals(guids.size(), 1);
        assertTrue(guids.contains(term4.getGuid()));
    }

    @Test(
            groups = {"search.search.regex"},
            dependsOnGroups = {"search.search.consistent"})
    void regex() throws AtlanException {
        String toSearch = term3.getName() + "|" + term4.getName();
        Set<String> guids = GlossaryTerm.select()
                .where(GlossaryTerm.ANCHOR.eq(glossary.getQualifiedName()))
                .where(GlossaryTerm.NAME.regex(toSearch))
                .stream()
                .map(Asset::getGuid)
                .collect(Collectors.toSet());
        assertNotNull(guids);
        assertEquals(guids.size(), 2);
        assertTrue(guids.contains(term3.getGuid()));
        assertTrue(guids.contains(term4.getGuid()));
    }

    @Test(
            groups = {"search.search.range"},
            dependsOnGroups = {"search.search.consistent"})
    void range() throws AtlanException {
        Set<String> guids = GlossaryTerm.select()
                .where(GlossaryTerm.ANCHOR.eq(glossary.getQualifiedName()))
                .where(GlossaryTerm.UPDATE_TIME.gt(term5.getCreateTime()))
                .stream()
                .map(Asset::getGuid)
                .collect(Collectors.toSet());
        assertNotNull(guids);
        assertEquals(guids.size(), 3);
        guids = GlossaryTerm.select()
                .where(GlossaryTerm.ANCHOR.eq(glossary.getQualifiedName()))
                .where(GlossaryTerm.UPDATE_TIME.gte(term5.getCreateTime()))
                .stream()
                .map(Asset::getGuid)
                .collect(Collectors.toSet());
        assertNotNull(guids);
        assertEquals(guids.size(), 4);
        guids = GlossaryTerm.select()
                .where(GlossaryTerm.ANCHOR.eq(glossary.getQualifiedName()))
                .where(GlossaryTerm.CREATE_TIME.lt(term5.getCreateTime()))
                .stream()
                .map(Asset::getGuid)
                .collect(Collectors.toSet());
        assertNotNull(guids);
        assertEquals(guids.size(), 4);
        guids = GlossaryTerm.select()
                .where(GlossaryTerm.ANCHOR.eq(glossary.getQualifiedName()))
                .where(GlossaryTerm.CREATE_TIME.lte(term5.getCreateTime()))
                .stream()
                .map(Asset::getGuid)
                .collect(Collectors.toSet());
        assertNotNull(guids);
        assertEquals(guids.size(), 5);
        guids = GlossaryTerm.select()
                .where(GlossaryTerm.ANCHOR.eq(glossary.getQualifiedName()))
                .where(GlossaryTerm.CREATE_TIME.between(term2.getCreateTime(), term4.getCreateTime()))
                .stream()
                .map(Asset::getGuid)
                .collect(Collectors.toSet());
        assertNotNull(guids);
        assertEquals(guids.size(), 3);
    }

    @Test(
            groups = {"search.purge.term"},
            dependsOnGroups = {"search.create.*", "search.search.*"},
            alwaysRun = true)
    void purgeAssets() throws AtlanException {
        GlossaryTest.deleteTerm(term1.getGuid());
        GlossaryTest.deleteTerm(term2.getGuid());
        GlossaryTest.deleteTerm(term3.getGuid());
        GlossaryTest.deleteTerm(term4.getGuid());
        GlossaryTest.deleteTerm(term5.getGuid());
        GlossaryTest.deleteCategory(category1.getGuid());
        GlossaryTest.deleteCategory(category2.getGuid());
        GlossaryTest.deleteCategory(category3.getGuid());
        GlossaryTest.deleteGlossary(glossary.getGuid());
    }
}
