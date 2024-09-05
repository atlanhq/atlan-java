/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.pkg.PackageTest
import com.atlan.pkg.adoption.AdoptionExporter
import com.atlan.pkg.serde.xls.ExcelReader
import mu.KotlinLogging
import org.testng.Assert.assertFalse
import org.testng.Assert.assertTrue
import org.testng.annotations.Test
import java.io.File
import java.math.BigDecimal

/**
 * Test export of asset views adoption information, sorted by number of unique users.
 */
class ExportUniqueUserViewsTest : PackageTest() {
    override val logger = KotlinLogging.logger {}

    private val files =
        listOf(
            "debug.log",
            "adoption-export.xlsx",
        )

    override fun setup() {
        setup(
            AdoptionExportCfg(
                includeViews = "BY_USERS",
                viewsMax = 100,
            ),
        )
        AdoptionExporter.main(arrayOf(testDirectory))
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
            assertTrue(rows.isNotEmpty())
            var lastCount = Int.MAX_VALUE
            rows.forEach { row ->
                assertFalse(row["Type"].isNullOrBlank())
                assertFalse(row["Qualified name"].isNullOrBlank())
                assertFalse(row["Name"].isNullOrBlank())
                assertFalse(row["Link"].isNullOrBlank())
                val users = row["Distinct users"]
                assertFalse(users.isNullOrBlank())
                val userCount = BigDecimal(users)
                assertTrue(userCount.toInt() <= lastCount)
                lastCount = userCount.toInt()
                assertFalse(row["Total views"].isNullOrBlank())
            }
        }
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
