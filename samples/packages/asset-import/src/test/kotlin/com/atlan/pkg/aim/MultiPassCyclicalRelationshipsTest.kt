/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Connection
import com.atlan.model.assets.ModelDataModel
import com.atlan.model.assets.ModelEntity
import com.atlan.model.assets.ModelVersion
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.search.IndexSearchResponse
import com.atlan.net.HttpClient
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import org.testng.Assert.assertTrue
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test import of an assets file that has both ends of the same cyclical relationship in it.
 */
class MultiPassCyclicalRelationshipsTest : PackageTest("mpcr") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val connectionName = makeUnique("c1")
    private val connectorType = AtlanConnectorType.MODEL

    private val testFile = "valid-cyclical-relationships.csv"

    private val files =
        listOf(
            testFile,
            "debug.log",
        )

    private val modelAttrs =
        setOf(
            ModelDataModel.NAME,
            ModelDataModel.MODEL_VERSIONS,
            ModelDataModel.MODEL_TYPE,
        )

    private val versionAttrs =
        setOf(
            ModelVersion.NAME,
            ModelDataModel.MODEL_TYPE,
            ModelVersion.MODEL_NAME,
            ModelVersion.MODEL_QUALIFIED_NAME,
            ModelVersion.MODEL_DATA_MODEL,
        )

    private val entityAttrs =
        setOf(
            ModelEntity.NAME,
            ModelDataModel.MODEL_TYPE,
            ModelEntity.MODEL_QUALIFIED_NAME,
            ModelEntity.MODEL_NAME,
            ModelEntity.MODEL_VERSIONS,
            ModelEntity.MODEL_VERSION_NAME,
            ModelEntity.MODEL_VERSION_QUALIFIED_NAME,
            ModelEntity.MODEL_ENTITY_MAPPED_TO_ENTITIES,
            ModelEntity.MODEL_ENTITY_MAPPED_FROM_ENTITIES,
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
        val c1 = Connection.creator(client, connectionName, connectorType).build()
        val response = c1.save(client).block()
        return response.getResult(c1)
    }

    override fun setup() {
        val connection = createConnection()
        prepFile(connection.qualifiedName)
        runCustomPackage(
            AssetImportCfg(
                assetsFile = Paths.get(testDirectory, testFile).toString(),
                assetsUpsertSemantic = "upsert",
            ),
            Importer::main,
        )
    }

    override fun teardown() {
        removeConnection(connectionName, connectorType)
    }

    @Test
    fun connectionCreated() {
        val c1 = Connection.findByName(client, connectionName, connectorType, listOf(Connection.CONNECTOR_TYPE))
        assertNotNull(c1)
        assertEquals(1, c1.size)
        assertEquals(connectionName, c1.first().name)
        assertEquals(connectorType, c1.first().connectorType)
    }

    @Test
    fun dataModelsCreated() {
        val c1 = Connection.findByName(client, connectionName, connectorType)[0]!!
        val request =
            ModelDataModel
                .select(client)
                .where(ModelDataModel.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .includesOnResults(modelAttrs)
                .toRequest()
        val response = retrySearchUntil(request, 2)
        assertNotNull(response)
        assertEquals(2, response.assets.size)
        assertEquals(setOf(ModelDataModel.TYPE_NAME), response.assets.map { it.typeName }.toSet())
        assertEquals(setOf("LDM", "PDM"), response.assets.map { it.name }.toSet())
        assertEquals(setOf("LOGICAL", "PHYSICAL"), response.assets.map { (it as ModelDataModel).modelType }.toSet())
    }

    @Test
    fun versionsCreated() {
        val c1 = Connection.findByName(client, connectionName, connectorType)[0]!!
        val request =
            ModelVersion
                .select(client)
                .where(ModelVersion.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .includesOnResults(versionAttrs)
                .toRequest()
        val response = retrySearchUntil(request, 2)
        assertNotNull(response)
        assertEquals(2, response.assets.size)
        assertEquals(setOf(ModelVersion.TYPE_NAME), response.assets.map { it.typeName }.toSet())
        assertEquals(setOf("v1"), response.assets.map { it.name }.toSet())
        assertEquals(setOf("LOGICAL", "PHYSICAL"), response.assets.map { (it as ModelVersion).modelType }.toSet())
    }

    @Test
    fun entitiesCreated() {
        val c1 = Connection.findByName(client, connectionName, connectorType)[0]!!
        val request =
            ModelEntity
                .select(client)
                .where(ModelEntity.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .includesOnResults(entityAttrs)
                .toRequest()
        val response = retrySearchUntil(request, 2)
        assertNotNull(response)
        assertEquals(2, response.assets.size)
        assertEquals(setOf(ModelEntity.TYPE_NAME), response.assets.map { it.typeName }.toSet())
        assertEquals(setOf("L_E1", "P_E2"), response.assets.map { it.name }.toSet())
        assertEquals(setOf("LOGICAL", "PHYSICAL"), response.assets.map { (it as ModelEntity).modelType }.toSet())
        assertEquals(setOf("LDM", "PDM"), response.assets.map { (it as ModelEntity).modelName }.toSet())
        response.assets.forEach {
            assertTrue(it is ModelEntity)
            val entity = it as ModelEntity
            assertNotNull(entity.modelVersions)
            assertEquals(1, entity.modelVersions.size)
        }
    }

    @Test
    fun entitiesRelated() {
        val c1 = Connection.findByName(client, connectionName, connectorType)[0]!!
        val request =
            ModelEntity
                .select(client)
                .where(ModelEntity.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .includesOnResults(entityAttrs)
                .includeOnRelations(ModelEntity.NAME)
                .toRequest()
        var response: IndexSearchResponse
        var count = 0
        do {
            Thread.sleep(HttpClient.waitTime(count).toMillis())
            response = retrySearchUntil(request, 2)
            val to =
                response.assets
                    .flatMap { (it as ModelEntity).modelEntityMappedToEntities }
                    .filterNotNull()
                    .toSet()
            val from =
                response.assets
                    .flatMap { (it as ModelEntity).modelEntityMappedFromEntities }
                    .filterNotNull()
                    .toSet()
            count++
        } while ((to.isEmpty() || from.isEmpty()) && count < client.maxNetworkRetries)
        assertNotNull(response)
        assertEquals(2, response.assets.size)
        assertEquals(setOf(ModelEntity.TYPE_NAME), response.assets.map { it.typeName }.toSet())
        response.assets.forEach {
            val entity = it as ModelEntity
            when (entity.name) {
                "L_E1" -> {
                    assertEquals(1, entity.modelEntityMappedToEntities.size)
                    assertTrue(entity.modelEntityMappedFromEntities.isNullOrEmpty())
                    assertEquals("P_E2", entity.modelEntityMappedToEntities.first().name)
                }

                "P_E2" -> {
                    assertTrue(entity.modelEntityMappedToEntities.isNullOrEmpty())
                    assertEquals(1, entity.modelEntityMappedFromEntities.size)
                    assertEquals("L_E1", entity.modelEntityMappedFromEntities.first().name)
                }
            }
        }
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
