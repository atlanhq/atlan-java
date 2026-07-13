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
 * Test that an invalid field separator is rejected up-front with a non-zero exit code,
 * before any file parsing (which would otherwise blow up on the subsequent `[0]` access).
 *
 * The guard requires the separator to be exactly one character, so both an empty string
 * and a multi-character value must fail with exit code 2. (CSA-484)
 */
class EmptyFieldSeparatorTest : PackageTest("efs") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val testFile = "assets.csv"

    private fun prepFile() {
        // The guard fires before the file is read, so any content is sufficient -- it just
        // needs to exist so that a file is considered "provided".
        val output = Paths.get(testDirectory, testFile).toFile()
        output.writeText("qualifiedName,typeName,name\n")
    }

    /**
     * Run the importer with the provided field separator and return the exit code.
     * `assetsConfig = "advanced"` is required for the non-default separator to take effect
     * (otherwise `getEffectiveValue` falls back to the default comma).
     */
    private fun runWithSeparator(separator: String): Int? {
        sysExit.execute {
            runCustomPackage(
                AssetImportCfg(
                    assetsFile = Paths.get(testDirectory, testFile).toString(),
                    assetsUpsertSemantic = "upsert",
                    assetsConfig = "advanced",
                    assetsFieldSeparator = separator,
                ),
                Importer::main,
            )
        }
        return sysExit.exitCode
    }

    override fun setup() {
        prepFile()
    }

    @Test
    fun emptyFieldSeparatorExitsWithCode2() {
        assertEquals(2, runWithSeparator(""), "An empty field separator should fail with exit code 2")
    }

    @Test
    fun multiCharacterFieldSeparatorExitsWithCode2() {
        assertEquals(2, runWithSeparator(";;"), "A multi-character field separator should fail with exit code 2")
    }
}
