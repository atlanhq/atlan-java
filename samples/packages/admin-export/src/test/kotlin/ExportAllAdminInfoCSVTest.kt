/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.ae.AdminExporter
import com.atlan.pkg.serde.csv.CSVXformer
import de.siegmar.fastcsv.reader.CsvReader
import de.siegmar.fastcsv.reader.CsvRecord
import de.siegmar.fastcsv.reader.FieldMismatchStrategy
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

    @Test(enabled = false)
    fun filesCreated() {
        validateFilesExist(files)
    }

    @Test(enabled = false)
    fun excelIsEmpty() {
        val xlFile = "$testDirectory${File.separator}admin-export.xlsx"
        assertTrue(File(xlFile).isFile)
        assertEquals(0, File(xlFile).length())
    }

    @Test(enabled = false)
    fun testUsers() {
        val file = "$testDirectory${File.separator}users.csv"
        val header = CSVXformer.getHeader(file)
        assertFalse(header.isEmpty())
        CsvReader
            .builder()
            .fieldSeparator(',')
            .quoteCharacter('"')
            .skipEmptyLines(true)
            .missingFieldStrategy(FieldMismatchStrategy.STRICT)
            .extraFieldStrategy(FieldMismatchStrategy.STRICT)
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

    @Test(enabled = false)
    fun testGroups() {
        val file = "$testDirectory${File.separator}groups.csv"
        val header = CSVXformer.getHeader(file)
        assertFalse(header.isEmpty())
        CsvReader
            .builder()
            .fieldSeparator(',')
            .quoteCharacter('"')
            .skipEmptyLines(true)
            .missingFieldStrategy(FieldMismatchStrategy.STRICT)
            .extraFieldStrategy(FieldMismatchStrategy.STRICT)
            .ofCsvRecord(file)
            .stream()
            .skip(1)
            .forEach { r: CsvRecord ->
                val row = CSVXformer.getRowByHeader(header, r.fields)
                assertFalse(row["Group name"].isNullOrBlank())
            }
    }

    @Test(enabled = false)
    fun testPersonas() {
        val file = "$testDirectory${File.separator}personas.csv"
        val header = CSVXformer.getHeader(file)
        assertFalse(header.isEmpty())
        CsvReader
            .builder()
            .fieldSeparator(',')
            .quoteCharacter('"')
            .skipEmptyLines(true)
            .missingFieldStrategy(FieldMismatchStrategy.STRICT)
            .extraFieldStrategy(FieldMismatchStrategy.STRICT)
            .ofCsvRecord(file)
            .stream()
            .skip(1)
            .forEach { r: CsvRecord ->
                val row = CSVXformer.getRowByHeader(header, r.fields)
                assertFalse(row["Persona name"].isNullOrBlank())
            }
    }

    @Test(enabled = false)
    fun testPurposes() {
        val file = "$testDirectory${File.separator}purposes.csv"
        val header = CSVXformer.getHeader(file)
        assertFalse(header.isEmpty())
        CsvReader
            .builder()
            .fieldSeparator(',')
            .quoteCharacter('"')
            .skipEmptyLines(true)
            .missingFieldStrategy(FieldMismatchStrategy.STRICT)
            .extraFieldStrategy(FieldMismatchStrategy.STRICT)
            .ofCsvRecord(file)
            .stream()
            .skip(1)
            .forEach { r: CsvRecord ->
                val row = CSVXformer.getRowByHeader(header, r.fields)
                assertFalse(row["Purpose name"].isNullOrBlank())
                assertFalse(row["Tags"].isNullOrBlank())
            }
    }

    @Test(enabled = false)
    fun testPolicies() {
        val file = "$testDirectory${File.separator}policies.csv"
        val header = CSVXformer.getHeader(file)
        assertFalse(header.isEmpty())
        CsvReader
            .builder()
            .fieldSeparator(',')
            .quoteCharacter('"')
            .skipEmptyLines(true)
            .missingFieldStrategy(FieldMismatchStrategy.STRICT)
            .extraFieldStrategy(FieldMismatchStrategy.STRICT)
            .ofCsvRecord(file)
            .stream()
            .skip(1)
            .forEach { r: CsvRecord ->
                val row = CSVXformer.getRowByHeader(header, r.fields)
                assertFalse(row["Policy name"].isNullOrBlank())
                assertFalse(row["Resources"].isNullOrBlank())
            }
    }

    @Test(enabled = false)
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
