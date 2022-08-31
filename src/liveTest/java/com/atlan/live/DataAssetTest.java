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
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import org.testng.annotations.Test;

@Test(groups = {"data_asset"})
public class DataAssetTest extends AtlanLiveTest {

    public static final String CONNECTOR_NAME = "netsuite";

    public static final String CONNECTION_NAME = "jc-tc-data";
    public static final String DATABASE_NAME = "jc-test-database";
    public static final String SCHEMA_NAME = "jc-test-schema";
    public static final String TABLE_NAME = "jc-test-table";
    public static final String VIEW_NAME = "jc-test-view";
    public static final String MVIEW_NAME = "jc-test-materialized-view";
    public static final String COLUMN_NAME1 = "jc-test-column1";
    public static final String COLUMN_NAME2 = "jc-test-column2";
    public static final String COLUMN_NAME3 = "jc-test-column3";
    public static final String COLUMN_NAME4 = "jc-test-column4";
    public static final String COLUMN_NAME5 = "jc-test-column5";
    public static final String COLUMN_NAME6 = "jc-test-column6";

    public static String connectionGuid = null;
    public static String connectionQame = null;
    public static String databaseGuid = null;
    public static String databaseQame = null;
    public static String schemaGuid = null;
    public static String schemaQame = null;
    public static String tableGuid = null;
    public static String tableQame = null;
    public static String viewGuid = null;
    public static String viewQame = null;
    public static String mviewGuid = null;
    public static String mviewQame = null;
    public static String columnGuid1 = null;
    public static String columnQame1 = null;
    public static String columnGuid2 = null;
    public static String columnQame2 = null;
    public static String columnGuid3 = null;
    public static String columnQame3 = null;
    public static String columnGuid4 = null;
    public static String columnQame4 = null;
    public static String columnGuid5 = null;
    public static String columnQame5 = null;
    public static String columnGuid6 = null;
    public static String columnQame6 = null;

    @Test(groups = {"invalid.connection"})
    void invalidConnection() {
        assertThrows(
                InvalidRequestException.class,
                () -> Connection.creator(
                        CONNECTION_NAME, AtlanConnectionCategory.WAREHOUSE, CONNECTOR_NAME, null, null, null));
    }

