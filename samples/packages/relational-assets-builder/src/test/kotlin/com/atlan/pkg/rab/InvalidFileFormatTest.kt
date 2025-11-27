/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import RelationalAssetsBuilderCfg
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Test import of invalid file formats in the input.
 */
class InvalidFileFormatTest : PackageTest("iff") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val assetsFile = "invalid-format-assets.csv"
    private val columnsFile = "invalid-format-columns.csv"

    private val files =
        listOf(
            assetsFile,
            columnsFile,
            "debug.log",
        )

    private fun prepFiles() {
        // Prepare a copy of each of the invalid format files
        files.filter { it != "debug.log" }.forEach { name ->
            val input = Paths.get("src", "test", "resources", name).toFile()
            val output = Paths.get(testDirectory, name).toFile()
            input.copyTo(output, true)
        }
    }

    override fun setup() {
        prepFiles()
    }

    @Test
    fun assetsFileFailsWithMeaningfulError() {
        val exception =
            assertFailsWith<IllegalArgumentException> {
                runCustomPackage(
                    RelationalAssetsBuilderCfg(
                        assetsFile = Paths.get(testDirectory, assetsFile).toString(),
                        assetsUpsertSemantic = "upsert",
                        assetsFailOnErrors = true,
                    ),
                    Importer::main,
                )
            }
        assertEquals(
            """
            Invalid input file received. Input CSV is missing required columns: [typeName, connectionName, connectorName, databaseName, schemaName, entityName, columnName, dataType]
            """.trimIndent(),
            exception.message,
        )
    }

    @Test
    fun columnsFileFailsWithMeaningfulError() {
        val exception =
            assertFailsWith<IllegalArgumentException> {
                runCustomPackage(
                    RelationalAssetsBuilderCfg(
                        assetsFile = Paths.get(testDirectory, columnsFile).toString(),
                        assetsUpsertSemantic = "upsert",
                        assetsFailOnErrors = true,
                    ),
                    Importer::main,
                )
            }
        assertEquals(
            """
            Invalid input file received. Input CSV is missing required columns: [dataType]
            """.trimIndent(),
            exception.message,
        )
    }

    @Test(dependsOnMethods = ["assetsFileFailsWithMeaningfulError"])
    fun filesCreated() {
        validateFilesExist(files)
    }
}
