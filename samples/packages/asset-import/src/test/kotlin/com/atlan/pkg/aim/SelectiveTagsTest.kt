/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.core.AtlanTag
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanDeleteType
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
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

/**
 * Test selective management of tag associations.
 */
class SelectiveTagsTest : PackageTest("st") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val snowflake = Connection.findByName(client, "development", AtlanConnectorType.SNOWFLAKE)?.get(0)!!
    private val db1 = makeUnique("db1")
    private val db2 = makeUnique("db2")
    private val db3 = makeUnique("db3")
    private val tag1 = makeUnique("t1")
    private val tag2 = makeUnique("t2")
    private val testFile = "selective_tags.csv"

    private fun prepFile() {
        // Prepare a copy of the file with unique names for glossaries and tags
        val input = Paths.get("src", "test", "resources", testFile).toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{{CONNECTION}}", snowflake.qualifiedName)
                        .replace("{{DB1}}", db1)
                        .replace("{{DB2}}", db2)
                        .replace("{{DB3}}", db3)
                        .replace("{{TAG1}}", tag1)
                        .replace("{{TAG2}}", tag2)
                output.appendText("$revised\n")
            }
        }
    }

    private fun createTagsAndDatabases() {
        val maxNetworkRetries = 30
        val t1 = AtlanTagDef.creator(tag1, AtlanIcon.FIRE, AtlanTagColor.RED).build()
        val t2 = AtlanTagDef.creator(tag2, AtlanIcon.FIRE_EXTINGUISHER, AtlanTagColor.GREEN).build()
        client.typeDefs.create(
            listOf(t1, t2),
            RequestOptions.from(client).maxNetworkRetries(maxNetworkRetries).build(),
        )
        val d1 =
            Database
                .creator(db1, snowflake.qualifiedName)
                .atlanTag(AtlanTag.of(tag1))
                .build()
        val d2 =
            Database
                .creator(db2, snowflake.qualifiedName)
                .atlanTag(AtlanTag.of(tag2))
                .build()
        val d3 =
            Database
                .creator(db3, snowflake.qualifiedName)
                .build()
        val response = client.assets.save(listOf(d1, d2, d3))
        assertNotNull(response)
        val one = response.getResult(d1)
        assertNotNull(one)
        val two = response.getResult(d2)
        assertNotNull(two)
        val three = response.getResult(d3)
        assertNotNull(three)
    }

    override fun setup() {
        prepFile()
        createTagsAndDatabases()
    }

    override fun teardown() {
        val guids =
            Database
                .select(client)
                .where(Database.CONNECTION_QUALIFIED_NAME.eq(snowflake.qualifiedName))
                .where(Database.NAME.`in`(listOf(db1, db2, db3)))
                .stream()
                .map { it.guid }
                .toList()
        client.assets.delete(guids, AtlanDeleteType.PURGE)
        removeTag(tag1)
        removeTag(tag2)
    }

    @Test(groups = ["aim.st.prereq"])
    fun db1WithTag1() {
        val database = getDatabase(db1)
        assertNotNull(database.atlanTags)
        assertEquals(1, database.atlanTags.size)
        assertEquals(tag1, database.atlanTags.first().typeName)
    }

    @Test(groups = ["aim.st.prereq"])
    fun db2WithTag2() {
        val database = getDatabase(db2)
        assertNotNull(database.atlanTags)
        assertEquals(1, database.atlanTags.size)
        assertEquals(tag2, database.atlanTags.first().typeName)
    }

    @Test(groups = ["aim.st.prereq"])
    fun db3WithoutTags() {
        val database = getDatabase(db3)
        assertNotNull(database.atlanTags)
        assertTrue(database.atlanTags == null || database.atlanTags.isEmpty())
    }

    @Test(groups = ["aim.st.run"], dependsOnGroups = ["aim.st.prereq"])
    fun runImport() {
        runCustomPackage(
            AssetImportCfg(
                assetsFile = Paths.get(testDirectory, testFile).toString(),
                assetsUpsertSemantic = "update",
                assetsFailOnErrors = false,
                assetsTagHandling = AtlanTagHandling.APPEND.value,
            ),
            Importer::main,
        )
    }

    @Test(groups = ["aim.st.test"], dependsOnGroups = ["aim.st.run"])
    fun db1WithExtraTag() {
        val database = getDatabase(db1)
        assertNotNull(database.atlanTags)
        assertFalse(database.atlanTags.isEmpty())
        assertEquals(2, database.atlanTags.size)
        assertEquals(setOf(tag1, tag2), database.atlanTags.map { it.typeName }.toSet())
    }

    @Test(groups = ["aim.st.test"], dependsOnGroups = ["aim.st.run"])
    fun db2WithNoTags() {
        val database = getDatabase(db2)
        assertNotNull(database.atlanTags)
        assertTrue(database.atlanTags == null || database.atlanTags.isEmpty())
    }

    @Test(groups = ["aim.st.test"], dependsOnGroups = ["aim.st.run"])
    fun db3WithSingleTag() {
        val database = getDatabase(db3)
        assertNotNull(database.atlanTags)
        assertFalse(database.atlanTags.isEmpty())
        assertEquals(1, database.atlanTags.size)
        assertEquals(setOf(tag2), database.atlanTags.map { it.typeName }.toSet())
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }

    private fun getDatabase(dbName: String): Database {
        val request =
            Database
                .select(client)
                .where(Database.CONNECTION_QUALIFIED_NAME.eq(snowflake.qualifiedName))
                .where(Database.NAME.eq(dbName))
                .includeOnResults(Database.ATLAN_TAGS)
                .toRequest()
        val response = retrySearchUntil(request, 1L)
        assertNotNull(response)
        assertNotNull(response.assets)
        assertEquals(1, response.assets.size)
        val database = response.assets[0]
        assertNotNull(database)
        assertEquals(dbName, database.name)
        assertTrue(database is Database)
        return database as Database
    }
}
