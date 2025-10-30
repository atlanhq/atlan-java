/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Column
import com.atlan.model.assets.ColumnProcess
import com.atlan.model.assets.Connection
import com.atlan.model.assets.LineageProcess
import com.atlan.model.assets.Table
import com.atlan.model.assets.View
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.cache.PersistentConnectionCache
import org.testng.Assert
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Test import of a file containing case-insensitive asset references.
 */
class CaseInsensitiveTest : PackageTest("ci") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val connectionName = makeUnique("c1")
    private val connectorType = AtlanConnectorType.TERADATA

    private val testFile = "case-insensitive.csv"

    private val files =
        listOf(
            testFile,
            "debug.log",
        )

    private fun prepFile() {
        // Prepare a copy of the file with unique names for connection and glossary
        val input = Paths.get("src", "test", "resources", testFile).toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{CTYPE}", connectorType.value)
                        .replace("{CNAME}", connectionName)
                output.appendText("$revised\n")
            }
        }
    }

    private fun createConnection(): Connection {
        val c1 = Connection.creator(client, connectionName, connectorType).build()
        val response = c1.save(client).block()
        return response.getResult(c1)
    }

    override fun setup() {
        createConnection()
        prepFile()
        runCustomPackage(
            AssetImportCfg(
                assetsFile = Paths.get(testDirectory, testFile).toString(),
                assetsUpsertSemantic = "upsert",
                assetsAttrToOverwrite = listOf(),
                assetsConfig = "advanced",
                assetsFailOnErrors = false,
                assetsCaseSensitive = false,
            ),
            Importer::main,
        )
    }

    override fun teardown() {
        removeConnection(connectionName, connectorType)
    }

    @Test
    fun connectionCreated() {
        val c1 = Connection.findByName(client, connectionName, connectorType)
        assertEquals(1, c1.size)
        assertEquals(connectionName, c1[0].name)
    }

    @Test
    fun tableCreated() {
        val c = Connection.findByName(client, connectionName, connectorType)[0]!!
        val request =
            Table
                .select(client)
                .where(Table.QUALIFIED_NAME.startsWith(c.qualifiedName))
                .includeOnResults(Table.NAME)
                .includeOnResults(Table.INPUT_TO_PROCESSES)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val tables = response.assets
        assertEquals(1, tables.size)
        val table = tables[0] as Table
        assertEquals("source_table", table.name)
        assertEquals(1, table.inputToProcesses.size)
    }

    @Test
    fun viewCreated() {
        val c = Connection.findByName(client, connectionName, connectorType)[0]!!
        val request =
            View
                .select(client)
                .where(View.QUALIFIED_NAME.startsWith(c.qualifiedName))
                .includeOnResults(View.NAME)
                .includeOnResults(View.OUTPUT_FROM_PROCESSES)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val views = response.assets
        assertEquals(1, views.size)
        val view = views[0] as View
        assertEquals("target_view", view.name)
        assertEquals(1, view.outputFromProcesses.size)
    }

    @Test
    fun columnsCreated() {
        val c = Connection.findByName(client, connectionName, connectorType)[0]!!
        val request =
            Column
                .select(client)
                .where(Column.QUALIFIED_NAME.startsWith(c.qualifiedName))
                .includeOnResults(Column.NAME)
                .includeOnResults(Column.INPUT_TO_PROCESSES)
                .includeOnResults(Column.OUTPUT_FROM_PROCESSES)
                .toRequest()
        val response = retrySearchUntil(request, 4)
        val columns = response.assets
        assertEquals(4, columns.size)
        assertEquals(setOf("col1", "col2"), columns.map { it.name }.toSet())
        // TODO: check CLL details
    }

    @Test
    fun lineageCreated() {
        val c = Connection.findByName(client, connectionName, connectorType)[0]!!
        val request =
            LineageProcess
                .select(client)
                .where(LineageProcess.QUALIFIED_NAME.startsWith(c.qualifiedName))
                .includeOnResults(LineageProcess.NAME)
                .includeOnResults(LineageProcess.INPUTS)
                .includeOnResults(LineageProcess.OUTPUTS)
                .includeOnRelations(Asset.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        assertEquals(1, response.assets.size)
        val lineage = response.assets[0] as LineageProcess
        assertEquals(1, lineage.inputs.size)
        assertEquals(1, lineage.outputs.size)
    }

    @Test
    fun columnLevelLineageCreated() {
        val c = Connection.findByName(client, connectionName, connectorType)[0]!!
        val request =
            ColumnProcess
                .select(client)
                .where(ColumnProcess.QUALIFIED_NAME.startsWith(c.qualifiedName))
                .includeOnResults(ColumnProcess.NAME)
                .includeOnResults(ColumnProcess.INPUTS)
                .includeOnResults(ColumnProcess.OUTPUTS)
                .includeOnResults(ColumnProcess.PROCESS)
                .includeOnRelations(Asset.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 2)
        assertEquals(2, response.assets.size)
        response.assets.forEach { process ->
            val lineage = process as ColumnProcess
            assertNotNull(lineage.process)
            assertEquals(1, lineage.inputs.size)
            assertEquals(1, lineage.outputs.size)
            assertEquals("source_table > target_view", lineage.process.name)
        }
    }

    @Test
    fun connectionCacheCreated() {
        validateConnectionCache()
    }

    private fun validateConnectionCache() {
        val c1 = Connection.findByName(client, connectionName, connectorType)[0]!!
        val dbFile = Paths.get(testDirectory, "connection-cache", "${c1.qualifiedName}.sqlite").toFile()
        Assert.assertTrue(dbFile.isFile)
        Assert.assertTrue(dbFile.exists())
        val cache = PersistentConnectionCache(dbFile.path)
        val assets = cache.listAssets()
        assertNotNull(assets)
        Assert.assertFalse(assets.isEmpty())
        assertEquals(9, assets.size)
        assertEquals(setOf(Table.TYPE_NAME, View.TYPE_NAME, Column.TYPE_NAME, LineageProcess.TYPE_NAME, ColumnProcess.TYPE_NAME), assets.map { it.typeName }.toSet())
        assertEquals(1, assets.count { it.typeName == Table.TYPE_NAME })
        assertEquals(1, assets.count { it.typeName == View.TYPE_NAME })
        assertEquals(4, assets.count { it.typeName == Column.TYPE_NAME })
        assertEquals(1, assets.count { it.typeName == LineageProcess.TYPE_NAME })
        assertEquals(2, assets.count { it.typeName == ColumnProcess.TYPE_NAME })
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
    }
}
