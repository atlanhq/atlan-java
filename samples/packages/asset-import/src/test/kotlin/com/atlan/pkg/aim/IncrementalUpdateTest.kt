/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.assets.Schema
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanDeleteType
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

/**
 * Test incremental update of assets, and ensure the updates are not processed multiple times per type.
 */
class IncrementalUpdateTest : PackageTest("iut") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val db1 = makeUnique("db1")
    private val db2 = makeUnique("db2")
    private val sch1 = makeUnique("sch1")
    private val sch2 = makeUnique("sch2")
    private val testFile = "assets_incremental.csv"
    private val revisedFile = "revised.csv"

    private val files =
        listOf(
            testFile,
            "debug.log",
        )

    private fun prepFiles() {
        // Prepare copies of the files with unique names for objects
        val gtcIn = Paths.get("src", "test", "resources", testFile).toFile()
        val gtcOut = Paths.get(testDirectory, testFile).toFile()
        gtcIn.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{DBNAME1}", db1)
                        .replace("{DBNAME2}", db2)
                        .replace("{SCHNAME1}", sch1)
                        .replace("{SCHNAME2}", sch2)
                gtcOut.appendText("$revised,\n")
            }
        }
    }

    private fun modifyFile() {
        // Modify the loaded file to make some changes (testing upsert)
        val input = Paths.get(testDirectory, testFile).toFile()
        val output = Paths.get(testDirectory, revisedFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("incremental updates", "more incremental updates")
                output.appendText("$revised\n")
            }
        }
    }

    override fun setup() {
        prepFiles()
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
        val connection = Connection.findByName(client, "production", AtlanConnectorType.SNOWFLAKE)[0]!!
        val dbs =
            Database
                .select(client)
                .where(Database.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(Database.NAME.`in`(listOf(db1, db2)))
                .stream()
                .map { it.guid }
                .toList()
        client.assets.delete(dbs, AtlanDeleteType.PURGE)
        val schs =
            Schema
                .select(client)
                .where(Database.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(Schema.NAME.`in`(listOf(sch1, sch2)))
                .stream()
                .map { it.guid }
                .toList()
        client.assets.delete(schs, AtlanDeleteType.PURGE)
    }

    private val attrs: List<AtlanField> =
        listOf(
            Asset.NAME,
            Asset.DESCRIPTION,
        )

    @Test(groups = ["aim.iut.create"])
    fun db1Created() {
        val db = getDatabase(db1)
        assertNotNull(db)
        assertEquals("Test database for incremental updates", db.description)
    }

    @Test(groups = ["aim.iut.create"])
    fun db2Created() {
        val db = getDatabase(db2)
        assertNotNull(db)
        assertEquals("Another test database for incremental updates", db.description)
    }

    @Test(groups = ["aim.iut.create"])
    fun sch1Created() {
        val sch = getSchema(sch1)
        assertNotNull(sch)
        assertEquals("Test schema for incremental updates", sch.description)
    }

    @Test(groups = ["aim.iut.create"])
    fun sch2Created() {
        val sch = getSchema(sch2)
        assertNotNull(sch)
        assertEquals("Another test schema for incremental updates", sch.description)
    }

    private fun getDatabase(name: String): Database {
        val connection = Connection.findByName(client, "production", AtlanConnectorType.SNOWFLAKE)[0]!!
        val db =
            Database
                .select(client)
                .where(Database.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(Database.NAME.eq(name))
                .includesOnResults(attrs)
                .stream()
                .map { it as Database }
                .toList()
        assertEquals(1, db.size)
        assertEquals(name, db[0].name)
        return db[0]
    }

    private fun getSchema(name: String): Schema {
        val connection = Connection.findByName(client, "production", AtlanConnectorType.SNOWFLAKE)[0]!!
        val sch =
            Schema
                .select(client)
                .where(Schema.CONNECTION_QUALIFIED_NAME.eq(connection.qualifiedName))
                .where(Schema.NAME.eq(name))
                .includesOnResults(attrs)
                .stream()
                .map { it as Schema }
                .toList()
        assertEquals(1, sch.size)
        assertEquals(name, sch[0].name)
        return sch[0]
    }

    @Test(groups = ["aim.iut.runUpdate"], dependsOnGroups = ["aim.iut.create"])
    fun upsertRevisions() {
        modifyFile()
        runCustomPackage(
            AssetImportCfg(
                assetsFile = Paths.get(testDirectory, revisedFile).toString(),
                assetsUpsertSemantic = "update",
                assetsFailOnErrors = true,
                assetsDeltaSemantic = "full",
                assetsDeltaReloadCalculation = "changes",
                assetsPreviousFileDirect = Paths.get(testDirectory, testFile).toString(),
            ),
            Importer::main,
        )
    }

    @Test(dependsOnGroups = ["aim.iut.runUpdate"])
    fun db1Updated() {
        val db = getDatabase(db1)
        assertNotNull(db)
        assertEquals("Test database for more incremental updates", db.description)
    }

    @Test(dependsOnGroups = ["aim.iut.runUpdate"])
    fun db2Updated() {
        val db = getDatabase(db2)
        assertNotNull(db)
        assertEquals("Another test database for more incremental updates", db.description)
    }

    @Test(dependsOnGroups = ["aim.iut.runUpdate"])
    fun sch1Updated() {
        val sch = getDatabase(sch1)
        assertNotNull(sch)
        assertEquals("Test schema for more incremental updates", sch.description)
    }

    @Test(dependsOnGroups = ["aim.iut.runUpdate"])
    fun sch2Updated() {
        val sch = getDatabase(sch2)
        assertNotNull(sch)
        assertEquals("Another test schema for more incremental updates", sch.description)
    }

    @Test(dependsOnGroups = ["aim.iut.runUpdate"])
    fun filesCreated() {
        validateFilesExist(files)
    }

    @Test(dependsOnGroups = ["aim.iut.runUpdate"])
    fun countsAsExpected() {
        assertFalse(
            logHasMessage(
                "INFO",
                "Loading a total of 4 assets...",
            ),
            "Too many assets were updated per type.",
        )
    }

    @Test(dependsOnGroups = ["aim.iut.runUpdate"])
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
