/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.cache.RoleCache;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.*;
import com.atlan.model.core.Entity;
import com.atlan.model.core.EntityMutationResponse;
import com.atlan.model.enums.*;
import com.atlan.model.relations.Reference;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.testng.annotations.Test;

@Test(groups = {"preset-asset"})
public class PresetAssetTest extends AtlanLiveTest {

    public static final String CONNECTION_NAME = "preset-connection";
    public static final String WORKSPACE_NAME = "ps-workspace";
    public static final String COLLECTION_NAME = "ps-collection";
    public static final String CHART_NAME = "ps-chart";
    public static final String DATASET_NAME = "ps-dataset";

    public static String connectionGuid = null;
    public static String connectionQame = null;

    public static String workspaceGuid = null;
    public static String workspaceQame = null;

    public static String collectionGuid = null;
    public static String collectionQame = null;
    public static String chartGuid = null;
    public static String chartQame = null;
    public static String datasetGuid = null;
    public static String datasetQame = null;

    @Test(groups = {"invalid.connection"})
    void invalidConnection() {
        assertThrows(
                InvalidRequestException.class,
                () -> Connection.creator(CONNECTION_NAME, AtlanConnectorType.PRESET, null, null, null));
    }

    @Test(groups = {"create.connection.preset"})
    void createConnection() {
        try {
            String adminRoleGuid = RoleCache.getIdForName("$admin");
            if (adminRoleGuid != null) {
                Connection connection = Connection.creator(
                                CONNECTION_NAME, AtlanConnectorType.PRESET, List.of(adminRoleGuid), null, null)
                        .build();
                EntityMutationResponse response = connection.upsert();
                assertNotNull(response);
                assertTrue(response.getUpdatedEntities().isEmpty());
                assertTrue(response.getDeletedEntities().isEmpty());
                assertEquals(response.getCreatedEntities().size(), 1);
                Entity one = response.getCreatedEntities().get(0);
                assertNotNull(one);
                assertEquals(one.getTypeName(), Connection.TYPE_NAME);
                assertTrue(one instanceof Connection);
                connection = (Connection) one;
                connectionGuid = connection.getGuid();
                assertNotNull(connectionGuid);
                connectionQame = connection.getQualifiedName();
                assertNotNull(connectionQame);
                assertEquals(connection.getName(), CONNECTION_NAME);
            }
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a connection.");
        }
    }

    @Test(
            groups = {"read.connection.preset"},
            dependsOnGroups = {"create.connection.preset"})
    void retrieveConnection() {
        Entity full = null;
        do {
            try {
                full = Entity.retrieveFull(connectionGuid);
            } catch (AtlanException e) {
                e.printStackTrace();
                assertNull(e, "Unexpected exception while trying to read-back the created connection.");
            }
        } while (full == null);
    }

