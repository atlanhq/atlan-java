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
 * Tests all aspects of Superset assets.
 */
@Slf4j
public class SupersetAssetTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("Superset");

    public static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.SUPERSET;
    public static final String CONNECTION_NAME = PREFIX;

    private static final String DASHBOARD_NAME = PREFIX + "-dash";
    private static final String CHART_NAME = PREFIX + "-cht";
    private static final String DATASET_NAME = PREFIX + "-ds";

    private static Connection connection = null;
    private static SupersetDashboard dashboard = null;
    private static SupersetChart chart = null;
    private static SupersetDataset dataset = null;

    @Test(groups = {"superset.create.connection"})
    void createConnection() throws AtlanException, InterruptedException {
        connection = ConnectionTest.createConnection(CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"superset.create.dashboard"},
            dependsOnGroups = {"superset.create.connection"})
    void createDashboard() throws AtlanException {
        SupersetDashboard toCreate = SupersetDashboard.creator(DASHBOARD_NAME, connection.getQualifiedName())
                .build();
        AssetMutationResponse response = toCreate.save();
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof SupersetDashboard);
        dashboard = (SupersetDashboard) one;
        assertNotNull(dashboard.getGuid());
        assertNotNull(dashboard.getQualifiedName());
        assertEquals(dashboard.getName(), DASHBOARD_NAME);
        assertEquals(dashboard.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(dashboard.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(dashboard.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"superset.create.chart"},
            dependsOnGroups = {"superset.create.dashboard"})
    void createChart() throws AtlanException {
        SupersetChart toCreate = SupersetChart.creator(CHART_NAME, dashboard).build();
        AssetMutationResponse response = toCreate.save();
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof SupersetDashboard);
        SupersetDashboard c = (SupersetDashboard) one;
        assertEquals(c.getGuid(), dashboard.getGuid());
        assertEquals(c.getQualifiedName(), dashboard.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof SupersetChart);
        chart = (SupersetChart) one;
        assertNotNull(chart.getGuid());
        assertNotNull(chart.getQualifiedName());
        assertEquals(chart.getName(), CHART_NAME);
        assertEquals(chart.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(chart.getSupersetDashboardQualifiedName(), dashboard.getQualifiedName());
        assertEquals(chart.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"superset.create.dataset"},
            dependsOnGroups = {"superset.create.dashboard"})
    void createDataset() throws AtlanException {
        SupersetDataset toCreate =
                SupersetDataset.creator(DATASET_NAME, dashboard).build();
        AssetMutationResponse response = toCreate.save();
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof SupersetDashboard);
        SupersetDashboard c = (SupersetDashboard) one;
        assertEquals(c.getGuid(), dashboard.getGuid());
        assertEquals(c.getQualifiedName(), dashboard.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof SupersetDataset);
        dataset = (SupersetDataset) one;
        assertNotNull(dataset.getGuid());
        assertNotNull(dataset.getQualifiedName());
        assertEquals(dataset.getName(), DATASET_NAME);
        assertEquals(dataset.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(dataset.getSupersetDashboardQualifiedName(), dashboard.getQualifiedName());
        assertEquals(dataset.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"superset.update.dashboard"},
            dependsOnGroups = {"superset.create.dashboard"})
    void updateDashboard() throws AtlanException {
        SupersetDashboard updated = SupersetDashboard.updateCertificate(
                dashboard.getQualifiedName(), CERTIFICATE_STATUS, CERTIFICATE_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(updated.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        updated = SupersetDashboard.updateAnnouncement(
                dashboard.getQualifiedName(), ANNOUNCEMENT_TYPE, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"superset.read.dashboard"},
            dependsOnGroups = {"superset.create.*", "superset.update.dashboard"})
    void retrieveDashboard() throws AtlanException {
        SupersetDashboard c = SupersetDashboard.get(dashboard.getGuid());
        assertNotNull(c);
        assertTrue(c.isComplete());
        assertEquals(c.getGuid(), dashboard.getGuid());
        assertEquals(c.getQualifiedName(), dashboard.getQualifiedName());
        assertEquals(c.getName(), DASHBOARD_NAME);
        assertEquals(c.getCertificateStatus(), CERTIFICATE_STATUS);
        assertNotNull(c.getSupersetCharts());
        assertEquals(c.getSupersetCharts().size(), 1);
        Set<String> types =
                c.getSupersetCharts().stream().map(ISupersetChart::getTypeName).collect(Collectors.toSet());
        assertEquals(types.size(), 1);
        assertTrue(types.contains(SupersetChart.TYPE_NAME));
        Set<String> guids =
                c.getSupersetCharts().stream().map(ISupersetChart::getGuid).collect(Collectors.toSet());
        assertEquals(guids.size(), 1);
        assertTrue(guids.contains(chart.getGuid()));
        assertNotNull(c.getSupersetDatasets());
        assertEquals(c.getSupersetDatasets().size(), 1);
        types = c.getSupersetDatasets().stream()
                .map(ISupersetDataset::getTypeName)
                .collect(Collectors.toSet());
        assertEquals(types.size(), 1);
        assertTrue(types.contains(SupersetDataset.TYPE_NAME));
        guids = c.getSupersetDatasets().stream().map(ISupersetDataset::getGuid).collect(Collectors.toSet());
        assertEquals(guids.size(), 1);
        assertTrue(guids.contains(dataset.getGuid()));
    }

    @Test(
            groups = {"superset.update.dashboard.again"},
            dependsOnGroups = {"superset.read.dashboard"})
    void updateDashboardAgain() throws AtlanException {
        SupersetDashboard updated = SupersetDashboard.removeCertificate(dashboard.getQualifiedName(), DASHBOARD_NAME);
        assertNotNull(updated);
        assertNull(updated.getCertificateStatus());
        assertNull(updated.getCertificateStatusMessage());
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        updated = SupersetDashboard.removeAnnouncement(dashboard.getQualifiedName(), DASHBOARD_NAME);
        assertNotNull(updated);
        assertNull(updated.getAnnouncementType());
        assertNull(updated.getAnnouncementTitle());
        assertNull(updated.getAnnouncementMessage());
    }

    @Test(
            groups = {"superset.search.assets"},
            dependsOnGroups = {"superset.update.dashboard.again"})
    void searchAssets() throws AtlanException, InterruptedException {
        IndexSearchRequest index = Atlan.getDefaultClient()
                .assets
                .select()
                .where(Asset.SUPER_TYPE_NAMES.eq(ISuperset.TYPE_NAME))
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
        assertTrue(one instanceof SupersetDashboard);
        assertFalse(one.isComplete());
        SupersetDashboard d = (SupersetDashboard) one;
        assertEquals(d.getQualifiedName(), dashboard.getQualifiedName());
        assertEquals(d.getName(), dashboard.getName());
        assertEquals(d.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(1);
        assertTrue(one instanceof SupersetChart);
        assertFalse(one.isComplete());
        SupersetChart c = (SupersetChart) one;
        assertEquals(c.getQualifiedName(), chart.getQualifiedName());
        assertEquals(c.getName(), chart.getName());
        assertEquals(c.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(2);
        assertTrue(one instanceof SupersetDataset);
        assertFalse(one.isComplete());
        SupersetDataset ds = (SupersetDataset) one;
        assertEquals(ds.getQualifiedName(), dataset.getQualifiedName());
        assertEquals(ds.getName(), dataset.getName());
        assertEquals(ds.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"superset.delete.chart"},
            dependsOnGroups = {"superset.update.*", "superset.search.*"})
    void deleteChart() throws AtlanException {
        AssetMutationResponse response = Asset.delete(chart.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof SupersetChart);
        SupersetChart s = (SupersetChart) one;
        assertEquals(s.getGuid(), chart.getGuid());
        assertEquals(s.getQualifiedName(), chart.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "SOFT");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"superset.delete.chart.read"},
            dependsOnGroups = {"superset.delete.chart"})
    void readDeletedChart() throws AtlanException {
        validateDeletedAsset(chart, log);
    }

    @Test(
            groups = {"superset.delete.chart.restore"},
            dependsOnGroups = {"superset.delete.chart.read"})
    void restoreChart() throws AtlanException {
        assertTrue(SupersetChart.restore(chart.getQualifiedName()));
        SupersetChart restored = SupersetChart.get(chart.getQualifiedName());
        assertEquals(restored.getGuid(), chart.getGuid());
        assertEquals(restored.getQualifiedName(), chart.getQualifiedName());
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"superset.purge.chart"},
            dependsOnGroups = {"superset.delete.chart.restore"})
    void purgeChart() throws AtlanException {
        AssetMutationResponse response = Asset.purge(chart.getGuid());
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof SupersetChart);
        SupersetChart s = (SupersetChart) one;
        assertEquals(s.getGuid(), chart.getGuid());
        assertEquals(s.getQualifiedName(), chart.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "PURGE");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"superset.purge.connection"},
            dependsOnGroups = {
                "superset.create.*",
                "superset.read.*",
                "superset.search.*",
                "superset.update.*",
                "superset.purge.chart"
            })
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(connection.getQualifiedName(), log);
    }
}
