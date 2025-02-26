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
    private static final String PARENT_NAME = PREFIX + "-parent";
    private static final String CHILD_NAME1 = PREFIX + "-child1";
    private static final String CHILD_NAME2 = PREFIX + "-child2";

    private static Connection connection = null;
    private static CustomEntity parent = null;
    private static CustomEntity child1 = null;
    private static CustomEntity child2 = null;

    @Test(groups = {"custom.create.connection"})
    void createConnection() throws AtlanException, InterruptedException {
        connection = ConnectionTest.createConnection(client, CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"custom.create.parent"},
            dependsOnGroups = {"custom.create.connection"})
    void createParent() throws AtlanException {
        CustomEntity toCreate = CustomEntity.creator(PARENT_NAME, connection.getQualifiedName())
                .iconUrl("http://assets.atlan.com/assets/ph-bowl-food-light.svg")
                .subType("Fruit Salad")
                .build();
        AssetMutationResponse response = toCreate.save(client);
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof CustomEntity);
        parent = (CustomEntity) one;
        assertNotNull(parent.getGuid());
        assertNotNull(parent.getQualifiedName());
        assertEquals(parent.getName(), PARENT_NAME);
        assertEquals(parent.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(parent.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"custom.create.children"},
            dependsOnGroups = {"custom.create.parent"})
    void createChildren() throws AtlanException {
        CustomEntity one = CustomEntity.creator(CHILD_NAME1, connection.getQualifiedName())
                .iconUrl("http://assets.atlan.com/assets/ph-apple-logo-light.svg")
                .subType("Apple")
                .customParentEntity(CustomEntity.refByGuid(parent.getGuid()))
                .build();
        CustomEntity two = CustomEntity.creator(CHILD_NAME2, connection.getQualifiedName())
                .iconUrl("http://assets.atlan.com/assets/ph-orange-slice-light.svg")
                .subType("Orange")
                .customParentEntity(CustomEntity.refByQualifiedName(parent.getQualifiedName()))
                .customRelatedToEntity(CustomEntity.refByGuid(one.getGuid()))
                .build();
        AssetMutationResponse response = client.assets.save(List.of(one, two));
        assertNotNull(response);
        assertNotNull(response.getUpdatedAssets());
        assertEquals(response.getUpdatedAssets().size(), 1);
        assertEquals(response.getUpdatedAssets().get(0).getGuid(), parent.getGuid());
        assertNotNull(response.getCreatedAssets());
        assertEquals(response.getCreatedAssets().size(), 2);
        for (Asset asset : response.getCreatedAssets()) {
            assertTrue(asset instanceof CustomEntity);
            assertNotNull(asset.getGuid());
            assertNotNull(asset.getQualifiedName());
            assertEquals(asset.getConnectorType(), CONNECTOR_TYPE);
            assertEquals(asset.getConnectionQualifiedName(), connection.getQualifiedName());
            if (asset.getName().equals(CHILD_NAME1)) {
                child1 = (CustomEntity) asset;
            } else if (asset.getName().equals(CHILD_NAME2)) {
                child2 = (CustomEntity) asset;
            } else {
                throw new IllegalStateException("Created entity did not match either we attempted to create.");
            }
        }
    }

    @Test(
            groups = {"custom.update.parent"},
            dependsOnGroups = {"custom.create.*"})
    void updateParent() throws AtlanException {
        CustomEntity updated = CustomEntity.updateCertificate(
                client, parent.getQualifiedName(), CERTIFICATE_STATUS, CERTIFICATE_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(updated.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        updated = CustomEntity.updateAnnouncement(
                client, parent.getQualifiedName(), ANNOUNCEMENT_TYPE, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"custom.read.parent"},
            dependsOnGroups = {"custom.create.*", "custom.update.parent"})
    void retrieveParent() throws AtlanException {
        CustomEntity c = CustomEntity.get(client, parent.getGuid(), true);
        assertNotNull(c);
        assertTrue(c.isComplete());
        assertEquals(c.getGuid(), parent.getGuid());
        assertEquals(c.getQualifiedName(), parent.getQualifiedName());
        assertEquals(c.getName(), PARENT_NAME);
        assertEquals(c.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(c.getCustomChildEntities().size(), 2);
        assertEquals(c.getSubType(), "Fruit Salad");
        assertEquals(c.getIconUrl(), "http://assets.atlan.com/assets/ph-bowl-food-light.svg");
    }

    @Test(
            groups = {"custom.read.child1"},
            dependsOnGroups = {"custom.create.*", "custom.update.parent"})
    void retrieveChild1() throws AtlanException {
        CustomEntity c = CustomEntity.get(client, child1.getGuid(), true);
        assertNotNull(c);
        assertTrue(c.isComplete());
        assertEquals(c.getGuid(), child1.getGuid());
        assertEquals(c.getQualifiedName(), child1.getQualifiedName());
        assertEquals(c.getName(), CHILD_NAME1);
        assertTrue(
                c.getCustomChildEntities() == null || c.getCustomChildEntities().isEmpty());
        assertNotNull(c.getCustomParentEntity());
        assertEquals(c.getCustomParentEntity().getGuid(), parent.getGuid());
        assertFalse(c.getCustomRelatedFromEntities() == null
                || c.getCustomRelatedFromEntities().isEmpty());
        assertEquals(c.getCustomRelatedFromEntities().size(), 1);
        assertEquals(c.getCustomRelatedFromEntities().first().getGuid(), child2.getGuid());
        assertEquals(c.getSubType(), "Apple");
        assertEquals(c.getIconUrl(), "http://assets.atlan.com/assets/ph-apple-logo-light.svg");
    }

    @Test(
            groups = {"custom.search.assets"},
            dependsOnGroups = {"custom.read.*"})
    void searchAssets() throws AtlanException, InterruptedException {
        IndexSearchRequest index = client.assets
                .select()
                .where(Asset.SUPER_TYPE_NAMES.eq(ICustom.TYPE_NAME))
                .where(Asset.QUALIFIED_NAME.startsWith(connection.getQualifiedName()))
                .pageSize(10)
                .aggregate("type", IReferenceable.TYPE_NAME.bucketBy())
                .sort(Asset.CREATE_TIME.order(SortOrder.Asc))
                .sort(Asset.NAME.order(SortOrder.Asc))
                .includeOnResults(Asset.NAME)
                .includeOnResults(Asset.CONNECTION_QUALIFIED_NAME)
                .toRequest();

        IndexSearchResponse response = retrySearchUntil(index, 3L);

        assertNotNull(response.getAggregations());
        assertEquals(response.getAggregations().size(), 1);
        assertTrue(response.getAggregations().get("type") instanceof AggregationBucketResult);
        assertEquals(
                ((AggregationBucketResult) response.getAggregations().get("type"))
                        .getBuckets()
                        .size(),
                1);

        assertEquals(response.getApproximateCount().longValue(), 3L);
        List<Asset> entities = response.getAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 3);

        Asset one = entities.get(0);
        assertTrue(one instanceof CustomEntity);
        assertFalse(one.isComplete());
        CustomEntity d = (CustomEntity) one;
        assertEquals(d.getQualifiedName(), parent.getQualifiedName());
        assertEquals(d.getName(), parent.getName());
        assertEquals(d.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(1);
        assertTrue(one instanceof CustomEntity);
        assertFalse(one.isComplete());
        d = (CustomEntity) one;
        assertEquals(d.getQualifiedName(), child1.getQualifiedName());
        assertEquals(d.getName(), child1.getName());
        assertEquals(d.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(2);
        assertTrue(one instanceof CustomEntity);
        assertFalse(one.isComplete());
        d = (CustomEntity) one;
        assertEquals(d.getQualifiedName(), child2.getQualifiedName());
        assertEquals(d.getName(), child2.getName());
        assertEquals(d.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"custom.delete.child2"},
            dependsOnGroups = {"custom.update.*", "custom.search.*"})
    void deleteChild2() throws AtlanException {
        AssetMutationResponse response = Asset.delete(client, child2.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof CustomEntity);
        CustomEntity s = (CustomEntity) one;
        assertEquals(s.getGuid(), child2.getGuid());
        assertEquals(s.getQualifiedName(), child2.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "SOFT");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"custom.delete.child2.read"},
            dependsOnGroups = {"custom.delete.child2"})
    void readDeletedEntity() throws AtlanException {
        validateDeletedAsset(child2, log);
    }

    @Test(
            groups = {"custom.delete.child2.restore"},
            dependsOnGroups = {"custom.delete.child2.read"})
    void restoreEntity() throws AtlanException {
        assertTrue(CustomEntity.restore(client, child2.getQualifiedName()));
        CustomEntity restored = CustomEntity.get(client, child2.getQualifiedName());
        assertFalse(restored.isComplete());
        assertEquals(restored.getGuid(), child2.getGuid());
        assertEquals(restored.getQualifiedName(), child2.getQualifiedName());
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"custom.purge.child2"},
            dependsOnGroups = {"custom.delete.child2.restore"})
    void purgeEntity() throws AtlanException {
        AssetMutationResponse response = Asset.purge(client, child2.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof CustomEntity);
        CustomEntity s = (CustomEntity) one;
        assertEquals(s.getGuid(), child2.getGuid());
        assertEquals(s.getQualifiedName(), child2.getQualifiedName());
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
                "custom.purge.child2"
            },
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(client, connection.getQualifiedName(), log);
    }
}
