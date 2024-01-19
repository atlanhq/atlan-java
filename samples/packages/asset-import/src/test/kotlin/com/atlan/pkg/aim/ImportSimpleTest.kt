/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.pkg.PackageTest
import org.testng.ITestContext
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import java.nio.file.Paths
import kotlin.test.Test

/**
 * Test import of a very simple file with only a single row.
 */
class ImportSimpleTest : PackageTest() {

    private val testFile = "input.csv"

    private val files = listOf(
        testFile,
        "debug.log",
    )

    private fun prepFile() {
        // Prepare a copy of the file with unique names for glossaries and tags
        val input = Paths.get("src", "test", "resources", "test_dsm.csv").toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.copyTo(output, overwrite = true)
    }

    @BeforeClass
    fun beforeClass() {
        prepFile()
        setup(
            AssetImportCfg(
                assetsFile = Paths.get(testDirectory, testFile).toString(),
                assetsUpsertSemantic = "update",
                assetsAttrToOverwrite = listOf(),
                assetsFailOnErrors = false,
                glossariesFile = null,
                glossariesUpsertSemantic = null,
                glossariesAttrToOverwrite = null,
                glossariesFailOnErrors = false,
            ),
        )
        Importer.main(arrayOf())
    }

    @Test
    fun warningsInLog() {
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
    }

    @AfterClass(alwaysRun = true)
    fun afterClass(context: ITestContext) {
        teardown(context.failedTests.size() > 0)
    }
}
