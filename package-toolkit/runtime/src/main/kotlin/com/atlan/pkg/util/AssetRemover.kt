/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.util

import com.atlan.Atlan
import com.atlan.model.assets.Asset
import com.atlan.model.enums.AtlanDeleteType
import com.atlan.model.search.FluentSearch
import com.atlan.util.AssetBatch
import de.siegmar.fastcsv.reader.CsvReader
import de.siegmar.fastcsv.reader.CsvRecord
import mu.KLogger
import java.io.IOException
import java.nio.file.Paths
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.round

/**
 * Utility class to help pre-calculate a delta between full-load files.
 * When we receive files that contain a full load (refresh) of an entire set of assets, it can
 * be very time-consuming to figure out which assets to remove. This utility class tackles the
 * problem by comparing the latest input file with a previously-loaded input file in order to
 * calculate which assets appeared in the previously-loaded input file which no longer appear in
 * the latest input file. This is done entirely independently of any calls to Atlan itself,
 * thereby reducing time to calculate which assets should be removed.
 *
 * @param connectionsMap a mapping from tenant-agnostic connection identity to tenant-specific qualifiedName for the connection
 * @param resolver for resolving asset identities entirely from CSV file input (no calls to Atlan)
 * @param logger for tracking status and documenting any errors
 * @param removeTypes names of asset types that should be considered for deleted (default: all)
 * @param removalPrefix qualifiedName prefix that must match an asset for it to be deleted (default: all)
 * @param purge if true, any asset that matches will be permanently deleted (otherwise, default: only archived)
 */
