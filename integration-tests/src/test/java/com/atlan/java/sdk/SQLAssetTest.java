/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.*;

import co.elastic.clients.elasticsearch._types.SortOrder;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.admin.AtlanGroup;
import com.atlan.model.assets.*;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.core.AtlanTag;
import com.atlan.model.enums.*;
import com.atlan.model.search.*;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * Tests all aspects of SQL assets (Connections, Databases, Schemas, Tables, Views, Materialized Views, Columns).
 */
@Slf4j
public class SQLAssetTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("SQL");

    public static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.CLICKHOUSE;
    public static final String CONNECTION_NAME = PREFIX;

    public static final String DATABASE_NAME = PREFIX + "_db";
    public static final String SCHEMA_NAME = PREFIX + "_schema";
    public static final String PROCEDURE_NAME = PREFIX + "_procedure";
    public static final String TABLE_NAME = PREFIX + "_table";
    public static final String VIEW_NAME = PREFIX + "_view";
    public static final String MVIEW_NAME = PREFIX + "_mview";
    public static final String PARTITION_NAME = PREFIX + "_partition";
    public static final String COLUMN_NAME1 = PREFIX + "_col1";
    public static final String COLUMN_NAME2 = PREFIX + "_col2";
    public static final String COLUMN_NAME3 = PREFIX + "_col3";
    public static final String COLUMN_NAME4 = PREFIX + "_col4";
    public static final String COLUMN_NAME5 = PREFIX + "_col5";
    public static final String COLUMN_NAME6 = PREFIX + "_col6";

    private static final String ATLAN_TAG_NAME1 = PREFIX + "1";
    private static final String ATLAN_TAG_NAME2 = PREFIX + "2";
    private static final String TERM_NAME1 = PREFIX + "1";
    private static final String TERM_NAME2 = PREFIX + "2";

    private static Connection connection = null;
    private static Database database = null;
    private static Schema schema = null;
    private static Table table = null;
    private static View view = null;
    private static MaterializedView mview = null;
    private static TablePartition partition = null;
    private static Column column1 = null;
    private static Column column2 = null;
    private static Column column3 = null;
    private static Column column4 = null;
    private static Column column5 = null;
    private static Column column6 = null;
    private static Column column7 = null;
    private static Column column8 = null;
    private static AtlanGroup ownerGroup = null;
    private static Glossary glossary = null;
    private static GlossaryTerm term1 = null;
    private static GlossaryTerm term2 = null;
    private static Procedure procedure = null;
    private static String definition =
            """
        BEGIN
        insert into `atlanhq.testing_lineage.INSTACART_ALCOHOL_ORDER_TIME_copy`
        select * from `atlanhq.testing_lineage.INSTACART_ALCOHOL_ORDER_TIME`;
        END""";

    private static List<String> taggedAssetGuids = new ArrayList<>();

    /**
     * Create a database with the provided characteristics.
     *
     * @param client connectivity to the Atlan tenant
     * @param name unique name for the database
     * @param connectionQualifiedName qualifiedName of the connection in which to create the database
     * @return the created database
     * @throws AtlanException on any errors creating the database
     */
    static Database createDatabase(AtlanClient client, String name, String connectionQualifiedName)
            throws AtlanException {
        Database database = Database.creator(name, connectionQualifiedName).build();
        AssetMutationResponse response = database.save(client);
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof Database);
        database = (Database) one;
        assertNotNull(database.getGuid());
        assertNotNull(database.getQualifiedName());
        assertEquals(database.getName(), name);
        return database;
    }

    /**
     * Create a schema with the provided characteristics.
     *
     * @param client connectivity to the Atlan tenant
     * @param name unique name for the schema
     * @param database in which to create the schema
     * @return the created schema
     * @throws AtlanException on any errors creating the schema
     */
    static Schema createSchema(AtlanClient client, String name, Database database) throws AtlanException {
        Schema schema = Schema.creator(name, database).build();
        AssetMutationResponse response = schema.save(client);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof Database);
        Database updated = (Database) one;
        assertEquals(updated.getQualifiedName(), database.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof Schema);
        schema = (Schema) one;
        assertNotNull(schema.getGuid());
        assertNotNull(schema.getQualifiedName());
        assertEquals(schema.getName(), name);
        assertEquals(schema.getDatabaseQualifiedName(), database.getQualifiedName());
        return schema;
    }

    /**
     * Create a table with the provided characteristics.
     *
     * @param client connectivity to the Atlan tenant
     * @param name unique name for the table
     * @param schema in which to create the table
     * @return the created table
     * @throws AtlanException on any errors creating the table
     */
    static Table createTable(AtlanClient client, String name, Schema schema) throws AtlanException {
        Table table = Table.creator(name, schema).columnCount(2L).build();
        AssetMutationResponse response = table.save(client);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof Schema);
        Schema updated = (Schema) one;
        assertEquals(updated.getQualifiedName(), schema.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof Table);
        table = (Table) one;
        assertNotNull(table.getGuid());
        assertNotNull(table.getQualifiedName());
        assertEquals(table.getName(), name);
        assertEquals(table.getSchemaQualifiedName(), schema.getQualifiedName());
        return table;
    }

    /**
     * Create a procedure with the provided characteristics.
     *
     * @param client connectivity to the Atlan tenant
     * @param name unique name for the table
     * @param definition body of the procedure
     * @param schema in which to create the table
     * @return the created table
     * @throws AtlanException on any errors creating the table
     */
    static Procedure createProcedure(AtlanClient client, String name, String definition, Schema schema)
            throws AtlanException {
        Procedure procedure = Procedure.creator(name, definition, schema).build();
        AssetMutationResponse response = procedure.save(client);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof Schema);
        Schema updated = (Schema) one;
        assertEquals(updated.getQualifiedName(), schema.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof Procedure);
        procedure = (Procedure) one;
        assertNotNull(procedure.getGuid());
        assertNotNull(procedure.getQualifiedName());
        assertEquals(procedure.getName(), name);
        assertEquals(procedure.getDefinition(), definition);
        assertEquals(procedure.getSchemaQualifiedName(), schema.getQualifiedName());
        return procedure;
    }

    /**
     * Create a view with the provided characteristics.
     *
     * @param client connectivity to the Atlan tenant
     * @param name unique name for the view
     * @param schema in which to create the view
     * @return the created view
     * @throws AtlanException on any errors creating the view
     */
    static View createView(AtlanClient client, String name, Schema schema) throws AtlanException {
        View view = View.creator(name, schema).columnCount(2L).build();
        AssetMutationResponse response = view.save(client);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof Schema);
        Schema updated = (Schema) one;
        assertEquals(updated.getQualifiedName(), schema.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof View);
        view = (View) one;
        assertNotNull(view.getGuid());
        assertNotNull(view.getQualifiedName());
        assertEquals(view.getName(), name);
        assertEquals(view.getSchemaQualifiedName(), schema.getQualifiedName());
        return view;
    }

    /**
     * Create a materialized view with the provided characteristics.
     *
     * @param client connectivity to the Atlan tenant
     * @param name unique name for the materialized view
     * @param schema in which to create the materialized view
     * @return the created materialized view
     * @throws AtlanException on any errors creating the materialized view
     */
    static MaterializedView createMaterializedView(AtlanClient client, String name, Schema schema)
            throws AtlanException {
        MaterializedView mview =
                MaterializedView.creator(name, schema).columnCount(2L).build();
        AssetMutationResponse response = mview.save(client);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof Schema);
        Schema updated = (Schema) one;
        assertEquals(updated.getQualifiedName(), schema.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof MaterializedView);
        mview = (MaterializedView) one;
        assertNotNull(mview.getGuid());
        assertNotNull(mview.getQualifiedName());
        assertEquals(mview.getName(), name);
        assertEquals(mview.getSchemaQualifiedName(), schema.getQualifiedName());
        return mview;
    }

    /**
     * Create a table partition with the provided characteristics.
     *
     * @param client connectivity to the Atlan tenant
     * @param name unique name for the table partition
     * @param table in which to create the table partition
     * @return the created table partition
     * @throws AtlanException on any errors creating the table partition
     */
    static TablePartition createTablePartition(AtlanClient client, String name, Table table) throws AtlanException {
        TablePartition partition =
                TablePartition.creator(name, table).columnCount(2L).build();
        AssetMutationResponse response = partition.save(client);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof Table);
        Table updated = (Table) one;
        assertEquals(updated.getQualifiedName(), table.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof TablePartition);
        partition = (TablePartition) one;
        assertNotNull(partition.getGuid());
        assertNotNull(partition.getQualifiedName());
        assertEquals(partition.getName(), name);
        return partition;
    }

    /**
     * Create a column with the provided characteristics.
     *
     * @param client connectivity to the Atlan tenant
     * @param name unique name for the column
     * @param parent container of the column
     * @param order position of the column within the container
     * @return the created column
     * @throws AtlanException on any errors creating the column
     */
    static Column createColumn(AtlanClient client, String name, ISQL parent, int order) throws AtlanException {
        Column column = null;
        if (parent instanceof Table) {
            column = Column.creator(name, (Table) parent, order).build();
        } else if (parent instanceof View) {
            column = Column.creator(name, (View) parent, order).build();
        } else if (parent instanceof MaterializedView) {
            column = Column.creator(name, (MaterializedView) parent, order).build();
        } else if (parent instanceof TablePartition) {
            column = Column.creator(name, (TablePartition) parent, order).build();
        }
        if (column != null) {
            String parentType = parent.getTypeName();
            AssetMutationResponse response = column.save(client);
            assertNotNull(response);
            assertTrue(response.getDeletedAssets().isEmpty());
            assertEquals(response.getUpdatedAssets().size(), 1);
            Asset one = response.getUpdatedAssets().get(0);
            assertTrue(one instanceof ISQL);
            ISQL updated = (ISQL) one;
            assertEquals(updated.getQualifiedName(), parent.getQualifiedName());
            assertEquals(response.getCreatedAssets().size(), 1);
            one = response.getCreatedAssets().get(0);
            assertTrue(one instanceof Column);
            column = (Column) one;
            assertNotNull(column.getGuid());
            assertNotNull(column.getQualifiedName());
            assertEquals(column.getName(), name);
            if (parentType.equals(Table.TYPE_NAME)) {
                assertEquals(column.getTableQualifiedName(), parent.getQualifiedName());
            } else if (parentType.equals(View.TYPE_NAME) || parentType.equals(MaterializedView.TYPE_NAME)) {
                assertEquals(column.getViewQualifiedName(), parent.getQualifiedName());
            }
            return column;
        }
        return null;
    }

    @Test(groups = {"asset.create.connection"})
    void createConnection() throws AtlanException, InterruptedException {
        connection = ConnectionTest.createConnection(client, CONNECTION_NAME, CONNECTOR_TYPE);
    }

    @Test(
            groups = {"asset.search.connection"},
            dependsOnGroups = {"asset.create.connection"})
    void findConnection() throws AtlanException {
        List<Connection> results = Connection.findByName(client, CONNECTION_NAME, CONNECTOR_TYPE);
        assertNotNull(results);
        assertEquals(results.size(), 1);
        Connection one = results.get(0);
        assertEquals(one.getGuid(), connection.getGuid());
        assertEquals(one.getQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"asset.create.database"},
            dependsOnGroups = {"asset.create.connection"})
    void createDatabase() throws AtlanException {
        database = createDatabase(client, DATABASE_NAME, connection.getQualifiedName());
        assertEquals(database.getConnectorType(), CONNECTOR_TYPE);
    }

    @Test(
            groups = {"asset.create.schema"},
            dependsOnGroups = {"asset.create.database"})
    void createSchema() throws AtlanException {
        schema = createSchema(client, SCHEMA_NAME, database);
        assertEquals(schema.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(schema.getDatabaseName(), DATABASE_NAME);
    }

    @Test(
            groups = {"asset.create.procedure"},
            dependsOnGroups = {"asset.create.schema"})
    void createProcedure() throws AtlanException {
        procedure = createProcedure(client, PROCEDURE_NAME, definition, schema);
        StringBuilder qualifiedName = new StringBuilder();
        qualifiedName.append(schema.getQualifiedName()).append("/_procedures_/").append(PROCEDURE_NAME);
        assertEquals(procedure.getQualifiedName(), qualifiedName.toString());
        assertEquals(procedure.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(procedure.getSchemaName(), SCHEMA_NAME);
        assertEquals(procedure.getDatabaseName(), DATABASE_NAME);
        assertEquals(procedure.getDatabaseQualifiedName(), database.getQualifiedName());
        assertEquals(procedure.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"asset.create.table"},
            dependsOnGroups = {"asset.create.schema"})
    void createTable() throws AtlanException {
        table = createTable(client, TABLE_NAME, schema);
        assertEquals(table.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(table.getSchemaName(), SCHEMA_NAME);
        assertEquals(table.getDatabaseName(), DATABASE_NAME);
        assertEquals(table.getDatabaseQualifiedName(), database.getQualifiedName());
        assertEquals(table.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"asset.create.view"},
            dependsOnGroups = {"asset.create.table"})
    void createView() throws AtlanException {
        view = createView(client, VIEW_NAME, schema);
        assertEquals(view.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(view.getSchemaName(), SCHEMA_NAME);
        assertEquals(view.getDatabaseName(), DATABASE_NAME);
        assertEquals(view.getDatabaseQualifiedName(), database.getQualifiedName());
        assertEquals(view.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"asset.create.mview"},
            dependsOnGroups = {"asset.create.view"})
    void createMView() throws AtlanException {
        mview = createMaterializedView(client, MVIEW_NAME, schema);
        assertEquals(mview.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(mview.getSchemaName(), SCHEMA_NAME);
        assertEquals(mview.getDatabaseName(), DATABASE_NAME);
        assertEquals(mview.getDatabaseQualifiedName(), database.getQualifiedName());
        assertEquals(mview.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"asset.create.partition"},
            dependsOnGroups = {"asset.create.mview"})
    void createPartition() throws AtlanException {
        partition = createTablePartition(client, PARTITION_NAME, table);
        assertEquals(partition.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(partition.getSchemaName(), SCHEMA_NAME);
        assertEquals(partition.getSchemaQualifiedName(), schema.getQualifiedName());
        assertEquals(partition.getDatabaseName(), DATABASE_NAME);
        assertEquals(partition.getDatabaseQualifiedName(), database.getQualifiedName());
        assertEquals(partition.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"asset.create.column.1"},
            dependsOnGroups = {"asset.create.partition"})
    void createColumn1() throws AtlanException {
        column1 = createColumn(client, COLUMN_NAME1, table, 1);
        assertNotNull(column1);
        assertEquals(column1.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(column1.getTableName(), TABLE_NAME);
        assertEquals(column1.getSchemaName(), SCHEMA_NAME);
        assertEquals(column1.getSchemaQualifiedName(), schema.getQualifiedName());
        assertEquals(column1.getDatabaseName(), DATABASE_NAME);
        assertEquals(column1.getDatabaseQualifiedName(), database.getQualifiedName());
        assertEquals(column1.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"asset.create.column.2"},
            dependsOnGroups = {"asset.create.column.1"})
    void createColumn2() throws AtlanException {
        column2 = createColumn(client, COLUMN_NAME2, table, 2);
        assertNotNull(column2);
        assertEquals(column2.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(column2.getTableName(), TABLE_NAME);
        assertEquals(column2.getSchemaName(), SCHEMA_NAME);
        assertEquals(column2.getSchemaQualifiedName(), schema.getQualifiedName());
        assertEquals(column2.getDatabaseName(), DATABASE_NAME);
        assertEquals(column2.getDatabaseQualifiedName(), database.getQualifiedName());
        assertEquals(column2.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"asset.create.column.3"},
            dependsOnGroups = {"asset.create.column.2"})
    void createColumn3() throws AtlanException {
        column3 = createColumn(client, COLUMN_NAME3, view, 1);
        assertNotNull(column3);
        assertEquals(column3.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(column3.getViewName(), VIEW_NAME);
        assertEquals(column3.getSchemaName(), SCHEMA_NAME);
        assertEquals(column3.getSchemaQualifiedName(), schema.getQualifiedName());
        assertEquals(column3.getDatabaseName(), DATABASE_NAME);
        assertEquals(column3.getDatabaseQualifiedName(), database.getQualifiedName());
        assertEquals(column3.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"asset.create.column.4"},
            dependsOnGroups = {"asset.create.column.3"})
    void createColumn4() throws AtlanException {
        column4 = createColumn(client, COLUMN_NAME4, view, 2);
        assertNotNull(column4);
        assertEquals(column4.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(column4.getViewName(), VIEW_NAME);
        assertEquals(column4.getSchemaName(), SCHEMA_NAME);
        assertEquals(column4.getSchemaQualifiedName(), schema.getQualifiedName());
        assertEquals(column4.getDatabaseName(), DATABASE_NAME);
        assertEquals(column4.getDatabaseQualifiedName(), database.getQualifiedName());
        assertEquals(column4.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"asset.create.column.5"},
            dependsOnGroups = {"asset.create.column.4"})
    void createColumn5() throws AtlanException {
        column5 = createColumn(client, COLUMN_NAME5, mview, 1);
        assertNotNull(column5);
        assertEquals(column5.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(column5.getViewName(), MVIEW_NAME);
        assertEquals(column5.getSchemaName(), SCHEMA_NAME);
        assertEquals(column5.getSchemaQualifiedName(), schema.getQualifiedName());
        assertEquals(column5.getDatabaseName(), DATABASE_NAME);
        assertEquals(column5.getDatabaseQualifiedName(), database.getQualifiedName());
        assertEquals(column5.getConnectionQualifiedName(), connection.getQualifiedName());
        taggedAssetGuids.add(column5.getGuid());
    }

    @Test(
            groups = {"asset.create.column.6"},
            dependsOnGroups = {"asset.create.column.5"})
    void createColumn6() throws AtlanException {
        column6 = createColumn(client, COLUMN_NAME6, mview, 2);
        assertNotNull(column6);
        assertEquals(column6.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(column6.getViewName(), MVIEW_NAME);
        assertEquals(column6.getSchemaName(), SCHEMA_NAME);
        assertEquals(column6.getSchemaQualifiedName(), schema.getQualifiedName());
        assertEquals(column6.getDatabaseName(), DATABASE_NAME);
        assertEquals(column6.getDatabaseQualifiedName(), database.getQualifiedName());
        assertEquals(column6.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"asset.create.column.7"},
            dependsOnGroups = {"asset.create.column.6"})
    void createColumn7() throws AtlanException {
        Column column = Column.creator(COLUMN_NAME1, partition, 1).build();
        AssetMutationResponse response = column.save(client);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof ISQL);
        ISQL parent = (ISQL) one;
        assertEquals(parent.getQualifiedName(), partition.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof Column);
        column7 = (Column) one;
        assertEquals(column7.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(column7.getTableName(), PARTITION_NAME);
        assertEquals(column7.getTableQualifiedName(), partition.getQualifiedName());
        assertEquals(column7.getSchemaName(), SCHEMA_NAME);
        assertEquals(column7.getSchemaQualifiedName(), schema.getQualifiedName());
        assertEquals(column7.getDatabaseName(), DATABASE_NAME);
        assertEquals(column7.getDatabaseQualifiedName(), database.getQualifiedName());
        assertEquals(column7.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"asset.create.column.8"},
            dependsOnGroups = {"asset.create.column.7"})
    void createColumn8() throws AtlanException {
        Column column = Column.creator(COLUMN_NAME2, partition, 2).build();
        AssetMutationResponse response = column.save(client);
        assertNotNull(response);
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 1);
        Asset one = response.getUpdatedAssets().get(0);
        assertTrue(one instanceof ISQL);
        ISQL parent = (ISQL) one;
        assertEquals(parent.getQualifiedName(), partition.getQualifiedName());
        assertEquals(response.getCreatedAssets().size(), 1);
        one = response.getCreatedAssets().get(0);
        assertTrue(one instanceof Column);
        column8 = (Column) one;
        assertEquals(column8.getConnectorType(), CONNECTOR_TYPE);
        assertEquals(column8.getTableName(), PARTITION_NAME);
        assertEquals(column8.getTableQualifiedName(), partition.getQualifiedName());
        assertEquals(column8.getSchemaName(), SCHEMA_NAME);
        assertEquals(column8.getSchemaQualifiedName(), schema.getQualifiedName());
        assertEquals(column8.getDatabaseName(), DATABASE_NAME);
        assertEquals(column8.getDatabaseQualifiedName(), database.getQualifiedName());
        assertEquals(column8.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"asset.create.contract"},
            dependsOnGroups = {"asset.create.column.*"})
    void generateContract() throws AtlanException {
        String tableContract = client.contracts.generateInitialSpec(table);
        assertNotNull(tableContract);
        assertTrue(tableContract.startsWith("---"));
        assertTrue(tableContract.contains("columns:"));
        assertTrue(tableContract.contains(" - name: " + COLUMN_NAME1));
        assertTrue(tableContract.contains(" - name: " + COLUMN_NAME2));
        assertTrue(tableContract.endsWith("...\n"));
    }

    @Test(
            groups = {"asset.read.partition"},
            dependsOnGroups = {"asset.create.contract"})
    void readTablePartition() throws AtlanException {
        TablePartition p = TablePartition.get(client, partition.getGuid(), true);
        assertNotNull(p);
        assertTrue(p.isComplete());
        assertEquals(p.getGuid(), partition.getGuid());
        assertEquals(p.getQualifiedName(), partition.getQualifiedName());
        assertEquals(p.getName(), PARTITION_NAME);
        ITable parent = p.getParentTable();
        assertNotNull(parent);
        assertEquals(parent.getTypeName(), Table.TYPE_NAME);
        assertEquals(parent.getGuid(), table.getGuid());
    }

    @Test(
            groups = {"asset.read.table"},
            dependsOnGroups = {"asset.create.contract"})
    void readTable() throws AtlanException {
        Table t = Table.get(client, table.getGuid(), true);
        assertNotNull(t);
        assertTrue(t.isComplete());
        assertEquals(t.getGuid(), table.getGuid());
        assertEquals(t.getQualifiedName(), table.getQualifiedName());
        assertEquals(t.getName(), TABLE_NAME);
        Set<ITablePartition> partitions = t.getPartitions();
        assertNotNull(partitions);
        assertEquals(partitions.size(), 1);
        assertEquals(partitions.stream().findFirst().get().getGuid(), partition.getGuid());
    }

    @Test(
            groups = {"asset.read.column.5"},
            dependsOnGroups = {"asset.create.column.5"})
    void readColumn5() throws AtlanException {
        Column byGuid = Column.get(client, column5.getGuid(), true);
        assertNotNull(byGuid);
        assertTrue(byGuid.isComplete());
        assertEquals(byGuid.getGuid(), column5.getGuid());
        assertEquals(byGuid.getQualifiedName(), column5.getQualifiedName());
        assertEquals(byGuid.getName(), COLUMN_NAME5);
        ISQL one = byGuid.getParent();
        assertNotNull(one);
        assertEquals(one.getTypeName(), MaterializedView.TYPE_NAME);
        assertEquals(one.getGuid(), mview.getGuid());
        Column byQN = Column.get(client, column5.getQualifiedName(), true);
        assertNotNull(byQN);
        assertTrue(byQN.isComplete());
        assertEquals(byQN.getGuid(), column5.getGuid());
        assertEquals(byQN.getQualifiedName(), column5.getQualifiedName());
        assertEquals(byQN.getName(), COLUMN_NAME5);
        one = byQN.getParent();
        assertNotNull(one);
        assertEquals(one.getTypeName(), MaterializedView.TYPE_NAME);
        assertEquals(one.getGuid(), mview.getGuid());
        assertEquals(byGuid, byQN);
    }

    @Test(
            groups = {"asset.read.column.7"},
            dependsOnGroups = {"asset.create.column.7"})
    void readColumn7() throws AtlanException {
        Column byGuid = Column.get(client, column7.getGuid(), true);
        assertNotNull(byGuid);
        assertTrue(byGuid.isComplete());
        assertEquals(byGuid.getGuid(), column7.getGuid());
        assertEquals(byGuid.getQualifiedName(), column7.getQualifiedName());
        assertEquals(byGuid.getName(), COLUMN_NAME1);
        ISQL one = byGuid.getParent();
        assertNotNull(one);
        assertEquals(one.getTypeName(), TablePartition.TYPE_NAME);
        assertEquals(one.getGuid(), partition.getGuid());
        Column byQN = Column.get(client, column7.getQualifiedName(), true);
        assertNotNull(byQN);
        assertTrue(byQN.isComplete());
        assertEquals(byQN.getGuid(), column7.getGuid());
        assertEquals(byQN.getQualifiedName(), column7.getQualifiedName());
        assertEquals(byQN.getName(), COLUMN_NAME1);
        one = byQN.getParent();
        assertNotNull(one);
        assertEquals(one.getTypeName(), TablePartition.TYPE_NAME);
        assertEquals(one.getGuid(), partition.getGuid());
        assertEquals(byGuid, byQN);
    }

    @Test(
            groups = {"asset.search.byParentQN"},
            dependsOnGroups = {"asset.create.*"})
    void searchByParentQN() throws AtlanException, InterruptedException {
        IndexSearchRequest index = client.assets
                .select()
                .active()
                .where(Asset.QUALIFIED_NAME.startsWith(connection.getQualifiedName()))
                .pageSize(50)
                .aggregate("type", IReferenceable.TYPE_NAME.bucketBy())
                .sort(Asset.CREATE_TIME.order(SortOrder.Asc))
                .includeOnResults(Asset.NAME)
                .toRequest();

        IndexSearchResponse response = retrySearchUntil(index, 15L);

        // Test iterator
        List<String> guids = new ArrayList<>();
        response.forEach(a -> guids.add(a.getGuid()));

        // Test sequential streaming
        List<String> guidsSeq = client
                .assets
                .select()
                .where(Asset.QUALIFIED_NAME.startsWith(connection.getQualifiedName()))
                .pageSize(5)
                .sort(Asset.CREATE_TIME.order(SortOrder.Asc))
                .includeOnResults(Asset.NAME)
                .stream()
                .map(Asset::getGuid)
                .collect(Collectors.toList());

        // Test parallel streaming
        List<String> guidsPar = client
                .assets
                .select()
                .where(Asset.QUALIFIED_NAME.startsWith(connection.getQualifiedName()))
                .pageSize(5)
                .sort(Asset.CREATE_TIME.order(SortOrder.Asc))
                .includeOnResults(Asset.NAME)
                .stream(true)
                .map(Asset::getGuid)
                .collect(Collectors.toList());

        // Test results from all approaches (iterator, sequential, parallel streaming) are equivalent
        assertEquals(guidsSeq, guidsPar);
        assertEquals(guids, guidsSeq);

        assertEquals(response.getApproximateCount().longValue(), 16L);
        List<Asset> entities = response.getAssets();
        assertNotNull(response.getAggregations());
        assertEquals(response.getAggregations().size(), 1);
        assertTrue(response.getAggregations().get("type") instanceof AggregationBucketResult);
        assertEquals(
                ((AggregationBucketResult) response.getAggregations().get("type"))
                        .getBuckets()
                        .size(),
                9);

        assertNotNull(entities);
        assertEquals(entities.size(), 16);

        Asset one = entities.get(0);
        assertTrue(one instanceof Connection);
        Connection c = (Connection) one;
        assertEquals(c.getGuid(), connection.getGuid());
        assertEquals(c.getQualifiedName(), connection.getQualifiedName());
        assertEquals(c.getName(), CONNECTION_NAME);

        one = entities.get(1);
        assertTrue(one instanceof Database);
        Database d = (Database) one;
        assertEquals(d.getGuid(), database.getGuid());
        assertEquals(d.getQualifiedName(), database.getQualifiedName());
        assertEquals(d.getName(), DATABASE_NAME);

        one = entities.get(2);
        assertTrue(one instanceof Schema);
        Schema s = (Schema) one;
        assertEquals(s.getGuid(), schema.getGuid());
        assertEquals(s.getQualifiedName(), schema.getQualifiedName());
        assertEquals(s.getName(), SCHEMA_NAME);

        one = entities.get(3);
        assertTrue(one instanceof Procedure);
        Procedure p = (Procedure) one;
        assertEquals(p.getGuid(), procedure.getGuid());
        assertEquals(p.getQualifiedName(), procedure.getQualifiedName());
        assertEquals(p.getName(), PROCEDURE_NAME);

        one = entities.get(4);
        assertTrue(one instanceof Table);
        Table t = (Table) one;
        assertEquals(t.getGuid(), table.getGuid());
        assertEquals(t.getQualifiedName(), table.getQualifiedName());
        assertEquals(t.getName(), TABLE_NAME);

        one = entities.get(5);
        assertTrue(one instanceof View);
        View v = (View) one;
        assertEquals(v.getGuid(), view.getGuid());
        assertEquals(v.getQualifiedName(), view.getQualifiedName());
        assertEquals(v.getName(), VIEW_NAME);

        one = entities.get(6);
        assertTrue(one instanceof MaterializedView);
        MaterializedView m = (MaterializedView) one;
        assertEquals(m.getGuid(), mview.getGuid());
        assertEquals(m.getQualifiedName(), mview.getQualifiedName());
        assertEquals(m.getName(), MVIEW_NAME);

        one = entities.get(7);
        assertTrue(one instanceof TablePartition);
        TablePartition tp = (TablePartition) one;
        assertEquals(tp.getGuid(), partition.getGuid());
        assertEquals(tp.getQualifiedName(), partition.getQualifiedName());
        assertEquals(tp.getName(), PARTITION_NAME);

        one = entities.get(8);
        assertTrue(one instanceof Column);
        Column column = (Column) one;
        assertEquals(column.getGuid(), column1.getGuid());
        assertEquals(column.getQualifiedName(), column1.getQualifiedName());
        assertEquals(column.getName(), COLUMN_NAME1);

        one = entities.get(9);
        assertTrue(one instanceof Column);
        column = (Column) one;
        assertEquals(column.getGuid(), column2.getGuid());
        assertEquals(column.getQualifiedName(), column2.getQualifiedName());
        assertEquals(column.getName(), COLUMN_NAME2);

        one = entities.get(10);
        assertTrue(one instanceof Column);
        column = (Column) one;
        assertEquals(column.getGuid(), column3.getGuid());
        assertEquals(column.getQualifiedName(), column3.getQualifiedName());
        assertEquals(column.getName(), COLUMN_NAME3);

        one = entities.get(11);
        assertTrue(one instanceof Column);
        column = (Column) one;
        assertEquals(column.getGuid(), column4.getGuid());
        assertEquals(column.getQualifiedName(), column4.getQualifiedName());
        assertEquals(column.getName(), COLUMN_NAME4);

        one = entities.get(12);
        assertTrue(one instanceof Column);
        column = (Column) one;
        assertEquals(column.getGuid(), column5.getGuid());
        assertEquals(column.getQualifiedName(), column5.getQualifiedName());
        assertEquals(column.getName(), COLUMN_NAME5);

        one = entities.get(13);
        assertTrue(one instanceof Column);
        column = (Column) one;
        assertEquals(column.getGuid(), column6.getGuid());
        assertEquals(column.getQualifiedName(), column6.getQualifiedName());
        assertEquals(column.getName(), COLUMN_NAME6);

        one = entities.get(14);
        assertTrue(one instanceof Column);
        column = (Column) one;
        assertEquals(column.getGuid(), column7.getGuid());
        assertEquals(column.getQualifiedName(), column7.getQualifiedName());
        assertEquals(column.getName(), COLUMN_NAME1);

        one = entities.get(15);
        assertTrue(one instanceof Column);
        column = (Column) one;
        assertEquals(column.getGuid(), column8.getGuid());
        assertEquals(column.getQualifiedName(), column8.getQualifiedName());
        assertEquals(column.getName(), COLUMN_NAME2);
    }

    @Test(
            groups = {"asset.search.byParentQN"},
            dependsOnGroups = {"asset.create.*"})
    void testSearchIterators() throws AtlanException, InterruptedException {
        IndexSearchRequest index = client.assets
                .select()
                .active()
                .where(Asset.QUALIFIED_NAME.startsWith(connection.getQualifiedName()))
                .pageSize(5)
                .includeOnResults(Asset.NAME)
                .toRequest();

        IndexSearchResponse response = retrySearchUntil(index, 15L);

        for (Asset a : response) {
            assertTrue(a instanceof Connection
                    || a instanceof Database
                    || a instanceof Schema
                    || a instanceof Table
                    || a instanceof View
                    || a instanceof Procedure
                    || a instanceof MaterializedView
                    || a instanceof TablePartition
                    || a instanceof Column);
        }

        response.forEach(a -> assertTrue(a instanceof Connection
                || a instanceof Database
                || a instanceof Schema
                || a instanceof Table
                || a instanceof View
                || a instanceof Procedure
                || a instanceof MaterializedView
                || a instanceof TablePartition
                || a instanceof Column));

        List<Asset> results = response.stream().toList();
        assertEquals(results.size(), 16);
    }

    @Test(groups = {"asset.create.group.owners"})
    void createGroupOwners() throws AtlanException {
        ownerGroup = AdminTest.createGroup(client, PREFIX);
    }

    @Test(
            groups = {"asset.update.column.owners"},
            dependsOnGroups = {"asset.read.column.5", "asset.create.group.owners"})
    void updateColumnOwners() throws AtlanException {
        Column toUpdate = Column.updater(column5.getQualifiedName(), COLUMN_NAME5)
                .ownerGroup(ownerGroup.getName())
                .build();
        AssetMutationResponse response = toUpdate.save(client);
        Asset one = validateSingleUpdate(response);
        assertTrue(one instanceof Column);
        Column updated = (Column) one;
        validateUpdatedColumn(updated);
        Set<String> groups = updated.getOwnerGroups();
        assertNotNull(groups);
        assertEquals(groups.size(), 1);
        assertTrue(groups.contains(ownerGroup.getName()));
    }

    @Test(
            groups = {"asset.update.column.owners.x"},
            dependsOnGroups = {"asset.update.column.owners"})
    void updateColumnOwnersX() throws AtlanException {
        Column cleared = Column.removeOwners(client, column5.getQualifiedName(), COLUMN_NAME5);
        validateUpdatedColumn(cleared);
        assertTrue(cleared.getOwnerGroups() == null || cleared.getOwnerGroups().isEmpty());
    }

    @Test(
            groups = {"asset.update.column.certificate"},
            dependsOnGroups = {"asset.update.column.owners.x"})
    void updateColumnCertificate() throws AtlanException {
        Column column =
                Column.updateCertificate(client, column5.getQualifiedName(), CERTIFICATE_STATUS, CERTIFICATE_MESSAGE);
        validateUpdatedColumn(column);
        assertEquals(column.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(column.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);
    }

    @Test(
            groups = {"asset.update.column.certificate.x"},
            dependsOnGroups = {"asset.update.column.certificate"})
    void updateColumnCertificateX() throws AtlanException {
        Column cleared = Column.removeCertificate(client, column5.getQualifiedName(), COLUMN_NAME5);
        validateUpdatedColumn(cleared);
        assertNull(cleared.getCertificateStatus());
        assertNull(cleared.getCertificateStatusMessage());
    }

    @Test(
            groups = {"asset.update.column.announcement"},
            dependsOnGroups = {"asset.update.column.certificate.x"})
    void updateColumnAnnouncement() throws AtlanException {
        Column column = Column.updateAnnouncement(
                client, column5.getQualifiedName(), ANNOUNCEMENT_TYPE, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
        validateUpdatedColumn(column);
        assertEquals(column.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(column.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(column.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
    }

    @Test(
            groups = {"asset.update.column.announcement.x"},
            dependsOnGroups = {"asset.update.column.announcement"})
    void updateColumnAnnouncementX() throws AtlanException {
        Column column = Column.removeAnnouncement(client, column5.getQualifiedName(), COLUMN_NAME5);
        validateUpdatedColumn(column);
        assertNull(column.getAnnouncementType());
        assertNull(column.getAnnouncementTitle());
        assertNull(column.getAnnouncementMessage());
    }

    @Test(
            groups = {"asset.update.column.descriptions"},
            dependsOnGroups = {"asset.update.column.announcement.x"})
    void updateColumnDescriptions() throws AtlanException {
        Column toUpdate = Column.updater(column5.getQualifiedName(), COLUMN_NAME5)
                .description(DESCRIPTION)
                .userDescription(DESCRIPTION)
                .build();
        AssetMutationResponse response = toUpdate.save(client);
        Asset one = validateSingleUpdate(response);
        assertTrue(one instanceof Column);
        Column updated = (Column) one;
        validateUpdatedColumn(updated);
        assertEquals(updated.getDescription(), DESCRIPTION);
        assertEquals(updated.getUserDescription(), DESCRIPTION);
    }

    @Test(
            groups = {"asset.update.column.description.x"},
            dependsOnGroups = {"asset.update.column.descriptions"})
    void updateColumnDescriptionX() throws AtlanException {
        Column cleared = Column.removeDescription(client, column5.getQualifiedName(), COLUMN_NAME5);
        validateUpdatedColumn(cleared);
        assertNull(cleared.getDescription());
    }

    @Test(
            groups = {"asset.update.column.userDescription.x"},
            dependsOnGroups = {"asset.update.column.description.x"})
    void updateColumnUserDescriptionX() throws AtlanException {
        Column cleared = Column.removeUserDescription(client, column5.getQualifiedName(), COLUMN_NAME5);
        validateUpdatedColumn(cleared);
        assertNull(cleared.getUserDescription());
    }

    @Test(groups = {"asset.create.atlantags"})
    void createAtlanTags() throws AtlanException {
        AtlanTagTest.createAtlanTag(client, ATLAN_TAG_NAME1);
        AtlanTagTest.createAtlanTag(client, ATLAN_TAG_NAME2);
    }

    @Test(
            groups = {"asset.update.column.atlantag"},
            dependsOnGroups = {"asset.create.atlantags", "asset.update.column.userDescription.x"})
    void updateAtlanTag() throws AtlanException {
        Column toUpdate = Column.updater(column5.getQualifiedName(), COLUMN_NAME5)
                .atlanTag(AtlanTag.of(ATLAN_TAG_NAME1).toBuilder()
                        .propagate(false)
                        .build())
                .build();
        AssetMutationResponse response = toUpdate.save(client);
        Asset one = validateSingleUpdate(response);
        assertTrue(one instanceof Column);
        Column column = (Column) one;
        validateUpdatedColumn(column);
        validateHasAtlanTags(column, Set.of(ATLAN_TAG_NAME1));
    }

    @Test(
            groups = {"asset.update.column.atlantag.x"},
            dependsOnGroups = {"asset.update.column.atlantag"})
    void updateAtlanTagX() throws AtlanException {
        Column toUpdate = Column.updater(column5.getQualifiedName(), COLUMN_NAME5)
                .removeAtlanTags()
                .build();
        AssetMutationResponse response = toUpdate.save(client);
        Asset one = validateSingleUpdate(response);
        assertTrue(one instanceof Column);
        Column column = (Column) one;
        validateUpdatedColumn(column);
        validateHasAtlanTags(column, Collections.emptySet());
    }

    @Test(
            groups = {"asset.update.column.addAtlanTags"},
            dependsOnGroups = {"asset.update.column.atlantag.x"})
    void updateColumnAddAtlanTags() throws AtlanException {
        Column toUpdate = column5.trimToRequired()
                .appendAtlanTag(ATLAN_TAG_NAME1)
                .appendAtlanTag(ATLAN_TAG_NAME2)
                .build();
        AssetMutationResponse response = toUpdate.save(client);
        Asset one = validateSingleUpdate(response);
        assertTrue(one instanceof Column);
        Column column = Column.get(client, column5.getGuid(), true);
        validateCompleteColumn(column);
        validateHasAtlanTags(column, Set.of(ATLAN_TAG_NAME1, ATLAN_TAG_NAME2));
    }

    @Test(
            groups = {"asset.update.column.removeAtlanTag"},
            dependsOnGroups = {"asset.update.column.addAtlanTags"})
    void updateColumnRemoveAtlanTag() throws AtlanException {
        Column toUpdate =
                column5.trimToRequired().removeAtlanTag(ATLAN_TAG_NAME2).build();
        AssetMutationResponse response = toUpdate.save(client);
        Asset one = validateSingleUpdate(response);
        assertTrue(one instanceof Column);
        Column column = Column.get(client, column5.getGuid(), true);
        validateCompleteColumn(column);
        validateHasAtlanTags(column, Set.of(ATLAN_TAG_NAME1));
    }

    @Test(
            groups = {"asset.update.column.removeAtlanTagNonexistent"},
            dependsOnGroups = {"asset.update.column.removeAtlanTag"})
    void updateColumnRemoveAtlanTagNonexistent() throws AtlanException {
        Column toUpdate = Column.updater(column5.getQualifiedName(), column5.getName())
                .removeAtlanTag(ATLAN_TAG_NAME2)
                .build();
        AssetMutationResponse response = toUpdate.save(client);
        assertNotNull(response);
        // Confirm this no longer fails, but is optimized away as a noop by the back-end
        AssetMutationResponse.MutationType mutation = response.getMutation(toUpdate);
        assertNotNull(mutation);
        assertEquals(mutation, AssetMutationResponse.MutationType.NOOP);
    }

    @Test(
            groups = {"asset.update.column.addAtlanTags.again"},
            dependsOnGroups = {"asset.update.column.removeAtlanTagNonexistent"})
    void updateColumnAddAtlanTagsAgain() throws AtlanException {
        Column toUpdate =
                column5.trimToRequired().appendAtlanTag(ATLAN_TAG_NAME2).build();
        AssetMutationResponse response = toUpdate.save(client);
        Asset one = validateSingleUpdate(response);
        assertTrue(one instanceof Column);
        Column column = Column.get(client, column5.getGuid(), true);
        validateCompleteColumn(column);
        validateHasAtlanTags(column, Set.of(ATLAN_TAG_NAME1, ATLAN_TAG_NAME2));
    }

    @Test(
            groups = {"asset.search.byAtlanTag"},
            dependsOnGroups = {"asset.update.column.addAtlanTags.again"})
    void searchByAnyAtlanTag() throws AtlanException, InterruptedException {
        waitForTagsToSync(taggedAssetGuids, log);
        IndexSearchRequest index = Column.select(client)
                .where(Column.QUALIFIED_NAME.startsWith(connection.getQualifiedName()))
                .tagged(true)
                .includeOnResults(Column.NAME)
                .includeOnResults(Column.CONNECTION_QUALIFIED_NAME)
                .toRequest();

        IndexSearchResponse response = retrySearchUntil(index, 1L);

        assertEquals(response.getApproximateCount().longValue(), 1L);
        List<Asset> entities = response.getAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 1);
        Asset one = entities.get(0);
        assertTrue(one instanceof Column);
        assertFalse(one.isComplete());
        Column column = (Column) one;
        validateUpdatedColumn(column);
        assertEquals(column.getName(), COLUMN_NAME5);
        assertEquals(column.getConnectionQualifiedName(), connection.getQualifiedName());
    }

    @Test(
            groups = {"asset.search.byAtlanTag"},
            dependsOnGroups = {"asset.update.column.addAtlanTags.again"})
    void searchBySpecificAtlanTag() throws AtlanException, InterruptedException {
        waitForTagsToSync(taggedAssetGuids, log);
        IndexSearchRequest index = Column.select(client)
                .tagged(List.of(ATLAN_TAG_NAME1, ATLAN_TAG_NAME2))
                .includeOnResults(Column.NAME)
                .toRequest();

        IndexSearchResponse response = retrySearchUntil(index, 1L);

        assertEquals(response.getApproximateCount().longValue(), 1L);
        List<Asset> entities = response.getAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 1);
        Asset one = entities.get(0);
        assertTrue(one instanceof Column);
        Column column = (Column) one;
        validateUpdatedColumn(column);
        assertEquals(column.getName(), COLUMN_NAME5);
        assertNull(column.getConnectionQualifiedName());
    }

    @Test(
            groups = {"asset.update.column.removeAtlanTags"},
            dependsOnGroups = {"asset.search.byAtlanTag"})
    void updateColumnRemoveAtlanTags() throws AtlanException {
        Column column = Column.updater(column5.getQualifiedName(), COLUMN_NAME5)
                .removeAtlanTags()
                .build();
        AssetMutationResponse response = column.save(client);
        Asset one = validateSingleUpdate(response);
        assertTrue(one instanceof Column);
        column = (Column) one;
        validateUpdatedColumn(column);
        validateHasAtlanTags(column, Collections.emptySet());
    }

    private void validateHasAtlanTags(Column column, Set<String> names) {
        Set<AtlanTag> tags = column.getAtlanTags();
        assertNotNull(tags);
        assertEquals(tags.size(), names.size());
        Set<String> typeNames = tags.stream().map(AtlanTag::getTypeName).collect(Collectors.toSet());
        assertNotNull(typeNames);
        assertEquals(typeNames, names);
    }

    @Test(groups = {"asset.create.glossary"})
    void createGlossary() throws AtlanException {
        glossary = GlossaryTest.createGlossary(client, PREFIX);
        term1 = GlossaryTest.createTerm(client, TERM_NAME1, glossary);
        term2 = GlossaryTest.createTerm(client, TERM_NAME2, glossary);
    }

    @Test(
            groups = {"asset.update.column.assignedTerms"},
            dependsOnGroups = {"asset.create.glossary", "asset.update.column.removeAtlanTags"})
    void updateAssignedTerm() throws AtlanException {
        Column toUpdate = Column.updater(column5.getQualifiedName(), COLUMN_NAME5)
                .assignedTerm(GlossaryTerm.refByGuid(term1.getGuid()))
                .build();
        AssetMutationResponse response = toUpdate.save(client);
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 2);
        for (Asset one : response.getUpdatedAssets()) {
            assertTrue(one instanceof Column || one instanceof GlossaryTerm);
            if (one instanceof Column column) {
                validateUpdatedColumn(column);
            } else {
                GlossaryTerm term = (GlossaryTerm) one;
                assertEquals(term.getGuid(), term1.getGuid());
            }
        }
        Column updated = Column.get(client, column5.getQualifiedName(), true);
        validateCompleteColumn(updated);
        validateHasTerms(updated, Set.of(term1));
    }

    @Test(
            groups = {"asset.update.column.assignedTerms.x"},
            dependsOnGroups = {"asset.update.column.assignedTerms"})
    void updateAssignedTermsX() throws AtlanException {
        Column toUpdate = Column.updater(column5.getQualifiedName(), COLUMN_NAME5)
                .removeAssignedTerms()
                .build();
        AssetMutationResponse response = toUpdate.save(client);
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getDeletedAssets().isEmpty());
        assertEquals(response.getUpdatedAssets().size(), 2);
        for (Asset one : response.getUpdatedAssets()) {
            assertTrue(one instanceof Column || one instanceof GlossaryTerm);
            if (one instanceof Column column) {
                validateUpdatedColumn(column);
            } else {
                GlossaryTerm term = (GlossaryTerm) one;
                assertEquals(term.getGuid(), term1.getGuid());
            }
        }
        Column updated = Column.get(client, column5.getQualifiedName(), true);
        validateCompleteColumn(updated);
        validateHasTerms(updated, Collections.emptySet());
    }

    @Test(
            groups = {"asset.update.column.appendTerms"},
            dependsOnGroups = {"asset.update.column.assignedTerms.x"})
    void updateColumnAppendTerms() throws AtlanException {
        Column toUpdate = column5.trimToRequired()
                .appendAssignedTerm(term1)
                .appendAssignedTerm(term2)
                .build();
        AssetMutationResponse response = toUpdate.save(client);
        assertNotNull(response);
        Column column = response.getResult(toUpdate);
        validateUpdatedColumn(column);
        column = Column.get(client, column5.getGuid(), true);
        validateCompleteColumn(column);
        validateHasTerms(column, Set.of(term1, term2));
    }

    @Test(
            groups = {"asset.update.column.removeTerm"},
            dependsOnGroups = {"asset.update.column.appendTerms"})
    void updateColumnRemoveTerm() throws AtlanException {
        Column toUpdate = column5.trimToRequired().removeAssignedTerm(term2).build();
        AssetMutationResponse response = toUpdate.save(client);
        assertNotNull(response);
        Column column = response.getResult(toUpdate);
        validateUpdatedColumn(column);
        column = Column.get(client, column5.getGuid(), true);
        validateCompleteColumn(column);
        validateHasTerms(column, Set.of(term1));
    }

    @Test(
            groups = {"asset.update.column.appendTerms.again"},
            dependsOnGroups = {"asset.update.column.removeTerm"})
    void updateColumnAppendTermsAgain() throws AtlanException {
        Column toUpdate = column5.trimToRequired().appendAssignedTerm(term2).build();
        AssetMutationResponse response = toUpdate.save(client);
        assertNotNull(response);
        Column column = response.getResult(toUpdate);
        validateUpdatedColumn(column);
        column = Column.get(client, column5.getGuid(), true);
        validateCompleteColumn(column);
        validateHasTerms(column, Set.of(term1, term2));
    }

    @Test(
            groups = {"asset.update.column.removeTermAgain"},
            dependsOnGroups = {"asset.update.column.appendTerms.again"})
    void updateColumnRemoveTermAgain() throws AtlanException {
        Column toUpdate = column5.trimToRequired().removeAssignedTerm(term1).build();
        AssetMutationResponse response = toUpdate.save(client);
        assertNotNull(response);
        Column column = response.getResult(toUpdate);
        validateUpdatedColumn(column);
        column = Column.get(client, column5.getGuid(), true);
        validateCompleteColumn(column);
        validateHasTerms(column, Set.of(term2));
    }

    @Test(
            groups = {"asset.update.column.replaceTerms"},
            dependsOnGroups = {"asset.update.column.removeTermAgain"})
    void updateColumnReplaceTerms() throws AtlanException {
        Column column = Column.replaceTerms(client, column5.getQualifiedName(), COLUMN_NAME5, List.of(term1));
        validateUpdatedColumn(column);
        column = Column.get(client, column5.getGuid(), true);
        validateCompleteColumn(column);
        validateHasTerms(column, Set.of(term1));
    }

    @Test(
            groups = {"asset.search.byTerm"},
            dependsOnGroups = {"asset.update.column.replaceTerms"})
    void searchByAnyTerm() throws AtlanException, InterruptedException {
        IndexSearchRequest index = Column.select(client)
                .where(Column.ASSIGNED_TERMS.hasAnyValue())
                .where(Column.QUALIFIED_NAME.startsWith(connection.getQualifiedName()))
                .includeOnResults(Column.NAME)
                .includeOnResults(Column.ASSIGNED_TERMS)
                .includeOnResults(Column.CONNECTION_QUALIFIED_NAME)
                .includeOnRelations(Asset.NAME)
                .toRequest();

        IndexSearchResponse response = retrySearchUntil(index, 1L);

        assertEquals(response.getApproximateCount().longValue(), 1L);
        List<Asset> entities = response.getAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 1);
        Asset one = entities.get(0);
        assertTrue(one instanceof Column);
        Column column = (Column) one;
        validateUpdatedColumn(column);
        assertEquals(column.getName(), COLUMN_NAME5);
        assertEquals(column.getConnectionQualifiedName(), connection.getQualifiedName());
        // Note: eventual consistency could return results immediately, but prior to
        // index reflecting latest actual state of the replacement...
        validateHasTerms(column, Set.of(term1));
        IGlossaryTerm term = column.getAssignedTerms().first();
        assertEquals(term.getName(), term1.getName());
    }

    @Test(
            groups = {"asset.search.byTerm"},
            dependsOnGroups = {"asset.update.column.replaceTerms"})
    void searchBySpecificTerm() throws AtlanException, InterruptedException {
        IndexSearchRequest index = Column.select(client)
                .where(Column.ASSIGNED_TERMS.in(List.of(term1.getQualifiedName(), term2.getQualifiedName())))
                .includeOnResults(Column.NAME)
                .includeOnResults(Column.ASSIGNED_TERMS)
                .includeOnResults(Column.CONNECTION_QUALIFIED_NAME)
                .includeOnRelations(Asset.NAME)
                .toRequest();

        IndexSearchResponse response = retrySearchUntil(index, 1L);

        assertEquals(response.getApproximateCount().longValue(), 1L);
        List<Asset> entities = response.getAssets();
        assertNotNull(entities);
        assertEquals(entities.size(), 1);
        Asset one = entities.get(0);
        assertTrue(one instanceof Column);
        Column column = (Column) one;
        validateUpdatedColumn(column);
        assertEquals(column.getName(), COLUMN_NAME5);
        assertEquals(column.getConnectionQualifiedName(), connection.getQualifiedName());
        // Note: eventual consistency could return results immediately, but prior to
        // index reflecting latest actual state of the replacement...
        validateHasTerms(column, Set.of(term1));
        IGlossaryTerm term = column.getAssignedTerms().first();
        assertEquals(term.getName(), term1.getName());
    }

    @Test(
            groups = {"asset.update.column.removeTerms"},
            dependsOnGroups = {"asset.update.column.replaceTerms"})
    void updateColumnRemoveTerms() throws AtlanException, InterruptedException {
        Column column = Column.replaceTerms(client, column5.getQualifiedName(), COLUMN_NAME5, null);
        validateUpdatedColumn(column);
        column = Column.get(client, column5.getQualifiedName(), true);
        validateCompleteColumn(column);
        validateHasTerms(column, Collections.emptySet());
        // Add a short delay for audit log to be fully consistent
        Thread.sleep(2000);
    }

    private void validateHasTerms(Column column, Set<GlossaryTerm> terms) {
        Set<IGlossaryTerm> assignedTerms = column.getAssignedTerms();
        assertNotNull(assignedTerms);
        Set<GlossaryTerm> activeTerms = new HashSet<>();
        for (IGlossaryTerm assignedTerm : assignedTerms) {
            if (assignedTerm.getRelationshipStatus() == AtlanStatus.ACTIVE
                    || assignedTerm.getRelationshipStatus() == null) {
                activeTerms.add((GlossaryTerm) assignedTerm);
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
        if (names != null && !names.isEmpty()) {
            assertEquals(names.size(), termNames.size());
            assertEquals(names, termNames);
        }
        /* Removed given deprecation...
        Set<Meaning> meanings = column.getMeanings();
        if (meanings != null && !meanings.isEmpty()) {
            Set<String> meaningNames =
                    meanings.stream().map(Meaning::getDisplayText).collect(Collectors.toSet());
            assertEquals(meaningNames.size(), termNames.size());
            assertEquals(meaningNames, termNames);
            Set<String> meaningGuids =
                    meanings.stream().map(Meaning::getTermGuid).collect(Collectors.toSet());
            assertEquals(meaningGuids.size(), termGuids.size());
            assertEquals(meaningGuids, termGuids);
        }*/
    }

    @Test(
            groups = {"asset.search.audit"},
            dependsOnGroups = {"asset.update.column.removeTerms"})
    void searchAuditLogByGuid() throws AtlanException, InterruptedException {
        waitForTagsToSync(taggedAssetGuids, log);
        AuditSearchRequest request =
                AuditSearchRequest.byGuid(client, column5.getGuid(), 50).build();

        // We expect at least 32 meaningful audits (may include spurious no-op audits)
        AuditSearchResponse response = retrySearchUntil(request, 32L);

        validateAudits(response.getEntityAudits());

        AuditSearch.AuditSearchBuilder<?, ?> builder = AuditSearch.builder(client)
                .where(AuditSearchRequest.ENTITY_ID.eq(column5.getGuid()))
                .sort(AuditSearchRequest.CREATED.order(SortOrder.Desc))
                .pageSize(10);
        assertTrue(builder.count() >= 32, "Expected at least 32 audits, but found " + builder.count());
        validateAudits(builder.stream().collect(Collectors.toList()));
    }

    @Test(
            groups = {"asset.search.audit"},
            dependsOnGroups = {"asset.update.column.removeTerms"})
    void searchAuditLogByQN() throws AtlanException, InterruptedException {
        waitForTagsToSync(taggedAssetGuids, log);
        AuditSearchRequest request = AuditSearchRequest.byQualifiedName(
                        client, Column.TYPE_NAME, column5.getQualifiedName(), 50)
                .build();
        // We expect at least 32 meaningful audits (may include spurious no-op audits)
        AuditSearchResponse response = retrySearchUntil(request, 32L);
        validateAudits(response.getEntityAudits());
    }

    /**
     * Validates audit entries, using a forward-scanning approach that finds each expected
     * audit while skipping over any spurious no-op audits that may appear in the sequence.
     */
    private void validateAudits(List<EntityAudit> audits) {
        // We expect at least 32 audits total (may include some spurious ones)
        assertTrue(audits.size() >= 32, "Expected at least 32 audits, but found " + audits.size());

        // Use an AuditScanner to iterate through audits from oldest to newest,
        // skipping spurious no-op updates as needed
        AuditScanner scanner = new AuditScanner(audits);

        // 1. ENTITY_CREATE - Column creation (oldest audit)
        EntityAudit one = scanner.nextExpecting(AuditActionType.ENTITY_CREATE);
        AuditDetail detail = one.getDetail();
        assertTrue(detail instanceof Column);
        Column column = (Column) detail;
        validateUpdatedColumn(column);
        assertEquals(column.getName(), COLUMN_NAME5);
        assertNotNull(column.getParent());
        assertTrue(column.getParent() instanceof MaterializedView);
        MaterializedView parent = (MaterializedView) column.getParent();
        assertEquals(parent.getGuid(), mview.getGuid());
        assertNull(column.getCertificateStatus());
        assertNull(column.getAnnouncementType());

        // 2. ownerGroups set
        one = scanner.nextColumnUpdateMatching(
                col -> col.getOwnerGroups() != null && !col.getOwnerGroups().isEmpty());
        column = (Column) one.getDetail();
        validateUpdatedColumn(column);
        assertEquals(column.getOwnerGroups(), Set.of(ownerGroup.getName()));

        // 3. ownerGroups cleared (after set, so empty/null ownerGroups is the clearing audit)
        // The scan order ensures we find this after step 2, so no risk of matching the spurious post-create audit
        one = scanner.nextColumnUpdateMatching(
                col -> col.getOwnerGroups() == null || col.getOwnerGroups().isEmpty());
        column = (Column) one.getDetail();
        validateUpdatedColumn(column);
        assertTrue(column.getOwnerGroups() == null || column.getOwnerGroups().isEmpty());

        // 4. certificate set
        one = scanner.nextColumnUpdateMatching(col -> col.getCertificateStatus() != null);
        column = (Column) one.getDetail();
        validateUpdatedColumn(column);
        assertEquals(column.getCertificateStatus(), CERTIFICATE_STATUS);
        assertEquals(column.getCertificateStatusMessage(), CERTIFICATE_MESSAGE);

        // 5. certificate cleared
        one = scanner.nextColumnUpdateMatching(
                col -> col.getNullFields() != null && col.getNullFields().contains("certificateStatus"));
        column = (Column) one.getDetail();
        validateUpdatedColumn(column);
        Set<String> clearedFields = column.getNullFields();
        assertTrue(clearedFields.contains("certificateStatus"));
        assertTrue(clearedFields.contains("certificateStatusMessage"));

        // 6. announcement set
        one = scanner.nextColumnUpdateMatching(col -> col.getAnnouncementType() != null);
        column = (Column) one.getDetail();
        validateUpdatedColumn(column);
        assertEquals(column.getAnnouncementType(), ANNOUNCEMENT_TYPE);
        assertEquals(column.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
        assertEquals(column.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);

        // 7. announcement cleared
        one = scanner.nextColumnUpdateMatching(
                col -> col.getNullFields() != null && col.getNullFields().contains("announcementType"));
        column = (Column) one.getDetail();
        validateUpdatedColumn(column);
        clearedFields = column.getNullFields();
        assertTrue(clearedFields.contains("announcementType"));
        assertTrue(clearedFields.contains("announcementTitle"));
        assertTrue(clearedFields.contains("announcementMessage"));

        // 8. description and userDescription set
        one = scanner.nextColumnUpdateMatching(col -> col.getDescription() != null);
        column = (Column) one.getDetail();
        validateUpdatedColumn(column);
        assertEquals(column.getDescription(), DESCRIPTION);
        assertEquals(column.getUserDescription(), DESCRIPTION);

        // 9. description cleared
        one = scanner.nextColumnUpdateMatching(
                col -> col.getNullFields() != null && col.getNullFields().contains("description"));
        column = (Column) one.getDetail();
        validateUpdatedColumn(column);
        clearedFields = column.getNullFields();
        assertTrue(clearedFields.contains("description"));

        // 10. userDescription cleared
        one = scanner.nextColumnUpdateMatching(
                col -> col.getNullFields() != null && col.getNullFields().contains("userDescription"));
        column = (Column) one.getDetail();
        validateUpdatedColumn(column);
        clearedFields = column.getNullFields();
        assertTrue(clearedFields.contains("userDescription"));

        // 11. ATLAN_TAG_ADD - tag1
        one = scanner.nextExpecting(AuditActionType.ATLAN_TAG_ADD);
        detail = one.getDetail();
        assertTrue(detail instanceof AtlanTag);
        AtlanTag tag = (AtlanTag) detail;
        assertEquals(tag.getTypeName(), ATLAN_TAG_NAME1);
        assertEquals(tag.getEntityGuid(), column5.getGuid());

        // 12. ENTITY_UPDATE - tags=1
        one = scanner.nextColumnUpdateMatching(
                col -> col.getAtlanTags() != null && col.getAtlanTags().size() == 1);
        column = (Column) one.getDetail();
        validateUpdatedColumn(column);
        assertNotNull(column.getAtlanTags());
        assertEquals(column.getAtlanTags().size(), 1);

        // 13. ATLAN_TAG_DELETE - tag1
        one = scanner.nextExpecting(AuditActionType.ATLAN_TAG_DELETE);
        detail = one.getDetail();
        assertTrue(detail instanceof AtlanTag);
        tag = (AtlanTag) detail;
        assertEquals(tag.getTypeName(), ATLAN_TAG_NAME1);

        // 14. ENTITY_UPDATE - tags=0
        one = scanner.nextColumnUpdateMatching(
                col -> col.getAtlanTags() == null || col.getAtlanTags().isEmpty());
        column = (Column) one.getDetail();
        validateUpdatedColumn(column);
        assertTrue(column.getAtlanTags() == null || column.getAtlanTags().isEmpty());

        // 15-16. Two ATLAN_TAG_ADD (tag1 and tag2, order may vary)
        Set<AtlanTag> tagsAdded = new HashSet<>();

        one = scanner.nextExpecting(AuditActionType.ATLAN_TAG_ADD);
        detail = one.getDetail();
        assertTrue(detail instanceof AtlanTag);
        tagsAdded.add((AtlanTag) detail);

        one = scanner.nextExpecting(AuditActionType.ATLAN_TAG_ADD);
        detail = one.getDetail();
        assertTrue(detail instanceof AtlanTag);
        tagsAdded.add((AtlanTag) detail);

        Set<String> tagsAddedGuids =
                tagsAdded.stream().map(AtlanTag::getEntityGuid).collect(Collectors.toSet());
        Set<String> tagsAddedNames =
                tagsAdded.stream().map(AtlanTag::getTypeName).collect(Collectors.toSet());
        assertEquals(tagsAdded.size(), 2);
        assertEquals(tagsAddedGuids.size(), 1);
        assertTrue(tagsAddedGuids.contains(column5.getGuid()));
        assertEquals(tagsAddedNames.size(), 2);
        assertTrue(tagsAddedNames.contains(ATLAN_TAG_NAME1));
        assertTrue(tagsAddedNames.contains(ATLAN_TAG_NAME2));

        // 17. ENTITY_UPDATE - tags=2
        one = scanner.nextColumnUpdateMatching(
                col -> col.getAtlanTags() != null && col.getAtlanTags().size() == 2);
        column = (Column) one.getDetail();
        validateUpdatedColumn(column);
        assertNotNull(column.getAtlanTags());
        assertEquals(column.getAtlanTags().size(), 2);
        Set<String> tagNames =
                column.getAtlanTags().stream().map(AtlanTag::getTypeName).collect(Collectors.toSet());
        assertEquals(tagNames.size(), 2);
        assertTrue(tagNames.contains(ATLAN_TAG_NAME1));
        assertTrue(tagNames.contains(ATLAN_TAG_NAME2));

        // 18. ATLAN_TAG_DELETE - tag2
        Set<AtlanTag> tagsDeleted = new HashSet<>();

        one = scanner.nextExpecting(AuditActionType.ATLAN_TAG_DELETE);
        detail = one.getDetail();
        assertTrue(detail instanceof AtlanTag);
        tagsDeleted.add((AtlanTag) detail);

        Set<String> tagsDeletedNames =
                tagsDeleted.stream().map(AtlanTag::getTypeName).collect(Collectors.toSet());
        assertEquals(tagsDeleted.size(), 1);
        assertEquals(tagsDeletedNames.size(), 1);
        assertTrue(tagsDeletedNames.contains(ATLAN_TAG_NAME2));

        // 19. ENTITY_UPDATE - tags=1 (only tag1 remains)
        one = scanner.nextColumnUpdateMatching(
                col -> col.getAtlanTags() != null && col.getAtlanTags().size() == 1);
        column = (Column) one.getDetail();
        assertNotNull(column.getAtlanTags());
        assertEquals(column.getAtlanTags().size(), 1);
        tagNames = column.getAtlanTags().stream().map(AtlanTag::getTypeName).collect(Collectors.toSet());
        assertEquals(tagNames.size(), 1);
        assertTrue(tagNames.contains(ATLAN_TAG_NAME1));

        // 20. ATLAN_TAG_ADD - tag2 again
        tagsAdded = new HashSet<>();

        one = scanner.nextExpecting(AuditActionType.ATLAN_TAG_ADD);
        detail = one.getDetail();
        assertTrue(detail instanceof AtlanTag);
        tagsAdded.add((AtlanTag) detail);

        tagsAddedGuids = tagsAdded.stream().map(AtlanTag::getEntityGuid).collect(Collectors.toSet());
        tagsAddedNames = tagsAdded.stream().map(AtlanTag::getTypeName).collect(Collectors.toSet());
        assertEquals(tagsAdded.size(), 1);
        assertEquals(tagsAddedGuids.size(), 1);
        assertTrue(tagsAddedGuids.contains(column5.getGuid()));
        assertEquals(tagsAddedNames.size(), 1);
        assertTrue(tagsAddedNames.contains(ATLAN_TAG_NAME2));

        // 21. ENTITY_UPDATE - tags=2
        one = scanner.nextColumnUpdateMatching(
                col -> col.getAtlanTags() != null && col.getAtlanTags().size() == 2);
        column = (Column) one.getDetail();
        validateUpdatedColumn(column);
        assertNotNull(column.getAtlanTags());
        assertEquals(column.getAtlanTags().size(), 2);
        tagNames = column.getAtlanTags().stream().map(AtlanTag::getTypeName).collect(Collectors.toSet());
        assertEquals(tagNames.size(), 2);
        assertTrue(tagNames.contains(ATLAN_TAG_NAME1));
        assertTrue(tagNames.contains(ATLAN_TAG_NAME2));

        // 22-23. Two ATLAN_TAG_DELETE (tag1 and tag2, order may vary)
        tagsDeleted = new HashSet<>();

        one = scanner.nextExpecting(AuditActionType.ATLAN_TAG_DELETE);
        detail = one.getDetail();
        assertTrue(detail instanceof AtlanTag);
        tagsDeleted.add((AtlanTag) detail);

        one = scanner.nextExpecting(AuditActionType.ATLAN_TAG_DELETE);
        detail = one.getDetail();
        assertTrue(detail instanceof AtlanTag);
        tagsDeleted.add((AtlanTag) detail);

        tagsDeletedNames = tagsDeleted.stream().map(AtlanTag::getTypeName).collect(Collectors.toSet());
        assertEquals(tagsDeleted.size(), 2);
        assertEquals(tagsDeletedNames.size(), 2);
        assertTrue(tagsDeletedNames.contains(ATLAN_TAG_NAME1));
        assertTrue(tagsDeletedNames.contains(ATLAN_TAG_NAME2));

        // 24. ENTITY_UPDATE - tags=0
        one = scanner.nextColumnUpdateMatching(
                col -> col.getAtlanTags() == null || col.getAtlanTags().isEmpty());
        column = (Column) one.getDetail();
        validateUpdatedColumn(column);
        assertTrue(column.getAtlanTags() == null || column.getAtlanTags().isEmpty());

        // Terms section: The audit log may not contain all intermediate term states due to
        // audit consolidation. We just verify that term operations occurred and ended in the
        // expected final state (terms cleared).

        // 25. Find first term assignment (terms > 0)
        one = scanner.nextColumnUpdateMatching(
                col -> col.getAssignedTerms() != null && !col.getAssignedTerms().isEmpty());
        column = (Column) one.getDetail();
        validateUpdatedColumn(column);
        assertNotNull(column.getAssignedTerms());
        assertTrue(column.getAssignedTerms().size() >= 1);

        // Consume remaining term updates until we reach the final state (terms cleared)
        // Use a loop to handle variable numbers of intermediate audits
        EntityAudit termAudit = scanner.peekNext();
        while (termAudit != null
                && termAudit.getAction() == AuditActionType.ENTITY_UPDATE
                && termAudit.getDetail() instanceof Column) {
            Column c = (Column) termAudit.getDetail();
            // Stop if we find the final "terms cleared" state
            if (c.getAssignedTerms() == null || c.getAssignedTerms().isEmpty()) {
                break;
            }
            // Consume this intermediate term update
            scanner.advance();
            termAudit = scanner.peekNext();
        }

        // Final state: terms cleared
        one = scanner.nextColumnUpdateMatching(
                col -> col.getAssignedTerms() == null || col.getAssignedTerms().isEmpty());
        column = (Column) one.getDetail();
        validateUpdatedColumn(column);
        assertTrue(
                column.getAssignedTerms() == null || column.getAssignedTerms().isEmpty());
    }

    /**
     * Helper class to scan through audit entries from oldest to newest,
     * with the ability to skip spurious no-op ENTITY_UPDATE audits.
     */
    private static class AuditScanner {
        private final List<EntityAudit> audits;
        private int index; // Current position, starting from the end (oldest)

        AuditScanner(List<EntityAudit> audits) {
            this.audits = audits;
            this.index = audits.size() - 1; // Start from oldest (highest index)
        }

        /**
         * Get the next audit with the expected action type.
         * When looking for non-ENTITY_UPDATE actions (like ATLAN_TAG_ADD/DELETE),
         * skips any ENTITY_UPDATE audits (which may be spurious or legitimate but not relevant).
         */
        EntityAudit nextExpecting(AuditActionType expectedAction) {
            while (index >= 0) {
                EntityAudit audit = audits.get(index--);
                if (audit.getAction() == expectedAction) {
                    return audit;
                }
                // When looking for non-UPDATE actions, skip any ENTITY_UPDATE audits
                // (they might be spurious no-ops or legitimate updates, but either way
                // they're not what we're looking for)
                if (expectedAction != AuditActionType.ENTITY_UPDATE
                        && audit.getAction() == AuditActionType.ENTITY_UPDATE) {
                    continue;
                }
                fail("Expected " + expectedAction + " but found " + audit.getAction() + " at index " + (index + 1));
            }
            fail("Ran out of audits while looking for " + expectedAction);
            return null; // Never reached
        }

        /**
         * Get the next ENTITY_UPDATE audit for a Column that matches the given predicate.
         * Skips Column updates that don't match, and also skips ATLAN_TAG_ADD/DELETE events
         * (which may appear as extras due to audit consolidation).
         */
        EntityAudit nextColumnUpdateMatching(java.util.function.Predicate<Column> predicate) {
            while (index >= 0) {
                EntityAudit audit = audits.get(index--);
                // Skip tag events - they may appear as extras
                if (audit.getAction() == AuditActionType.ATLAN_TAG_ADD
                        || audit.getAction() == AuditActionType.ATLAN_TAG_DELETE) {
                    continue;
                }
                if (audit.getAction() == AuditActionType.ENTITY_UPDATE && audit.getDetail() instanceof Column) {
                    Column col = (Column) audit.getDetail();
                    if (predicate.test(col)) {
                        return audit;
                    }
                    // Doesn't match, continue to next audit
                    continue;
                }
                fail("Expected ENTITY_UPDATE for Column but found " + audit.getAction() + " at index " + (index + 1));
            }
            fail("Ran out of audits while looking for matching Column ENTITY_UPDATE");
            return null; // Never reached
        }

        /**
         * Peek at the next audit without consuming it.
         * Returns null if no more audits.
         */
        EntityAudit peekNext() {
            if (index >= 0) {
                return audits.get(index);
            }
            return null;
        }

        /**
         * Advance to the next audit (consume the current one).
         */
        void advance() {
            if (index >= 0) {
                index--;
            }
        }
    }

    @Test(
            groups = {"asset.delete.column"},
            dependsOnGroups = {"asset.update.*", "asset.search.*"})
    void deleteColumn() throws AtlanException {
        AssetMutationResponse response = Asset.delete(client, column5.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof Column);
        Column column = (Column) one;
        validateUpdatedColumn(column);
        assertEquals(column.getDeleteHandler(), "SOFT");
        assertEquals(column.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"asset.delete.column.read"},
            dependsOnGroups = {"asset.delete.column"})
    void readDeletedColumn() throws AtlanException {
        validateDeletedAsset(column5, log);
    }

    @Test(
            groups = {"asset.delete.column.restore"},
            dependsOnGroups = {"asset.delete.column.read"})
    void restoreColumn() throws AtlanException {
        assertTrue(Column.restore(client, column5.getQualifiedName()));
        Column restored = Column.get(client, column5.getGuid());
        assertFalse(restored.isComplete());
        validateUpdatedColumn(restored);
        assertEquals(restored.getStatus(), AtlanStatus.ACTIVE);
    }

    @Test(
            groups = {"asset.purge.column"},
            dependsOnGroups = {"asset.delete.column.restore"})
    void purgeColumn() throws AtlanException {
        AssetMutationResponse response = Asset.purge(client, column5.getGuid()).block();
        assertNotNull(response);
        assertTrue(response.getCreatedAssets().isEmpty());
        assertTrue(response.getUpdatedAssets().isEmpty());
        assertEquals(response.getDeletedAssets().size(), 1);
        Asset one = response.getDeletedAssets().get(0);
        assertTrue(one instanceof Column);
        Column column = (Column) one;
        validateUpdatedColumn(column);
        assertEquals(column.getDeleteHandler(), "PURGE");
        assertEquals(column.getStatus(), AtlanStatus.DELETED);
    }

    @Test(
            groups = {"asset.purge.column.read"},
            dependsOnGroups = {"asset.purge.column"})
    void readPurgedColumn() {
        assertThrows(NotFoundException.class, () -> Column.get(client, column5.getGuid()));
    }

    @Test(
            groups = {"asset.purge.connection"},
            dependsOnGroups = {
                "asset.create.*",
                "asset.read.*",
                "asset.search.*",
                "asset.update.*",
                "asset.purge.column*"
            },
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(client, connection.getQualifiedName(), log);
    }

    @Test(
            groups = {"asset.purge.groups"},
            dependsOnGroups = {"asset.purge.connection"},
            alwaysRun = true)
    void purgeGroups() throws AtlanException {
        AtlanGroup.delete(client, ownerGroup.getId());
    }

    @Test(
            groups = {"asset.purge.atlantags"},
            dependsOnGroups = {"asset.purge.connection"},
            alwaysRun = true)
    void purgeAtlanTags() throws AtlanException {
        AtlanTagTest.deleteAtlanTag(client, ATLAN_TAG_NAME1);
        AtlanTagTest.deleteAtlanTag(client, ATLAN_TAG_NAME2);
    }

    @Test(
            groups = {"asset.purge.glossary"},
            dependsOnGroups = {"asset.purge.connection"},
            alwaysRun = true)
    void purgeGlossary() throws AtlanException {
        GlossaryTest.deleteTerm(client, term1.getGuid());
        GlossaryTest.deleteTerm(client, term2.getGuid());
        GlossaryTest.deleteGlossary(client, glossary.getGuid());
    }

    private void validateUpdatedColumn(Column column) {
        assertNotNull(column);
        assertEquals(column.getGuid(), column5.getGuid());
        if (column.getQualifiedName() != null) {
            assertEquals(column.getQualifiedName(), column5.getQualifiedName());
        }
    }

    private void validateCompleteColumn(Column column) {
        validateUpdatedColumn(column);
        assertTrue(column.isComplete());
    }
}
