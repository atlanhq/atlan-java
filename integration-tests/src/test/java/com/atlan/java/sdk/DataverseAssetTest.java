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
 * Tests all aspects of Dataverse assets.
 */
@Slf4j
public class DataverseAssetTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("Dataverse");

    public static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.DATAVERSE;
    public static final String CONNECTION_NAME = PREFIX;

    private static final String ENTITY_NAME = PREFIX + "-entity";
    private static final String ATTRIBUTE_NAME = PREFIX + "-attribute";

    private static Connection connection = null;
    private static DataverseEntity entity = null;
    private static DataverseAttribute attribute = null;

    @Test(groups = {"dataverse.create.connection"})
    void createConnection() throws AtlanException, InterruptedException {
        connection = ConnectionTest.createConnection(client, CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"dataverse.create.entity"},
            dependsOnGroups = {"dataverse.create.connection"})
    void createEntity() throws AtlanException {
        DataverseEntity toCreate = DataverseEntity.creator(ENTITY_NAME, connection.getQualifiedName())
                .build();
        AssetMutationResponse response = toCreate.save(client);
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof DataverseEntity);
        entity = (DataverseEntity) one;
        assertNotNull(entity.getGuid());
        assertNotNull(entity.getQualifiedName());
        assertEquals(entity.getName(), ENTITY_NAME);
        assertEquals(entity.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(entity.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"dataverse.create.attribute"},
            dependsOnGroups = {"dataverse.create.entity"})
    void createAttribute() throws AtlanException {
        DataverseAttribute toCreate =
                DataverseAttribute.creator(ATTRIBUTE_NAME, entity).build();
        AssetMutationResponse response = toCreate.save(client);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof DataverseEntity);
        DataverseEntity c = (DataverseEntity) one;
        assertEquals(c.getGuid(), entity.getGuid());
        assertEquals(c.getQualifiedName(), entity.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof DataverseAttribute);
        attribute = (DataverseAttribute) one;
        assertNotNull(attribute.getGuid());
        assertNotNull(attribute.getQualifiedName());
        assertEquals(attribute.getName(), ATTRIBUTE_NAME);
        assertEquals(attribute.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(attribute.getDataverseEntityQualifiedName(), entity.getQualifiedName());
        assertEquals(attribute.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"dataverse.read.attribute"},
            dependsOnGroups = {"dataverse.create.*", "dataverse.update.attribute"})
    void retrieveAttribute() throws AtlanException {
        DataverseAttribute c = DataverseAttribute.get(client, attribute.getGuid(), true);
        assertNotNull(c);
        assertTrue(c.isComplete());
        assertEquals(c.getGuid(), attribute.getGuid());
        assertEquals(c.getQualifiedName(), attribute.getQualifiedName());
        assertEquals(c.getName(), ATTRIBUTE_NAME);
        assertEquals(c.getCertificateStatus(), CERTIFICATE_STATUS);
        assertNotNull(c.getDataverseEntity());
        assertEquals(c.getDataverseEntity().getTypeName(), DataverseEntity.TYPE_NAME);
        assertEquals(c.getDataverseEntity().getGuid(), entity.getGuid());
        assertNotNull(c.getDataverseEntityQualifiedName());
        assertEquals(c.getDataverseEntityQualifiedName(), entity.getQualifiedName());
    }

    @Test(
            groups = {"dataverse.update.attribute"},
            dependsOnGroups = {"dataverse.create.attribute"})
    void updateAttribute() throws AtlanException {
        DataverseAttribute updated = DataverseAttribute.updateCertificate(
                client, attribute.getQualifiedName(), CERTIFICATE_STATUS, CERTIFICATE_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(updated.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        updated = DataverseAttribute.updateAnnouncement(
                client, attribute.getQualifiedName(), ANNOUNCEMENT_TYPE, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"dataverse.search.assets"},
            dependsOnGroups = {"dataverse.create.attribute"})
    void searchAssets() throws AtlanException, InterruptedException {
        IndexSearchRequest index = client.assets
                .select()
                .where(Asset.SUPER_TYPE_NAMES.eq(IDataverse.TYPE_NAME))
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

        assertEquals(response.getApproximateCount().longValue(), 2L);
        List<Asset> entities = response.getAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 2);

        Asset one = entities.get(0);
        assertTrue(one instanceof DataverseEntity);
        assertFalse(one.isComplete());
        DataverseEntity c = (DataverseEntity) one;
        assertEquals(c.getQualifiedName(), entity.getQualifiedName());
        assertEquals(c.getName(), entity.getName());
        assertEquals(c.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(1);
        assertTrue(one instanceof DataverseAttribute);
        assertFalse(one.isComplete());
        DataverseAttribute ds = (DataverseAttribute) one;
        assertEquals(ds.getQualifiedName(), attribute.getQualifiedName());
        assertEquals(ds.getName(), attribute.getName());
        assertEquals(ds.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"dataverse.delete.attribute"},
            dependsOnGroups = {"dataverse.update.*", "dataverse.search.*"})
    void deleteAttribute() throws AtlanException {
        AssetMutationResponse response =
                Asset.delete(client, attribute.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof DataverseAttribute);
        DataverseAttribute s = (DataverseAttribute) one;
        assertEquals(s.getGuid(), attribute.getGuid());
        assertEquals(s.getQualifiedName(), attribute.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "SOFT");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"dataverse.delete.attribute.read"},
            dependsOnGroups = {"dataverse.delete.attribute"})
    void readDeletedAttribute() throws AtlanException {
        validateDeletedAsset(attribute, log);
    }

    @Test(
            groups = {"dataverse.delete.attribute.restore"},
            dependsOnGroups = {"dataverse.delete.attribute.read"})
    void restoreAttribute() throws AtlanException {
        assertTrue(DataverseAttribute.restore(client, attribute.getQualifiedName()));
        DataverseAttribute restored = DataverseAttribute.get(client, attribute.getQualifiedName());
        assertFalse(restored.isComplete());
        assertEquals(restored.getGuid(), attribute.getGuid());
        assertEquals(restored.getQualifiedName(), attribute.getQualifiedName());
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"dataverse.purge.attribute"},
            dependsOnGroups = {"dataverse.delete.attribute.restore"})
    void purgeAttribute() throws AtlanException {
        AssetMutationResponse response =
                Asset.purge(client, attribute.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof DataverseAttribute);
        DataverseAttribute s = (DataverseAttribute) one;
        assertEquals(s.getGuid(), attribute.getGuid());
        assertEquals(s.getQualifiedName(), attribute.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "PURGE");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"dataverse.purge.connection"},
            dependsOnGroups = {
                "dataverse.create.*",
                "dataverse.read.*",
                "dataverse.search.*",
                "dataverse.update.*",
                "dataverse.purge.attribute"
            },
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(client, connection.getQualifiedName(), log);
    }
}
