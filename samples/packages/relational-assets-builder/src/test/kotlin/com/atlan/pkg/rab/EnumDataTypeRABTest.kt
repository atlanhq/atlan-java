/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import AssetImportCfg
import RelationalAssetsBuilderCfg
import com.atlan.model.assets.Column
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Table
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.rab.Importer.PREVIOUS_FILES_PREFIX
import java.io.File
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Test that RAB handles Glue enum data types (e.g. enum('a','b')) without crashing.
 * Regression test for CSA-403: NumberFormatException when enum values were mistaken for
 * precision/scale parameters by DataTypeXformer.
 */
class EnumDataTypeRABTest : PackageTest("enum") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val conn1 = makeUnique("c1")
    private val conn1Type = AtlanConnectorType.ICEBERG

    private val testFile = "input.csv"
    private val files = listOf(testFile, "debug.log")

    private fun prepFile() {
        val input = Paths.get("src", "test", "resources", "assets-enum.csv").toFile()
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

    private val columnAttrs: List<AtlanField> =
        listOf(
            Column.NAME,
            Column.DATA_TYPE,
            Column.RAW_DATA_TYPE_DEFINITION,
            Column.ORDER,
            Column.PRECISION,
            Column.NUMERIC_SCALE,
            Column.MAX_LENGTH,
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

    @Test(groups = ["rab.enum.create"])
    fun enumColumnUpsertedWithoutCrash() {
        // Core regression: importing enum('scheduled',...) must not throw NumberFormatException
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            Column
                .select(client)
                .where(Column.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(Column.NAME.eq("status"))
                .includesOnResults(columnAttrs)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val col = response.assets[0] as Column
        assertEquals("status", col.name)
        // dataType is the full string — baseTypeName only strips <> angle-bracket params, not ()
        assertEquals("enum('scheduled','failed','cancelled','retry','success','deliveryInProgress')", col.dataType)
        // rawDataTypeDefinition preserves the full original string
        assertEquals("enum('scheduled','failed','cancelled','retry','success','deliveryInProgress')", col.rawDataTypeDefinition)
        // precision and scale must be null — enum values are not numeric
        assertNull(col.precision)
        assertNull(col.numericScale)
        assertNull(col.maxLength)
    }

    @Test(groups = ["rab.enum.create"])
    fun enumColumnsHaveCorrectOrder() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            Table
                .select(client)
                .where(Table.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(Table.NAME.eq("webhook_event"))
                .toRequest()
        val tableResponse = retrySearchUntil(request, 1)
        val tbl = tableResponse.assets[0] as Table
        assertNotNull(tbl)

        // All 3 columns (status, created_at, retry_count) must have been created
        val colRequest =
            Column
                .select(client)
                .where(Column.TABLE_QUALIFIED_NAME.eq(tbl.qualifiedName))
                .includesOnResults(columnAttrs)
                .toRequest()
        val colResponse = retrySearchUntil(colRequest, 3)
        assertEquals(3, colResponse.assets.size)
        val names = colResponse.assets.map { it.name }.toSet()
        assertEquals(setOf("status", "created_at", "retry_count"), names)
    }

    @Test(groups = ["rab.enum.create"])
    fun nonEnumColumnsUnaffected() {
        // retry_count is plain int — make sure it still upserts correctly alongside enum columns
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            Column
                .select(client)
                .where(Column.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(Column.NAME.eq("retry_count"))
                .includesOnResults(columnAttrs)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val col = response.assets[0] as Column
        assertEquals("int", col.dataType)
    }

    @Test(dependsOnGroups = ["rab.enum.create"])
    fun filesCreated() {
        validateFilesExist(files)
    }

    @Test(dependsOnGroups = ["rab.enum.create"])
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
