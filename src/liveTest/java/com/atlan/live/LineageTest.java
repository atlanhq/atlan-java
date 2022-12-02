/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.*;
import com.atlan.model.core.Entity;
import com.atlan.model.core.EntityMutationResponse;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.lineage.LineageRequest;
import com.atlan.model.lineage.LineageResponse;
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
 * Tests all aspects of lineage.
 */
@Test(groups = {"lineage"})
@Slf4j
public class LineageTest extends AtlanLiveTest {

    private static final String PREFIX = "LineageTest";

    private static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.VERTICA;
    private static final String CONNECTION_NAME = "java-sdk-" + PREFIX;
    private static final String DATABASE_NAME = PREFIX + "_db";
    private static final String SCHEMA_NAME = PREFIX + "_schema";
    private static final String TABLE_NAME = PREFIX + "_tbl";
    private static final String MVIEW_NAME = PREFIX + "_mv";
    private static final String VIEW_NAME = PREFIX + "_v";
    private static final String COLUMN_NAME1 = PREFIX + "1";
    private static final String COLUMN_NAME2 = PREFIX + "2";
    private static final String COLUMN_NAME3 = PREFIX + "3";
    private static final String COLUMN_NAME4 = PREFIX + "4";
    private static final String COLUMN_NAME5 = PREFIX + "5";
    private static final String COLUMN_NAME6 = PREFIX + "6";

    private static Connection connection = null;
    private static Table table = null;
    private static MaterializedView mview = null;
    private static View view = null;
    private static Column column1 = null;
    private static Column column2 = null;
    private static Column column3 = null;
    private static Column column4 = null;
    private static Column column5 = null;
    private static Column column6 = null;

    private static LineageProcess start = null;
    private static LineageProcess end = null;

    @Test(groups = {"create.connection"})
    void createConnection() throws AtlanException {
        connection = ConnectionTest.createConnection(CONNECTION_NAME, CONNECTOR_TYPE);
        Database database = SQLAssetTest.createDatabase(DATABASE_NAME, connection.getQualifiedName());
        Schema schema = SQLAssetTest.createSchema(SCHEMA_NAME, database.getQualifiedName());
        table = SQLAssetTest.createTable(TABLE_NAME, schema.getQualifiedName());
        mview = SQLAssetTest.createMaterializedView(MVIEW_NAME, schema.getQualifiedName());
        view = SQLAssetTest.createView(VIEW_NAME, schema.getQualifiedName());
        column1 = SQLAssetTest.createColumn(COLUMN_NAME1, Table.TYPE_NAME, table.getQualifiedName(), 1);
        column2 = SQLAssetTest.createColumn(COLUMN_NAME2, Table.TYPE_NAME, table.getQualifiedName(), 2);
        column3 = SQLAssetTest.createColumn(COLUMN_NAME3, MaterializedView.TYPE_NAME, mview.getQualifiedName(), 1);
        column4 = SQLAssetTest.createColumn(COLUMN_NAME4, MaterializedView.TYPE_NAME, mview.getQualifiedName(), 2);
        column5 = SQLAssetTest.createColumn(COLUMN_NAME5, View.TYPE_NAME, view.getQualifiedName(), 1);
        column6 = SQLAssetTest.createColumn(COLUMN_NAME6, View.TYPE_NAME, view.getQualifiedName(), 2);
    }

    // TODO: column-level lineage
    // TODO: classification propagation through lineage
    // TODO: classification propagation through parent-child
    // TODO: classification propagation through term-asset
    // TODO: classification propagation through term-asset-child-lineage

