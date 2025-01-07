/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.csv

import com.atlan.model.assets.Asset
import com.atlan.pkg.Utils
import com.atlan.pkg.serde.TabularWriter
import de.siegmar.fastcsv.writer.CsvWriter
import de.siegmar.fastcsv.writer.LineDelimiter
import de.siegmar.fastcsv.writer.QuoteStrategies
import mu.KLogger
import java.io.Closeable
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import java.util.stream.Stream

/**
 * CSV file writer, using a specific field separator character.
 *
 * @param path location and filename of the CSV file to produce
 * @param fieldSeparator character to use to separate fields (for example ',' or ';')
 */
class CSVWriter
    @JvmOverloads
    constructor(
        path: String,
        fieldSeparator: Char = ',',
    ) : Closeable,
        TabularWriter {
        private val writer =
            CsvWriter
                .builder()
                .fieldSeparator(fieldSeparator)
                .quoteCharacter('"')
                .quoteStrategy(QuoteStrategies.NON_EMPTY)
                .lineDelimiter(LineDelimiter.PLATFORM)
                .build(ThreadSafeWriter(path))

        private val header = mutableListOf<String>()

        /**
         * Write a header row into the CSV file.
         * Note: since this is a CSV output, the description will be dropped (no standard way to add
         * a comment to a CSV file).
         *
         * @param headers ordered map of header names and descriptions
         */
        override fun writeHeader(headers: Map<String, String>) {
            header.addAll(headers.keys)
            writeRecord(headers.keys)
        }

        /**
         * Write a header row into the CSV file.
         *
         * @param values to use for the header
         */
        override fun writeHeader(values: Iterable<String>) {
            header.addAll(values)
            writeRecord(values)
        }

        /**
         * Write a row of data into the CSV file, where key of the map is the column name and the value
         * is the value to write for that column of the row of data.
         * Note: be sure you have first called {@code writeHeader} to output the header row.
         *
         * @param values map keyed by column name with values for the row of data
         */
        override fun writeRecord(values: Map<String, Any?>?) {
            if (values != null) {
                val list = mutableListOf<Any>()
                header.forEach { name ->
                    list.add(values.getOrDefault(name, "") ?: "")
                }
                writeRecord(list)
            }
        }

        /**
         * Write a row of data into the CSV file, where the values are already sequenced
         * in the same order as the header columns.
         *
         * @param data to use for the row of data
         */
        override fun writeRecord(data: Iterable<Any?>?) {
            if (data != null) {
                synchronized(writer) { writer.writeRecord(data.map { it?.toString() ?: "" }) }
            }
        }

        /**
         * Parallel-write the provided asset stream into the CSV file.
         * (For the highest performance, we recommend sending in a parallel stream of assets.)
         *
         * @param stream of assets, typically from a FluentSearch (parallel stream recommended)
         * @param assetToRow translator from an asset object to a row of CSV values
         * @param totalAssetCount the total number of assets that will be output (used for logging / completion tracking)
         * @param pageSize the page size being used by the asset stream
         * @param logger through which to report the overall progress
         */
        fun streamAssets(
            stream: Stream<Asset>,
            assetToRow: RowGenerator,
            totalAssetCount: Long,
            pageSize: Int,
            logger: KLogger,
        ) {
            logger.info { "Extracting a total of $totalAssetCount assets..." }
            val count = AtomicLong(0)
            val map = ConcurrentHashMap<String, String>()
            stream.forEach { a: Asset ->
                writeAsset(a, assetToRow, count, totalAssetCount, pageSize, map, logger)
            }
            logger.info { "Total unique assets extracted: ${map.size}" }
        }

        /**
         * Append assets that have already been retrieved (not being streamed) into the CSV file.
         * This is useful, for example, where information is cached up-front and thus need not be re-retrieved.
         *
         * @param list of assets, pre-retrieved
         * @param assetToRow translator from an asset object into a row of CSV values
         * @param totalAssetCount the total number of assets that will be output (used for logging / completion tracking)
         * @param pageSize the page size to use for periodically logging progress
         * @param logger through which to report the overall progress
         */
        fun appendAssets(
            list: List<Asset>,
            assetToRow: RowGenerator,
            totalAssetCount: Long,
            pageSize: Int,
            logger: KLogger,
        ) {
            val count = AtomicLong(0)
            val map = ConcurrentHashMap<String, String>()
            list.forEach { a: Asset ->
                writeAsset(a, assetToRow, count, totalAssetCount, pageSize, map, logger)
            }
        }

        private fun writeAsset(
            a: Asset,
            assetToRow: RowGenerator,
            count: AtomicLong,
            totalAssetCount: Long,
            pageSize: Int,
            map: ConcurrentHashMap<String, String>,
            logger: KLogger,
        ) {
            val duplicate = map.put(a.guid, a.typeName + "::" + a.guid)
            if (duplicate != null) {
                logger.warn { "Hit a duplicate asset entry — there could be page skew: $duplicate" }
            }
            val values = assetToRow.buildFromAsset(a)
            writeRecord(values)
            Utils.logProgress(count, totalAssetCount, logger, pageSize)
        }

        /** {@inheritDoc}  */
        @Throws(IOException::class)
        override fun close() {
            writer.close()
        }
    }
