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
import java.math.BigDecimal
import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * Test export of detailed view and change information.
 */
class ExportDetailsTest : PackageTest("d") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val files =
        listOf(
            "debug.log",
            "adoption-export.xlsx",
        )

    override fun setup() {
        runCustomPackage(
            AdoptionExportCfg(
                includeViews = "BY_VIEWS",
                viewsMax = 100,
                viewsDetails = "YES",
                includeChanges = "YES",
                changesMax = 100,
                changesDetails = "YES",
                changesAutomations = "SDK",
                changesFrom = Instant.now().minus(1, ChronoUnit.DAYS).epochSecond,
                changesTo = Instant.now().epochSecond,
                includeSearches = "YES",
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
            assertTrue(xlsx.hasSheet("Views")) // there by default
            assertTrue(xlsx.hasSheet("Changes"))
            assertTrue(xlsx.hasSheet("User views"))
            assertTrue(xlsx.hasSheet("User changes"))
            assertTrue(xlsx.hasSheet("User searches"))
        }
    }

    @Test
    fun testChanges() {
        val xlFile = "$testDirectory${File.separator}adoption-export.xlsx"
        ExcelReader(xlFile).use { xlsx ->
            val rows = xlsx.getRowsFromSheet("Changes")
            assertTrue(rows.isNotEmpty())
            var lastCount = Int.MAX_VALUE
            rows.forEach { row ->
                assertFalse(row["Type"].isNullOrBlank())
                assertFalse(row["Qualified name"].isNullOrBlank())
                assertFalse(row["Name"].isNullOrBlank())
                assertFalse(row["Link"].isNullOrBlank())
                val changes = row["Total changes"]
                assertFalse(changes.isNullOrBlank())
                val changeCount = BigDecimal(changes)
                assertTrue(changeCount.toInt() <= lastCount)
                lastCount = changeCount.toInt()
            }
        }
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
