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
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.Connection;
import com.atlan.model.assets.IModel;
import com.atlan.model.assets.IModelEntity;
import com.atlan.model.assets.IModelVersion;
import com.atlan.model.assets.IReferenceable;
import com.atlan.model.assets.ModelAttribute;
import com.atlan.model.assets.ModelAttributeAssociation;
import com.atlan.model.assets.ModelDataModel;
import com.atlan.model.assets.ModelEntity;
import com.atlan.model.assets.ModelEntityAssociation;
import com.atlan.model.assets.ModelVersion;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.DataModelType;
import com.atlan.model.enums.ModelCardinalityType;
import com.atlan.model.graph.ModelGraph;
import com.atlan.model.search.AggregationBucketResult;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

@Ignore
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
    private static final String EA1_NAME = PREFIX + "-ea1";
    private static final String AA1_NAME = PREFIX + "-aa1";

    private static Connection connection = null;
    private static ModelDataModel model = null;
    private static ModelVersion version1 = null;
    private static ModelVersion version2 = null;
    private static ModelEntity entity1 = null;
    private static ModelEntity entity2 = null;
    private static ModelAttribute attr1 = null;
    private static ModelAttribute attr2 = null;
    private static ModelEntityAssociation ea1 = null;
    private static ModelAttributeAssociation aa1 = null;

    private static final long present = Instant.now().toEpochMilli();
    private static final long past = present - (1000 * 3600);
    private static final long future = present + (1000 * 3600);

    @Test(groups = {"model.create.connection"})
    void createConnection() throws AtlanException, InterruptedException {
        connection = ConnectionTest.createConnection(client, CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"model.create.model"},
            dependsOnGroups = {"model.create.connection"})
    void createModel() throws AtlanException {
        ModelDataModel toCreate = ModelDataModel.creator(
                        MODEL_NAME, connection.getQualifiedName(), DataModelType.LOGICAL)
                .modelBusinessDate(past)
                .build();
        AssetMutationResponse response = toCreate.save(client);
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
        ModelEntity toCreate =
                ModelEntity.creator(ENT1_NAME, model).modelBusinessDate(present).build();
        AssetMutationResponse response = toCreate.save(client);
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
        assertEquals(version1.getModelExpiredAtBusinessDate(), 0);
        assertEquals(version1.getModelExpiredAtSystemDate(), 0);
        // TODO: assertEquals(version1.getModelName(), model.getName());
        // TODO: assertEquals(version1.getModelQualifiedName(), model.getQualifiedName());
    }

    @Test(
            groups = {"model.create.attributes"},
            dependsOnGroups = {"model.create.entity"})
    void createAttributes() throws AtlanException {
        // TODO: it should also be possible to create attributes without an entity in the same payload
        entity2 = ModelEntity.creator(ENT2_NAME, model)
                .modelBusinessDate(future)
                .modelEntityAttributeCount(2L)
                .build();
        ModelAttribute first = ModelAttribute.creator(ATTR1_NAME, entity2)
                .modelBusinessDate(future)
                .modelAttributeDataType("STRING")
                .modelAttributeIsPrimary(true)
                .build();
        ModelAttribute second = ModelAttribute.creator(ATTR2_NAME, entity2)
                .modelBusinessDate(future)
                .modelAttributeDataType("STRING")
                .build();
        ModelEntityAssociation ea = ModelEntityAssociation.creator(EA1_NAME, entity1, entity2)
                .modelBusinessDate(future)
                .modelEntityAssociationCardinality(ModelCardinalityType.ONE_TO_MANY)
                .build();
        ModelAttributeAssociation aa = ModelAttributeAssociation.creator(AA1_NAME, first, second)
                .modelBusinessDate(future)
                .build();
        AssetMutationResponse response = client.assets.save(List.of(entity2, first, second, ea, aa));
        assertNotNull(response);
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset parent = response.getUpdatedAssets().get(0);
        assertTrue(parent instanceof ModelVersion);
        ModelVersion old = (ModelVersion) parent;
        assertEquals(old.getGuid(), version1.getGuid());
        assertTrue(old.getModelExpiredAtBusinessDate() > 0);
        assertTrue(old.getModelExpiredAtSystemDate() > 0);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getCreatedAssets().size(), 6);
        List<Asset> sorted = response.getCreatedAssets().stream()
                .sorted(Comparator.comparing(Asset::getTypeName).thenComparing(Asset::getName))
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
        assertTrue(one instanceof ModelAttributeAssociation);
        aa1 = (ModelAttributeAssociation) one;
        assertNotNull(aa1);
        assertNotNull(aa1.getGuid());
        assertNotNull(aa1.getQualifiedName());
        assertEquals(aa1.getName(), AA1_NAME);
        assertEquals(aa1.getModelName(), MODEL_NAME);
        one = sorted.get(3);
        assertTrue(one instanceof ModelEntity);
        entity2 = (ModelEntity) one;
        assertNotNull(entity2.getGuid());
        assertNotNull(entity2.getQualifiedName());
        assertEquals(entity2.getName(), ENT2_NAME);
        assertEquals(entity2.getModelName(), MODEL_NAME);
        one = sorted.get(4);
        assertTrue(one instanceof ModelEntityAssociation);
        ea1 = (ModelEntityAssociation) one;
        assertNotNull(ea1.getGuid());
        assertNotNull(ea1.getQualifiedName());
        assertEquals(ea1.getName(), EA1_NAME);
        assertEquals(ea1.getModelEntityAssociationFromQualifiedName(), entity1.getModelVersionAgnosticQualifiedName());
        assertEquals(ea1.getModelEntityAssociationToQualifiedName(), entity2.getModelVersionAgnosticQualifiedName());
        one = sorted.get(5);
        assertTrue(one instanceof ModelVersion);
        version2 = (ModelVersion) one;
        assertNotNull(version2.getGuid());
        assertNotNull(version2.getQualifiedName());
        assertNotNull(version2.getName());
        assertNotEquals(version1.getGuid(), version2.getGuid());
        assertEquals(version2.getModelExpiredAtBusinessDate(), 0);
        assertEquals(version2.getModelExpiredAtSystemDate(), 0);
        // TODO: assertEquals(version2.getModelName(), model.getName());
        // TODO: assertEquals(version2.getModelQualifiedName(), model.getQualifiedName());
    }

    @Test(
            groups = {"model.read.model"},
            dependsOnGroups = {"model.create.*"})
    void readModel() throws AtlanException {
        ModelDataModel read = ModelDataModel.get(client, model.getQualifiedName(), true);
        assertNotNull(read);
        assertEquals(read.getGuid(), model.getGuid());
        assertEquals(read.getQualifiedName(), model.getQualifiedName());
        assertEquals(read.getName(), model.getName());
        assertEquals(read.getConnectionQualifiedName(), model.getConnectionQualifiedName());
        assertNotNull(read.getModelVersions());
        assertEquals(read.getModelVersions().size(), 2);
        Set<String> versions =
                read.getModelVersions().stream().map(IModelVersion::getGuid).collect(Collectors.toSet());
        assertTrue(versions.contains(version1.getGuid()));
        assertTrue(versions.contains(version2.getGuid()));
    }

    @Test(
            groups = {"model.read.version"},
            dependsOnGroups = {"model.create.*"})
    void readVersion1() throws AtlanException {
        ModelVersion read = ModelVersion.get(client, version1.getQualifiedName(), true);
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
        // Version 1 should now have an expiration date, since version 2 now exists
        assertTrue(read.getModelExpiredAtBusinessDate() > 0);
        assertTrue(read.getModelExpiredAtSystemDate() > 0);
    }

    @Test(
            groups = {"model.read.version"},
            dependsOnGroups = {"model.create.*"})
    void readVersion2() throws AtlanException {
        ModelVersion read = ModelVersion.get(client, version2.getQualifiedName(), true);
        assertNotNull(read);
        assertEquals(read.getGuid(), version2.getGuid());
        assertEquals(read.getQualifiedName(), version2.getQualifiedName());
        assertEquals(read.getName(), version2.getName());
        assertEquals(read.getModelQualifiedName(), version2.getModelQualifiedName());
        assertNotNull(read.getModelDataModel());
        assertEquals(read.getModelDataModel().getGuid(), model.getGuid());
        assertNotNull(read.getModelVersionEntities());
        assertEquals(read.getModelVersionEntities().size(), 2);
        Set<String> guids = read.getModelVersionEntities().stream()
                .map(IModelEntity::getGuid)
                .collect(Collectors.toSet());
        assertEquals(guids.size(), 2);
        assertTrue(guids.contains(entity1.getGuid()));
        assertTrue(guids.contains(entity2.getGuid()));
        // Version 2 should still have no expiration date, since no further versions exist
        assertEquals(read.getModelExpiredAtBusinessDate(), 0);
        assertEquals(read.getModelExpiredAtSystemDate(), 0);
    }

    @Test(
            groups = {"model.read.entity"},
            dependsOnGroups = {"model.create.*"})
    void readEntity() throws AtlanException {
        ModelEntity read = ModelEntity.get(client, entity1.getQualifiedName(), true);
        assertNotNull(read);
        assertEquals(read.getGuid(), entity1.getGuid());
        assertEquals(read.getQualifiedName(), entity1.getQualifiedName());
        assertEquals(read.getName(), entity1.getName());
        assertEquals(read.getModelQualifiedName(), model.getQualifiedName());
        assertNotNull(read.getModelVersions());
        // Note: same entity should be present in both versions
        assertEquals(read.getModelVersions().size(), 2);
        Set<String> versions =
                read.getModelVersions().stream().map(IModelVersion::getGuid).collect(Collectors.toSet());
        assertTrue(versions.contains(version1.getGuid()));
        assertTrue(versions.contains(version2.getGuid()));
        // Note: there should be a "related to" entity, but really only for version 2 (?)
        // TODO: assertFalse(read.getModelEntityRelatedToEntities().isEmpty());
        assertTrue(read.getModelEntityRelatedFromEntities().isEmpty());
    }

    @Test(
            groups = {"model.read.entityassociation"},
            dependsOnGroups = {"model.create.*"})
    void readEntityAssociation() throws AtlanException {
        ModelEntityAssociation read = ModelEntityAssociation.get(client, ea1.getQualifiedName(), true);
        assertNotNull(read);
        assertEquals(read.getGuid(), ea1.getGuid());
        assertEquals(read.getQualifiedName(), ea1.getQualifiedName());
        assertEquals(read.getName(), ea1.getName());
        assertEquals(read.getModelQualifiedName(), ea1.getModelQualifiedName());
        // Note: these should not be empty
        // TODO: assertNotNull(read.getModelEntityAssociationFrom());
        // TODO: assertEquals(read.getModelEntityAssociationFrom().getGuid(), entity1.getGuid());
        // TODO: assertNotNull(read.getModelEntityAssociationTo());
        // TODO: assertEquals(read.getModelEntityAssociationTo().getGuid(), entity2.getGuid());
    }

    @Test(
            groups = {"model.read.attribute"},
            dependsOnGroups = {"model.create.*"})
    void readAttribute1() throws AtlanException {
        ModelAttribute read = ModelAttribute.get(client, attr1.getQualifiedName(), true);
        assertNotNull(read);
        assertEquals(read.getGuid(), attr1.getGuid());
        assertEquals(read.getQualifiedName(), attr1.getQualifiedName());
        assertEquals(read.getName(), attr1.getName());
        assertEquals(read.getModelEntityName(), ENT2_NAME);
        assertEquals(read.getModelEntityQualifiedName(), entity2.getModelVersionAgnosticQualifiedName());
        assertNotNull(read.getModelAttributeEntities());
        assertEquals(read.getModelAttributeEntities().size(), 1);
        assertEquals(read.getModelAttributeEntities().first().getGuid(), entity2.getGuid());
        // Note: there should be a "related to" entity
        assertFalse(read.getModelAttributeRelatedToAttributes().isEmpty());
        assertEquals(read.getModelAttributeRelatedToAttributes().size(), 1);
        assertEquals(read.getModelAttributeRelatedToAttributes().first().getGuid(), aa1.getGuid());
        assertTrue(read.getModelAttributeRelatedFromAttributes().isEmpty());
    }

    @Test(
            groups = {"model.read.attribute"},
            dependsOnGroups = {"model.create.*"})
    void readAttribute2() throws AtlanException {
        ModelAttribute read = ModelAttribute.get(client, attr2.getQualifiedName(), true);
        assertNotNull(read);
        assertEquals(read.getGuid(), attr2.getGuid());
        assertEquals(read.getQualifiedName(), attr2.getQualifiedName());
        assertEquals(read.getName(), attr2.getName());
        assertEquals(read.getModelEntityName(), ENT2_NAME);
        assertEquals(read.getModelEntityQualifiedName(), entity2.getModelVersionAgnosticQualifiedName());
        assertNotNull(read.getModelAttributeEntities());
        assertEquals(read.getModelAttributeEntities().size(), 1);
        assertEquals(read.getModelAttributeEntities().first().getGuid(), entity2.getGuid());
        // Note: there should be a "related from" entity
        assertTrue(read.getModelAttributeRelatedToAttributes().isEmpty());
        assertFalse(read.getModelAttributeRelatedFromAttributes().isEmpty());
        assertEquals(read.getModelAttributeRelatedFromAttributes().size(), 1);
        assertEquals(read.getModelAttributeRelatedFromAttributes().first().getGuid(), aa1.getGuid());
    }

    @Test(
            groups = {"model.read.attributeassociation"},
            dependsOnGroups = {"model.create.*"})
    void readAttributeAssociation() throws AtlanException {
        ModelAttributeAssociation read = ModelAttributeAssociation.get(client, aa1.getQualifiedName(), true);
        assertNotNull(read);
        assertEquals(read.getGuid(), aa1.getGuid());
        assertEquals(read.getQualifiedName(), aa1.getQualifiedName());
        assertEquals(read.getName(), aa1.getName());
        assertEquals(read.getModelQualifiedName(), aa1.getModelQualifiedName());
        // Note: these should not be empty
        assertNotNull(read.getModelAttributeAssociationFrom());
        assertEquals(read.getModelAttributeAssociationFrom().getGuid(), attr1.getGuid());
        assertNotNull(read.getModelAttributeAssociationTo());
        assertEquals(read.getModelAttributeAssociationTo().getGuid(), attr2.getGuid());
    }

    // Update the model -- no new version expected

    @Test(
            groups = {"model.update.model"},
            dependsOnGroups = {"model.read.*"})
    void updateModel() throws AtlanException {
        ModelDataModel toUpdate = model.trimToRequired()
                .certificateStatus(CERTIFICATE_STATUS)
                .certificateStatusMessage(CERTIFICATE_MESSAGE)
                .build();
        AssetMutationResponse response = toUpdate.save(client);
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getDeletedAssets().isEmpty());
        ModelDataModel updated = response.getResult(toUpdate);
        assertNotNull(updated);
        assertEquals(updated.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(updated.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        toUpdate = model.trimToRequired()
                .announcementType(ANNOUNCEMENT_TYPE)
                .announcementTitle(ANNOUNCEMENT_TITLE)
                .announcementMessage(ANNOUNCEMENT_MESSAGE)
                .build();
        response = toUpdate.save(client);
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getDeletedAssets().isEmpty());
        updated = response.getResult(toUpdate);
        assertNotNull(updated);
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"model.reread.model"},
            dependsOnGroups = {"model.update.model"})
    void readUpdatedModel() throws AtlanException {
        ModelDataModel read = ModelDataModel.get(client, model.getQualifiedName());
        assertFalse(read.isComplete());
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
            dependsOnGroups = {"model.reread.model"})
    void updateModelAgain() throws AtlanException {
        ModelDataModel updated = ModelDataModel.removeCertificate(client, model.getQualifiedName(), MODEL_NAME);
        assertNotNull(updated);
        assertNull(updated.getCertificateStatus());
        assertNull(updated.getCertificateStatusMessage());
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        updated = ModelDataModel.removeAnnouncement(client, model.getQualifiedName(), MODEL_NAME);
        assertNotNull(updated);
        assertNull(updated.getAnnouncementType());
        assertNull(updated.getAnnouncementTitle());
        assertNull(updated.getAnnouncementMessage());
    }

    // TODO: Update the entity -- new version expected! (saving the update currently fails)

    /*@Test(
            groups = {"model.update.entity"},
            dependsOnGroups = {"model.update.model.again"})
    void updateEntity() throws AtlanException {
        ModelEntity toUpdate = entity1.trimToRequired()
                .certificateStatus(CERTIFICATE_STATUS)
                .certificateStatusMessage(CERTIFICATE_MESSAGE)
                .build();
        AssetMutationResponse response = toUpdate.save();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty()); // TODO: should be a version?
        assertTrue(response.getDeletedAssets().isEmpty());
        ModelEntity updated = response.getResult(toUpdate);
        assertNotNull(updated);
        assertEquals(updated.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(updated.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        toUpdate = entity1.trimToRequired()
                .announcementType(ANNOUNCEMENT_TYPE)
                .announcementTitle(ANNOUNCEMENT_TITLE)
                .announcementMessage(ANNOUNCEMENT_MESSAGE)
                .build();
        response = toUpdate.save();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty()); // TODO: should be a version?
        assertTrue(response.getDeletedAssets().isEmpty());
        updated = response.getResult(toUpdate);
        assertNotNull(updated);
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"model.reread.entity"},
            dependsOnGroups = {"model.update.entity"})
    void readUpdatedEntity() throws AtlanException {
        ModelEntity read = ModelEntity.get(entity1.getQualifiedName());
        assertNotNull(read);
        assertEquals(read.getGuid(), entity1.getGuid());
        assertEquals(read.getQualifiedName(), entity1.getQualifiedName());
        assertEquals(read.getName(), entity1.getName());
        assertEquals(read.getConnectorType(), entity1.getConnectorType());
        assertEquals(read.getConnectionQualifiedName(), entity1.getConnectionQualifiedName());
        assertEquals(read.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(read.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        assertEquals(read.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(read.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(read.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"model.update.entity.again"},
            dependsOnGroups = {"model.reread.entity"})
    void updateEntityAgain() throws AtlanException {
        ModelEntity updated = ModelEntity.removeCertificate(entity1.getQualifiedName(), ENT1_NAME);
        assertNotNull(updated);
        assertNull(updated.getCertificateStatus());
        assertNull(updated.getCertificateStatusMessage());
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        updated = ModelEntity.removeAnnouncement(entity1.getQualifiedName(), ENT1_NAME);
        assertNotNull(updated);
        assertNull(updated.getAnnouncementType());
        assertNull(updated.getAnnouncementTitle());
        assertNull(updated.getAnnouncementMessage());
    }*/

    @Test(
            groups = {"model.search.assets"},
            dependsOnGroups = {"model.update.model.again"})
    void searchAssets() throws AtlanException, InterruptedException {
        IndexSearchRequest index = client.assets
                .select()
                .where(Asset.SUPER_TYPE_NAMES.eq("Model"))
                .where(Asset.QUALIFIED_NAME.startsWith(connection.getQualifiedName()))
                .aggregate("type", IReferenceable.TYPE_NAME.bucketBy())
                .sort(Asset.CREATE_TIME.order(SortOrder.Asc))
                .sort(Asset.NAME.order(SortOrder.Asc))
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
                6);

        assertEquals(response.getApproximateCount().longValue(), 9L);
        List<Asset> entities = response.getAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 9);

        Asset one = entities.get(0);
        assertTrue(one instanceof ModelDataModel);
        assertFalse(one.isComplete());
        ModelDataModel model1 = (ModelDataModel) one;
        assertEquals(model1.getQualifiedName(), model.getQualifiedName());
        assertEquals(model1.getName(), model.getName());
        assertEquals(model1.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(1);
        assertTrue(one instanceof ModelEntity);
        assertFalse(one.isComplete());
        ModelEntity e1 = (ModelEntity) one;
        assertEquals(e1.getQualifiedName(), ModelTest.entity1.getQualifiedName());
        assertEquals(e1.getName(), ModelTest.entity1.getName());
        assertEquals(e1.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(2);
        assertTrue(one instanceof ModelVersion);
        assertFalse(one.isComplete());
        ModelVersion v1 = (ModelVersion) one;
        assertEquals(v1.getQualifiedName(), version1.getQualifiedName());
        assertEquals(v1.getName(), version1.getName());
        // TODO: assertEquals(v1.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(3);
        assertTrue(one instanceof ModelAttributeAssociation);
        assertFalse(one.isComplete());
        ModelAttributeAssociation atas1 = (ModelAttributeAssociation) one;
        assertEquals(atas1.getQualifiedName(), aa1.getQualifiedName());
        assertEquals(atas1.getName(), aa1.getName());
        assertEquals(atas1.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(4);
        assertTrue(one instanceof ModelAttribute);
        assertFalse(one.isComplete());
        ModelAttribute a1 = (ModelAttribute) one;
        assertEquals(a1.getQualifiedName(), attr1.getQualifiedName());
        assertEquals(a1.getName(), attr1.getName());
        assertEquals(a1.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(5);
        assertTrue(one instanceof ModelAttribute);
        assertFalse(one.isComplete());
        ModelAttribute a2 = (ModelAttribute) one;
        assertEquals(a2.getQualifiedName(), attr2.getQualifiedName());
        assertEquals(a2.getName(), attr2.getName());
        assertEquals(a2.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(6);
        assertTrue(one instanceof ModelEntityAssociation);
        assertFalse(one.isComplete());
        ModelEntityAssociation entas1 = (ModelEntityAssociation) one;
        assertEquals(entas1.getQualifiedName(), ea1.getQualifiedName());
        assertEquals(entas1.getName(), ea1.getName());
        assertEquals(entas1.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(7);
        assertTrue(one instanceof ModelEntity);
        assertFalse(one.isComplete());
        ModelEntity e2 = (ModelEntity) one;
        assertEquals(e2.getQualifiedName(), entity2.getQualifiedName());
        assertEquals(e2.getName(), entity2.getName());
        assertEquals(e2.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(8);
        assertTrue(one instanceof ModelVersion);
        assertFalse(one.isComplete());
        ModelVersion v2 = (ModelVersion) one;
        assertEquals(v2.getQualifiedName(), version2.getQualifiedName());
        assertEquals(v2.getName(), version2.getName());
        // TODO: assertEquals(v2.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"model.search.assets.byTime"},
            dependsOnGroups = {"model.search.assets"})
    void searchPreHistory() throws AtlanException {
        // Should contain nothing -- we knew nothing prior to past
        List<Asset> assets = IModel.findByTime(client, past - 1, connection.getQualifiedName(), null);
        assertNotNull(assets);
        assertTrue(assets.isEmpty());
    }

    @Test(
            groups = {"model.search.assets.byTime"},
            dependsOnGroups = {"model.search.assets"})
    void searchByPast() throws AtlanException {
        // Should be the same at this exact moment through until just up to the next version
        validatePast(past);
        validatePast(present - 1);
    }

    private void validatePast(long time) throws AtlanException {
        // Should contain only the model that was created at this time
        List<Asset> assets = IModel.findByTime(client, time, connection.getQualifiedName(), null);
        assertNotNull(assets);
        assertEquals(assets.size(), 1);
        assertEquals(assets.get(0).getGuid(), model.getGuid());
        ModelGraph g = ModelGraph.from(client, time, connection.getQualifiedName());
        assertNotNull(g);
        assertNotNull(g.getModel());
        assertEquals(g.getModel().getGuid(), model.getGuid());
        assertNull(g.getVersion());
        assertTrue(g.getEntities().isEmpty());
    }

    @Test(
            groups = {"model.search.assets.byTime"},
            dependsOnGroups = {"model.search.assets"})
    void searchByPresent() throws AtlanException {
        // Should be the same at this exact moment through until just up to the next version
        validatePresent(present);
        validatePresent(future - 1);
    }

    private void validatePresent(long time) throws AtlanException {
        // Should contain only the model (created previously) + entity that was created at this time
        List<Asset> assets = IModel.findByTime(client, time, connection.getQualifiedName(), null);
        assertNotNull(assets);
        // TODO: the modelBusinessDate on the version1 is greater than `present` so is excluded the first time, but it
        // should be included
        assertTrue(assets.size() == 2 || assets.size() == 3);
        Set<String> types = assets.stream().map(Asset::getTypeName).collect(Collectors.toSet());
        assertTrue(types.size() == 2 || assets.size() == 3);
        assertTrue(types.contains(ModelDataModel.TYPE_NAME));
        assertTrue(types.contains(ModelEntity.TYPE_NAME));
        if (types.size() == 3) {
            assertTrue(types.contains(ModelVersion.TYPE_NAME));
        }
        Set<String> guids = assets.stream().map(Asset::getGuid).collect(Collectors.toSet());
        assertTrue(guids.size() == 2 || guids.size() == 3);
        assertTrue(guids.contains(model.getGuid()));
        assertTrue(guids.contains(entity1.getGuid()));
        if (guids.size() == 3) {
            // TODO: this should always match version1 (only), not version2 (businessDate during version creation is
            // misaligned)
            assertTrue(guids.contains(version2.getGuid()));
        }
        ModelGraph g = ModelGraph.from(client, time, connection.getQualifiedName());
        assertNotNull(g);
        assertNotNull(g.getModel());
        assertEquals(g.getModel().getGuid(), model.getGuid());
        if (guids.size() == 3) {
            assertNotNull(g.getVersion());
            assertEquals(g.getVersion().getGuid(), version2.getGuid());
        }
        assertNotNull(g.getEntities());
        assertEquals(g.getEntities().size(), 1);
        assertTrue(g.getEntities().get(0).getAttributes().isEmpty());
    }

    @Test(
            groups = {"model.search.assets.byTime"},
            dependsOnGroups = {"model.search.assets"})
    void searchByFuture() throws AtlanException {
        // Should be the same at this exact moment through to beyond (no subsequent versions to close this one)
        validateFuture(future);
        validateFuture(future + 10000);
    }

    private void validateFuture(long time) throws AtlanException {
        // Should contain all of:
        // - the model (created previously)
        // - the entity (created previously)
        // - another entity + 2 attributes that were created at this time
        // - an entity association
        // - an attribute association
        List<Asset> assets = IModel.findByTime(client, time, connection.getQualifiedName(), null);
        assertNotNull(assets);
        assertEquals(assets.size(), 8);
        Set<String> types = assets.stream().map(Asset::getTypeName).collect(Collectors.toSet());
        assertEquals(types.size(), 6);
        assertTrue(types.contains(ModelDataModel.TYPE_NAME));
        assertTrue(types.contains(ModelEntity.TYPE_NAME));
        assertTrue(types.contains(ModelAttribute.TYPE_NAME));
        assertTrue(types.contains(ModelVersion.TYPE_NAME));
        assertTrue(types.contains(ModelEntityAssociation.TYPE_NAME));
        assertTrue(types.contains(ModelAttributeAssociation.TYPE_NAME));
        Set<String> guids = assets.stream().map(Asset::getGuid).collect(Collectors.toSet());
        assertEquals(guids.size(), 8);
        assertTrue(guids.contains(model.getGuid()));
        assertTrue(guids.contains(version2.getGuid()));
        assertTrue(guids.contains(entity1.getGuid()));
        assertTrue(guids.contains(entity2.getGuid()));
        assertTrue(guids.contains(attr1.getGuid()));
        assertTrue(guids.contains(attr2.getGuid()));
        assertTrue(guids.contains(ea1.getGuid()));
        assertTrue(guids.contains(aa1.getGuid()));
        ModelGraph g = ModelGraph.from(client, time, connection.getQualifiedName());
        assertNotNull(g);
        assertNotNull(g.getModel());
        assertEquals(g.getModel().getGuid(), model.getGuid());
        assertNotNull(g.getVersion());
        assertEquals(g.getVersion().getGuid(), version2.getGuid());
        assertNotNull(g.getEntities());
        assertEquals(g.getEntities().size(), 2);
        g.getEntities().forEach(it -> {
            if (it.getDetails().getGuid().equals(entity1.getGuid())) {
                assertTrue(it.getAttributes().isEmpty());
            } else {
                assertFalse(it.getAttributes().isEmpty());
                assertEquals(it.getAttributes().size(), 2);
                Set<String> attrs =
                        it.getAttributes().stream().map(ModelAttribute::getGuid).collect(Collectors.toSet());
                assertEquals(attrs.size(), 2);
                assertTrue(attrs.contains(attr1.getGuid()));
                assertTrue(attrs.contains(attr2.getGuid()));
            }
        });
    }

    @Test(
            groups = {"model.purge.connection"},
            dependsOnGroups = {"model.create.*", "model.read.*", "model.reread.*", "model.search.*", "model.update.*"},
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(client, connection.getQualifiedName(), log);
    }
}
