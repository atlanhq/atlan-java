/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import PackageConfig.name
import RelationalAssetsBuilderCfg
import com.atlan.Atlan
import com.atlan.model.assets.Column
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Database
import com.atlan.model.assets.Schema
import com.atlan.model.assets.Table
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.PackageTest
import org.testng.ITestContext
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test creation of relational assets where one of the columns has an invalid parent.
 */
class InvalidParentTest : PackageTest() {

    private val conn1 = makeUnique("c1")
    private val conn1Type = AtlanConnectorType.AZURE_COSMOS_DB

    private val testFile = "input.csv"
    private val revisedFile = "revised.csv"

    private val files = listOf(
        testFile,
        "debug.log",
    )

    private fun prepFile() {
        // Prepare a copy of the file with unique names for connections
        val input = Paths.get("src", "test", "resources", "rab.csv").toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised = line
                    .replace("COSMOS-john", conn1)
                output.appendText("$revised\n")
            }
        }
    }

    private val connectionAttrs: List<AtlanField> = listOf(
        Connection.NAME,
        Connection.CONNECTOR_TYPE,
        Connection.ADMIN_ROLES,
        Connection.ADMIN_GROUPS,
        Connection.ADMIN_USERS,
    )

    private val databaseAttrs: List<AtlanField> = listOf(
        Database.NAME,
        Database.CONNECTION_QUALIFIED_NAME,
        Database.CONNECTOR_TYPE,
        Database.DISPLAY_NAME,
        Database.DESCRIPTION,
        Database.SCHEMA_COUNT,
        Database.SCHEMAS,
    )

    private val schemaAttrs: List<AtlanField> = listOf(
        Schema.NAME,
        Schema.CONNECTION_QUALIFIED_NAME,
        Schema.CONNECTOR_TYPE,
        Schema.DISPLAY_NAME,
        Schema.DESCRIPTION,
        Schema.DATABASE_NAME,
        Schema.DATABASE_QUALIFIED_NAME,
        Schema.TABLE_COUNT,
        Schema.VIEW_COUNT,
        Schema.TABLES,
        Schema.VIEWS,
    )

    private val tableAttrs: List<AtlanField> = listOf(
        Table.NAME,
        Table.CONNECTION_QUALIFIED_NAME,
        Table.CONNECTOR_TYPE,
        Table.DATABASE_NAME,
        Table.DATABASE_QUALIFIED_NAME,
        Table.SCHEMA_NAME,
        Table.SCHEMA_QUALIFIED_NAME,
        Table.DISPLAY_NAME,
        Table.DESCRIPTION,
        Table.CERTIFICATE_STATUS,
        Table.CERTIFICATE_STATUS_MESSAGE,
        Table.README,
        Table.ATLAN_TAGS,
        Table.COLUMN_COUNT,
        Table.COLUMNS,
    )

    private val columnAttrs: List<AtlanField> = listOf(
        Column.NAME,
        Column.CONNECTION_QUALIFIED_NAME,
        Column.CONNECTOR_TYPE,
        Column.DATABASE_NAME,
        Column.DATABASE_QUALIFIED_NAME,
        Column.SCHEMA_NAME,
        Column.SCHEMA_QUALIFIED_NAME,
        Column.TABLE_NAME,
        Column.TABLE_QUALIFIED_NAME,
        Column.VIEW_NAME,
        Column.VIEW_QUALIFIED_NAME,
        Column.DISPLAY_NAME,
        Column.DESCRIPTION,
        Column.DATA_TYPE,
        Column.ORDER,
    )

    @BeforeClass
    fun beforeClass() {
        prepFile()
        setup(
            RelationalAssetsBuilderCfg(
                assetsFile = Paths.get(testDirectory, testFile).toString(),
                assetsUpsertSemantic = "upsert",
                assetsFailOnErrors = true,
            ),
        )
        Importer.main(arrayOf())
    }

    @Test(groups = ["create"])
    fun connection1Created() {
        validateConnection()
    }

    private fun validateConnection() {
        val found = Connection.findByName(conn1, conn1Type, connectionAttrs)
        assertNotNull(found)
        assertEquals(1, found.size)
        val c1 = found[0]
        assertEquals(conn1, c1.name)
        assertEquals(conn1Type, c1.connectorType)
        val adminRoleId = Atlan.getDefaultClient().roleCache.getIdForName("\$admin")
        assertEquals(setOf(adminRoleId), c1.adminRoles)
        val apiToken = Atlan.getDefaultClient().users.currentUser.username
        assertEquals(setOf(apiToken), c1.adminUsers)
    }

    fun database1Created() {
        val c1 = Connection.findByName(conn1, conn1Type, connectionAttrs)[0]!!
        val request = Database.select()
            .where(Database.CONNECTION_QUALIFIED_NAME.eq(c1.qualifiedName))
            .includesOnResults(databaseAttrs)
            .includeOnRelations(Schema.NAME)
            .toRequest()
        val response = retrySearchUntil(request, 1)
        val found = response.stream().toList()
        assertEquals(1, found.size)
        val db = found[0] as Database
        assertEquals("cosmosdb2", db.name)
        assertEquals(c1.qualifiedName, db.connectionQualifiedName)
        assertEquals(conn1Type, db.connectorType)
    }

    @AfterClass(alwaysRun = true)
    fun afterClass(context: ITestContext) {
        removeConnection(conn1, conn1Type)
        teardown(context.failedTests.size() > 0)
    }
}
