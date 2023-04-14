/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static com.atlan.util.QueryFactory.*;
import static org.testng.Assert.*;

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.*;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.*;
import com.atlan.model.search.AggregationBucketResult;
import com.atlan.model.search.IndexSearchDSL;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.net.HttpClient;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * Tests all aspects of S3 assets.
 */
@Slf4j
public class S3AssetTest extends AtlanLiveTest {

    private static final String PREFIX = AtlanLiveTest.PREFIX + "S3";

    public static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.S3;
    public static final String CONNECTION_NAME = PREFIX;

    private static final String BUCKET_NAME = "mybucket";
    private static final String BUCKET_ARN = AtlanLiveTest.PREFIX + "arn:aws:s3:::mybucket";
    private static final String OBJECT_NAME = "myobject.csv";
    private static final String OBJECT_ARN = AtlanLiveTest.PREFIX + "arn:aws:s3:::mybucket/prefix/myobject.csv";

    private static Connection connection = null;
    private static S3Bucket bucket = null;
    private static S3Object object = null;

    @Test(groups = {"s3.create.connection"})
    void createConnection() throws AtlanException {
        connection = ConnectionTest.createConnection(CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"s3.create.bucket"},
            dependsOnGroups = {"s3.create.connection"})
    void createBucket() throws AtlanException {
        S3Bucket toCreate = S3Bucket.creator(BUCKET_NAME, connection.getQualifiedName(), BUCKET_ARN)
                .build();
        AssetMutationResponse response = toCreate.upsert();
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof S3Bucket);
        bucket = (S3Bucket) one;
        assertNotNull(bucket.getGuid());
        assertNotNull(bucket.getQualifiedName());
        assertEquals(bucket.getName(), BUCKET_NAME);
        assertEquals(bucket.getAwsArn(), BUCKET_ARN);
        assertEquals(bucket.getConnectorType(), AtlanConnectorType.S3);
    }

    @Test(
            groups = {"s3.create.object"},
            dependsOnGroups = {"s3.create.bucket"})
    void createObject() throws AtlanException {
        S3Object toCreate = S3Object.creator(OBJECT_NAME, bucket.getQualifiedName(), BUCKET_NAME, OBJECT_ARN)
                .build();
        AssetMutationResponse response = toCreate.upsert();
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof S3Bucket);
        S3Bucket b = (S3Bucket) one;
        assertEquals(b.getGuid(), bucket.getGuid());
        assertEquals(b.getQualifiedName(), bucket.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof S3Object);
        object = (S3Object) one;
        assertNotNull(object.getGuid());
        assertNotNull(object.getQualifiedName());
        assertEquals(object.getName(), OBJECT_NAME);
        assertEquals(object.getAwsArn(), OBJECT_ARN);
        assertEquals(object.getConnectorType(), AtlanConnectorType.S3);
        assertEquals(object.getS3BucketName(), BUCKET_NAME);
        assertEquals(object.getS3BucketQualifiedName(), bucket.getQualifiedName());
    }

    @Test(
            groups = {"s3.update.bucket"},
            dependsOnGroups = {"s3.create.bucket"})
    void updateBucket() throws AtlanException {
        S3Bucket updated =
                S3Bucket.updateCertificate(bucket.getQualifiedName(), CERTIFICATE_STATUS, CERTIFICATE_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(updated.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        updated = S3Bucket.updateAnnouncement(
                bucket.getQualifiedName(), ANNOUNCEMENT_TYPE, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"s3.read.bucket"},
            dependsOnGroups = {"s3.create.object", "s3.update.bucket"})
    void retrieveBucket() throws AtlanException {
        S3Bucket b = S3Bucket.retrieveByGuid(bucket.getGuid());
        assertNotNull(b);
        assertTrue(b.isComplete());
        assertEquals(b.getGuid(), bucket.getGuid());
        assertEquals(b.getQualifiedName(), bucket.getQualifiedName());
        assertEquals(b.getName(), BUCKET_NAME);
        assertEquals(b.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(b.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        assertNotNull(b.getObjects());
        assertEquals(b.getObjects().size(), 1);
        Set<String> types = b.getObjects().stream().map(S3Object::getTypeName).collect(Collectors.toSet());
        assertEquals(types.size(), 1);
        assertTrue(types.contains(S3Object.TYPE_NAME));
        Set<String> guids = b.getObjects().stream().map(S3Object::getGuid).collect(Collectors.toSet());
        assertEquals(guids.size(), 1);
        assertTrue(guids.contains(object.getGuid()));
    }

    @Test(
            groups = {"s3.update.bucket.again"},
            dependsOnGroups = {"s3.read.bucket"})
    void updateS3BucketAgain() throws AtlanException {
        S3Bucket updated = S3Bucket.removeCertificate(bucket.getQualifiedName(), BUCKET_NAME);
        assertNotNull(updated);
        assertNull(updated.getCertificateStatus());
        assertNull(updated.getCertificateStatusMessage());
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        updated = S3Bucket.removeAnnouncement(bucket.getQualifiedName(), BUCKET_NAME);
        assertNotNull(updated);
        assertNull(updated.getAnnouncementType());
        assertNull(updated.getAnnouncementTitle());
        assertNull(updated.getAnnouncementMessage());
    }

    @Test(
            groups = {"s3.search.assets"},
            dependsOnGroups = {"s3.update.bucket.again"})
    void searchAssets() throws AtlanException, InterruptedException {
        Query combined = CompoundQuery.builder()
                .must(beActive())
                .must(haveSuperType(S3.TYPE_NAME))
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

        int count = 0;
        while (response.getApproximateCount() < 2L && count < Atlan.getMaxNetworkRetries()) {
            Thread.sleep(HttpClient.waitTime(count).toMillis());
            response = index.search();
            count++;
        }

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
        assertTrue(one instanceof S3Bucket);
        assertFalse(one.isComplete());
        S3Bucket b = (S3Bucket) one;
        assertEquals(b.getQualifiedName(), bucket.getQualifiedName());
        assertEquals(b.getName(), bucket.getName());
        assertEquals(b.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(1);
        assertTrue(one instanceof S3Object);
        assertFalse(one.isComplete());
        S3Object o = (S3Object) one;
        assertEquals(o.getQualifiedName(), object.getQualifiedName());
        assertEquals(o.getName(), object.getName());
        assertEquals(o.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"s3.delete.object"},
            dependsOnGroups = {"s3.update.*", "s3.search.*"})
    void deleteObject() throws AtlanException {
        AssetMutationResponse response = Asset.delete(object.getGuid());
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof S3Object);
        S3Object s = (S3Object) one;
        assertEquals(s.getGuid(), object.getGuid());
        assertEquals(s.getQualifiedName(), object.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "SOFT");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"s3.delete.object.read"},
            dependsOnGroups = {"s3.delete.object"})
    void readDeletedObject() throws AtlanException {
        S3Object deleted = S3Object.retrieveByGuid(object.getGuid());
        assertEquals(deleted.getGuid(), object.getGuid());
        assertEquals(deleted.getQualifiedName(), object.getQualifiedName());
        assertEquals(deleted.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"s3.delete.object.restore"},
            dependsOnGroups = {"s3.delete.object.read"})
    void restoreObject() throws AtlanException {
        assertTrue(S3Object.restore(object.getQualifiedName()));
        S3Object restored = S3Object.retrieveByQualifiedName(object.getQualifiedName());
        assertEquals(restored.getGuid(), object.getGuid());
        assertEquals(restored.getQualifiedName(), object.getQualifiedName());
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"s3.purge.object"},
            dependsOnGroups = {"s3.delete.object.restore"})
    void purgeObject() throws AtlanException {
        AssetMutationResponse response = Asset.purge(object.getGuid());
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof S3Object);
        S3Object s = (S3Object) one;
        assertEquals(s.getGuid(), object.getGuid());
        assertEquals(s.getQualifiedName(), object.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "PURGE");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"s3.purge.connection"},
            dependsOnGroups = {"s3.create.*", "s3.read.*", "s3.search.*", "s3.update.*", "s3.purge.object"},
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(connection.getQualifiedName(), log);
    }
}
