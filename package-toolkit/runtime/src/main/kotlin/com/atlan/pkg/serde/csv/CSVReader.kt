/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.csv

import com.atlan.cache.ReflectionCache
import com.atlan.exception.AtlanException
import com.atlan.model.assets.Asset
import com.atlan.model.enums.AssetCreationHandling
import com.atlan.model.enums.AtlanDeleteType
import com.atlan.model.enums.AtlanTagHandling
import com.atlan.model.enums.CustomMetadataHandling
import com.atlan.model.enums.LinkIdempotencyInvariant
import com.atlan.model.fields.AtlanField
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.serde.cell.AssetRefXformer
import com.atlan.util.ParallelBatch
import de.siegmar.fastcsv.reader.CsvReader
import de.siegmar.fastcsv.reader.CsvRecord
import de.siegmar.fastcsv.reader.CsvRecordHandler
import mu.KLogger
import java.io.Closeable
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.atomic.AtomicLong

/**
 * Utility class for reading from CSV files, using FastCSV.
 *
 * @param path location and filename of the CSV file to read
 * @param updateOnly when true, the reader will first look up assets to ensure they exist (and only update them, never create)
 * @param trackBatches if true, minimal details about every asset created or updated is tracked (if false, only counts of each are tracked)
 * @param caseSensitive (only applies when updateOnly is true) attempt to match assets case-sensitively (true) or case-insensitively (false)
 * @param customMetadataHandling how to handle any custom metadata in the input file
 * @param atlanTagHandling how to handle any Atlan tag associations in the input file (by default this will replace tags, for backwards-compatibility)
 * @param creationHandling when allowing assets to be created, how they should be created (full or partial)
 * @param tableViewAgnostic if true, tables and views will be treated interchangeably (an asset in the batch marked as a table will attempt to match a view if not found as a table, and vice versa)
 * @param fieldSeparator character to use to separate fields (for example ',' or ';')
 * @param linkIdempotency how to avoid potential duplicate Links on assets, for example by treating their URL as unique or their name as unique
 */
