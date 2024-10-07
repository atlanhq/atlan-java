/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import co.elastic.clients.elasticsearch._types.SortOrder;
import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.Connection;
import com.atlan.model.assets.IModelEntity;
import com.atlan.model.assets.IReferenceable;
import com.atlan.model.assets.ModelAttribute;
import com.atlan.model.assets.ModelDataModel;
import com.atlan.model.assets.ModelEntity;
import com.atlan.model.assets.ModelVersion;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.search.AggregationBucketResult;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

@Slf4j
public class ModelTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("Model");

    public static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.MODEL;
    public static final String CONNECTION_NAME = PREFIX;

    private static final String MODEL_NAME = PREFIX + "-model";
    private static final String ENT1_NAME = PREFIX + "-ent1";
    private static final String ENT2_NAME = PREFIX + "-ent2";
    private static final String ATTR1_NAME = PREFIX + "-attr1";
    private static final String ATTR2_NAME = PREFIX + "-attr2";

    private static Connection connection = null;
    private static ModelDataModel model = null;
    private static ModelVersion version1 = null;
    private static ModelVersion version2 = null;
    private static ModelEntity entity1 = null;
    private static ModelEntity entity2 = null;
    private static ModelAttribute attr1 = null;
    private static ModelAttribute attr2 = null;

    private static final long present = Instant.now().toEpochMilli();
    private static final long past = present - (1000 * 3600);
    private static final long future = present + (1000 * 3600);

    @Test(groups = {"model.create.connection"})
    void createConnection() throws AtlanException, InterruptedException {
        connection = ConnectionTest.createConnection(CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"model.create.model"},
            dependsOnGroups = {"model.create.connection"})
    void createModel() throws AtlanException {
        ModelDataModel toCreate = ModelDataModel.creator(MODEL_NAME, connection.getQualifiedName())
                .modelBusinessDate(past)
                .build();
        AssetMutationResponse response = toCreate.save();
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof ModelDataModel);
        model = (ModelDataModel) one;
        assertNotNull(model.getGuid());
        assertNotNull(model.getQualifiedName());
        assertEquals(model.getName(), MODEL_NAME);
        assertEquals(model.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(model.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"model.create.entity"},
            dependsOnGroups = {"model.create.model"})
    void createEntity() throws AtlanException {
        ModelEntity toCreate = ModelEntity.creator(ENT1_NAME, model)
                .modelBusinessDate(present)
                .build();
        AssetMutationResponse response = toCreate.save();
        assertNotNull(response);
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getCreatedAssets().size(), 2);
        Asset one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof ModelEntity);
        entity1 = (ModelEntity) one;
        assertNotNull(entity1.getGuid());
        assertNotNull(entity1.getQualifiedName());
        assertEquals(entity1.getName(), ENT1_NAME);
        assertEquals(entity1.getModelName(), MODEL_NAME);
        assertEquals(entity1.getModelQualifiedName(), model.getQualifiedName());
        assertEquals(entity1.getModelVersionAgnosticQualifiedName(), toCreate.getModelVersionAgnosticQualifiedName());
        // Validate a single version has been created
        one = response.getCreatedAssets().get(1);
        assertTrue(one instanceof ModelVersion);
        version1 = (ModelVersion) one;
        assertNotNull(version1.getGuid());
        assertNotNull(version1.getQualifiedName());
        assertNotNull(version1.getName());
        // TODO: assertEquals(version1.getModelName(), model.getName());
        // TODO: assertEquals(version1.getModelQualifiedName(), model.getQualifiedName());
    }

    @Test(
            groups = {"model.create.attributes"},
            dependsOnGroups = {"model.create.entity"})
    void createAttributes() throws AtlanException {
        entity2 = ModelEntity.creator(ENT2_NAME, model)
            .modelBusinessDate(future)
            .build();
        ModelAttribute first = ModelAttribute.creator(ATTR1_NAME, entity2)
                .modelBusinessDate(future)
                .build();
        ModelAttribute second = ModelAttribute.creator(ATTR2_NAME, entity2)
                .modelBusinessDate(future)
                .build();
        AssetMutationResponse response = Atlan.getDefaultClient().assets.save(List.of(entity2, first, second), false);
        assertNotNull(response);
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset parent = response.getUpdatedAssets().get(0);
        assertTrue(parent instanceof ModelVersion);
        ModelVersion old = (ModelVersion) parent;
        assertEquals(old.getGuid(), version1.getGuid());
        // TODO: validate what has actually changed on version1
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getCreatedAssets().size(), 4);
        List<Asset> sorted = response.getCreatedAssets().stream()
            .sorted(Comparator.comparing(Asset::getTypeName)
                .thenComparing(Asset::getName))
            .toList();
        Asset one = sorted.get(0);
        assertTrue(one instanceof ModelAttribute);
        attr1 = (ModelAttribute) one;
        assertNotNull(attr1.getGuid());
        assertNotNull(attr1.getQualifiedName());
        assertEquals(attr1.getName(), ATTR1_NAME);
        assertEquals(attr1.getModelName(), MODEL_NAME);
        assertEquals(attr1.getModelEntityName(), ENT2_NAME);
        one = sorted.get(1);
        assertTrue(one instanceof ModelAttribute);
        attr2 = (ModelAttribute) one;
        assertNotNull(attr2.getGuid());
        assertNotNull(attr2.getQualifiedName());
        assertEquals(attr2.getName(), ATTR2_NAME);
        assertEquals(attr2.getModelName(), MODEL_NAME);
        assertEquals(attr2.getModelEntityName(), ENT2_NAME);
        one = sorted.get(2);
        assertTrue(one instanceof ModelEntity);
        entity2 = (ModelEntity) one;
        assertNotNull(entity2.getGuid());
        assertNotNull(entity2.getQualifiedName());
        assertEquals(entity2.getName(), ENT2_NAME);
        assertEquals(entity2.getModelName(), MODEL_NAME);
        one = sorted.get(3);
        assertTrue(one instanceof ModelVersion);
        version2 = (ModelVersion) one;
        assertNotNull(version2.getGuid());
        assertNotNull(version2.getQualifiedName());
        assertNotNull(version2.getName());
        assertNotEquals(version1.getGuid(), version2.getGuid());
        // TODO: assertEquals(version2.getModelName(), model.getName());
        // TODO: assertEquals(version2.getModelQualifiedName(), model.getQualifiedName());
    }

    @Test(
            groups = {"model.read.model"},
            dependsOnGroups = {"model.create.*"})
    void readModel() throws AtlanException {
        ModelDataModel read = ModelDataModel.get(model.getQualifiedName());
        assertNotNull(read);
        assertEquals(read.getGuid(), model.getGuid());
        assertEquals(read.getQualifiedName(), model.getQualifiedName());
        assertEquals(read.getName(), model.getName());
        assertEquals(read.getConnectionQualifiedName(), model.getConnectionQualifiedName());
        assertNotNull(read.getModelVersions());
        assertEquals(read.getModelVersions().size(), 2);
    }

    @Test(
            groups = {"model.read.version"},
            dependsOnGroups = {"model.create.*"})
    void readVersion1() throws AtlanException {
        ModelVersion read = ModelVersion.get(version1.getQualifiedName());
        assertNotNull(read);
        assertEquals(read.getGuid(), version1.getGuid());
        assertEquals(read.getQualifiedName(), version1.getQualifiedName());
        assertEquals(read.getName(), version1.getName());
        assertEquals(read.getModelQualifiedName(), version1.getModelQualifiedName());
        assertNotNull(read.getModelDataModel());
        assertEquals(read.getModelDataModel().getGuid(), model.getGuid());
        assertNotNull(read.getModelVersionEntities());
        assertEquals(read.getModelVersionEntities().size(), 1);
        assertEquals(read.getModelVersionEntities().first().getGuid(), entity1.getGuid());
    }

    @Test(
            groups = {"model.read.version"},
            dependsOnGroups = {"model.create.*"})
    void readVersion2() throws AtlanException {
        ModelVersion read = ModelVersion.get(version2.getQualifiedName());
        assertNotNull(read);
        assertEquals(read.getGuid(), version2.getGuid());
        assertEquals(read.getQualifiedName(), version2.getQualifiedName());
        assertEquals(read.getName(), version2.getName());
        assertEquals(read.getModelQualifiedName(), version2.getModelQualifiedName());
        assertNotNull(read.getModelDataModel());
        assertEquals(read.getModelDataModel().getGuid(), model.getGuid());
        assertNotNull(read.getModelVersionEntities());
        assertEquals(read.getModelVersionEntities().size(), 2);
        Set<String> guids = read.getModelVersionEntities().stream().map(IModelEntity::getGuid).collect(Collectors.toSet());
        assertEquals(guids.size(), 2);
        assertTrue(guids.contains(entity1.getGuid()));
        assertTrue(guids.contains(entity2.getGuid()));
    }

    @Test(
            groups = {"model.read.entity"},
            dependsOnGroups = {"model.create.*"})
    void readEntity() throws AtlanException {
        ModelEntity read = ModelEntity.get(entity1.getQualifiedName());
        assertNotNull(read);
        assertEquals(read.getGuid(), entity1.getGuid());
        assertEquals(read.getQualifiedName(), entity1.getQualifiedName());
        assertEquals(read.getName(), entity1.getName());
        assertEquals(read.getModelQualifiedName(), model.getQualifiedName());
        assertNotNull(read.getModelVersions());
        // Note: same entity should be present in both versions
        assertEquals(read.getModelVersions().size(), 2);
    }

    @Test(
            groups = {"model.read.attribute"},
            dependsOnGroups = {"model.create.*"})
    void readAttribute1() throws AtlanException {
        ModelAttribute read = ModelAttribute.get(attr1.getQualifiedName());
        assertNotNull(read);
        assertEquals(read.getGuid(), attr1.getGuid());
        assertEquals(read.getQualifiedName(), attr1.getQualifiedName());
        assertEquals(read.getName(), attr1.getName());
        assertEquals(read.getModelEntityName(), ENT2_NAME);
        assertEquals(read.getModelEntityQualifiedName(), entity2.getQualifiedName());
        assertNotNull(read.getModelAttributeEntities());
        assertEquals(read.getModelAttributeEntities().size(), 1);
        assertEquals(read.getModelAttributeEntities().first().getGuid(), entity2.getGuid());
    }

    @Test(
            groups = {"model.read.attribute"},
            dependsOnGroups = {"model.create.*"})
    void readAttribute2() throws AtlanException {
        ModelAttribute read = ModelAttribute.get(attr2.getQualifiedName());
        assertNotNull(read);
        assertEquals(read.getGuid(), attr2.getGuid());
        assertEquals(read.getQualifiedName(), attr2.getQualifiedName());
        assertEquals(read.getName(), attr2.getName());
        assertEquals(read.getModelEntityName(), ENT2_NAME);
        assertEquals(read.getModelEntityQualifiedName(), entity2.getQualifiedName());
        assertNotNull(read.getModelAttributeEntities());
        assertEquals(read.getModelAttributeEntities().size(), 1);
        assertEquals(read.getModelAttributeEntities().first().getGuid(), entity2.getGuid());
    }

    @Test(
            groups = {"model.update.model"},
            dependsOnGroups = {"model.create.*"})
    void updateModel() throws AtlanException {
        ModelDataModel updated =
                ModelDataModel.updateCertificate(model.getQualifiedName(), CERTIFICATE_STATUS, CERTIFICATE_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(updated.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        updated = ModelDataModel.updateAnnouncement(
                model.getQualifiedName(), ANNOUNCEMENT_TYPE, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"model.read.updatedModel"},
            dependsOnGroups = {"model.update.model"})
    void readUpdatedModel() throws AtlanException {
        ModelDataModel read = ModelDataModel.get(model.getQualifiedName());
        assertNotNull(read);
        assertEquals(read.getGuid(), model.getGuid());
        assertEquals(read.getQualifiedName(), model.getQualifiedName());
        assertEquals(read.getName(), model.getName());
        assertEquals(read.getConnectorType(), model.getConnectorType());
        assertEquals(read.getConnectionQualifiedName(), model.getConnectionQualifiedName());
        assertEquals(read.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(read.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        assertEquals(read.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(read.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(read.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"model.update.model.again"},
            dependsOnGroups = {"model.read.updatedModel"})
    void updateModelAgain() throws AtlanException {
        ModelDataModel updated = ModelDataModel.removeCertificate(model.getQualifiedName(), MODEL_NAME);
        assertNotNull(updated);
        assertNull(updated.getCertificateStatus());
        assertNull(updated.getCertificateStatusMessage());
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        updated = ModelDataModel.removeAnnouncement(model.getQualifiedName(), MODEL_NAME);
        assertNotNull(updated);
        assertNull(updated.getAnnouncementType());
        assertNull(updated.getAnnouncementTitle());
        assertNull(updated.getAnnouncementMessage());
    }

    @Test(
            groups = {"model.search.assets"},
            dependsOnGroups = {"model.update.model.again"})
    void searchAssets() throws AtlanException, InterruptedException {
        IndexSearchRequest index = Atlan.getDefaultClient()
                .assets
                .select()
                .where(Asset.SUPER_TYPE_NAMES.eq("Model"))
                .where(Asset.QUALIFIED_NAME.startsWith(connection.getQualifiedName()))
                .aggregate("type", IReferenceable.TYPE_NAME.bucketBy())
                .sort(Asset.CREATE_TIME.order(SortOrder.Asc))
                .sort(Asset.NAME.order(SortOrder.Asc))
                .includeOnResults(Asset.NAME)
                .includeOnResults(Asset.CONNECTION_QUALIFIED_NAME)
                .toRequest();

        IndexSearchResponse response = retrySearchUntil(index, 7L);

        assertNotNull(response.getAggregations());
        assertEquals(response.getAggregations().size(), 1);
        assertTrue(response.getAggregations().get("type") instanceof AggregationBucketResult);
        assertEquals(
                ((AggregationBucketResult) response.getAggregations().get("type"))
                        .getBuckets()
                        .size(),
                4);

        assertEquals(response.getApproximateCount().longValue(), 7L);
        List<Asset> entities = response.getAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 7);

        Asset one = entities.get(0);
        assertTrue(one instanceof ModelDataModel);
        assertFalse(one.isComplete());
        ModelDataModel model1 = (ModelDataModel) one;
        assertEquals(model1.getQualifiedName(), model.getQualifiedName());
        assertEquals(model1.getName(), model.getName());
        assertEquals(model1.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(1);
        assertTrue(one instanceof ModelVersion);
        assertFalse(one.isComplete());
        ModelVersion v1 = (ModelVersion) one;
        assertEquals(v1.getQualifiedName(), version1.getQualifiedName());
        assertEquals(v1.getName(), version1.getName());
        assertEquals(v1.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(2);
        assertTrue(one instanceof ModelEntity);
        assertFalse(one.isComplete());
        ModelEntity e1 = (ModelEntity) one;
        assertEquals(e1.getQualifiedName(), ModelTest.entity1.getQualifiedName());
        assertEquals(e1.getName(), ModelTest.entity1.getName());
        assertEquals(e1.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(3);
        assertTrue(one instanceof ModelVersion);
        assertFalse(one.isComplete());
        ModelVersion v2 = (ModelVersion) one;
        assertEquals(v2.getQualifiedName(), version2.getQualifiedName());
        assertEquals(v2.getName(), version2.getName());
        assertEquals(v2.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(4);
        assertTrue(one instanceof ModelEntity);
        assertFalse(one.isComplete());
        ModelEntity e2 = (ModelEntity) one;
        assertEquals(e2.getQualifiedName(), ModelTest.entity2.getQualifiedName());
        assertEquals(e2.getName(), ModelTest.entity2.getName());
        assertEquals(e2.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(5);
        assertTrue(one instanceof ModelAttribute);
        assertFalse(one.isComplete());
        ModelAttribute a1 = (ModelAttribute) one;
        assertEquals(a1.getQualifiedName(), attr1.getQualifiedName());
        assertEquals(a1.getName(), attr1.getName());
        assertEquals(a1.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(6);
        assertTrue(one instanceof ModelAttribute);
        assertFalse(one.isComplete());
        ModelAttribute a2 = (ModelAttribute) one;
        assertEquals(a2.getQualifiedName(), attr2.getQualifiedName());
        assertEquals(a2.getName(), attr2.getName());
        assertEquals(a2.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    // TODO: search assets by business date

    @Test(
            groups = {"model.purge.connection"},
            dependsOnGroups = {"model.create.*", "model.read.*", "model.search.*", "model.update.*"},
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(connection.getQualifiedName(), log);
    }
}
