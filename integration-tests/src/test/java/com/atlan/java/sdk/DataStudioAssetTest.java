/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.*;

import co.elastic.clients.elasticsearch._types.SortOrder;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.*;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.*;
import com.atlan.model.search.AggregationBucketResult;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * Tests all aspects of Google Data Studio assets.
 */
@Slf4j
public class DataStudioAssetTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("DataStudio");

    public static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.DATASTUDIO;
    public static final String CONNECTION_NAME = PREFIX;
    private static final String REPORT_NAME = PREFIX + "-report";
    private static final String SOURCE_NAME = PREFIX + "-source";

    private static Connection connection = null;
    private static DataStudioAsset report = null;
    private static DataStudioAsset source = null;

    @Test(groups = {"gds.create.connection"})
    void createConnection() throws AtlanException, InterruptedException {
        connection = ConnectionTest.createConnection(CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"gds.create.report"},
            dependsOnGroups = {"gds.create.connection"})
    void createReport() throws AtlanException {
        DataStudioAsset toCreate = DataStudioAsset.creator(
                        REPORT_NAME, connection.getQualifiedName(), GoogleDataStudioAssetType.REPORT)
                .build();
        AssetMutationResponse response = toCreate.save(client);
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof DataStudioAsset);
        report = (DataStudioAsset) one;
        assertNotNull(report.getGuid());
        assertNotNull(report.getQualifiedName());
        assertEquals(report.getName(), REPORT_NAME);
        assertEquals(report.getConnectorType(), AtlanConnectorType.DATASTUDIO);
        assertEquals(report.getConnectionQualifiedName(), connection.getQualifiedName());
        assertEquals(report.getDataStudioAssetType(), GoogleDataStudioAssetType.REPORT);
    }

    @Test(
            groups = {"gds.create.source"},
            dependsOnGroups = {"gds.create.report"})
    void createSource() throws AtlanException {
        DataStudioAsset toCreate = DataStudioAsset.creator(
                        SOURCE_NAME, connection.getQualifiedName(), GoogleDataStudioAssetType.DATA_SOURCE)
                .build();
        AssetMutationResponse response = toCreate.save(client);
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof DataStudioAsset);
        source = (DataStudioAsset) one;
        assertNotNull(source.getGuid());
        assertNotNull(source.getQualifiedName());
        assertEquals(source.getName(), SOURCE_NAME);
        assertEquals(source.getConnectorType(), AtlanConnectorType.DATASTUDIO);
        assertEquals(source.getConnectionQualifiedName(), connection.getQualifiedName());
        assertEquals(source.getDataStudioAssetType(), GoogleDataStudioAssetType.DATA_SOURCE);
    }

    @Test(
            groups = {"gds.update.report"},
            dependsOnGroups = {"gds.create.report"})
    void updateReport() throws AtlanException {
        DataStudioAsset updated = DataStudioAsset.updateCertificate(
                client, report.getQualifiedName(), CERTIFICATE_STATUS, CERTIFICATE_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(updated.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        updated = DataStudioAsset.updateAnnouncement(
                client, report.getQualifiedName(), ANNOUNCEMENT_TYPE, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"gds.read.report"},
            dependsOnGroups = {"gds.update.report"})
    void retrieveReport() throws AtlanException {
        DataStudioAsset r = DataStudioAsset.get(client, report.getGuid(), true);
        assertNotNull(r);
        assertTrue(r.isComplete());
        assertEquals(r.getGuid(), report.getGuid());
        assertEquals(r.getQualifiedName(), report.getQualifiedName());
        assertEquals(r.getName(), REPORT_NAME);
        assertEquals(r.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(r.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        assertEquals(r.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(r.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(r.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"gds.update.report.again"},
            dependsOnGroups = {"gds.read.report"})
    void updateReportAgain() throws AtlanException {
        DataStudioAsset updated = DataStudioAsset.removeCertificate(client, report.getQualifiedName(), REPORT_NAME);
        assertNotNull(updated);
        assertNull(updated.getCertificateStatus());
        assertNull(updated.getCertificateStatusMessage());
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        updated = DataStudioAsset.removeAnnouncement(client, report.getQualifiedName(), REPORT_NAME);
        assertNotNull(updated);
        assertNull(updated.getAnnouncementType());
        assertNull(updated.getAnnouncementTitle());
        assertNull(updated.getAnnouncementMessage());
    }

    @Test(
            groups = {"gds.search.assets"},
            dependsOnGroups = {"gds.update.report.again"})
    void searchAssets() throws AtlanException, InterruptedException {
        IndexSearchRequest index = DataStudioAsset.select(client)
                .where(DataStudioAsset.QUALIFIED_NAME.startsWith(connection.getQualifiedName()))
                .aggregate("type", IReferenceable.TYPE_NAME.bucketBy())
                .sort(DataStudioAsset.CREATE_TIME.order(SortOrder.Asc))
                .includeOnResults(DataStudioAsset.NAME)
                .includeOnResults(DataStudioAsset.CONNECTION_QUALIFIED_NAME)
                .toRequest();
        IndexSearchResponse response = retrySearchUntil(index, 2L);

        assertNotNull(response.getAggregations());
        assertEquals(response.getAggregations().size(), 1);
        assertTrue(response.getAggregations().get("type") instanceof AggregationBucketResult);
        assertEquals(
                ((AggregationBucketResult) response.getAggregations().get("type"))
                        .getBuckets()
                        .size(),
                1);

        assertEquals(response.getApproximateCount().longValue(), 2L);
        List<Asset> entities = response.getAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 2);

        Asset one = entities.get(0);
        assertTrue(one instanceof DataStudioAsset);
        assertFalse(one.isComplete());
        DataStudioAsset asset = (DataStudioAsset) one;
        assertEquals(asset.getQualifiedName(), report.getQualifiedName());
        assertEquals(asset.getName(), report.getName());
        assertEquals(asset.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(1);
        assertTrue(one instanceof DataStudioAsset);
        assertFalse(one.isComplete());
        asset = (DataStudioAsset) one;
        assertEquals(asset.getQualifiedName(), source.getQualifiedName());
        assertEquals(asset.getName(), source.getName());
        assertEquals(asset.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"gds.delete.source"},
            dependsOnGroups = {"gds.update.*", "gds.search.*"})
    void deleteSource() throws AtlanException {
        AssetMutationResponse response = Asset.delete(client, source.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof DataStudioAsset);
        DataStudioAsset s = (DataStudioAsset) one;
        assertEquals(s.getGuid(), source.getGuid());
        assertEquals(s.getQualifiedName(), source.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "SOFT");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"gds.delete.source.read"},
            dependsOnGroups = {"gds.delete.source"})
    void readDeletedSource() throws AtlanException {
        validateDeletedAsset(source, log);
    }

    @Test(
            groups = {"gds.delete.source.restore"},
            dependsOnGroups = {"gds.delete.source.read"})
    void restoreSource() throws AtlanException {
        assertTrue(DataStudioAsset.restore(client, source.getQualifiedName()));
        DataStudioAsset restored = DataStudioAsset.get(client, source.getQualifiedName());
        assertEquals(restored.getGuid(), source.getGuid());
        assertEquals(restored.getQualifiedName(), source.getQualifiedName());
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"gds.purge.source"},
            dependsOnGroups = {"gds.delete.source.restore"})
    void purgeSource() throws AtlanException {
        AssetMutationResponse response = Asset.purge(client, source.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof DataStudioAsset);
        DataStudioAsset s = (DataStudioAsset) one;
        assertEquals(s.getGuid(), source.getGuid());
        assertEquals(s.getQualifiedName(), source.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "PURGE");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"gds.purge.connection"},
            dependsOnGroups = {"gds.create.*", "gds.read.*", "gds.search.*", "gds.update.*", "gds.purge.source"},
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(connection.getQualifiedName(), log);
    }
}
