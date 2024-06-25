/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import RelationalAssetsBuilderCfg
import com.atlan.model.assets.Connection
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.PackageTest
import org.testng.annotations.Test
import java.nio.file.Paths
import kotlin.IllegalStateException
import kotlin.test.assertFailsWith

/**
 * Test creation of relational assets where one of the columns has an invalid parent.
 */
class InvalidParentTest : PackageTest() {

    private val conn1 = makeUnique("c1")
    private val conn1Type = AtlanConnectorType.AZURE_COSMOS_DB

    private val testFile = "input.csv"

    private val files = listOf(
        testFile,
        "debug.log",
    )

    private fun prepFile() {
        // Prepare a copy of the file with unique names for connections
        val input = Paths.get("src", "test", "resources", "invalid_parent.csv").toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised = line
                    .replace("{CONNECTION}", conn1)
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

    override fun setup() {
        prepFile()
        setup(
            RelationalAssetsBuilderCfg(
                assetsFile = Paths.get(testDirectory, testFile).toString(),
                assetsUpsertSemantic = "upsert",
            ),
        )
        assertFailsWith(IllegalStateException::class, "Could not find any table/view at: $conn1/azure-cosmos-db/cosmosdb/xyz/schemaMismatch") {
            Importer.main(arrayOf(testDirectory))
        }
    }

    override fun teardown() {
        removeConnection(conn1, conn1Type)
    }

    @Test
    fun placeholder() {
        // do nothing -- only test is that the import fails as part of BeforeClass
    }
}
