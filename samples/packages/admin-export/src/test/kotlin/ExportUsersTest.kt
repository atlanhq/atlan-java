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
import kotlin.test.assertNotNull

/**
 * Test export of only users.
 */
class ExportUsersTest : PackageTest() {
    private val files = listOf(
        "debug.log",
        "admin-export.xlsx",
    )

    @BeforeClass
    fun beforeClass() {
        setup(
            AdminExportCfg(
                objectsToInclude = listOf("users"),
                includeNativePolicies = false,
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
            assertTrue(xlsx.hasSheet("Users"))
            assertFalse(xlsx.hasSheet("Groups"))
        }
    }

    @Test
    fun testUsers() {
        val xlFile = "$testDirectory${File.separator}admin-export.xlsx"
        ExcelReader(xlFile).use { xlsx ->
            val rows = xlsx.getRowsFromSheet("Users")
            assertTrue(rows.isNotEmpty())
            rows.forEach { row ->
                assertFalse(row["Username"].isNullOrBlank())
                assertFalse(row["First name"].isNullOrBlank())
                assertFalse(row["Last name"].isNullOrBlank())
                assertFalse(row["Email address"].isNullOrBlank())
                assertTrue(row["Email address"]!!.contains('@'))
            }
            val first = rows[0]
            assertNotNull(first["Username"])
            assertNotNull(first["First name"])
            assertNotNull(first["Last name"])
            assertNotNull(first["Email address"])
            assertTrue(first["Email address"]!!.contains('@'))
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
