/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.pkg.PackageTest
import com.atlan.pkg.ae.AdminExporter
import com.atlan.pkg.serde.xls.ExcelReader
import mu.KotlinLogging
import org.testng.Assert.assertFalse
import org.testng.Assert.assertTrue
import org.testng.annotations.Test
import java.io.File

/**
 * Test export of only users.
 */
class ExportUsersTest : PackageTest() {
    override val logger = KotlinLogging.logger {}

    private val files =
        listOf(
            "debug.log",
            "admin-export.xlsx",
        )

    override fun setup() {
        setup(
            AdminExportCfg(
                objectsToInclude = listOf("users"),
                includeNativePolicies = false,
                emailAddresses = null,
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
            assertFalse(xlsx.hasSheet("Personas"))
            assertFalse(xlsx.hasSheet("Purposes"))
            assertFalse(xlsx.hasSheet("Policies"))
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
                // assertFalse(row["First name"].isNullOrBlank())
                // assertFalse(row["Last name"].isNullOrBlank())
                assertFalse(row["Email address"].isNullOrBlank())
                assertTrue(row["Email address"]!!.contains('@'))
            }
        }
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
