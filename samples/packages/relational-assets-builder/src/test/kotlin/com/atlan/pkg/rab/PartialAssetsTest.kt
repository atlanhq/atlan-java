/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import AssetImportCfg
import RelationalAssetsBuilderCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Column
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.assets.IColumn
import com.atlan.model.assets.Readme
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.model.assets.View
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.CertificateStatus
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.rab.Importer.PREVIOUS_FILES_PREFIX
import org.testng.Assert.assertTrue
import java.io.File
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

/**
 * Test creation of partial relational assets.
 */
class PartialAssetsTest : PackageTest("pa") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val conn1 = makeUnique("c1")
    private val conn1Type = AtlanConnectorType.MARIADB

    private val testFile = "input.csv"

    private val files =
        listOf(
            testFile,
            "debug.log",
        )

    private fun prepFile() {
        // Prepare a copy of the file with unique names for connections
        val input = Paths.get("src", "test", "resources", "assets_partial.csv").toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{{CONNECTION1}}", conn1)
                        .replace("{{API_TOKEN_USER}}", client.users.currentUser.username)
                output.appendText("$revised\n")
            }
        }
    }

    private val connectionAttrs: List<AtlanField> =
        listOf(
            Connection.NAME,
            Connection.CONNECTOR_TYPE,
            Connection.ADMIN_ROLES,
            Connection.ADMIN_GROUPS,
            Connection.ADMIN_USERS,
            Connection.IS_PARTIAL,
        )

    private val databaseAttrs: List<AtlanField> =
        listOf(
            Database.NAME,
            Database.CONNECTION_QUALIFIED_NAME,
            Database.CONNECTOR_TYPE,
            Database.DISPLAY_NAME,
            Database.DESCRIPTION,
            Database.SCHEMA_COUNT,
            Database.SCHEMAS,
            Database.IS_PARTIAL,
        )

    private val schemaAttrs: List<AtlanField> =
        listOf(
            Schema.NAME,
            Schema.CONNECTION_QUALIFIED_NAME,
            Schema.CONNECTOR_TYPE,
            Schema.DISPLAY_NAME,
            Schema.DESCRIPTION,
            Schema.DATABASE_NAME,
            Schema.DATABASE_QUALIFIED_NAME,
            Schema.TABLE_COUNT,
            Schema.VIEW_COUNT,
            Schema.TABLES,
            Schema.VIEWS,
            Schema.IS_PARTIAL,
        )

    private val tableAttrs: List<AtlanField> =
        listOf(
            Table.NAME,
            Table.STATUS,
            Table.CONNECTION_QUALIFIED_NAME,
            Table.CONNECTOR_TYPE,
            Table.DATABASE_NAME,
            Table.DATABASE_QUALIFIED_NAME,
            Table.SCHEMA_NAME,
            Table.SCHEMA_QUALIFIED_NAME,
            Table.DISPLAY_NAME,
            Table.DESCRIPTION,
            Table.CERTIFICATE_STATUS,
            Table.CERTIFICATE_STATUS_MESSAGE,
            Table.README,
            Table.ATLAN_TAGS,
            Table.COLUMN_COUNT,
            Table.COLUMNS,
            Table.IS_PARTIAL,
        )

    private val columnAttrs: List<AtlanField> =
        listOf(
            Column.NAME,
            Column.STATUS,
            Column.CONNECTION_QUALIFIED_NAME,
            Column.CONNECTOR_TYPE,
            Column.DATABASE_NAME,
            Column.DATABASE_QUALIFIED_NAME,
            Column.SCHEMA_NAME,
            Column.SCHEMA_QUALIFIED_NAME,
            Column.TABLE_NAME,
            Column.TABLE_QUALIFIED_NAME,
            Column.VIEW_NAME,
            Column.VIEW_QUALIFIED_NAME,
            Column.DISPLAY_NAME,
            Column.DESCRIPTION,
            Column.DATA_TYPE,
            Column.ORDER,
            Column.IS_PARTIAL,
        )

    override fun setup() {
        prepFile()
        runCustomPackage(
            RelationalAssetsBuilderCfg(
                assetsFile = Paths.get(testDirectory, testFile).toString(),
                assetsUpsertSemantic = "partial",
                assetsFailOnErrors = true,
                trackBatches = false,
            ),
            Importer::main,
        )
        runCustomPackage(
            AssetImportCfg(
                assetsFile = "$testDirectory${File.separator}current-file-transformed.csv",
                assetsUpsertSemantic = "partial",
                assetsFailOnErrors = true,
                assetsPreviousFilePrefix = PREVIOUS_FILES_PREFIX,
                trackBatches = false,
            ),
            com.atlan.pkg.aim.Importer::main,
        )
    }

    override fun teardown() {
        removeConnection(conn1, conn1Type)
    }

    @Test
    fun connection1Created() {
        validateConnection()
    }

    private fun validateConnection() {
        val found = Connection.findByName(client, conn1, conn1Type, connectionAttrs)
        assertNotNull(found)
        assertEquals(1, found.size)
        val c1 = found[0]
        assertEquals(conn1, c1.name)
        assertEquals(conn1Type, c1.connectorType)
        val adminRoleId = client.roleCache.getIdForName("\$admin")
        assertEquals(setOf(adminRoleId), c1.adminRoles)
        val apiToken = client.users.currentUser.username
        assertEquals(setOf("chris", apiToken), c1.adminUsers)
        assertEquals(setOf("admins"), c1.adminGroups)
        assertFalse(c1.isPartial)
    }

    @Test
    fun database1Created() {
        val displayName = "Test DB"
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            Database
                .select(client)
                .where(Database.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .includesOnResults(databaseAttrs)
                .includeOnRelations(Schema.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val db = found[0] as Database
        assertEquals("TEST_DB", db.name)
        assertEquals(displayName, db.displayName)
        assertEquals(c1.qualifiedName, db.connectionQualifiedName)
        assertEquals(conn1Type, db.connectorType)
        assertEquals(1, db.schemaCount)
        assertEquals(1, db.schemas.size)
        assertEquals("TEST_SCHEMA", db.schemas.first().name)
        assertTrue(db.isPartial)
    }

    @Test
    fun schema1Created() {
        val displayName = "Test schema"
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            Schema
                .select(client)
                .where(Schema.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .includesOnResults(schemaAttrs)
                .includeOnRelations(Asset.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val sch = found[0] as Schema
        assertEquals("TEST_SCHEMA", sch.name)
        assertEquals(displayName, sch.displayName)
        assertEquals(c1.qualifiedName, sch.connectionQualifiedName)
        assertEquals(conn1Type, sch.connectorType)
        assertEquals("TEST_DB", sch.databaseName)
        assertTrue(sch.databaseQualifiedName.endsWith("/TEST_DB"))
        assertEquals(1, sch.tableCount)
        assertEquals(1, sch.viewCount)
        assertEquals(1, sch.tables.size)
        assertEquals("TEST_TBL", sch.tables.first().name)
        assertEquals(1, sch.views.size)
        assertEquals("TEST_VIEW", sch.views.first().name)
        assertTrue(sch.isPartial)
    }

    @Test
    fun table1Created() {
        val displayName = "Test table"
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            Table
                .select(client)
                .where(Table.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .includesOnResults(tableAttrs)
                .includeOnRelations(Asset.NAME)
                .includeOnRelations(Readme.DESCRIPTION)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val tbl = found[0] as Table
        assertEquals("TEST_TBL", tbl.name)
        assertEquals(displayName, tbl.displayName)
        assertEquals(c1.qualifiedName, tbl.connectionQualifiedName)
        assertEquals(conn1Type, tbl.connectorType)
        assertEquals("TEST_DB", tbl.databaseName)
        assertTrue(tbl.databaseQualifiedName.endsWith("/TEST_DB"))
        assertEquals("TEST_SCHEMA", tbl.schemaName)
        assertTrue(tbl.schemaQualifiedName.endsWith("/TEST_DB/TEST_SCHEMA"))
        assertEquals(2, tbl.columnCount)
        assertEquals(CertificateStatus.VERIFIED, tbl.certificateStatus)
        assertEquals("Ready to use", tbl.certificateStatusMessage)
        assertEquals("<h1>Table readme</h1>", tbl.readme.description)
        assertEquals(2, tbl.columns.size)
        val colNames =
            tbl.columns
                .stream()
                .map(IColumn::getName)
                .toList()
        assertTrue(colNames.contains("COL1"))
        assertTrue(colNames.contains("COL2"))
        assertTrue(tbl.isPartial)
    }

    @Test
    fun columnsForTable1Created() {
        val displayCol1 = "Test column 1"
        val displayCol2 = "Test column 2"
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            Column
                .select(client)
                .where(Column.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(Column.TABLE_NAME.eq("TEST_TBL"))
                .includesOnResults(columnAttrs)
                .toRequest()
        val response = retrySearchUntil(request, 2)
        val found = response.assets
        assertEquals(2, found.size)
        val colNames = found.stream().map(Asset::getName).toList()
        assertTrue(colNames.contains("COL1"))
        assertTrue(colNames.contains("COL2"))
        found.forEach { col ->
            col as Column
            assertEquals(c1.qualifiedName, col.connectionQualifiedName)
            assertEquals(conn1Type, col.connectorType)
            assertEquals("TEST_DB", col.databaseName)
            assertTrue(col.databaseQualifiedName.endsWith("/TEST_DB"))
            assertEquals("TEST_SCHEMA", col.schemaName)
            assertTrue(col.schemaQualifiedName.endsWith("/TEST_DB/TEST_SCHEMA"))
            assertEquals("TEST_TBL", col.tableName)
            assertTrue(col.tableQualifiedName.endsWith("/TEST_DB/TEST_SCHEMA/TEST_TBL"))
            assertTrue(col.viewName.isNullOrEmpty())
            assertTrue(col.viewQualifiedName.isNullOrEmpty())
            assertTrue(col.isPartial)
            when (col.name) {
                "COL1" -> {
                    assertEquals("VARCHAR", col.dataType)
                    assertEquals(1, col.order)
                    assertEquals(displayCol1, col.displayName)
                }
                "COL2" -> {
                    assertEquals("BIGINT", col.dataType)
                    assertEquals(2, col.order)
                    assertEquals(displayCol2, col.displayName)
                }
            }
        }
    }

    @Test
    fun view1Created() {
        validateView()
    }

    private fun validateView() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            View
                .select(client)
                .where(View.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .includesOnResults(tableAttrs)
                .includeOnRelations(Asset.NAME)
                .includeOnRelations(Readme.DESCRIPTION)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val view = found[0] as View
        assertEquals("TEST_VIEW", view.name)
        assertEquals("Test view", view.displayName)
        assertEquals(c1.qualifiedName, view.connectionQualifiedName)
        assertEquals(conn1Type, view.connectorType)
        assertEquals(CertificateStatus.DRAFT, view.certificateStatus)
        assertTrue(view.certificateStatusMessage.isNullOrBlank())
        assertEquals(2, view.columnCount)
        assertEquals("<h2>View readme</h2>", view.readme.description)
        assertEquals(2, view.columns.size)
        val colNames =
            view.columns
                .stream()
                .map(IColumn::getName)
                .toList()
        assertTrue(colNames.contains("COL3"))
        assertTrue(colNames.contains("COL4"))
        assertTrue(view.isPartial)
    }

    @Test
    fun columnsForView1Created() {
        validateColumnsForView()
    }

    private fun validateColumnsForView() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            Column
                .select(client)
                .where(Column.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(Column.VIEW_NAME.eq("TEST_VIEW"))
                .includesOnResults(columnAttrs)
                .toRequest()
        val response = retrySearchUntil(request, 2)
        val found = response.assets
        assertEquals(2, found.size)
        val colNames = found.stream().map(Asset::getName).toList()
        assertTrue(colNames.contains("COL3"))
        assertTrue(colNames.contains("COL4"))
        found.forEach { col ->
            col as Column
            assertEquals(c1.qualifiedName, col.connectionQualifiedName)
            assertEquals(conn1Type, col.connectorType)
            assertEquals("TEST_DB", col.databaseName)
            assertTrue(col.databaseQualifiedName.endsWith("/TEST_DB"))
            assertEquals("TEST_SCHEMA", col.schemaName)
            assertTrue(col.schemaQualifiedName.endsWith("/TEST_DB/TEST_SCHEMA"))
            assertEquals("TEST_VIEW", col.viewName)
            assertTrue(col.viewQualifiedName.endsWith("/TEST_DB/TEST_SCHEMA/TEST_VIEW"))
            assertTrue(col.tableName.isNullOrEmpty())
            assertTrue(col.tableQualifiedName.isNullOrEmpty())
            assertTrue(col.isPartial)
            when (col.name) {
                "COL3" -> {
                    assertEquals("INT32", col.dataType)
                    assertEquals(1, col.order)
                    assertEquals("Test column 3", col.displayName)
                }
                "COL4" -> {
                    assertEquals("DECIMAL", col.dataType)
                    assertEquals(2, col.order)
                    assertEquals("Test column 4", col.displayName)
                }
            }
        }
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
