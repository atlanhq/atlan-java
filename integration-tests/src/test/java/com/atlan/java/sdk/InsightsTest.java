/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.*;

import co.elastic.clients.elasticsearch._types.SortOrder;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.*;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.*;
import com.atlan.model.search.*;
import com.atlan.net.HttpClient;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * Tests all aspects of S3 assets.
 */
@Slf4j
public class InsightsTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("INS");

    public static final String COLLECTION_NAME = PREFIX;

    private static final String FOLDER_NAME = PREFIX + "_folder";
    private static final String SUB_FOLDER_NAME = FOLDER_NAME + "_sub";
    private static final String QUERY_NAME = PREFIX + "_query";

    private static AtlanCollection collection = null;
    private static Folder folder = null;
    private static Folder subfolder = null;
    private static AtlanQuery query = null;

    @Test(groups = {"insights.create.collection"})
    void createCollection() throws AtlanException, InterruptedException {
        AtlanCollection col = AtlanCollection.creator(client, COLLECTION_NAME)
                .adminGroup(PersonaTest.EXISTING_GROUP_NAME)
                .build();
        AssetMutationResponse response = null;
        int retryCount = 0;
        while (response == null && retryCount < client.getMaxNetworkRetries()) {
            retryCount++;
            try {
                response = col.save(client).block();
            } catch (InvalidRequestException e) {
                if (retryCount < client.getMaxNetworkRetries()) {
                    if (e.getCode() != null
                            && e.getCode().equals("ATLAN-JAVA-400-000")
                            && e.getMessage().equals("Server responded with ATLAS-400-00-029: Auth request failed")) {
                        Thread.sleep(HttpClient.waitTime(retryCount).toMillis());
                    }
                } else {
                    log.error("Overran retry limit ({}), rethrowing exception.", client.getMaxNetworkRetries());
                    throw e;
                }
            }
        }
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof AtlanCollection);
        collection = (AtlanCollection) one;
        assertNotNull(collection.getGuid());
        assertNotNull(collection.getQualifiedName());
        assertEquals(collection.getName(), COLLECTION_NAME);
    }

    @Test(
            groups = {"insights.create.folder"},
            dependsOnGroups = {"insights.create.collection"})
    void createFolder() throws AtlanException {
        Folder toCreate = Folder.creator(FOLDER_NAME, collection).build();
        AssetMutationResponse response = toCreate.save(client);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof AtlanCollection);
        AtlanCollection c = (AtlanCollection) one;
        assertEquals(c.getGuid(), collection.getGuid());
        assertEquals(c.getQualifiedName(), collection.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof Folder);
        folder = (Folder) one;
        assertNotNull(folder.getGuid());
        assertNotNull(folder.getQualifiedName());
        assertEquals(folder.getName(), FOLDER_NAME);
        assertEquals(folder.getCollectionQualifiedName(), collection.getQualifiedName());
        assertEquals(folder.getParentQualifiedName(), collection.getQualifiedName());
    }

    @Test(
            groups = {"insights.create.subfolder"},
            dependsOnGroups = {"insights.create.folder"})
    void createSubFolder() throws AtlanException {
        Folder toCreate = Folder.creator(SUB_FOLDER_NAME, folder).build();
        AssetMutationResponse response = toCreate.save(client);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof Folder);
        Folder f = (Folder) one;
        assertEquals(f.getGuid(), folder.getGuid());
        assertEquals(f.getQualifiedName(), folder.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof Folder);
        subfolder = (Folder) one;
        assertNotNull(subfolder.getGuid());
        assertNotNull(subfolder.getQualifiedName());
        assertEquals(subfolder.getName(), SUB_FOLDER_NAME);
        assertEquals(subfolder.getCollectionQualifiedName(), collection.getQualifiedName());
        assertEquals(subfolder.getParentQualifiedName(), folder.getQualifiedName());
    }

    @Test(
            groups = {"insights.create.query"},
            dependsOnGroups = {"insights.create.subfolder"})
    void createQuery() throws AtlanException {
        List<Connection> connection = Connection.findByName(client, "development", AtlanConnectorType.SNOWFLAKE);
        assertNotNull(connection);
        assertEquals(connection.size(), 1);
        Optional<Asset> schema = Schema.select(client)
                .where(Schema.CONNECTION_QUALIFIED_NAME.eq(connection.get(0).getQualifiedName()))
                .where(Schema.DATABASE_NAME.eq("ANALYTICS"))
                .where(Schema.NAME.eq("WIDE_WORLD_IMPORTERS"))
                .stream()
                .findFirst();
        assertTrue(schema.isPresent());
        AtlanQuery toCreate = AtlanQuery.creator(QUERY_NAME, folder)
                .withRawQuery(schema.get().getQualifiedName(), "SELECT * FROM DIM_CUSTOMERS;")
                .build();
        AssetMutationResponse response = toCreate.save(client);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof Folder);
        Folder f = (Folder) one;
        assertEquals(f.getGuid(), folder.getGuid());
        assertEquals(f.getQualifiedName(), folder.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof AtlanQuery);
        query = (AtlanQuery) one;
        assertNotNull(query.getGuid());
        assertNotNull(query.getQualifiedName());
        assertEquals(query.getName(), QUERY_NAME);
        assertEquals(query.getParentQualifiedName(), folder.getQualifiedName());
        assertEquals(query.getCollectionQualifiedName(), collection.getQualifiedName());
    }

    @Test(
            groups = {"insights.update.query"},
            dependsOnGroups = {"insights.create.query"})
    void updateQuery() throws AtlanException {
        AtlanQuery updated = AtlanQuery.updateCertificate(
                client,
                query.getQualifiedName(),
                query.getName(),
                query.getCollectionQualifiedName(),
                query.getParentQualifiedName(),
                CERTIFICATE_STATUS,
                CERTIFICATE_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(updated.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        updated = AtlanQuery.updateAnnouncement(
                client,
                query.getQualifiedName(),
                query.getName(),
                query.getCollectionQualifiedName(),
                query.getParentQualifiedName(),
                ANNOUNCEMENT_TYPE,
                ANNOUNCEMENT_TITLE,
                ANNOUNCEMENT_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"insights.read.query"},
            dependsOnGroups = {"insights.create.query", "insights.update.query"})
    void retrieveQuery() throws AtlanException {
        AtlanQuery q = AtlanQuery.get(client, query.getGuid(), true);
        assertNotNull(q);
        assertTrue(q.isComplete());
        assertEquals(q.getGuid(), query.getGuid());
        assertEquals(q.getQualifiedName(), query.getQualifiedName());
        assertEquals(q.getName(), QUERY_NAME);
        assertEquals(q.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(q.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        assertNotNull(q.getParent());
        assertEquals(q.getParent().getGuid(), folder.getGuid());
        assertEquals(q.getCollectionQualifiedName(), collection.getQualifiedName());
        assertEquals(q.getParentQualifiedName(), folder.getQualifiedName());
    }

    @Test(
            groups = {"insights.read.folder"},
            dependsOnGroups = {"insights.create.query", "insights.update.query"})
    void retrieveFolder() throws AtlanException {
        Folder f = Folder.get(client, folder.getGuid(), true);
        assertNotNull(f);
        assertTrue(f.isComplete());
        assertEquals(f.getGuid(), folder.getGuid());
        assertEquals(f.getQualifiedName(), folder.getQualifiedName());
        assertEquals(f.getName(), FOLDER_NAME);
        assertNotNull(f.getChildrenFolders());
        assertEquals(f.getChildrenFolders().size(), 1);
        assertEquals(f.getChildrenFolders().first().getGuid(), subfolder.getGuid());
        assertNotNull(f.getChildrenQueries());
        assertEquals(f.getChildrenQueries().size(), 1);
        assertEquals(f.getChildrenQueries().first().getGuid(), query.getGuid());
    }

    @Test(
            groups = {"insights.update.query.again"},
            dependsOnGroups = {"insights.read.query", "insights.read.folder"})
    void updateQueryAgain() throws AtlanException {
        AtlanQuery updated = AtlanQuery.removeCertificate(
                client,
                query.getQualifiedName(),
                query.getName(),
                query.getCollectionQualifiedName(),
                query.getParentQualifiedName());
        assertNotNull(updated);
        assertNull(updated.getCertificateStatus());
        assertNull(updated.getCertificateStatusMessage());
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        updated = AtlanQuery.removeAnnouncement(
                client,
                query.getQualifiedName(),
                QUERY_NAME,
                query.getCollectionQualifiedName(),
                query.getParentQualifiedName());
        assertNotNull(updated);
        assertNull(updated.getAnnouncementType());
        assertNull(updated.getAnnouncementTitle());
        assertNull(updated.getAnnouncementMessage());
    }

    @Test(
            groups = {"insights.search.assets"},
            dependsOnGroups = {"insights.update.query.again"})
    void searchAssets() throws AtlanException, InterruptedException {
        IndexSearchRequest index = client.assets
                .select()
                .where(Asset.QUALIFIED_NAME.startsWith(collection.getQualifiedName()))
                .pageSize(10)
                .aggregate("type", IReferenceable.TYPE_NAME.bucketBy())
                .sort(Asset.CREATE_TIME.order(SortOrder.Asc))
                .includeOnResults(Asset.NAME)
                .includeOnResults(AtlanQuery.COLLECTION_QUALIFIED_NAME)
                .includeOnResults(AtlanQuery.PARENT_QUALIFIED_NAME)
                .toRequest();

        IndexSearchResponse response = retrySearchUntil(index, 3L);

        assertNotNull(response.getAggregations());
        assertEquals(response.getAggregations().size(), 1);
        assertTrue(response.getAggregations().get("type") instanceof AggregationBucketResult);
        assertEquals(
                ((AggregationBucketResult) response.getAggregations().get("type"))
                        .getBuckets()
                        .size(),
                3);

        assertEquals(response.getApproximateCount().longValue(), 4L);
        List<Asset> entities = response.getAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 4);

        Asset one = entities.get(0);
        assertTrue(one instanceof AtlanCollection);
        assertFalse(one.isComplete());
        AtlanCollection c = (AtlanCollection) one;
        assertEquals(c.getQualifiedName(), collection.getQualifiedName());
        assertEquals(c.getName(), collection.getName());

        one = entities.get(1);
        assertTrue(one instanceof Folder);
        assertFalse(one.isComplete());
        Folder f = (Folder) one;
        assertEquals(f.getQualifiedName(), folder.getQualifiedName());
        assertEquals(f.getName(), folder.getName());
        assertEquals(f.getCollectionQualifiedName(), collection.getQualifiedName());

        one = entities.get(2);
        assertTrue(one instanceof Folder);
        assertFalse(one.isComplete());
        f = (Folder) one;
        assertEquals(f.getQualifiedName(), subfolder.getQualifiedName());
        assertEquals(f.getName(), subfolder.getName());
        assertEquals(f.getCollectionQualifiedName(), collection.getQualifiedName());
        assertEquals(f.getParentQualifiedName(), folder.getQualifiedName());

        one = entities.get(3);
        assertTrue(one instanceof AtlanQuery);
        assertFalse(one.isComplete());
        AtlanQuery q = (AtlanQuery) one;
        assertEquals(q.getQualifiedName(), query.getQualifiedName());
        assertEquals(q.getName(), query.getName());
        assertEquals(q.getCollectionQualifiedName(), collection.getQualifiedName());
        assertEquals(q.getParentQualifiedName(), folder.getQualifiedName());
    }

    @Test(
            groups = {"insights.delete.query"},
            dependsOnGroups = {"insights.update.*", "insights.search.*"})
    void deleteQuery() throws AtlanException {
        AssetMutationResponse response = Asset.delete(client, query.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof AtlanQuery);
        AtlanQuery q = (AtlanQuery) one;
        assertEquals(q.getGuid(), query.getGuid());
        assertEquals(q.getQualifiedName(), query.getQualifiedName());
        assertEquals(q.getDeleteHandler(), "SOFT");
        assertEquals(q.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"insights.delete.query.read"},
            dependsOnGroups = {"insights.delete.query"})
    void readDeletedQuery() throws AtlanException {
        validateDeletedAsset(query, log);
    }

    @Test(
            groups = {"insights.delete.query.restore"},
            dependsOnGroups = {"insights.delete.query.read"})
    void restoreQuery() throws AtlanException {
        assertTrue(AtlanQuery.restore(client, query.getQualifiedName()));
        AtlanQuery restored = AtlanQuery.get(client, query.getQualifiedName());
        assertFalse(restored.isComplete());
        assertEquals(restored.getGuid(), query.getGuid());
        assertEquals(restored.getQualifiedName(), query.getQualifiedName());
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"insights.purge.query"},
            dependsOnGroups = {"insights.delete.query.restore"})
    void purgeQuery() throws AtlanException {
        AssetMutationResponse response = Asset.purge(client, query.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof AtlanQuery);
        AtlanQuery s = (AtlanQuery) one;
        assertEquals(s.getGuid(), query.getGuid());
        assertEquals(s.getQualifiedName(), query.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "PURGE");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"insights.purge.collection"},
            dependsOnGroups = {
                "insights.create.*",
                "insights.read.*",
                "insights.search.*",
                "insights.update.*",
                "insights.purge.query"
            },
            alwaysRun = true)
    void purgeCollection() throws AtlanException {
        String qualifiedName = collection.getQualifiedName();
        List<String> guids =
                client.assets.select().where(Asset.QUALIFIED_NAME.startsWith(qualifiedName)).pageSize(50).stream()
                        .map(Asset::getGuid)
                        .collect(Collectors.toList());
        if (!guids.isEmpty()) {
            int totalToDelete = guids.size();
            log.info(" --- Purging {} assets from {}... ---", totalToDelete, qualifiedName);
            if (totalToDelete < 20) {
                client.assets.delete(guids, AtlanDeleteType.PURGE).block();
            } else {
                for (int i = 0; i < totalToDelete; i += 20) {
                    log.info(" ... next batch of 20 ({}%)", Math.round((i * 100.0) / totalToDelete));
                    List<String> sublist = guids.subList(i, Math.min(i + 20, totalToDelete));
                    client.assets.delete(sublist, AtlanDeleteType.PURGE).block();
                }
            }
        }
        // Purge the collection itself, now that all assets are purged
        Optional<Asset> found =
                AtlanCollection.select(client).where(AtlanCollection.QUALIFIED_NAME.eq(qualifiedName)).stream()
                        .findFirst();
        if (found.isPresent()) {
            client.assets.delete(found.get().getGuid(), AtlanDeleteType.PURGE).block();
        }
    }
}
