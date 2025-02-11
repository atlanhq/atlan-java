/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.assets.Link
import com.atlan.model.assets.Schema
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanDeleteType
import com.atlan.model.enums.AtlanStatus
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

private const val LINK_NAME = "HP"

private const val LINK_URL = "https://hp.com"

private const val DB_NAME = "DB"

/**
 * Test that after a link on an asset has been soft deleted and it's imported it becomes active
 */
class DeletedLinkTest : PackageTest("dlt") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val conn1 = makeUnique("dl1")
    private val conn1Type = AtlanConnectorType.POSTGRES
    private lateinit var connection: Connection
    private lateinit var linkGuid: String

    private val testFile = "deleted_link.csv"

    private fun prepFile(connectionQN: String = connection.qualifiedName) {
        // Prepare a copy of the file with unique names for connections
        val input = Paths.get("src", "test", "resources", testFile).toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("iceberg", conn1Type.value)
                        .replace("{{CONNECTION}}", connectionQN)
                output.appendText("$revised\n")
            }
        }
    }

    private val connectionAttrs: List<AtlanField> =
        listOf(
            Connection.NAME,
            Connection.CONNECTOR_TYPE,
            Connection.ADMIN_ROLES,
            Connection.ADMIN_GROUPS,
            Connection.ADMIN_USERS,
        )

    private val databaseAttrs: List<AtlanField> =
        listOf(
            Database.NAME,
            Database.CONNECTION_QUALIFIED_NAME,
            Database.CONNECTOR_TYPE,
            Database.LINKS,
        )

    private fun createConnection(): Connection {
        val c1 = Connection.creator(client, conn1, conn1Type).build()
        val response = c1.save(client).block()
        return response.getResult(c1)
    }

    override fun setup() {
        connection = createConnection()
        prepFile()
    }

    override fun teardown() {
        removeConnection(conn1, conn1Type)
    }

    @Test(groups = ["ai.dl.create"])
    fun connection1Created() {
        validateConnection()
    }

    private fun validateConnection() {
        val found = Connection.findByName(client, conn1, conn1Type, connectionAttrs)
        assertNotNull(found)
        assertEquals(1, found.size)
        val c1 = found[0]
        assertEquals(conn1, c1.name)
        assertEquals(conn1Type, c1.connectorType)
    }

    @Test(groups = ["ai.dl.import_1"], dependsOnGroups = ["ai.dl.create"])
    private fun firstImport() {
        importCsv()

        validateLinkPresent()
    }

    @Test(groups = ["ai.dl.import_1"], dependsOnGroups = ["ai.dl.import_1"])
    private fun secondImport() {
        client.assets.delete(linkGuid, AtlanDeleteType.SOFT).block()
        importCsv()

        validateLinkPresent()
    }

    private fun importCsv() {
        runCustomPackage(
            AssetImportCfg(
                assetsFile = Paths.get(testDirectory, testFile).toString(),
                assetsUpsertSemantic = "upsert",
                assetsFailOnErrors = false,
                assetsCaseSensitive = true,
                assetsTableViewAgnostic = false,
                assetsFieldSeparator = ",",
                assetsBatchSize = 20,
            ),
            Importer::main,
        )
    }

    private fun validateLinkPresent() {
        val c1 = Connection.findByName(client, conn1, conn1Type, connectionAttrs)[0]!!
        val request =
            Database
                .select(client)
                .where(Database.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
                .includesOnResults(databaseAttrs)
                .includeOnRelations(Schema.NAME)
                .includeOnRelations(Link.NAME)
                .includeOnRelations(Link.LINK)
                .includeOnRelations(Link.STATUS)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.assets
        assertEquals(1, found.size)
        val db = found[0] as Database
        assertEquals(DB_NAME, db.name)
        assertEquals(c1.qualifiedName, db.connectionQualifiedName)
        assertEquals(conn1Type, db.connectorType)
        val links = db.links
        assertEquals(1, links.size)
        var link = links.elementAt(0)
        assertEquals(LINK_NAME, link.name)
        assertEquals(LINK_URL, link.link)
        assertTrue { link.qualifiedName.startsWith(db.qualifiedName) }
        link = Link.get(client, link.guid)
        assertEquals(AtlanStatus.ACTIVE, link.status)
        linkGuid = link.guid
    }
}
