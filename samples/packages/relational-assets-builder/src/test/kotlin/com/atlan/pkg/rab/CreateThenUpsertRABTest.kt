/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import RelationalAssetsBuilderCfg
import com.atlan.Atlan
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Column
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.assets.IColumn
import com.atlan.model.assets.Readme
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.model.assets.View
import com.atlan.model.core.AtlanTag
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanIcon
import com.atlan.model.enums.AtlanStatus
import com.atlan.model.enums.AtlanTagColor
import com.atlan.model.enums.CertificateStatus
import com.atlan.model.fields.AtlanField
import com.atlan.model.typedefs.AtlanTagDef
import com.atlan.net.RequestOptions
import com.atlan.pkg.PackageTest
import mu.KotlinLogging
import org.testng.Assert.assertFalse
import org.testng.Assert.assertTrue
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test creation of relational assets followed by an upsert of the same relational assets.
 */
class CreateThenUpsertRABTest : PackageTest() {
    override val logger = KotlinLogging.logger {}

    private val conn1 = makeUnique("ctu1")
    private val conn1Type = AtlanConnectorType.ICEBERG
    private val tag1 = makeUnique("ctut1")
    private val tag2 = makeUnique("ctut2")

    private val testFile = "input.csv"
    private val revisedFile = "revised.csv"

    private val files =
        listOf(
            testFile,
            "debug.log",
        )

