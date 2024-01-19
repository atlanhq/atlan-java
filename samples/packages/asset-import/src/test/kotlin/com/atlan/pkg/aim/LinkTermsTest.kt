/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.Atlan
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.assets.KafkaTopic
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageTest
import org.testng.ITestContext
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Test import of a very simple file containing assigned terms.
 */
class LinkTermsTest : PackageTest() {

    private val glossaryName = makeUnique("ltg1")
    private val connectionName = makeUnique("ltc1")

    private val testFile = "input.csv"
    private val revisedFile = "revised.csv"

    private val files = listOf(
        testFile,
        "debug.log",
    )

    private fun prepFile(connectionQN: String) {
        // Prepare a copy of the file with unique names for connection and glossary
        val input = Paths.get("src", "test", "resources", "link_terms.csv").toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised = line
                    .replace("{GLOSSARY}", glossaryName)
                    .replace("{CONNECTION}", connectionQN)
                output.appendText("$revised\n")
            }
        }
    }

    private fun modifyFile() {
        // Modify the loaded file to make some changes (testing upsert)
        val input = Paths.get(testDirectory, testFile).toFile()
        val output = Paths.get(testDirectory, revisedFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                if (line.contains("/topic/")) {
                    val revised = line
                        .replace("/topic/", "/TOPIC/")
                        .replace("KafkaTopic,", "KafkaTopic,Now with description")
                    output.appendText("$revised\n")
                } else {
                    output.appendText("$line\n")
                }
            }
        }
    }

    private fun createGlossary() {
        val client = Atlan.getDefaultClient()
        val g1 = Glossary.creator(glossaryName).build()
        val response = g1.save(client)
        val result = response.getResult(g1)
        val t1 = GlossaryTerm.creator("Test Term", result).build()
        t1.save(client)
    }

    private fun createConnection(): Connection {
        val client = Atlan.getDefaultClient()
        val c1 = Connection.creator(connectionName, AtlanConnectorType.KAFKA, listOf(client.roleCache.getIdForName("\$admin")), null, null).build()
        val response = c1.save(client).block()
        return response.getResult(c1)
    }

    @BeforeClass
    fun beforeClass() {
        createGlossary()
        val connection = createConnection()
        prepFile(connection.qualifiedName)
        setup(
            AssetImportCfg(
                assetsFile = Paths.get(testDirectory, testFile).toString(),
                assetsUpsertSemantic = "upsert",
                assetsAttrToOverwrite = listOf(),
                assetsFailOnErrors = false,
                glossariesFile = null,
                glossariesUpsertSemantic = null,
                glossariesAttrToOverwrite = null,
                glossariesFailOnErrors = false,
            ),
        )
        Importer.main(arrayOf())
    }

    @Test(groups = ["create"])
    fun connectionCreated() {
        val c1 = Connection.findByName(connectionName, AtlanConnectorType.KAFKA)
        assertEquals(1, c1.size)
        assertEquals(connectionName, c1[0].name)
    }

    @Test(groups = ["create"])
    fun assetCreated() {
        val c = Connection.findByName(connectionName, AtlanConnectorType.KAFKA)[0]!!
        val request = KafkaTopic.select()
            .where(KafkaTopic.QUALIFIED_NAME.startsWith(c.qualifiedName))
            .includeOnResults(KafkaTopic.NAME)
            .includeOnResults(KafkaTopic.SOURCE_READ_COUNT)
            .includeOnResults(KafkaTopic.SOURCE_READ_USER_COUNT)
            .toRequest()
        val response = retrySearchUntil(request, 1)
        val topics = response.assets
        assertEquals(1, topics.size)
        assertEquals("test_topic", topics[0].name)
        assertEquals(10, topics[0].sourceReadCount)
        assertEquals(5, topics[0].sourceReadUserCount)
    }

    @Test(groups = ["create"])
    fun termAssigned() {
        val c = Connection.findByName(connectionName, AtlanConnectorType.KAFKA)[0]!!
        val request = KafkaTopic.select()
            .where(KafkaTopic.QUALIFIED_NAME.startsWith(c.qualifiedName))
            .includeOnResults(KafkaTopic.NAME)
            .includeOnResults(KafkaTopic.ASSIGNED_TERMS)
            .includeOnRelations(GlossaryTerm.NAME)
            .toRequest()
        val response = retrySearchUntil(request, 1)
        val topics = response.assets
        assertEquals(1, topics.size)
        assertEquals("test_topic", topics[0].name)
        assertEquals(1, topics[0].assignedTerms.size)
        assertEquals("Test Term", topics[0].assignedTerms.first().name)
        assertNull(topics[0].description)
    }

    @Test(groups = ["runUpdate"], dependsOnGroups = ["create"])
    fun upsertRevisions() {
        modifyFile()
        setup(
            AssetImportCfg(
                assetsFile = Paths.get(testDirectory, revisedFile).toString(),
                assetsUpsertSemantic = "update",
                assetsCaseSensitive = false,
                assetsFailOnErrors = true,
            ),
        )
        Importer.main(arrayOf())
        // Allow Elastic index and deletion to become consistent
        Thread.sleep(10000)
    }

    @Test(groups = ["update"], dependsOnGroups = ["runUpdate"])
    fun testRevisions() {
        val c = Connection.findByName(connectionName, AtlanConnectorType.KAFKA)[0]!!
        val request = KafkaTopic.select()
            .where(KafkaTopic.QUALIFIED_NAME.startsWith(c.qualifiedName))
            .includeOnResults(KafkaTopic.NAME)
            .includeOnResults(KafkaTopic.ASSIGNED_TERMS)
            .includeOnResults(KafkaTopic.DESCRIPTION)
            .includeOnRelations(GlossaryTerm.NAME)
            .toRequest()
        val response = retrySearchUntil(request, 1)
        val topics = response.assets
        assertEquals(1, topics.size)
        assertEquals("test_topic", topics[0].name)
        assertEquals(1, topics[0].assignedTerms.size)
        assertEquals("Test Term", topics[0].assignedTerms.first().name)
        assertEquals("Now with description", topics[0].description)
    }

    @Test(dependsOnGroups = ["create", "runUpdate", "update"])
    fun filesCreated() {
        validateFilesExist(files)
    }

    @AfterClass(alwaysRun = true)
    fun afterClass(context: ITestContext) {
        removeConnection(connectionName, AtlanConnectorType.KAFKA)
        removeGlossary(glossaryName)
        teardown(context.failedTests.size() > 0)
    }
}
