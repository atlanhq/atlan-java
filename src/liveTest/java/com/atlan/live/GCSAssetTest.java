/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.*;
import com.atlan.model.core.Entity;
import com.atlan.model.core.EntityMutationResponse;
import com.atlan.model.enums.*;
import com.atlan.model.search.IndexSearchDSL;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.util.QueryFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * Tests all aspects of Google Cloud Storage assets.
 */
@Test(groups = {"gcs"})
@Slf4j
public class GCSAssetTest extends AtlanLiveTest {

    private static final String PREFIX = "GCSAssetTest";

    private static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.GCS;
    private static final String CONNECTION_NAME = "java-sdk-" + PREFIX;
    private static final String BUCKET_NAME = PREFIX + "-bucket";
    private static final String OBJECT_NAME = PREFIX + "-object.csv";

    private static Connection connection = null;
    private static GCSBucket bucket = null;
    private static GCSObject object = null;

    @Test(groups = {"create.connection"})
    void createConnection() throws AtlanException {
        connection = ConnectionTest.createConnection(CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"create.bucket"},
            dependsOnGroups = {"create.connection"})
    void createBucket() throws AtlanException {
        GCSBucket gcsBucket =
                GCSBucket.creator(BUCKET_NAME, connection.getQualifiedName()).build();
        EntityMutationResponse response = gcsBucket.upsert();
        Entity one = validateSingleCreate(response);
        assertTrue(one instanceof GCSBucket);
        bucket = (GCSBucket) one;
        assertNotNull(bucket.getGuid());
        assertNotNull(bucket.getQualifiedName());
        assertEquals(bucket.getName(), BUCKET_NAME);
        assertEquals(bucket.getConnectorType(), AtlanConnectorType.GCS);
    }

    @Test(
            groups = {"create.object"},
            dependsOnGroups = {"create.bucket"})
    void createObject() throws AtlanException {
        GCSObject gcsObject =
                GCSObject.creator(OBJECT_NAME, bucket.getQualifiedName()).build();
        EntityMutationResponse response = gcsObject.upsert();
        assertNotNull(response);
        assertTrue(response.getDeletedEntities().isEmpty());
        assertEquals(response.getUpdatedEntities().size(), 1);
        Entity one = response.getUpdatedEntities().get(0);
        assertTrue(one instanceof GCSBucket);
        GCSBucket b = (GCSBucket) one;
        assertEquals(b.getGuid(), bucket.getGuid());
        assertEquals(b.getQualifiedName(), bucket.getQualifiedName());
        assertEquals(response.getCreatedEntities().size(), 1);
        one = response.getCreatedEntities().get(0);
        assertTrue(one instanceof GCSObject);
        object = (GCSObject) one;
        assertNotNull(object.getGuid());
        assertNotNull(object.getQualifiedName());
        assertEquals(object.getName(), OBJECT_NAME);
        assertEquals(object.getConnectorType(), AtlanConnectorType.GCS);
        assertEquals(object.getGcsBucketName(), BUCKET_NAME);
        assertEquals(object.getGcsBucketQualifiedName(), bucket.getQualifiedName());
    }