    private fun prepFile() {
        // Prepare a copy of the file with unique names for connections
        val input = Paths.get("src", "test", "resources", "assets.csv").toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{{CONNECTION1}}", conn1)
                        .replace("{{TAG1}}", tag1)
                        .replace("{{TAG2}}", tag2)
                        .replace("{{API_TOKEN_USER}}", Atlan.getDefaultClient().users.currentUser.username)
                output.appendText("$revised\n")
            }
        }
    }

    private fun modifyFile() {
        // Modify the loaded file to make some changes (testing upsert)
        val input = Paths.get(testDirectory, testFile).toFile()
        val output = Paths.get(testDirectory, revisedFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                if (!line.contains("TEST_VIEW")) {
                    val revised =
                        line
                            .replace("Test ", "Revised ")
                            .replace("{{API_TOKEN_USER}}", Atlan.getDefaultClient().users.currentUser.username)
                    output.appendText("$revised\n")
                }
            }
        }
    }

    private fun createTags() {
        val maxNetworkRetries = 30
        val client = Atlan.getDefaultClient()
        val t1 = AtlanTagDef.creator(tag1, AtlanIcon.DATABASE, AtlanTagColor.GREEN).build()
        val t2 = AtlanTagDef.creator(tag2, AtlanIcon.COLUMNS, AtlanTagColor.RED).build()
        client.typeDefs.create(
            listOf(t1, t2),
            RequestOptions.from(client).maxNetworkRetries(maxNetworkRetries).build(),
        )
    }

    private val connectionAttrs: List<AtlanField> =
        listOf(
            Connection.NAME,
            Connection.CONNECTOR_TYPE,
            Connection.ADMIN_ROLES,
            Connection.ADMIN_GROUPS,
            Connection.ADMIN_USERS,
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
        )

    override fun setup() {
        prepFile()
        createTags()
        setup(
            RelationalAssetsBuilderCfg(
                assetsFile = Paths.get(testDirectory, testFile).toString(),
                assetsUpsertSemantic = "upsert",
                assetsFailOnErrors = true,
                trackBatches = false,
                deltaSemantic = "full",
                skipObjectStore = true,
            ),
        )
        Importer.main(arrayOf(testDirectory))
    }

    override fun teardown() {
        removeConnection(conn1, conn1Type)
        removeTag(tag1)
        removeTag(tag2)
    }

    @Test(groups = ["rab.ctu.create"])
    fun connection1Created() {
        validateConnection()
    }

    private fun validateConnection() {
        val found = Connection.findByName(conn1, conn1Type, connectionAttrs)
        assertNotNull(found)
        assertEquals(1, found.size)
        val c1 = found[0]
        assertEquals(conn1, c1.name)
        assertEquals(conn1Type, c1.connectorType)
        val adminRoleId = Atlan.getDefaultClient().roleCache.getIdForName("\$admin")
        assertEquals(setOf(adminRoleId), c1.adminRoles)
        val apiToken = Atlan.getDefaultClient().users.currentUser.username
        assertEquals(setOf("chris", apiToken), c1.adminUsers)
        assertEquals(setOf("admins"), c1.adminGroups)
    }

    @Test(groups = ["rab.ctu.create"])
    fun database1Created() {
        validateDatabase("Test DB")
    }

    private fun validateDatabase(displayName: String) {
        val c1 = Connection.findByName(conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            Database.select()
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
    }

    @Test(groups = ["rab.ctu.create"])
    fun schema1Created() {
        validateSchema("Test schema")
    }

    private fun validateSchema(displayName: String) {
        val c1 = Connection.findByName(conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            Schema.select()
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
        if (displayName == "Revised schema") {
            assertTrue(sch.views.isEmpty())
        } else {
            assertEquals(1, sch.views.size)
            assertEquals("TEST_VIEW", sch.views.first().name)
        }
    }

    @Test(groups = ["rab.ctu.create"])
    fun table1Created() {
        validateTable("Test table")
    }

    private fun validateTable(displayName: String) {
        val c1 = Connection.findByName(conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            Table.select()
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
        assertEquals(2, tbl.atlanTags.size)
        val tagNames = tbl.atlanTags.stream().map(AtlanTag::getTypeName).toList()
        assertTrue(tagNames.contains(tag1))
        assertTrue(tagNames.contains(tag2))
        tbl.atlanTags.forEach { tag ->
            when (tag.typeName) {
                tag1 -> {
                    assertTrue(tag.propagate)
                    assertTrue(tag.removePropagationsOnEntityDelete)
                    assertFalse(tag.restrictPropagationThroughLineage)
                }
                tag2 -> {
                    assertFalse(tag.propagate)
                }
            }
        }
        assertEquals(2, tbl.columns.size)
        val colNames = tbl.columns.stream().map(IColumn::getName).toList()
        assertTrue(colNames.contains("COL1"))
        assertTrue(colNames.contains("COL2"))
    }

    @Test(groups = ["rab.ctu.create"])
    fun columnsForTable1Created() {
        validateColumnsForTable1("Test column 1", "Test column 2")
    }

    private fun validateColumnsForTable1(
        displayCol1: String,
        displayCol2: String,
    ) {
        val c1 = Connection.findByName(conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            Column.select()
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

    @Test(groups = ["rab.ctu.create"])
    fun view1Created() {
        validateView()
    }

    private fun validateView(exists: Boolean = true) {
        val c1 = Connection.findByName(conn1, conn1Type, connectionAttrs)[0]!!
        if (!exists) {
            val request =
                View.select(true)
                    .where(View.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                    .where(View.STATUS.eq(AtlanStatus.DELETED))
                    .includesOnResults(tableAttrs)
                    .includeOnRelations(Asset.NAME)
                    .includeOnRelations(Readme.DESCRIPTION)
                    .toRequest()
            val response = retrySearchUntil(request, 1)
            val found = response.assets
            assertEquals(1, found.size)
            val view = found[0] as View
            assertEquals(AtlanStatus.DELETED, view.status)
        } else {
            val request =
                View.select()
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
            assertEquals(1, view.atlanTags.size)
            assertEquals(tag1, view.atlanTags.first().typeName)
            assertTrue(view.atlanTags.first().propagate)
            assertTrue(view.atlanTags.first().removePropagationsOnEntityDelete)
            assertTrue(view.atlanTags.first().restrictPropagationThroughLineage)
            assertEquals(2, view.columns.size)
            val colNames = view.columns.stream().map(IColumn::getName).toList()
            assertTrue(colNames.contains("COL3"))
            assertTrue(colNames.contains("COL4"))
        }
    }

    @Test(groups = ["rab.ctu.create"])
    fun columnsForView1Created() {
        validateColumnsForView()
    }

    private fun validateColumnsForView(exists: Boolean = true) {
        val c1 = Connection.findByName(conn1, conn1Type, connectionAttrs)[0]!!
        if (!exists) {
            val request =
                Column.select(true)
                    .where(Column.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                    .where(Column.VIEW_NAME.eq("TEST_VIEW"))
                    .where(Column.STATUS.eq(AtlanStatus.DELETED))
                    .includesOnResults(columnAttrs)
                    .toRequest()
            val response = retrySearchUntil(request, 2)
            val found = response.assets
            assertEquals(2, found.size)
            val states = found.stream().map(Asset::getStatus).toList().toSet()
            assertEquals(1, states.size)
            assertEquals(AtlanStatus.DELETED, states.first())
        } else {
            val request =
                Column.select()
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
    }

    @Test(groups = ["rab.ctu.runUpdate"], dependsOnGroups = ["rab.ctu.create"])
    fun upsertRevisions() {
        modifyFile()
        setup(
            RelationalAssetsBuilderCfg(
                assetsFile = Paths.get(testDirectory, revisedFile).toString(),
                assetsUpsertSemantic = "upsert",
                assetsFailOnErrors = true,
                deltaSemantic = "full",
                skipObjectStore = true,
                previousFileDirect = Paths.get(testDirectory, testFile).toString(),
            ),
        )
        Importer.main(arrayOf(testDirectory))
        // Allow Elastic index and deletion to become consistent
        Thread.sleep(15000)
        val c1 = Connection.findByName(conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            Column.select()
                .where(Column.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(Column.TABLE_NAME.eq("TEST_TBL"))
                .where(Column.DISPLAY_NAME.startsWith("Revised column"))
                .includesOnResults(columnAttrs)
                .toRequest()
        retrySearchUntil(request, 2)
    }

    @Test(groups = ["rab.ctu.update"], dependsOnGroups = ["rab.ctu.runUpdate"])
    fun connectionUnchanged() {
        validateConnection()
    }

    @Test(groups = ["rab.ctu.update"], dependsOnGroups = ["rab.ctu.runUpdate"])
    fun databaseChanged() {
        validateDatabase("Revised DB")
    }

    @Test(groups = ["rab.ctu.update"], dependsOnGroups = ["rab.ctu.runUpdate"])
    fun schemaChanged() {
        validateSchema("Revised schema")
    }

    @Test(groups = ["rab.ctu.update"], dependsOnGroups = ["rab.ctu.runUpdate"])
    fun tableChanged() {
        validateTable("Revised table")
    }

    @Test(groups = ["rab.ctu.update"], dependsOnGroups = ["rab.ctu.runUpdate"])
    fun columnsForTable1Changed() {
        validateColumnsForTable1("Revised column 1", "Revised column 2")
    }

    @Test(groups = ["rab.ctu.update"], dependsOnGroups = ["rab.ctu.runUpdate"])
    fun viewRemoved() {
        validateView(false)
    }

    @Test(groups = ["rab.ctu.update"], dependsOnGroups = ["rab.ctu.runUpdate"])
    fun columnsForView1Removed() {
        validateColumnsForView(false)
    }

    @Test(dependsOnGroups = ["rab.ctu.*"])
    fun filesCreated() {
        validateFilesExist(files)
    }

    @Test(dependsOnGroups = ["rab.ctu.*"])
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
