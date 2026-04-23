/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.ae.AdminExporter
import com.atlan.pkg.serde.xls.ExcelReader
import org.testng.Assert.assertFalse
import org.testng.Assert.assertTrue
import org.testng.annotations.Test
import java.io.File
import kotlin.test.assertEquals

/**
 * Test export of all administrative information.
 * Disabled: requires internal cluster access for user impersonation via CLIENT_ID/CLIENT_SECRET.
 */
@Test(enabled = false)
class ExportAllAdminInfoTest : PackageTest("aa") {
    override val logger = Utils.getLogger(this.javaClass.name)

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

    @Test(enabled = false)
    fun filesCreated() {
        validateFilesExist(files)
    }

    @Test(enabled = false)
    fun csvFilesExistButAreEmpty() {
        val csvFiles =
            listOf(
                "users.csv",
                "groups.csv",
                "personas.csv",
                "purposes.csv",
                "policies.csv",
            )
        csvFiles.forEach {
            val file = File("$testDirectory${File.separator}$it")
            assertTrue(file.isFile)
            assertEquals(0, file.length())
        }
    }

    @Test(enabled = false)
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

    @Test(enabled = false)
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

    @Test(enabled = false)
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

    @Test(enabled = false)
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

    @Test(enabled = false)
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

    @Test(enabled = false)
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

    @Test(enabled = false)
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
