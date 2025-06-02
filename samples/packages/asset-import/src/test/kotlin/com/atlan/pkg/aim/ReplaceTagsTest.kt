/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.core.AtlanTag
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanIcon
import com.atlan.model.enums.AtlanTagColor
import com.atlan.model.enums.AtlanTagHandling
import com.atlan.model.typedefs.AtlanTagDef
import com.atlan.net.RequestOptions
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import org.testng.Assert.assertTrue
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * One-off test of a specific input file, for debugging purposes.
 */
class ReplaceTagsTest : PackageTest("rt") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val db = makeUnique("db1")
    private val tagName = makeUnique("t1")
    private val testFile = "remove_tags.csv"

    private fun prepFile(connectionQN: String) {
        // Prepare a copy of the file with unique names for glossaries and tags
        val input = Paths.get("src", "test", "resources", testFile).toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{{CONNECTION}}", connectionQN)
                        .replace("{{DB}}", db)
                output.appendText("$revised\n")
            }
        }
    }

    private fun createTagAndDatabase(connectionQN: String) {
        val maxNetworkRetries = 30
        val t1 = AtlanTagDef.creator(tagName, AtlanIcon.AIRPLANE, AtlanTagColor.GREEN).build()
        client.typeDefs.create(
            listOf(t1),
            RequestOptions.from(client).maxNetworkRetries(maxNetworkRetries).build(),
        )
        val toCreate =
            Database
                .creator(db, connectionQN)
                .atlanTag(AtlanTag.of(tagName))
                .build()
        val response = toCreate.save(client)
        assertNotNull(response)
        val db = response.getResult(toCreate)
        assertNotNull(db)
    }

    override fun setup() {
        val snowflake = Connection.findByName(client, "development", AtlanConnectorType.SNOWFLAKE)?.get(0)!!
        prepFile(snowflake.qualifiedName)
        createTagAndDatabase(snowflake.qualifiedName)
    }

    override fun teardown() {
        val snowflake = Connection.findByName(client, "development", AtlanConnectorType.SNOWFLAKE)?.get(0)!!
        Database
            .select(client)
            .where(Database.CONNECTION_QUALIFIED_NAME.eq(snowflake.qualifiedName))
            .where(Database.NAME.eq(db))
            .stream()
            .findFirst()
            .ifPresent {
                Database.purge(client, it.guid)
            }
        removeTag(tagName)
    }

    @Test(groups = ["aim.rt.prereq"])
    fun databaseWithTag() {
        val database = getDatabase()
        assertNotNull(database.atlanTags)
        assertEquals(1, database.atlanTags.size)
        assertEquals(tagName, database.atlanTags.first().typeName)
    }

    @Test(groups = ["aim.rt.run"], dependsOnGroups = ["aim.rt.prereq"])
    fun runImport() {
        runCustomPackage(
            AssetImportCfg(
                assetsFile = Paths.get(testDirectory, testFile).toString(),
                assetsUpsertSemantic = "update",
                assetsConfig = "advanced",
                assetsFailOnErrors = false,
                assetsTagHandling = AtlanTagHandling.REPLACE.value,
            ),
            Importer::main,
        )
    }

    @Test(groups = ["aim.rt.test"], dependsOnGroups = ["aim.rt.run"])
    fun databaseWithoutTag() {
        val database = getDatabase()
        assertTrue(database.atlanTags == null || database.atlanTags.isEmpty())
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }

    private fun getDatabase(): Database {
        val snowflake = Connection.findByName(client, "development", AtlanConnectorType.SNOWFLAKE)?.get(0)!!
        val request =
            Database
                .select(client)
                .where(Database.CONNECTION_QUALIFIED_NAME.eq(snowflake.qualifiedName))
                .where(Database.NAME.eq(db))
                .includeOnResults(Database.ATLAN_TAGS)
                .toRequest()
        val response = retrySearchUntil(request, 1L)
        assertNotNull(response)
        assertNotNull(response.assets)
        assertEquals(1, response.assets.size)
        val database = response.assets[0]
        assertNotNull(database)
        assertEquals(db, database.name)
        assertTrue(database is Database)
        return database as Database
    }
}
