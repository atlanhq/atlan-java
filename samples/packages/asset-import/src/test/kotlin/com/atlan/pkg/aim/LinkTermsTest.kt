/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Column
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.assets.Table
import com.atlan.model.assets.View
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanIcon
import com.atlan.model.enums.AtlanTagColor
import com.atlan.model.typedefs.AtlanTagDef
import com.atlan.net.RequestOptions
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.cache.PersistentConnectionCache
import org.testng.Assert
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Test import of a very simple file containing assigned terms.
 */
class LinkTermsTest : PackageTest("lt") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val glossaryName = makeUnique("g1")
    private val connectionName = makeUnique("c1")
    private val connectorType = AtlanConnectorType.NETEZZA

    private val tag1 = makeUnique("t1")
    private val tag2 = makeUnique("t2")

    private val testFile = "input.csv"
    private val revisedFile = "case_insensitive.csv"

    private val files =
        listOf(
            testFile,
            "debug.log",
        )

    private fun prepFile(connectionQN: String) {
        // Prepare a copy of the file with unique names for connection and glossary
        val input = Paths.get("src", "test", "resources", "link_terms.csv").toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{GLOSSARY}", glossaryName)
                        .replace("{CONNECTION}", connectionQN)
                        .replace("{TAG1}", tag1)
                        .replace("{TAG2}", tag2)
                output.appendText("$revised\n")
            }
        }
    }

    private fun createTags() {
        val maxNetworkRetries = 30
        val t1 = AtlanTagDef.creator(tag1, AtlanIcon.ROCKET_LAUNCH, AtlanTagColor.YELLOW).build()
        val t2 = AtlanTagDef.creator(tag2, AtlanIcon.ROCKET_LAUNCH, AtlanTagColor.YELLOW).build()
        client.typeDefs.create(
            listOf(t1, t2),
            RequestOptions.from(client).maxNetworkRetries(maxNetworkRetries).build(),
        )
    }

    private fun modifyFile() {
        // Modify the loaded file to make some changes (testing upsert)
        val input = Paths.get(testDirectory, testFile).toFile()
        val output = Paths.get(testDirectory, revisedFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                if (line.contains("/schema/test_table2")) {
                    val revised =
                        line
                            .replace("/schema/test_table2", "/SCHEMA/Test_Table2")
                            .replace("View,", "Table,Now with description")
                    output.appendText("$revised\n")
                } else {
                    output.appendText("$line\n")
                }
            }
        }
    }

    private fun createGlossary() {
        val g1 = Glossary.creator(glossaryName).build()
        val response = g1.save(client)
        val result = response.getResult(g1)
        val t1 = GlossaryTerm.creator("Test Term", result).build()
        t1.save(client)
    }

    private fun createConnection(): Connection {
        val c1 = Connection.creator(client, connectionName, connectorType).build()
        val response = c1.save(client).block()
        return response.getResult(c1)
    }

    override fun setup() {
        createGlossary()
        val connection = createConnection()
        prepFile(connection.qualifiedName)
        createTags()
        runCustomPackage(
            AssetImportCfg(
                assetsFile = Paths.get(testDirectory, testFile).toString(),
                assetsUpsertSemantic = "upsert",
                assetsAttrToOverwrite = listOf(),
                assetsConfig = "advanced",
                assetsFailOnErrors = false,
            ),
            Importer::main,
        )
    }

    override fun teardown() {
        removeConnection(connectionName, connectorType)
        removeGlossary(glossaryName)
        removeTag(tag1)
        removeTag(tag2)
    }

    @Test(groups = ["aim.lt.create"])
    fun connectionCreated() {
        val c1 = Connection.findByName(client, connectionName, connectorType)
        assertEquals(1, c1.size)
        assertEquals(connectionName, c1[0].name)
    }

    @Test(groups = ["aim.lt.create"])
    fun tableCreated() {
        val c = Connection.findByName(client, connectionName, connectorType)[0]!!
        val request =
            Table
                .select(client)
                .where(Table.QUALIFIED_NAME.startsWith(c.qualifiedName))
                .includeOnResults(Table.NAME)
                .includeOnResults(Table.SOURCE_READ_COUNT)
                .includeOnResults(Table.SOURCE_READ_USER_COUNT)
                .includeOnResults(Table.ATLAN_TAGS)
                .includeOnResults(Table.LAST_ROW_CHANGED_AT)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val tables = response.assets
        assertEquals(1, tables.size)
        assertEquals("test_table1", tables[0].name)
        assertEquals(10, tables[0].sourceReadCount)
        assertEquals(5, tables[0].sourceReadUserCount)
        assertTrue(tables[0].atlanTags.isEmpty())
        assertEquals(1708035825000L, tables[0].lastRowChangedAt)
    }

    @Test(groups = ["aim.lt.create"])
    fun viewCreated() {
        val c = Connection.findByName(client, connectionName, connectorType)[0]!!
        val request =
            View
                .select(client)
                .where(View.QUALIFIED_NAME.startsWith(c.qualifiedName))
                .includeOnResults(View.NAME)
                .includeOnResults(View.SOURCE_READ_COUNT)
                .includeOnResults(View.SOURCE_READ_USER_COUNT)
                .includeOnResults(View.ATLAN_TAGS)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val views = response.assets
        assertEquals(1, views.size)
        assertEquals("test_table2", views[0].name)
        assertEquals(3, views[0].sourceReadCount)
        assertEquals(2, views[0].sourceReadUserCount)
        assertEquals(1, views[0].atlanTags.size)
        assertEquals(tag1, views[0].atlanTags.first().typeName)
        assertFalse(views[0].atlanTags.first().propagate)
    }

    @Test(groups = ["aim.lt.create"])
    fun columnCreated() {
        val c = Connection.findByName(client, connectionName, connectorType)[0]!!
        val request =
            Column
                .select(client)
                .where(Column.QUALIFIED_NAME.startsWith(c.qualifiedName))
                .includeOnResults(Column.NAME)
                .includeOnResults(Column.ATLAN_TAGS)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val columns = response.assets
        assertEquals(1, columns.size)
        assertEquals("column1", columns[0].name)
        assertTrue(columns[0].atlanTags.isEmpty())
    }

    @Test(groups = ["aim.lt.create"])
    fun termAssigned() {
        val c = Connection.findByName(client, connectionName, connectorType)[0]!!
        val request =
            client.assets
                .select()
                .where(Asset.TYPE_NAME.`in`(setOf(Table.TYPE_NAME, View.TYPE_NAME)))
                .where(Asset.QUALIFIED_NAME.startsWith(c.qualifiedName))
                .includeOnResults(Asset.NAME)
                .includeOnResults(Asset.ASSIGNED_TERMS)
                .includeOnRelations(GlossaryTerm.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 2)
        val tables = response.assets
        assertEquals(2, tables.size)
        tables.forEach {
            assertTrue(it.name.startsWith("test_table"))
            assertEquals(1, it.assignedTerms.size)
            assertEquals("Test Term", it.assignedTerms.first().name)
            assertNull(it.description)
        }
    }

    @Test(groups = ["aim.lt.create"])
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
        assertEquals(3, assets.size)
        // Note: the revised update is table/view-agnostic, so will still be the same resulting entities
        assertEquals(setOf(Table.TYPE_NAME, View.TYPE_NAME, Column.TYPE_NAME), assets.map { it.typeName }.toSet())
        assertEquals(1, assets.count { it.typeName == Table.TYPE_NAME })
        assertEquals(1, assets.count { it.typeName == View.TYPE_NAME })
        assertEquals(1, assets.count { it.typeName == Column.TYPE_NAME })
    }

    @Test(groups = ["aim.lt.runUpdate"], dependsOnGroups = ["aim.lt.create"])
    fun upsertLT() {
        modifyFile()
        runCustomPackage(
            AssetImportCfg(
                assetsFile = Paths.get(testDirectory, revisedFile).toString(),
                assetsUpsertSemantic = "update",
                assetsConfig = "advanced",
                assetsCaseSensitive = false,
                assetsFailOnErrors = true,
                assetsTableViewAgnostic = true,
            ),
            Importer::main,
        )
        // Allow Elastic index and deletion to become consistent
        Thread.sleep(10000)
    }

    @Test(groups = ["aim.lt.update"], dependsOnGroups = ["aim.lt.runUpdate"])
    fun testRevisedTable() {
        val c = Connection.findByName(client, connectionName, connectorType)[0]!!
        val request =
            Table
                .select(client)
                .where(Table.QUALIFIED_NAME.startsWith(c.qualifiedName))
                .includeOnResults(Table.NAME)
                .includeOnResults(Table.ASSIGNED_TERMS)
                .includeOnResults(Table.DESCRIPTION)
                .includeOnResults(Table.LAST_ROW_CHANGED_AT)
                .includeOnRelations(GlossaryTerm.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val tables = response.assets
        assertEquals(1, tables.size)
        assertEquals("test_table1", tables[0].name)
        assertEquals(1, tables[0].assignedTerms.size)
        assertEquals("Test Term", tables[0].assignedTerms.first().name)
        assertNull(tables[0].description)
        assertEquals(1708035825000L, tables[0].lastRowChangedAt)
    }

    @Test(groups = ["aim.lt.update"], dependsOnGroups = ["aim.lt.runUpdate"])
    fun testRevisedView() {
        val c = Connection.findByName(client, connectionName, connectorType)[0]!!
        val request =
            View
                .select(client)
                .where(View.QUALIFIED_NAME.startsWith(c.qualifiedName))
                .includeOnResults(View.NAME)
                .includeOnResults(View.ASSIGNED_TERMS)
                .includeOnResults(View.DESCRIPTION)
                .includeOnRelations(GlossaryTerm.NAME)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val views = response.assets
        assertEquals(1, views.size)
        assertEquals("test_table2", views[0].name)
        assertEquals(1, views[0].assignedTerms.size)
        assertEquals("Test Term", views[0].assignedTerms.first().name)
        assertEquals("Now with description", views[0].description)
    }

    @Test(groups = ["aim.lt.update"], dependsOnGroups = ["aim.lt.runUpdate"])
    fun connectionCacheUpdated() {
        validateConnectionCache()
    }

    @Test(dependsOnGroups = ["aim.lt.*"])
    fun filesCreated() {
        validateFilesExist(files)
    }
}