    @Test(groups = {"create.connection.data"})
    void createConnection() {
        try {
            String adminRoleGuid = RoleCache.getIdForName("$admin");
            if (adminRoleGuid != null) {
                Connection connection = Connection.creator(
                                CONNECTION_NAME,
                                AtlanConnectionCategory.WAREHOUSE,
                                CONNECTOR_NAME,
                                Collections.singletonList(adminRoleGuid),
                                null,
                                null)
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
            groups = {"read.connection.data"},
            dependsOnGroups = {"create.connection.data"})
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
            groups = {"create.database"},
            dependsOnGroups = {"read.connection.data"})
    void createDatabase() {
        try {
            Database database = Database.creator(DATABASE_NAME, CONNECTOR_NAME, connectionQame)
                    .build();
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
            assertEquals(database.getConnectorName(), CONNECTOR_NAME);
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
            Schema schema = Schema.creator(SCHEMA_NAME, CONNECTOR_NAME, DATABASE_NAME, databaseQame, connectionQame)
                    .build();
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
            assertEquals(schema.getConnectorName(), CONNECTOR_NAME);
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
            Table table = Table.creator(
                            TABLE_NAME,
                            CONNECTOR_NAME,
                            SCHEMA_NAME,
                            schemaQame,
                            DATABASE_NAME,
                            databaseQame,
                            connectionQame)
                    .build();
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
            assertEquals(table.getConnectorName(), CONNECTOR_NAME);
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
            View view = View.creator(
                            VIEW_NAME,
                            CONNECTOR_NAME,
                            SCHEMA_NAME,
                            schemaQame,
                            DATABASE_NAME,
                            databaseQame,
                            connectionQame)
                    .build();
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
            assertEquals(view.getConnectorName(), CONNECTOR_NAME);
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
            MaterializedView mview = MaterializedView.creator(
                            MVIEW_NAME,
                            CONNECTOR_NAME,
                            SCHEMA_NAME,
                            schemaQame,
                            DATABASE_NAME,
                            databaseQame,
                            connectionQame)
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
            assertEquals(mview.getConnectorName(), CONNECTOR_NAME);
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
            Column column = Column.creator(
                            COLUMN_NAME1,
                            CONNECTOR_NAME,
                            Table.TYPE_NAME,
                            TABLE_NAME,
                            tableQame,
                            SCHEMA_NAME,
                            schemaQame,
                            DATABASE_NAME,
                            databaseQame,
                            connectionQame)
                    .build();
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
            columnGuid1 = column.getGuid();
            assertNotNull(columnGuid1);
            columnQame1 = column.getQualifiedName();
            assertNotNull(columnQame1);
            assertEquals(column.getName(), COLUMN_NAME1);
            assertEquals(column.getConnectorName(), CONNECTOR_NAME);
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
            Column column = Column.creator(
                            COLUMN_NAME2,
                            CONNECTOR_NAME,
                            Table.TYPE_NAME,
                            TABLE_NAME,
                            tableQame,
                            SCHEMA_NAME,
                            schemaQame,
                            DATABASE_NAME,
                            databaseQame,
                            connectionQame)
                    .build();
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
            columnGuid2 = column.getGuid();
            assertNotNull(columnGuid2);
            columnQame2 = column.getQualifiedName();
            assertNotNull(columnQame2);
            assertEquals(column.getName(), COLUMN_NAME2);
            assertEquals(column.getConnectorName(), CONNECTOR_NAME);
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
            Column column = Column.creator(
                            COLUMN_NAME3,
                            CONNECTOR_NAME,
                            View.TYPE_NAME,
                            VIEW_NAME,
                            viewQame,
                            SCHEMA_NAME,
                            schemaQame,
                            DATABASE_NAME,
                            databaseQame,
                            connectionQame)
                    .build();
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
            columnGuid3 = column.getGuid();
            assertNotNull(columnGuid3);
            columnQame3 = column.getQualifiedName();
            assertNotNull(columnQame3);
            assertEquals(column.getName(), COLUMN_NAME3);
            assertEquals(column.getConnectorName(), CONNECTOR_NAME);
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
            Column column = Column.creator(
                            COLUMN_NAME4,
                            CONNECTOR_NAME,
                            View.TYPE_NAME,
                            VIEW_NAME,
                            viewQame,
                            SCHEMA_NAME,
                            schemaQame,
                            DATABASE_NAME,
                            databaseQame,
                            connectionQame)
                    .build();
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
            columnGuid4 = column.getGuid();
            assertNotNull(columnGuid4);
            columnQame4 = column.getQualifiedName();
            assertNotNull(columnQame4);
            assertEquals(column.getName(), COLUMN_NAME4);
            assertEquals(column.getConnectorName(), CONNECTOR_NAME);
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
            Column column = Column.creator(
                            COLUMN_NAME5,
                            CONNECTOR_NAME,
                            MaterializedView.TYPE_NAME,
                            MVIEW_NAME,
                            mviewQame,
                            SCHEMA_NAME,
                            schemaQame,
                            DATABASE_NAME,
                            databaseQame,
                            connectionQame)
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
            assertEquals(column.getConnectorName(), CONNECTOR_NAME);
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
            Column column = Column.creator(
                            COLUMN_NAME6,
                            CONNECTOR_NAME,
                            MaterializedView.TYPE_NAME,
                            MVIEW_NAME,
                            mviewQame,
                            SCHEMA_NAME,
                            schemaQame,
                            DATABASE_NAME,
                            databaseQame,
                            connectionQame)
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
            columnGuid6 = column.getGuid();
            assertNotNull(columnGuid6);
            columnQame6 = column.getQualifiedName();
            assertNotNull(columnQame6);
            assertEquals(column.getName(), COLUMN_NAME6);
            assertEquals(column.getConnectorName(), CONNECTOR_NAME);
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
            groups = {"read.column.1"},
            dependsOnGroups = {"create.column.*"})
    void retrieveColumn1() {
        try {
            Entity full = Entity.retrieveFull(columnGuid1);
            assertNotNull(full);
            assertTrue(full instanceof Column);
            Column column = (Column) full;
            assertEquals(column.getGuid(), columnGuid1);
            assertEquals(column.getQualifiedName(), columnQame1);
            assertEquals(column.getName(), COLUMN_NAME1);
            Reference one = column.getParent();
            assertNotNull(one);
            assertEquals(one.getTypeName(), Table.TYPE_NAME);
            assertEquals(one.getGuid(), tableGuid);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to retrieve a table column.");
        }
    }

    @Test(
            groups = {"read.column.3"},
            dependsOnGroups = {"create.column.*"})
    void retrieveColumn3() {
        try {
            Entity full = Entity.retrieveFull(columnGuid3);
            assertNotNull(full);
            assertTrue(full instanceof Column);
            Column column = (Column) full;
            assertEquals(column.getGuid(), columnGuid3);
            assertEquals(column.getQualifiedName(), columnQame3);
            assertEquals(column.getName(), COLUMN_NAME3);
            Reference one = column.getParent();
            assertNotNull(one);
            assertEquals(one.getTypeName(), View.TYPE_NAME);
            assertEquals(one.getGuid(), viewGuid);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to retrieve a view column.");
        }
    }

    @Test(
            groups = {"read.column.5"},
            dependsOnGroups = {"create.column.*"})
    void retrieveColumn5() {
        try {
            Entity full = Entity.retrieveFull(columnGuid5);
            assertNotNull(full);
            assertTrue(full instanceof Column);
            Column column = (Column) full;
            assertEquals(column.getGuid(), columnGuid5);
            assertEquals(column.getQualifiedName(), columnQame5);
            assertEquals(column.getName(), COLUMN_NAME5);
            Reference one = column.getParent();
            assertNotNull(one);
            assertEquals(one.getTypeName(), MaterializedView.TYPE_NAME);
            assertEquals(one.getGuid(), mviewGuid);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to retrieve a materialized view column.");
        }
    }

    @Test(
            groups = {"update.table"},
            dependsOnGroups = {"create.table"})
    void updateTable() {
        try {
            Table updated = Table.updateCertificate(tableQame, AtlanCertificateStatus.VERIFIED, null);
            assertNotNull(updated);
            assertEquals(updated.getCertificateStatus(), AtlanCertificateStatus.VERIFIED);
            updated = Table.updateAnnouncement(
                    tableQame, AtlanAnnouncementType.INFORMATION, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_MESSAGE);
            assertNotNull(updated);
            assertEquals(updated.getAnnouncementType(), AtlanAnnouncementType.INFORMATION);
            assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
            assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to update a table.");
        }
    }

    @Test(
            groups = {"read.table"},
            dependsOnGroups = {"create.column.*", "update.table"})
    void retrieveTable() {
        try {
            Entity full = Entity.retrieveFull(tableGuid);
            assertNotNull(full);
            assertTrue(full instanceof Table);
            Table table = (Table) full;
            assertEquals(table.getGuid(), tableGuid);
            assertEquals(table.getQualifiedName(), tableQame);
            assertEquals(table.getName(), TABLE_NAME);
            assertEquals(table.getCertificateStatus(), AtlanCertificateStatus.VERIFIED);
            assertNotNull(table.getColumns());
            assertEquals(table.getColumns().size(), 2);
            Set<String> types =
                    table.getColumns().stream().map(Reference::getTypeName).collect(Collectors.toSet());
            assertEquals(types.size(), 1);
            assertTrue(types.contains(Column.TYPE_NAME));
            Set<String> guids =
                    table.getColumns().stream().map(Reference::getGuid).collect(Collectors.toSet());
            assertEquals(guids.size(), 2);
            assertTrue(guids.contains(columnGuid1));
            assertTrue(guids.contains(columnGuid2));
            Reference one = table.getSchema();
            assertNotNull(one);
            assertEquals(one.getTypeName(), Schema.TYPE_NAME);
            assertEquals(one.getGuid(), schemaGuid);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to retrieve a table.");
        }
    }

    @Test(
            groups = {"read.view"},
            dependsOnGroups = {"create.column.*"})
    void retrieveView() {
        try {
            Entity full = Entity.retrieveFull(viewGuid);
            assertNotNull(full);
            assertTrue(full instanceof View);
            View view = (View) full;
            assertEquals(view.getGuid(), viewGuid);
            assertEquals(view.getQualifiedName(), viewQame);
            assertEquals(view.getName(), VIEW_NAME);
            assertNotNull(view.getColumns());
            assertEquals(view.getColumns().size(), 2);
            Set<String> types =
                    view.getColumns().stream().map(Reference::getTypeName).collect(Collectors.toSet());
            assertEquals(types.size(), 1);
            assertTrue(types.contains(Column.TYPE_NAME));
            Set<String> guids =
                    view.getColumns().stream().map(Reference::getGuid).collect(Collectors.toSet());
            assertEquals(guids.size(), 2);
            assertTrue(guids.contains(columnGuid3));
            assertTrue(guids.contains(columnGuid4));
            Reference one = view.getSchema();
            assertNotNull(one);
            assertEquals(one.getTypeName(), Schema.TYPE_NAME);
            assertEquals(one.getGuid(), schemaGuid);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to retrieve a view.");
        }
    }

    @Test(
            groups = {"read.mview"},
            dependsOnGroups = {"create.column.*"})
    void retrieveMaterializedView() {
        try {
            Entity full = Entity.retrieveFull(mviewGuid);
            assertNotNull(full);
            assertTrue(full instanceof MaterializedView);
            MaterializedView mview = (MaterializedView) full;
            assertEquals(mview.getGuid(), mviewGuid);
            assertEquals(mview.getQualifiedName(), mviewQame);
            assertEquals(mview.getName(), MVIEW_NAME);
            assertNotNull(mview.getColumns());
            assertEquals(mview.getColumns().size(), 2);
            Set<String> types =
                    mview.getColumns().stream().map(Reference::getTypeName).collect(Collectors.toSet());
            assertEquals(types.size(), 1);
            assertTrue(types.contains(Column.TYPE_NAME));
            Set<String> guids =
                    mview.getColumns().stream().map(Reference::getGuid).collect(Collectors.toSet());
            assertEquals(guids.size(), 2);
            assertTrue(guids.contains(columnGuid5));
            assertTrue(guids.contains(columnGuid6));
            Reference one = mview.getSchema();
            assertNotNull(one);
            assertEquals(one.getTypeName(), Schema.TYPE_NAME);
            assertEquals(one.getGuid(), schemaGuid);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to retrieve a materialized view.");
        }
    }

    @Test(
            groups = {"read.schema"},
            dependsOnGroups = {"create.column.*"})
    void retrieveSchema() {
        try {
            Entity full = Entity.retrieveFull(Schema.TYPE_NAME, schemaQame);
            assertNotNull(full);
            assertTrue(full instanceof Schema);
            Schema schema = (Schema) full;
            assertEquals(schema.getGuid(), schemaGuid);
            assertEquals(schema.getQualifiedName(), schemaQame);
            assertEquals(schema.getName(), SCHEMA_NAME);
            Reference one = schema.getDatabase();
            assertNotNull(one);
            assertEquals(one.getTypeName(), Database.TYPE_NAME);
            assertEquals(one.getGuid(), databaseGuid);
            assertNotNull(schema.getTables());
            assertEquals(schema.getTables().size(), 1);
            Set<String> types =
                    schema.getTables().stream().map(Reference::getTypeName).collect(Collectors.toSet());
            assertEquals(types.size(), 1);
            assertTrue(types.contains(Table.TYPE_NAME));
            Set<String> guids =
                    schema.getTables().stream().map(Reference::getGuid).collect(Collectors.toSet());
            assertEquals(guids.size(), 1);
            assertTrue(guids.contains(tableGuid));
            types = schema.getViews().stream().map(Reference::getTypeName).collect(Collectors.toSet());
            assertEquals(types.size(), 1);
            assertTrue(types.contains(View.TYPE_NAME));
            guids = schema.getViews().stream().map(Reference::getGuid).collect(Collectors.toSet());
            assertEquals(guids.size(), 1);
            assertTrue(guids.contains(viewGuid));
            types = schema.getMaterializedViews().stream()
                    .map(Reference::getTypeName)
                    .collect(Collectors.toSet());
            assertEquals(types.size(), 1);
            assertTrue(types.contains(MaterializedView.TYPE_NAME));
            guids = schema.getMaterializedViews().stream()
                    .map(Reference::getGuid)
                    .collect(Collectors.toSet());
            assertEquals(guids.size(), 1);
            assertTrue(guids.contains(mviewGuid));
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to retrieve a schema.");
        }
    }

    @Test(
            groups = {"read.database"},
            dependsOnGroups = {"create.column.*"})
    void retrieveDatabase() {
        try {
            Entity full = Entity.retrieveFull(Database.TYPE_NAME, databaseQame);
            assertNotNull(full);
            assertTrue(full instanceof Database);
            Database database = (Database) full;
            assertEquals(database.getGuid(), databaseGuid);
            assertEquals(database.getQualifiedName(), databaseQame);
            assertEquals(database.getName(), DATABASE_NAME);
            assertNotNull(database.getSchemas());
            assertEquals(database.getSchemas().size(), 1);
            Set<String> types =
                    database.getSchemas().stream().map(Reference::getTypeName).collect(Collectors.toSet());
            assertEquals(types.size(), 1);
            assertTrue(types.contains(Schema.TYPE_NAME));
            Set<String> guids =
                    database.getSchemas().stream().map(Reference::getGuid).collect(Collectors.toSet());
            assertEquals(guids.size(), 1);
            assertTrue(guids.contains(schemaGuid));
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to retrieve a schema.");
        }
    }

    @Test(
            groups = {"update.table.again"},
            dependsOnGroups = {"read.table"})
    void updateTableAgain() {
        try {
            Table updated = Table.removeCertificate(tableQame, TABLE_NAME);
            assertNotNull(updated);
            assertNull(updated.getCertificateStatus());
            assertNull(updated.getCertificateStatusMessage());
            assertEquals(updated.getAnnouncementType(), AtlanAnnouncementType.INFORMATION);
            assertEquals(updated.getAnnouncementTitle(), ANNOUNCEMENT_TITLE);
            assertEquals(updated.getAnnouncementMessage(), ANNOUNCEMENT_MESSAGE);
            updated = Table.removeAnnouncement(tableQame, TABLE_NAME);
            assertNotNull(updated);
            assertNull(updated.getAnnouncementType());
            assertNull(updated.getAnnouncementTitle());
            assertNull(updated.getAnnouncementMessage());
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception while trying to remove certificates and announcements from a table.");
        }
    }
}
