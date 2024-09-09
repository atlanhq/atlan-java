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
 * Test export of all policies, including internal.
 */
class ExportDifferentFilenameTest : PackageTest() {
    override val logger = KotlinLogging.logger {}

    private val filename = "other-admin-export.xlsx"

    private val files =
        listOf(
            "debug.log",
            filename,
        )

    override fun setup() {
        setup(
            AdminExportCfg(
                objectsToInclude =
                    listOf(
                        "policies",
                    ),
                includeNativePolicies = true,
                emailAddresses = null,
                filename = filename,
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
        val xlFile = "$testDirectory${File.separator}$filename"
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
        val xlFile = "$testDirectory${File.separator}$filename"
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
}
