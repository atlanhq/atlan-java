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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * Tests all aspects of Azure Event Hub assets.
 */
@Slf4j
public class AzureEventHubTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("AEH");

    public static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.AZURE_EVENT_HUB;
    public static final String CONNECTION_NAME = PREFIX;
    private static final String HUB_NAME1 = PREFIX + "-hub1";
    private static final String HUB_NAME2 = PREFIX + "-hub2";
    private static final String CG_NAME = PREFIX + "-consumer-group";

    private static Connection connection = null;
    private static AzureEventHub hub1 = null;
    private static AzureEventHub hub2 = null;
    private static AzureEventHubConsumerGroup consumerGroup = null;

    @Test(groups = {"aeh.create.connection"})
    void createConnection() throws AtlanException, InterruptedException {
        connection = ConnectionTest.createConnection(CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"aeh.create.hub1"},
            dependsOnGroups = {"aeh.create.connection"})
    void createHub1() throws AtlanException {
        AzureEventHub azureEventHub =
                AzureEventHub.creator(HUB_NAME1, connection.getQualifiedName()).build();
        AssetMutationResponse response = azureEventHub.save();
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof AzureEventHub);
        hub1 = (AzureEventHub) one;
        assertNotNull(hub1.getGuid());
        assertNotNull(hub1.getQualifiedName());
        assertEquals(hub1.getName(), HUB_NAME1);
        assertEquals(hub1.getConnectorType(), CONNECTOR_TYPE);
    }

    @Test(
            groups = {"aeh.create.hub2"},
            dependsOnGroups = {"aeh.create.hub1"})
    void createHub2() throws AtlanException {
        AzureEventHub azureEventHub =
                AzureEventHub.creator(HUB_NAME2, connection.getQualifiedName()).build();
        AssetMutationResponse response = azureEventHub.save();
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof AzureEventHub);
        hub2 = (AzureEventHub) one;
        assertNotNull(hub2.getGuid());
        assertNotNull(hub2.getQualifiedName());
        assertEquals(hub2.getName(), HUB_NAME2);
        assertEquals(hub2.getConnectorType(), CONNECTOR_TYPE);
    }

    @Test(
            groups = {"aeh.create.consumergroup"},
            dependsOnGroups = {"aeh.create.hub1", "aeh.create.hub2"})
    void createConsumerGroup() throws AtlanException {
        AzureEventHubConsumerGroup cg = AzureEventHubConsumerGroup.creatorObj(CG_NAME, List.of(hub1, hub2))
                .build();
        AssetMutationResponse response = cg.save();
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 2);
        Set<String> types =
                response.getUpdatedAssets().stream().map(Asset::getTypeName).collect(Collectors.toSet());
        assertEquals(types.size(), 1);
        assertTrue(types.contains(AzureEventHub.TYPE_NAME));
        Set<String> guids =
                response.getUpdatedAssets().stream().map(Asset::getGuid).collect(Collectors.toSet());
        assertEquals(guids.size(), 2);
        assertTrue(guids.contains(hub1.getGuid()));
        assertTrue(guids.contains(hub2.getGuid()));
        assertEquals(response.getCreatedAssets().size(), 1);
        Asset one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof AzureEventHubConsumerGroup);
        consumerGroup = (AzureEventHubConsumerGroup) one;
        assertNotNull(consumerGroup.getGuid());
        assertNotNull(consumerGroup.getQualifiedName());
        assertEquals(consumerGroup.getName(), CG_NAME);
        assertEquals(consumerGroup.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(consumerGroup.getKafkaTopicNames(), List.of(HUB_NAME1, HUB_NAME2));
        assertEquals(
                consumerGroup.getKafkaTopicQualifiedNames(), List.of(hub1.getQualifiedName(), hub2.getQualifiedName()));
    }

    @Test(
            groups = {"aeh.update.consumergroup"},
            dependsOnGroups = {"aeh.create.consumergroup"})
    void updateGroup() throws AtlanException {
        AzureEventHubConsumerGroup updated = AzureEventHubConsumerGroup.updateCertificate(
                consumerGroup.getQualifiedName(), CERTIFICATE_STATUS, CERTIFICATE_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getCertificateStatus(), CERTIFICATE_STATUS);
        updated = AzureEventHubConsumerGroup.updateAnnouncement(
                consumerGroup.getQualifiedName(), ANNOUNCEMENT_TYPE, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"aeh.read.consumergroup"},
            dependsOnGroups = {"aeh.create.consumergroup", "aeh.update.consumergroup"})
    void retrieveGroup() throws AtlanException {
        AzureEventHubConsumerGroup b = AzureEventHubConsumerGroup.get(consumerGroup.getGuid());
        assertNotNull(b);
        assertTrue(b.isComplete());
        assertEquals(b.getGuid(), consumerGroup.getGuid());
        assertEquals(b.getQualifiedName(), consumerGroup.getQualifiedName());
        assertEquals(b.getName(), CG_NAME);
        assertEquals(b.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(b.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        assertNotNull(b.getKafkaTopics());
        assertEquals(b.getKafkaTopics().size(), 2);
        Set<String> types =
                b.getKafkaTopics().stream().map(IKafkaTopic::getTypeName).collect(Collectors.toSet());
        assertEquals(types.size(), 1);
        assertTrue(types.contains(AzureEventHub.TYPE_NAME));
        Set<String> guids =
                b.getKafkaTopics().stream().map(IKafkaTopic::getGuid).collect(Collectors.toSet());
        assertEquals(guids.size(), 2);
        assertTrue(guids.contains(hub1.getGuid()));
        assertTrue(guids.contains(hub2.getGuid()));
    }

    @Test(
            groups = {"aeh.update.consumergroup.again"},
            dependsOnGroups = {"aeh.read.consumergroup"})
    void updateGroupAgain() throws AtlanException {
        AzureEventHubConsumerGroup updated =
                AzureEventHubConsumerGroup.removeCertificate(consumerGroup.getQualifiedName(), CG_NAME);
        assertNotNull(updated);
        assertNull(updated.getCertificateStatus());
        assertNull(updated.getCertificateStatusMessage());
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        updated = AzureEventHubConsumerGroup.removeAnnouncement(consumerGroup.getQualifiedName(), CG_NAME);
        assertNotNull(updated);
        assertNull(updated.getAnnouncementType());
        assertNull(updated.getAnnouncementTitle());
        assertNull(updated.getAnnouncementMessage());
    }

    @Test(
            groups = {"aeh.search.assets"},
            dependsOnGroups = {"aeh.update.consumergroup.again"})
    void searchAssets() throws AtlanException, InterruptedException {
        IndexSearchRequest index = Atlan.getDefaultClient()
                .assets
                .select()
                .where(CompoundQuery.superType(IKafka.TYPE_NAME))
                .where(Asset.QUALIFIED_NAME.startsWith(connection.getQualifiedName()))
                .pageSize(10)
                .aggregate("type", IReferenceable.TYPE_NAME.bucketBy())
                .sort(Asset.CREATE_TIME.order(SortOrder.Asc))
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
                2);

        assertEquals(response.getApproximateCount().longValue(), 3L);
        List<Asset> entities = response.getAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 3);

        Asset one = entities.get(0);
        assertTrue(one instanceof IKafka);
        assertFalse(one.isComplete());
        IKafka asset = (IKafka) one;
        assertEquals(asset.getQualifiedName(), hub1.getQualifiedName());
        assertEquals(asset.getName(), hub1.getName());
        assertEquals(asset.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(1);
        assertTrue(one instanceof IKafka);
        assertFalse(one.isComplete());
        asset = (IKafka) one;
        assertEquals(asset.getQualifiedName(), hub2.getQualifiedName());
        assertEquals(asset.getName(), hub2.getName());
        assertEquals(asset.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(2);
        assertTrue(one instanceof IKafka);
        assertFalse(one.isComplete());
        asset = (IKafka) one;
        assertEquals(asset.getQualifiedName(), consumerGroup.getQualifiedName());
        assertEquals(asset.getName(), consumerGroup.getName());
        assertEquals(asset.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"aeh.delete.consumergroup"},
            dependsOnGroups = {"aeh.update.*", "aeh.search.*"})
    void deleteGroup() throws AtlanException {
        AssetMutationResponse response = Asset.delete(consumerGroup.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof AzureEventHubConsumerGroup);
        AzureEventHubConsumerGroup s = (AzureEventHubConsumerGroup) one;
        assertEquals(s.getGuid(), consumerGroup.getGuid());
        assertEquals(s.getQualifiedName(), consumerGroup.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "SOFT");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"aeh.delete.consumergroup.read"},
            dependsOnGroups = {"aeh.delete.consumergroup"})
    void readDeletedGroup() throws AtlanException {
        AzureEventHubConsumerGroup deleted = AzureEventHubConsumerGroup.get(consumerGroup.getGuid());
        assertNotNull(deleted);
        assertEquals(deleted.getGuid(), consumerGroup.getGuid());
        assertEquals(deleted.getQualifiedName(), consumerGroup.getQualifiedName());
        assertEquals(deleted.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"aeh.delete.consumergroup.restore"},
            dependsOnGroups = {"aeh.delete.consumergroup.read"})
    void restoreGroup() throws AtlanException {
        assertTrue(AzureEventHubConsumerGroup.restore(consumerGroup.getQualifiedName()));
        AzureEventHubConsumerGroup restored = AzureEventHubConsumerGroup.get(consumerGroup.getQualifiedName());
        assertEquals(restored.getGuid(), consumerGroup.getGuid());
        assertEquals(restored.getQualifiedName(), consumerGroup.getQualifiedName());
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"aeh.purge.consumergroup"},
            dependsOnGroups = {"aeh.delete.consumergroup.restore"})
    void purgeGroup() throws AtlanException {
        AssetMutationResponse response = Asset.purge(consumerGroup.getGuid());
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof AzureEventHubConsumerGroup);
        AzureEventHubConsumerGroup s = (AzureEventHubConsumerGroup) one;
        assertEquals(s.getGuid(), consumerGroup.getGuid());
        assertEquals(s.getQualifiedName(), consumerGroup.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "PURGE");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"aeh.purge.connection"},
            dependsOnGroups = {"aeh.create.*", "aeh.read.*", "aeh.search.*", "aeh.update.*", "aeh.purge.consumergroup"},
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(connection.getQualifiedName(), log);
    }
}
