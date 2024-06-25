/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.pkg.PackageTest
import mu.KotlinLogging
import kotlin.test.Test

/**
 * Test export of only glossaries, no assets.
 */
class OnlyGlossariesTest : PackageTest() {
    override val logger = KotlinLogging.logger {}

    private val files = listOf(
        "glossary-export.csv",
        "debug.log",
        "asset-export.csv",
    )

    override fun setup() {
        setup(
            AssetExportBasicCfg(
                exportScope = "GLOSSARIES_ONLY",
                qnPrefix = "",
                includeGlossaries = false,
            ),
        )
        Exporter.main(arrayOf(testDirectory))
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files.subList(0, files.size - 1))
        validateFileExistsButEmpty(files.subList(2, files.size))
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
