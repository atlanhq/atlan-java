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
 * Tests all aspects of Kafka assets.
 */
@Slf4j
public class KafkaTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("Kafka");

    public static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.APACHE_KAFKA;
    public static final String CONNECTION_NAME = PREFIX;
    private static final String TOPIC_NAME1 = PREFIX + "-topic1";
    private static final String TOPIC_NAME2 = PREFIX + "-topic2";
    private static final String CG_NAME = PREFIX + "-consumer-group";

    private static Connection connection = null;
    private static KafkaTopic topic1 = null;
    private static KafkaTopic topic2 = null;
    private static KafkaConsumerGroup consumerGroup = null;

    @Test(groups = {"kafka.create.connection"})
    void createConnection() throws AtlanException, InterruptedException {
        connection = ConnectionTest.createConnection(CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"kafka.create.topic1"},
            dependsOnGroups = {"kafka.create.connection"})
    void createTopic1() throws AtlanException {
        KafkaTopic kafkaTopic =
                KafkaTopic.creator(TOPIC_NAME1, connection.getQualifiedName()).build();
        AssetMutationResponse response = kafkaTopic.save();
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof KafkaTopic);
        topic1 = (KafkaTopic) one;
        assertNotNull(topic1.getGuid());
        assertNotNull(topic1.getQualifiedName());
        assertEquals(topic1.getName(), TOPIC_NAME1);
        assertEquals(topic1.getConnectorType(), CONNECTOR_TYPE);
    }

    @Test(
            groups = {"kafka.create.topic2"},
            dependsOnGroups = {"kafka.create.topic1"})
    void createTopic2() throws AtlanException {
        KafkaTopic kafkaTopic =
                KafkaTopic.creator(TOPIC_NAME2, connection.getQualifiedName()).build();
        AssetMutationResponse response = kafkaTopic.save();
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof KafkaTopic);
        topic2 = (KafkaTopic) one;
        assertNotNull(topic2.getGuid());
        assertNotNull(topic2.getQualifiedName());
        assertEquals(topic2.getName(), TOPIC_NAME2);
        assertEquals(topic2.getConnectorType(), CONNECTOR_TYPE);
    }

    @Test(
            groups = {"kafka.create.consumergroup"},
            dependsOnGroups = {"kafka.create.topic1", "kafka.create.topic2"})
    void createConsumerGroup() throws AtlanException {
        KafkaConsumerGroup cg =
                KafkaConsumerGroup.creatorObj(CG_NAME, List.of(topic1, topic2)).build();
        AssetMutationResponse response = cg.save();
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 2);
        Set<String> types =
                response.getUpdatedAssets().stream().map(Asset::getTypeName).collect(Collectors.toSet());
        assertEquals(types.size(), 1);
        assertTrue(types.contains(KafkaTopic.TYPE_NAME));
        Set<String> guids =
                response.getUpdatedAssets().stream().map(Asset::getGuid).collect(Collectors.toSet());
        assertEquals(guids.size(), 2);
        assertTrue(guids.contains(topic1.getGuid()));
        assertTrue(guids.contains(topic2.getGuid()));
        assertEquals(response.getCreatedAssets().size(), 1);
        Asset one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof KafkaConsumerGroup);
        consumerGroup = (KafkaConsumerGroup) one;
        assertNotNull(consumerGroup.getGuid());
        assertNotNull(consumerGroup.getQualifiedName());
        assertEquals(consumerGroup.getName(), CG_NAME);
        assertEquals(consumerGroup.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(consumerGroup.getKafkaTopicNames(), List.of(TOPIC_NAME1, TOPIC_NAME2));
        assertEquals(
                consumerGroup.getKafkaTopicQualifiedNames(),
                List.of(topic1.getQualifiedName(), topic2.getQualifiedName()));
    }

    @Test(
            groups = {"kafka.update.consumergroup"},
            dependsOnGroups = {"kafka.create.consumergroup"})
    void updateGroup() throws AtlanException {
        KafkaConsumerGroup updated = KafkaConsumerGroup.updateCertificate(
                consumerGroup.getQualifiedName(), CERTIFICATE_STATUS, CERTIFICATE_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getCertificateStatus(), CERTIFICATE_STATUS);
        updated = KafkaConsumerGroup.updateAnnouncement(
                consumerGroup.getQualifiedName(), ANNOUNCEMENT_TYPE, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"kafka.read.consumergroup"},
            dependsOnGroups = {"kafka.create.consumergroup", "kafka.update.consumergroup"})
    void retrieveGroup() throws AtlanException {
        KafkaConsumerGroup b = KafkaConsumerGroup.get(consumerGroup.getGuid());
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
        assertTrue(types.contains(KafkaTopic.TYPE_NAME));
        Set<String> guids =
                b.getKafkaTopics().stream().map(IKafkaTopic::getGuid).collect(Collectors.toSet());
        assertEquals(guids.size(), 2);
        assertTrue(guids.contains(topic1.getGuid()));
        assertTrue(guids.contains(topic2.getGuid()));
    }

    @Test(
            groups = {"kafka.update.consumergroup.again"},
            dependsOnGroups = {"kafka.read.consumergroup"})
    void updateGroupAgain() throws AtlanException {
        KafkaConsumerGroup updated = KafkaConsumerGroup.removeCertificate(consumerGroup.getQualifiedName(), CG_NAME);
        assertNotNull(updated);
        assertNull(updated.getCertificateStatus());
        assertNull(updated.getCertificateStatusMessage());
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        updated = KafkaConsumerGroup.removeAnnouncement(consumerGroup.getQualifiedName(), CG_NAME);
        assertNotNull(updated);
        assertNull(updated.getAnnouncementType());
        assertNull(updated.getAnnouncementTitle());
        assertNull(updated.getAnnouncementMessage());
    }

    @Test(
            groups = {"kafka.search.assets"},
            dependsOnGroups = {"kafka.update.consumergroup.again"})
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
        assertEquals(asset.getQualifiedName(), topic1.getQualifiedName());
        assertEquals(asset.getName(), topic1.getName());
        assertEquals(asset.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(1);
        assertTrue(one instanceof IKafka);
        assertFalse(one.isComplete());
        asset = (IKafka) one;
        assertEquals(asset.getQualifiedName(), topic2.getQualifiedName());
        assertEquals(asset.getName(), topic2.getName());
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
            groups = {"kafka.delete.consumergroup"},
            dependsOnGroups = {"kafka.update.*", "kafka.search.*"})
    void deleteGroup() throws AtlanException {
        AssetMutationResponse response = Asset.delete(consumerGroup.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof KafkaConsumerGroup);
        KafkaConsumerGroup s = (KafkaConsumerGroup) one;
        assertEquals(s.getGuid(), consumerGroup.getGuid());
        assertEquals(s.getQualifiedName(), consumerGroup.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "SOFT");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"kafka.delete.consumergroup.read"},
            dependsOnGroups = {"kafka.delete.consumergroup"})
    void readDeletedGroup() throws AtlanException {
        KafkaConsumerGroup deleted = KafkaConsumerGroup.get(consumerGroup.getGuid());
        assertNotNull(deleted);
        assertEquals(deleted.getGuid(), consumerGroup.getGuid());
        assertEquals(deleted.getQualifiedName(), consumerGroup.getQualifiedName());
        assertEquals(deleted.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"kafka.delete.consumergroup.restore"},
            dependsOnGroups = {"kafka.delete.consumergroup.read"})
    void restoreGroup() throws AtlanException {
        assertTrue(KafkaConsumerGroup.restore(consumerGroup.getQualifiedName()));
        KafkaConsumerGroup restored = KafkaConsumerGroup.get(consumerGroup.getQualifiedName());
        assertEquals(restored.getGuid(), consumerGroup.getGuid());
        assertEquals(restored.getQualifiedName(), consumerGroup.getQualifiedName());
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"kafka.purge.consumergroup"},
            dependsOnGroups = {"kafka.delete.consumergroup.restore"})
    void purgeGroup() throws AtlanException {
        AssetMutationResponse response = Asset.purge(consumerGroup.getGuid());
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof KafkaConsumerGroup);
        KafkaConsumerGroup s = (KafkaConsumerGroup) one;
        assertEquals(s.getGuid(), consumerGroup.getGuid());
        assertEquals(s.getQualifiedName(), consumerGroup.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "PURGE");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"kafka.purge.connection"},
            dependsOnGroups = {
                "kafka.create.*",
                "kafka.read.*",
                "kafka.search.*",
                "kafka.update.*",
                "kafka.purge.consumergroup"
            },
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(connection.getQualifiedName(), log);
    }
}
