/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.csv

import de.siegmar.fastcsv.reader.CsvReader
import de.siegmar.fastcsv.reader.CsvRecord
import de.siegmar.fastcsv.writer.CsvWriter
import de.siegmar.fastcsv.writer.LineDelimiter
import de.siegmar.fastcsv.writer.QuoteStrategies
import mu.KLogger
import java.io.Closeable
import java.io.IOException
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.concurrent.atomic.AtomicLong

/**
 * Transform between CSV file formats.
 *
 * Using the inputFile as a source, apply any transformation logic or mappings to produce
 * content in the outputFile.
 *
 * @param inputFile file from which to read the CSV records that need to be transformed
 * @param targetHeader column names (in order) to use in the output file
 * @param logger through which to record progress and any errors
 * @param fieldSeparator (optional) separator to use, in case the CSV files are not actually comma-separated
 */
abstract class CSVXformer(
    private val inputFile: String,
    val targetHeader: Iterable<String?>?,
    private val logger: KLogger,
    private val fieldSeparator: Char = ',',
) : Closeable,
    RowTransformer {
    private val reader: CsvReader<CsvRecord>
    private val counter: CsvReader<CsvRecord>
    private val header: List<String>

    init {
        val input = Paths.get(inputFile)
        val builder =
            CsvReader
                .builder()
                .fieldSeparator(fieldSeparator)
                .quoteCharacter('"')
                .skipEmptyLines(true)
                .ignoreDifferentFieldCount(false)
        header = getHeader(inputFile, fieldSeparator)
        reader = builder.ofCsvRecord(input)
        counter = builder.ofCsvRecord(input)
    }

    companion object {
        /**
         * Extract the header from a provided file.
         *
         * @param file path to the CSV file for which to extract the header
         * @param fieldSeparator field separator used within the CSV file (defaults to ',' if not specified)
         * @return a list of the header names, in order
         */
        fun getHeader(
            file: String,
            fieldSeparator: Char = ',',
        ): List<String> {
            val input = Paths.get(file)
            val builder =
                CsvReader
                    .builder()
                    .fieldSeparator(fieldSeparator)
                    .quoteCharacter('"')
                    .skipEmptyLines(true)
                    .ignoreDifferentFieldCount(false)
            builder.ofCsvRecord(input).use { tmp ->
                val one = tmp.stream().findFirst()
                return one
                    .map { obj: CsvRecord ->
                        obj.fields.map { field ->
                            trimWhitespace(field)
                        }
                    }.orElse(emptyList())
            }
        }

        /**
         * Trim all whitespace from the provided value, including byte order marks (BOM) or other zero-width space (ZWSP) characters
         *
         * @param s the original string to trim
         * @return a "clean" string without any of these characters or whitespace around it
         */
        fun trimWhitespace(s: String): String = s.trim().trim('\uFEFF', '\u200B')

        /**
         * Translate a row of input values into a map, keyed by input header name
         * with the value being the value for that column on the row.
         *
         * @param header list of header column names
         * @param values list of values, in the same order as the header columns
         * @param trimValues whether to apply whitespace trimming to values, too
         * @return map from header name to value on that row
         */
        fun getRowByHeader(
            header: List<String>,
            values: List<String>,
            trimValues: Boolean = false,
        ): Map<String, String> {
            val map = mutableMapOf<String, String>()
            header.forEachIndexed { index, s ->
                // Explicitly trim all whitespace from headers, including byte order mark (BOM) or zero-width space (ZWSP) characters
                val trimmed = trimWhitespace(s)
                if (trimValues) {
                    map[trimmed] = trimWhitespace(values.getOrElse(index) { "" })
                } else {
                    map[trimmed] = values.getOrElse(index) { "" }
                }
            }
            return map.toMap()
        }
    }

    /**
     * Run the transformation and produce the output into the specified file.
     *
     * @param outputFile path to a file into which the transformed CSV output will be written.
     */
    fun transform(outputFile: String) {
        CsvWriter
            .builder()
            .fieldSeparator(fieldSeparator)
            .quoteCharacter('"')
            .quoteStrategy(QuoteStrategies.NON_EMPTY)
            .lineDelimiter(LineDelimiter.PLATFORM)
            .build(
                Paths.get(outputFile),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE,
            )
            // TODO: parallelize? .build(ThreadSafeWriter(outputFile))
            .use { writer ->
                val start = System.currentTimeMillis()
                logger.info { "Transforming $inputFile..." }
                map(writer)
                logger.info { "Total transformation time: ${System.currentTimeMillis() - start} ms" }
            }
    }

    /**
     * Run the transformation and produce the output into the specified file.
     * Note: when using this method, it is your responsibility to first output the header into the writer.
     * (No header will ever be included via this method.)
     *
     * @param writer CSV writer into which the transformed CSV output will be written.
     */
    fun transform(writer: CsvWriter) {
        val start = System.currentTimeMillis()
        logger.info { "Transforming $inputFile..." }
        mapWithoutHeader(writer)
        logger.info { "Total transformation time: ${System.currentTimeMillis() - start} ms" }
    }

    /**
     * Actually run the transformation.
     *
     * @param writer into which to write each transformed row of data
     */
    private fun map(writer: CsvWriter) {
        // Start by outputting the header row in the target CSV file
        writer.writeRecord(targetHeader)
        mapWithoutHeader(writer)
    }

    /**
     * Actually run the transformation, not including any header.
     *
     * @param writer into which to write each transformed row of data
     */
    private fun mapWithoutHeader(writer: CsvWriter) {
        // Calculate total number of rows that need to be transformed...
        val filteredRowCount = AtomicLong(0)
        counter.stream().skip(1).forEach { row ->
            // TODO: parallelize?
            val rowByHeader = getRowByHeader(row.fields)
            if (includeRow(rowByHeader)) {
                filteredRowCount.incrementAndGet()
            }
        }
        val totalRowCount = filteredRowCount.get()
        logger.info { "Transforming a total of $totalRowCount rows..." }
        // Actually do the mapping, of only those rows we need to transform...
        reader.stream().skip(1).forEach { row ->
            // TODO: parallelize?
            val inputRow = getRowByHeader(row.fields)
            if (includeRow(inputRow)) {
                mapRow(inputRow).forEach { outputRow ->
                    writer.writeRecord(outputRow)
                }
            }
        }
    }

    /**
     * Translate a row of input values into a map, keyed by input header name
     * with the value being the value for that column on the row.
     *
     * @param values a row of values, in the same order as the headers
     * @return map from header name to value on that row
     */
    private fun getRowByHeader(values: List<String>): Map<String, String> = getRowByHeader(header, values)

    /** {@inheritDoc}  */
    @Throws(IOException::class)
    override fun close() {
        reader.use {
            counter.close()
        }
    }
}
