/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.adoption.AdoptionExporter
import com.atlan.pkg.serde.csv.CSVXformer
import de.siegmar.fastcsv.reader.CsvReader
import de.siegmar.fastcsv.reader.CsvRecord
import org.testng.Assert.assertFalse
import org.testng.Assert.assertTrue
import org.testng.annotations.Test
import java.io.File
import java.math.BigDecimal
import kotlin.test.assertEquals

/**
 * Test export of asset changes information.
 */
class ExportChangesCSVTest : PackageTest("cc") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val files =
        listOf(
            "debug.log",
            "changes.csv",
        )

    override fun setup() {
        runCustomPackage(
            AdoptionExportCfg(
                includeChanges = "YES",
                changesMax = 100,
                fileFormat = "CSV",
            ),
            AdoptionExporter::main,
        )
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
    }

    @Test
    fun excelIsEmpty() {
        val xlFile = "$testDirectory${File.separator}adoption-export.xlsx"
        assertTrue(File(xlFile).isFile)
        assertEquals(0, File(xlFile).length())
    }

    @Test
    fun testChanges() {
        val file = "$testDirectory${File.separator}changes.csv"
        val header = CSVXformer.getHeader(file)
        assertFalse(header.isEmpty())
        var lastCount = Int.MAX_VALUE
        CsvReader
            .builder()
            .fieldSeparator(',')
            .quoteCharacter('"')
            .skipEmptyLines(true)
            .allowMissingFields(false)
            .allowExtraFields(false)
            .ofCsvRecord(file)
            .stream()
            .skip(1)
            .forEach { r: CsvRecord ->
                val row = CSVXformer.getRowByHeader(header, r.fields)
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

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
