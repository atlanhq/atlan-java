/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.pkg.PackageTest
import com.atlan.pkg.ae.AdminExporter
import com.atlan.pkg.serde.xls.ExcelReader
import org.testng.Assert.assertFalse
import org.testng.Assert.assertTrue
import org.testng.ITestContext
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import java.io.File

/**
 * Test export of all policies, including internal.
 */
class ExportAllPoliciesTest : PackageTest() {
    private val files = listOf(
        "debug.log",
        "admin-export.xlsx",
    )

    @BeforeClass
    fun beforeClass() {
        setup(
            AdminExportCfg(
                objectsToInclude = listOf(
                    "policies",
                ),
                includeNativePolicies = true,
            ),
        )
        AdminExporter.main(arrayOf(testDirectory))
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
    }

    @Test
    fun hasExpectedSheets() {
        val xlFile = "$testDirectory${File.separator}admin-export.xlsx"
        ExcelReader(xlFile).use { xlsx ->
            assertFalse(xlsx.hasSheet("Users"))
            assertFalse(xlsx.hasSheet("Groups"))
            assertFalse(xlsx.hasSheet("Personas"))
            assertFalse(xlsx.hasSheet("Purposes"))
            assertTrue(xlsx.hasSheet("Policies"))
        }
    }

    @Test
    fun testPolicies() {
        val xlFile = "$testDirectory${File.separator}admin-export.xlsx"
        ExcelReader(xlFile).use { xlsx ->
            val rows = xlsx.getRowsFromSheet("Policies")
            assertTrue(rows.isNotEmpty())
            rows.forEach { row ->
                assertFalse(row["Policy name"].isNullOrBlank())
                assertFalse(row["Kind"].isNullOrBlank())
                assertFalse(row["Type"].isNullOrBlank())
            }
        }
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
