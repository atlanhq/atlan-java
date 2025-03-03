/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Column
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import org.testng.Assert.assertTrue
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test import of nested columns.
 */
class NestedColumnsTest : PackageTest("nc") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val c1 = makeUnique("c1")
    private val c1Type = AtlanConnectorType.BIGQUERY

    private val testFile = "nested_columns.csv"

    private val files =
        listOf(
            testFile,
            "debug.log",
        )

    private fun prepFile(connectionQN: String) {
        // Prepare a copy of the file with unique names for glossaries and tags
        val input = Paths.get("src", "test", "resources", testFile).toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{{CONNECTION}}", connectionQN)
                output.appendText("$revised\n")
            }
        }
    }

    private fun createConnection(): Connection {
        val conn = Connection.creator(client, c1, c1Type).build()
        val response = conn.save(client).block()
        return response.getResult(conn)
    }

    override fun setup() {
        val connection = createConnection()
        prepFile(connection.qualifiedName)
        runCustomPackage(
            AssetImportCfg(
                assetsFile = Paths.get(testDirectory, testFile).toString(),
                assetsUpsertSemantic = "upsert",
                assetsFailOnErrors = true,
            ),
            Importer::main,
        )
    }

    override fun teardown() {
        removeConnection(c1, c1Type)
    }

    private val connectionAttrs: List<AtlanField> =
        listOf(
            Connection.NAME,
            Connection.CONNECTOR_TYPE,
            Connection.ADMIN_ROLES,
            Connection.ADMIN_GROUPS,
            Connection.ADMIN_USERS,
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
            Column.TABLE,
            Column.PARENT_COLUMN_NAME,
            Column.PARENT_COLUMN_QUALIFIED_NAME,
            Column.PARENT_COLUMN,
            Column.NESTED_COLUMNS,
            Column.COLUMN_HIERARCHY,
            Column.DATA_TYPE,
            Column.SUB_TYPE,
            Column.ORDER,
            Column.NESTED_COLUMN_ORDER,
        )

    @Test
    fun tableExists() {
        val c1 = Connection.findByName(client, c1, c1Type, connectionAttrs)[0]!!
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
        assertEquals("stg_stock_items", tbl.name)
        assertEquals(c1.qualifiedName, tbl.connectionQualifiedName)
        assertEquals(c1Type, tbl.connectorType)
        assertEquals("nested-sample", tbl.databaseName)
        assertTrue(tbl.databaseQualifiedName.endsWith("/nested-sample"))
        assertEquals("nested-sample", tbl.schemaName)
        assertTrue(tbl.schemaQualifiedName.endsWith("/nested-sample/nested-sample"))
        assertEquals("nested-sample", tbl.schema.name)
    }

    @Test
    fun columnsExist() {
        val c1 = Connection.findByName(client, c1, c1Type, connectionAttrs)[0]!!
        val request =
            Column
                .select(client)
                .where(Column.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .includesOnResults(columnAttrs)
                .includeOnRelations(Asset.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 4)
        val found = response.assets
        assertEquals(4, found.size)
        found.forEach { col ->
            col as Column
            assertEquals(c1.qualifiedName, col.connectionQualifiedName)
            assertEquals(c1Type, col.connectorType)
            assertEquals("nested-sample", col.databaseName)
            assertTrue(col.databaseQualifiedName.endsWith("/nested-sample"))
            assertEquals("nested-sample", col.schemaName)
            assertTrue(col.schemaQualifiedName.endsWith("/nested-sample/nested-sample"))
            assertEquals("stg_stock_items", col.tableName)
            assertTrue(col.tableQualifiedName.endsWith("/nested-sample/nested-sample/stg_stock_items"))
            when (col.name) {
                "parent_column" -> {
                    assertEquals("RECORD", col.dataType)
                    assertEquals(1, col.order)
                    assertEquals(2, col.nestedColumns.size)
                }
                "child_1" -> {
                    assertEquals("RECORD", col.dataType)
                    assertEquals(2, col.order)
                    assertEquals("1", col.nestedColumnOrder)
                    assertEquals("parent_column", col.parentColumnName)
                    assertTrue(col.parentColumnQualifiedName.endsWith("/nested-sample/nested-sample/stg_stock_items/parent_column"))
                    assertEquals("parent_column", col.parentColumn.name)
                    assertEquals(1, col.columnHierarchy.size)
                }
                "child_2" -> {
                    assertEquals("VARCHAR", col.dataType)
                    assertEquals(3, col.order)
                    assertEquals("2", col.nestedColumnOrder)
                    assertEquals("parent_column", col.parentColumnName)
                    assertTrue(col.parentColumnQualifiedName.endsWith("/nested-sample/nested-sample/stg_stock_items/parent_column"))
                    assertEquals("parent_column", col.parentColumn.name)
                    assertEquals(1, col.columnHierarchy.size)
                }
                "child_2.1" -> {
                    assertEquals("INT", col.dataType)
                    assertEquals(4, col.order)
                    assertEquals("1", col.nestedColumnOrder)
                    assertEquals("child_2", col.parentColumnName)
                    assertTrue(col.parentColumnQualifiedName.endsWith("/nested-sample/nested-sample/stg_stock_items/parent_column/child_2"))
                    assertEquals("child_2", col.parentColumn.name)
                    assertEquals(2, col.columnHierarchy.size)
                }
            }
        }
    }

    @Test
    fun customAttributesOnColumn() {
        val c1 = Connection.findByName(client, c1, c1Type, connectionAttrs)[0]!!
        val request =
            Column
                .select(client)
                .where(Column.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(Column.NAME.eq("child_2.1"))
                .where(Column.PARENT_COLUMN_NAME.eq("child_2"))
                .includesOnResults(columnAttrs)
                .includeOnRelations(Asset.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val col = found[0] as Column
        val column = Column.get(client, col.guid)
        assertNotNull(column)
        assertNotNull(column.customAttributes)
        assertEquals(2, column.customAttributes.size)
        assertEquals(setOf("column_mode", "is_self_referencing"), column.customAttributes.keys)
        assertEquals("NULLABLE", column.customAttributes["column_mode"])
        assertEquals("NO", column.customAttributes["is_self_referencing"])
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
