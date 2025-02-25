/* SPDX-License-Identifier: Apache-2.0
   Copyright 2025 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Connection
import com.atlan.model.assets.DataDomain
import com.atlan.model.assets.Table
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanDeleteType
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import org.testng.annotations.Test
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ImportDomainRelationshipTest : PackageTest("idr") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val connectionName = makeUnique("c1")
    private val domainName = connectionName
    private val connectorType = AtlanConnectorType.SNOWFLAKE

    private val testFile = "domain_relationships.csv"
    private val files =
        listOf(
            testFile,
            "debug.log",
        )

    private fun prepFile(connectionQN: String) {
        // Prepare a copy of the file with unique names for the connection and domain
        val input = Paths.get("src", "test", "resources", testFile).toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{{CONNECTION}}", connectionQN)
                        .replace("{{DOMAIN_NAME}}", domainName)
                        .replace("{{CONNECTION_NAME}}", connectionName)
                output.appendText("$revised\n")
            }
        }
    }

    private fun createConnection(): Connection {
        val c1 = Connection.creator(client, connectionName, connectorType).build()
        val response = c1.save(client).block()
        return response.getResult(c1)
    }

    private fun createDomain() {
        val dmn1 = DataDomain.creator(domainName).build()
        val response = dmn1.save(client)
        response.getResult(dmn1)
    }

    private fun archiveTable() {
        val connection = Connection.findByName(client, connectionName, connectorType)?.get(0)?.qualifiedName!!
        val request =
            Table
                .select(client)
                .where(Table.QUALIFIED_NAME.startsWith(connection))
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val guids =
            response
                .stream()
                .map { it.guid }
                .toList()
        client.assets.delete(guids, AtlanDeleteType.HARD).block()
        val domain = DataDomain.select(client).where(DataDomain.NAME.startsWith(connectionName)).toRequest()
        val domainResponse = retrySearchUntil(domain, 1)
        val domainGuids =
            domainResponse
                .stream()
                .map { it.guid }
                .toList()
        client.assets.delete(domainGuids, AtlanDeleteType.HARD).block()
    }

    override fun setup() {
        val connection = createConnection()
        prepFile(connection.qualifiedName)
//        createAssets()
        createDomain()
        runCustomPackage(
            AssetImportCfg(
                assetsFile = Paths.get(testDirectory, testFile).toString(),
                assetsUpsertSemantic = "upsert",
            ),
            Importer::main,
        )
    }

    override fun teardown() {
        archiveTable()
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
    fun domainCreated() {
        val d1 = DataDomain.findByName(client, domainName)
        assertNotNull(d1)
        assertEquals(1, d1.size)
        assertEquals(domainName, d1.first().name)
    }

    @Test
    fun isDomainAssetRelationship() {
        val domain = DataDomain.select(client).where(DataDomain.NAME.startsWith(connectionName)).toRequest()
        val domainResponse = retrySearchUntil(domain, 1)
        val domains =
            domainResponse
                .stream()
                .map { (it as DataDomain) }
                .toList()

        val tables = Table.select(client).where(Table.DOMAIN_GUIDS.`in`(domains.map { it.guid })).toRequest()
        val tableResponse = retrySearchUntil(tables, 1)
        val tablesList =
            tableResponse
                .stream()
                .map { it as Table }
                .toList()
        assertEquals(1, tablesList.size)
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
