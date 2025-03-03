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
import com.atlan.model.enums.LinkIdempotencyInvariant
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

private const val LINK_NAME1 = "Example"
private const val LINK_NAME2 = "Another"
private const val LINK_NAME3 = "Final"
private const val LINK_URL1 = "https://example.com"
private const val LINK_URL2 = "https://example.com/another"
private const val LINK_URL3 = "https://example.com/final"
private const val DB_NAME = "DB"

/**
 * Test that links are updated rather than being duplicated, for various scenarios.
 */
class AvoidDuplicateLinkTest : PackageTest("adl") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val conn1 = makeUnique("adl1")
    private val conn1Type = AtlanConnectorType.YUGABYTEDB
    private lateinit var connection: Connection
    private var linkGuid1: String? = null
    private var linkGuid2: String? = null
    private var linkGuid3: String? = null

    private val testFile = "idempotent_link.csv"

    private fun prepFile(
        connectionQN: String,
        linkName: String,
        linkURL: String,
        filename: String,
    ) {
        // Prepare a copy of the file with unique names for connections
        val input = Paths.get("src", "test", "resources", testFile).toFile()
        val output = Paths.get(testDirectory, filename).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{{CTYPE}}", conn1Type.value)
                        .replace("{{CONNECTION}}", connectionQN)
                        .replace("{{NAME}}", linkName)
                        .replace("{{URL}}", linkURL)
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
    }

    override fun teardown() {
        if (linkGuid1 != null) {
            client.assets.delete(linkGuid1, AtlanDeleteType.PURGE)
        }
        if (linkGuid2 != null) {
            client.assets.delete(linkGuid2, AtlanDeleteType.PURGE)
        }
        if (linkGuid3 != null) {
            client.assets.delete(linkGuid3, AtlanDeleteType.PURGE)
        }
        removeConnection(conn1, conn1Type)
    }

    @Test(groups = ["aim.adl.create"])
    fun connection1Created() {
        val found = Connection.findByName(client, conn1, conn1Type, connectionAttrs)
        assertNotNull(found)
        assertEquals(1, found.size)
        val c1 = found[0]
        assertEquals(conn1, c1.name)
        assertEquals(conn1Type, c1.connectorType)
    }

    @Test(groups = ["aim.adl.base"], dependsOnGroups = ["aim.adl.create"])
    private fun baseline() {
        val filename = "base.csv"
        prepFile(
            connection.qualifiedName,
            LINK_NAME1,
            LINK_URL1,
            filename,
        )
        importCsv(filename, LinkIdempotencyInvariant.URL)
        validateLinkPresent(
            LINK_NAME1,
            LINK_URL1,
        )
    }

    @Test(groups = ["aim.adl.name"], dependsOnGroups = ["aim.adl.base"])
    private fun idempotentName() {
        val filename = "idempotent_name.csv"
        prepFile(
            connection.qualifiedName,
            LINK_NAME1,
            LINK_URL2,
            filename,
        )
        importCsv(filename, LinkIdempotencyInvariant.NAME)
        validateLinkPresent(
            LINK_NAME1,
            LINK_URL2,
        )
    }

    @Test(groups = ["aim.adl.url"], dependsOnGroups = ["aim.adl.name"])
    private fun idempotentURL() {
        val filename = "idempotent_url.csv"
        prepFile(
            connection.qualifiedName,
            LINK_NAME2,
            LINK_URL2,
            filename,
        )
        importCsv(filename, LinkIdempotencyInvariant.URL)
        validateLinkPresent(
            LINK_NAME2,
            LINK_URL2,
        )
    }

    @Test(groups = ["aim.adl.addName"], dependsOnGroups = ["aim.adl.url"])
    private fun idempotentAddName() {
        val filename = "add_name.csv"
        prepFile(
            connection.qualifiedName,
            LINK_NAME1,
            LINK_URL1,
            filename,
        )
        importCsv(filename, LinkIdempotencyInvariant.NAME)
        validateTwoLinks()
    }

    @Test(groups = ["aim.adl.addURL"], dependsOnGroups = ["aim.adl.addName"])
    private fun idempotentAddURL() {
        val filename = "add_url.csv"
        prepFile(
            connection.qualifiedName,
            LINK_NAME3,
            LINK_URL3,
            filename,
        )
        importCsv(filename, LinkIdempotencyInvariant.URL)
        validateThreeLinks()
    }

    private fun importCsv(
        filename: String,
        idempotency: LinkIdempotencyInvariant,
    ) {
        runCustomPackage(
            AssetImportCfg(
                assetsFile = Paths.get(testDirectory, filename).toString(),
                assetsUpsertSemantic = "upsert",
                assetsFailOnErrors = false,
                assetsLinkIdempotency = idempotency.value,
            ),
            Importer::main,
        )
    }

    private fun validateLinkPresent(
        name: String,
        url: String,
    ) {
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
        assertEquals(name, link.name)
        assertEquals(url, link.link)
        assertTrue { link.qualifiedName.startsWith(db.qualifiedName) }
        link = Link.get(client, link.guid)
        assertEquals(AtlanStatus.ACTIVE, link.status)
        if (linkGuid1 == null) {
            linkGuid1 = link.guid
        } else {
            assertEquals(linkGuid1, link.guid)
        }
    }

    private fun validateTwoLinks() {
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
        assertEquals(2, links.size)
        val names = links.map { it.name }.toSet()
        assertEquals(setOf(LINK_NAME1, LINK_NAME2), names)
        val urls = links.map { it.link }.toSet()
        assertEquals(setOf(LINK_URL1, LINK_URL2), urls)
        links.filter { it.name == LINK_NAME2 }.forEach { linkGuid2 = it.guid }
    }

    private fun validateThreeLinks() {
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
        assertEquals(3, links.size)
        val names = links.map { it.name }.toSet()
        assertEquals(setOf(LINK_NAME1, LINK_NAME2, LINK_NAME3), names)
        val urls = links.map { it.link }.toSet()
        assertEquals(setOf(LINK_URL1, LINK_URL2, LINK_URL3), urls)
        links.filter { it.name == LINK_NAME3 }.forEach { linkGuid3 = it.guid }
    }
}
