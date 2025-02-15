/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.model.assets.*;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanLineageDirection;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.lineage.*;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
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
    void createConnection() throws AtlanException, InterruptedException {
        connection = ConnectionTest.createConnection(client, CONNECTION_NAME, CONNECTOR_TYPE);
        Database database = SQLAssetTest.createDatabase(client, DATABASE_NAME, connection.getQualifiedName());
        Schema schema = SQLAssetTest.createSchema(client, SCHEMA_NAME, database);
        table = SQLAssetTest.createTable(client, TABLE_NAME, schema);
        mview = SQLAssetTest.createMaterializedView(client, MVIEW_NAME, schema);
        view = SQLAssetTest.createView(client, VIEW_NAME, schema);
        SQLAssetTest.createColumn(client, COLUMN_NAME1, table, 1);
        SQLAssetTest.createColumn(client, COLUMN_NAME2, table, 2);
        SQLAssetTest.createColumn(client, COLUMN_NAME3, mview, 1);
        SQLAssetTest.createColumn(client, COLUMN_NAME4, mview, 2);
        SQLAssetTest.createColumn(client, COLUMN_NAME5, view, 1);
        SQLAssetTest.createColumn(client, COLUMN_NAME6, view, 2);
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
        AssetMutationResponse response = toCreate.save(client);
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
        for (ICatalog input : start.getInputs()) {
            assertNotNull(input);
            assertEquals(input.getTypeName(), Table.TYPE_NAME);
            assertEquals(input.getGuid(), table.getGuid());
        }
        assertNotNull(start.getOutputs());
        assertEquals(start.getOutputs().size(), 1);
        for (ICatalog output : start.getOutputs()) {
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
        AssetMutationResponse response = toCreate.save(client);
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
        for (ICatalog input : end.getInputs()) {
            assertNotNull(input);
            assertEquals(input.getTypeName(), MaterializedView.TYPE_NAME);
            assertEquals(input.getGuid(), mview.getGuid());
        }
        assertNotNull(end.getOutputs());
        assertEquals(end.getOutputs().size(), 1);
        for (ICatalog output : end.getOutputs()) {
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
    void fetchLineageListStart() throws AtlanException {
        LineageListResponse response =
                FluentLineage.builder(client, table).toRequest().fetch(client);
        assertNotNull(response);
        assertNotNull(response.getAssets());
        assertEquals(response.getAssets().size(), 4);
        assertEquals(response.getEntityCount(), 4);
        Asset one = response.getAssets().get(0);
        assertTrue(one instanceof LineageProcess);
        assertEquals(one.getDepth(), 1);
        one = response.getAssets().get(1);
        assertTrue(one instanceof MaterializedView);
        assertEquals(one.getGuid(), mview.getGuid());
        assertEquals(one.getDepth(), 1);
        one = response.getAssets().get(2);
        assertTrue(one instanceof LineageProcess);
        assertEquals(one.getDepth(), 2);
        one = response.getAssets().get(3);
        assertTrue(one instanceof View);
        assertEquals(one.getDepth(), 2);
        assertEquals(one.getGuid(), view.getGuid());
        response = FluentLineage.builder(client, table)
                .direction(AtlanLineageDirection.UPSTREAM)
                .toRequest()
                .fetch(client);
        assertNotNull(response);
        assertNotNull(response.getAssets());
        assertTrue(response.getAssets().isEmpty());
        assertFalse(response.getHasMore());
    }

    @Test(
            groups = {"lineage.read.lineage"},
            dependsOnGroups = {"lineage.create.lineage.*"})
    void fetchLineageListStartDetailed() throws AtlanException {
        LineageListResponse response = FluentLineage.builder(client, table)
                .toRequestBuilder()
                .immediateNeighbors(true)
                .build()
                .fetch(client);
        assertNotNull(response);
        assertNotNull(response.getAssets());
        assertEquals(response.getAssets().size(), 5);
        assertEquals(response.getEntityCount(), 4);
        Asset one = response.getAssets().get(0);
        assertTrue(one instanceof Table);
        assertEquals(one.getGuid(), table.getGuid());
        assertEquals(one.getDepth(), 0);
        assertNull(one.getImmediateUpstream());
        assertNotNull(one.getImmediateDownstream());
        assertEquals(one.getImmediateDownstream().size(), 1);
        assertEquals(one.getImmediateDownstream().get(0).getGuid(), mview.getGuid());
        one = response.getAssets().get(1);
        assertTrue(one instanceof LineageProcess);
        assertEquals(one.getGuid(), start.getGuid());
        assertEquals(one.getDepth(), 1);
        assertNotNull(one.getImmediateUpstream());
        assertTrue(one.getImmediateUpstream().isEmpty());
        assertNotNull(one.getImmediateDownstream());
        assertEquals(one.getImmediateDownstream().size(), 1);
        assertEquals(one.getImmediateDownstream().get(0).getGuid(), end.getGuid());
        one = response.getAssets().get(2);
        assertTrue(one instanceof MaterializedView);
        assertEquals(one.getGuid(), mview.getGuid());
        assertEquals(one.getDepth(), 1);
        assertNotNull(one.getImmediateUpstream());
        assertEquals(one.getImmediateUpstream().size(), 1);
        assertEquals(one.getImmediateUpstream().get(0).getGuid(), table.getGuid());
        assertNotNull(one.getImmediateDownstream());
        assertEquals(one.getImmediateDownstream().size(), 1);
        assertEquals(one.getImmediateDownstream().get(0).getGuid(), view.getGuid());
        one = response.getAssets().get(3);
        assertTrue(one instanceof LineageProcess);
        assertEquals(one.getGuid(), end.getGuid());
        assertEquals(one.getDepth(), 2);
        assertNotNull(one.getImmediateUpstream());
        assertEquals(one.getImmediateUpstream().size(), 1);
        assertEquals(one.getImmediateUpstream().get(0).getGuid(), start.getGuid());
        assertNotNull(one.getImmediateDownstream());
        assertTrue(one.getImmediateDownstream().isEmpty());
        one = response.getAssets().get(4);
        assertTrue(one instanceof View);
        assertEquals(one.getDepth(), 2);
        assertEquals(one.getGuid(), view.getGuid());
        assertNotNull(one.getImmediateUpstream());
        assertEquals(one.getImmediateUpstream().size(), 1);
        assertEquals(one.getImmediateUpstream().get(0).getGuid(), mview.getGuid());
        assertNull(one.getImmediateDownstream());
        response = FluentLineage.builder(client, table)
                .direction(AtlanLineageDirection.UPSTREAM)
                .toRequestBuilder()
                .immediateNeighbors(true)
                .build()
                .fetch(client);
        assertNotNull(response);
        assertNotNull(response.getAssets());
        assertEquals(response.getAssets().size(), 1);
        one = response.getAssets().get(0);
        assertTrue(one instanceof Table);
        assertEquals(one.getGuid(), table.getGuid());
        assertEquals(one.getDepth(), 0);
        assertNull(one.getImmediateUpstream());
        assertNull(one.getImmediateDownstream());
        assertFalse(response.getHasMore());
    }

    @Test(
            groups = {"lineage.read.lineage"},
            dependsOnGroups = {"lineage.create.lineage.*"})
    void testLineageListIterators() throws AtlanException {
        LineageListRequest lineage =
                table.requestLineage(client).includeOnResults(Asset.NAME).toRequest();
        LineageListResponse response = lineage.fetch(client);
        int count = 0;
        for (Asset a : response) {
            assertTrue(a instanceof LineageProcess || a instanceof MaterializedView || a instanceof View);
            count++;
        }
        assertEquals(count, 4);
        response.forEach(a -> {
            assertTrue(a instanceof LineageProcess || a instanceof MaterializedView || a instanceof View);
        });
        List<Asset> results = response.stream().toList();
        assertEquals(results.size(), 4);
        Asset one = results.get(0);
        assertTrue(one instanceof LineageProcess);
        assertEquals(one.getDepth(), 1);
        one = results.get(1);
        assertTrue(one instanceof MaterializedView);
        assertEquals(one.getGuid(), mview.getGuid());
        assertEquals(one.getDepth(), 1);
        one = results.get(2);
        assertTrue(one instanceof LineageProcess);
        one = results.get(3);
        assertEquals(one.getDepth(), 2);
        assertTrue(one instanceof View);
        assertEquals(one.getGuid(), view.getGuid());
        assertEquals(one.getDepth(), 2);
    }

    @Test(
            groups = {"lineage.read.lineage"},
            dependsOnGroups = {"lineage.create.lineage.*"})
    void fetchLineageListMiddle() throws AtlanException {
        LineageListRequest lineage =
                mview.requestLineage(client).includeOnResults(Asset.NAME).toRequest();
        LineageListResponse response = lineage.fetch(client);
        assertNotNull(response);
        assertNotNull(response.getAssets());
        assertEquals(response.getAssets().size(), 2);
        assertEquals(response.getEntityCount(), 2);
        Asset one = response.getAssets().get(0);
        assertTrue(one instanceof LineageProcess);
        one = response.getAssets().get(1);
        assertTrue(one instanceof View);
        assertEquals(one.getGuid(), view.getGuid());
        lineage = FluentLineage.builder(client, mview)
                .direction(AtlanLineageDirection.UPSTREAM)
                .toRequest();
        response = lineage.fetch(client);
        assertNotNull(response);
        assertNotNull(response.getAssets());
        assertEquals(response.getAssets().size(), 2);
        assertEquals(response.getEntityCount(), 2);
        one = response.getAssets().get(0);
        assertTrue(one instanceof LineageProcess);
        assertEquals(one.getDepth(), 1);
        one = response.getAssets().get(1);
        assertTrue(one instanceof Table);
        assertEquals(one.getGuid(), table.getGuid());
        assertEquals(one.getDepth(), 1);
    }

    @Test(
            groups = {"lineage.read.lineage.end"},
            dependsOnGroups = {"lineage.create.lineage.*"})
    void fetchLineageListEnd() throws AtlanException {
        LineageListRequest lineage = view.requestLineage(client)
                .direction(AtlanLineageDirection.DOWNSTREAM)
                .includeOnResults(Asset.NAME)
                .toRequest();
        LineageListResponse response = lineage.fetch(client);
        assertNotNull(response);
        assertNotNull(response.getAssets());
        assertTrue(response.getAssets().isEmpty());
        assertFalse(response.getHasMore());
        lineage = FluentLineage.builder(client, view)
                .direction(AtlanLineageDirection.UPSTREAM)
                .toRequest();
        response = lineage.fetch(client);
        assertNotNull(response);
        assertNotNull(response.getAssets());
        assertEquals(response.getAssets().size(), 4);
        assertEquals(response.getEntityCount(), 4);
        Asset one = response.getAssets().get(0);
        assertTrue(one instanceof LineageProcess);
        one = response.getAssets().get(1);
        assertEquals(one.getDepth(), 1);
        assertTrue(one instanceof MaterializedView);
        assertEquals(one.getGuid(), mview.getGuid());
        assertEquals(one.getDepth(), 1);
        one = response.getAssets().get(2);
        assertTrue(one instanceof LineageProcess);
        assertEquals(one.getDepth(), 2);
        one = response.getAssets().get(3);
        assertTrue(one instanceof Table);
        assertEquals(one.getGuid(), table.getGuid());
        assertEquals(one.getDepth(), 2);
    }

    @Test(
            groups = {"lineage.search.lineage"},
            dependsOnGroups = {"lineage.read.lineage.*"})
    void searchByLineage() throws AtlanException, InterruptedException {
        IndexSearchRequest index = client.assets
                .select()
                .withLineage()
                .where(Asset.SUPER_TYPE_NAMES.eq(ISQL.TYPE_NAME))
                .where(Asset.QUALIFIED_NAME.startsWith(connection.getQualifiedName()))
                .includeOnResults(Asset.NAME)
                .includeOnResults(Asset.HAS_LINEAGE)
                .toRequest();

        IndexSearchResponse response = retrySearchUntil(index, 3L);

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
        AssetMutationResponse response = Asset.delete(client, start.getGuid()).block();
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
        assertTrue(LineageProcess.restore(client, start.getQualifiedName()));
        LineageProcess restored = LineageProcess.get(client, start.getGuid());
        assertFalse(restored.isComplete());
        assertEquals(restored.getGuid(), start.getGuid());
        assertEquals(restored.getQualifiedName(), start.getQualifiedName());
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"lineage.purge.lineage"},
            dependsOnGroups = {"lineage.delete.lineage.restore"},
            alwaysRun = true)
    void purgeLineage() throws AtlanException {
        AssetMutationResponse response =
                LineageProcess.purge(client, start.getGuid()).block();
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
        ConnectionTest.deleteConnection(client, connection.getQualifiedName(), log);
    }
}
