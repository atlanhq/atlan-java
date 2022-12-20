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
 * Tests all aspects of Preset assets.
 */
@Test(groups = {"preset"})
@Slf4j
public class PresetAssetTest extends AtlanLiveTest {

    private static final String PREFIX = "PresetAssetTest";

    private static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.PRESET;
    private static final String CONNECTION_NAME = "java-sdk-" + PREFIX;

    private static final String WORKSPACE_NAME = PREFIX + "-ws";
    private static final String COLLECTION_NAME = PREFIX + "-coll";
    private static final String CHART_NAME = PREFIX + "-cht";
    private static final String DATASET_NAME = PREFIX + "-ds";

    private static Connection connection = null;
    private static PresetWorkspace workspace = null;
    private static PresetDashboard collection = null;
    private static PresetChart chart = null;
    private static PresetDataset dataset = null;

    @Test(groups = {"create.connection"})
    void createConnection() throws AtlanException {
        connection = ConnectionTest.createConnection(CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"create.workspace"},
            dependsOnGroups = {"create.connection"})
    void createWorkspace() throws AtlanException {
        PresetWorkspace toCreate = PresetWorkspace.creator(WORKSPACE_NAME, connection.getQualifiedName())
                .build();
        AssetMutationResponse response = toCreate.upsert();
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof PresetWorkspace);
        workspace = (PresetWorkspace) one;
        assertNotNull(workspace.getGuid());
        assertNotNull(workspace.getQualifiedName());
        assertEquals(workspace.getName(), WORKSPACE_NAME);
        assertEquals(workspace.getConnectorType(), AtlanConnectorType.PRESET);
        assertEquals(workspace.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"create.collection"},
            dependsOnGroups = {"create.workspace"})
    void createCollection() throws AtlanException {
        PresetDashboard toCreate = PresetDashboard.creator(COLLECTION_NAME, workspace.getQualifiedName())
                .build();
        AssetMutationResponse response = toCreate.upsert();
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof PresetWorkspace);
        PresetWorkspace w = (PresetWorkspace) one;
        assertEquals(w.getGuid(), workspace.getGuid());
        assertEquals(w.getQualifiedName(), workspace.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof PresetDashboard);
        collection = (PresetDashboard) one;
        assertNotNull(collection.getGuid());
        assertNotNull(collection.getQualifiedName());
        assertEquals(collection.getName(), COLLECTION_NAME);
        assertEquals(collection.getConnectorType(), AtlanConnectorType.PRESET);
        assertEquals(collection.getPresetWorkspaceQualifiedName(), workspace.getQualifiedName());
        assertEquals(collection.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"create.chart"},
            dependsOnGroups = {"create.collection"})
    void createChart() throws AtlanException {
        PresetChart toCreate =
                PresetChart.creator(CHART_NAME, collection.getQualifiedName()).build();
        AssetMutationResponse response = toCreate.upsert();
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof PresetDashboard);
        PresetDashboard c = (PresetDashboard) one;
        assertEquals(c.getGuid(), collection.getGuid());
        assertEquals(c.getQualifiedName(), collection.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof PresetChart);
        chart = (PresetChart) one;
        assertNotNull(chart.getGuid());
        assertNotNull(chart.getQualifiedName());
        assertEquals(chart.getName(), CHART_NAME);
        assertEquals(chart.getConnectorType(), AtlanConnectorType.PRESET);
        assertEquals(chart.getPresetDashboardQualifiedName(), collection.getQualifiedName());
        assertEquals(chart.getPresetWorkspaceQualifiedName(), workspace.getQualifiedName());
        assertEquals(chart.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"create.dataset"},
            dependsOnGroups = {"create.collection"})
    void createDataset() throws AtlanException {
        PresetDataset toCreate = PresetDataset.creator(DATASET_NAME, collection.getQualifiedName())
                .build();
        AssetMutationResponse response = toCreate.upsert();
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof PresetDashboard);
        PresetDashboard c = (PresetDashboard) one;
        assertEquals(c.getGuid(), collection.getGuid());
        assertEquals(c.getQualifiedName(), collection.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof PresetDataset);
        dataset = (PresetDataset) one;
        assertNotNull(dataset.getGuid());
        assertNotNull(dataset.getQualifiedName());
        assertEquals(dataset.getName(), DATASET_NAME);
        assertEquals(dataset.getConnectorType(), AtlanConnectorType.PRESET);
        assertEquals(dataset.getPresetDashboardQualifiedName(), collection.getQualifiedName());
        assertEquals(dataset.getPresetWorkspaceQualifiedName(), workspace.getQualifiedName());
        assertEquals(dataset.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"update.collection"},
            dependsOnGroups = {"create.collection"})
    void updateCollection() throws AtlanException {
        PresetDashboard updated = PresetDashboard.updateCertificate(
                collection.getQualifiedName(), CERTIFICATE_STATUS, CERTIFICATE_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(updated.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        updated = PresetDashboard.updateAnnouncement(
                collection.getQualifiedName(), ANNOUNCEMENT_TYPE, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"read.collection"},
            dependsOnGroups = {"create.*", "update.collection"})
    void retrieveCollection() throws AtlanException {
        PresetDashboard c = PresetDashboard.retrieveByGuid(collection.getGuid());
        assertNotNull(c);
        assertTrue(c.isComplete());
        assertEquals(c.getGuid(), collection.getGuid());
        assertEquals(c.getQualifiedName(), collection.getQualifiedName());
        assertEquals(c.getName(), COLLECTION_NAME);
        assertEquals(c.getCertificateStatus(), CERTIFICATE_STATUS);
        assertNotNull(c.getPresetCharts());
        assertEquals(c.getPresetCharts().size(), 1);
        Set<String> types =
                c.getPresetCharts().stream().map(PresetChart::getTypeName).collect(Collectors.toSet());
        assertEquals(types.size(), 1);
        assertTrue(types.contains(PresetChart.TYPE_NAME));
        Set<String> guids =
                c.getPresetCharts().stream().map(PresetChart::getGuid).collect(Collectors.toSet());
        assertEquals(guids.size(), 1);
        assertTrue(guids.contains(chart.getGuid()));
        assertNotNull(c.getPresetDatasets());
        assertEquals(c.getPresetDatasets().size(), 1);
        types = c.getPresetDatasets().stream().map(PresetDataset::getTypeName).collect(Collectors.toSet());
        assertEquals(types.size(), 1);
        assertTrue(types.contains(PresetDataset.TYPE_NAME));
        guids = c.getPresetDatasets().stream().map(PresetDataset::getGuid).collect(Collectors.toSet());
        assertEquals(guids.size(), 1);
        assertTrue(guids.contains(dataset.getGuid()));
    }

    @Test(
            groups = {"update.collection.again"},
            dependsOnGroups = {"read.collection"})
    void updateCollectionAgain() throws AtlanException {
        PresetDashboard updated = PresetDashboard.removeCertificate(collection.getQualifiedName(), COLLECTION_NAME);
        assertNotNull(updated);
        assertNull(updated.getCertificateStatus());
        assertNull(updated.getCertificateStatusMessage());
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        updated = PresetDashboard.removeAnnouncement(collection.getQualifiedName(), COLLECTION_NAME);
        assertNotNull(updated);
        assertNull(updated.getAnnouncementType());
        assertNull(updated.getAnnouncementTitle());
        assertNull(updated.getAnnouncementMessage());
    }

    @Test(
            groups = {"search.assets"},
            dependsOnGroups = {"update.collection.again"})
    void searchAssets() throws AtlanException {
        Query byState = QueryFactory.active();
        Query byType = QueryFactory.withSuperType(Preset.TYPE_NAME);
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
                4);

        assertEquals(response.getApproximateCount().longValue(), 4L);
        List<Asset> entities = response.getAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 4);

        Asset one = entities.get(0);
        assertTrue(one instanceof PresetWorkspace);
        assertFalse(one.isComplete());
        PresetWorkspace w = (PresetWorkspace) one;
        assertEquals(w.getQualifiedName(), workspace.getQualifiedName());
        assertEquals(w.getName(), workspace.getName());
        assertEquals(w.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(1);
        assertTrue(one instanceof PresetDashboard);
        assertFalse(one.isComplete());
        PresetDashboard d = (PresetDashboard) one;
        assertEquals(d.getQualifiedName(), collection.getQualifiedName());
        assertEquals(d.getName(), collection.getName());
        assertEquals(d.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(2);
        assertTrue(one instanceof PresetChart);
        assertFalse(one.isComplete());
        PresetChart c = (PresetChart) one;
        assertEquals(c.getQualifiedName(), chart.getQualifiedName());
        assertEquals(c.getName(), chart.getName());
        assertEquals(c.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(3);
        assertTrue(one instanceof PresetDataset);
        assertFalse(one.isComplete());
        PresetDataset ds = (PresetDataset) one;
        assertEquals(ds.getQualifiedName(), dataset.getQualifiedName());
        assertEquals(ds.getName(), dataset.getName());
        assertEquals(ds.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"delete.chart"},
            dependsOnGroups = {"update.*", "search.*"})
    void deleteChart() throws AtlanException {
        AssetMutationResponse response = Asset.delete(chart.getGuid());
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof PresetChart);
        PresetChart s = (PresetChart) one;
        assertEquals(s.getGuid(), chart.getGuid());
        assertEquals(s.getQualifiedName(), chart.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "SOFT");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"delete.chart.read"},
            dependsOnGroups = {"delete.chart"})
    void readDeletedChart() throws AtlanException {
        PresetChart deleted = PresetChart.retrieveByGuid(chart.getGuid());
        assertEquals(deleted.getGuid(), chart.getGuid());
        assertEquals(deleted.getQualifiedName(), chart.getQualifiedName());
        assertEquals(deleted.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"delete.chart.restore"},
            dependsOnGroups = {"delete.chart.read"})
    void restoreChart() throws AtlanException {
        assertTrue(PresetChart.restore(chart.getQualifiedName()));
        PresetChart restored = PresetChart.retrieveByQualifiedName(chart.getQualifiedName());
        assertEquals(restored.getGuid(), chart.getGuid());
        assertEquals(restored.getQualifiedName(), chart.getQualifiedName());
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"purge.chart"},
            dependsOnGroups = {"delete.chart.restore"})
    void purgeChart() throws AtlanException {
        AssetMutationResponse response = Asset.purge(chart.getGuid());
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof PresetChart);
        PresetChart s = (PresetChart) one;
        assertEquals(s.getGuid(), chart.getGuid());
        assertEquals(s.getQualifiedName(), chart.getQualifiedName());
        assertEquals(s.getDeleteHandler(), "HARD");
        assertEquals(s.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"purge.connection"},
            dependsOnGroups = {"create.*", "read.*", "search.*", "update.*", "purge.chart"},
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(connection.getQualifiedName(), log);
    }
}