    @Test(
            groups = {"create.preset.workspace"},
            dependsOnGroups = {"read.connection.preset"})
    void createWorkspace() {
        try {
            PresetWorkspace workspace =
                    PresetWorkspace.creator(WORKSPACE_NAME, connectionQame).build();
            EntityMutationResponse response = workspace.upsert();
            assertNotNull(response);
            assertTrue(response.getUpdatedEntities().isEmpty());
            assertTrue(response.getDeletedEntities().isEmpty());
            assertEquals(response.getCreatedEntities().size(), 1);
            Entity one = response.getCreatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), PresetWorkspace.TYPE_NAME);
            assertTrue(one instanceof PresetWorkspace);
            workspace = (PresetWorkspace) one;
            workspaceGuid = workspace.getGuid();
            assertNotNull(workspaceGuid);
            workspaceQame = workspace.getQualifiedName();
            assertNotNull(workspaceQame);
            assertEquals(workspace.getName(), WORKSPACE_NAME);
            assertEquals(workspace.getConnectorType(), AtlanConnectorType.PRESET);
            assertEquals(workspace.getConnectionQualifiedName(), connectionQame);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a Preset workspace.");
        }
    }

    @Test(
            groups = {"create.preset.collection"},
            dependsOnGroups = {"create.preset.workspace"})
    void createCollection() {
        try {
            PresetDashboard collection =
                    PresetDashboard.creator(COLLECTION_NAME, workspaceQame).build();
            EntityMutationResponse response = collection.upsert();
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertTrue(response.getUpdatedEntities().isEmpty());
            assertEquals(response.getCreatedEntities().size(), 1);
            Entity one = response.getCreatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), PresetDashboard.TYPE_NAME);
            assertTrue(one instanceof PresetDashboard);
            collection = (PresetDashboard) one;
            collectionGuid = collection.getGuid();
            assertNotNull(collectionGuid);
            collectionQame = collection.getQualifiedName();
            assertNotNull(collectionQame);
            assertEquals(collection.getName(), COLLECTION_NAME);
            assertEquals(collection.getConnectorType(), AtlanConnectorType.PRESET);
            assertEquals(collection.getPresetWorkspaceQualifiedName(), workspaceQame);
            assertEquals(collection.getConnectionQualifiedName(), connectionQame);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a Preset collection.");
        }
    }

    @Test(
            groups = {"create.preset.chart"},
            dependsOnGroups = {"create.preset.collection"})
    void createChart() {
        try {
            PresetChart chart = PresetChart.creator(CHART_NAME, collectionQame).build();
            EntityMutationResponse response = chart.upsert();
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 1);
            Entity one = response.getUpdatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), PresetDashboard.TYPE_NAME);
            assertTrue(one instanceof PresetDashboard);
            PresetDashboard collection = (PresetDashboard) one;
            assertEquals(collection.getGuid(), collectionGuid);
            assertEquals(collection.getQualifiedName(), collectionQame);
            assertEquals(response.getCreatedEntities().size(), 1);
            one = response.getCreatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), PresetChart.TYPE_NAME);
            assertTrue(one instanceof PresetChart);
            chart = (PresetChart) one;
            chartGuid = chart.getGuid();
            assertNotNull(chartGuid);
            chartQame = chart.getQualifiedName();
            assertNotNull(chartQame);
            assertEquals(chart.getName(), CHART_NAME);
            assertEquals(chart.getConnectorType(), AtlanConnectorType.PRESET);
            assertEquals(chart.getPresetDashboardQualifiedName(), collectionQame);
            assertEquals(chart.getPresetWorkspaceQualifiedName(), workspaceQame);
            assertEquals(chart.getConnectionQualifiedName(), connectionQame);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a Preset chart.");
        }
    }

    @Test(
            groups = {"create.preset.dataset"},
            dependsOnGroups = {"create.preset.collection"})
    void createDataset() {
        try {
            PresetDataset dataset =
                    PresetDataset.creator(DATASET_NAME, collectionQame).build();
            EntityMutationResponse response = dataset.upsert();
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 1);
            Entity one = response.getUpdatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), PresetDashboard.TYPE_NAME);
            assertTrue(one instanceof PresetDashboard);
            PresetDashboard collection = (PresetDashboard) one;
            assertEquals(collection.getGuid(), collectionGuid);
            assertEquals(collection.getQualifiedName(), collectionQame);
            assertEquals(response.getCreatedEntities().size(), 1);
            one = response.getCreatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), PresetDataset.TYPE_NAME);
            assertTrue(one instanceof PresetDataset);
            dataset = (PresetDataset) one;
            datasetGuid = dataset.getGuid();
            assertNotNull(datasetGuid);
            datasetQame = dataset.getQualifiedName();
            assertNotNull(datasetQame);
            assertEquals(dataset.getName(), DATASET_NAME);
            assertEquals(dataset.getConnectorType(), AtlanConnectorType.PRESET);
            assertEquals(dataset.getPresetDashboardQualifiedName(), collectionQame);
            assertEquals(dataset.getPresetWorkspaceQualifiedName(), workspaceQame);
            assertEquals(dataset.getConnectionQualifiedName(), connectionQame);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a Preset dataset.");
        }
    }

    @Test(
            groups = {"update.preset.collection"},
            dependsOnGroups = {"create.preset.collection"})
    void updateCollection() {
        try {
            PresetDashboard updated =
                    PresetDashboard.updateCertificate(collectionQame, AtlanCertificateStatus.VERIFIED, null);
            assertNotNull(updated);
            assertEquals(updated.getCertificateStatus(), AtlanCertificateStatus.VERIFIED);
            updated = PresetDashboard.updateAnnouncement(
                    collectionQame, AtlanAnnouncementType.INFORMATION, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
            assertNotNull(updated);
            assertEquals(updated.getAnnouncementType(), AtlanAnnouncementType.INFORMATION);
            assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
            assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to update a Preset collection.");
        }
    }

    @Test(
            groups = {"read.preset.collection"},
            dependsOnGroups = {"create.preset.*", "update.preset.collection"})
    void retrieveCollection() {
        try {
            Entity full = Entity.retrieveFull(collectionGuid);
            assertNotNull(full);
            assertTrue(full instanceof PresetDashboard);
            PresetDashboard collection = (PresetDashboard) full;
            assertEquals(collection.getGuid(), collectionGuid);
            assertEquals(collection.getQualifiedName(), collectionQame);
            assertEquals(collection.getName(), COLLECTION_NAME);
            assertEquals(collection.getCertificateStatus(), AtlanCertificateStatus.VERIFIED);
            assertNotNull(collection.getPresetCharts());
            assertEquals(collection.getPresetCharts().size(), 1);
            Set<String> types = collection.getPresetCharts().stream()
                    .map(Reference::getTypeName)
                    .collect(Collectors.toSet());
            assertEquals(types.size(), 1);
            assertTrue(types.contains(PresetChart.TYPE_NAME));
            Set<String> guids = collection.getPresetCharts().stream()
                    .map(Reference::getGuid)
                    .collect(Collectors.toSet());
            assertEquals(guids.size(), 1);
            assertTrue(guids.contains(chartGuid));
            assertNotNull(collection.getPresetDatasets());
            assertEquals(collection.getPresetDatasets().size(), 1);
            types = collection.getPresetDatasets().stream()
                    .map(Reference::getTypeName)
                    .collect(Collectors.toSet());
            assertEquals(types.size(), 1);
            assertTrue(types.contains(PresetDataset.TYPE_NAME));
            guids = collection.getPresetDatasets().stream()
                    .map(Reference::getGuid)
                    .collect(Collectors.toSet());
            assertEquals(guids.size(), 1);
            assertTrue(guids.contains(datasetGuid));
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to retrieve a Preset collection.");
        }
    }

    @Test(
            groups = {"update.preset.collection.again"},
            dependsOnGroups = {"read.preset.collection"})
    void updateCollectionAgain() {
        try {
            PresetDashboard updated = PresetDashboard.removeCertificate(collectionQame, COLLECTION_NAME);
            assertNotNull(updated);
            assertNull(updated.getCertificateStatus());
            assertNull(updated.getCertificateStatusMessage());
            assertEquals(updated.getAnnouncementType(), AtlanAnnouncementType.INFORMATION);
            assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
            assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
            updated = PresetDashboard.removeAnnouncement(collectionQame, COLLECTION_NAME);
            assertNotNull(updated);
            assertNull(updated.getAnnouncementType());
            assertNull(updated.getAnnouncementTitle());
            assertNull(updated.getAnnouncementMessage());
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(
                    e,
                    "Unexpected exception while trying to remove certificates and announcements from a Preset collection.");
        }
    }
}
