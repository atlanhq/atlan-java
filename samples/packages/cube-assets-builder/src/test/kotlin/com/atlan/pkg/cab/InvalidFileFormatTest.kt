/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cab

import CubeAssetsBuilderCfg
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
    private val fieldsFile = "invalid-format-fields.csv"

    private val files =
        listOf(
            assetsFile,
            fieldsFile,
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
                    CubeAssetsBuilderCfg(
                        assetsFile = Paths.get(testDirectory, assetsFile).toString(),
                        assetsUpsertSemantic = "upsert",
                        assetsFailOnErrors = true,
                    ),
                    Importer::main,
                )
            }
        assertEquals(
            """
            Invalid input file received. Input CSV is missing required columns: [typeName, connectionName, connectorName, cubeName, cubeDimensionName, cubeHierarchyName, fieldName]
            """.trimIndent(),
            exception.message,
        )
    }

    @Test
    fun fieldsFileFailsWithMeaningfulError() {
        val exception =
            assertFailsWith<IllegalArgumentException> {
                runCustomPackage(
                    CubeAssetsBuilderCfg(
                        assetsFile = Paths.get(testDirectory, fieldsFile).toString(),
                        assetsUpsertSemantic = "upsert",
                        assetsFailOnErrors = true,
                    ),
                    Importer::main,
                )
            }
        assertEquals(
            """
            Invalid input file received. Input CSV is missing required columns: [fieldName]
            """.trimIndent(),
            exception.message,
        )
    }

    @Test(dependsOnMethods = ["assetsFileFailsWithMeaningfulError"])
    fun filesCreated() {
        validateFilesExist(files)
    }
}
