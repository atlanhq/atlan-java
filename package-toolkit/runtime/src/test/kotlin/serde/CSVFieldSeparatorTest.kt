/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package serde

import de.siegmar.fastcsv.reader.CsvParseException
import de.siegmar.fastcsv.reader.CsvReader
import de.siegmar.fastcsv.reader.CsvRecord
import de.siegmar.fastcsv.reader.FieldMismatchStrategy
import de.siegmar.fastcsv.writer.CsvWriter
import de.siegmar.fastcsv.writer.LineDelimiter
import de.siegmar.fastcsv.writer.QuoteStrategies
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Proves that reading a semicolon-delimited CSV with a hardcoded comma
 * field separator (as CSVReader.streamRows does in its parallel chunk path)
 * crashes with CsvParseException.
 *
 * Bug: CSVReader.streamRows() at lines 190, 202, 219 hardcodes ','
 * when splitting and re-reading chunk files, because fieldSeparator
 * is a bare constructor param (not a val) and is inaccessible in methods.
 */
class CSVFieldSeparatorTest {
    companion object {
        private val HEADER = listOf("qualifiedName", "name", "typeName", "description")
        private val ROW1 = listOf("default/schema/table/col1", "col1", "Column", "First column")
        private val ROW2 = listOf("default/schema/table/col2", "col2", "Column", "Second column")
    }

    private fun writeCSV(separator: Char): java.nio.file.Path {
        val path = Files.createTempFile("csv_sep_test_", ".csv")
        val writer =
            CsvWriter
                .builder()
                .fieldSeparator(separator)
                .quoteCharacter('"')
                .quoteStrategy(QuoteStrategies.NON_EMPTY)
                .lineDelimiter(LineDelimiter.LF)
                .build(path)
        writer.writeRecord(HEADER)
        writer.writeRecord(ROW1)
        writer.writeRecord(ROW2)
        writer.close()
        return path
    }

    private fun readCSV(
        path: java.nio.file.Path,
        separator: Char,
    ): List<List<String>> {
        val reader =
            CsvReader
                .builder()
                .fieldSeparator(separator)
                .quoteCharacter('"')
                .skipEmptyLines(true)
                .extraFieldStrategy(FieldMismatchStrategy.STRICT)
                .missingFieldStrategy(FieldMismatchStrategy.STRICT)
                .ofCsvRecord(path)
        val rows = mutableListOf<List<String>>()
        reader.stream().skip(1).forEach { r: CsvRecord ->
            rows.add(r.fields.toList())
        }
        reader.close()
        return rows
    }

    @Test
    fun semicolonCSVReadWithCorrectSeparator() {
        val path = writeCSV(';')
        val rows = readCSV(path, ';')
        assertEquals(2, rows.size, "Should read 2 data rows")
        assertEquals(4, rows[0].size, "Each row should have 4 fields")
        assertEquals("default/schema/table/col1", rows[0][0])
        assertEquals("col1", rows[0][1])
        assertEquals("Column", rows[0][2])
        assertEquals("First column", rows[0][3])
        Files.deleteIfExists(path)
    }

    @Test
    fun semicolonCSVReadWithHardcodedCommaCrashes() {
        // Simulates what CSVReader.streamRows() does at line 219:
        // reads semicolon-delimited chunk files with hardcoded fieldSeparator(',')
        val path = writeCSV(';')
        assertFailsWith<CsvParseException>(
            message = "Reading semicolon CSV with comma separator should throw CsvParseException",
        ) {
            readCSV(path, ',')
        }
        Files.deleteIfExists(path)
    }

    @Test
    fun commaCSVReadWithCommaSeparatorWorks() {
        val path = writeCSV(',')
        val rows = readCSV(path, ',')
        assertEquals(2, rows.size)
        assertEquals(4, rows[0].size)
        assertEquals("default/schema/table/col1", rows[0][0])
        Files.deleteIfExists(path)
    }
}
