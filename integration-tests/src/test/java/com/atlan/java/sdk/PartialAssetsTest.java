/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.*;

import co.elastic.clients.elasticsearch._types.SortOrder;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.*;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.*;
import com.atlan.model.search.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * Tests all aspects of partial assets.
 */
@Slf4j
public class PartialAssetsTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("PA");

    public static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.S3;
    public static final String CONNECTION_NAME = PREFIX;

    private static final String BUCKET_NAME = makeUnique("mybucket");
    private static final String OBJECT_NAME = makeUnique("myobject") + ".csv";
    private static final String OBJECT_PREFIX = "/some/folder/structure";
    private static final String PARTIAL_NAME = makeUnique("obj");
    private static final String FIELD_IN_FULL_NAME = makeUnique("ff");
    private static final String FIELD_IN_PARTIAL_NAME = makeUnique("pf");

    private static Connection connection = null;
    private static S3Bucket bucket = null;
    private static S3Object object = null;
    private static PartialObject partial = null;
    private static PartialField fieldInFull = null;
    private static PartialField fieldInPartial = null;

    @Test(groups = {"pa.create.connection"})
    void createConnection() throws AtlanException, InterruptedException {
        connection = ConnectionTest.createConnection(client, CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"pa.create.bucket"},
            dependsOnGroups = {"pa.create.connection"})
    void createBucket() throws AtlanException {
        S3Bucket toCreate =
                S3Bucket.creator(BUCKET_NAME, connection.getQualifiedName()).build();
        AssetMutationResponse response = toCreate.save(client);
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof S3Bucket);
        bucket = (S3Bucket) one;
        assertNotNull(bucket.getGuid());
        assertNotNull(bucket.getQualifiedName());
        assertEquals(bucket.getName(), BUCKET_NAME);
        assertNull(bucket.getAwsArn());
        assertEquals(bucket.getConnectorType(), CONNECTOR_TYPE);
    }

    @Test(
            groups = {"pa.create.object"},
            dependsOnGroups = {"pa.create.bucket"})
    void createFullObject() throws AtlanException {
        S3Object toCreate =
                S3Object.creatorWithPrefix(OBJECT_NAME, bucket, OBJECT_PREFIX).build();
        AssetMutationResponse response = toCreate.save(client);
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
        assertEquals(object.getS3ObjectKey(), OBJECT_PREFIX + "/" + OBJECT_NAME);
        assertNull(object.getAwsArn());
        assertEquals(object.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(object.getS3BucketName(), BUCKET_NAME);
        assertEquals(object.getS3BucketQualifiedName(), bucket.getQualifiedName());
    }

    @Test(
            groups = {"pa.create.partial"},
            dependsOnGroups = {"pa.create.bucket"})
    void createPartialObject() throws AtlanException {
        PartialObject toCreate =
                PartialObject.creator(PARTIAL_NAME, S3Object.TYPE_NAME, bucket).build();
        AssetMutationResponse response = toCreate.save(client);
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
        assertTrue(one instanceof PartialObject);
        partial = (PartialObject) one;
        assertNotNull(partial.getGuid());
        assertNotNull(partial.getQualifiedName());
        assertEquals(partial.getName(), PARTIAL_NAME);
        assertEquals(partial.getPartialParentType(), S3Bucket.TYPE_NAME);
        assertEquals(partial.getPartialParentQualifiedName(), bucket.getQualifiedName());
        assertEquals(partial.getConnectorType(), CONNECTOR_TYPE);
    }

    @Test(
            groups = {"pa.create.field"},
            dependsOnGroups = {"pa.create.object", "pa.create.bucket"})
    void createPartialFieldInFull() throws AtlanException {
        PartialField toCreate =
                PartialField.creator(FIELD_IN_FULL_NAME, "S3Column", object).build();
        AssetMutationResponse response = toCreate.save(client);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof S3Object);
        S3Object o = (S3Object) one;
        assertEquals(o.getGuid(), object.getGuid());
        assertEquals(o.getQualifiedName(), object.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof PartialField);
        fieldInFull = (PartialField) one;
        assertNotNull(fieldInFull.getGuid());
        assertNotNull(fieldInFull.getQualifiedName());
        assertEquals(fieldInFull.getName(), FIELD_IN_FULL_NAME);
        assertEquals(fieldInFull.getPartialParentType(), S3Object.TYPE_NAME);
        assertEquals(fieldInFull.getPartialParentQualifiedName(), object.getQualifiedName());
        assertEquals(fieldInFull.getConnectorType(), CONNECTOR_TYPE);
    }

    @Test(
            groups = {"pa.create.field"},
            dependsOnGroups = {"pa.create.partial", "pa.create.bucket"})
    void createPartialFieldInObj() throws AtlanException {
        PartialField toCreate =
                PartialField.creator(FIELD_IN_PARTIAL_NAME, "S3Column", partial).build();
        AssetMutationResponse response = toCreate.save(client);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof PartialObject);
        PartialObject o = (PartialObject) one;
        assertEquals(o.getGuid(), partial.getGuid());
        assertEquals(o.getQualifiedName(), partial.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof PartialField);
        fieldInPartial = (PartialField) one;
        assertNotNull(fieldInPartial.getGuid());
        assertNotNull(fieldInPartial.getQualifiedName());
        assertEquals(fieldInPartial.getName(), FIELD_IN_PARTIAL_NAME);
        assertEquals(fieldInPartial.getPartialParentType(), PartialObject.TYPE_NAME);
        assertEquals(fieldInPartial.getPartialParentQualifiedName(), partial.getQualifiedName());
        assertEquals(fieldInPartial.getConnectorType(), CONNECTOR_TYPE);
    }

    @Test(
            groups = {"pa.read.object"},
            dependsOnGroups = {"pa.create.field", "pa.create.object", "pa.create.bucket"})
    void retrieveFullObject() throws AtlanException {
        S3Object o = S3Object.get(client, object.getGuid(), true);
        assertNotNull(o);
        assertTrue(o.isComplete());
        assertEquals(o.getGuid(), object.getGuid());
        assertEquals(o.getQualifiedName(), object.getQualifiedName());
        assertEquals(o.getName(), OBJECT_NAME);
        assertNotNull(o.getPartialChildFields());
        assertEquals(o.getPartialChildFields().size(), 1);
        Set<String> types = o.getPartialChildFields().stream()
                .map(IPartialField::getTypeName)
                .collect(Collectors.toSet());
        assertEquals(types.size(), 1);
        assertTrue(types.contains(PartialField.TYPE_NAME));
        Set<String> guids =
                o.getPartialChildFields().stream().map(IPartialField::getGuid).collect(Collectors.toSet());
        assertEquals(guids.size(), 1);
        assertTrue(guids.contains(fieldInFull.getGuid()));
    }

    @Test(
            groups = {"pa.read.partial"},
            dependsOnGroups = {"pa.create.field", "pa.create.partial", "pa.create.bucket"})
    void retrievePartialObject() throws AtlanException {
        PartialObject o = PartialObject.get(client, partial.getGuid(), true);
        assertNotNull(o);
        assertTrue(o.isComplete());
        assertEquals(o.getGuid(), partial.getGuid());
        assertEquals(o.getQualifiedName(), partial.getQualifiedName());
        assertEquals(o.getName(), PARTIAL_NAME);
        assertEquals(o.getPartialResolvedTypeName(), S3Object.TYPE_NAME);
        assertNotNull(o.getPartialChildFields());
        assertEquals(o.getPartialChildFields().size(), 1);
        Set<String> types = o.getPartialChildFields().stream()
                .map(IPartialField::getTypeName)
                .collect(Collectors.toSet());
        assertEquals(types.size(), 1);
        assertTrue(types.contains(PartialField.TYPE_NAME));
        Set<String> guids =
                o.getPartialChildFields().stream().map(IPartialField::getGuid).collect(Collectors.toSet());
        assertEquals(guids.size(), 1);
        assertTrue(guids.contains(fieldInPartial.getGuid()));
        assertNotNull(o.getPartialParentAsset());
        assertEquals(o.getPartialParentType(), S3Bucket.TYPE_NAME);
        assertEquals(o.getPartialParentQualifiedName(), bucket.getQualifiedName());
        assertEquals(o.getPartialParentAsset().getGuid(), bucket.getGuid());
    }

    @Test(
            groups = {"pa.read.field"},
            dependsOnGroups = {"pa.create.field", "pa.create.object", "pa.create.bucket"})
    void retrieveFieldInFull() throws AtlanException {
        PartialField f = PartialField.get(client, fieldInFull.getGuid(), true);
        assertNotNull(f);
        assertTrue(f.isComplete());
        assertEquals(f.getGuid(), fieldInFull.getGuid());
        assertEquals(f.getQualifiedName(), fieldInFull.getQualifiedName());
        assertEquals(f.getName(), FIELD_IN_FULL_NAME);
        assertEquals(f.getPartialResolvedTypeName(), "S3Column");
        assertNotNull(f.getPartialParentAsset());
        assertEquals(f.getPartialParentType(), S3Object.TYPE_NAME);
        assertEquals(f.getPartialParentQualifiedName(), object.getQualifiedName());
        assertEquals(f.getPartialParentAsset().getGuid(), object.getGuid());
    }

    @Test(
            groups = {"pa.read.field"},
            dependsOnGroups = {"pa.create.field", "pa.create.partial", "pa.create.bucket"})
    void retrieveFieldInPartial() throws AtlanException {
        PartialField f = PartialField.get(client, fieldInPartial.getGuid(), true);
        assertNotNull(f);
        assertTrue(f.isComplete());
        assertEquals(f.getGuid(), fieldInPartial.getGuid());
        assertEquals(f.getQualifiedName(), fieldInPartial.getQualifiedName());
        assertEquals(f.getName(), FIELD_IN_PARTIAL_NAME);
        assertEquals(f.getPartialResolvedTypeName(), "S3Column");
        assertNotNull(f.getPartialParentAsset());
        assertEquals(f.getPartialParentType(), PartialObject.TYPE_NAME);
        assertEquals(f.getPartialParentQualifiedName(), partial.getQualifiedName());
        assertEquals(f.getPartialParentAsset().getGuid(), partial.getGuid());
    }

    @Test(
            groups = {"pa.search.assets"},
            dependsOnGroups = {"pa.read.object", "pa.read.partial"})
    void searchPartials() throws AtlanException, InterruptedException {
        IndexSearchRequest index = client.assets
                .select()
                .where(Asset.SUPER_TYPE_NAMES.eq(IPartial.TYPE_NAME))
                .where(Asset.QUALIFIED_NAME.startsWith(connection.getQualifiedName()))
                .pageSize(10)
                .aggregate("type", IReferenceable.TYPE_NAME.bucketBy())
                .sort(Asset.CREATE_TIME.order(SortOrder.Asc))
                .includeOnResults(Asset.NAME)
                .includeOnResults(Asset.CONNECTION_QUALIFIED_NAME)
                .toRequest();

        IndexSearchResponse response = retrySearchUntil(index, 2L);

        assertNotNull(response.getAggregations());
        assertEquals(response.getAggregations().size(), 1);
        assertTrue(response.getAggregations().get("type") instanceof AggregationBucketResult);
        assertEquals(
                ((AggregationBucketResult) response.getAggregations().get("type"))
                        .getBuckets()
                        .size(),
                2);

        assertEquals(response.getApproximateCount().longValue(), 3L);
        List<Asset> entities = response.getAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 3);

        Set<String> types = entities.stream().map(IAsset::getTypeName).collect(Collectors.toSet());
        assertEquals(types.size(), 2);
        assertTrue(types.contains(PartialObject.TYPE_NAME));
        assertTrue(types.contains(PartialField.TYPE_NAME));

        Set<String> guids = entities.stream().map(IAsset::getGuid).collect(Collectors.toSet());
        assertEquals(guids.size(), 3);
        assertTrue(guids.contains(partial.getGuid()));
        assertTrue(guids.contains(fieldInFull.getGuid()));
        assertTrue(guids.contains(fieldInPartial.getGuid()));
    }

    @Test(
            groups = {"pa.delete.field"},
            dependsOnGroups = {"pa.read.*", "pa.search.*"})
    void deleteField() throws AtlanException {
        AssetMutationResponse response =
                Asset.delete(client, fieldInFull.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof PartialField);
        PartialField s = (PartialField) one;
        assertEquals(s.getGuid(), fieldInFull.getGuid());
        assertEquals(s.getQualifiedName(), fieldInFull.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "SOFT");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"pa.delete.field.read"},
            dependsOnGroups = {"pa.delete.field"})
    void readDeletedField() throws AtlanException {
        validateDeletedAsset(fieldInFull, log);
    }

    @Test(
            groups = {"pa.delete.field.restore"},
            dependsOnGroups = {"pa.delete.field.read"})
    void restoreField() throws AtlanException {
        assertTrue(PartialField.restore(client, fieldInFull.getQualifiedName()));
        PartialField restored = PartialField.get(client, fieldInFull.getQualifiedName());
        assertFalse(restored.isComplete());
        assertEquals(restored.getGuid(), fieldInFull.getGuid());
        assertEquals(restored.getQualifiedName(), fieldInFull.getQualifiedName());
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"pa.purge.field"},
            dependsOnGroups = {"pa.delete.field.restore"})
    void purgeField() throws AtlanException {
        AssetMutationResponse response =
                Asset.purge(client, fieldInFull.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof PartialField);
        PartialField s = (PartialField) one;
        assertEquals(s.getGuid(), fieldInFull.getGuid());
        assertEquals(s.getQualifiedName(), fieldInFull.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "PURGE");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"pa.purge.connection"},
            dependsOnGroups = {"pa.create.*", "pa.read.*", "pa.search.*", "pa.purge.field"},
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(client, connection.getQualifiedName(), log);
    }
}
