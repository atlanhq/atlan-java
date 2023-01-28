/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static com.atlan.util.QueryFactory.*;
import static org.testng.Assert.*;

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.*;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.*;
import com.atlan.model.search.AggregationBucketResult;
import com.atlan.model.search.IndexSearchDSL;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * Tests all aspects of Google Data Studio assets.
 */
@Test(groups = {"gds"})
@Slf4j
public class DataStudioAssetTest extends AtlanLiveTest {

    private static final String PREFIX = "DataStudioAssetTest";

    private static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.DATASTUDIO;
    private static final String CONNECTION_NAME = "java-sdk-" + PREFIX;
    private static final String REPORT_NAME = PREFIX + "-report";
    private static final String SOURCE_NAME = PREFIX + "-source";

    private static Connection connection = null;
    private static DataStudioAsset report = null;
    private static DataStudioAsset source = null;

    @Test(groups = {"create.connection"})
    void createConnection() throws AtlanException {
        connection = ConnectionTest.createConnection(CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"create.report"},
            dependsOnGroups = {"create.connection"})
    void createReport() throws AtlanException {
        DataStudioAsset toCreate = DataStudioAsset.creator(
                        REPORT_NAME, connection.getQualifiedName(), GoogleDataStudioAssetType.REPORT)
                .build();
        AssetMutationResponse response = toCreate.upsert();
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
            groups = {"create.source"},
            dependsOnGroups = {"create.report"})
    void createSource() throws AtlanException {
        DataStudioAsset toCreate = DataStudioAsset.creator(
                        SOURCE_NAME, connection.getQualifiedName(), GoogleDataStudioAssetType.DATA_SOURCE)
                .build();
        AssetMutationResponse response = toCreate.upsert();
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
            groups = {"update.report"},
            dependsOnGroups = {"create.report"})
    void updateReport() throws AtlanException {
        DataStudioAsset updated =
                DataStudioAsset.updateCertificate(report.getQualifiedName(), CERTIFICATE_STATUS, CERTIFICATE_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(updated.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        updated = DataStudioAsset.updateAnnouncement(
                report.getQualifiedName(), ANNOUNCEMENT_TYPE, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"read.report"},
            dependsOnGroups = {"update.report"})
    void retrieveReport() throws AtlanException {
        DataStudioAsset r = DataStudioAsset.retrieveByGuid(report.getGuid());
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
            groups = {"update.report.again"},
            dependsOnGroups = {"read.report"})
    void updateReportAgain() throws AtlanException {
        DataStudioAsset updated = DataStudioAsset.removeCertificate(report.getQualifiedName(), REPORT_NAME);
        assertNotNull(updated);
        assertNull(updated.getCertificateStatus());
        assertNull(updated.getCertificateStatusMessage());
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        updated = DataStudioAsset.removeAnnouncement(report.getQualifiedName(), REPORT_NAME);
        assertNotNull(updated);
        assertNull(updated.getAnnouncementType());
        assertNull(updated.getAnnouncementTitle());
        assertNull(updated.getAnnouncementMessage());
    }

    @Test(
            groups = {"search.assets"},
            dependsOnGroups = {"update.report.again"})
    void searchAssets() throws AtlanException {
        Query combined = CompoundQuery.builder()
                .must(beActive())
                .must(beOfType(DataStudioAsset.TYPE_NAME))
                .must(have(KeywordFields.QUALIFIED_NAME).startingWith(connection.getQualifiedName()))
                .build()
                ._toQuery();

        IndexSearchRequest index = IndexSearchRequest.builder()
                .dsl(IndexSearchDSL.builder()
                        .from(0)
                        .size(10)
                        .query(combined)
                        .aggregation("type", Aggregate.bucketBy(KeywordFields.TYPE_NAME))
                        .sortOption(Sort.by(NumericFields.TIMESTAMP, SortOrder.Asc))
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
            groups = {"delete.source"},
            dependsOnGroups = {"update.*", "search.*"})
    void deleteSource() throws AtlanException {
        AssetMutationResponse response = Asset.delete(source.getGuid());
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
            groups = {"delete.source.read"},
            dependsOnGroups = {"delete.source"})
    void readDeletedSource() throws AtlanException {
        DataStudioAsset deleted = DataStudioAsset.retrieveByGuid(source.getGuid());
        assertEquals(deleted.getGuid(), source.getGuid());
        assertEquals(deleted.getQualifiedName(), source.getQualifiedName());
        assertEquals(deleted.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"delete.source.restore"},
            dependsOnGroups = {"delete.source.read"})
    void restoreSource() throws AtlanException {
        assertTrue(DataStudioAsset.restore(source.getQualifiedName()));
        DataStudioAsset restored = DataStudioAsset.retrieveByQualifiedName(source.getQualifiedName());
        assertEquals(restored.getGuid(), source.getGuid());
        assertEquals(restored.getQualifiedName(), source.getQualifiedName());
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"purge.source"},
            dependsOnGroups = {"delete.source.restore"})
    void purgeSource() throws AtlanException {
        AssetMutationResponse response = Asset.purge(source.getGuid());
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
            groups = {"purge.connection"},
            dependsOnGroups = {"create.*", "read.*", "search.*", "update.*", "purge.source"},
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(connection.getQualifiedName(), log);
    }
}
