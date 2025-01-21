/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.*;
import static org.testng.Assert.assertEquals;

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
 * Tests all aspects of Anaplan assets.
 */
@Slf4j
public class AppAssetTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("App");

    public static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.APP;
    public static final String CONNECTION_NAME = PREFIX;

    private static final String APPLICATION_NAME = PREFIX + "lication";
    private static final String APPLICATION_FIELD_NAME = PREFIX + "lication-field";

    private static Connection connection = null;
    private static Application application = null;
    private static ApplicationField applicationField = null;

    @Test(groups = {"app.create.connection"})
    void createConnection() throws AtlanException, InterruptedException {
        connection = ConnectionTest.createConnection(client, CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"app.create.application"},
            dependsOnGroups = {"app.create.connection"})
    void createApplication() throws AtlanException {
        Application toCreate = Application.creator(APPLICATION_NAME, connection.getQualifiedName())
                .build();
        AssetMutationResponse response = toCreate.save(client);
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof Application);
        application = (Application) one;
        assertNotNull(application.getGuid());
        assertNotNull(application.getQualifiedName());
        assertEquals(application.getName(), APPLICATION_NAME);
        assertEquals(application.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(application.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"app.create.applicationField"},
            dependsOnGroups = {"app.create.application"})
    void createApplicationField() throws AtlanException {
        ApplicationField toCreate =
                ApplicationField.creator(APPLICATION_FIELD_NAME, application).build();
        AssetMutationResponse response = toCreate.save(client);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof Application);
        Application c = (Application) one;
        assertEquals(c.getGuid(), application.getGuid());
        assertEquals(c.getQualifiedName(), application.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof ApplicationField);
        applicationField = (ApplicationField) one;
        assertNotNull(applicationField.getGuid());
        assertNotNull(applicationField.getQualifiedName());
        assertEquals(applicationField.getName(), APPLICATION_FIELD_NAME);
        assertEquals(applicationField.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(applicationField.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"app.update.applicationField"},
            dependsOnGroups = {"app.create.applicationField"})
    void updateApplicationField() throws AtlanException {
        ApplicationField updated = ApplicationField.updateCertificate(
                client, applicationField.getQualifiedName(), CERTIFICATE_STATUS, CERTIFICATE_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(updated.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        updated = ApplicationField.updateAnnouncement(
                client,
                applicationField.getQualifiedName(),
                ANNOUNCEMENT_TYPE,
                ANNOUNCEMENT_TITLE,
                ANNOUNCEMENT_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"app.read.applicationField"},
            dependsOnGroups = {"app.create.*", "app.update.applicationField"})
    void retrieveApplicationField() throws AtlanException {
        ApplicationField c = ApplicationField.get(client, applicationField.getGuid(), true);
        assertNotNull(c);
        assertTrue(c.isComplete());
        assertEquals(c.getGuid(), applicationField.getGuid());
        assertEquals(c.getQualifiedName(), applicationField.getQualifiedName());
        assertEquals(c.getName(), APPLICATION_FIELD_NAME);
        assertEquals(c.getCertificateStatus(), CERTIFICATE_STATUS);
    }

    @Test(
            groups = {"app.update.applicationField.again"},
            dependsOnGroups = {"app.read.applicationField"})
    void updateApplicationFieldAgain() throws AtlanException {
        ApplicationField updated =
                ApplicationField.removeCertificate(client, applicationField.getQualifiedName(), APPLICATION_FIELD_NAME);
        assertNotNull(updated);
        assertNull(updated.getCertificateStatus());
        assertNull(updated.getCertificateStatusMessage());
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        updated = ApplicationField.removeAnnouncement(
                client, applicationField.getQualifiedName(), APPLICATION_FIELD_NAME);
        assertNotNull(updated);
        assertNull(updated.getAnnouncementType());
        assertNull(updated.getAnnouncementTitle());
        assertNull(updated.getAnnouncementMessage());
    }

    @Test(
            groups = {"app.search.assets"},
            dependsOnGroups = {"app.update.applicationField.again"})
    void searchAssets() throws AtlanException, InterruptedException {
        IndexSearchRequest index = client.assets
                .select()
                .where(Asset.SUPER_TYPE_NAMES.eq(IApp.TYPE_NAME))
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
        assertTrue(one instanceof Application);
        assertFalse(one.isComplete());
        Application d = (Application) one;
        assertEquals(d.getQualifiedName(), application.getQualifiedName());
        assertEquals(d.getName(), application.getName());
        assertEquals(d.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(1);
        assertTrue(one instanceof ApplicationField);
        assertFalse(one.isComplete());
        ApplicationField c = (ApplicationField) one;
        assertEquals(c.getQualifiedName(), applicationField.getQualifiedName());
        assertEquals(c.getName(), applicationField.getName());
        assertEquals(c.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"app.delete.applicationField"},
            dependsOnGroups = {"app.update.*", "app.search.*"})
    void deleteApplicationField() throws AtlanException {
        AssetMutationResponse response =
                Asset.delete(client, applicationField.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof ApplicationField);
        ApplicationField s = (ApplicationField) one;
        assertEquals(s.getGuid(), applicationField.getGuid());
        assertEquals(s.getQualifiedName(), applicationField.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "SOFT");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"app.delete.applicationField.read"},
            dependsOnGroups = {"app.delete.applicationField"})
    void readDeletedApplicationField() throws AtlanException {
        validateDeletedAsset(applicationField, log);
    }

    @Test(
            groups = {"app.delete.applicationField.restore"},
            dependsOnGroups = {"app.delete.applicationField.read"})
    void restoreApplicationField() throws AtlanException {
        assertTrue(ApplicationField.restore(client, applicationField.getQualifiedName()));
        ApplicationField restored = ApplicationField.get(client, applicationField.getQualifiedName());
        assertFalse(restored.isComplete());
        assertEquals(restored.getGuid(), applicationField.getGuid());
        assertEquals(restored.getQualifiedName(), applicationField.getQualifiedName());
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"app.purge.applicationField"},
            dependsOnGroups = {"app.delete.applicationField.restore"})
    void purgeApplicationField() throws AtlanException {
        AssetMutationResponse response =
                Asset.purge(client, applicationField.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof ApplicationField);
        ApplicationField s = (ApplicationField) one;
        assertEquals(s.getGuid(), applicationField.getGuid());
        assertEquals(s.getQualifiedName(), applicationField.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "PURGE");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"app.purge.connection"},
            dependsOnGroups = {
                "app.create.*",
                "app.read.*",
                "app.search.*",
                "app.update.*",
                "app.purge.applicationField"
            },
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(client, connection.getQualifiedName(), log);
    }
}
