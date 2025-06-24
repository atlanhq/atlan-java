/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.ae.AdminExporter
import com.atlan.pkg.serde.csv.CSVXformer
import de.siegmar.fastcsv.reader.CsvReader
import de.siegmar.fastcsv.reader.CsvRecord
import org.testng.Assert.assertFalse
import org.testng.Assert.assertTrue
import org.testng.annotations.Test
import java.io.File
import kotlin.test.assertEquals

/**
 * Test export of all administrative information.
 */
class ExportAllAdminInfoCSVTest : PackageTest("aacsv") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val files =
        listOf(
            "debug.log",
            "users.csv",
            "groups.csv",
            "personas.csv",
            "purposes.csv",
            "policies.csv",
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
                fileFormat = "CSV",
            ),
            AdminExporter::main,
        )
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
    }

    @Test
    fun excelIsEmpty() {
        val xlFile = "$testDirectory${File.separator}admin-export.xlsx"
        assertTrue(File(xlFile).isFile)
        assertEquals(0, File(xlFile).length())
    }

    @Test
    fun testUsers() {
        val file = "$testDirectory${File.separator}users.csv"
        val header = CSVXformer.getHeader(file)
        assertFalse(header.isEmpty())
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
                assertFalse(row["Username"].isNullOrBlank())
                assertFalse(row["Email address"].isNullOrBlank())
                assertTrue(row["Email address"]!!.contains('@'))
            }
    }

    @Test
    fun testGroups() {
        val file = "$testDirectory${File.separator}groups.csv"
        val header = CSVXformer.getHeader(file)
        assertFalse(header.isEmpty())
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
                assertFalse(row["Group name"].isNullOrBlank())
            }
    }

    @Test
    fun testPersonas() {
        val file = "$testDirectory${File.separator}personas.csv"
        val header = CSVXformer.getHeader(file)
        assertFalse(header.isEmpty())
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
                assertFalse(row["Persona name"].isNullOrBlank())
            }
    }

    @Test
    fun testPurposes() {
        val file = "$testDirectory${File.separator}purposes.csv"
        val header = CSVXformer.getHeader(file)
        assertFalse(header.isEmpty())
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
                assertFalse(row["Purpose name"].isNullOrBlank())
                assertFalse(row["Tags"].isNullOrBlank())
            }
    }

    @Test
    fun testPolicies() {
        val file = "$testDirectory${File.separator}policies.csv"
        val header = CSVXformer.getHeader(file)
        assertFalse(header.isEmpty())
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
                assertFalse(row["Policy name"].isNullOrBlank())
                assertFalse(row["Resources"].isNullOrBlank())
            }
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
