/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Connection
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageTest
import org.testng.ITestContext
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import kotlin.test.Test

/**
 * Test export of only assets, no glossaries.
 */
class NoGlossariesTest : PackageTest() {

    private val files = listOf(
        "asset-export.csv",
        "debug.log",
        "glossary-export.csv",
    )

    @BeforeClass
    fun beforeClass() {
        setup(
            AssetExportBasicCfg(
                exportScope = "ALL",
                qnPrefix = Connection.findByName("development", AtlanConnectorType.SNOWFLAKE)?.get(0)?.qualifiedName,
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
