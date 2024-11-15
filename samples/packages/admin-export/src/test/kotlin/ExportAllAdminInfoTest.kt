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
 * Test export of all administrative information.
 */
class ExportAllAdminInfoTest : PackageTest("aa") {
    override val logger = KotlinLogging.logger {}

    private val files =
        listOf(
            "debug.log",
            "admin-export.xlsx",
        )

    override fun setup() {
        runCustomPackage(
            AdminExportCfg(
                objectsToInclude =
                    listOf(
                        "users",
                        "groups",
                        "personas",
                        "purposes",
                        "policies",
                    ),
                includeNativePolicies = false,
                emailAddresses = null,
            ),
            AdminExporter::main,
        )
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
            assertTrue(xlsx.hasSheet("Groups"))
            assertTrue(xlsx.hasSheet("Personas"))
            assertTrue(xlsx.hasSheet("Purposes"))
            assertTrue(xlsx.hasSheet("Policies"))
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
    fun testGroups() {
        val xlFile = "$testDirectory${File.separator}admin-export.xlsx"
        ExcelReader(xlFile).use { xlsx ->
            val rows = xlsx.getRowsFromSheet("Groups")
            assertTrue(rows.isNotEmpty())
            rows.forEach { row ->
                assertFalse(row["Group name"].isNullOrBlank())
            }
        }
    }

    @Test
    fun testPersonas() {
        val xlFile = "$testDirectory${File.separator}admin-export.xlsx"
        ExcelReader(xlFile).use { xlsx ->
            val rows = xlsx.getRowsFromSheet("Personas")
            assertTrue(rows.isNotEmpty())
            rows.forEach { row ->
                assertFalse(row["Persona name"].isNullOrBlank())
            }
        }
    }

    @Test
    fun testPurposes() {
        val xlFile = "$testDirectory${File.separator}admin-export.xlsx"
        ExcelReader(xlFile).use { xlsx ->
            val rows = xlsx.getRowsFromSheet("Purposes")
            assertTrue(rows.isNotEmpty())
            rows.forEach { row ->
                assertFalse(row["Purpose name"].isNullOrBlank())
                assertFalse(row["Tags"].isNullOrBlank())
            }
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
                assertFalse(row["Resources"].isNullOrBlank())
            }
        }
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
