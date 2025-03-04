/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.*;

import co.elastic.clients.elasticsearch._types.SortOrder;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.Connection;
import com.atlan.model.assets.Glossary;
import com.atlan.model.assets.GlossaryCategory;
import com.atlan.model.assets.GlossaryTerm;
import com.atlan.model.assets.IReferenceable;
import com.atlan.model.assets.Table;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.core.AtlanTag;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.search.FluentSearch;
import com.atlan.model.search.IndexSearchDSL;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.model.structs.SourceTagAttachment;
import com.atlan.model.structs.SourceTagAttachmentValue;
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

    private static final String EXISTING_SOURCE_SYNCED_TAG = "Confidential";

    private static Glossary glossary = null;
    private static GlossaryCategory category1 = null;
    private static GlossaryCategory category2 = null;
    private static GlossaryCategory category3 = null;
    private static GlossaryTerm term1 = null;
    private static GlossaryTerm term2 = null;
    private static GlossaryTerm term3 = null;
    private static GlossaryTerm term4 = null;
    private static GlossaryTerm term5 = null;

    @Test(groups = {"search."})
    void findSourceSyncedAssets() throws AtlanException {
        List<Connection> connections = Connection.findByName(client, "development", AtlanConnectorType.SNOWFLAKE);
        assertNotNull(connections);
        assertEquals(connections.size(), 1);
        Connection snowflake = connections.get(0);
        assertNotNull(snowflake.getQualifiedName());
        List<Asset> tables =
                Table
                    .select(client)
                    .taggedWithValue(
                        EXISTING_SOURCE_SYNCED_TAG,
                        snowflake.getQualifiedName() + "/ANALYTICS/WIDE_WORLD_IMPORTERS",
                        "Highly Restricted",
                        false)
                    .stream()
                    .toList();
        assertNotNull(tables);
        assertFalse(tables.isEmpty());
        for (Asset one : tables) {
            assertTrue(one instanceof Table);
            Table table = (Table) one;
            Set<AtlanTag> tags = table.getAtlanTags();
            assertNotNull(tags);
            assertFalse(tags.isEmpty());
            List<AtlanTag> synced = tags.stream()
                    .filter(tag -> tag.getTypeName().equals(EXISTING_SOURCE_SYNCED_TAG))
                    .toList();
            assertNotNull(synced);
            assertFalse(synced.isEmpty());
            for (AtlanTag t : synced) {
                List<SourceTagAttachment> attachments = t.getSourceTagAttachments();
                assertNotNull(attachments);
                assertFalse(attachments.isEmpty());
                for (SourceTagAttachment sta : attachments) {
                    List<SourceTagAttachmentValue> values = sta.getSourceTagValues();
                    assertNotNull(values);
                    assertFalse(values.isEmpty());
                    for (SourceTagAttachmentValue value : values) {
                        String attachedValue = value.getTagAttachmentValue();
                        assertNotNull(attachedValue);
                        assertEquals(attachedValue, "Highly Restricted");
                    }
                }
            }
        }
    }

    @Test(groups = {"search.create.terms"})
    void createAssets() throws AtlanException {
        glossary = GlossaryTest.createGlossary(client, PREFIX);
        category1 = GlossaryTest.createCategory(client, PREFIX + "1", glossary);
        category2 = GlossaryTest.createCategory(client, PREFIX + "2", glossary);
        category3 = GlossaryTest.createCategory(client, PREFIX + "3", glossary);
        term1 = GlossaryTest.createTerm(client, PREFIX + "1", glossary);
        term2 = GlossaryTest.createTerm(client, PREFIX + "2", glossary);
        term3 = GlossaryTest.createTerm(client, PREFIX + "3", glossary);
        term4 = GlossaryTest.createTerm(client, PREFIX + "4", glossary);
        term5 = GlossaryTest.createTerm(client, PREFIX + "5", glossary);
    }

    @Test(
            groups = {"search.defaults"},
            dependsOnGroups = {"search.create.terms"})
    void testDefaultSorting() throws AtlanException {
        // Empty sorting
        IndexSearchRequest request = GlossaryTerm.select(client)
                .where(Asset.QUALIFIED_NAME.eq(term1.getQualifiedName()))
                .toRequest();
        IndexSearchResponse response = request.search(client);
        assertNotNull(response);
        IndexSearchDSL dsl = response.getQuery();
        assertNotNull(dsl);
        assertNotNull(dsl.getSort());
        assertEquals(dsl.getSort().size(), 1);
        assertTrue(dsl.getSort().get(0).isField());
        assertEquals(
                IReferenceable.GUID.getKeywordFieldName(),
                dsl.getSort().get(0).field().field());
        // Sort without GUID
        request = client.assets
                .select()
                .where(Asset.QUALIFIED_NAME.eq("abc123"))
                .sort(Asset.QUALIFIED_NAME.order(SortOrder.Asc))
                .toRequest();
        response = request.search(client);
        assertNotNull(response);
        dsl = response.getQuery();
        assertNotNull(dsl);
        assertNotNull(dsl.getSort());
        assertEquals(dsl.getSort().size(), 2);
        assertTrue(dsl.getSort().get(0).isField());
        assertTrue(dsl.getSort().get(1).isField());
        assertEquals(
                Asset.QUALIFIED_NAME.getKeywordFieldName(),
                dsl.getSort().get(0).field().field());
        assertEquals(
                IReferenceable.GUID.getKeywordFieldName(),
                dsl.getSort().get(1).field().field());
        // Sort with only GUID
        request = client.assets
                .select()
                .where(Asset.QUALIFIED_NAME.eq("abc123"))
                .sort(IReferenceable.GUID.order(SortOrder.Asc))
                .toRequest();
        response = request.search(client);
        assertNotNull(response);
        dsl = response.getQuery();
        assertNotNull(dsl);
        assertNotNull(dsl.getSort());
        assertEquals(dsl.getSort().size(), 1);
        assertTrue(dsl.getSort().get(0).isField());
        assertEquals(
                IReferenceable.GUID.getKeywordFieldName(),
                dsl.getSort().get(0).field().field());
        // Sort with GUID and others
        request = client.assets
                .select()
                .where(Asset.QUALIFIED_NAME.eq("abc123"))
                .sort(Asset.QUALIFIED_NAME.order(SortOrder.Asc))
                .sort(IReferenceable.GUID.order(SortOrder.Desc))
                .toRequest();
        response = request.search(client);
        assertNotNull(response);
        dsl = response.getQuery();
        assertNotNull(dsl);
        assertNotNull(dsl.getSort());
        assertEquals(dsl.getSort().size(), 2);
        assertTrue(dsl.getSort().get(0).isField());
        assertTrue(dsl.getSort().get(1).isField());
        assertEquals(
                Asset.QUALIFIED_NAME.getKeywordFieldName(),
                dsl.getSort().get(0).field().field());
        assertEquals(
                IReferenceable.GUID.getKeywordFieldName(),
                dsl.getSort().get(1).field().field());
    }

    @Test(
            groups = {"search.create.links"},
            dependsOnGroups = {"search.create.terms"})
    void linkAssets() throws AtlanException {
        AssetMutationResponse response = term1.trimToRequired()
                .category(category1.trimToReference())
                .build()
                .save(client);
        assertNotNull(response);
        assertEquals(response.getUpdatedAssets().size(), 2);
        assertEquals(response.getUpdatedAssets(GlossaryTerm.class).size(), 1);
        assertEquals(response.getUpdatedAssets(GlossaryCategory.class).size(), 1);
        response = term2.trimToRequired()
                .category(category1.trimToReference())
                .category(category2.trimToReference())
                .build()
                .save(client);
        assertNotNull(response);
        assertEquals(response.getUpdatedAssets().size(), 3);
        assertEquals(response.getUpdatedAssets(GlossaryTerm.class).size(), 1);
        assertEquals(response.getUpdatedAssets(GlossaryCategory.class).size(), 2);
        response = term3.trimToRequired()
                .category(category1.trimToReference())
                .category(category2.trimToReference())
                .category(category3.trimToReference())
                .build()
                .save(client);
        assertNotNull(response);
        assertEquals(response.getUpdatedAssets().size(), 4);
        assertEquals(response.getUpdatedAssets(GlossaryTerm.class).size(), 1);
        assertEquals(response.getUpdatedAssets(GlossaryCategory.class).size(), 3);
    }

    @Test(
            groups = {"search.search.consistent"},
            dependsOnGroups = {"search.create.links"})
    void waitForConsistency() throws AtlanException, InterruptedException {
        IndexSearchRequest index = GlossaryTerm.select(client)
                .where(GlossaryTerm.ANCHOR.eq(glossary.getQualifiedName()))
                .where(GlossaryTerm.UPDATE_TIME.gt(term5.getCreateTime()))
                .toRequest();
        IndexSearchResponse response = retrySearchUntil(index, 3L);
        assertEquals(response.getApproximateCount(), 3);
    }

    @Test(
            groups = {"search.search.terms_set"},
            dependsOnGroups = {"search.search.consistent"})
    void termsSet() throws AtlanException {
        Set<String> guids = GlossaryTerm.select(client)
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
        Set<String> guids = GlossaryTerm.select(client)
                .where(GlossaryTerm.ANCHOR.eq(glossary.getQualifiedName()))
                .where(GlossaryTerm.NAME.wildcard(toSearch))
                .stream()
                .map(Asset::getGuid)
                .collect(Collectors.toSet());
        assertNotNull(guids);
        assertEquals(guids.size(), 1);
        assertTrue(guids.contains(term4.getGuid()));
        toSearch = orgTermName.substring(0, orgTermName.length() - 4) + "*4";
        guids = GlossaryTerm.select(client)
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
        Set<String> guids = GlossaryTerm.select(client)
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
        Set<String> guids = GlossaryTerm.select(client)
                .where(GlossaryTerm.ANCHOR.eq(glossary.getQualifiedName()))
                .where(GlossaryTerm.UPDATE_TIME.gt(term5.getCreateTime()))
                .stream()
                .map(Asset::getGuid)
                .collect(Collectors.toSet());
        assertNotNull(guids);
        assertEquals(guids.size(), 3);
        guids = GlossaryTerm.select(client)
                .where(GlossaryTerm.ANCHOR.eq(glossary.getQualifiedName()))
                .where(GlossaryTerm.UPDATE_TIME.gte(term5.getCreateTime()))
                .stream()
                .map(Asset::getGuid)
                .collect(Collectors.toSet());
        assertNotNull(guids);
        assertEquals(guids.size(), 4);
        guids = GlossaryTerm.select(client)
                .where(GlossaryTerm.ANCHOR.eq(glossary.getQualifiedName()))
                .where(GlossaryTerm.CREATE_TIME.lt(term5.getCreateTime()))
                .stream()
                .map(Asset::getGuid)
                .collect(Collectors.toSet());
        assertNotNull(guids);
        assertEquals(guids.size(), 4);
        guids = GlossaryTerm.select(client)
                .where(GlossaryTerm.ANCHOR.eq(glossary.getQualifiedName()))
                .where(GlossaryTerm.CREATE_TIME.lte(term5.getCreateTime()))
                .stream()
                .map(Asset::getGuid)
                .collect(Collectors.toSet());
        assertNotNull(guids);
        assertEquals(guids.size(), 5);
        guids = GlossaryTerm.select(client)
                .where(GlossaryTerm.ANCHOR.eq(glossary.getQualifiedName()))
                .where(GlossaryTerm.CREATE_TIME.between(term2.getCreateTime(), term4.getCreateTime()))
                .stream()
                .map(Asset::getGuid)
                .collect(Collectors.toSet());
        assertNotNull(guids);
        assertEquals(guids.size(), 3);
    }

    @Test(
            groups = {"search.search.streams"},
            dependsOnGroups = {"search.search.consistent"})
    void streamVariations() throws AtlanException {
        FluentSearch request = GlossaryTerm.select(client)
                .where(GlossaryTerm.ANCHOR.eq(glossary.getQualifiedName()))
                .where(GlossaryTerm.CREATE_TIME.lte(term5.getCreateTime()))
                .pageSize(2)
                .build();
        Set<String> guidsDirect = request.stream().map(Asset::getGuid).collect(Collectors.toSet());
        assertNotNull(guidsDirect);
        assertEquals(guidsDirect.size(), 5);
        Set<String> guidsBulk = request.bulkStream().map(Asset::getGuid).collect(Collectors.toSet());
        assertNotNull(guidsBulk);
        assertEquals(guidsBulk.size(), 5);
        Set<String> guidsParallel = request.parallelStream().map(Asset::getGuid).collect(Collectors.toSet());
        assertNotNull(guidsParallel);
        assertEquals(guidsParallel.size(), 5);
        assertEquals(guidsDirect, guidsBulk);
        assertEquals(guidsBulk, guidsParallel);
    }

    @Test(
            groups = {"search.purge.term"},
            dependsOnGroups = {"search.create.*", "search.search.*"},
            alwaysRun = true)
    void purgeAssets() throws AtlanException {
        GlossaryTest.deleteTerm(client, term1.getGuid());
        GlossaryTest.deleteTerm(client, term2.getGuid());
        GlossaryTest.deleteTerm(client, term3.getGuid());
        GlossaryTest.deleteTerm(client, term4.getGuid());
        GlossaryTest.deleteTerm(client, term5.getGuid());
        GlossaryTest.deleteCategory(client, category1.getGuid());
        GlossaryTest.deleteCategory(client, category2.getGuid());
        GlossaryTest.deleteCategory(client, category3.getGuid());
        GlossaryTest.deleteGlossary(client, glossary.getGuid());
    }
}
