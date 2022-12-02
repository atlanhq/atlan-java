/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.*;
import com.atlan.model.core.Entity;
import com.atlan.model.core.EntityMutationResponse;
import com.atlan.model.enums.*;
import com.atlan.model.search.IndexSearchDSL;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.util.QueryFactory;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

@Test(groups = {"api-asset"})
@Slf4j
public class APIAssetTest extends AtlanLiveTest {

    private static final String PREFIX = "APIAssetTest";

    private static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.API;
    private static final String CONNECTION_NAME = "java-sdk-" + PREFIX;
    private static final String SPEC_NAME = PREFIX + "-spec";
    private static final String PATH_NAME = "/api/" + PREFIX;

    private static Connection connection = null;
    private static APISpec spec = null;
    private static APIPath path = null;

    @Test(groups = {"create.connection"})
    void createConnection() throws AtlanException {
        connection = ConnectionTest.createConnection(CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"create.spec"},
            dependsOnGroups = {"create.connection"})
    void createSpec() throws AtlanException {
        APISpec toCreate =
                APISpec.creator(SPEC_NAME, connection.getQualifiedName()).build();
        EntityMutationResponse response = toCreate.upsert();
        Entity one = validateSingleCreate(response);
        assertTrue(one instanceof APISpec);
        spec = (APISpec) one;
        assertNotNull(spec.getGuid());
        assertNotNull(spec.getQualifiedName());
        assertEquals(spec.getName(), SPEC_NAME);
        assertEquals(spec.getConnectorType(), AtlanConnectorType.API);
        assertEquals(spec.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"create.path"},
            dependsOnGroups = {"create.spec"})
    void createPath() throws AtlanException {
        APIPath toCreate = APIPath.creator(PATH_NAME, spec.getQualifiedName()).build();
        EntityMutationResponse response = toCreate.upsert();
        assertNotNull(response);
        assertTrue(response.getDeletedEntities().isEmpty());
        assertEquals(response.getUpdatedEntities().size(), 1);

        Entity one = response.getUpdatedEntities().get(0);
        assertNotNull(one);
        assertTrue(one instanceof APISpec);
        APISpec s = (APISpec) one;
        assertEquals(s.getGuid(), spec.getGuid());
        assertEquals(s.getQualifiedName(), spec.getQualifiedName());

        assertEquals(response.getCreatedEntities().size(), 1);
        one = response.getCreatedEntities().get(0);
        assertTrue(one instanceof APIPath);
        path = (APIPath) one;
        assertNotNull(path.getGuid());
        assertNotNull(path.getQualifiedName());
        assertEquals(path.getName(), PATH_NAME);
        assertEquals(path.getConnectorType(), AtlanConnectorType.API);
        assertEquals(path.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"update.spec"},
            dependsOnGroups = {"create.spec"})
    void updateSpec() throws AtlanException {
        APISpec updated = APISpec.updateCertificate(spec.getQualifiedName(), CERTIFICATE_STATUS, CERTIFICATE_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(updated.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        updated = APISpec.updateAnnouncement(
                spec.getQualifiedName(), ANNOUNCEMENT_TYPE, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"read.spec"},
            dependsOnGroups = {"create.path", "update.spec"})
    void retrieveSpec() throws AtlanException {
        APISpec s = APISpec.retrieveByGuid(spec.getGuid());
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
            groups = {"update.spec.again"},
            dependsOnGroups = {"read.spec"})
    void updateSpecAgain() throws AtlanException {
        APISpec updated = APISpec.removeCertificate(spec.getQualifiedName(), SPEC_NAME);
        assertNotNull(updated);
        assertNull(updated.getCertificateStatus());
        assertNull(updated.getCertificateStatusMessage());
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        updated = APISpec.removeAnnouncement(spec.getQualifiedName(), SPEC_NAME);
        assertNotNull(updated);
        assertNull(updated.getAnnouncementType());
        assertNull(updated.getAnnouncementTitle());
        assertNull(updated.getAnnouncementMessage());
    }

    @Test(
            groups = {"search.assets"},
            dependsOnGroups = {"update.spec.again"})
    void searchAssets() throws AtlanException {
        Query byState = QueryFactory.active();
        Query byType = QueryFactory.withType(APIPath.TYPE_NAME);
        Query byQN = QueryFactory.whereQualifiedNameStartsWith(connection.getQualifiedName());
        Query combined = BoolQuery.of(b -> b.filter(byState, byType, byQN))._toQuery();

        SortOptions sort = SortOptions.of(
                s -> s.field(FieldSort.of(f -> f.field("__timestamp").order(SortOrder.Asc))));

        IndexSearchRequest index = IndexSearchRequest.builder()
                .dsl(IndexSearchDSL.builder()
                        .from(0)
                        .size(10)
                        .query(combined)
                        .sortOption(sort)
                        .build())
                .attribute("name")
                .attribute("connectionQualifiedName")
                .build();

        IndexSearchResponse response = index.search();
        assertNotNull(response);
        assertEquals(response.getApproximateCount().longValue(), 1L);
        List<Entity> entities = response.getEntities();
        assertNotNull(entities);
        assertEquals(entities.size(), 1);

        Entity one = entities.get(0);
        assertTrue(one instanceof APIPath);
        assertFalse(one.isComplete());
        APIPath asset = (APIPath) one;
        assertEquals(asset.getQualifiedName(), path.getQualifiedName());
        assertEquals(asset.getName(), path.getName());
        assertEquals(asset.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"delete.path"},
            dependsOnGroups = {"update.*", "search.*"})
    void deletePath() throws AtlanException {
        EntityMutationResponse response = Entity.delete(path.getGuid());
        assertNotNull(response);
        assertTrue(response.getCreatedEntities().isEmpty());
        assertTrue(response.getUpdatedEntities().isEmpty());
        assertEquals(response.getDeletedEntities().size(), 1);
        Entity one = response.getDeletedEntities().get(0);
        assertTrue(one instanceof APIPath);
        APIPath s = (APIPath) one;
        assertEquals(s.getGuid(), path.getGuid());
        assertEquals(s.getQualifiedName(), path.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "SOFT");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"delete.path.read"},
            dependsOnGroups = {"delete.path"})
    void readDeletedPath() throws AtlanException {
        APIPath deleted = APIPath.retrieveByGuid(path.getGuid());
        assertEquals(deleted.getGuid(), path.getGuid());
        assertEquals(deleted.getQualifiedName(), path.getQualifiedName());
        assertEquals(deleted.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"delete.path.restore"},
            dependsOnGroups = {"delete.path.read"})
    void restorePath() throws AtlanException {
        assertTrue(APIPath.restore(path.getQualifiedName()));
        APIPath restored = APIPath.retrieveByQualifiedName(path.getQualifiedName());
        assertEquals(restored.getGuid(), path.getGuid());
        assertEquals(restored.getQualifiedName(), path.getQualifiedName());
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"purge.path"},
            dependsOnGroups = {"delete.path.restore"})
    void purgePath() throws AtlanException {
        EntityMutationResponse response = Entity.purge(path.getGuid());
        assertNotNull(response);
        assertTrue(response.getCreatedEntities().isEmpty());
        assertTrue(response.getUpdatedEntities().isEmpty());
        assertEquals(response.getDeletedEntities().size(), 1);
        Entity one = response.getDeletedEntities().get(0);
        assertTrue(one instanceof APIPath);
        APIPath s = (APIPath) one;
        assertEquals(s.getGuid(), path.getGuid());
        assertEquals(s.getQualifiedName(), path.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "HARD");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"purge.connection"},
            dependsOnGroups = {"create.*", "read.*", "search.*", "update.*", "purge.path"},
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(connection.getQualifiedName(), log);
    }
}
