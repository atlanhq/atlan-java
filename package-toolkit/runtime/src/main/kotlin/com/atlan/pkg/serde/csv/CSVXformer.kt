/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.csv

import de.siegmar.fastcsv.reader.CsvReader
import de.siegmar.fastcsv.reader.CsvRow
import de.siegmar.fastcsv.writer.CsvWriter
import de.siegmar.fastcsv.writer.LineDelimiter
import de.siegmar.fastcsv.writer.QuoteStrategy
import mu.KLogger
import java.io.Closeable
import java.io.IOException
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
    private val targetHeader: Iterable<String?>?,
    private val logger: KLogger,
    private val fieldSeparator: Char = ',',
) : Closeable, RowTransformer {
    private val reader: CsvReader
    private val counter: CsvReader
    private val header: List<String>

    init {
        val builder = CsvReader.builder()
            .fieldSeparator(fieldSeparator)
            .quoteCharacter('"')
            .skipEmptyRows(true)
            .errorOnDifferentFieldCount(true)
        builder.build(inputFile).use { tmp ->
            val one = tmp.stream().findFirst()
            header =
                one.map { obj: CsvRow -> obj.fields }
                    .orElse(emptyList())
        }
        reader = builder.build(inputFile)
        counter = builder.build(inputFile)
    }

    /**
     * Run the transformation and produce the output into the specified file.
     *
     * @param outputFile path to a file into which the transformed CSV output will be written.
     */
    fun transform(outputFile: String) {
        CsvWriter.builder()
            .fieldSeparator(fieldSeparator)
            .quoteCharacter('"')
            .quoteStrategy(QuoteStrategy.REQUIRED)
            .lineDelimiter(LineDelimiter.PLATFORM)
            .build(ThreadSafeWriter(outputFile))
            .use { writer ->
                val start = System.currentTimeMillis()
                logger.info { "Transforming $inputFile..." }
                map(writer)
                logger.info { "Total transformation time: ${System.currentTimeMillis() - start} ms" }
            }
    }

    /**
     * Actually run the transformation.
     *
     * @param writer into which to write each transformed row of data
     */
    private fun map(writer: CsvWriter) {
        // Start by outputting the header row in the target CSV file
        writer.writeRow(targetHeader)
        // Calculate total number of rows that need to be transformed...
        val filteredRowCount = AtomicLong(0)
        counter.stream().skip(1).parallel().forEach { row ->
            val rowByHeader = getRowByHeader(row.fields)
            if (includeRow(rowByHeader)) {
                filteredRowCount.incrementAndGet()
            }
        }
        val totalRowCount = filteredRowCount.get()
        logger.info { "Transforming a total of $totalRowCount rows..." }
        // Actually do the mapping, of only those rows we need to transform...
        reader.stream().skip(1).parallel().forEach { row ->
            val rowByHeader = getRowByHeader(row.fields)
            if (includeRow(rowByHeader)) {
                val mappedValues = mapRow(rowByHeader)
                writer.writeRow(mappedValues)
            }
        }
    }

    /**
     * Translate a row of input values into a map, keyed by input header name
     * with the value being the value for that column on the row.
     *
     * @return map from header name to value on that row
     */
    private fun getRowByHeader(values: List<String>): Map<String, String> {
        val map = mutableMapOf<String, String>()
        header.forEachIndexed { index, s ->
            map[s] = values.getOrElse(index) { "" }
        }
        return map.toMap()
    }

    /** {@inheritDoc}  */
    @Throws(IOException::class)
    override fun close() {
        reader.close()
    }
}
