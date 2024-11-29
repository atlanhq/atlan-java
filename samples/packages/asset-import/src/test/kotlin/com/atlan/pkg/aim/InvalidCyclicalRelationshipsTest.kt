/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageTest
import mu.KotlinLogging
import org.testng.Assert.assertTrue
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

/**
 * Test import of an assets file that has both ends of the same cyclical relationship in it.
 */
class InvalidCyclicalRelationshipsTest : PackageTest("icr") {
    override val logger = KotlinLogging.logger {}

    private val connectionName = makeUnique("c1")
    private val connectorType = AtlanConnectorType.ADOBE_EXPERIENCE_MANAGER

    private val testFile = "invalid-cyclical-relationships.csv"

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
        val c1 = Connection.creator(connectionName, connectorType).build()
        val response = c1.save(client).block()
        return response.getResult(c1)
    }

    override fun setup() {
        val connection = createConnection()
        prepFile(connection.qualifiedName)
    }

    override fun teardown() {
        removeConnection(connectionName, connectorType)
    }

    @Test
    fun packageFails() {
        val exception =
            assertFailsWith<IllegalStateException> {
                runCustomPackage(
                    AssetImportCfg(
                        assetsFile = Paths.get(testDirectory, testFile).toString(),
                        assetsUpsertSemantic = "upsert",
                        assetsAttrToOverwrite = null,
                        assetsFailOnErrors = true,
                    ),
                    Importer::main,
                )
            }
        assertEquals(
            """
            Both ends of the same relationship found in the input file for type ModelEntity: modelEntityMappedToEntities <> modelEntityMappedFromEntities.
            You should only use one end of this relationship or the other when importing.
            """.trimIndent(),
            exception.message,
        )
    }

    @Test
    fun connectionCreated() {
        val c1 = Connection.findByName(connectionName, connectorType, listOf(Connection.CONNECTOR_TYPE))
        assertNotNull(c1)
        assertEquals(1, c1.size)
        assertEquals(connectionName, c1.first().name)
        assertEquals(connectorType, c1.first().connectorType)
    }

    @Test
    fun noAssetsCreated() {
        val c1 = Connection.findByName(connectionName, connectorType)[0]!!
        val assets =
            client.assets.select()
                .where(Asset.QUALIFIED_NAME.startsWith(c1.qualifiedName))
                .stream()
                .toList()
        assertEquals(1, assets.size)
        assertTrue(assets[0] is Connection)
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
    }
}
