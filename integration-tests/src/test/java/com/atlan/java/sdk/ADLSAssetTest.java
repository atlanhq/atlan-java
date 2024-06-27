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
 * Tests all aspects of Azure Data Lake Storage assets.
 */
@Slf4j
public class ADLSAssetTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("ADLS");

    public static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.ADLS;
    public static final String CONNECTION_NAME = PREFIX;
    private static final String ACCOUNT_NAME = PREFIX + "-account";
    private static final String CONTAINER_NAME = PREFIX + "-container";
    private static final String OBJECT_NAME = PREFIX + "-object.csv";

    private static Connection connection = null;
    private static ADLSAccount account = null;
    private static ADLSContainer container = null;
    private static ADLSObject object = null;

    @Test(groups = {"adls.create.connection"})
    void createConnection() throws AtlanException, InterruptedException {
        connection = ConnectionTest.createConnection(CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"adls.create.account"},
            dependsOnGroups = {"adls.create.connection"})
    void createAccount() throws AtlanException {
        ADLSAccount adlsAccount =
                ADLSAccount.creator(ACCOUNT_NAME, connection.getQualifiedName()).build();
        AssetMutationResponse response = adlsAccount.save();
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof ADLSAccount);
        account = (ADLSAccount) one;
        assertNotNull(account.getGuid());
        assertNotNull(account.getQualifiedName());
        assertEquals(account.getName(), ACCOUNT_NAME);
        assertEquals(account.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(account.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"adls.create.container"},
            dependsOnGroups = {"adls.create.account"})
    void createContainer() throws AtlanException {
        ADLSContainer adlsContainer =
                ADLSContainer.creator(CONTAINER_NAME, account).build();
        AssetMutationResponse response = adlsContainer.save();
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof ADLSAccount);
        ADLSAccount a = (ADLSAccount) one;
        assertEquals(a.getGuid(), account.getGuid());
        assertEquals(a.getQualifiedName(), account.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof ADLSContainer);
        container = (ADLSContainer) one;
        assertNotNull(container.getGuid());
        assertNotNull(container.getQualifiedName());
        assertEquals(container.getName(), CONTAINER_NAME);
        assertEquals(container.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(container.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"adls.create.object"},
            dependsOnGroups = {"adls.create.container"})
    void createObject() throws AtlanException {
        ADLSObject adlsObject = ADLSObject.creator(OBJECT_NAME, container).build();
        AssetMutationResponse response = adlsObject.save();
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof ADLSContainer);
        ADLSContainer b = (ADLSContainer) one;
        assertEquals(b.getGuid(), container.getGuid());
        assertEquals(b.getQualifiedName(), container.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof ADLSObject);
        object = (ADLSObject) one;
        assertNotNull(object.getGuid());
        assertNotNull(object.getQualifiedName());
        assertEquals(object.getName(), OBJECT_NAME);
        assertEquals(object.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(object.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"adls.update.container"},
            dependsOnGroups = {"adls.create.container"})
    void updateContainer() throws AtlanException {
        ADLSContainer updated =
                ADLSContainer.updateCertificate(container.getQualifiedName(), CERTIFICATE_STATUS, CERTIFICATE_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getCertificateStatus(), CERTIFICATE_STATUS);
        updated = ADLSContainer.updateAnnouncement(
                container.getQualifiedName(), ANNOUNCEMENT_TYPE, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"adls.read.container"},
            dependsOnGroups = {"adls.create.object", "adls.update.container"})
    void retrieveContainer() throws AtlanException {
        ADLSContainer b = ADLSContainer.get(container.getGuid());
        assertNotNull(b);
        assertTrue(b.isComplete());
        assertEquals(b.getGuid(), container.getGuid());
        assertEquals(b.getQualifiedName(), container.getQualifiedName());
        assertEquals(b.getName(), CONTAINER_NAME);
        assertEquals(b.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(b.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        assertNotNull(b.getAdlsObjects());
        assertEquals(b.getAdlsObjects().size(), 1);
        Set<String> types =
                b.getAdlsObjects().stream().map(IADLSObject::getTypeName).collect(Collectors.toSet());
        assertEquals(types.size(), 1);
        assertTrue(types.contains(ADLSObject.TYPE_NAME));
        Set<String> guids =
                b.getAdlsObjects().stream().map(IADLSObject::getGuid).collect(Collectors.toSet());
        assertEquals(guids.size(), 1);
        assertTrue(guids.contains(object.getGuid()));
    }

    @Test(
            groups = {"adls.update.container.again"},
            dependsOnGroups = {"adls.read.container"})
    void updateContainerAgain() throws AtlanException {
        ADLSContainer updated = ADLSContainer.removeCertificate(container.getQualifiedName(), CONTAINER_NAME);
        assertNotNull(updated);
        assertNull(updated.getCertificateStatus());
        assertNull(updated.getCertificateStatusMessage());
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        updated = ADLSContainer.removeAnnouncement(container.getQualifiedName(), CONTAINER_NAME);
        assertNotNull(updated);
        assertNull(updated.getAnnouncementType());
        assertNull(updated.getAnnouncementTitle());
        assertNull(updated.getAnnouncementMessage());
    }

    @Test(
            groups = {"adls.search.assets"},
            dependsOnGroups = {"adls.update.container.again"})
    void searchAssets() throws AtlanException, InterruptedException {
        IndexSearchRequest index = Atlan.getDefaultClient()
                .assets
                .select()
                .where(CompoundQuery.superType(IADLS.TYPE_NAME))
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
                3);

        assertEquals(response.getApproximateCount().longValue(), 3L);
        List<Asset> entities = response.getAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 3);

        Asset one = entities.get(0);
        assertTrue(one instanceof IADLS);
        assertFalse(one.isComplete());
        IADLS asset = (IADLS) one;
        assertEquals(asset.getQualifiedName(), account.getQualifiedName());
        assertEquals(asset.getName(), account.getName());
        assertEquals(asset.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(1);
        assertTrue(one instanceof IADLS);
        assertFalse(one.isComplete());
        asset = (IADLS) one;
        assertEquals(asset.getQualifiedName(), container.getQualifiedName());
        assertEquals(asset.getName(), container.getName());
        assertEquals(asset.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(2);
        assertTrue(one instanceof IADLS);
        assertFalse(one.isComplete());
        asset = (IADLS) one;
        assertEquals(asset.getQualifiedName(), object.getQualifiedName());
        assertEquals(asset.getName(), object.getName());
        assertEquals(asset.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"adls.delete.object"},
            dependsOnGroups = {"adls.update.*", "adls.search.*"})
    void deleteObject() throws AtlanException {
        AssetMutationResponse response = Asset.delete(object.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof ADLSObject);
        ADLSObject s = (ADLSObject) one;
        assertEquals(s.getGuid(), object.getGuid());
        assertEquals(s.getQualifiedName(), object.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "SOFT");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"adls.delete.object.read"},
            dependsOnGroups = {"adls.delete.object"})
    void readDeletedObject() throws AtlanException {
        validateDeletedAsset(object, log);
    }

    @Test(
            groups = {"adls.delete.object.restore"},
            dependsOnGroups = {"adls.delete.object.read"})
    void restoreObject() throws AtlanException {
        assertTrue(ADLSObject.restore(object.getQualifiedName()));
        ADLSObject restored = ADLSObject.get(object.getQualifiedName());
        assertEquals(restored.getGuid(), object.getGuid());
        assertEquals(restored.getQualifiedName(), object.getQualifiedName());
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"adls.purge.object"},
            dependsOnGroups = {"adls.delete.object.restore"})
    void purgeObject() throws AtlanException {
        AssetMutationResponse response = Asset.purge(object.getGuid());
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof ADLSObject);
        ADLSObject s = (ADLSObject) one;
        assertEquals(s.getGuid(), object.getGuid());
        assertEquals(s.getQualifiedName(), object.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "PURGE");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"adls.purge.connection"},
            dependsOnGroups = {"adls.create.*", "adls.read.*", "adls.search.*", "adls.update.*", "adls.purge.object"})
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(connection.getQualifiedName(), log);
    }
}
