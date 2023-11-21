/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Asset
import com.atlan.pkg.Utils
import de.siegmar.fastcsv.writer.CsvWriter
import de.siegmar.fastcsv.writer.LineDelimiter
import de.siegmar.fastcsv.writer.QuoteStrategy
import mu.KLogger
import java.io.BufferedWriter
import java.io.Closeable
import java.io.IOException
import java.io.Writer
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import java.util.stream.Stream

/**
 * CSV file writer, using a specific field separator character.
 *
 * @param path location and filename of the CSV file to produce
 * @param fieldSeparator character to use to separate fields (for example ',' or ';')
 */
class CSVWriter @JvmOverloads constructor(path: String, fieldSeparator: Char = ',') : Closeable {

    private val writer: CsvWriter

    init {
        writer = CsvWriter.builder()
            .fieldSeparator(fieldSeparator)
            .quoteCharacter('"')
            .quoteStrategy(QuoteStrategy.REQUIRED)
            .lineDelimiter(LineDelimiter.PLATFORM)
            .build(ThreadSafeWriter(path))
    }

    /**
     * Write a header row into the CSV file.
     *
     * @param values to use for the header
     */
    fun writeHeader(values: Iterable<String?>?) {
        writer.writeRow(values)
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
    fun streamAssets(stream: Stream<Asset>, assetToRow: RowGenerator, totalAssetCount: Long, pageSize: Int, logger: KLogger) {
        logger.info("Extracting a total of {} assets...", totalAssetCount)
        val count = AtomicLong(0)
        val map = ConcurrentHashMap<String, String>()
        stream.forEach { a: Asset ->
            writeAsset(a, assetToRow, count, totalAssetCount, pageSize, map, logger)
        }
        logger.info("Total unique assets extracted: {}", map.size)
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
    fun appendAssets(list: List<Asset>, assetToRow: RowGenerator, totalAssetCount: Long, pageSize: Int, logger: KLogger) {
        val count = AtomicLong(0)
        val map = ConcurrentHashMap<String, String>()
        list.forEach { a: Asset ->
            writeAsset(a, assetToRow, count, totalAssetCount, pageSize, map, logger)
        }
    }

    private fun writeAsset(a: Asset, assetToRow: RowGenerator, count: AtomicLong, totalAssetCount: Long, pageSize: Int, map: ConcurrentHashMap<String, String>, logger: KLogger) {
        val duplicate = map.put(a.guid, a.typeName + "::" + a.guid)
        if (duplicate != null) {
            logger.warn("Hit a duplicate asset entry — there could be page skew: {}", duplicate)
        }
        val values = assetToRow.buildFromAsset(a)
        synchronized(writer) { writer.writeRow(values) }
        Utils.logProgress(count, totalAssetCount, logger, pageSize)
    }

    /** {@inheritDoc}  */
    @Throws(IOException::class)
    override fun close() {
        writer.close()
    }

    /**
     * To allow us to safely parallel-write files across threads.
     */
    private class ThreadSafeWriter(filePath: String) : Writer(), Closeable {
        private val writer: BufferedWriter

        init {
            writer = Files.newBufferedWriter(Paths.get(filePath), StandardCharsets.UTF_8)
        }

        /** {@inheritDoc}  */
        @Synchronized
        @Throws(IOException::class)
        override fun write(cbuf: CharArray, off: Int, len: Int) {
            writer.write(cbuf, off, len)
        }

        /** {@inheritDoc}  */
        @Synchronized
        @Throws(IOException::class)
        override fun flush() {
            writer.flush()
        }

        /** {@inheritDoc}  */
        @Synchronized
        @Throws(IOException::class)
        override fun close() {
            writer.close()
        }
    }
}
