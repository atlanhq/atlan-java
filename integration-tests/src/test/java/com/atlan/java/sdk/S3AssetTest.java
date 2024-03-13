/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.*;

import co.elastic.clients.elasticsearch._types.SortOrder;
import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.*;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.*;
import com.atlan.model.search.*;
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

    private static final String PREFIX = makeUnique("S3");

    public static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.S3;
    public static final String CONNECTION_NAME = PREFIX;

    private static final String BUCKET_NAME = makeUnique("mybucket");
    private static final String BUCKET_ARN = "arn:aws:s3:::" + BUCKET_NAME;
    private static final String OBJECT_NAME = makeUnique("myobject") + ".csv";
    private static final String OBJECT_ARN = "arn:aws:s3:::" + BUCKET_NAME + "/prefix/" + OBJECT_NAME;
    private static final String OBJECT_PREFIX = "/some/folder/structure";

    private static Connection connection = null;
    private static S3Bucket bucketARN = null;
    private static S3Bucket bucketByName = null;
    private static S3Object objectARN = null;
    private static S3Object objectByName = null;

    @Test(groups = {"s3.create.connection"})
    void createConnection() throws AtlanException, InterruptedException {
        connection = ConnectionTest.createConnection(CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"s3.create.bucket"},
            dependsOnGroups = {"s3.create.connection"})
    void createBucketARN() throws AtlanException {
        S3Bucket toCreate = S3Bucket.creator(BUCKET_NAME, connection.getQualifiedName(), BUCKET_ARN)
                .build();
        AssetMutationResponse response = toCreate.save();
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof S3Bucket);
        bucketARN = (S3Bucket) one;
        assertNotNull(bucketARN.getGuid());
        assertNotNull(bucketARN.getQualifiedName());
        assertEquals(bucketARN.getName(), BUCKET_NAME);
        assertEquals(bucketARN.getAwsArn(), BUCKET_ARN);
        assertEquals(bucketARN.getConnectorType(), AtlanConnectorType.S3);
    }

    @Test(
            groups = {"s3.create.bucket"},
            dependsOnGroups = {"s3.create.connection"})
    void createBucketByName() throws AtlanException {
        S3Bucket toCreate =
                S3Bucket.creator(BUCKET_NAME, connection.getQualifiedName()).build();
        AssetMutationResponse response = toCreate.save();
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof S3Bucket);
        bucketByName = (S3Bucket) one;
        assertNotNull(bucketByName.getGuid());
        assertNotNull(bucketByName.getQualifiedName());
        assertEquals(bucketByName.getName(), BUCKET_NAME);
        assertNull(bucketByName.getAwsArn());
        assertEquals(bucketByName.getConnectorType(), AtlanConnectorType.S3);
    }

    @Test(
            groups = {"s3.create.object"},
            dependsOnGroups = {"s3.create.bucket"})
    void createObjectARN() throws AtlanException {
        S3Object toCreate = S3Object.creator(OBJECT_NAME, bucketARN, OBJECT_ARN).build();
        AssetMutationResponse response = toCreate.save();
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof S3Bucket);
        S3Bucket b = (S3Bucket) one;
        assertEquals(b.getGuid(), bucketARN.getGuid());
        assertEquals(b.getQualifiedName(), bucketARN.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof S3Object);
        objectARN = (S3Object) one;
        assertNotNull(objectARN.getGuid());
        assertNotNull(objectARN.getQualifiedName());
        assertEquals(objectARN.getName(), OBJECT_NAME);
        assertEquals(objectARN.getAwsArn(), OBJECT_ARN);
        assertEquals(objectARN.getConnectorType(), AtlanConnectorType.S3);
        assertEquals(objectARN.getS3BucketName(), BUCKET_NAME);
        assertEquals(objectARN.getS3BucketQualifiedName(), bucketARN.getQualifiedName());
    }

    @Test(
            groups = {"s3.create.object"},
            dependsOnGroups = {"s3.create.bucket"})
    void createObjectByName() throws AtlanException {
        S3Object toCreate = S3Object.creatorWithPrefix(OBJECT_NAME, bucketByName, OBJECT_PREFIX)
                .build();
        AssetMutationResponse response = toCreate.save();
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof S3Bucket);
        S3Bucket b = (S3Bucket) one;
        assertEquals(b.getGuid(), bucketByName.getGuid());
        assertEquals(b.getQualifiedName(), bucketByName.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof S3Object);
        objectByName = (S3Object) one;
        assertNotNull(objectByName.getGuid());
        assertNotNull(objectByName.getQualifiedName());
        assertEquals(objectByName.getName(), OBJECT_NAME);
        assertEquals(objectByName.getS3ObjectKey(), OBJECT_PREFIX + "/" + OBJECT_NAME);
        assertNull(objectByName.getAwsArn());
        assertEquals(objectByName.getConnectorType(), AtlanConnectorType.S3);
        assertEquals(objectByName.getS3BucketName(), BUCKET_NAME);
        assertEquals(objectByName.getS3BucketQualifiedName(), bucketByName.getQualifiedName());
    }

    @Test(
            groups = {"s3.update.bucket"},
            dependsOnGroups = {"s3.create.bucket"})
    void updateBucketARN() throws AtlanException {
        S3Bucket updated =
                S3Bucket.updateCertificate(bucketARN.getQualifiedName(), CERTIFICATE_STATUS, CERTIFICATE_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(updated.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        updated = S3Bucket.updateAnnouncement(
                bucketARN.getQualifiedName(), ANNOUNCEMENT_TYPE, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"s3.update.bucket"},
            dependsOnGroups = {"s3.create.bucket"})
    void updateBucketByName() throws AtlanException {
        S3Bucket updated =
                S3Bucket.updateCertificate(bucketByName.getQualifiedName(), CERTIFICATE_STATUS, CERTIFICATE_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(updated.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        updated = S3Bucket.updateAnnouncement(
                bucketByName.getQualifiedName(), ANNOUNCEMENT_TYPE, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"s3.read.bucket"},
            dependsOnGroups = {"s3.create.object", "s3.update.bucket"})
    void retrieveBucketARN() throws AtlanException {
        S3Bucket b = S3Bucket.get(bucketARN.getGuid());
        assertNotNull(b);
        assertTrue(b.isComplete());
        assertEquals(b.getGuid(), bucketARN.getGuid());
        assertEquals(b.getQualifiedName(), bucketARN.getQualifiedName());
        assertEquals(b.getName(), BUCKET_NAME);
        assertEquals(b.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(b.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        assertNotNull(b.getObjects());
        assertEquals(b.getObjects().size(), 1);
        Set<String> types = b.getObjects().stream().map(IS3Object::getTypeName).collect(Collectors.toSet());
        assertEquals(types.size(), 1);
        assertTrue(types.contains(S3Object.TYPE_NAME));
        Set<String> guids = b.getObjects().stream().map(IS3Object::getGuid).collect(Collectors.toSet());
        assertEquals(guids.size(), 1);
        assertTrue(guids.contains(objectARN.getGuid()));
    }

    @Test(
            groups = {"s3.read.bucket"},
            dependsOnGroups = {"s3.create.object", "s3.update.bucket"})
    void retrieveBucketByName() throws AtlanException {
        S3Bucket b = S3Bucket.get(bucketByName.getGuid());
        assertNotNull(b);
        assertTrue(b.isComplete());
        assertEquals(b.getGuid(), bucketByName.getGuid());
        assertEquals(b.getQualifiedName(), bucketByName.getQualifiedName());
        assertEquals(b.getName(), BUCKET_NAME);
        assertEquals(b.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(b.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        assertNotNull(b.getObjects());
        assertEquals(b.getObjects().size(), 1);
        Set<String> types = b.getObjects().stream().map(IS3Object::getTypeName).collect(Collectors.toSet());
        assertEquals(types.size(), 1);
        assertTrue(types.contains(S3Object.TYPE_NAME));
        Set<String> guids = b.getObjects().stream().map(IS3Object::getGuid).collect(Collectors.toSet());
        assertEquals(guids.size(), 1);
        assertTrue(guids.contains(objectByName.getGuid()));
    }

    @Test(
            groups = {"s3.update.bucket.again"},
            dependsOnGroups = {"s3.read.bucket"})
    void updateS3BucketAgainARN() throws AtlanException {
        S3Bucket updated = S3Bucket.removeCertificate(bucketARN.getQualifiedName(), BUCKET_NAME);
        assertNotNull(updated);
        assertNull(updated.getCertificateStatus());
        assertNull(updated.getCertificateStatusMessage());
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        updated = S3Bucket.removeAnnouncement(bucketARN.getQualifiedName(), BUCKET_NAME);
        assertNotNull(updated);
        assertNull(updated.getAnnouncementType());
        assertNull(updated.getAnnouncementTitle());
        assertNull(updated.getAnnouncementMessage());
    }

    @Test(
            groups = {"s3.update.bucket.again"},
            dependsOnGroups = {"s3.read.bucket"})
    void updateS3BucketAgainByName() throws AtlanException {
        S3Bucket updated = S3Bucket.removeCertificate(bucketByName.getQualifiedName(), BUCKET_NAME);
        assertNotNull(updated);
        assertNull(updated.getCertificateStatus());
        assertNull(updated.getCertificateStatusMessage());
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        updated = S3Bucket.removeAnnouncement(bucketByName.getQualifiedName(), BUCKET_NAME);
        assertNotNull(updated);
        assertNull(updated.getAnnouncementType());
        assertNull(updated.getAnnouncementTitle());
        assertNull(updated.getAnnouncementMessage());
    }

    @Test(
            groups = {"s3.search.assets"},
            dependsOnGroups = {"s3.update.bucket.again"})
    void searchAssetsARN() throws AtlanException, InterruptedException {
        IndexSearchRequest index = Atlan.getDefaultClient()
                .assets
                .select()
                .where(CompoundQuery.superType(IS3.TYPE_NAME))
                .where(Asset.QUALIFIED_NAME.startsWith(connection.getQualifiedName()))
                .where(IAWS.AWS_ARN.hasAnyValue())
                .pageSize(10)
                .aggregate("type", IReferenceable.TYPE_NAME.bucketBy())
                .sort(Asset.CREATE_TIME.order(SortOrder.Asc))
                .includeOnResults(Asset.NAME)
                .includeOnResults(Asset.CONNECTION_QUALIFIED_NAME)
                .toRequest();

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
        assertEquals(b.getQualifiedName(), bucketARN.getQualifiedName());
        assertEquals(b.getName(), bucketARN.getName());
        assertEquals(b.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(1);
        assertTrue(one instanceof S3Object);
        assertFalse(one.isComplete());
        S3Object o = (S3Object) one;
        assertEquals(o.getQualifiedName(), objectARN.getQualifiedName());
        assertEquals(o.getName(), objectARN.getName());
        assertEquals(o.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"s3.search.assets"},
            dependsOnGroups = {"s3.update.bucket.again"})
    void searchAssetsByName() throws AtlanException, InterruptedException {
        IndexSearchRequest index = Atlan.getDefaultClient()
                .assets
                .select()
                .where(CompoundQuery.superType(IS3.TYPE_NAME))
                .where(Asset.QUALIFIED_NAME.startsWith(connection.getQualifiedName()))
                .whereNot(IAWS.AWS_ARN.hasAnyValue())
                .pageSize(10)
                .aggregate("type", IReferenceable.TYPE_NAME.bucketBy())
                .sort(Asset.CREATE_TIME.order(SortOrder.Asc))
                .includeOnResults(Asset.NAME)
                .includeOnResults(Asset.CONNECTION_QUALIFIED_NAME)
                .toRequest();

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
        assertEquals(b.getQualifiedName(), bucketByName.getQualifiedName());
        assertEquals(b.getName(), bucketByName.getName());
        assertEquals(b.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(1);
        assertTrue(one instanceof S3Object);
        assertFalse(one.isComplete());
        S3Object o = (S3Object) one;
        assertEquals(o.getQualifiedName(), objectByName.getQualifiedName());
        assertEquals(o.getName(), objectByName.getName());
        assertEquals(o.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"s3.delete.object"},
            dependsOnGroups = {"s3.update.*", "s3.search.*"})
    void deleteObjectARN() throws AtlanException {
        AssetMutationResponse response = Asset.delete(objectARN.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof S3Object);
        S3Object s = (S3Object) one;
        assertEquals(s.getGuid(), objectARN.getGuid());
        assertEquals(s.getQualifiedName(), objectARN.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "SOFT");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"s3.delete.object"},
            dependsOnGroups = {"s3.update.*", "s3.search.*"})
    void deleteObjectByName() throws AtlanException {
        AssetMutationResponse response = Asset.delete(objectByName.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof S3Object);
        S3Object s = (S3Object) one;
        assertEquals(s.getGuid(), objectByName.getGuid());
        assertEquals(s.getQualifiedName(), objectByName.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "SOFT");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"s3.delete.wait"},
            dependsOnGroups = {"s3.delete.object"})
    void forceDelete() throws InterruptedException {
        // Force a wait for consistency on deleted assets
        Thread.sleep(2000);
    }

    @Test(
            groups = {"s3.delete.object.read"},
            dependsOnGroups = {"s3.delete.wait"})
    void readDeletedObjectARN() throws AtlanException {
        S3Object deleted = S3Object.get(objectARN.getGuid());
        assertNotNull(deleted);
        assertEquals(deleted.getGuid(), objectARN.getGuid());
        assertEquals(deleted.getQualifiedName(), objectARN.getQualifiedName());
        assertEquals(deleted.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"s3.delete.object.read"},
            dependsOnGroups = {"s3.delete.wait"})
    void readDeletedObjectByName() throws AtlanException {
        S3Object deleted = S3Object.get(objectByName.getGuid());
        assertNotNull(deleted);
        assertEquals(deleted.getGuid(), objectByName.getGuid());
        assertEquals(deleted.getQualifiedName(), objectByName.getQualifiedName());
        assertEquals(deleted.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"s3.delete.object.restore"},
            dependsOnGroups = {"s3.delete.object.read"})
    void restoreObjectARN() throws AtlanException {
        assertTrue(S3Object.restore(objectARN.getQualifiedName()));
        S3Object restored = S3Object.get(objectARN.getQualifiedName());
        assertEquals(restored.getGuid(), objectARN.getGuid());
        assertEquals(restored.getQualifiedName(), objectARN.getQualifiedName());
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"s3.delete.object.restore"},
            dependsOnGroups = {"s3.delete.object.read"})
    void restoreObjectByName() throws AtlanException {
        assertTrue(S3Object.restore(objectByName.getQualifiedName()));
        S3Object restored = S3Object.get(objectByName.getQualifiedName());
        assertEquals(restored.getGuid(), objectByName.getGuid());
        assertEquals(restored.getQualifiedName(), objectByName.getQualifiedName());
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"s3.purge.object"},
            dependsOnGroups = {"s3.delete.object.restore"})
    void purgeObjectARN() throws AtlanException {
        AssetMutationResponse response = Asset.purge(objectARN.getGuid());
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof S3Object);
        S3Object s = (S3Object) one;
        assertEquals(s.getGuid(), objectARN.getGuid());
        assertEquals(s.getQualifiedName(), objectARN.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "PURGE");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"s3.purge.object"},
            dependsOnGroups = {"s3.delete.object.restore"})
    void purgeObjectByName() throws AtlanException {
        AssetMutationResponse response = Asset.purge(objectByName.getGuid());
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof S3Object);
        S3Object s = (S3Object) one;
        assertEquals(s.getGuid(), objectByName.getGuid());
        assertEquals(s.getQualifiedName(), objectByName.getQualifiedName());
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
