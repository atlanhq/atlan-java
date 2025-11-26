/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
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

    private val table = makeUnique("t1")

    private val assetsFile = "invalid-format-assets.csv"
    private val glossariesFile = "invalid-format-glossaries.csv"
    private val productsFile = "invalid-format-products.csv"
    private val tagsFile = "invalid-format-tags.csv"

    private val files =
        listOf(
            assetsFile,
            glossariesFile,
            productsFile,
            tagsFile,
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
                    AssetImportCfg(
                        assetsFile = Paths.get(testDirectory, assetsFile).toString(),
                        assetsUpsertSemantic = "upsert",
                        assetsFailOnErrors = true,
                    ),
                    Importer::main,
                )
            }
        assertEquals(
            """
            Invalid input file received. Input CSV is missing required columns: [typeName, qualifiedName]
            """.trimIndent(),
            exception.message,
        )
    }

    @Test
    fun glossariesFileFailsWithMeaningfulError() {
        val exception =
            assertFailsWith<IllegalArgumentException> {
                runCustomPackage(
                    AssetImportCfg(
                        glossariesFile = Paths.get(testDirectory, glossariesFile).toString(),
                        glossariesUpsertSemantic = "upsert",
                        glossariesFailOnErrors = true,
                    ),
                    Importer::main,
                )
            }
        assertEquals(
            """
            Invalid input file received. Input CSV is missing required columns: [typeName]
            """.trimIndent(),
            exception.message,
        )
    }

    @Test
    fun productsFileFailsWithMeaningfulError() {
        val exception =
            assertFailsWith<IllegalArgumentException> {
                runCustomPackage(
                    AssetImportCfg(
                        dataProductsFile = Paths.get(testDirectory, productsFile).toString(),
                        dataProductsUpsertSemantic = "upsert",
                        dataProductsFailOnErrors = true,
                    ),
                    Importer::main,
                )
            }
        assertEquals(
            """
            Invalid input file received. Input CSV is missing required columns: [typeName]
            """.trimIndent(),
            exception.message,
        )
    }

    @Test
    fun tagsFileFailsWithMeaningfulError() {
        val exception =
            assertFailsWith<IllegalArgumentException> {
                runCustomPackage(
                    AssetImportCfg(
                        tagsFile = Paths.get(testDirectory, tagsFile).toString(),
                        tagsFailOnErrors = true,
                    ),
                    Importer::main,
                )
            }
        assertEquals(
            """
            Invalid input file received. Input CSV is missing required columns: [Atlan tag name]
            """.trimIndent(),
            exception.message,
        )
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
    }
}
