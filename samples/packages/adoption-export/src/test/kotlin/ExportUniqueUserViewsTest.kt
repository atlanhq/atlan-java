/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.adoption.AdoptionExporter
import com.atlan.pkg.serde.xls.ExcelReader
import org.testng.Assert.assertFalse
import org.testng.Assert.assertTrue
import org.testng.annotations.Test
import java.io.File

/**
 * Test export of asset views adoption information, sorted by number of unique users.
 */
class ExportUniqueUserViewsTest : PackageTest("uuv") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val files =
        listOf(
            "debug.log",
            "adoption-export.xlsx",
        )

    override fun setup() {
        runCustomPackage(
            AdoptionExportCfg(
                includeViews = "BY_USERS",
                viewsMax = 100,
            ),
            AdoptionExporter::main,
        )
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
    }

    @Test
    fun hasExpectedSheets() {
        val xlFile = "$testDirectory${File.separator}adoption-export.xlsx"
        ExcelReader(xlFile).use { xlsx ->
            assertTrue(xlsx.hasSheet("Views"))
        }
    }

    @Test
    fun testViews() {
        val xlFile = "$testDirectory${File.separator}adoption-export.xlsx"
        ExcelReader(xlFile).use { xlsx ->
            val rows = xlsx.getRowsFromSheet("Views")
            if (rows.isEmpty()) return@use // no view data available in environment
            rows.forEach { row ->
                assertFalse(row["Type"].isNullOrBlank())
                assertFalse(row["Qualified name"].isNullOrBlank())
                assertFalse(row["Name"].isNullOrBlank())
                assertFalse(row["Link"].isNullOrBlank())
                assertFalse(row["Distinct users"].isNullOrBlank())
                assertFalse(row["Total views"].isNullOrBlank())
            }
        }
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