    @Test(
            groups = {"create.lineage.start"},
            dependsOnGroups = {"create.connection"})
    void createLineageStart() throws AtlanException {
        final String processName = TABLE_NAME + " >> " + MVIEW_NAME;
        LineageProcess toCreate = LineageProcess.creator(
                        processName,
                        CONNECTOR_TYPE,
                        CONNECTION_NAME,
                        connection.getQualifiedName(),
                        List.of(Table.refByGuid(table.getGuid())),
                        List.of(MaterializedView.refByGuid(mview.getGuid())))
                .build();
        EntityMutationResponse response = toCreate.upsert();
        assertNotNull(response);
        assertEquals(response.getCreatedEntities().size(), 1);
        Entity one = response.getCreatedEntities().get(0);
        assertTrue(one instanceof LineageProcess);
        start = (LineageProcess) one;
        assertNotNull(start.getGuid());
        assertNotNull(start.getQualifiedName());
        assertEquals(start.getName(), processName);
        assertNotNull(start.getInputs());
        assertEquals(start.getInputs().size(), 1);
        for (Catalog input : start.getInputs()) {
            assertNotNull(input);
            assertEquals(input.getTypeName(), Table.TYPE_NAME);
            assertEquals(input.getGuid(), table.getGuid());
        }
        assertNotNull(start.getOutputs());
        assertEquals(start.getOutputs().size(), 1);
        for (Catalog output : start.getOutputs()) {
            assertNotNull(output);
            assertEquals(output.getTypeName(), MaterializedView.TYPE_NAME);
            assertEquals(output.getGuid(), mview.getGuid());
        }
        assertEquals(response.getUpdatedEntities().size(), 2);
        Set<String> types =
                response.getUpdatedEntities().stream().map(Entity::getTypeName).collect(Collectors.toSet());
        assertEquals(types.size(), 2);
        assertTrue(types.contains(Table.TYPE_NAME));
        assertTrue(types.contains(MaterializedView.TYPE_NAME));
        Set<String> guids =
                response.getUpdatedEntities().stream().map(Entity::getGuid).collect(Collectors.toSet());
        assertEquals(guids.size(), 2);
        assertTrue(guids.contains(table.getGuid()));
        assertTrue(guids.contains(mview.getGuid()));
    }

    @Test(
            groups = {"create.lineage.end"},
            dependsOnGroups = {"create.lineage.start"})
    void createLineageEnd() throws AtlanException {
        final String processName = MVIEW_NAME + " >> " + VIEW_NAME;
        LineageProcess toCreate = LineageProcess.creator(
                        processName,
                        CONNECTOR_TYPE,
                        CONNECTION_NAME,
                        connection.getQualifiedName(),
                        List.of(MaterializedView.refByGuid(mview.getGuid())),
                        List.of(View.refByGuid(view.getGuid())))
                .build();
        EntityMutationResponse response = toCreate.upsert();
        assertNotNull(response);
        assertEquals(response.getCreatedEntities().size(), 1);
        Entity one = response.getCreatedEntities().get(0);
        assertTrue(one instanceof LineageProcess);
        end = (LineageProcess) one;
        assertNotNull(end.getGuid());
        assertNotNull(end.getQualifiedName());
        assertEquals(end.getName(), processName);
        assertNotNull(end.getInputs());
        assertEquals(end.getInputs().size(), 1);
        for (Catalog input : end.getInputs()) {
            assertNotNull(input);
            assertEquals(input.getTypeName(), MaterializedView.TYPE_NAME);
            assertEquals(input.getGuid(), mview.getGuid());
        }
        assertNotNull(end.getOutputs());
        assertEquals(end.getOutputs().size(), 1);
        for (Catalog output : end.getOutputs()) {
            assertNotNull(output);
            assertEquals(output.getTypeName(), View.TYPE_NAME);
            assertEquals(output.getGuid(), view.getGuid());
        }
        assertEquals(response.getUpdatedEntities().size(), 2);
        Set<String> types =
                response.getUpdatedEntities().stream().map(Entity::getTypeName).collect(Collectors.toSet());
        assertEquals(types.size(), 2);
        assertTrue(types.contains(MaterializedView.TYPE_NAME));
        assertTrue(types.contains(View.TYPE_NAME));
        Set<String> guids =
                response.getUpdatedEntities().stream().map(Entity::getGuid).collect(Collectors.toSet());
        assertEquals(guids.size(), 2);
        assertTrue(guids.contains(mview.getGuid()));
        assertTrue(guids.contains(view.getGuid()));
    }

