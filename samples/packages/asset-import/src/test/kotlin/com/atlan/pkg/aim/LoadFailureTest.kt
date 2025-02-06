/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.aim

import AssetImportCfg
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test a failing import.
 */
class LoadFailureTest : PackageTest("lf") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val testFile = "assets_invalid.csv"

    private val files =
        listOf(
            testFile,
            "debug.log",
        )

    private fun prepFile() {
        // Prepare a copy of the file with unique names for glossaries and tags
        val input = Paths.get("src", "test", "resources", testFile).toFile()
        val output = Paths.get(testDirectory, testFile).toFile()
        input.copyTo(output, true)
    }

    override fun setup() {
        prepFile()
        sysExit.execute {
            runCustomPackage(
                AssetImportCfg(
                    assetsFile = Paths.get(testDirectory, testFile).toString(),
                    assetsUpsertSemantic = "upsert",
                    assetsFailOnErrors = true,
                ),
                Importer::main,
            )
        }
        // Confirm the workflow fails, with expected non-zero exit code
        assertEquals(3, sysExit.exitCode)
    }

    @Test
    fun failureInResults() {
        fileHasLineStartingWith(
            "results.csv",
            """
            "failed","Database","default/non-existent/1234567890/DB","DB","primary","java.lang.Exception: ATLAN-JAVA-403-000 Server responded with a permission error -- ATLAS-403-00-001
            """.trimIndent(),
        )
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
    }
}