class AssetRemover(
    private val connectionsMap: Map<AssetResolver.ConnectionIdentity, String>,
    private val resolver: AssetResolver,
    private val logger: KLogger,
    private val removeTypes: List<String> = listOf(),
    private val removalPrefix: String = "",
    private val purge: Boolean = false,
) {
    private val client = Atlan.getDefaultClient()
    val assetsToDelete = ConcurrentHashMap<AssetBatch.AssetIdentity, String>()

    companion object {
        private const val QUERY_BATCH = 50
        private const val DELETION_BATCH = 20
    }

    /**
     * Calculate which qualifiedNames should be deleted, by determining which qualifiedNames
     * appear in the previousFile that no longer appear in the currentFile.
     *
     * @param currentFile the latest file that should be loaded
     * @param previousFile the previous file that was loaded for the same assets
     * @throws IOException if there is no typeName column in the CSV file
     */
    @Throws(IOException::class)
    fun calculateDeletions(currentFile: String, previousFile: String) {
        logger.info { " --- Calculating delta... ---" }
        logger.info { " ... latest file: $currentFile" }
        logger.info { " ... previous file: $previousFile" }
        val currentIdentities = getAssetIdentities(currentFile)
        val previousIdentities = getAssetIdentities(previousFile)
        previousIdentities.forEach {
            if (!currentIdentities.contains(it)) {
                assetsToDelete[it] = ""
            }
        }
    }

    /**
     * Indicates whether any assets have been identified to delete.
     *
     * @return true if there is at least one asset to be deleted, otherwise false
     */
    fun hasAnythingToDelete(): Boolean {
        return assetsToDelete.isNotEmpty()
    }

    /**
     * Actually run the removal of any assets identified for deletion.
     */
    fun deleteAssets() {
        translateToGuids()
        deleteAssetsByGuid()
    }

    /**
     * Create a set of unique asset identities that appear in the provided file.
     *
     * @param filename to a CSV file having at least a typeName field
     * @return set of unique asset identities present in the file
     * @throws IOException if there is no typeName column in the CSV file
     */
    @Throws(IOException::class)
    private fun getAssetIdentities(filename: String): Set<AssetBatch.AssetIdentity> {
        val inputFile = Paths.get(filename)
        val builder = CsvReader.builder()
            .fieldSeparator(',')
            .quoteCharacter('"')
            .skipEmptyLines(true)
            .ignoreDifferentFieldCount(false)
        val header = builder.ofCsvRecord(inputFile).use { tmp ->
            tmp.stream().findFirst().map { obj: CsvRecord -> obj.fields }
                .orElse(emptyList())
        }
        val typeIdx = header.indexOf(Asset.TYPE_NAME.atlanFieldName)
        if (typeIdx < 0) {
            throw IOException(
                "Unable to find the column 'typeName'. This is a mandatory column in the input CSV.",
            )
        }
        val reader = builder.ofCsvRecord(inputFile)
        val set = mutableSetOf<AssetBatch.AssetIdentity>()
        reader.stream().skip(1).forEach { r: CsvRecord ->
            val values = r.fields
            val typeName = values[typeIdx]!!
            val qnDetails = resolver.getQualifiedNameDetails(values, header, typeName)
            val agnosticQN = qnDetails.uniqueQN
            val connectionIdentity = resolver.getConnectionIdentityFromQN(agnosticQN)
            if (connectionIdentity != null && connectionsMap.containsKey(connectionIdentity)) {
                val qualifiedName =
                    agnosticQN.replaceFirst(connectionIdentity.toString(), connectionsMap[connectionIdentity]!!)
                set.add(AssetBatch.AssetIdentity(typeName, qualifiedName))
            } else {
                logger.warn { "Unknown connection used in asset -- skipping: $agnosticQN" }
            }
        }
        return set
    }

    /**
     * Translate all assets to delete to their GUIDs
     */
    private fun translateToGuids() {
        val totalToTranslate = assetsToDelete.size
        logger.info { " --- Translating $totalToTranslate qualifiedNames to GUIDs... ---" }
        // Skip archived assets, as they're already deleted (leave it to separate process to
        // purge them, if desired)
        val qualifiedNamesToDelete = assetsToDelete.map { it.key.qualifiedName }.toList()
        val currentCount = AtomicLong(0)
        if (totalToTranslate < QUERY_BATCH) {
            translate(qualifiedNamesToDelete)
        } else {
            // Translate from qualifiedName to GUID in parallel
            qualifiedNamesToDelete
                .asSequence()
                .chunked(QUERY_BATCH)
                .toList()
                .parallelStream()
                .forEach { batch ->
                    val i = currentCount.getAndAdd(QUERY_BATCH.toLong())
                    logger.info { " ... next batch of $QUERY_BATCH (${round((i.toDouble() / totalToTranslate) * 100)}%)" }
                    translate(batch)
                }
        }
    }

    /**
     * Translate a specific list of qualifiedNames to GUIDs.
     *
     * @param qualifiedNamesToDelete the list of qualifiedNames to query all at the same time
     */
    private fun translate(qualifiedNamesToDelete: List<String>) {
        val builder = client.assets.select()
            .pageSize(QUERY_BATCH)
            .where(Asset.QUALIFIED_NAME.`in`(qualifiedNamesToDelete))
        if (removeTypes.isNotEmpty()) {
            builder.where(FluentSearch.assetTypes(removeTypes))
        }
        if (removalPrefix.isNotBlank()) {
            builder.where(Asset.QUALIFIED_NAME.startsWith(removalPrefix))
        }
        val response = builder
            .toRequestBuilder()
            .excludeMeanings(true)
            .excludeAtlanTags(true)
            .build()
            .search()
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
        val candidate = AssetBatch.AssetIdentity(asset.typeName, asset.qualifiedName)
        if (assetsToDelete.containsKey(candidate)) {
            assetsToDelete[candidate] = asset.guid
        }
    }

    /**
     * Delete all assets we have identified for deletion, in batches of 20 at a time.
     */
    private fun deleteAssetsByGuid() {
        if (assetsToDelete.isNotEmpty()) {
            val deletionType = if (purge) AtlanDeleteType.PURGE else AtlanDeleteType.SOFT
            val guidList = assetsToDelete.values.filter { it.isNotBlank() }.toList()
            val totalToDelete = guidList.size
            logger.info { " --- Deleting ($deletionType) $totalToDelete assets across $removeTypes... ---" }
            val currentCount = AtomicLong(0)
            if (totalToDelete < DELETION_BATCH) {
                if (totalToDelete > 0) {
                    client.assets.delete(guidList, deletionType)
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
                            client.assets.delete(batch, deletionType)
                        }
                    }
            }
        }
    }
}
