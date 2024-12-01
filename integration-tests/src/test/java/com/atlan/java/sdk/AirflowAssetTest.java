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
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * Tests all aspects of Airflow assets.
 */
@Slf4j
public class AirflowAssetTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("airflow");

    public static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.AIRFLOW;
    public static final String CONNECTION_NAME = PREFIX;

    private static final String DAG_NAME = makeUnique("dag");
    private static final String TASK_NAME = makeUnique("task");

    private static Connection connection = null;
    private static AirflowDag dag = null;
    private static AirflowTask task = null;

    @Test(groups = {"airflow.create.connection"})
    void createConnection() throws AtlanException, InterruptedException {
        connection = ConnectionTest.createConnection(client, CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"airflow.create.dag"},
            dependsOnGroups = {"airflow.create.connection"})
    void createDAG() throws AtlanException {
        AirflowDag toCreate =
                AirflowDag.creator(DAG_NAME, connection.getQualifiedName()).build();
        AssetMutationResponse response = toCreate.save(client);
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof AirflowDag);
        dag = (AirflowDag) one;
        assertNotNull(dag.getGuid());
        assertNotNull(dag.getQualifiedName());
        assertEquals(dag.getName(), DAG_NAME);
        assertEquals(dag.getConnectorType(), CONNECTOR_TYPE);
    }

    @Test(
            groups = {"airflow.create.task"},
            dependsOnGroups = {"airflow.create.dag"})
    void createTask() throws AtlanException {
        AirflowTask toCreate = AirflowTask.creator(TASK_NAME, dag).build();
        AssetMutationResponse response = toCreate.save(client);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof AirflowDag);
        AirflowDag d = (AirflowDag) one;
        assertEquals(d.getGuid(), dag.getGuid());
        assertEquals(d.getQualifiedName(), dag.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof AirflowTask);
        task = (AirflowTask) one;
        assertNotNull(task.getGuid());
        assertNotNull(task.getQualifiedName());
        assertEquals(task.getName(), TASK_NAME);
        assertEquals(task.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(task.getAirflowDagName(), DAG_NAME);
        assertEquals(task.getAirflowDagQualifiedName(), dag.getQualifiedName());
    }

    @Test(
            groups = {"airflow.update.dag"},
            dependsOnGroups = {"airflow.create.dag"})
    void updateDAG() throws AtlanException {
        AirflowDag updated =
                AirflowDag.updateCertificate(client, dag.getQualifiedName(), CERTIFICATE_STATUS, CERTIFICATE_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(updated.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        updated = AirflowDag.updateAnnouncement(
                client, dag.getQualifiedName(), ANNOUNCEMENT_TYPE, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
        assertNotNull(updated);
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"airflow.read.dag"},
            dependsOnGroups = {"airflow.create.task", "airflow.update.dag"})
    void retrieveDAG() throws AtlanException {
        AirflowDag d = AirflowDag.get(client, dag.getGuid(), true);
        assertNotNull(d);
        assertTrue(d.isComplete());
        assertEquals(d.getGuid(), dag.getGuid());
        assertEquals(d.getQualifiedName(), dag.getQualifiedName());
        assertEquals(d.getName(), DAG_NAME);
        assertEquals(d.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(d.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
        assertNotNull(d.getAirflowTasks());
        assertEquals(d.getAirflowTasks().size(), 1);
        Set<String> types =
                d.getAirflowTasks().stream().map(IAirflowTask::getTypeName).collect(Collectors.toSet());
        assertEquals(types.size(), 1);
        assertTrue(types.contains(AirflowTask.TYPE_NAME));
        Set<String> guids =
                d.getAirflowTasks().stream().map(IAirflowTask::getGuid).collect(Collectors.toSet());
        assertEquals(guids.size(), 1);
        assertTrue(guids.contains(task.getGuid()));
    }

    @Test(
            groups = {"airflow.update.dag.again"},
            dependsOnGroups = {"airflow.read.dag"})
    void updateDAGAgain() throws AtlanException {
        AirflowDag updated = AirflowDag.removeCertificate(client, dag.getQualifiedName(), DAG_NAME);
        assertNotNull(updated);
        assertNull(updated.getCertificateStatus());
        assertNull(updated.getCertificateStatusMessage());
        assertEquals(updated.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        updated = AirflowDag.removeAnnouncement(client, dag.getQualifiedName(), DAG_NAME);
        assertNotNull(updated);
        assertNull(updated.getAnnouncementType());
        assertNull(updated.getAnnouncementTitle());
        assertNull(updated.getAnnouncementMessage());
    }

    @Test(
            groups = {"airflow.search.assets"},
            dependsOnGroups = {"airflow.update.dag.again"})
    void searchAssets() throws AtlanException, InterruptedException {
        IndexSearchRequest index = client.assets
                .select()
                .where(Asset.SUPER_TYPE_NAMES.eq(IAirflow.TYPE_NAME))
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
        assertTrue(one instanceof AirflowDag);
        assertFalse(one.isComplete());
        AirflowDag d = (AirflowDag) one;
        assertEquals(d.getQualifiedName(), dag.getQualifiedName());
        assertEquals(d.getName(), dag.getName());
        assertEquals(d.getConnectionQualifiedName(), connection.getQualifiedName());

        one = entities.get(1);
        assertTrue(one instanceof AirflowTask);
        assertFalse(one.isComplete());
        AirflowTask t = (AirflowTask) one;
        assertEquals(t.getQualifiedName(), task.getQualifiedName());
        assertEquals(t.getName(), task.getName());
        assertEquals(t.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"airflow.delete.task"},
            dependsOnGroups = {"airflow.update.*", "airflow.search.*"})
    void deleteTask() throws AtlanException {
        AssetMutationResponse response = Asset.delete(client, task.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof AirflowTask);
        AirflowTask t = (AirflowTask) one;
        assertEquals(t.getGuid(), task.getGuid());
        assertEquals(t.getQualifiedName(), task.getQualifiedName());
        assertEquals(t.getDeleteHandler(), "SOFT");
        assertEquals(t.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"airflow.delete.task.read"},
            dependsOnGroups = {"airflow.delete.task"})
    void readDeletedTask() throws AtlanException {
        validateDeletedAsset(task, log);
    }

    @Test(
            groups = {"airflow.delete.task.restore"},
            dependsOnGroups = {"airflow.delete.task.read"})
    void restoreTask() throws AtlanException {
        assertTrue(AirflowTask.restore(client, task.getQualifiedName()));
        AirflowTask restored = AirflowTask.get(client, task.getQualifiedName());
        assertEquals(restored.getGuid(), task.getGuid());
        assertEquals(restored.getQualifiedName(), task.getQualifiedName());
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"airflow.purge.task"},
            dependsOnGroups = {"airflow.delete.task.restore"})
    void purgeTask() throws AtlanException {
        AssetMutationResponse response = Asset.purge(client, task.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof AirflowTask);
        AirflowTask t = (AirflowTask) one;
        assertEquals(t.getGuid(), task.getGuid());
        assertEquals(t.getQualifiedName(), task.getQualifiedName());
        assertEquals(t.getDeleteHandler(), "PURGE");
        assertEquals(t.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"airflow.purge.connection"},
            dependsOnGroups = {
                "airflow.create.*",
                "airflow.read.*",
                "airflow.search.*",
                "airflow.update.*",
                "airflow.purge.task"
            },
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(client, connection.getQualifiedName(), log);
    }
}
