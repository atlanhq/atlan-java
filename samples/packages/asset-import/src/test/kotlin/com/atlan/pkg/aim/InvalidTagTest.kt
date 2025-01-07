/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Table
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Test import of an invalid tag value.
 */
class InvalidTagTest : PackageTest("it") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val table = makeUnique("t1")

    private val testFile = "invalid_tag.csv"

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
            assertFailsWith<IllegalStateException> {
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
            "Unable to serialize: com.atlan.api.AssetEndpoint\$BulkEntityRequest -- Unexpected IOException (of type java.io.IOException): Unable to find Atlan tag with name: NON_EXISTENT_TAG",
            exception.message,
        )
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
    }
}
