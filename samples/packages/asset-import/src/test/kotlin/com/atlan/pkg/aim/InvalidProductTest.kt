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

class InvalidProductTest : PackageTest("idr") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val dataProduct1 = makeUnique("p1")
    private val testFile = "product_without_domain.csv"

    private val files =
        listOf(
            testFile,
            "debug.log",
        )

    private fun prepFile() {
        // Prepare a copy of the file with unique names for domains and products
        val input = Paths.get("src", "test", "resources", testFile).toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.useLines { lines ->
            lines.forEach { line ->
                val revised =
                    line
                        .replace("{{DATAPRODUCT1}}", dataProduct1)
                output.appendText("$revised\n")
            }
        }
    }

    override fun setup() {
        prepFile()
    }

    @Test
    fun failsWithMeaningfulError() {
        val exception =
            assertFailsWith<NoSuchElementException> {
                runCustomPackage(
                    AssetImportCfg(
                        dataProductsFile = Paths.get(testDirectory, testFile).toString(),
                        dataProductsUpsertSemantic = "upsert",
                        dataProductsFailOnErrors = true,
                    ),
                    Importer::main,
                )
            }
        assertEquals(
            "No dataDomain provided for the data product, cannot be processed.",
            exception.message,
        )
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
    }
}
