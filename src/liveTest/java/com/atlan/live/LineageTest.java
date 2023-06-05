/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static com.atlan.util.QueryFactory.*;
import static org.testng.Assert.*;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.*;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanLineageDirection;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.enums.KeywordFields;
import com.atlan.model.lineage.LineageListRequest;
import com.atlan.model.lineage.LineageListResponse;
import com.atlan.model.lineage.LineageRequest;
import com.atlan.model.lineage.LineageResponse;
import com.atlan.model.search.IndexSearchDSL;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.net.HttpClient;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * Tests all aspects of lineage.
 */
@Slf4j
public class LineageTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("Lineage");

    public static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.VERTICA;
    public static final String CONNECTION_NAME = PREFIX;
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

    private static LineageProcess start = null;
    private static LineageProcess end = null;

    @Test(groups = {"lineage.create.connection"})
    void createConnection() throws AtlanException {
        connection = ConnectionTest.createConnection(CONNECTION_NAME, CONNECTOR_TYPE);
        Database database = SQLAssetTest.createDatabase(DATABASE_NAME, connection.getQualifiedName());
        Schema schema = SQLAssetTest.createSchema(SCHEMA_NAME, database.getQualifiedName());
        table = SQLAssetTest.createTable(TABLE_NAME, schema.getQualifiedName());
        mview = SQLAssetTest.createMaterializedView(MVIEW_NAME, schema.getQualifiedName());
        view = SQLAssetTest.createView(VIEW_NAME, schema.getQualifiedName());
        SQLAssetTest.createColumn(COLUMN_NAME1, Table.TYPE_NAME, table.getQualifiedName(), 1);
        SQLAssetTest.createColumn(COLUMN_NAME2, Table.TYPE_NAME, table.getQualifiedName(), 2);
        SQLAssetTest.createColumn(COLUMN_NAME3, MaterializedView.TYPE_NAME, mview.getQualifiedName(), 1);
        SQLAssetTest.createColumn(COLUMN_NAME4, MaterializedView.TYPE_NAME, mview.getQualifiedName(), 2);
        SQLAssetTest.createColumn(COLUMN_NAME5, View.TYPE_NAME, view.getQualifiedName(), 1);
        SQLAssetTest.createColumn(COLUMN_NAME6, View.TYPE_NAME, view.getQualifiedName(), 2);
    }

    // TODO: column-level lineage
    // TODO: Atlan tag propagation through lineage
    // TODO: Atlan tag propagation through parent-child
    // TODO: Atlan tag propagation through term-asset
    // TODO: Atlan tag propagation through term-asset-child-lineage

    @Test(
            groups = {"lineage.create.lineage.start"},
            dependsOnGroups = {"lineage.create.connection"})
    void createLineageStart() throws AtlanException {
        final String processName = TABLE_NAME + " >> " + MVIEW_NAME;
        LineageProcess toCreate = LineageProcess.creator(
                        processName,
                        connection.getQualifiedName(),
                        null,
                        List.of(Table.refByGuid(table.getGuid())),
                        List.of(MaterializedView.refByGuid(mview.getGuid())),
                        null)
                .build();
        AssetMutationResponse response = toCreate.upsert();
        assertNotNull(response);
        assertEquals(response.getCreatedAssets().size(), 1);
        Asset one = response.getCreatedAssets().get(0);
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
        assertEquals(response.getUpdatedAssets().size(), 2);
        Set<String> types =
                response.getUpdatedAssets().stream().map(Asset::getTypeName).collect(Collectors.toSet());
        assertEquals(types.size(), 2);
        assertTrue(types.contains(Table.TYPE_NAME));
        assertTrue(types.contains(MaterializedView.TYPE_NAME));
        Set<String> guids =
                response.getUpdatedAssets().stream().map(Asset::getGuid).collect(Collectors.toSet());
        assertEquals(guids.size(), 2);
        assertTrue(guids.contains(table.getGuid()));
        assertTrue(guids.contains(mview.getGuid()));
    }

    @Test(
            groups = {"lineage.create.lineage.end"},
            dependsOnGroups = {"lineage.create.lineage.start"})
    void createLineageEnd() throws AtlanException {
        final String processName = MVIEW_NAME + " >> " + VIEW_NAME;
        LineageProcess toCreate = LineageProcess.creator(
                        processName,
                        connection.getQualifiedName(),
                        null,
                        List.of(MaterializedView.refByGuid(mview.getGuid())),
                        List.of(View.refByGuid(view.getGuid())),
                        null)
                .build();
        AssetMutationResponse response = toCreate.upsert();
        assertNotNull(response);
        assertEquals(response.getCreatedAssets().size(), 1);
        Asset one = response.getCreatedAssets().get(0);
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
        assertEquals(response.getUpdatedAssets().size(), 2);
        Set<String> types =
                response.getUpdatedAssets().stream().map(Asset::getTypeName).collect(Collectors.toSet());
        assertEquals(types.size(), 2);
        assertTrue(types.contains(MaterializedView.TYPE_NAME));
        assertTrue(types.contains(View.TYPE_NAME));
        Set<String> guids =
                response.getUpdatedAssets().stream().map(Asset::getGuid).collect(Collectors.toSet());
        assertEquals(guids.size(), 2);
        assertTrue(guids.contains(mview.getGuid()));
        assertTrue(guids.contains(view.getGuid()));
    }

    @Test(
            groups = {"lineage.read.lineage"},
            dependsOnGroups = {"lineage.create.lineage.*"})
    void fetchLineageStart() throws AtlanException {
        LineageRequest lineage =
                LineageRequest.builder().guid(table.getGuid()).hideProcess(true).build();
        LineageResponse response = lineage.fetch();
        assertNotNull(response);
        assertEquals(response.getBaseEntityGuid(), table.getGuid());
        assertTrue(response.getUpstreamAssetGuids().isEmpty());
        Set<String> downstreamGuids = response.getDownstreamAssetGuids();
        assertNotNull(downstreamGuids);
        assertEquals(downstreamGuids.size(), 1);
        assertTrue(downstreamGuids.contains(mview.getGuid()));
        assertEquals(response.getDownstreamAssets().size(), 1);
        downstreamGuids = response.getDownstreamProcessGuids();
        assertNotNull(downstreamGuids);
        assertEquals(downstreamGuids.size(), 1);
        assertTrue(downstreamGuids.contains(start.getGuid()));
        List<String> dfsDownstreamGuids = response.getAllDownstreamAssetGuidsDFS();
        assertEquals(dfsDownstreamGuids.size(), 3);
        assertEquals(dfsDownstreamGuids.get(0), table.getGuid());
        assertEquals(dfsDownstreamGuids.get(1), mview.getGuid());
        assertEquals(dfsDownstreamGuids.get(2), view.getGuid());
        List<Asset> dfsDownstream = response.getAllDownstreamAssetsDFS();
        assertEquals(dfsDownstream.size(), 3);
        Asset one = dfsDownstream.get(0);
        assertTrue(one instanceof Table);
        assertEquals(one.getGuid(), table.getGuid());
        one = dfsDownstream.get(1);
        assertEquals(one.getGuid(), mview.getGuid());
        one = dfsDownstream.get(2);
        assertEquals(one.getGuid(), view.getGuid());
        List<String> dfsUpstreamGuids = response.getAllUpstreamAssetGuidsDFS();
        assertEquals(dfsUpstreamGuids.size(), 1);
        assertEquals(dfsUpstreamGuids.get(0), table.getGuid());
    }

    @Test(
            groups = {"lineage.read.lineage"},
            dependsOnGroups = {"lineage.create.lineage.*"})
    void fetchLineageListStart() throws AtlanException {
        LineageListRequest lineage = LineageListRequest.builder()
                .guid(table.getGuid())
                .attribute("name")
                .build();
        LineageListResponse response = lineage.fetch();
        assertNotNull(response);
        assertNotNull(response.getEntities());
        assertEquals(response.getEntities().size(), 4);
        assertEquals(response.getEntityCount(), 4);
        Asset one = response.getEntities().get(0);
        assertTrue(one instanceof LineageProcess);
        one = response.getEntities().get(1);
        assertTrue(one instanceof MaterializedView);
        assertEquals(one.getGuid(), mview.getGuid());
        one = response.getEntities().get(2);
        assertTrue(one instanceof LineageProcess);
        one = response.getEntities().get(3);
        assertTrue(one instanceof View);
        assertEquals(one.getGuid(), view.getGuid());
        lineage = LineageListRequest.builder()
                .guid(table.getGuid())
                .direction(AtlanLineageDirection.UPSTREAM)
                .build();
        response = lineage.fetch();
        assertNotNull(response);
        assertNotNull(response.getEntities());
        assertTrue(response.getEntities().isEmpty());
        assertFalse(response.getHasMore());
    }

    @Test(
            groups = {"lineage.read.lineage"},
            dependsOnGroups = {"lineage.create.lineage.*"})
    void fetchLineageMiddle() throws AtlanException {
        LineageRequest lineage =
                LineageRequest.builder().guid(mview.getGuid()).hideProcess(true).build();
        LineageResponse response = lineage.fetch();
        assertNotNull(response);
        assertEquals(response.getBaseEntityGuid(), mview.getGuid());
        Set<String> upstreamGuids = response.getUpstreamAssetGuids();
        assertNotNull(upstreamGuids);
        assertEquals(upstreamGuids.size(), 1);
        assertTrue(upstreamGuids.contains(table.getGuid()));
        assertEquals(response.getUpstreamAssets().size(), 1);
        Asset one = response.getUpstreamAssets().get(0);
        assertNotNull(one);
        assertTrue(one instanceof Table);
        Table t = (Table) one;
        assertEquals(t.getQualifiedName(), table.getQualifiedName());
        upstreamGuids = response.getUpstreamProcessGuids();
        assertNotNull(upstreamGuids);
        assertEquals(upstreamGuids.size(), 1);
        assertTrue(upstreamGuids.contains(start.getGuid()));
        Set<String> downstreamGuids = response.getDownstreamAssetGuids();
        assertNotNull(downstreamGuids);
        assertEquals(downstreamGuids.size(), 1);
        assertTrue(downstreamGuids.contains(view.getGuid()));
        assertEquals(response.getDownstreamAssets().size(), 1);
        one = response.getDownstreamAssets().get(0);
        assertNotNull(one);
        assertTrue(one instanceof View);
        View v = (View) one;
        assertEquals(v.getQualifiedName(), view.getQualifiedName());
        downstreamGuids = response.getDownstreamProcessGuids();
        assertNotNull(downstreamGuids);
        assertEquals(downstreamGuids.size(), 1);
        assertTrue(downstreamGuids.contains(end.getGuid()));
        List<String> dfsDownstreamGuids = response.getAllDownstreamAssetGuidsDFS();
        assertEquals(dfsDownstreamGuids.size(), 2);
        assertEquals(dfsDownstreamGuids.get(0), mview.getGuid());
        assertEquals(dfsDownstreamGuids.get(1), view.getGuid());
        List<Asset> dfsDownstream = response.getAllDownstreamAssetsDFS();
        assertEquals(dfsDownstream.size(), 2);
        one = dfsDownstream.get(0);
        assertTrue(one instanceof MaterializedView);
        assertEquals(one.getGuid(), mview.getGuid());
        one = dfsDownstream.get(1);
        assertTrue(one instanceof View);
        assertEquals(one.getGuid(), view.getGuid());
        List<String> dfsUpstreamGuids = response.getAllUpstreamAssetGuidsDFS();
        assertEquals(dfsUpstreamGuids.size(), 2);
        assertEquals(dfsUpstreamGuids.get(0), mview.getGuid());
        assertEquals(dfsUpstreamGuids.get(1), table.getGuid());
        List<Asset> dfsUpstream = response.getAllUpstreamAssetsDFS();
        assertEquals(dfsUpstream.size(), 2);
        one = dfsUpstream.get(0);
        assertTrue(one instanceof MaterializedView);
        assertEquals(one.getGuid(), mview.getGuid());
        one = dfsUpstream.get(1);
        assertTrue(one instanceof Table);
        assertEquals(one.getGuid(), table.getGuid());
    }

    @Test(
            groups = {"lineage.read.lineage"},
            dependsOnGroups = {"lineage.create.lineage.*"})
    void fetchLineageListMiddle() throws AtlanException {
        LineageListRequest lineage = LineageListRequest.builder()
                .guid(mview.getGuid())
                .attribute("name")
                .build();
        LineageListResponse response = lineage.fetch();
        assertNotNull(response);
        assertNotNull(response.getEntities());
        assertEquals(response.getEntities().size(), 2);
        assertEquals(response.getEntityCount(), 2);
        Asset one = response.getEntities().get(0);
        assertTrue(one instanceof LineageProcess);
        one = response.getEntities().get(1);
        assertTrue(one instanceof View);
        assertEquals(one.getGuid(), view.getGuid());
        lineage = LineageListRequest.builder()
                .guid(mview.getGuid())
                .direction(AtlanLineageDirection.UPSTREAM)
                .build();
        response = lineage.fetch();
        assertNotNull(response);
        assertNotNull(response.getEntities());
        assertEquals(response.getEntities().size(), 2);
        assertEquals(response.getEntityCount(), 2);
        one = response.getEntities().get(0);
        assertTrue(one instanceof LineageProcess);
        one = response.getEntities().get(1);
        assertTrue(one instanceof Table);
        assertEquals(one.getGuid(), table.getGuid());
    }

    @Test(
            groups = {"lineage.read.lineage.end"},
            dependsOnGroups = {"lineage.create.lineage.*"})
    void fetchLineageEnd() throws AtlanException {
        LineageRequest lineage =
                LineageRequest.builder().guid(view.getGuid()).hideProcess(true).build();
        LineageResponse response = lineage.fetch();
        assertNotNull(response);
        assertEquals(response.getBaseEntityGuid(), view.getGuid());
        Set<String> upstreamGuids = response.getUpstreamAssetGuids();
        assertNotNull(upstreamGuids);
        assertEquals(upstreamGuids.size(), 1);
        assertEquals(response.getUpstreamAssets().size(), 1);
        assertTrue(upstreamGuids.contains(mview.getGuid()));
        upstreamGuids = response.getUpstreamProcessGuids();
        assertNotNull(upstreamGuids);
        assertEquals(upstreamGuids.size(), 1);
        assertTrue(upstreamGuids.contains(end.getGuid()));
        assertTrue(response.getDownstreamAssetGuids().isEmpty());
        List<String> dfsDownstreamGuids = response.getAllDownstreamAssetGuidsDFS();
        assertEquals(dfsDownstreamGuids.size(), 1);
        assertEquals(dfsDownstreamGuids.get(0), view.getGuid());
        List<Asset> dfsDownstream = response.getAllDownstreamAssetsDFS();
        assertEquals(dfsDownstream.size(), 1);
        Asset one = dfsDownstream.get(0);
        assertTrue(one instanceof View);
        assertEquals(one.getGuid(), view.getGuid());
        List<String> dfsUpstreamGuids = response.getAllUpstreamAssetGuidsDFS();
        assertEquals(dfsUpstreamGuids.size(), 3);
        assertEquals(dfsUpstreamGuids.get(0), view.getGuid());
        assertEquals(dfsUpstreamGuids.get(1), mview.getGuid());
        assertEquals(dfsUpstreamGuids.get(2), table.getGuid());
        List<Asset> dfsUpstream = response.getAllUpstreamAssetsDFS();
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
            groups = {"lineage.read.lineage.end"},
            dependsOnGroups = {"lineage.create.lineage.*"})
    void fetchLineageListEnd() throws AtlanException {
        LineageListRequest lineage = LineageListRequest.builder()
                .guid(view.getGuid())
                .direction(AtlanLineageDirection.DOWNSTREAM)
                .attribute("name")
                .build();
        LineageListResponse response = lineage.fetch();
        assertNotNull(response);
        assertNotNull(response.getEntities());
        assertTrue(response.getEntities().isEmpty());
        assertFalse(response.getHasMore());
        lineage = LineageListRequest.builder()
                .guid(view.getGuid())
                .direction(AtlanLineageDirection.UPSTREAM)
                .build();
        response = lineage.fetch();
        assertNotNull(response);
        assertNotNull(response.getEntities());
        assertEquals(response.getEntities().size(), 4);
        assertEquals(response.getEntityCount(), 4);
        Asset one = response.getEntities().get(0);
        assertTrue(one instanceof LineageProcess);
        one = response.getEntities().get(1);
        assertTrue(one instanceof MaterializedView);
        assertEquals(one.getGuid(), mview.getGuid());
        one = response.getEntities().get(2);
        assertTrue(one instanceof LineageProcess);
        one = response.getEntities().get(3);
        assertTrue(one instanceof Table);
        assertEquals(one.getGuid(), table.getGuid());
    }

    @Test(
            groups = {"lineage.read.lineage.invalid"},
            dependsOnGroups = {"lineage.create.lineage.*"})
    void fetchLineageInvalid() throws AtlanException {
        LineageRequest lineage = LineageRequest.builder()
                .guid(table.getGuid())
                .hideProcess(false)
                .build();
        LineageResponse response = lineage.fetch();
        assertNotNull(response);
        assertEquals(response.getBaseEntityGuid(), table.getGuid());
        assertThrows(InvalidRequestException.class, response::getDownstreamAssetGuids);
    }

    @Test(
            groups = {"lineage.search.lineage"},
            dependsOnGroups = {"lineage.read.lineage.*"})
    void searchByLineage() throws AtlanException, InterruptedException {
        Query combined = CompoundQuery.builder()
                .must(beActive())
                .must(haveLineage())
                .must(haveSuperType(SQL.TYPE_NAME))
                .must(have(KeywordFields.QUALIFIED_NAME).startingWith(connection.getQualifiedName()))
                .build()
                ._toQuery();

        IndexSearchRequest index = IndexSearchRequest.builder()
                .dsl(IndexSearchDSL.builder().query(combined).build())
                .attribute("name")
                .attribute("__hasLineage")
                .build();

        IndexSearchResponse response = index.search();

        int count = 0;
        while (response.getApproximateCount() < 3L && count < Atlan.getMaxNetworkRetries()) {
            Thread.sleep(HttpClient.waitTime(count).toMillis());
            response = index.search();
            count++;
        }

        assertNotNull(response);
        assertEquals(response.getApproximateCount().longValue(), 3L);
        List<Asset> assets = response.getAssets();
        assertNotNull(assets);
        assertEquals(assets.size(), 3);
        Set<String> types = assets.stream().map(Asset::getTypeName).collect(Collectors.toSet());
        assertEquals(types.size(), 3);
        assertTrue(types.contains(Table.TYPE_NAME));
        assertTrue(types.contains(MaterializedView.TYPE_NAME));
        assertTrue(types.contains(View.TYPE_NAME));
        Set<String> guids = assets.stream().map(Asset::getGuid).collect(Collectors.toSet());
        assertEquals(guids.size(), 3);
        assertTrue(guids.contains(table.getGuid()));
        assertTrue(guids.contains(mview.getGuid()));
        assertTrue(guids.contains(view.getGuid()));
        for (Asset one : assets) {
            assertTrue(one.getHasLineage());
        }
    }

    @Test(
            groups = {"lineage.delete.lineage"},
            dependsOnGroups = {"lineage.search.lineage"})
    void deleteLineage() throws AtlanException {
        AssetMutationResponse response = Asset.delete(start.getGuid());
        assertNotNull(response);
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertNotNull(one);
        assertTrue(one instanceof LineageProcess);
        LineageProcess process = (LineageProcess) one;
        assertEquals(process.getGuid(), start.getGuid());
        assertEquals(process.getQualifiedName(), start.getQualifiedName());
        assertEquals(process.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"lineage.delete.lineage.restore"},
            dependsOnGroups = {"lineage.delete.lineage"})
    void restoreLineage() throws AtlanException {
        assertTrue(LineageProcess.restore(start.getQualifiedName()));
        LineageProcess restored = LineageProcess.retrieveByGuid(start.getGuid());
        assertEquals(restored.getGuid(), start.getGuid());
        assertEquals(restored.getQualifiedName(), start.getQualifiedName());
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"lineage.purge.lineage"},
            dependsOnGroups = {"lineage.delete.lineage.restore"},
            alwaysRun = true)
    void purgeLineage() throws AtlanException {
        AssetMutationResponse response = LineageProcess.purge(start.getGuid());
        assertNotNull(response);
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertNotNull(one);
        assertTrue(one instanceof LineageProcess);
        LineageProcess process = (LineageProcess) one;
        assertEquals(process.getGuid(), start.getGuid());
        assertEquals(process.getQualifiedName(), start.getQualifiedName());
        assertEquals(process.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"lineage.purge.connection"},
            dependsOnGroups = {"lineage.create.*", "lineage.read.*", "lineage.search.*", "lineage.purge.lineage"},
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(connection.getQualifiedName(), log);
    }
}
