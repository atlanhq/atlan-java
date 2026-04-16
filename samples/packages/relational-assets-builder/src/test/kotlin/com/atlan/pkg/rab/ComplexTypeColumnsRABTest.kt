/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import AssetImportCfg
import RelationalAssetsBuilderCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Column
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Table
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.rab.Importer.PREVIOUS_FILES_PREFIX
import org.testng.Assert.assertFalse
import org.testng.Assert.assertTrue
import java.io.File
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test that RAB correctly expands STRUCT/ARRAY/MAP complex-type columns into nested child columns.
 */
class ComplexTypeColumnsRABTest : PackageTest("ctcol") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val conn1 = makeUnique("c1")
    private val conn1Type = AtlanConnectorType.ICEBERG

    private val testFile = "input.csv"
    private val files = listOf(testFile, "debug.log")

    private fun prepFile() {
        val input = Paths.get("src", "test", "resources", "assets-complex.csv").toFile()
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
        )

    private val tableAttrs: List<AtlanField> =
        listOf(
            Table.NAME,
            Table.CONNECTION_QUALIFIED_NAME,
            Table.COLUMN_COUNT,
            Table.COLUMNS,
        )

    private val columnAttrs: List<AtlanField> =
        listOf(
            Column.NAME,
            Column.STATUS,
            Column.CONNECTION_QUALIFIED_NAME,
            Column.TABLE_NAME,
            Column.TABLE_QUALIFIED_NAME,
            Column.DATA_TYPE,
            Column.RAW_DATA_TYPE_DEFINITION,
            Column.ORDER,
            Column.PARENT_COLUMN_QUALIFIED_NAME,
            Column.PARENT_COLUMN_NAME,
            Column.COLUMN_DEPTH_LEVEL,
            Column.COLUMN_HIERARCHY,
            Column.NESTED_COLUMN_ORDER,
        )

    override fun setup() {
        prepFile()
        runCustomPackage(
            RelationalAssetsBuilderCfg(
                assetsFile = Paths.get(testDirectory, testFile).toString(),
                assetsUpsertSemantic = "upsert",
                assetsFailOnErrors = true,
                deltaSemantic = "full",
            ),
            Importer::main,
        )
        runCustomPackage(
            AssetImportCfg(
                assetsFile = "$testDirectory${File.separator}current-file-transformed.csv",
                assetsUpsertSemantic = "upsert",
                assetsDeltaSemantic = "full",
                assetsFailOnErrors = true,
                assetsPreviousFilePrefix = PREVIOUS_FILES_PREFIX,
            ),
            com.atlan.pkg.aim.Importer::main,
        )
    }

    override fun teardown() {
        removeConnection(conn1, conn1Type)
    }

    @Test(groups = ["rab.ctcol.create"])
    fun tableHasOnlyTopLevelColumns() {
        // The table_columns relationship must only contain the 5 top-level columns;
        // nested child columns have their table reference cleared by ColumnXformer.
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            Table
                .select(client)
                .where(Table.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .includesOnResults(tableAttrs)
                .includeOnRelations(Asset.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val tbl = response.assets[0] as Table
        assertEquals("TEST_TBL", tbl.name)
        assertEquals(5, tbl.columnCount)
        val topLevelNames = tbl.columns.map { it.name }.toSet()
        assertEquals(setOf("PLAIN_COL", "STRUCT_COL", "ARRAY_COL", "MAP_COL", "NESTED_STRUCT_COL"), topLevelNames)
    }

    @Test(groups = ["rab.ctcol.create"])
    fun structColChildColumns() {
        // STRUCT<city:STRING, zip:INT> → two direct children at depth 1
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val structColQN = "${c1.qualifiedName}/TEST_DB/TEST_SCHEMA/TEST_TBL/STRUCT_COL"
        val request =
            Column
                .select(client)
                .where(Column.PARENT_COLUMN_QUALIFIED_NAME.eq(structColQN))
                .includesOnResults(columnAttrs)
                .toRequest()
        val response = retrySearchUntil(request, 2)
        val found = response.assets
        assertEquals(2, found.size)
        assertEquals(setOf("city", "zip"), found.map { it.name }.toSet())
        found.forEach { asset ->
            val col = asset as Column
            assertEquals(structColQN, col.parentColumnQualifiedName)
            assertEquals("STRUCT_COL", col.parentColumnName)
            assertEquals(1, col.columnDepthLevel)
            assertNotNull(col.columnHierarchy)
            assertFalse(col.columnHierarchy.isEmpty())
            assertTrue(col.tableName.isNullOrEmpty())
            assertTrue(col.tableQualifiedName.isNullOrEmpty())
            when (col.name) {
                "city" -> {
                    assertEquals("STRING", col.dataType)
                    assertEquals("1", col.nestedColumnOrder)
                }

                "zip" -> {
                    assertEquals("INT", col.dataType)
                    assertEquals("2", col.nestedColumnOrder)
                }
            }
        }
    }

    @Test(groups = ["rab.ctcol.create"])
    fun arrayColChildColumns() {
        // ARRAY<STRUCT<id:INT, name:STRING>> → two children whose QN passes through /items/ synthetic node
        // but whose parentColumnQualifiedName points directly at ARRAY_COL
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val arrayColQN = "${c1.qualifiedName}/TEST_DB/TEST_SCHEMA/TEST_TBL/ARRAY_COL"
        val request =
            Column
                .select(client)
                .where(Column.PARENT_COLUMN_QUALIFIED_NAME.eq(arrayColQN))
                .includesOnResults(columnAttrs)
                .toRequest()
        val response = retrySearchUntil(request, 2)
        val found = response.assets
        assertEquals(2, found.size)
        assertEquals(setOf("id", "name"), found.map { it.name }.toSet())
        found.forEach { asset ->
            val col = asset as Column
            assertEquals(arrayColQN, col.parentColumnQualifiedName)
            assertEquals(1, col.columnDepthLevel)
            // QN must route through the synthetic "items" node
            assertTrue(col.qualifiedName.contains("/items/"), "Expected /items/ in QN: ${col.qualifiedName}")
            assertTrue(col.tableName.isNullOrEmpty())
        }
    }

    @Test(groups = ["rab.ctcol.create"])
    fun mapColChildColumns() {
        // MAP<STRING, STRUCT<key:STRING, value:DOUBLE>> → two children whose QN passes through /values/ synthetic node
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val mapColQN = "${c1.qualifiedName}/TEST_DB/TEST_SCHEMA/TEST_TBL/MAP_COL"
        val request =
            Column
                .select(client)
                .where(Column.PARENT_COLUMN_QUALIFIED_NAME.eq(mapColQN))
                .includesOnResults(columnAttrs)
                .toRequest()
        val response = retrySearchUntil(request, 2)
        val found = response.assets
        assertEquals(2, found.size)
        assertEquals(setOf("key", "value"), found.map { it.name }.toSet())
        found.forEach { asset ->
            val col = asset as Column
            assertEquals(mapColQN, col.parentColumnQualifiedName)
            assertEquals(1, col.columnDepthLevel)
            // QN must route through the synthetic "values" node
            assertTrue(col.qualifiedName.contains("/values/"), "Expected /values/ in QN: ${col.qualifiedName}")
            assertTrue(col.tableName.isNullOrEmpty())
        }
    }

    @Test(groups = ["rab.ctcol.create"])
    fun nestedStructColDepth1Children() {
        // STRUCT<outer:STRUCT<...>, label:STRING> → two depth-1 children directly under NESTED_STRUCT_COL
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val nestedColQN = "${c1.qualifiedName}/TEST_DB/TEST_SCHEMA/TEST_TBL/NESTED_STRUCT_COL"
        val request =
            Column
                .select(client)
                .where(Column.PARENT_COLUMN_QUALIFIED_NAME.eq(nestedColQN))
                .includesOnResults(columnAttrs)
                .toRequest()
        val response = retrySearchUntil(request, 2)
        val found = response.assets
        assertEquals(2, found.size)
        assertEquals(setOf("outer", "label"), found.map { it.name }.toSet())
        found.forEach { asset ->
            val col = asset as Column
            assertEquals(nestedColQN, col.parentColumnQualifiedName)
            assertEquals(1, col.columnDepthLevel)
            assertTrue(col.tableName.isNullOrEmpty())
            when (col.name) {
                "outer" -> assertEquals("STRUCT", col.dataType)
                "label" -> assertEquals("STRING", col.dataType)
            }
        }
    }

    @Test(groups = ["rab.ctcol.create"])
    fun nestedStructColDepth2Children() {
        // The STRUCT-typed "outer" field expands to inner:STRING, count:INT at depth 2
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val outerColQN = "${c1.qualifiedName}/TEST_DB/TEST_SCHEMA/TEST_TBL/NESTED_STRUCT_COL/outer"
        val request =
            Column
                .select(client)
                .where(Column.PARENT_COLUMN_QUALIFIED_NAME.eq(outerColQN))
                .includesOnResults(columnAttrs)
                .toRequest()
        val response = retrySearchUntil(request, 2)
        val found = response.assets
        assertEquals(2, found.size)
        assertEquals(setOf("inner", "count"), found.map { it.name }.toSet())
        found.forEach { asset ->
            val col = asset as Column
            assertEquals(outerColQN, col.parentColumnQualifiedName)
            assertEquals("outer", col.parentColumnName)
            assertEquals(2, col.columnDepthLevel)
            // Hierarchy must list both NESTED_STRUCT_COL (depth 1) and outer (depth 2)
            assertNotNull(col.columnHierarchy)
            assertEquals(2, col.columnHierarchy.size)
            assertTrue(col.tableName.isNullOrEmpty())
        }
    }

    @Test(dependsOnGroups = ["rab.ctcol.create"])
    fun filesCreated() {
        validateFilesExist(files)
    }

    @Test(dependsOnGroups = ["rab.ctcol.create"])
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