    @Test(
            groups = {"update.bucket"},
            dependsOnGroups = {"create.bucket"})
    void updateBucket() throws AtlanException {
        GCSBucket updated =
                GCSBucket.updateCertificate(bucket.getQualifiedName(), CERTIFICATE_STATUS, CERTIFICATE_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getCertificateStatus(), CERTIFICATE_STATUS);
        updated = GCSBucket.updateAnnouncement(
                bucket.getQualifiedName(), ANNOUNCEMENT_TYPE, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"read.bucket"},
            dependsOnGroups = {"create.object", "update.bucket"})
    void retrieveBucket() throws AtlanException {
        GCSBucket b = GCSBucket.retrieveByGuid(bucket.getGuid());
        assertNotNull(b);
        assertTrue(b.isComplete());
        assertEquals(b.getGuid(), bucket.getGuid());
        assertEquals(b.getQualifiedName(), bucket.getQualifiedName());
        assertEquals(b.getName(), BUCKET_NAME);
        assertEquals(b.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(b.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        assertNotNull(b.getGcsObjects());
        assertEquals(b.getGcsObjects().size(), 1);
        Set<String> types =
                b.getGcsObjects().stream().map(GCSObject::getTypeName).collect(Collectors.toSet());
        assertEquals(types.size(), 1);
        assertTrue(types.contains(GCSObject.TYPE_NAME));
        Set<String> guids = b.getGcsObjects().stream().map(GCSObject::getGuid).collect(Collectors.toSet());
        assertEquals(guids.size(), 1);
        assertTrue(guids.contains(object.getGuid()));
    }

    @Test(
            groups = {"update.bucket.again"},
            dependsOnGroups = {"read.bucket"})
    void updateBucketAgain() throws AtlanException {
        GCSBucket updated = GCSBucket.removeCertificate(bucket.getQualifiedName(), BUCKET_NAME);
        assertNotNull(updated);
        assertNull(updated.getCertificateStatus());
        assertNull(updated.getCertificateStatusMessage());
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        updated = GCSBucket.removeAnnouncement(bucket.getQualifiedName(), BUCKET_NAME);
        assertNotNull(updated);
        assertNull(updated.getAnnouncementType());
        assertNull(updated.getAnnouncementTitle());
        assertNull(updated.getAnnouncementMessage());
    }

    @Test(
            groups = {"search.assets"},
            dependsOnGroups = {"update.bucket.again"})
    void searchAssets() throws AtlanException {
        Query byState = QueryFactory.active();
        Query byType = QueryFactory.withSuperType(GCS.TYPE_NAME);
        Query byQN = QueryFactory.whereQualifiedNameStartsWith(connection.getQualifiedName());
        Query combined = BoolQuery.of(b -> b.filter(byState, byType, byQN))._toQuery();

        SortOptions sort = SortOptions.of(
                s -> s.field(FieldSort.of(f -> f.field("__timestamp").order(SortOrder.Asc))));

        IndexSearchRequest index = IndexSearchRequest.builder()
                .dsl(IndexSearchDSL.builder()
                        .from(0)
                        .size(10)
                        .query(combined)
                        .sortOption(sort)
                        .build())
                .attribute("name")
                .attribute("connectionQualifiedName")
                .build();

        IndexSearchResponse response = index.search();
        assertNotNull(response);
        assertEquals(response.getApproximateCount().longValue(), 2L);
        List<Entity> entities = response.getEntities();
        assertNotNull(entities);
        assertEquals(entities.size(), 2);

        Entity one = entities.get(0);
        assertTrue(one instanceof GCS);
        assertFalse(one.isComplete());
        GCS asset = (GCS) one;
        assertEquals(asset.getQualifiedName(), bucket.getQualifiedName());
        assertEquals(asset.getName(), bucket.getName());
        assertEquals(asset.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(1);
        assertTrue(one instanceof GCS);
        assertFalse(one.isComplete());
        asset = (GCS) one;
        assertEquals(asset.getQualifiedName(), object.getQualifiedName());
        assertEquals(asset.getName(), object.getName());
        assertEquals(asset.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"delete.object"},
            dependsOnGroups = {"update.*", "search.*"})
    void deleteObject() throws AtlanException {
        EntityMutationResponse response = Entity.delete(object.getGuid());
        assertNotNull(response);
        assertTrue(response.getCreatedEntities().isEmpty());
        assertTrue(response.getUpdatedEntities().isEmpty());
        assertEquals(response.getDeletedEntities().size(), 1);
        Entity one = response.getDeletedEntities().get(0);
        assertTrue(one instanceof GCSObject);
        GCSObject s = (GCSObject) one;
        assertEquals(s.getGuid(), object.getGuid());
        assertEquals(s.getQualifiedName(), object.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "SOFT");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"delete.object.read"},
            dependsOnGroups = {"delete.object"})
    void readDeletedObject() throws AtlanException {
        GCSObject deleted = GCSObject.retrieveByGuid(object.getGuid());
        assertEquals(deleted.getGuid(), object.getGuid());
        assertEquals(deleted.getQualifiedName(), object.getQualifiedName());
        assertEquals(deleted.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"delete.object.restore"},
            dependsOnGroups = {"delete.object.read"})
    void restoreObject() throws AtlanException {
        assertTrue(GCSObject.restore(object.getQualifiedName()));
        GCSObject restored = GCSObject.retrieveByQualifiedName(object.getQualifiedName());
        assertEquals(restored.getGuid(), object.getGuid());
        assertEquals(restored.getQualifiedName(), object.getQualifiedName());
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"purge.object"},
            dependsOnGroups = {"delete.object.restore"})
    void purgeObject() throws AtlanException {
        EntityMutationResponse response = Entity.purge(object.getGuid());
        assertNotNull(response);
        assertTrue(response.getCreatedEntities().isEmpty());
        assertTrue(response.getUpdatedEntities().isEmpty());
        assertEquals(response.getDeletedEntities().size(), 1);
        Entity one = response.getDeletedEntities().get(0);
        assertTrue(one instanceof GCSObject);
        GCSObject s = (GCSObject) one;
        assertEquals(s.getGuid(), object.getGuid());
        assertEquals(s.getQualifiedName(), object.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "HARD");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"purge.connection"},
            dependsOnGroups = {"create.*", "read.*", "search.*", "update.*", "purge.object"},
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(connection.getQualifiedName(), log);
    }
}
