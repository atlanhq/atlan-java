/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.util

import com.atlan.AtlanClient
import com.atlan.cache.OffHeapAssetCache
import com.atlan.model.assets.Asset
import com.atlan.model.core.AtlanCloseable
import com.atlan.model.enums.AtlanDeleteType
import com.atlan.pkg.PackageContext
import com.atlan.pkg.cache.ChecksumCache
import com.atlan.pkg.cache.PersistentConnectionCache
import com.atlan.pkg.serde.csv.CSVXformer
import com.atlan.util.AssetBatch.AssetIdentity
import com.google.common.hash.Hashing
import de.siegmar.fastcsv.reader.CsvReader
import de.siegmar.fastcsv.reader.CsvRecord
import mu.KLogger
import java.io.File.separator
import java.io.IOException
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.round
import kotlin.streams.asSequence

/**
 * Utility class to help pre-calculate a delta between full-load files.
 * When we receive files that contain a full load (refresh) of an entire set of assets, it can
 * be very time-consuming to figure out which assets have actually changed or need to be removed.
 * This utility class tackles the problem by comparing the latest input file with a
 * previously-loaded input file in order to calculate which assets appeared in the previously-loaded
 * input file which no longer appear in the latest input file (and therefore need to be removed).
 * It also calculates a checksum for each comparable row between the files, and only retains any
 * rows in the new file that have a different checksum than they did in the previous file
 * (indicating that something on them has changed, and therefore they need to be re-loaded).
 * This is done entirely independently of any calls to Atlan itself, thereby reducing time to
 * calculate which assets should be removed or updated.
 *
 * @param ctx context of the running package
 * @param resolver for resolving asset identities entirely from CSV file input (no calls to Atlan)
 * @param logger for tracking status and documenting any errors
 * @param removeTypes names of asset types that should be considered for deletion (default: all)
 * @param removalPrefix qualifiedName prefix that must match an asset for it to be deleted (default: all)
 * @param purge if true, any asset that matches will be permanently deleted (otherwise, default: only archived)
 * @param compareChecksums if true, compare the checksums of every asset identity to determine which have changed (otherwise, default: skip checksum comparisons)
 * @param fallback directory to use as a fallback backing store (locally) in the absence of an object store
 */
