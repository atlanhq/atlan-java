/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Table
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import org.testng.Assert.assertTrue
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Test import of an invalid enumerated value in custom metadata.
 */
class InvalidCMFailureTest : PackageTest("icmf") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val table = makeUnique("t1")

    private val testFile = "invalid-cm-value.csv"

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
                        .replace("{{TABLE}}", table)
                output.appendText("$revised\n")
            }
        }
    }

    override fun setup() {
        val snowflakeConnection = Connection.findByName(client, "development", AtlanConnectorType.SNOWFLAKE)?.get(0)!!
        prepFile(snowflakeConnection.qualifiedName)
    }

    override fun teardown() {
        val snowflakeConnection = Connection.findByName(client, "development", AtlanConnectorType.SNOWFLAKE)?.get(0)!!
        Table
            .select(client)
            .where(Table.CONNECTION_QUALIFIED_NAME.eq(snowflakeConnection.qualifiedName))
            .where(Table.NAME.eq(table))
            .stream()
            .findFirst()
            .ifPresent {
                Table.purge(client, it.guid)
            }
    }

    @Test
    fun failsWithMeaningfulError() {
        val exception =
            assertFailsWith<IllegalArgumentException> {
                runCustomPackage(
                    AssetImportCfg(
                        assetsFile = Paths.get(testDirectory, testFile).toString(),
                        assetsUpsertSemantic = "upsert",
                        assetsFailOnErrors = true,
                    ),
                    Importer::main,
                )
            }
        assertEquals(
            """
            Invalid value provided for custom metadata Data Quality attribute Type: Freshness.
            You must use one of the valid values defined for the options associated with this attribute.
            """.trimIndent(),
            exception.message,
        )
    }

    @Test
    fun tableNotCreated() {
        val snowflakeConnection = Connection.findByName(client, "development", AtlanConnectorType.SNOWFLAKE)?.get(0)!!
        val tablesFound =
            Table
                .select(client)
                .where(Table.CONNECTION_QUALIFIED_NAME.eq(snowflakeConnection.qualifiedName))
                .where(Table.NAME.eq(table))
                .stream()
                .toList()
        assertTrue(tablesFound.isEmpty())
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
    }
}
