/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.model.assets.Connection
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
class MultiConnectionDeltaTest : PackageTest("mcd") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val table = makeUnique("t1")

    private val testFile = "multi-connection-delta.csv"

    private val files =
        listOf(
            testFile,
            "debug.log",
        )

    private fun prepFile(
        connectionQN1: String,
        connectionQN2: String,
    ) {
        val input = Paths.get("src", "test", "resources", testFile).toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{{CONNECTION1}}", connectionQN1)
                        .replace("{{CONNECTION2}}", connectionQN2)
                        .replace("{{TABLE}}", table)
                output.appendText("$revised\n")
            }
        }
    }

    override fun setup() {
        val snowflakeConnection = Connection.findByName(client, "production", AtlanConnectorType.SNOWFLAKE)?.get(0)!!
        val mssqlConnection = Connection.findByName(client, "production", AtlanConnectorType.MSSQL)?.get(0)!!
        prepFile(snowflakeConnection.qualifiedName, mssqlConnection.qualifiedName)
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
                        assetsDeltaSemantic = "full",
                    ),
                    Importer::main,
                )
            }
        assertEquals(
            """
            Assets in multiple connections detected in the input file.
            Full delta processing currently only works for a single connection per input file, exiting.
            """.trimIndent(),
            exception.message,
        )
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
    }
}