class FileBasedDelta(
    private val ctx: PackageContext<*>,
    private val resolver: AssetResolver,
    private val logger: KLogger,
    private val removeTypes: List<String> = listOf(),
    private val removalPrefix: String = "",
    private val purge: Boolean = false,
    private val compareChecksums: Boolean = false,
    private val fallback: String = Paths.get(separator, "tmp").toString(),
) : AtlanCloseable {
    val assetsToReload = ChecksumCache("changes")
    val assetsToDelete = ChecksumCache("deletes")
    private var guidsToDeleteToDetails: OffHeapAssetCache? = null

    companion object {
        private const val QUERY_BATCH = 50
        private const val DELETION_BATCH = 20
    }

    /**
     * Calculate which assets should be reloaded or deleted, by determining which unique
     * asset identities appear in the previousFile that no longer appear in the currentFile,
     * or which identities appear to have different content between the two files.
     *
     * @param currentFile the latest file that should be loaded
     * @param previousFile the previous file that was loaded for the same assets
     * @throws IOException if there is no typeName column in the CSV file
     */
    @Throws(IOException::class)
    fun calculateDelta(
        currentFile: String,
        previousFile: String,
    ) {
        logger.info { " --- Calculating delta... ---" }
        logger.info { " ... latest file: $currentFile" }
        logger.info { " ... previous file: $previousFile" }
        ChecksumCache("current-checksum").use { current ->
            getAssetChecksums(currentFile, current)
            ChecksumCache("previous-checksum").use { previous ->
                getAssetChecksums(previousFile, previous)
                previous.entrySet().forEach { entry ->
                    if (!current.containsKey(entry.key)) {
                        assetsToDelete.put(entry.key, entry.value)
                    } else if (compareChecksums) {
                        val checksum = entry.value
                        if (current.get(entry.key) != checksum) {
                            assetsToReload.put(entry.key, checksum)
                        }
                    }
                }
                if (compareChecksums) {
                    // Now go through the latest file, and if there are any assets there that did not
                    // appear in the previous file these are net-new assets -- make sure we include them, too
                    current.entrySet().forEach { entry ->
                        if (!previous.containsKey(entry.key)) {
                            assetsToReload.put(entry.key, entry.value)
                        }
                    }
                }
            }
        }
    }

    /**
     * Indicates whether any assets have been identified to delete.
     *
     * @return true if there is at least one asset to be deleted, otherwise false
     */
    fun hasAnythingToDelete(): Boolean = assetsToDelete.isNotEmpty

    /**
     * Actually run the removal of any assets identified for deletion.
     *
     * @param client connectivity to the Atlan tenant
     * @return a list of the assets that were deleted
     */
    fun deleteAssets(client: AtlanClient): OffHeapAssetCache {
        guidsToDeleteToDetails = OffHeapAssetCache(client, "delete")
        translateToGuids(client)
        deleteAssetsByGuid(client)
        return guidsToDeleteToDetails!!
    }

    /**
     * Resolve the asset represented by a row of values in a CSV to an asset identity.
     *
     * @param values row of values for that asset from the CSV
     * @param header order of column names in the CSV file being processed
     * @return a unique asset identity for that row of the CSV
     */
    @Throws(IOException::class)
    fun resolveAsset(
        values: List<String>,
        header: List<String>,
    ): AssetIdentity? {
        val identity = resolver.resolveAsset(ctx, values, header)
        if (identity == null) {
            logger.warn { "Unknown connection used in asset -- skipping: $values" }
        }
        return identity
    }

    /**
     * Create a set of unique asset identities that appear in the provided file.
     *
     * @param filename to a CSV file having at least a typeName field
     * @param cache in which to record the checksums
     * @throws IOException if there is no typeName column in the CSV file
     */
    @Throws(IOException::class)
    private fun getAssetChecksums(
        filename: String,
        cache: ChecksumCache,
    ) {
        val header = CSVXformer.getHeader(filename, ',')
        val typeIdx = header.indexOf(Asset.TYPE_NAME.atlanFieldName)
        if (typeIdx < 0) {
            throw IOException(
                "Unable to find the column 'typeName'. This is a mandatory column in the input CSV.",
            )
        }
        val inputFile = Paths.get(filename)
        val builder =
            CsvReader
                .builder()
                .fieldSeparator(',')
                .quoteCharacter('"')
                .skipEmptyLines(true)
                .allowExtraFields(false)
                .allowMissingFields(false)
        val reader = builder.ofCsvRecord(inputFile)
        reader.stream().skip(1).forEach { r: CsvRecord ->
            val values = r.fields
            val assetIdentity = resolveAsset(values, header)
            if (assetIdentity != null) {
                val singleLine = r.fields.joinToString("§")
                val checksum = Hashing.murmur3_128().hashString(singleLine, Charsets.UTF_8).toString()
                cache.put(assetIdentity, checksum)
            }
        }
    }

    /**
     * Translate all assets to delete to their GUIDs
     *
     * @param client connectivity to the Atlan tenant
     */
    private fun translateToGuids(client: AtlanClient) {
        val totalToTranslate = assetsToDelete.size
        logger.info { " --- Translating $totalToTranslate qualifiedNames to GUIDs... ---" }
        // Skip archived assets, as they're already deleted (leave it to separate process to
        // purge them, if desired)
        val currentCount = AtomicLong(0)
        if (totalToTranslate < QUERY_BATCH) {
            translate(client, assetsToDelete.entrySet().map { it.key.qualifiedName }.toList())
        } else {
            // Translate from qualifiedName to GUID in parallel
            assetsToDelete
                .entrySet()
                .map { it.key.qualifiedName }
                .asSequence()
                .chunked(QUERY_BATCH)
                .toList()
                .parallelStream()
                .forEach { batch ->
                    val i = currentCount.getAndAdd(QUERY_BATCH.toLong())
                    logger.info { " ... next batch of $QUERY_BATCH (${round((i.toDouble() / totalToTranslate) * 100)}%)" }
                    translate(client, batch)
                }
        }
    }

    /**
     * Translate a specific list of qualifiedNames to GUIDs.
     *
     * @param client connectivity to the Atlan tenant
     * @param qualifiedNamesToDelete the list of qualifiedNames to query all at the same time
     */
    private fun translate(
        client: AtlanClient,
        qualifiedNamesToDelete: List<String>,
    ) {
        val builder =
            client.assets
                .select()
                .pageSize(QUERY_BATCH)
                .where(Asset.QUALIFIED_NAME.`in`(qualifiedNamesToDelete))
                .includesOnResults(PersistentConnectionCache.REQUIRED_FIELDS)
        if (removeTypes.isNotEmpty()) {
            builder.where(Asset.TYPE_NAME.`in`(removeTypes))
        }
        if (removalPrefix.isNotBlank()) {
            builder.where(Asset.QUALIFIED_NAME.startsWith(removalPrefix))
        }
        val response =
            builder
                .toRequestBuilder()
                .excludeMeanings(true)
                .excludeAtlanTags(true)
                .build()
                .search(client)
        response.forEach { validateResult(it) }
    }

    /**
     * Ensure the asset provided is one that we can delete (checking BOTH
     * typeName and qualifiedName match expectations), and if so, track its
     * GUID for deletion.
     *
     * @param asset to validate can be deleted, and if so, to track its GUID for deletion
     */
    private fun validateResult(asset: Asset) {
        val candidate = AssetIdentity(asset.typeName, asset.qualifiedName)
        if (assetsToDelete.containsKey(candidate)) {
            guidsToDeleteToDetails?.add(asset)
        }
    }

    /**
     * Delete all assets we have identified for deletion, in batches of 20 at a time.
     *
     * @param client connectivity to the Atlan tenant
     */
    private fun deleteAssetsByGuid(client: AtlanClient) {
        if (guidsToDeleteToDetails?.isNotEmpty == true) {
            val deletionType = if (purge) AtlanDeleteType.PURGE else AtlanDeleteType.SOFT
            val guidList = guidsToDeleteToDetails!!.entrySet()
            val totalToDelete = guidsToDeleteToDetails!!.size
            if (removeTypes.isNotEmpty()) {
                logger.info { " --- Deleting ($deletionType) $totalToDelete assets (limited to types: $removeTypes)... ---" }
            } else {
                logger.info { " --- Deleting ($deletionType) $totalToDelete assets... ---" }
            }
            val currentCount = AtomicLong(0)
            if (totalToDelete < DELETION_BATCH) {
                if (totalToDelete > 0) {
                    client.assets.delete(guidList.map { it.key }.toList(), deletionType)
                }
            } else {
                // Delete in parallel
                guidList
                    .asSequence()
                    .chunked(DELETION_BATCH)
                    .toList()
                    .parallelStream()
                    .forEach { batch ->
                        val i = currentCount.getAndAdd(DELETION_BATCH.toLong())
                        logger.info { " ... next batch of $DELETION_BATCH (${round((i.toDouble() / totalToDelete) * 100)}%)" }
                        if (batch.isNotEmpty()) {
                            client.assets.delete(batch.map { it.key }, deletionType)
                        }
                    }
            }
        }
    }

    /** {@inheritDoc} */
    override fun close() {
        AtlanCloseable.close(assetsToReload)
        AtlanCloseable.close(assetsToDelete)
        if (guidsToDeleteToDetails != null) {
            AtlanCloseable.close(guidsToDeleteToDetails)
        }
    }
}
