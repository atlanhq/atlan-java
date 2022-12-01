/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.LogicException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.admin.AtlanGroup;
import com.atlan.model.assets.*;
import com.atlan.model.core.Classification;
import com.atlan.model.core.Entity;
import com.atlan.model.core.EntityMutationResponse;
import com.atlan.model.enums.*;
import com.atlan.model.search.*;
import com.atlan.util.QueryFactory;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * Tests all aspects of SQL assets (Connections, Databases, Schemas, Tables, Views, Materialized Views, Columns).
 */
@Test(groups = {"asset"})
@Slf4j
public class SQLAssetTest extends AtlanLiveTest {

    private static final String PREFIX = "SQLAssetTest";

    private static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.SAPHANA;
    private static final String CONNECTION_NAME = "java-sdk-" + PREFIX;

    public static final String DATABASE_NAME = PREFIX + "_db";
    public static final String SCHEMA_NAME = PREFIX + "_schema";
    public static final String TABLE_NAME = PREFIX + "_table";
    public static final String VIEW_NAME = PREFIX + "_view";
    public static final String MVIEW_NAME = PREFIX + "_mview";
    public static final String COLUMN_NAME1 = PREFIX + "_col1";
    public static final String COLUMN_NAME2 = PREFIX + "_col2";
    public static final String COLUMN_NAME3 = PREFIX + "_col3";
    public static final String COLUMN_NAME4 = PREFIX + "_col4";
    public static final String COLUMN_NAME5 = PREFIX + "_col5";
    public static final String COLUMN_NAME6 = PREFIX + "_col6";

    private static final String CLASSIFICATION_NAME1 = PREFIX + "1";
    private static final String CLASSIFICATION_NAME2 = PREFIX + "2";
    private static final String TERM_NAME1 = PREFIX + "1";
    private static final String TERM_NAME2 = PREFIX + "2";

    private static String connectionGuid = null;
    private static String connectionQame = null;
    private static String databaseGuid = null;
    private static String databaseQame = null;
    private static String schemaGuid = null;
    private static String schemaQame = null;
    private static String tableGuid = null;
    private static String tableQame = null;
    private static String viewGuid = null;
    private static String viewQame = null;
    private static String mviewGuid = null;
    private static String mviewQame = null;
    private static String columnGuid5 = null;
    private static String columnQame5 = null;
    private static AtlanGroup ownerGroup = null;
    private static Glossary glossary = null;
    private static GlossaryTerm term1 = null;
    private static GlossaryTerm term2 = null;

    @Test(groups = {"create.connection"})
    void createConnection() throws AtlanException {
        Connection connection = ConnectionTest.createConnection(CONNECTION_NAME, CONNECTOR_TYPE);
        connectionGuid = connection.getGuid();
        connectionQame = connection.getQualifiedName();
    }

    @Test(
            groups = {"search.connection"},
            dependsOnGroups = {"create.connection"})
    void findConnection() {
        try {
            List<Connection> results = Connection.findByName(CONNECTION_NAME, CONNECTOR_TYPE, null);
            assertNotNull(results);
            assertEquals(results.size(), 1);
            Connection one = results.get(0);
            assertEquals(one.getGuid(), connectionGuid);
            assertEquals(one.getQualifiedName(), connectionQame);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to find the created connection by its name.");
        }
    }