    @Test(
            groups = {"read.lineage"},
            dependsOnGroups = {"create.lineage.*"})
    void fetchLineageStart() throws AtlanException {
        LineageRequest lineage =
                LineageRequest.builder().guid(table.getGuid()).hideProcess(true).build();
        LineageResponse response = lineage.fetch();
        assertNotNull(response);
        assertEquals(response.getBaseEntityGuid(), table.getGuid());
        assertTrue(response.getUpstreamEntityGuids().isEmpty());
        Set<String> downstreamGuids = response.getDownstreamEntityGuids();
        assertNotNull(downstreamGuids);
        assertEquals(downstreamGuids.size(), 1);
        assertTrue(downstreamGuids.contains(mview.getGuid()));
        assertEquals(response.getDownstreamEntities().size(), 1);
        downstreamGuids = response.getDownstreamProcessGuids();
        assertNotNull(downstreamGuids);
        assertEquals(downstreamGuids.size(), 1);
        assertTrue(downstreamGuids.contains(start.getGuid()));
        List<String> dfsDownstreamGuids = response.getAllDownstreamEntityGuidsDFS();
        assertEquals(dfsDownstreamGuids.size(), 3);
        assertEquals(dfsDownstreamGuids.get(0), table.getGuid());
        assertEquals(dfsDownstreamGuids.get(1), mview.getGuid());
        assertEquals(dfsDownstreamGuids.get(2), view.getGuid());
        List<Entity> dfsDownstream = response.getAllDownstreamEntitiesDFS();
        assertEquals(dfsDownstream.size(), 3);
        Entity one = dfsDownstream.get(0);
        assertTrue(one instanceof Table);
        assertEquals(one.getGuid(), table.getGuid());
        one = dfsDownstream.get(1);
        assertEquals(one.getGuid(), mview.getGuid());
        one = dfsDownstream.get(2);
        assertEquals(one.getGuid(), view.getGuid());
        List<String> dfsUpstreamGuids = response.getAllUpstreamEntityGuidsDFS();
        assertEquals(dfsUpstreamGuids.size(), 1);
        assertEquals(dfsUpstreamGuids.get(0), table.getGuid());
    }

    @Test(
            groups = {"read.lineage"},
            dependsOnGroups = {"create.lineage.*"})
    void fetchLineageMiddle() throws AtlanException {
        LineageRequest lineage =
                LineageRequest.builder().guid(mview.getGuid()).hideProcess(true).build();
        LineageResponse response = lineage.fetch();
        assertNotNull(response);
        assertEquals(response.getBaseEntityGuid(), mview.getGuid());
        Set<String> upstreamGuids = response.getUpstreamEntityGuids();
        assertNotNull(upstreamGuids);
        assertEquals(upstreamGuids.size(), 1);
        assertTrue(upstreamGuids.contains(table.getGuid()));
        assertEquals(response.getUpstreamEntities().size(), 1);
        Entity one = response.getUpstreamEntities().get(0);
        assertNotNull(one);
        assertTrue(one instanceof Table);
        Table t = (Table) one;
        assertEquals(t.getQualifiedName(), table.getQualifiedName());
        upstreamGuids = response.getUpstreamProcessGuids();
        assertNotNull(upstreamGuids);
        assertEquals(upstreamGuids.size(), 1);
        assertTrue(upstreamGuids.contains(start.getGuid()));
        Set<String> downstreamGuids = response.getDownstreamEntityGuids();
        assertNotNull(downstreamGuids);
        assertEquals(downstreamGuids.size(), 1);
        assertTrue(downstreamGuids.contains(view.getGuid()));
        assertEquals(response.getDownstreamEntities().size(), 1);
        one = response.getDownstreamEntities().get(0);
        assertNotNull(one);
        assertTrue(one instanceof View);
        View v = (View) one;
        assertEquals(v.getQualifiedName(), view.getQualifiedName());
        downstreamGuids = response.getDownstreamProcessGuids();
        assertNotNull(downstreamGuids);
        assertEquals(downstreamGuids.size(), 1);
        assertTrue(downstreamGuids.contains(end.getGuid()));
        List<String> dfsDownstreamGuids = response.getAllDownstreamEntityGuidsDFS();
        assertEquals(dfsDownstreamGuids.size(), 2);
        assertEquals(dfsDownstreamGuids.get(0), mview.getGuid());
        assertEquals(dfsDownstreamGuids.get(1), view.getGuid());
        List<Entity> dfsDownstream = response.getAllDownstreamEntitiesDFS();
        assertEquals(dfsDownstream.size(), 2);
        one = dfsDownstream.get(0);
        assertTrue(one instanceof MaterializedView);
        assertEquals(one.getGuid(), mview.getGuid());
        one = dfsDownstream.get(1);
        assertTrue(one instanceof View);
        assertEquals(one.getGuid(), view.getGuid());
        List<String> dfsUpstreamGuids = response.getAllUpstreamEntityGuidsDFS();
        assertEquals(dfsUpstreamGuids.size(), 2);
        assertEquals(dfsUpstreamGuids.get(0), mview.getGuid());
        assertEquals(dfsUpstreamGuids.get(1), table.getGuid());
        List<Entity> dfsUpstream = response.getAllUpstreamEntitiesDFS();
        assertEquals(dfsUpstream.size(), 2);
        one = dfsUpstream.get(0);
        assertTrue(one instanceof MaterializedView);
        assertEquals(one.getGuid(), mview.getGuid());
        one = dfsUpstream.get(1);
        assertTrue(one instanceof Table);
        assertEquals(one.getGuid(), table.getGuid());
    }

