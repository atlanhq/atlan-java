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
 * Test migration of asset metadata.
 */
class EnrichmentMigratorTest : PackageTest() {

    private val files = listOf(
        "asset-export.csv",
        "debug.log",
    )

    @BeforeClass
    fun beforeClass() {
        setup(
            EnrichmentMigratorCfg(
                sourceConnection = Connection.findByName("development", AtlanConnectorType.MSSQL)?.get(0)?.qualifiedName,
                targetConnection = Connection.findByName("development", AtlanConnectorType.SNOWFLAKE)?.get(0)?.qualifiedName,
                failOnErrors = false,
            ),
        )
        EnrichmentMigrator.main(arrayOf(testDirectory))
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
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
