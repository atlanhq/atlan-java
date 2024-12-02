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
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

@Slf4j
public class APIAssetTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("API");

    public static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.API;
    public static final String CONNECTION_NAME = PREFIX;
    private static final String SPEC_NAME = PREFIX + "-spec";
    private static final String PATH_NAME = "/api/" + PREFIX;

    private static Connection connection = null;
    private static APISpec spec = null;
    private static APIPath path = null;

    @Test(groups = {"api.create.connection"})
    void createConnection() throws AtlanException, InterruptedException {
        connection = ConnectionTest.createConnection(client, CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"api.create.spec"},
            dependsOnGroups = {"api.create.connection"})
    void createSpec() throws AtlanException {
        APISpec toCreate =
                APISpec.creator(SPEC_NAME, connection.getQualifiedName()).build();
        AssetMutationResponse response = toCreate.save(client);
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof APISpec);
        spec = (APISpec) one;
        assertNotNull(spec.getGuid());
        assertNotNull(spec.getQualifiedName());
        assertEquals(spec.getName(), SPEC_NAME);
        assertEquals(spec.getConnectorType(), AtlanConnectorType.API);
        assertEquals(spec.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"api.create.path"},
            dependsOnGroups = {"api.create.spec"})
    void createPath() throws AtlanException {
        APIPath toCreate = APIPath.creator(PATH_NAME, spec).build();
        AssetMutationResponse response = toCreate.save(client);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);

        Asset one = response.getUpdatedAssets().get(0);
        assertNotNull(one);
        assertTrue(one instanceof APISpec);
        APISpec s = (APISpec) one;
        assertEquals(s.getGuid(), spec.getGuid());
        assertEquals(s.getQualifiedName(), spec.getQualifiedName());

        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof APIPath);
        path = (APIPath) one;
        assertNotNull(path.getGuid());
        assertNotNull(path.getQualifiedName());
        assertEquals(path.getName(), PATH_NAME);
        assertEquals(path.getConnectorType(), AtlanConnectorType.API);
        assertEquals(path.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"api.update.spec"},
            dependsOnGroups = {"api.create.spec"})
    void updateSpec() throws AtlanException {
        APISpec updated =
                APISpec.updateCertificate(client, spec.getQualifiedName(), CERTIFICATE_STATUS, CERTIFICATE_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(updated.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        updated = APISpec.updateAnnouncement(
                client, spec.getQualifiedName(), ANNOUNCEMENT_TYPE, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"api.read.spec"},
            dependsOnGroups = {"api.create.path", "api.update.spec"})
    void retrieveSpec() throws AtlanException {
        APISpec s = APISpec.get(client, spec.getGuid(), true);
        assertNotNull(s);
        assertTrue(s.isComplete());
        assertEquals(s.getGuid(), spec.getGuid());
        assertEquals(s.getQualifiedName(), spec.getQualifiedName());
        assertEquals(s.getName(), SPEC_NAME);
        assertEquals(s.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(s.getCertificateStatusMessage(), ANNOUNCEMENT_MESSAGE);
        assertEquals(s.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(s.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(s.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"api.update.spec.again"},
            dependsOnGroups = {"api.read.spec"})
    void updateSpecAgain() throws AtlanException {
        APISpec updated = APISpec.removeCertificate(client, spec.getQualifiedName(), SPEC_NAME);
        assertNotNull(updated);
        assertNull(updated.getCertificateStatus());
        assertNull(updated.getCertificateStatusMessage());
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        updated = APISpec.removeAnnouncement(client, spec.getQualifiedName(), SPEC_NAME);
        assertNotNull(updated);
        assertNull(updated.getAnnouncementType());
        assertNull(updated.getAnnouncementTitle());
        assertNull(updated.getAnnouncementMessage());
    }

    @Test(
            groups = {"api.search.assets"},
            dependsOnGroups = {"api.update.spec.again"})
    void searchAssets() throws AtlanException, InterruptedException {
        IndexSearchRequest index = APIPath.select(client)
                .where(Asset.QUALIFIED_NAME.startsWith(connection.getQualifiedName()))
                .pageSize(10)
                .aggregate("type", IReferenceable.TYPE_NAME.bucketBy())
                .sort(Asset.CREATE_TIME.order(SortOrder.Asc))
                .includeOnResults(Asset.NAME)
                .includeOnResults(Asset.CONNECTION_QUALIFIED_NAME)
                .toRequest();

        IndexSearchResponse response = retrySearchUntil(index, 1L);

        assertNotNull(response.getAggregations());
        assertEquals(response.getAggregations().size(), 1);
        assertTrue(response.getAggregations().get("type") instanceof AggregationBucketResult);
        assertEquals(
                ((AggregationBucketResult) response.getAggregations().get("type"))
                        .getBuckets()
                        .size(),
                1);

        assertEquals(response.getApproximateCount().longValue(), 1L);
        List<Asset> entities = response.getAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 1);

        Asset one = entities.get(0);
        assertTrue(one instanceof APIPath);
        assertFalse(one.isComplete());
        APIPath asset = (APIPath) one;
        assertEquals(asset.getQualifiedName(), path.getQualifiedName());
        assertEquals(asset.getName(), path.getName());
        assertEquals(asset.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"api.delete.path"},
            dependsOnGroups = {"api.update.*", "api.search.*"})
    void deletePath() throws AtlanException {
        AssetMutationResponse response = Asset.delete(client, path.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof APIPath);
        APIPath s = (APIPath) one;
        assertEquals(s.getGuid(), path.getGuid());
        assertEquals(s.getQualifiedName(), path.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "SOFT");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"api.delete.path.read"},
            dependsOnGroups = {"api.delete.path"})
    void readDeletedPath() throws AtlanException {
        validateDeletedAsset(path, log);
    }

    @Test(
            groups = {"api.delete.path.restore"},
            dependsOnGroups = {"api.delete.path.read"})
    void restorePath() throws AtlanException {
        assertTrue(APIPath.restore(client, path.getQualifiedName()));
        APIPath restored = APIPath.get(client, path.getQualifiedName());
        assertFalse(restored.isComplete());
        assertEquals(restored.getGuid(), path.getGuid());
        assertEquals(restored.getQualifiedName(), path.getQualifiedName());
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"api.purge.path"},
            dependsOnGroups = {"api.delete.path.restore"})
    void purgePath() throws AtlanException {
        AssetMutationResponse response = Asset.purge(client, path.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof APIPath);
        APIPath s = (APIPath) one;
        assertEquals(s.getGuid(), path.getGuid());
        assertEquals(s.getQualifiedName(), path.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "PURGE");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"api.purge.connection"},
            dependsOnGroups = {"api.create.*", "api.read.*", "api.search.*", "api.update.*", "api.purge.path"},
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(client, connection.getQualifiedName(), log);
    }
}