    @Test(
            groups = {"read.lineage.end"},
            dependsOnGroups = {"create.lineage.*"})
    void fetchLineageEnd() throws AtlanException {
        LineageRequest lineage =
                LineageRequest.builder().guid(view.getGuid()).hideProcess(true).build();
        LineageResponse response = lineage.fetch();
        assertNotNull(response);
        assertEquals(response.getBaseEntityGuid(), view.getGuid());
        Set<String> upstreamGuids = response.getUpstreamEntityGuids();
        assertNotNull(upstreamGuids);
        assertEquals(upstreamGuids.size(), 1);
        assertEquals(response.getUpstreamEntities().size(), 1);
        assertTrue(upstreamGuids.contains(mview.getGuid()));
        upstreamGuids = response.getUpstreamProcessGuids();
        assertNotNull(upstreamGuids);
        assertEquals(upstreamGuids.size(), 1);
        assertTrue(upstreamGuids.contains(end.getGuid()));
        assertTrue(response.getDownstreamEntityGuids().isEmpty());
        List<String> dfsDownstreamGuids = response.getAllDownstreamEntityGuidsDFS();
        assertEquals(dfsDownstreamGuids.size(), 1);
        assertEquals(dfsDownstreamGuids.get(0), view.getGuid());
        List<Entity> dfsDownstream = response.getAllDownstreamEntitiesDFS();
        assertEquals(dfsDownstream.size(), 1);
        Entity one = dfsDownstream.get(0);
        assertTrue(one instanceof View);
        assertEquals(one.getGuid(), view.getGuid());
        List<String> dfsUpstreamGuids = response.getAllUpstreamEntityGuidsDFS();
        assertEquals(dfsUpstreamGuids.size(), 3);
        assertEquals(dfsUpstreamGuids.get(0), view.getGuid());
        assertEquals(dfsUpstreamGuids.get(1), mview.getGuid());
        assertEquals(dfsUpstreamGuids.get(2), table.getGuid());
        List<Entity> dfsUpstream = response.getAllUpstreamEntitiesDFS();
        assertEquals(dfsUpstream.size(), 3);
        one = dfsUpstream.get(0);
        assertTrue(one instanceof View);
        assertEquals(one.getGuid(), view.getGuid());
        one = dfsUpstream.get(1);
        assertTrue(one instanceof MaterializedView);
        assertEquals(one.getGuid(), mview.getGuid());
        one = dfsUpstream.get(2);
        assertTrue(one instanceof Table);
        assertEquals(one.getGuid(), table.getGuid());
    }