    @Test(
            groups = {"create.database"},
            dependsOnGroups = {"create.connection"})
    void createDatabase() {
        try {
            Database database = Database.creator(DATABASE_NAME, connectionQame).build();
            EntityMutationResponse response = database.upsert();
            assertNotNull(response);
            assertTrue(response.getUpdatedEntities().isEmpty());
            assertTrue(response.getDeletedEntities().isEmpty());
            assertEquals(response.getCreatedEntities().size(), 1);
            Entity one = response.getCreatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), Database.TYPE_NAME);
            assertTrue(one instanceof Database);
            database = (Database) one;
            databaseGuid = database.getGuid();
            assertNotNull(databaseGuid);
            databaseQame = database.getQualifiedName();
            assertNotNull(databaseQame);
            assertEquals(database.getName(), DATABASE_NAME);
            assertEquals(database.getConnectorType(), CONNECTOR_TYPE);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a database.");
        }
    }

    @Test(
            groups = {"create.schema"},
            dependsOnGroups = {"create.database"})
    void createSchema() {
        try {
            Schema schema = Schema.creator(SCHEMA_NAME, databaseQame).build();
            EntityMutationResponse response = schema.upsert();
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 1);
            Entity one = response.getUpdatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), Database.TYPE_NAME);
            assertTrue(one instanceof Database);
            Database database = (Database) one;
            assertEquals(database.getGuid(), databaseGuid);
            assertEquals(database.getQualifiedName(), databaseQame);
            assertEquals(response.getCreatedEntities().size(), 1);
            one = response.getCreatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), Schema.TYPE_NAME);
            assertTrue(one instanceof Schema);
            schema = (Schema) one;
            schemaGuid = schema.getGuid();
            assertNotNull(schemaGuid);
            schemaQame = schema.getQualifiedName();
            assertNotNull(schemaQame);
            assertEquals(schema.getName(), SCHEMA_NAME);
            assertEquals(schema.getConnectorType(), CONNECTOR_TYPE);
            assertEquals(schema.getDatabaseName(), DATABASE_NAME);
            assertEquals(schema.getDatabaseQualifiedName(), databaseQame);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a schema.");
        }
    }

    @Test(
            groups = {"create.table"},
            dependsOnGroups = {"create.schema"})
    void createTable() {
        try {
            Table table = Table.creator(TABLE_NAME, schemaQame).columnCount(2L).build();
            EntityMutationResponse response = table.upsert();
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 1);
            Entity one = response.getUpdatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), Schema.TYPE_NAME);
            assertTrue(one instanceof Schema);
            Schema schema = (Schema) one;
            assertEquals(schema.getGuid(), schemaGuid);
            assertEquals(schema.getQualifiedName(), schemaQame);
            assertEquals(response.getCreatedEntities().size(), 1);
            one = response.getCreatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), Table.TYPE_NAME);
            assertTrue(one instanceof Table);
            table = (Table) one;
            tableGuid = table.getGuid();
            assertNotNull(tableGuid);
            tableQame = table.getQualifiedName();
            assertNotNull(tableQame);
            assertEquals(table.getName(), TABLE_NAME);
            assertEquals(table.getConnectorType(), CONNECTOR_TYPE);
            assertEquals(table.getSchemaName(), SCHEMA_NAME);
            assertEquals(table.getSchemaQualifiedName(), schemaQame);
            assertEquals(table.getDatabaseName(), DATABASE_NAME);
            assertEquals(table.getDatabaseQualifiedName(), databaseQame);
            assertEquals(table.getConnectionQualifiedName(), connectionQame);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a table.");
        }
    }

    @Test(
            groups = {"create.view"},
            dependsOnGroups = {"create.schema"})
    void createView() {
        try {
            View view = View.creator(VIEW_NAME, schemaQame).columnCount(2L).build();
            EntityMutationResponse response = view.upsert();
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 1);
            Entity one = response.getUpdatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), Schema.TYPE_NAME);
            assertTrue(one instanceof Schema);
            Schema schema = (Schema) one;
            assertEquals(schema.getGuid(), schemaGuid);
            assertEquals(schema.getQualifiedName(), schemaQame);
            assertEquals(response.getCreatedEntities().size(), 1);
            one = response.getCreatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), View.TYPE_NAME);
            assertTrue(one instanceof View);
            view = (View) one;
            viewGuid = view.getGuid();
            assertNotNull(viewGuid);
            viewQame = view.getQualifiedName();
            assertNotNull(viewQame);
            assertEquals(view.getName(), VIEW_NAME);
            assertEquals(view.getConnectorType(), CONNECTOR_TYPE);
            assertEquals(view.getSchemaName(), SCHEMA_NAME);
            assertEquals(view.getSchemaQualifiedName(), schemaQame);
            assertEquals(view.getDatabaseName(), DATABASE_NAME);
            assertEquals(view.getDatabaseQualifiedName(), databaseQame);
            assertEquals(view.getConnectionQualifiedName(), connectionQame);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a view.");
        }
    }

    @Test(
            groups = {"create.mview"},
            dependsOnGroups = {"create.schema"})
    void createMView() {
        try {
            MaterializedView mview = MaterializedView.creator(MVIEW_NAME, schemaQame)
                    .columnCount(2L)
                    .build();
            EntityMutationResponse response = mview.upsert();
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 1);
            Entity one = response.getUpdatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), Schema.TYPE_NAME);
            assertTrue(one instanceof Schema);
            Schema schema = (Schema) one;
            assertEquals(schema.getGuid(), schemaGuid);
            assertEquals(schema.getQualifiedName(), schemaQame);
            assertEquals(response.getCreatedEntities().size(), 1);
            one = response.getCreatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), MaterializedView.TYPE_NAME);
            assertTrue(one instanceof MaterializedView);
            mview = (MaterializedView) one;
            mviewGuid = mview.getGuid();
            assertNotNull(mviewGuid);
            mviewQame = mview.getQualifiedName();
            assertNotNull(mviewQame);
            assertEquals(mview.getName(), MVIEW_NAME);
            assertEquals(mview.getConnectorType(), CONNECTOR_TYPE);
            assertEquals(mview.getSchemaName(), SCHEMA_NAME);
            assertEquals(mview.getSchemaQualifiedName(), schemaQame);
            assertEquals(mview.getDatabaseName(), DATABASE_NAME);
            assertEquals(mview.getDatabaseQualifiedName(), databaseQame);
            assertEquals(mview.getConnectionQualifiedName(), connectionQame);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a materialized view.");
        }
    }

    @Test(
            groups = {"create.column.1"},
            dependsOnGroups = {"create.table"})
    void createColumn1() {
        try {
            Column column =
                    Column.creator(COLUMN_NAME1, Table.TYPE_NAME, tableQame, 1).build();
            EntityMutationResponse response = column.upsert();
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 1);
            Entity one = response.getUpdatedEntities().get(0);
            assertTrue(one instanceof Table);
            Table table = (Table) one;
            assertNotNull(table);
            assertEquals(table.getGuid(), tableGuid);
            assertEquals(table.getQualifiedName(), tableQame);
            assertEquals(response.getCreatedEntities().size(), 1);
            one = response.getCreatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), Column.TYPE_NAME);
            assertTrue(one instanceof Column);
            column = (Column) one;
            String columnGuid1 = column.getGuid();
            assertNotNull(columnGuid1);
            String columnQame1 = column.getQualifiedName();
            assertNotNull(columnQame1);
            assertEquals(column.getName(), COLUMN_NAME1);
            assertEquals(column.getConnectorType(), CONNECTOR_TYPE);
            assertEquals(column.getTableName(), TABLE_NAME);
            assertEquals(column.getTableQualifiedName(), tableQame);
            assertEquals(column.getSchemaName(), SCHEMA_NAME);
            assertEquals(column.getSchemaQualifiedName(), schemaQame);
            assertEquals(column.getDatabaseName(), DATABASE_NAME);
            assertEquals(column.getDatabaseQualifiedName(), databaseQame);
            assertEquals(column.getConnectionQualifiedName(), connectionQame);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a column in a table.");
        }
    }

    @Test(
            groups = {"create.column.2"},
            dependsOnGroups = {"create.table"})
    void createColumn2() {
        try {
            Column column =
                    Column.creator(COLUMN_NAME2, Table.TYPE_NAME, tableQame, 2).build();
            EntityMutationResponse response = column.upsert();
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 1);
            Entity one = response.getUpdatedEntities().get(0);
            assertTrue(one instanceof Table);
            Table table = (Table) one;
            assertNotNull(table);
            assertEquals(table.getGuid(), tableGuid);
            assertEquals(table.getQualifiedName(), tableQame);
            assertEquals(response.getCreatedEntities().size(), 1);
            one = response.getCreatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), Column.TYPE_NAME);
            assertTrue(one instanceof Column);
            column = (Column) one;
            String columnGuid2 = column.getGuid();
            assertNotNull(columnGuid2);
            String columnQame2 = column.getQualifiedName();
            assertNotNull(columnQame2);
            assertEquals(column.getName(), COLUMN_NAME2);
            assertEquals(column.getConnectorType(), CONNECTOR_TYPE);
            assertEquals(column.getTableName(), TABLE_NAME);
            assertEquals(column.getTableQualifiedName(), tableQame);
            assertEquals(column.getSchemaName(), SCHEMA_NAME);
            assertEquals(column.getSchemaQualifiedName(), schemaQame);
            assertEquals(column.getDatabaseName(), DATABASE_NAME);
            assertEquals(column.getDatabaseQualifiedName(), databaseQame);
            assertEquals(column.getConnectionQualifiedName(), connectionQame);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a column in a table.");
        }
    }

    @Test(
            groups = {"create.column.3"},
            dependsOnGroups = {"create.view"})
    void createColumn3() {
        try {
            Column column =
                    Column.creator(COLUMN_NAME3, View.TYPE_NAME, viewQame, 1).build();
            EntityMutationResponse response = column.upsert();
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 1);
            Entity one = response.getUpdatedEntities().get(0);
            assertTrue(one instanceof View);
            View view = (View) one;
            assertNotNull(view);
            assertEquals(view.getGuid(), viewGuid);
            assertEquals(view.getQualifiedName(), viewQame);
            assertEquals(response.getCreatedEntities().size(), 1);
            one = response.getCreatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), Column.TYPE_NAME);
            assertTrue(one instanceof Column);
            column = (Column) one;
            String columnGuid3 = column.getGuid();
            assertNotNull(columnGuid3);
            String columnQame3 = column.getQualifiedName();
            assertNotNull(columnQame3);
            assertEquals(column.getName(), COLUMN_NAME3);
            assertEquals(column.getConnectorType(), CONNECTOR_TYPE);
            assertEquals(column.getViewName(), VIEW_NAME);
            assertEquals(column.getViewQualifiedName(), viewQame);
            assertEquals(column.getSchemaName(), SCHEMA_NAME);
            assertEquals(column.getSchemaQualifiedName(), schemaQame);
            assertEquals(column.getDatabaseName(), DATABASE_NAME);
            assertEquals(column.getDatabaseQualifiedName(), databaseQame);
            assertEquals(column.getConnectionQualifiedName(), connectionQame);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a column in a view.");
        }
    }

    @Test(
            groups = {"create.column.4"},
            dependsOnGroups = {"create.view"})
    void createColumn4() {
        try {
            Column column =
                    Column.creator(COLUMN_NAME4, View.TYPE_NAME, viewQame, 2).build();
            EntityMutationResponse response = column.upsert();
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 1);
            Entity one = response.getUpdatedEntities().get(0);
            assertTrue(one instanceof View);
            View view = (View) one;
            assertNotNull(view);
            assertEquals(view.getGuid(), viewGuid);
            assertEquals(view.getQualifiedName(), viewQame);
            assertEquals(response.getCreatedEntities().size(), 1);
            one = response.getCreatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), Column.TYPE_NAME);
            assertTrue(one instanceof Column);
            column = (Column) one;
            String columnGuid4 = column.getGuid();
            assertNotNull(columnGuid4);
            String columnQame4 = column.getQualifiedName();
            assertNotNull(columnQame4);
            assertEquals(column.getName(), COLUMN_NAME4);
            assertEquals(column.getConnectorType(), CONNECTOR_TYPE);
            assertEquals(column.getViewName(), VIEW_NAME);
            assertEquals(column.getViewQualifiedName(), viewQame);
            assertEquals(column.getSchemaName(), SCHEMA_NAME);
            assertEquals(column.getSchemaQualifiedName(), schemaQame);
            assertEquals(column.getDatabaseName(), DATABASE_NAME);
            assertEquals(column.getDatabaseQualifiedName(), databaseQame);
            assertEquals(column.getConnectionQualifiedName(), connectionQame);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a column in a view.");
        }
    }

    @Test(
            groups = {"create.column.5"},
            dependsOnGroups = {"create.view"})
    void createColumn5() {
        try {
            Column column = Column.creator(COLUMN_NAME5, MaterializedView.TYPE_NAME, mviewQame, 1)
                    .build();
            EntityMutationResponse response = column.upsert();
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 1);
            Entity one = response.getUpdatedEntities().get(0);
            assertTrue(one instanceof MaterializedView);
            MaterializedView mview = (MaterializedView) one;
            assertNotNull(mview);
            assertEquals(mview.getGuid(), mviewGuid);
            assertEquals(mview.getQualifiedName(), mviewQame);
            assertEquals(response.getCreatedEntities().size(), 1);
            one = response.getCreatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), Column.TYPE_NAME);
            assertTrue(one instanceof Column);
            column = (Column) one;
            columnGuid5 = column.getGuid();
            assertNotNull(columnGuid5);
            columnQame5 = column.getQualifiedName();
            assertNotNull(columnQame5);
            assertEquals(column.getName(), COLUMN_NAME5);
            assertEquals(column.getConnectorType(), CONNECTOR_TYPE);
            assertEquals(column.getViewName(), MVIEW_NAME);
            assertEquals(column.getViewQualifiedName(), mviewQame);
            assertEquals(column.getSchemaName(), SCHEMA_NAME);
            assertEquals(column.getSchemaQualifiedName(), schemaQame);
            assertEquals(column.getDatabaseName(), DATABASE_NAME);
            assertEquals(column.getDatabaseQualifiedName(), databaseQame);
            assertEquals(column.getConnectionQualifiedName(), connectionQame);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a column in a materialized view.");
        }
    }

    @Test(
            groups = {"create.column.6"},
            dependsOnGroups = {"create.view"})
    void createColumn6() {
        try {
            Column column = Column.creator(COLUMN_NAME6, MaterializedView.TYPE_NAME, mviewQame, 2)
                    .build();
            EntityMutationResponse response = column.upsert();
            assertNotNull(response);
            assertTrue(response.getDeletedEntities().isEmpty());
            assertEquals(response.getUpdatedEntities().size(), 1);
            Entity one = response.getUpdatedEntities().get(0);
            assertTrue(one instanceof MaterializedView);
            MaterializedView mview = (MaterializedView) one;
            assertNotNull(mview);
            assertEquals(mview.getGuid(), mviewGuid);
            assertEquals(mview.getQualifiedName(), mviewQame);
            assertEquals(response.getCreatedEntities().size(), 1);
            one = response.getCreatedEntities().get(0);
            assertNotNull(one);
            assertEquals(one.getTypeName(), Column.TYPE_NAME);
            assertTrue(one instanceof Column);
            column = (Column) one;
            String columnGuid6 = column.getGuid();
            assertNotNull(columnGuid6);
            String columnQame6 = column.getQualifiedName();
            assertNotNull(columnQame6);
            assertEquals(column.getName(), COLUMN_NAME6);
            assertEquals(column.getConnectorType(), CONNECTOR_TYPE);
            assertEquals(column.getViewName(), MVIEW_NAME);
            assertEquals(column.getViewQualifiedName(), mviewQame);
            assertEquals(column.getSchemaName(), SCHEMA_NAME);
            assertEquals(column.getSchemaQualifiedName(), schemaQame);
            assertEquals(column.getDatabaseName(), DATABASE_NAME);
            assertEquals(column.getDatabaseQualifiedName(), databaseQame);
            assertEquals(column.getConnectionQualifiedName(), connectionQame);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to create a column in a materialized view.");
        }
    }

    @Test(
            groups = {"read.column.5"},
            dependsOnGroups = {"create.column.5"})
    void readColumn5() throws AtlanException {
        Column byGuid = Column.retrieveByGuid(columnGuid5);
        assertNotNull(byGuid);
        assertTrue(byGuid.isComplete());
        assertEquals(byGuid.getGuid(), columnGuid5);
        assertEquals(byGuid.getQualifiedName(), columnQame5);
        assertEquals(byGuid.getName(), COLUMN_NAME5);
        SQL one = byGuid.getParent();
        assertNotNull(one);
        assertEquals(one.getTypeName(), MaterializedView.TYPE_NAME);
        assertEquals(one.getGuid(), mviewGuid);
        Column byQN = Column.retrieveByQualifiedName(columnQame5);
        assertNotNull(byQN);
        assertTrue(byQN.isComplete());
        assertEquals(byQN.getGuid(), columnGuid5);
        assertEquals(byQN.getQualifiedName(), columnQame5);
        assertEquals(byQN.getName(), COLUMN_NAME5);
        one = byQN.getParent();
        assertNotNull(one);
        assertEquals(one.getTypeName(), MaterializedView.TYPE_NAME);
        assertEquals(one.getGuid(), mviewGuid);
        assertEquals(byGuid, byQN);
    }

    @Test(
            groups = {"search.byParentQN"},
            dependsOnGroups = {"create.*"})
    void searchByParentQN() throws AtlanException, InterruptedException {

        Query byState = QueryFactory.active();
        Query byParentQN = QueryFactory.whereQualifiedNameStartsWith(connectionQame);
        Query combined = BoolQuery.of(b -> b.filter(byState, byParentQN))._toQuery();

        SortOptions sort = SortOptions.of(
                s -> s.field(FieldSort.of(f -> f.field("__timestamp").order(SortOrder.Asc))));

        IndexSearchRequest index = IndexSearchRequest.builder()
                .dsl(IndexSearchDSL.builder()
                        .from(0)
                        .size(50)
                        .query(combined)
                        .sortOption(sort)
                        .build())
                .attribute("name")
                .build();

        IndexSearchResponse response = index.search();
        assertNotNull(response);

        int count = 0;
        while (response.getApproximateCount() < 12L && count < Atlan.getMaxNetworkRetries()) {
            Thread.sleep(2000);
            response = index.search();
            count++;
        }

        assertEquals(response.getApproximateCount().longValue(), 12L);
        List<Entity> entities = response.getEntities();
        assertNotNull(entities);
        assertEquals(entities.size(), 12);

        Entity one = entities.get(0);
        assertTrue(one instanceof Connection);
        Connection connection = (Connection) one;
        assertEquals(connection.getGuid(), connectionGuid);
        assertEquals(connection.getQualifiedName(), connectionQame);
        assertEquals(connection.getName(), CONNECTION_NAME);

        one = entities.get(1);
        assertTrue(one instanceof Database);
        Database database = (Database) one;
        assertEquals(database.getGuid(), databaseGuid);
        assertEquals(database.getQualifiedName(), databaseQame);
        assertEquals(database.getName(), DATABASE_NAME);

        one = entities.get(2);
        assertTrue(one instanceof Schema);
        Schema schema = (Schema) one;
        assertEquals(schema.getGuid(), schemaGuid);
        assertEquals(schema.getQualifiedName(), schemaQame);
        assertEquals(schema.getName(), SCHEMA_NAME);

        one = entities.get(3);
        assertTrue(one instanceof MaterializedView);
        MaterializedView mview = (MaterializedView) one;
        assertEquals(mview.getGuid(), mviewGuid);
        assertEquals(mview.getQualifiedName(), mviewQame);
        assertEquals(mview.getName(), MVIEW_NAME);

        one = entities.get(4);
        assertTrue(one instanceof Table);
        Table table = (Table) one;
        assertEquals(table.getGuid(), tableGuid);
        assertEquals(table.getQualifiedName(), tableQame);
        assertEquals(table.getName(), TABLE_NAME);

        one = entities.get(5);
        assertTrue(one instanceof Column);
        Column column = (Column) one;
        assertEquals(column.getName(), COLUMN_NAME1);

        one = entities.get(6);
        assertTrue(one instanceof Column);
        column = (Column) one;
        assertEquals(column.getName(), COLUMN_NAME2);

        one = entities.get(7);
        assertTrue(one instanceof View);
        View view = (View) one;
        assertEquals(view.getGuid(), viewGuid);
        assertEquals(view.getQualifiedName(), viewQame);
        assertEquals(view.getName(), VIEW_NAME);

        one = entities.get(8);
        assertTrue(one instanceof Column);
        column = (Column) one;
        assertEquals(column.getName(), COLUMN_NAME3);

        one = entities.get(9);
        assertTrue(one instanceof Column);
        column = (Column) one;
        assertEquals(column.getName(), COLUMN_NAME4);

        one = entities.get(10);
        assertTrue(one instanceof Column);
        column = (Column) one;
        assertEquals(column.getGuid(), columnGuid5);
        assertEquals(column.getQualifiedName(), columnQame5);
        assertEquals(column.getName(), COLUMN_NAME5);

        one = entities.get(11);
        assertTrue(one instanceof Column);
        column = (Column) one;
        assertEquals(column.getName(), COLUMN_NAME6);
    }

    @Test(groups = {"create.group.owners"})
    void createGroupOwners() throws AtlanException {
        ownerGroup = AdminTest.createGroup(PREFIX);
    }

    @Test(
            groups = {"update.column.owners"},
            dependsOnGroups = {"read.column.5", "create.group.owners"})
    void updateColumnOwners() throws AtlanException {
        Column toUpdate = Column.updater(columnQame5, COLUMN_NAME5)
                .ownerGroup(ownerGroup.getName())
                .build();
        EntityMutationResponse response = toUpdate.upsert();
        Entity one = validateSingleUpdate(response);
        assertTrue(one instanceof Column);
        Column updated = (Column) one;
        validateUpdatedColumn(updated);
        Set<String> groups = updated.getOwnerGroups();
        assertNotNull(groups);
        assertEquals(groups.size(), 1);
        assertTrue(groups.contains(ownerGroup.getName()));
    }

    @Test(
            groups = {"update.column.owners.x"},
            dependsOnGroups = {"update.column.owners"})
    void updateColumnOwnersX() throws AtlanException {
        Column cleared = Column.removeOwners(columnQame5, COLUMN_NAME5);
        validateUpdatedColumn(cleared);
        log.info("Groups: {}", cleared.getOwnerGroups());
        assertTrue(cleared.getOwnerGroups() == null || cleared.getOwnerGroups().isEmpty());
    }

    @Test(
            groups = {"update.column.certificate"},
            dependsOnGroups = {"update.column.owners*"})
    void updateColumnCertificate() throws AtlanException {
        Column column = Column.updateCertificate(columnQame5, CERTIFICATE_STATUS, CERTIFICATE_MESSAGE);
        validateUpdatedColumn(column);
        assertEquals(column.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(column.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
    }

    @Test(
            groups = {"update.column.certificate.x"},
            dependsOnGroups = {"update.column.certificate"})
    void updateColumnCertificateX() throws AtlanException {
        Column cleared = Column.removeCertificate(columnQame5, COLUMN_NAME5);
        validateUpdatedColumn(cleared);
        assertNull(cleared.getCertificateStatus());
        assertNull(cleared.getCertificateStatusMessage());
    }

    @Test(
            groups = {"update.column.announcement"},
            dependsOnGroups = {"update.column.certificate*"})
    void updateColumnAnnouncement() throws AtlanException {
        Column column =
                Column.updateAnnouncement(columnQame5, ANNOUNCEMENT_TYPE, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
        validateUpdatedColumn(column);
        assertEquals(column.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(column.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(column.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"update.column.announcement.x"},
            dependsOnGroups = {"update.column.announcement"})
    void updateColumnAnnouncementX() throws AtlanException {
        Column column = Column.removeAnnouncement(columnQame5, COLUMN_NAME5);
        validateUpdatedColumn(column);
        assertNull(column.getAnnouncementType());
        assertNull(column.getAnnouncementTitle());
        assertNull(column.getAnnouncementMessage());
    }

    @Test(
            groups = {"update.column.descriptions"},
            dependsOnGroups = {"update.column.announcement*"})
    void updateColumnDescriptions() throws AtlanException {
        Column toUpdate = Column.updater(columnQame5, COLUMN_NAME5)
                .description(DESCRIPTION)
                .userDescription(DESCRIPTION)
                .build();
        EntityMutationResponse response = toUpdate.upsert();
        Entity one = validateSingleUpdate(response);
        assertTrue(one instanceof Column);
        Column updated = (Column) one;
        validateUpdatedColumn(updated);
        assertEquals(updated.getDescription(), DESCRIPTION);
        assertEquals(updated.getUserDescription(), DESCRIPTION);
    }

    @Test(
            groups = {"update.column.description.x"},
            dependsOnGroups = {"update.column.descriptions"})
    void updateColumnDescriptionX() throws AtlanException {
        Column cleared = Column.removeDescription(columnQame5, COLUMN_NAME5);
        validateUpdatedColumn(cleared);
        assertNull(cleared.getDescription());
    }

    @Test(
            groups = {"update.column.userDescription.x"},
            dependsOnGroups = {"update.column.description.x"})
    void updateColumnUserDescriptionX() throws AtlanException {
        Column cleared = Column.removeUserDescription(columnQame5, COLUMN_NAME5);
        validateUpdatedColumn(cleared);
        assertNull(cleared.getUserDescription());
    }

    // TODO: custom metadata variations — keep these separate
    //  + search by any custom metadata value
    //  + search by specific custom metadata value

    @Test(groups = {"create.classifications"})
    void createClassifications() throws AtlanException {
        ClassificationTest.createClassification(CLASSIFICATION_NAME1);
        ClassificationTest.createClassification(CLASSIFICATION_NAME2);
    }

    @Test(
            groups = {"update.column.classification"},
            dependsOnGroups = {"create.classifications", "update.column.userDescription.x"})
    void updateClassification() throws AtlanException {
        Column toUpdate = Column.updater(columnQame5, COLUMN_NAME5)
                .classification(Classification.of(CLASSIFICATION_NAME1, columnGuid5))
                .build();
        EntityMutationResponse response = toUpdate.upsert(true, false);
        Entity one = validateSingleUpdate(response);
        assertTrue(one instanceof Column);
        Column column = (Column) one;
        validateUpdatedColumn(column);
        validateHasClassifications(column, Set.of(CLASSIFICATION_NAME1));
    }

    @Test(
            groups = {"update.column.classification.x"},
            dependsOnGroups = {"update.column.classification"})
    void updateClassificationX() throws AtlanException {
        Column toUpdate = Column.updater(columnQame5, COLUMN_NAME5).build();
        toUpdate.removeClassifications();
        EntityMutationResponse response = toUpdate.upsert(true, false);
        Entity one = validateSingleUpdate(response);
        assertTrue(one instanceof Column);
        Column column = (Column) one;
        validateUpdatedColumn(column);
        validateHasClassifications(column, Collections.emptySet());
    }

    @Test(
            groups = {"update.column.addClassifications"},
            dependsOnGroups = {"update.column.classification.x"})
    void updateColumnAddClassifications() throws AtlanException {
        Column.addClassifications(columnQame5, List.of(CLASSIFICATION_NAME1, CLASSIFICATION_NAME2));
        Column column = Column.retrieveByGuid(columnGuid5);
        validateCompleteColumn(column);
        validateHasClassifications(column, Set.of(CLASSIFICATION_NAME1, CLASSIFICATION_NAME2));
    }

    @Test(
            groups = {"update.column.addClassificationDuplicate"},
            dependsOnGroups = {"update.column.addClassifications"})
    void updateColumnAddClassificationDuplicate() {
        assertThrows(
                InvalidRequestException.class,
                () -> Column.addClassifications(columnQame5, List.of(CLASSIFICATION_NAME1)));
    }

    @Test(
            groups = {"update.column.removeClassification"},
            dependsOnGroups = {"update.column.addClassificationDuplicate"})
    void updateColumnRemoveClassification() throws AtlanException {
        Column.removeClassification(columnQame5, CLASSIFICATION_NAME2);
        Column column = Column.retrieveByGuid(columnGuid5);
        validateCompleteColumn(column);
        validateHasClassifications(column, Set.of(CLASSIFICATION_NAME1));
    }

    @Test(
            groups = {"update.column.removeClassificationNonexistent"},
            dependsOnGroups = {"update.column.removeClassification"})
    void updateColumnRemoveClassificationNonexistent() {
        assertThrows(
                InvalidRequestException.class, () -> Column.removeClassification(columnQame5, CLASSIFICATION_NAME2));
    }

    @Test(
            groups = {"update.column.addClassifications.again"},
            dependsOnGroups = {"update.column.removeClassificationNonexistent"})
    void updateColumnAddClassificationsAgain() throws AtlanException {
        Column.addClassifications(columnQame5, List.of(CLASSIFICATION_NAME2));
        Column column = Column.retrieveByGuid(columnGuid5);
        validateCompleteColumn(column);
        validateHasClassifications(column, Set.of(CLASSIFICATION_NAME1, CLASSIFICATION_NAME2));
    }

    @Test(
            groups = {"search.byClassification"},
            dependsOnGroups = {"update.column.addClassifications.again"})
    void searchByAnyClassification() throws AtlanException, InterruptedException {

        Query byClassification = QueryFactory.withAnyValueFor("__traitNames");
        Query byState = QueryFactory.active();
        Query byType = QueryFactory.withType(Column.TYPE_NAME);
        Query combined =
                BoolQuery.of(b -> b.filter(byState, byType, byClassification))._toQuery();

        IndexSearchRequest index = IndexSearchRequest.builder()
                .dsl(IndexSearchDSL.builder().query(combined).build())
                .attribute("name")
                .attribute("connectionQualifiedName")
                .build();

        IndexSearchResponse response = index.search();
        assertNotNull(response);

        int count = 0;
        while (response.getApproximateCount() == 0L && count < Atlan.getMaxNetworkRetries()) {
            Thread.sleep(2000);
            response = index.search();
            count++;
        }

        assertEquals(response.getApproximateCount().longValue(), 1L);
        List<Entity> entities = response.getEntities();
        assertNotNull(entities);
        assertEquals(entities.size(), 1);
        Entity one = entities.get(0);
        assertTrue(one instanceof Column);
        assertFalse(one.isComplete());
        Column column = (Column) one;
        validateUpdatedColumn(column);
        assertEquals(column.getName(), COLUMN_NAME5);
        assertEquals(column.getConnectionQualifiedName(), connectionQame);
    }

    @Test(
            groups = {"search.byClassification"},
            dependsOnGroups = {"update.column.addClassifications.again"})
    void searchBySpecificClassification() throws AtlanException, InterruptedException {

        Query byClassification =
                QueryFactory.withAtLeastOneClassification(List.of(CLASSIFICATION_NAME1, CLASSIFICATION_NAME2));
        Query byState = QueryFactory.active();
        Query byType = QueryFactory.withType(Column.TYPE_NAME);
        Query combined =
                BoolQuery.of(b -> b.filter(byState, byType, byClassification))._toQuery();

        IndexSearchRequest index = IndexSearchRequest.builder()
                .dsl(IndexSearchDSL.builder().query(combined).build())
                .attribute("name")
                .build();

        IndexSearchResponse response = index.search();
        assertNotNull(response);

        int count = 0;
        while (response.getApproximateCount() == 0L && count < Atlan.getMaxNetworkRetries()) {
            Thread.sleep(2000);
            response = index.search();
            count++;
        }

        assertEquals(response.getApproximateCount().longValue(), 1L);
        List<Entity> entities = response.getEntities();
        assertNotNull(entities);
        assertEquals(entities.size(), 1);
        Entity one = entities.get(0);
        assertTrue(one instanceof Column);
        Column column = (Column) one;
        validateUpdatedColumn(column);
        assertEquals(column.getName(), COLUMN_NAME5);
        assertNull(column.getConnectionQualifiedName());
    }

    @Test(
            groups = {"update.column.removeClassifications"},
            dependsOnGroups = {"search.byClassification"})
    void updateColumnRemoveClassifications() throws AtlanException {
        Column column = Column.updater(columnQame5, COLUMN_NAME5).build();
        column.removeClassifications();
        EntityMutationResponse response = column.upsert(true, false);
        Entity one = validateSingleUpdate(response);
        assertTrue(one instanceof Column);
        column = (Column) one;
        validateUpdatedColumn(column);
        validateHasClassifications(column, Collections.emptySet());
    }

    private void validateHasClassifications(Column column, Set<String> names) {
        Set<String> clsNames = column.getClassificationNames();
        // Seems these are not always set (?)
        if (clsNames != null) {
            assertNotNull(clsNames);
            assertEquals(clsNames.size(), names.size());
            assertEquals(clsNames, names);
        }
        Set<Classification> classifications = column.getClassifications();
        assertNotNull(classifications);
        assertEquals(classifications.size(), names.size());
        Set<String> typeNames =
                classifications.stream().map(Classification::getTypeName).collect(Collectors.toSet());
        assertNotNull(typeNames);
        assertEquals(typeNames, names);
    }

    @Test(groups = {"create.glossary"})
    void createGlossary() throws AtlanException {
        glossary = GlossaryTest.createGlossary(PREFIX);
        term1 = GlossaryTest.createTerm(TERM_NAME1, glossary.getGuid());
        term2 = GlossaryTest.createTerm(TERM_NAME2, glossary.getGuid());
    }

    @Test(
            groups = {"update.column.assignedTerms"},
            dependsOnGroups = {"create.glossary", "update.column.removeClassifications"})
    void updateAssignedTerm() throws AtlanException {
        Column toUpdate = Column.updater(columnQame5, COLUMN_NAME5)
                .assignedTerm(GlossaryTerm.refByGuid(term1.getGuid()))
                .build();
        EntityMutationResponse response = toUpdate.upsert();
        assertNotNull(response);
        assertTrue(response.getCreatedEntities().isEmpty());
        assertTrue(response.getDeletedEntities().isEmpty());
        assertEquals(response.getUpdatedEntities().size(), 2);
        for (Entity one : response.getUpdatedEntities()) {
            if (one instanceof Column) {
                Column column = (Column) one;
                validateUpdatedColumn(column);
            } else if (one instanceof GlossaryTerm) {
                GlossaryTerm term = (GlossaryTerm) one;
                assertEquals(term.getGuid(), term1.getGuid());
            } else {
                throw new LogicException("Unexpected updated entity: " + one, "ATLAN_SDK_TEST-500-001", 500);
            }
        }
        Column updated = Column.retrieveByQualifiedName(columnQame5);
        validateCompleteColumn(updated);
        validateHasTerms(updated, Set.of(term1));
    }

    @Test(
            groups = {"update.column.assignedTerms.x"},
            dependsOnGroups = {"update.column.assignedTerms"})
    void updateAssignedTermsX() throws AtlanException {
        Column toUpdate = Column.updater(columnQame5, COLUMN_NAME5).build();
        toUpdate.removeAssignedTerms();
        EntityMutationResponse response = toUpdate.upsert();
        assertNotNull(response);
        assertTrue(response.getCreatedEntities().isEmpty());
        assertTrue(response.getDeletedEntities().isEmpty());
        assertEquals(response.getUpdatedEntities().size(), 2);
        for (Entity one : response.getUpdatedEntities()) {
            if (one instanceof Column) {
                Column column = (Column) one;
                validateUpdatedColumn(column);
            } else if (one instanceof GlossaryTerm) {
                GlossaryTerm term = (GlossaryTerm) one;
                assertEquals(term.getGuid(), term1.getGuid());
            } else {
                throw new LogicException("Unexpected updated entity: " + one, "ATLAN_SDK_TEST-500-001", 500);
            }
        }
        Column updated = Column.retrieveByQualifiedName(columnQame5);
        validateCompleteColumn(updated);
        validateHasTerms(updated, Collections.emptySet());
    }

    @Test(
            groups = {"update.column.appendTerms"},
            dependsOnGroups = {"update.column.assignedTerms.x"})
    void updateColumnAppendTerms() throws AtlanException {
        Column column = Column.appendTerms(columnQame5, List.of(term1, term2));
        validateUpdatedColumn(column);
        column = Column.retrieveByGuid(columnGuid5);
        validateCompleteColumn(column);
        validateHasTerms(column, Set.of(term1, term2));
    }

    @Test(
            groups = {"update.column.removeTerm"},
            dependsOnGroups = {"update.column.appendTerms"})
    void updateColumnRemoveTerm() throws AtlanException {
        Column column = Column.removeTerms(columnQame5, List.of(term2));
        validateUpdatedColumn(column);
        column = Column.retrieveByGuid(columnGuid5);
        validateCompleteColumn(column);
        validateHasTerms(column, Set.of(term1));
    }

    @Test(
            groups = {"update.column.appendTerms.again"},
            dependsOnGroups = {"update.column.removeTerm"})
    void updateColumnAppendTermsAgain() throws AtlanException {
        Column column = Column.appendTerms(columnQame5, List.of(term2));
        validateUpdatedColumn(column);
        column = Column.retrieveByGuid(columnGuid5);
        validateCompleteColumn(column);
        validateHasTerms(column, Set.of(term1, term2));
    }

    @Test(
            groups = {"update.column.removeTermAgain"},
            dependsOnGroups = {"update.column.appendTerms.again"})
    void updateColumnRemoveTermAgain() throws AtlanException {
        Column column = Column.removeTerms(columnQame5, List.of(term1));
        validateUpdatedColumn(column);
        column = Column.retrieveByGuid(columnGuid5);
        validateCompleteColumn(column);
        validateHasTerms(column, Set.of(term2));
    }

    @Test(
            groups = {"update.column.replaceTerms"},
            dependsOnGroups = {"update.column.removeTermAgain"})
    void updateColumnReplaceTerms() throws AtlanException {
        Column column = Column.replaceTerms(columnQame5, COLUMN_NAME5, List.of(term1));
        validateUpdatedColumn(column);
        column = Column.retrieveByGuid(columnGuid5);
        validateCompleteColumn(column);
        validateHasTerms(column, Set.of(term1));
    }

    @Test(
            groups = {"search.byTerm"},
            dependsOnGroups = {"update.column.replaceTerms"})
    void searchByAnyTerm() throws AtlanException, InterruptedException {

        Query byTermAssignment = QueryFactory.withAnyValueFor("__meanings");
        Query byState = QueryFactory.active();
        Query byType = QueryFactory.withType(Column.TYPE_NAME);
        Query combined =
                BoolQuery.of(b -> b.filter(byState, byType, byTermAssignment))._toQuery();

        IndexSearchRequest index = IndexSearchRequest.builder()
                .dsl(IndexSearchDSL.builder().query(combined).build())
                .attribute("name")
                .attribute("meanings")
                .attribute("connectionQualifiedName")
                .relationAttribute("name")
                .build();

        IndexSearchResponse response = index.search();
        assertNotNull(response);

        int count = 0;
        while (response.getApproximateCount() == 0L && count < Atlan.getMaxNetworkRetries()) {
            Thread.sleep(2000);
            response = index.search();
            count++;
        }

        assertEquals(response.getApproximateCount().longValue(), 1L);
        List<Entity> entities = response.getEntities();
        assertNotNull(entities);
        assertEquals(entities.size(), 1);
        Entity one = entities.get(0);
        assertTrue(one instanceof Column);
        Column column = (Column) one;
        validateUpdatedColumn(column);
        assertEquals(column.getName(), COLUMN_NAME5);
        assertEquals(column.getConnectionQualifiedName(), connectionQame);
        // Note: eventual consistency could return results immediately, but prior to
        // index reflecting latest actual state of the replacement...
        validateHasTerms(column, Set.of(term1));
        GlossaryTerm term = column.getAssignedTerms().first();
        assertEquals(term.getName(), term1.getName());
    }

    @Test(
            groups = {"search.byTerm"},
            dependsOnGroups = {"update.column.replaceTerms"})
    void searchBySpecificTerm() throws AtlanException, InterruptedException {

        Query byTermAssignment =
                QueryFactory.withAtLeastOneTerm(List.of(term1.getQualifiedName(), term2.getQualifiedName()));
        Query byState = QueryFactory.active();
        Query byType = QueryFactory.withType(Column.TYPE_NAME);
        Query combined =
                BoolQuery.of(b -> b.filter(byState, byType, byTermAssignment))._toQuery();

        IndexSearchRequest index = IndexSearchRequest.builder()
                .dsl(IndexSearchDSL.builder().query(combined).build())
                .attribute("name")
                .attribute("meanings")
                .attribute("connectionQualifiedName")
                .relationAttribute("name")
                .build();

        IndexSearchResponse response = index.search();
        assertNotNull(response);

        int count = 0;
        while (response.getApproximateCount() == 0L && count < Atlan.getMaxNetworkRetries()) {
            Thread.sleep(2000);
            response = index.search();
            count++;
        }

        assertEquals(response.getApproximateCount().longValue(), 1L);
        List<Entity> entities = response.getEntities();
        assertNotNull(entities);
        assertEquals(entities.size(), 1);
        Entity one = entities.get(0);
        assertTrue(one instanceof Column);
        Column column = (Column) one;
        validateUpdatedColumn(column);
        assertEquals(column.getName(), COLUMN_NAME5);
        assertEquals(column.getConnectionQualifiedName(), connectionQame);
        // Note: eventual consistency could return results immediately, but prior to
        // index reflecting latest actual state of the replacement...
        validateHasTerms(column, Set.of(term1));
        GlossaryTerm term = column.getAssignedTerms().first();
        assertEquals(term.getName(), term1.getName());
    }

    @Test(
            groups = {"update.column.removeTerms"},
            dependsOnGroups = {"update.column.replaceTerms"})
    void updateColumnRemoveTerms() throws AtlanException {
        Column column = Column.replaceTerms(columnQame5, COLUMN_NAME5, null);
        validateUpdatedColumn(column);
        column = Column.retrieveByQualifiedName(columnQame5);
        validateCompleteColumn(column);
        validateHasTerms(column, Collections.emptySet());
    }

    private void validateHasTerms(Column column, Set<GlossaryTerm> terms) {
        Set<GlossaryTerm> assignedTerms = column.getAssignedTerms();
        assertNotNull(assignedTerms);
        Set<GlossaryTerm> activeTerms = new HashSet<>();
        for (GlossaryTerm assignedTerm : assignedTerms) {
            if (assignedTerm.getRelationshipStatus() == AtlanStatus.ACTIVE
                    || assignedTerm.getRelationshipStatus() == null) {
                activeTerms.add(assignedTerm);
            }
        }
        assertEquals(activeTerms.size(), terms.size());
        Set<String> assignedTermGuids =
                activeTerms.stream().map(GlossaryTerm::getGuid).collect(Collectors.toSet());
        Set<String> termGuids = terms.stream().map(GlossaryTerm::getGuid).collect(Collectors.toSet());
        assertEquals(assignedTermGuids, termGuids);
        Set<String> termNames = terms.stream().map(GlossaryTerm::getName).collect(Collectors.toSet());
        Set<String> names = column.getMeaningNames();
        // Seems these are not always set (?)
        if (names != null) {
            assertEquals(names.size(), termNames.size());
            assertEquals(names, termNames);
        }
        Set<Meaning> meanings = column.getMeanings();
        if (meanings != null) {
            Set<String> meaningNames =
                    meanings.stream().map(Meaning::getDisplayText).collect(Collectors.toSet());
            assertEquals(meaningNames.size(), termNames.size());
            assertEquals(meaningNames, termNames);
            Set<String> meaningGuids =
                    meanings.stream().map(Meaning::getTermGuid).collect(Collectors.toSet());
            assertEquals(meaningGuids.size(), termGuids.size());
            assertEquals(meaningGuids, termGuids);
        }
    }

    @Test(
            groups = {"search.audit"},
            dependsOnGroups = {"update.column.removeTerms"})
    void searchAuditLogByGuid() throws AtlanException, InterruptedException {

        AuditSearchRequest request = AuditSearchRequest.byGuid(columnGuid5, 50).build();

        AuditSearchResponse response = request.search();
        assertNotNull(response);

        int count = 0;
        while (response.getCount() < 29L && count < Atlan.getMaxNetworkRetries()) {
            Thread.sleep(2000);
            response = request.search();
            count++;
        }

        validateAudits(response.getEntityAudits());
    }

    @Test(
            groups = {"search.audit"},
            dependsOnGroups = {"update.column.removeTerms"})
    void searchAuditLogByQN() throws AtlanException, InterruptedException {

        AuditSearchRequest request = AuditSearchRequest.byQualifiedName(Column.TYPE_NAME, columnQame5, 50)
                .build();

        AuditSearchResponse response = request.search();
        assertNotNull(response);

        int count = 0;
        while (response.getCount() < 29L && count < Atlan.getMaxNetworkRetries()) {
            Thread.sleep(2000);
            response = request.search();
            count++;
        }

        validateAudits(response.getEntityAudits());
    }

    private void validateAudits(List<EntityAudit> audits) {
        assertEquals(audits.size(), 29);

        EntityAudit one = audits.get(28);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.ENTITY_CREATE);
        AuditDetail detail = one.getDetail();
        assertTrue(detail instanceof Column);
        Column column = (Column) detail;
        validateUpdatedColumn(column);
        assertEquals(column.getName(), COLUMN_NAME5);
        assertNotNull(column.getParent());
        assertTrue(column.getParent() instanceof MaterializedView);
        MaterializedView parent = (MaterializedView) column.getParent();
        assertEquals(parent.getGuid(), mviewGuid);
        assertNull(column.getCertificateStatus());
        assertNull(column.getAnnouncementType());

        one = audits.get(27);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.ENTITY_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof Column);
        column = (Column) detail;
        validateUpdatedColumn(column);
        assertEquals(column.getOwnerGroups(), Set.of(ownerGroup.getName()));

        one = audits.get(26);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.ENTITY_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof Column);
        column = (Column) detail;
        validateUpdatedColumn(column);
        assertEquals(column.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(column.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);

        one = audits.get(25);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.ENTITY_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof Column);
        column = (Column) detail;
        validateUpdatedColumn(column);
        assertEquals(column.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(column.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(column.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);

        one = audits.get(24);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.ENTITY_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof Column);
        column = (Column) detail;
        validateUpdatedColumn(column);
        Set<String> clearedFields = column.getNullFields();
        assertTrue(clearedFields.contains("announcementType"));
        assertTrue(clearedFields.contains("announcementTitle"));
        assertTrue(clearedFields.contains("announcementMessage"));

        one = audits.get(23);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.ENTITY_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof Column);
        column = (Column) detail;
        validateUpdatedColumn(column);
        clearedFields = column.getNullFields();
        assertTrue(clearedFields.contains("certificateStatus"));
        assertTrue(clearedFields.contains("certificateStatusMessage"));

        one = audits.get(22);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.ENTITY_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof Column);
        column = (Column) detail;
        validateUpdatedColumn(column);
        assertEquals(column.getDescription(), DESCRIPTION);
        assertEquals(column.getUserDescription(), DESCRIPTION);

        one = audits.get(21);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.ENTITY_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof Column);
        column = (Column) detail;
        validateUpdatedColumn(column);
        clearedFields = column.getNullFields();
        assertTrue(clearedFields.contains("description"));

        one = audits.get(20);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.ENTITY_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof Column);
        column = (Column) detail;
        validateUpdatedColumn(column);
        assertTrue(column.getOwnerGroups().isEmpty());

        one = audits.get(19);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.ENTITY_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof Column);
        column = (Column) detail;
        validateUpdatedColumn(column);
        clearedFields = column.getNullFields();
        assertTrue(clearedFields.contains("userDescription"));

        one = audits.get(18);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CLASSIFICATION_ADD);
        detail = one.getDetail();
        assertTrue(detail instanceof Classification);
        Classification classification = (Classification) detail;
        assertEquals(classification.getTypeName(), CLASSIFICATION_NAME1);
        assertEquals(classification.getEntityGuid(), columnGuid5);

        one = audits.get(17);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.ENTITY_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof Column);
        column = (Column) detail;
        validateUpdatedColumn(column);
        assertNotNull(column.getClassifications());
        assertEquals(column.getClassifications().size(), 1);

        one = audits.get(16);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CLASSIFICATION_DELETE);
        detail = one.getDetail();
        assertTrue(detail instanceof Classification);
        classification = (Classification) detail;
        assertEquals(classification.getTypeName(), CLASSIFICATION_NAME1);

        one = audits.get(15);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.ENTITY_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof Column);
        column = (Column) detail;
        validateUpdatedColumn(column);
        assertTrue(column.getClassifications().isEmpty());

        Set<Classification> classificationsAdded = new HashSet<>();

        one = audits.get(14);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CLASSIFICATION_ADD);
        detail = one.getDetail();
        assertTrue(detail instanceof Classification);
        classificationsAdded.add((Classification) detail);

        one = audits.get(13);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CLASSIFICATION_ADD);
        detail = one.getDetail();
        assertTrue(detail instanceof Classification);
        classificationsAdded.add((Classification) detail);

        Set<String> classificationsAddedGuids =
                classificationsAdded.stream().map(Classification::getEntityGuid).collect(Collectors.toSet());
        Set<String> classificationsAddedNames =
                classificationsAdded.stream().map(Classification::getTypeName).collect(Collectors.toSet());
        assertEquals(classificationsAdded.size(), 2);
        assertEquals(classificationsAddedGuids.size(), 1);
        assertTrue(classificationsAddedGuids.contains(columnGuid5));
        assertEquals(classificationsAddedNames.size(), 2);
        assertTrue(classificationsAddedNames.contains(CLASSIFICATION_NAME1));
        assertTrue(classificationsAddedNames.contains(CLASSIFICATION_NAME2));

        one = audits.get(12);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CLASSIFICATION_DELETE);
        detail = one.getDetail();
        assertTrue(detail instanceof Classification);
        classification = (Classification) detail;
        assertEquals(classification.getTypeName(), CLASSIFICATION_NAME2);

        one = audits.get(11);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CLASSIFICATION_ADD);
        detail = one.getDetail();
        assertTrue(detail instanceof Classification);
        classification = (Classification) detail;
        assertEquals(classification.getTypeName(), CLASSIFICATION_NAME2);
        assertEquals(classification.getEntityGuid(), columnGuid5);

        Set<Classification> classificationsDeleted = new HashSet<>();

        one = audits.get(10);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CLASSIFICATION_DELETE);
        detail = one.getDetail();
        assertTrue(detail instanceof Classification);
        classificationsDeleted.add((Classification) detail);

        one = audits.get(9);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.CLASSIFICATION_DELETE);
        detail = one.getDetail();
        assertTrue(detail instanceof Classification);
        classificationsDeleted.add((Classification) detail);

        Set<String> classificationsDeletedNames =
                classificationsDeleted.stream().map(Classification::getTypeName).collect(Collectors.toSet());
        assertEquals(classificationsDeleted.size(), 2);
        assertEquals(classificationsDeletedNames.size(), 2);
        assertTrue(classificationsDeletedNames.contains(CLASSIFICATION_NAME1));
        assertTrue(classificationsDeletedNames.contains(CLASSIFICATION_NAME2));

        one = audits.get(8);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.ENTITY_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof Column);
        column = (Column) detail;
        validateUpdatedColumn(column);
        assertTrue(column.getClassifications().isEmpty());

        one = audits.get(7);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.ENTITY_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof Column);
        column = (Column) detail;
        validateUpdatedColumn(column);
        assertNotNull(column.getAssignedTerms());
        assertEquals(column.getAssignedTerms().size(), 1);
        assertEquals(column.getAssignedTerms().first().getGuid(), term1.getGuid());

        one = audits.get(6);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.ENTITY_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof Column);
        column = (Column) detail;
        validateUpdatedColumn(column);
        assertTrue(
                column.getAssignedTerms() == null || column.getAssignedTerms().isEmpty());

        one = audits.get(5);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.ENTITY_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof Column);
        column = (Column) detail;
        validateUpdatedColumn(column);
        Set<GlossaryTerm> terms = column.getAssignedTerms();
        assertEquals(terms.size(), 2);
        Set<String> termGuids = terms.stream().map(GlossaryTerm::getGuid).collect(Collectors.toSet());
        assertEquals(termGuids.size(), 2);
        assertTrue(termGuids.contains(term1.getGuid()));
        assertTrue(termGuids.contains(term2.getGuid()));

        one = audits.get(4);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.ENTITY_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof Column);
        column = (Column) detail;
        validateUpdatedColumn(column);
        assertNotNull(column.getAssignedTerms());
        assertEquals(column.getAssignedTerms().size(), 1);
        assertEquals(column.getAssignedTerms().first().getGuid(), term1.getGuid());

        one = audits.get(3);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.ENTITY_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof Column);
        column = (Column) detail;
        validateUpdatedColumn(column);
        terms = column.getAssignedTerms();
        assertEquals(terms.size(), 2);
        termGuids = terms.stream().map(GlossaryTerm::getGuid).collect(Collectors.toSet());
        assertEquals(termGuids.size(), 2);
        assertTrue(termGuids.contains(term1.getGuid()));
        assertTrue(termGuids.contains(term2.getGuid()));

        one = audits.get(2);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.ENTITY_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof Column);
        column = (Column) detail;
        validateUpdatedColumn(column);
        assertNotNull(column.getAssignedTerms());
        assertEquals(column.getAssignedTerms().size(), 1);
        assertEquals(column.getAssignedTerms().first().getGuid(), term2.getGuid());

        one = audits.get(1);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.ENTITY_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof Column);
        column = (Column) detail;
        validateUpdatedColumn(column);
        assertNotNull(column.getAssignedTerms());
        assertEquals(column.getAssignedTerms().size(), 1);
        assertEquals(column.getAssignedTerms().first().getGuid(), term1.getGuid());

        one = audits.get(0);
        assertNotNull(one);
        assertEquals(one.getAction(), AuditActionType.ENTITY_UPDATE);
        detail = one.getDetail();
        assertTrue(detail instanceof Column);
        column = (Column) detail;
        validateUpdatedColumn(column);
        assertTrue(
                column.getAssignedTerms() == null || column.getAssignedTerms().isEmpty());
    }

    @Test(
            groups = {"delete.column"},
            dependsOnGroups = {"update.*", "search.*"})
    void deleteColumn() throws AtlanException {
        EntityMutationResponse response = Entity.delete(columnGuid5);
        assertNotNull(response);
        assertTrue(response.getCreatedEntities().isEmpty());
        assertTrue(response.getUpdatedEntities().isEmpty());
        assertEquals(response.getDeletedEntities().size(), 1);
        Entity one = response.getDeletedEntities().get(0);
        assertTrue(one instanceof Column);
        Column column = (Column) one;
        validateUpdatedColumn(column);
        assertEquals(column.getDeleteHandler(), "SOFT");
        assertEquals(column.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"delete.column.read"},
            dependsOnGroups = {"delete.column"})
    void readDeletedColumn() throws AtlanException {
        Column deleted = Column.retrieveByGuid(columnGuid5);
        validateUpdatedColumn(deleted);
        assertEquals(deleted.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"delete.column.restore"},
            dependsOnGroups = {"delete.column.read"})
    void restoreColumn() throws AtlanException {
        assertTrue(Column.restore(columnQame5));
        Column restored = Column.retrieveByGuid(columnGuid5);
        validateUpdatedColumn(restored);
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"purge.column"},
            dependsOnGroups = {"delete.column.restore"})
    void purgeColumn() throws AtlanException {
        EntityMutationResponse response = Entity.purge(columnGuid5);
        assertNotNull(response);
        assertTrue(response.getCreatedEntities().isEmpty());
        assertTrue(response.getUpdatedEntities().isEmpty());
        assertEquals(response.getDeletedEntities().size(), 1);
        Entity one = response.getDeletedEntities().get(0);
        assertTrue(one instanceof Column);
        Column column = (Column) one;
        validateUpdatedColumn(column);
        assertEquals(column.getDeleteHandler(), "HARD");
        assertEquals(column.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"purge.column.read"},
            dependsOnGroups = {"purge.column"})
    void readPurgedColumn() {
        assertThrows(NotFoundException.class, () -> Column.retrieveByGuid(columnGuid5));
    }

    @Test(
            groups = {"purge.connection"},
            dependsOnGroups = {"create.*", "read.*", "search.*", "update.*", "purge.column*"},
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(connectionQame, log);
    }

    @Test(
            groups = {"purge.groups"},
            dependsOnGroups = {"purge.connection"},
            alwaysRun = true)
    void purgeGroups() throws AtlanException {
        AtlanGroup.delete(ownerGroup.getId());
    }

    @Test(
            groups = {"purge.classifications"},
            dependsOnGroups = {"purge.connection"},
            alwaysRun = true)
    void purgeClassifications() throws AtlanException {
        ClassificationTest.deleteClassification(CLASSIFICATION_NAME1);
        ClassificationTest.deleteClassification(CLASSIFICATION_NAME2);
    }

    @Test(
            groups = {"purge.glossary"},
            dependsOnGroups = {"purge.connection"},
            alwaysRun = true)
    void purgeGlossary() throws AtlanException {
        GlossaryTest.deleteTerm(term1.getGuid());
        GlossaryTest.deleteTerm(term2.getGuid());
        GlossaryTest.deleteGlossary(glossary.getGuid());
    }

    private void validateUpdatedColumn(Column column) {
        assertNotNull(column);
        assertEquals(column.getGuid(), columnGuid5);
        if (column.getQualifiedName() != null) {
            assertEquals(column.getQualifiedName(), columnQame5);
        }
    }

    private void validateCompleteColumn(Column column) {
        validateUpdatedColumn(column);
        assertTrue(column.isComplete());
    }
}
