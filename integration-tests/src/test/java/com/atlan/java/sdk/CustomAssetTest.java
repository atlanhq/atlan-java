/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.*;

import co.elastic.clients.elasticsearch._types.SortOrder;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.*;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.search.AggregationBucketResult;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * Tests all aspects of Custom assets.
 */
@Slf4j
public class CustomAssetTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("Custom");

    public static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.CUSTOM;
    public static final String CONNECTION_NAME = PREFIX;
    private static final String ENTITY_NAME = PREFIX + "-entity";

    private static Connection connection = null;
    private static CustomEntity entity = null;

    @Test(groups = {"custom.create.connection"})
    void createConnection() throws AtlanException, InterruptedException {
        connection = ConnectionTest.createConnection(client, CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"custom.create.entity"},
            dependsOnGroups = {"custom.create.connection"})
    void createEntity() throws AtlanException {
        CustomEntity toCreate =
                CustomEntity.creator(ENTITY_NAME, connection.getQualifiedName()).build();
        AssetMutationResponse response = toCreate.save(client);
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof CustomEntity);
        entity = (CustomEntity) one;
        assertNotNull(entity.getGuid());
        assertNotNull(entity.getQualifiedName());
        assertEquals(entity.getName(), ENTITY_NAME);
        assertEquals(entity.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(entity.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"custom.update.entity"},
            dependsOnGroups = {"custom.create.entity"})
    void updateEntity() throws AtlanException {
        CustomEntity updated = CustomEntity.updateCertificate(
                client, entity.getQualifiedName(), CERTIFICATE_STATUS, CERTIFICATE_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(updated.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        updated = CustomEntity.updateAnnouncement(
                client, entity.getQualifiedName(), ANNOUNCEMENT_TYPE, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"custom.read.entity"},
            dependsOnGroups = {"custom.create.*", "custom.update.entity"})
    void retrieveEntity() throws AtlanException {
        CustomEntity c = CustomEntity.get(client, entity.getGuid(), true);
        assertNotNull(c);
        assertTrue(c.isComplete());
        assertEquals(c.getGuid(), entity.getGuid());
        assertEquals(c.getQualifiedName(), entity.getQualifiedName());
        assertEquals(c.getName(), ENTITY_NAME);
        assertEquals(c.getCertificateStatus(), CERTIFICATE_STATUS);
    }

    @Test(
            groups = {"custom.search.assets"},
            dependsOnGroups = {"custom.read.entity"})
    void searchAssets() throws AtlanException, InterruptedException {
        IndexSearchRequest index = client.assets
                .select()
                .where(Asset.SUPER_TYPE_NAMES.eq(ICustom.TYPE_NAME))
                .where(Asset.QUALIFIED_NAME.startsWith(connection.getQualifiedName()))
                .pageSize(10)
                .aggregate("type", IReferenceable.TYPE_NAME.bucketBy())
                .sort(Asset.CREATE_TIME.order(SortOrder.Asc))
                .includeOnResults(Asset.NAME)
                .includeOnResults(Asset.CONNECTION_QUALIFIED_NAME)
                .toRequest();

        IndexSearchResponse response = retrySearchUntil(index, 9L);

        assertNotNull(response.getAggregations());
        assertEquals(response.getAggregations().size(), 1);
        assertTrue(response.getAggregations().get("type") instanceof AggregationBucketResult);
        assertEquals(
                ((AggregationBucketResult) response.getAggregations().get("type"))
                        .getBuckets()
                        .size(),
                3);

        assertEquals(response.getApproximateCount().longValue(), 3L);
        List<Asset> entities = response.getAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 3);

        Asset one = entities.get(0);
        assertTrue(one instanceof CustomEntity);
        assertFalse(one.isComplete());
        CustomEntity d = (CustomEntity) one;
        assertEquals(d.getQualifiedName(), entity.getQualifiedName());
        assertEquals(d.getName(), entity.getName());
        assertEquals(d.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"custom.delete.entity"},
            dependsOnGroups = {"custom.update.*", "custom.search.*"})
    void deleteEntity() throws AtlanException {
        AssetMutationResponse response = Asset.delete(client, entity.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof CustomEntity);
        CustomEntity s = (CustomEntity) one;
        assertEquals(s.getGuid(), entity.getGuid());
        assertEquals(s.getQualifiedName(), entity.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "SOFT");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"custom.delete.entity.read"},
            dependsOnGroups = {"custom.delete.entity"})
    void readDeletedEntity() throws AtlanException {
        validateDeletedAsset(entity, log);
    }

    @Test(
            groups = {"custom.delete.entity.restore"},
            dependsOnGroups = {"custom.delete.entity.read"})
    void restoreEntity() throws AtlanException {
        assertTrue(CustomEntity.restore(client, entity.getQualifiedName()));
        CustomEntity restored = CustomEntity.get(client, entity.getQualifiedName());
        assertFalse(restored.isComplete());
        assertEquals(restored.getGuid(), entity.getGuid());
        assertEquals(restored.getQualifiedName(), entity.getQualifiedName());
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"custom.purge.entity"},
            dependsOnGroups = {"custom.delete.entity.restore"})
    void purgeEntity() throws AtlanException {
        AssetMutationResponse response = Asset.purge(client, entity.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof CustomEntity);
        CustomEntity s = (CustomEntity) one;
        assertEquals(s.getGuid(), entity.getGuid());
        assertEquals(s.getQualifiedName(), entity.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "PURGE");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"custom.purge.connection"},
            dependsOnGroups = {
                "custom.create.*",
                "custom.read.*",
                "custom.search.*",
                "custom.update.*",
                "custom.purge.entity"
            },
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(client, connection.getQualifiedName(), log);
    }
}