    @Test(
            groups = {"read.lineage.invalid"},
            dependsOnGroups = {"create.lineage.*"})
    void fetchLineageInvalid() throws AtlanException {
        LineageRequest lineage = LineageRequest.builder()
                .guid(table.getGuid())
                .hideProcess(false)
                .build();
        LineageResponse response = lineage.fetch();
        assertNotNull(response);
        assertEquals(response.getBaseEntityGuid(), table.getGuid());
        assertThrows(InvalidRequestException.class, response::getDownstreamEntityGuids);
    }

    @Test(
            groups = {"search.lineage"},
            dependsOnGroups = {"read.lineage.*"})
    void searchByLineage() throws AtlanException {

        Query byLineage = QueryFactory.withLineage();
        Query byState = QueryFactory.active();
        Query byType = QueryFactory.withSuperType(SQL.TYPE_NAME);
        Query byQN = QueryFactory.whereQualifiedNameStartsWith(connection.getQualifiedName());
        Query combined =
                BoolQuery.of(b -> b.filter(byState, byType, byLineage, byQN))._toQuery();

        IndexSearchRequest index = IndexSearchRequest.builder()
                .dsl(IndexSearchDSL.builder().query(combined).build())
                .attribute("name")
                .attribute("__hasLineage")
                .build();

        IndexSearchResponse response = index.search();

        assertNotNull(response);
        assertEquals(response.getApproximateCount().longValue(), 3L);
        List<Entity> entities = response.getEntities();
        assertNotNull(entities);
        assertEquals(entities.size(), 3);
        Set<String> types = entities.stream().map(Entity::getTypeName).collect(Collectors.toSet());
        assertEquals(types.size(), 3);
        assertTrue(types.contains(Table.TYPE_NAME));
        assertTrue(types.contains(MaterializedView.TYPE_NAME));
        assertTrue(types.contains(View.TYPE_NAME));
        Set<String> guids = entities.stream().map(Entity::getGuid).collect(Collectors.toSet());
        assertEquals(guids.size(), 3);
        assertTrue(guids.contains(table.getGuid()));
        assertTrue(guids.contains(mview.getGuid()));
        assertTrue(guids.contains(view.getGuid()));
        for (Entity one : entities) {
            assertTrue(((SQL) one).getHasLineage());
        }
    }

    @Test(
            groups = {"delete.lineage"},
            dependsOnGroups = {"search.lineage"})
    void deleteLineage() throws AtlanException {
        EntityMutationResponse response = Entity.delete(start.getGuid());
        assertNotNull(response);
        assertEquals(response.getDeletedEntities().size(), 1);
        Entity one = response.getDeletedEntities().get(0);
        assertNotNull(one);
        assertTrue(one instanceof LineageProcess);
        LineageProcess process = (LineageProcess) one;
        assertEquals(process.getGuid(), start.getGuid());
        assertEquals(process.getQualifiedName(), start.getQualifiedName());
        assertEquals(process.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"delete.lineage.restore"},
            dependsOnGroups = {"delete.lineage"})
    void restoreLineage() throws AtlanException {
        assertTrue(LineageProcess.restore(start.getQualifiedName()));
        LineageProcess restored = LineageProcess.retrieveByGuid(start.getGuid());
        assertEquals(restored.getGuid(), start.getGuid());
        assertEquals(restored.getQualifiedName(), start.getQualifiedName());
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"purge.lineage"},
            dependsOnGroups = {"delete.lineage.restore"},
            alwaysRun = true)
    void purgeLineage() throws AtlanException {
        EntityMutationResponse response = LineageProcess.purge(start.getGuid());
        assertNotNull(response);
        assertEquals(response.getDeletedEntities().size(), 1);
        Entity one = response.getDeletedEntities().get(0);
        assertNotNull(one);
        assertTrue(one instanceof LineageProcess);
        LineageProcess process = (LineageProcess) one;
        assertEquals(process.getGuid(), start.getGuid());
        assertEquals(process.getQualifiedName(), start.getQualifiedName());
        assertEquals(process.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"purge.connection"},
            dependsOnGroups = {"create.*", "read.*", "search.*", "purge.lineage"},
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(connection.getQualifiedName(), log);
    }
}
