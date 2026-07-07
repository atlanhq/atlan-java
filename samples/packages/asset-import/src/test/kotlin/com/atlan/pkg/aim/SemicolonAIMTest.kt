/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanStatus
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test imports of a semicolon-delimited CSV file, including the parallel chunk-split path
 * (enough rows to split across multiple chunk files) and full delta calculation between
 * two semicolon-delimited files. (CSA-484)
 *
 * Deliberately hostile values are included to prove separator handling end-to-end:
 * - descriptions containing commas (must NOT be treated as separators)
 * - quoted descriptions containing semicolons (must not break parsing or chunk round-trip)
 */
class SemicolonAIMTest : PackageTest("semi") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val conn1 = makeUnique("c1")
    private val conn1Type = AtlanConnectorType.TERADATA
    private lateinit var connection: Connection

    private val testFile = "assets.csv"
    private val revisedFile = "revised.csv"
    private val tableCount = 100

    private val files =
        listOf(
            testFile,
            revisedFile,
            "debug.log",
        )

    private fun tableRow(
        connectionQN: String,
        i: Int,
    ): String {
        val description = "\"Description, with commas; and a semicolon, for table $i\""
        return "$connectionQN/DB/SCH/TBL$i;Table;TBL$i;${conn1Type.value};$connectionQN;$connectionQN/DB;DB;$connectionQN/DB/SCH;SCH;$description;;Schema@$connectionQN/DB/SCH"
    }

    private fun prepFile(connectionQN: String = connection.qualifiedName) {
        val output = Paths.get(testDirectory, testFile).toFile()
        output.appendText("qualifiedName;typeName;name;connectorType;connectionQualifiedName;databaseQualifiedName;databaseName;schemaQualifiedName;schemaName;description;database;schema\n")
        output.appendText("$connectionQN/DB;Database;DB;${conn1Type.value};$connectionQN;;;;;;;\n")
        output.appendText("$connectionQN/DB/SCH;Schema;SCH;${conn1Type.value};$connectionQN;$connectionQN/DB;DB;;;\"A schema; original\";Database@$connectionQN/DB;\n")
        for (i in 1..tableCount) {
            output.appendText("${tableRow(connectionQN, i)}\n")
        }
    }

    private fun modifyFile(connectionQN: String = connection.qualifiedName) {
        // Revise the schema description and drop the last table (testing delta deletion)
        val input = Paths.get(testDirectory, testFile).toFile()
        val output = Paths.get(testDirectory, revisedFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                if (!line.contains("TBL$tableCount;")) {
                    val revised = line.replace("A schema; original", "A schema; revised")
                    output.appendText("$revised\n")
                }
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
            Table.STATUS,
            Table.CONNECTION_QUALIFIED_NAME,
            Table.DESCRIPTION,
            Table.SCHEMA_NAME,
            Table.SCHEMA_QUALIFIED_NAME,
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
                assetsConfig = "advanced",
                assetsFailOnErrors = true,
                assetsFieldSeparator = ";",
                assetsDeltaSemantic = "full",
            ),
            Importer::main,
        )
    }

    override fun teardown() {
        removeConnection(conn1, conn1Type)
    }

    @Test(groups = ["aim.semi.create"])
    fun connectionCreated() {
        val found = Connection.findByName(client, conn1, conn1Type, connectionAttrs)
        assertNotNull(found)
        assertEquals(1, found.size)
        assertEquals(conn1, found[0].name)
    }

    @Test(groups = ["aim.semi.create"])
    fun databaseCreated() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            Database
                .select(client)
                .where(Database.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .toRequest()
        val response = retrySearchUntil(request, 1)
        assertEquals(1, response.assets.size)
        assertEquals("DB", response.assets[0].name)
    }

    @Test(groups = ["aim.semi.create"])
    fun schemaCreatedWithSemicolonDescription() {
        validateSchema("A schema; original")
    }

    private fun validateSchema(description: String) {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            Schema
                .select(client)
                .where(Schema.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .includesOnResults(listOf(Schema.NAME, Schema.DESCRIPTION))
                .toRequest()
        val response = retrySearchUntil(request, 1)
        assertEquals(1, response.assets.size)
        val sch = response.assets[0] as Schema
        assertEquals("SCH", sch.name)
        assertEquals(description, sch.description)
    }

    @Test(groups = ["aim.semi.create"])
    fun allTablesCreated() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            Table
                .select(client)
                .where(Table.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .includesOnResults(tableAttrs)
                .toRequest()
        val response = retrySearchUntil(request, tableCount.toLong())
        assertEquals(tableCount.toLong(), response.approximateCount)
    }

    @Test(groups = ["aim.semi.create"])
    fun commasSurviveChunkRoundTrip() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            Table
                .select(client)
                .where(Table.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(Table.NAME.eq("TBL1"))
                .includesOnResults(tableAttrs)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        assertEquals(1, response.assets.size)
        val tbl = response.assets[0] as Table
        assertEquals("Description, with commas; and a semicolon, for table 1", tbl.description)
        assertEquals("SCH", tbl.schemaName)
    }

    @Test(groups = ["aim.semi.runUpdate"], dependsOnGroups = ["aim.semi.create"])
    fun upsertRevisions() {
        modifyFile()
        runCustomPackage(
            AssetImportCfg(
                assetsFile = Paths.get(testDirectory, revisedFile).toString(),
                assetsUpsertSemantic = "upsert",
                assetsConfig = "advanced",
                assetsFailOnErrors = true,
                assetsFieldSeparator = ";",
                assetsDeltaSemantic = "full",
                assetsDeltaReloadCalculation = "changes",
                assetsPreviousFileDirect = Paths.get(testDirectory, testFile).toString(),
            ),
            Importer::main,
        )
        // Allow Elastic index and deletion to become consistent
        Thread.sleep(15000)
    }

    @Test(groups = ["aim.semi.update"], dependsOnGroups = ["aim.semi.runUpdate"])
    fun schemaDescriptionRevised() {
        validateSchema("A schema; revised")
    }

    @Test(groups = ["aim.semi.update"], dependsOnGroups = ["aim.semi.runUpdate"])
    fun droppedTableRemoved() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            Table
                .select(client, true)
                .where(Table.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .where(Table.NAME.eq("TBL$tableCount"))
                .where(Table.STATUS.eq(AtlanStatus.DELETED))
                .includesOnResults(tableAttrs)
                .toRequest()
        val response = retrySearchUntil(request, 1, true)
        assertEquals(1, response.assets.size)
        assertEquals(AtlanStatus.DELETED, response.assets[0].status)
    }

    @Test(groups = ["aim.semi.update"], dependsOnGroups = ["aim.semi.runUpdate"])
    fun remainingTablesUntouched() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            Table
                .select(client)
                .where(Table.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .toRequest()
        val response = retrySearchUntil(request, (tableCount - 1).toLong())
        assertEquals((tableCount - 1).toLong(), response.approximateCount)
    }

    @Test(dependsOnGroups = ["aim.semi.*"])
    fun filesCreated() {
        validateFilesExist(files)
    }

    @Test(dependsOnGroups = ["aim.semi.*"])
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
