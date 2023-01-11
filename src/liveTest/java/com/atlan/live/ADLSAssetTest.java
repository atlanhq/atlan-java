/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.*;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.*;
import com.atlan.model.search.AggregationBucketResult;
import com.atlan.model.search.IndexSearchDSL;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.util.QueryFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * Tests all aspects of Azure Data Lake Storage assets.
 */
@Test(groups = {"adls"})
@Slf4j
public class ADLSAssetTest extends AtlanLiveTest {

    private static final String PREFIX = "ADLSAssetTest";

    private static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.ADLS;
    private static final String CONNECTION_NAME = "java-sdk-" + PREFIX;
    private static final String ACCOUNT_NAME = PREFIX + "-account";
    private static final String CONTAINER_NAME = PREFIX + "-container";
    private static final String OBJECT_NAME = PREFIX + "-object.csv";

    private static Connection connection = null;
    private static ADLSAccount account = null;
    private static ADLSContainer container = null;
    private static ADLSObject object = null;

    @Test(groups = {"create.connection"})
    void createConnection() throws AtlanException {
        connection = ConnectionTest.createConnection(CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"create.account"},
            dependsOnGroups = {"create.connection"})
    void createAccount() throws AtlanException {
        ADLSAccount adlsAccount = ADLSAccount.creator(
                        ACCOUNT_NAME,
                        PREFIX,
                        PREFIX,
                        PREFIX,
                        ADLSPerformance.PREMIUM,
                        ADLSReplicationType.GEO_REDUNDANT,
                        connection.getQualifiedName())
                .build();
        AssetMutationResponse response = adlsAccount.upsert();
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof ADLSAccount);
        account = (ADLSAccount) one;
        assertNotNull(account.getGuid());
        assertNotNull(account.getQualifiedName());
        assertEquals(account.getName(), ACCOUNT_NAME);
        assertEquals(account.getAdlsAccountName(), ACCOUNT_NAME);
        assertEquals(account.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(account.getAdlsAccountSecondaryLocation(), PREFIX);
        assertEquals(account.getAdlsAccountResourceGroup(), PREFIX);
        assertEquals(account.getAdlsAccountSubscription(), PREFIX);
        assertEquals(account.getAdlsAccountPerformance(), ADLSPerformance.PREMIUM);
        assertEquals(account.getAdlsAccountReplication(), ADLSReplicationType.GEO_REDUNDANT);
        assertEquals(account.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"create.container"},
            dependsOnGroups = {"create.account"})
    void createContainer() throws AtlanException {
        ADLSContainer adlsContainer = ADLSContainer.creator(CONTAINER_NAME, PREFIX, account.getQualifiedName())
                .build();
        AssetMutationResponse response = adlsContainer.upsert();
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
        assertEquals(account.getAdlsAccountSecondaryLocation(), PREFIX);
        assertEquals(container.getAdlsContainerName(), CONTAINER_NAME);
        assertEquals(container.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"create.object"},
            dependsOnGroups = {"create.container"})
    void createObject() throws AtlanException {
        ADLSObject adlsObject = ADLSObject.creator(OBJECT_NAME, PREFIX, container.getQualifiedName())
                .build();
        AssetMutationResponse response = adlsObject.upsert();
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
        assertEquals(account.getAdlsAccountSecondaryLocation(), PREFIX);
        assertEquals(object.getAdlsObjectName(), OBJECT_NAME);
        assertEquals(object.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"update.container"},
            dependsOnGroups = {"create.container"})
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
            groups = {"read.container"},
            dependsOnGroups = {"create.object", "update.container"})
    void retrieveContainer() throws AtlanException {
        ADLSContainer b = ADLSContainer.retrieveByGuid(container.getGuid());
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
                b.getAdlsObjects().stream().map(ADLSObject::getTypeName).collect(Collectors.toSet());
        assertEquals(types.size(), 1);
        assertTrue(types.contains(ADLSObject.TYPE_NAME));
        Set<String> guids = b.getAdlsObjects().stream().map(ADLSObject::getGuid).collect(Collectors.toSet());
        assertEquals(guids.size(), 1);
        assertTrue(guids.contains(object.getGuid()));
    }

    @Test(
            groups = {"update.container.again"},
            dependsOnGroups = {"read.container"})
    void updateContainerAgain() throws AtlanException {
        ADLSContainer updated = ADLSContainer.removeCertificate(container.getQualifiedName(), CONTAINER_NAME, PREFIX);
        assertNotNull(updated);
        assertNull(updated.getCertificateStatus());
        assertNull(updated.getCertificateStatusMessage());
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        updated = ADLSContainer.removeAnnouncement(container.getQualifiedName(), CONTAINER_NAME, PREFIX);
        assertNotNull(updated);
        assertNull(updated.getAnnouncementType());
        assertNull(updated.getAnnouncementTitle());
        assertNull(updated.getAnnouncementMessage());
    }

    @Test(
            groups = {"search.assets"},
            dependsOnGroups = {"update.container.again"})
    void searchAssets() throws AtlanException {
        Query byState = QueryFactory.active();
        Query byType = QueryFactory.withSuperType(ADLS.TYPE_NAME);
        Query byQN = QueryFactory.whereQualifiedNameStartsWith(connection.getQualifiedName());
        Query combined = BoolQuery.of(b -> b.filter(byState, byType, byQN))._toQuery();

        SortOptions sort = SortOptions.of(
                s -> s.field(FieldSort.of(f -> f.field("__timestamp").order(SortOrder.Asc))));

        Aggregation aggregation = Aggregation.of(a -> a.terms(t -> t.field("__typeName.keyword")));

        IndexSearchRequest index = IndexSearchRequest.builder()
                .dsl(IndexSearchDSL.builder()
                        .from(0)
                        .size(10)
                        .query(combined)
                        .aggregation("type", aggregation)
                        .sortOption(sort)
                        .build())
                .attribute("name")
                .attribute("connectionQualifiedName")
                .build();

        IndexSearchResponse response = index.search();
        assertNotNull(response);
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
        assertTrue(one instanceof ADLS);
        assertFalse(one.isComplete());
        ADLS asset = (ADLS) one;
        assertEquals(asset.getQualifiedName(), account.getQualifiedName());
        assertEquals(asset.getName(), account.getName());
        assertEquals(asset.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(1);
        assertTrue(one instanceof ADLS);
        assertFalse(one.isComplete());
        asset = (ADLS) one;
        assertEquals(asset.getQualifiedName(), container.getQualifiedName());
        assertEquals(asset.getName(), container.getName());
        assertEquals(asset.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(2);
        assertTrue(one instanceof ADLS);
        assertFalse(one.isComplete());
        asset = (ADLS) one;
        assertEquals(asset.getQualifiedName(), object.getQualifiedName());
        assertEquals(asset.getName(), object.getName());
        assertEquals(asset.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"delete.object"},
            dependsOnGroups = {"update.*", "search.*"})
    void deleteObject() throws AtlanException {
        AssetMutationResponse response = Asset.delete(object.getGuid());
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
            groups = {"delete.object.read"},
            dependsOnGroups = {"delete.object"})
    void readDeletedObject() throws AtlanException {
        ADLSObject deleted = ADLSObject.retrieveByGuid(object.getGuid());
        assertEquals(deleted.getGuid(), object.getGuid());
        assertEquals(deleted.getQualifiedName(), object.getQualifiedName());
        assertEquals(deleted.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"delete.object.restore"},
            dependsOnGroups = {"delete.object.read"})
    void restoreObject() throws AtlanException {
        assertTrue(ADLSObject.restore(object.getQualifiedName()));
        ADLSObject restored = ADLSObject.retrieveByQualifiedName(object.getQualifiedName());
        assertEquals(restored.getGuid(), object.getGuid());
        assertEquals(restored.getQualifiedName(), object.getQualifiedName());
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"purge.object"},
            dependsOnGroups = {"delete.object.restore"})
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
            groups = {"purge.connection"},
            dependsOnGroups = {"create.*", "read.*", "search.*", "update.*", "purge.object"},
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(connection.getQualifiedName(), log);
    }
}
