/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.csv

import com.atlan.Atlan
import com.atlan.cache.ReflectionCache
import com.atlan.exception.AtlanException
import com.atlan.model.assets.Asset
import com.atlan.model.enums.AtlanDeleteType
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.Utils
import com.atlan.pkg.serde.cell.AssetRefXformer
import com.atlan.util.AssetBatch
import com.atlan.util.ParallelBatch
import de.siegmar.fastcsv.reader.CsvReader
import de.siegmar.fastcsv.reader.CsvRow
import mu.KLogger
import java.io.Closeable
import java.io.IOException
import java.nio.file.Paths
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 * Utility class for reading from CSV files, using FastCSV.
 *
 * @param path location and filename of the CSV file to read
 * @param updateOnly when true, the reader will first look up assets to ensure they exist (and only update them, never create)
 * @param fieldSeparator character to use to separate fields (for example ',' or ';')
 */
class CSVReader @JvmOverloads constructor(
    path: String,
    private val updateOnly: Boolean,
    fieldSeparator: Char = ',',
) : Closeable {

    private val reader: CsvReader
    private val counter: CsvReader
    private val header: List<String>
    private val typeIdx: Int
    private val qualifiedNameIdx: Int

    val created: ConcurrentHashMap<String, Asset>

    init {
        val inputFile = Paths.get(path)
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
        typeIdx = header.indexOf(Asset.TYPE_NAME.atlanFieldName)
        qualifiedNameIdx = header.indexOf(Asset.QUALIFIED_NAME.atlanFieldName)
        if (typeIdx < 0) {
            throw IOException(
                "Unable to find the column 'typeName'. This is a mandatory column in the input CSV.",
            )
        }
        created = ConcurrentHashMap()
        reader = builder.build(inputFile)
        counter = builder.build(inputFile)
    }

    /**
     * Parallel-read the CSV file into batched asset updates against Atlan.
     * Note: this requires the input CSV file to be fully parallel-loadable without any
     * conflicts. That means: every row is a unique asset, no two rows update any relationship
     * attribute that points at the same related asset (such as an assigned term).
     *
     * @param rowToAsset translator from a row of CSV values to an asset object
     * @param batchSize maximum number of Assets to bulk-save in Atlan per API request
     * @param logger through which to report the overall progress
     * @param skipColumns columns to skip during the processing (i.e. where they need to be processed in a later pass)
     * @return true if all rows were processed successfully, or false if there were any failures
     */
    fun streamRows(rowToAsset: AssetGenerator, batchSize: Int, logger: KLogger, skipColumns: Set<String> = setOf()): Boolean {
        val client = Atlan.getDefaultClient()
        val primaryBatch = ParallelBatch(
            client,
            batchSize,
            true,
            AssetBatch.CustomMetadataHandling.MERGE,
            true,
            updateOnly,
        )
        val relatedBatch = ParallelBatch(
            client,
            batchSize,
            true,
            AssetBatch.CustomMetadataHandling.MERGE,
            true,
        )
        val relatedHolds: MutableMap<String, RelatedAssetHold> = ConcurrentHashMap()
        val deferDeletes: MutableMap<String, Set<AtlanField>> = ConcurrentHashMap()
        var someFailure = false

        val filteredRowCount = AtomicLong(0)
        counter.stream().skip(1).parallel().forEach { row ->
            if (rowToAsset.includeRow(row.fields, header, typeIdx, qualifiedNameIdx)) {
                filteredRowCount.incrementAndGet()
            }
        }
        val totalRowCount = filteredRowCount.get()
        // Step 1: load the main assets
        logger.info { "Loading a total of $totalRowCount assets..." }
        val count = AtomicLong(0)
        reader.stream().skip(1).parallel().forEach { r: CsvRow ->
            val assets = rowToAsset.buildFromRow(r.fields, header, typeIdx, qualifiedNameIdx, skipColumns)
            if (assets != null) {
                try {
                    val asset = assets.primary.build()
                    primaryBatch.add(asset)
                    Utils.logProgress(count, totalRowCount, logger, batchSize)
                    if (assets.related.isNotEmpty()) {
                        relatedHolds[asset.guid] = RelatedAssetHold(asset, assets.related)
                    }
                    if (assets.delete.isNotEmpty()) {
                        deferDeletes[asset.guid] = assets.delete
                    }
                } catch (e: AtlanException) {
                    logger.error("Unable to load batch.", e)
                }
            }
        }
        primaryBatch.flush()
        primaryBatch.created.forEach { asset ->
            created[asset.guid] = asset
        }
        val totalCreates = primaryBatch.created.size
        val totalUpdates = primaryBatch.updated.size
        val totalSkipped = primaryBatch.skipped.size
        val totalFailures = AtomicLong(0)
        someFailure = someFailure || primaryBatch.failures.isNotEmpty()
        logFailures(primaryBatch, logger, totalFailures)
        logSkipped(primaryBatch, logger)
        logger.info { "Total assets created: $totalCreates" }
        logger.info { "Total assets updated: $totalUpdates" }
        logger.info { "Total assets skipped: $totalSkipped" }
        logger.info { "Total assets failed : $totalFailures" }

        // Step 2: load the deferred related assets (and final-flush the main asset batches, too)
        val totalRelated = AtomicLong(0)
        val searchAndDelete = mutableMapOf<String, Set<AtlanField>>()
        relatedHolds.values.forEach { b -> totalRelated.getAndAdd(b.relatedMap.size.toLong()) }
        logger.info { "Processing $totalRelated total related assets in a second pass." }
        relatedHolds.entries.parallelStream().forEach { hold: MutableMap.MutableEntry<String, RelatedAssetHold> ->
            val placeholderGuid = hold.key
            val relatedAssetHold = hold.value
            val resolvedGuid = primaryBatch.resolvedGuids[placeholderGuid]
            val resolvedAsset = relatedAssetHold.fromAsset.toBuilder().guid(resolvedGuid).build() as Asset
            AssetRefXformer.buildRelated(resolvedAsset, relatedAssetHold.relatedMap, relatedBatch, count, totalRelated, logger, batchSize)
        }
        deferDeletes.entries.parallelStream().forEach { delete: MutableMap.MutableEntry<String, Set<AtlanField>> ->
            val placeholderGuid = delete.key
            val resolvedGuid = primaryBatch.resolvedGuids[placeholderGuid]!!
            searchAndDelete[resolvedGuid] = delete.value
        }

        // Step 3: final-flush the deferred related assets
        relatedBatch.flush()
        val totalCreatesR = relatedBatch.created.size
        val totalUpdatesR = relatedBatch.updated.size
        val totalFailuresR = AtomicLong(0)
        someFailure = someFailure || relatedBatch.failures.isNotEmpty()
        logFailures(relatedBatch, logger, totalFailuresR)
        logger.info { "Total related assets created: $totalCreatesR" }
        logger.info { "Total related assets updated: $totalUpdatesR" }
        logger.info { "Total related assets failed : $totalFailuresR" }

        // Step 4: bulk-delete any related assets marked for removal
        val totalToScan = searchAndDelete.size.toLong()
        val totalScanned = AtomicLong(0)
        val totalDeleted = AtomicLong(0)
        logger.info { "Scanning $totalToScan total assets in a final pass for possible README removal." }
        searchAndDelete.entries.parallelStream().forEach { entry ->
            val guid = entry.key
            val fields = entry.value
            client.assets.select()
                .where(Asset.GUID.eq(guid))
                .includesOnResults(fields)
                .stream()
                .forEach { result ->
                    val guids = mutableListOf<String>()
                    for (field in fields) {
                        val getter = ReflectionCache.getGetter(result.javaClass, field.atlanFieldName)
                        val reference = getter.invoke(result)
                        if (reference is Asset) {
                            guids.add(reference.guid)
                        } else if (reference != null && Collection::class.java.isAssignableFrom(reference.javaClass)) {
                            for (element in reference as Collection<*>) {
                                if (element is Asset) {
                                    guids.add(element.guid)
                                }
                            }
                        }
                    }
                    if (guids.isNotEmpty()) {
                        val response = client.assets.delete(guids, AtlanDeleteType.SOFT)
                        totalDeleted.getAndAdd(response.deletedAssets.size.toLong())
                    }
                }
            Utils.logProgress(totalScanned, totalToScan, logger, batchSize)
        }
        logger.info { "Total READMEs deleted: $totalDeleted" }
        return someFailure
    }

    private fun logFailures(b: ParallelBatch, logger: KLogger, totalFailures: AtomicLong) {
        if (b.failures.isNotEmpty()) {
            for (f in b.failures) {
                logger.info { "Failed batch reason: ${f.failureReason}" }
                totalFailures.getAndAdd(f.failedAssets.size.toLong())
                for (failed in f.failedAssets) {
                    logger.info {
                        " ... included asset: ${failed.typeName}::${failed.qualifiedName}"
                    }
                }
            }
        }
    }

    private fun logSkipped(b: ParallelBatch, logger: KLogger) {
        if (b.skipped.isNotEmpty()) {
            logger.info { "Skipped the following assets as they do not exist in Atlan (running in update-only mode):" }
            b.skipped?.forEach {
                logger.info { " ... skipped asset: ${it.typeName}::${it.qualifiedName}" }
            }
        }
    }

    /** {@inheritDoc}  */
    @Throws(IOException::class)
    override fun close() {
        reader.close()
    }

    data class RelatedAssetHold(
        val fromAsset: Asset,
        val relatedMap: Map<String, Collection<Asset>>,
    )
}
