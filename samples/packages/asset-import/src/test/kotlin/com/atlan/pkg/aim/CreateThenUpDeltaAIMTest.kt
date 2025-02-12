/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.assets.MaterializedView
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.model.assets.View
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanStatus
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.cache.PersistentConnectionCache
import org.testng.Assert.assertFalse
import org.testng.Assert.assertTrue
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test creation of assets followed by an upsert of the same assets, including calculating a delta.
 */
class CreateThenUpDeltaAIMTest : PackageTest("ctuda") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val conn1 = makeUnique("c1")
    private val conn1Type = AtlanConnectorType.IBM_DB2
    private lateinit var connection: Connection

    private val testFile = "assets.csv"
    private val revisedFile = "revised.csv"

    private val files =
        listOf(
            testFile,
            "debug.log",
        )

    private fun prepFile(connectionQN: String = connection.qualifiedName) {
        // Prepare a copy of the file with unique names for connections
        val input = Paths.get("src", "test", "resources", testFile).toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{{CTYPE}}", conn1Type.value)
                        .replace("{{CONNECTION}}", connectionQN)
                output.appendText("$revised\n")
            }
        }
    }

    private fun modifyFile(connectionQN: String = connection.qualifiedName) {
        // Modify the loaded file to make some changes (testing upsert)
        val input = Paths.get(testDirectory, testFile).toFile()
        val output = Paths.get(testDirectory, revisedFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                if (!line.contains("VIEW")) {
                    val revised =
                        line
                            .replace("A schema", "Revised schema description")
                    output.appendText("$revised\n")
                }
            }
        }
        // Create some net-new assets
        output.appendText("$connectionQN/DB/SCH/VIEW2,View,VIEW2,ibmdb2,$connectionQN,$connectionQN/DB,DB,$connectionQN/DB/SCH,SCH,,,Schema@$connectionQN/DB/SCH\n")
        output.appendText("$connectionQN/DB/SCH/MVIEW1,MaterialisedView,MVIEW1,ibmdb2,$connectionQN,$connectionQN/DB,DB,$connectionQN/DB/SCH,SCH,,,Schema@$connectionQN/DB/SCH\n")
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
            Database.DESCRIPTION,
            Database.SCHEMAS,
        )

    private val schemaAttrs: List<AtlanField> =
        listOf(
            Schema.NAME,
            Schema.CONNECTION_QUALIFIED_NAME,
            Schema.CONNECTOR_TYPE,
            Schema.DESCRIPTION,
            Schema.DATABASE_NAME,
            Schema.DATABASE_QUALIFIED_NAME,
            Schema.TABLES,
            Schema.VIEWS,
            Schema.MATERIALIZED_VIEWS,
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
            Table.SCHEMA,
        )

    private fun createConnection(): Connection {
        val c1 = Connection.creator(client, conn1, conn1Type).build()
        val response = c1.save(client).block()
        return response.getResult(c1)
    }

    override fun setup() {
        connection = createConnection()
        prepFile()
        runCustomPackage(
            AssetImportCfg(
                assetsFile = Paths.get(testDirectory, testFile).toString(),
                assetsUpsertSemantic = "upsert",
                assetsFailOnErrors = true,
                assetsDeltaSemantic = "full",
            ),
            Importer::main,
        )
    }

    override fun teardown() {
        removeConnection(conn1, conn1Type)
    }

    @Test(groups = ["aim.ctud.create"])
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
    }

    @Test(groups = ["aim.ctud.create"])
    fun database1Created() {
        validateDatabase()
    }

    private fun validateDatabase() {
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
        assertEquals("DB", db.name)
        assertEquals(c1.qualifiedName, db.connectionQualifiedName)
        assertEquals(conn1Type, db.connectorType)
        assertEquals(1, db.schemas.size)
        assertEquals("SCH", db.schemas.first().name)
    }

    @Test(groups = ["aim.ctud.create"])
    fun schema1Created() {
        validateSchema("A schema")
    }

    private fun validateSchema(description: String) {
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
        assertEquals("SCH", sch.name)
        assertEquals(description, sch.description)
        assertEquals(c1.qualifiedName, sch.connectionQualifiedName)
        assertEquals(conn1Type, sch.connectorType)
        assertEquals("DB", sch.databaseName)
        assertTrue(sch.databaseQualifiedName.endsWith("/DB"))
        assertEquals(1, sch.tables.size)
        assertEquals("TBL", sch.tables.first().name)
        if (description == "Revised schema description") {
            assertEquals(1, sch.views.size)
            assertEquals("VIEW2", sch.views.first().name)
            assertEquals(1, sch.materializedViews.size)
            assertEquals("MVIEW1", sch.materializedViews.first().name)
        } else {
            assertEquals(1, sch.views.size)
            assertEquals("VIEW", sch.views.first().name)
        }
    }

    @Test(groups = ["aim.ctud.create"])
    fun table1Created() {
        validateTable()
    }

    private fun validateTable() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            Table
                .select(client)
                .where(Table.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .includesOnResults(tableAttrs)
                .includeOnRelations(Schema.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val tbl = found[0] as Table
        assertEquals("TBL", tbl.name)
        assertEquals(c1.qualifiedName, tbl.connectionQualifiedName)
        assertEquals(conn1Type, tbl.connectorType)
        assertEquals("DB", tbl.databaseName)
        assertTrue(tbl.databaseQualifiedName.endsWith("/DB"))
        assertEquals("SCH", tbl.schemaName)
        assertTrue(tbl.schemaQualifiedName.endsWith("/DB/SCH"))
        assertEquals("SCH", tbl.schema.name)
    }

    @Test(groups = ["aim.ctud.create"])
    fun view1Created() {
        validateView()
    }

    private fun validateView(exists: Boolean = true) {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        if (!exists) {
            val request =
                View
                    .select(client, true)
                    .where(View.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                    .where(View.STATUS.eq(AtlanStatus.DELETED))
                    .includesOnResults(tableAttrs)
                    .toRequest()
            val response = retrySearchUntil(request, 1, true)
            val found = response.assets
            assertEquals(1, found.size)
            val view = found[0] as View
            if (view.status != AtlanStatus.DELETED) {
                logger.error { "Exact request: ${request.toJson(client)}" }
                logger.error { "Exact response: ${response.rawJsonObject}" }
            }
            assertEquals(AtlanStatus.DELETED, view.status)
        } else {
            val request =
                View
                    .select(client)
                    .where(View.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                    .includesOnResults(tableAttrs)
                    .includeOnRelations(Schema.NAME)
                    .toRequest()
            val response = retrySearchUntil(request, 1)
            val found = response.assets
            assertEquals(1, found.size)
            val view = found[0] as View
            assertEquals("VIEW", view.name)
            assertEquals(c1.qualifiedName, view.connectionQualifiedName)
            assertEquals(conn1Type, view.connectorType)
            assertEquals("SCH", view.schema.name)
        }
    }

    @Test(groups = ["aim.ctud.create"])
    fun connectionCacheCreated() {
        validateConnectionCache()
    }

    private fun validateConnectionCache(created: Boolean = true) {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val dbFile = Paths.get(testDirectory, "connection-cache", "${c1.qualifiedName}.sqlite").toFile()
        assertTrue(dbFile.isFile)
        assertTrue(dbFile.exists())
        val cache = PersistentConnectionCache(dbFile.path)
        val assets = cache.listAssets()
        assertNotNull(assets)
        assertFalse(assets.isEmpty())
        if (created) {
            assertEquals(4, assets.size)
            assertEquals(setOf(Database.TYPE_NAME, Schema.TYPE_NAME, Table.TYPE_NAME, View.TYPE_NAME), assets.map { it.typeName }.toSet())
            assertEquals(1, assets.count { it.typeName == Table.TYPE_NAME })
            assertEquals(1, assets.count { it.typeName == View.TYPE_NAME })
            assertEquals(setOf("VIEW"), assets.filter { it.typeName == View.TYPE_NAME }.map { it.name }.toSet())
        } else {
            assertEquals(5, assets.size)
            assertEquals(setOf(Database.TYPE_NAME, Schema.TYPE_NAME, Table.TYPE_NAME, View.TYPE_NAME, MaterializedView.TYPE_NAME), assets.map { it.typeName }.toSet())
            assertEquals(1, assets.count { it.typeName == Table.TYPE_NAME })
            assertEquals(1, assets.count { it.typeName == View.TYPE_NAME })
            assertEquals(1, assets.count { it.typeName == MaterializedView.TYPE_NAME })
            assertEquals(setOf("VIEW2"), assets.filter { it.typeName == View.TYPE_NAME }.map { it.name }.toSet())
        }
    }

    @Test(groups = ["aim.ctud.runUpdate"], dependsOnGroups = ["aim.ctud.create"])
    fun upsertRevisions() {
        modifyFile()
        runCustomPackage(
            AssetImportCfg(
                assetsFile = Paths.get(testDirectory, revisedFile).toString(),
                assetsUpsertSemantic = "upsert",
                assetsFailOnErrors = true,
                assetsDeltaSemantic = "full",
                assetsDeltaReloadCalculation = "changes",
                assetsPreviousFileDirect = Paths.get(testDirectory, testFile).toString(),
            ),
            Importer::main,
        )
        // Allow Elastic index and deletion to become consistent
        Thread.sleep(15000)
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            MaterializedView
                .select(client)
                .where(MaterializedView.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(MaterializedView.NAME.eq("MVIEW1"))
                .toRequest()
        retrySearchUntil(request, 1)
    }

    @Test(groups = ["aim.ctud.update"], dependsOnGroups = ["aim.ctud.runUpdate"])
    fun connectionUnchanged() {
        validateConnection()
    }

    @Test(groups = ["aim.ctud.update"], dependsOnGroups = ["aim.ctud.runUpdate"])
    fun databaseUnchanged() {
        validateDatabase()
    }

    @Test(groups = ["aim.ctud.update"], dependsOnGroups = ["aim.ctud.runUpdate"])
    fun schemaChanged() {
        validateSchema("Revised schema description")
    }

    @Test(groups = ["aim.ctud.update"], dependsOnGroups = ["aim.ctud.runUpdate"])
    fun tableUnchanged() {
        validateTable()
    }

    @Test(groups = ["aim.ctud.update"], dependsOnGroups = ["aim.ctud.runUpdate"])
    fun viewRemoved() {
        validateView(false)
    }

    @Test(groups = ["aim.ctud.update"], dependsOnGroups = ["aim.ctud.runUpdate"])
    fun entirelyNewView() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            View
                .select(client)
                .where(View.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .includesOnResults(tableAttrs)
                .includeOnRelations(Schema.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val view = found[0] as View
        assertEquals("VIEW2", view.name)
        assertEquals(c1.qualifiedName, view.connectionQualifiedName)
        assertEquals(conn1Type, view.connectorType)
        assertEquals("SCH", view.schema.name)
    }

    @Test(groups = ["aim.ctud.update"], dependsOnGroups = ["aim.ctud.runUpdate"])
    fun connectionCacheUpdated() {
        validateConnectionCache(false)
    }

    @Test(dependsOnGroups = ["aim.ctud.*"])
    fun filesCreated() {
        validateFilesExist(files)
    }

    @Test(dependsOnGroups = ["aim.ctud.*"])
    fun previousRunFilesCreated() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val directory = Paths.get(testDirectory, Importer.PREVIOUS_FILES_PREFIX, c1.qualifiedName).toFile()
        assertNotNull(directory)
        assertTrue(directory.isDirectory)
        val files = directory.walkTopDown().filter { it.isFile }.toList()
        assertEquals(2, files.size)
    }

    @Test(dependsOnGroups = ["aim.ctud.*"])
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