class CSVReader
    @JvmOverloads
    constructor(
        path: String,
        private val updateOnly: Boolean,
        private val trackBatches: Boolean = true,
        private val caseSensitive: Boolean = true,
        private val customMetadataHandling: CustomMetadataHandling = CustomMetadataHandling.MERGE,
        private val atlanTagHandling: AtlanTagHandling = AtlanTagHandling.REPLACE,
        private val creationHandling: AssetCreationHandling = AssetCreationHandling.FULL,
        private val tableViewAgnostic: Boolean = false,
        fieldSeparator: Char = ',',
        private val linkIdempotency: LinkIdempotencyInvariant = LinkIdempotencyInvariant.URL,
    ) : Closeable {
        private val reader: CsvReader<CsvRecord>
        private val counter: CsvReader<CsvRecord>
        private val preproc: CsvReader<CsvRecord>
        private val header: List<String> = CSVXformer.getHeader(path, fieldSeparator)
        private val typeIdx: Int = header.indexOf(Asset.TYPE_NAME.atlanFieldName)
        private val qualifiedNameIdx: Int = header.indexOf(Asset.QUALIFIED_NAME.atlanFieldName)
        private val includesTags: Boolean = header.indexOf("atlanTags") != -1

        init {
            if (typeIdx < 0) {
                throw IOException(
                    "Unable to find the column 'typeName'. This is a mandatory column in the input CSV.",
                )
            }
            val inputFile = Paths.get(path)
            val builder =
                CsvReader
                    .builder()
                    .fieldSeparator(fieldSeparator)
                    .quoteCharacter('"')
                    .skipEmptyLines(true)
                    .ignoreDifferentFieldCount(false)
            reader = builder.ofCsvRecord(inputFile)
            counter = builder.ofCsvRecord(inputFile)
            preproc = builder.ofCsvRecord(inputFile)
        }

        /**
         * Preprocess the CSV file row-by-row, optionally outputting a transformed file.
         *
         * @param csvPreprocessor preprocess for a row of CSV values
         * @param logger through which to report the overall progress
         * @param outputFile (optional) name of the output file into which to write preprocessed row values
         * @param outputHeaders (optional) header column names to output into the file containing preprocessed row values
         * @return any resulting details captured during the preprocessing
         */
        fun preprocess(
            csvPreprocessor: RowPreprocessor,
            logger: KLogger,
            outputFile: String? = null,
            outputHeaders: List<String>? = null,
        ): RowPreprocessor.Results =
            if (outputFile != null) {
                logger.info { "Transforming input CSV file to $outputFile..." }
                CSVWriter(outputFile).use { csv ->
                    csv.writeHeader(outputHeaders ?: header)
                    preproc.stream().skip(1).forEach { r: CsvRecord ->
                        val transformed = csvPreprocessor.preprocessRow(r.fields, header, typeIdx, qualifiedNameIdx)
                        csv.writeRecord(transformed)
                    }
                    preproc.close()
                }
                csvPreprocessor.finalize(header, outputFile)
            } else {
                logger.info { "Preprocessing input CSV file..." }
                preproc.stream().skip(1).forEach { r: CsvRecord ->
                    csvPreprocessor.preprocessRow(r.fields, header, typeIdx, qualifiedNameIdx)
                }
                preproc.close()
                csvPreprocessor.finalize(header)
            }

        /**
         * Parallel-read the CSV file into batched asset updates against Atlan.
         * Note: this requires the input CSV file to be fully parallel-loadable without any
         * conflicts. That means: every row is a unique asset, no two rows update any relationship
         * attribute that points at the same related asset (such as an assigned term).
         *
         * @param ctx context in which the custom package is running
         * @param rowToAsset translator from a row of CSV values to an asset object
         * @param batchSize maximum number of Assets to bulk-save in Atlan per API request
         * @param logger through which to report the overall progress
         * @param skipColumns columns to skip during the processing (i.e. where they need to be processed in a later pass)
         * @return details of the results of the import
         */
        fun streamRows(
            ctx: PackageContext<*>,
            rowToAsset: AssetGenerator,
            batchSize: Int,
            logger: KLogger,
            skipColumns: Set<String> = setOf(),
        ): ImportResults {
            val relatedHolds: MutableMap<String, RelatedAssetHold> = ConcurrentHashMap()
            val deferDeletes: MutableMap<String, Set<AtlanField>> = ConcurrentHashMap()
            var someFailure = false

            val parallelism = ForkJoinPool.getCommonPoolParallelism()
            val filteredRowCount = AtomicLong(0)
            counter.stream().skip(1).forEach { row ->
                if (rowToAsset.includeRow(row.fields, header, typeIdx, qualifiedNameIdx)) {
                    filteredRowCount.incrementAndGet()
                }
            }
            counter.close()
            val totalRowCount = filteredRowCount.get()
            ParallelBatch(
                ctx.client,
                batchSize,
                if (includesTags) atlanTagHandling else AtlanTagHandling.IGNORE,
                customMetadataHandling,
                true,
                updateOnly,
                trackBatches,
                caseSensitive,
                creationHandling,
                tableViewAgnostic,
            ).use { primaryBatch ->
                ParallelBatch(
                    ctx.client,
                    batchSize,
                    AtlanTagHandling.IGNORE,
                    CustomMetadataHandling.IGNORE,
                    true,
                    false,
                    trackBatches,
                    caseSensitive,
                    AssetCreationHandling.FULL,
                    false,
                ).use { relatedBatch ->
                    // Step 0: split the single file into one file per thread
                    val csvChunkFiles = mutableListOf<Path>()
                    val chunkSize = totalRowCount / parallelism
                    val currentRecordCount = AtomicLong(0)
                    var chunkPath = Files.createTempFile("chunk_0_", ".csv")
                    var writer = CSVWriter(chunkPath.toString(), ',')
                    reader.stream().skip(1).forEach { row: CsvRecord ->
                        // Split the original file up into multiple smaller files for parallel-processing
                        if (rowToAsset.includeRow(row.fields, header, typeIdx, qualifiedNameIdx)) {
                            // open initial file
                            val current = currentRecordCount.incrementAndGet()
                            writer.writeRecord(row.fields)
                            if (current > chunkSize) {
                                writer.close()
                                csvChunkFiles.add(chunkPath)
                                currentRecordCount.set(0)
                                chunkPath = Files.createTempFile("chunk_${csvChunkFiles.size}_", ".csv")
                                writer = CSVWriter(chunkPath.toString(), ',')
                            }
                        }
                    }
                    reader.close()
                    writer.close()
                    if (currentRecordCount.get() > 0) {
                        // If there are any remaining records, close the writer and add it
                        csvChunkFiles.add(chunkPath)
                    }
                    // Step 1: load the main assets
                    logger.info { "Loading a total of $totalRowCount assets..." }
                    val count = AtomicLong(0)
                    csvChunkFiles.parallelStream().forEach { f ->
                        val reader =
                            CsvReader
                                .builder()
                                .fieldSeparator(',')
                                .quoteCharacter('"')
                                .skipEmptyLines(true)
                                .ignoreDifferentFieldCount(false)
                                .build(CsvRecordHandler(), f)
                        reader.stream().forEach { r: CsvRecord ->
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
                    }
                    // Delete temp files
                    csvChunkFiles.forEach { it.toFile().delete() }
                    primaryBatch.flush()
                    val totalCreates = primaryBatch.numCreated
                    val totalUpdates = primaryBatch.numUpdated
                    val totalRestore = primaryBatch.numRestored
                    val totalSkipped = primaryBatch.numSkipped
                    val totalFailures = AtomicLong(0)
                    someFailure = someFailure || (primaryBatch.failures?.isNotEmpty() == true)
                    logFailures(primaryBatch, logger, totalFailures)
                    logSkipped(primaryBatch, logger)
                    logger.info { "Total assets created : $totalCreates" }
                    logger.info { "Total assets updated : $totalUpdates" }
                    logger.info { "Total assets restored: $totalRestore" }
                    if (totalSkipped > 0) logger.warn { "Total assets skipped : $totalSkipped" } else logger.info { "Total assets skipped : $totalSkipped" }
                    if (totalFailures.get() > 0) logger.warn { "Total assets failed  : $totalFailures" } else logger.info { "Total assets failed  : $totalFailures" }

                    // Step 2: load the deferred related assets (and final-flush the main asset batches, too)
                    val totalRelated = AtomicLong(0)
                    val relatedCount = AtomicLong(0)
                    val searchAndDelete: MutableMap<String, Set<AtlanField>> = ConcurrentHashMap()
                    relatedHolds.values.forEach { b -> totalRelated.getAndAdd(b.relatedMap.size.toLong()) }
                    logger.info { "Processing $totalRelated total related assets in a second pass." }
                    relatedHolds.entries.parallelStream().forEach { hold: MutableMap.MutableEntry<String, RelatedAssetHold> ->
                        val placeholderGuid = hold.key
                        val relatedAssetHold = hold.value
                        val resolvedGuid = primaryBatch.resolvedGuids[placeholderGuid]
                        if (!resolvedGuid.isNullOrBlank()) {
                            val resolvedAsset =
                                relatedAssetHold.fromAsset
                                    .toBuilder()
                                    .guid(resolvedGuid)
                                    .build() as Asset
                            AssetRefXformer.buildRelated(
                                ctx,
                                resolvedAsset,
                                relatedAssetHold.relatedMap,
                                relatedBatch,
                                relatedCount,
                                totalRelated,
                                logger,
                                batchSize,
                                linkIdempotency,
                            )
                        } else {
                            logger.info { " ... skipped related asset as primary asset was skipped (above)." }
                            relatedCount.getAndIncrement()
                        }
                    }
                    deferDeletes.entries.parallelStream().forEach { delete: MutableMap.MutableEntry<String, Set<AtlanField>> ->
                        val placeholderGuid = delete.key
                        val resolvedGuid = primaryBatch.resolvedGuids[placeholderGuid]
                        if (!resolvedGuid.isNullOrBlank()) {
                            searchAndDelete[resolvedGuid] = delete.value
                        }
                    }

                    // Step 3: final-flush the deferred related assets
                    relatedBatch.flush()
                    val totalCreatesR = relatedBatch.numCreated
                    val totalUpdatesR = relatedBatch.numUpdated
                    val totalFailuresR = AtomicLong(0)
                    someFailure = someFailure || (relatedBatch.failures?.isNotEmpty() == true)
                    logFailures(relatedBatch, logger, totalFailuresR)
                    logger.info { "Total related assets created: $totalCreatesR" }
                    logger.info { "Total related assets updated: $totalUpdatesR" }
                    if (totalFailuresR.get() > 0) logger.warn { "Total related assets failed : $totalFailuresR" } else logger.info { "Total related assets failed : $totalFailuresR" }

                    // Step 4: bulk-delete any related assets marked for removal
                    val totalToScan = searchAndDelete.size.toLong()
                    val totalScanned = AtomicLong(0)
                    val totalDeleted = AtomicLong(0)
                    logger.info { "Scanning $totalToScan total assets in a final pass for possible README removal." }
                    searchAndDelete.entries.parallelStream().forEach { entry ->
                        val guid = entry.key
                        val fields = entry.value
                        ctx.client.assets
                            .select()
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
                                    val response = ctx.client.assets.delete(guids, AtlanDeleteType.SOFT)
                                    totalDeleted.getAndAdd(response.deletedAssets.size.toLong())
                                }
                            }
                        Utils.logProgress(totalScanned, totalToScan, logger, batchSize)
                    }
                    logger.info { "Total READMEs deleted: $totalDeleted" }
                    // Note: it looks weird that we combineAll here, but this is necessary to COPY contents of
                    // the details, as the originals will be auto-closed prior to returning
                    val results =
                        ImportResults(
                            someFailure,
                            ImportResults.Details.combineAll(
                                ctx.client,
                                true,
                                ImportResults.Details(
                                    primaryBatch.resolvedGuids.toMap(),
                                    primaryBatch.resolvedQualifiedNames.toMap(),
                                    primaryBatch.created,
                                    primaryBatch.updated,
                                    primaryBatch.restored,
                                    primaryBatch.skipped,
                                    primaryBatch.failures,
                                    primaryBatch.numCreated,
                                    primaryBatch.numUpdated,
                                    primaryBatch.numRestored,
                                ),
                            ),
                            ImportResults.Details.combineAll(
                                ctx.client,
                                true,
                                ImportResults.Details(
                                    relatedBatch.resolvedGuids.toMap(),
                                    primaryBatch.resolvedQualifiedNames.toMap(),
                                    relatedBatch.created,
                                    relatedBatch.updated,
                                    relatedBatch.restored,
                                    relatedBatch.skipped,
                                    relatedBatch.failures,
                                    relatedBatch.numCreated,
                                    relatedBatch.numUpdated,
                                    relatedBatch.numRestored,
                                ),
                            ),
                        )
                    return results
                }
            }
        }

        private fun logFailures(
            b: ParallelBatch,
            logger: KLogger,
            totalFailures: AtomicLong,
        ) {
            if (b.failures?.isNotEmpty() == true) {
                for (f in b.failures.entrySet()) {
                    logger.warn { "Failed batch reason: ${f.value.failureReason}" }
                    totalFailures.getAndAdd(
                        f.value.failedAssets.size
                            .toLong(),
                    )
                    for (failed in f.value.failedAssets) {
                        logger.warn {
                            " ... included asset: ${failed.typeName}::${failed.qualifiedName}"
                        }
                    }
                }
            }
        }

        private fun logSkipped(
            b: ParallelBatch,
            logger: KLogger,
        ) {
            if (b.skipped != null && b.skipped.isNotEmpty) {
                logger.warn { "Skipped the following assets as they do not exist in Atlan (running in update-only mode):" }
                b.skipped?.values()?.forEach {
                    logger.warn { " ... skipped asset: ${it.typeName}::${it.qualifiedName}" }
                }
            }
        }

        /** {@inheritDoc}  */
        @Throws(IOException::class)
        override fun close() {
            preproc.close()
            counter.close()
            reader.close()
        }

        data class RelatedAssetHold(
            val fromAsset: Asset,
            val relatedMap: Map<String, Collection<Asset>>,
        )
    }
