/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static com.atlan.util.QueryFactory.*;
import static org.testng.Assert.*;

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.*;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.*;
import com.atlan.model.search.AggregationBucketResult;
import com.atlan.model.search.IndexSearchDSL;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * Tests all aspects of Google Cloud Storage assets.
 */
@Slf4j
public class GCSAssetTest extends AtlanLiveTest {

    private static final String PREFIX = "GCSAssetTest";

    public static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.GCS;
    public static final String CONNECTION_NAME = "java-sdk-" + PREFIX;
    private static final String BUCKET_NAME = PREFIX + "-bucket";
    private static final String OBJECT_NAME = PREFIX + "-object.csv";

    private static Connection connection = null;
    private static GCSBucket bucket = null;
    private static GCSObject object = null;

    @Test(groups = {"gcs.create.connection"})
    void createConnection() throws AtlanException {
        connection = ConnectionTest.createConnection(CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"gcs.create.bucket"},
            dependsOnGroups = {"gcs.create.connection"})
    void createBucket() throws AtlanException {
        GCSBucket gcsBucket =
                GCSBucket.creator(BUCKET_NAME, connection.getQualifiedName()).build();
        AssetMutationResponse response = gcsBucket.upsert();
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof GCSBucket);
        bucket = (GCSBucket) one;
        assertNotNull(bucket.getGuid());
        assertNotNull(bucket.getQualifiedName());
        assertEquals(bucket.getName(), BUCKET_NAME);
        assertEquals(bucket.getConnectorType(), AtlanConnectorType.GCS);
    }

    @Test(
            groups = {"gcs.create.object"},
            dependsOnGroups = {"gcs.create.bucket"})
    void createObject() throws AtlanException {
        GCSObject gcsObject =
                GCSObject.creator(OBJECT_NAME, bucket.getQualifiedName()).build();
        AssetMutationResponse response = gcsObject.upsert();
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof GCSBucket);
        GCSBucket b = (GCSBucket) one;
        assertEquals(b.getGuid(), bucket.getGuid());
        assertEquals(b.getQualifiedName(), bucket.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
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
            groups = {"gcs.update.bucket"},
            dependsOnGroups = {"gcs.create.bucket"})
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
            groups = {"gcs.read.bucket"},
            dependsOnGroups = {"gcs.create.object", "gcs.update.bucket"})
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
            groups = {"gcs.update.bucket.again"},
            dependsOnGroups = {"gcs.read.bucket"})
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
            groups = {"gcs.search.assets"},
            dependsOnGroups = {"gcs.update.bucket.again"})
    void searchAssets() throws AtlanException {
        Query combined = CompoundQuery.builder()
                .must(beActive())
                .must(haveSuperType(GCS.TYPE_NAME))
                .must(have(KeywordFields.QUALIFIED_NAME).startingWith(connection.getQualifiedName()))
                .build()
                ._toQuery();

        IndexSearchRequest index = IndexSearchRequest.builder()
                .dsl(IndexSearchDSL.builder()
                        .from(0)
                        .size(10)
                        .query(combined)
                        .aggregation("type", Aggregate.bucketBy(KeywordFields.TYPE_NAME))
                        .sortOption(Sort.by(NumericFields.TIMESTAMP, SortOrder.Asc))
                        .build())
                .attribute("name")
                .attribute("connectionQualifiedName")
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
                2);

        assertEquals(response.getApproximateCount().longValue(), 2L);
        List<Asset> entities = response.getAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 2);

        Asset one = entities.get(0);
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
            groups = {"gcs.delete.object"},
            dependsOnGroups = {"gcs.update.*", "gcs.search.*"})
    void deleteObject() throws AtlanException {
        AssetMutationResponse response = Asset.delete(object.getGuid());
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof GCSObject);
        GCSObject s = (GCSObject) one;
        assertEquals(s.getGuid(), object.getGuid());
        assertEquals(s.getQualifiedName(), object.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "SOFT");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"gcs.delete.object.read"},
            dependsOnGroups = {"gcs.delete.object"})
    void readDeletedObject() throws AtlanException {
        GCSObject deleted = GCSObject.retrieveByGuid(object.getGuid());
        assertEquals(deleted.getGuid(), object.getGuid());
        assertEquals(deleted.getQualifiedName(), object.getQualifiedName());
        assertEquals(deleted.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"gcs.delete.object.restore"},
            dependsOnGroups = {"gcs.delete.object.read"})
    void restoreObject() throws AtlanException {
        assertTrue(GCSObject.restore(object.getQualifiedName()));
        GCSObject restored = GCSObject.retrieveByQualifiedName(object.getQualifiedName());
        assertEquals(restored.getGuid(), object.getGuid());
        assertEquals(restored.getQualifiedName(), object.getQualifiedName());
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"gcs.purge.object"},
            dependsOnGroups = {"gcs.delete.object.restore"})
    void purgeObject() throws AtlanException {
        AssetMutationResponse response = Asset.purge(object.getGuid());
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof GCSObject);
        GCSObject s = (GCSObject) one;
        assertEquals(s.getGuid(), object.getGuid());
        assertEquals(s.getQualifiedName(), object.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "PURGE");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"gcs.purge.connection"},
            dependsOnGroups = {"gcs.create.*", "gcs.read.*", "gcs.search.*", "gcs.update.*", "gcs.purge.object"},
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(connection.getQualifiedName(), log);
    }
}
