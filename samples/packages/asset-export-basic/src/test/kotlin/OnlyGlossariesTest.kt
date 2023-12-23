/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.pkg.PackageTest
import org.testng.ITestContext
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import kotlin.test.Test

/**
 * Test export of only glossaries, no assets.
 */
class OnlyGlossariesTest : PackageTest() {

    private val files = listOf(
        "glossary-export.csv",
        "debug.log",
        "asset-export.csv",
    )

    @BeforeClass
    fun beforeClass() {
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

    @AfterClass(alwaysRun = true)
    fun afterClass(context: ITestContext) {
        teardown(context.failedTests.size() > 0)
    }
}
