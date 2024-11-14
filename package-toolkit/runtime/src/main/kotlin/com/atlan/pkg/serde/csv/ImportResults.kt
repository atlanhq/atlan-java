/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.csv

import com.atlan.AtlanClient
import com.atlan.cache.OffHeapAssetCache
import com.atlan.util.AssetBatch.AssetIdentity
import java.io.Closeable
import java.io.IOException

/**
 * Class to capture details about the results of an import.
 *
 * @param anyFailures if there were 0 failures in the import this will be false, otherwise true
 * @param primary details about the primary assets
 * @param related details about the related assets (READMEs, links, etc)
 */
data class ImportResults(
    val anyFailures: Boolean,
    val primary: Details,
    val related: Details,
) : Closeable {
    /**
     * Details about the import results.
     *
     * @param guidAssignments mapping from placeholder to actual (resolved) GUIDs, even if no change was made to an asset
     * @param qualifiedNames mapping from case-insensitive to actual (resolved) qualifiedName, even if no change was made to an asset
     * @param created list of (minimal) assets that were created (note: when tracking is turned off in batch-processing, this will be null)
     * @param updated list of (minimal) assets that were updated (note: when tracking is turned off in batch-processing, this will be null)
     * @param restored list of (minimal) assets that were potentially-restored (note: when tracking is turned off in batch-processing, this will be null)
     * @param skipped list of (minimal) assets that were skipped
     * @param numCreated number of assets that were created (count only)
     * @param numUpdated number of assets that were updated (count only)
     * @param numRestored number of assets that were potentially restored (count only)
     */
    class Details(
        val guidAssignments: Map<String, String>,
        val qualifiedNames: Map<AssetIdentity, String>,
        val created: OffHeapAssetCache?,
        val updated: OffHeapAssetCache?,
        val restored: OffHeapAssetCache?,
        val skipped: OffHeapAssetCache?,
        val numCreated: Long,
        val numUpdated: Long,
        val numRestored: Long,
    ) : Closeable {
        companion object {
            /**
             * Combine multiple sets of details with another.
             *
             * @param client connectivity to the Atlan tenant
             * @param closeOriginal whether to close the original caches
             * @param others the sets of details to combine
             * @return the combined set of details, as a single set of details
             */
            fun combineAll(
                client: AtlanClient,
                closeOriginal: Boolean,
                vararg others: Details?,
            ): Details {
                var totalCreated = 0L
                var totalUpdated = 0L
                var totalRestored = 0L
                var totalSkipped = 0L
                others.filterNotNull()
                    .forEach { result ->
                        totalCreated += result.numCreated
                        totalUpdated += result.numUpdated
                        totalRestored += result.numRestored
                        totalSkipped += result.skipped?.size() ?: 0
                    }
                val created = OffHeapAssetCache(client, "ir-created")
                val updated = OffHeapAssetCache(client, "ir-updated")
                val restored = OffHeapAssetCache(client, "ir-restored")
                val skipped = OffHeapAssetCache(client, "ir-skipped")
                val guidAssignments = mutableMapOf<String, String>()
                val qualifiedNames = mutableMapOf<AssetIdentity, String>()
                others.filterNotNull()
                    .forEach { result ->
                        guidAssignments.putAll(result.guidAssignments)
                        qualifiedNames.putAll(result.qualifiedNames)
                        created.extendedWith(result.created, closeOriginal)
                        updated.extendedWith(result.updated, closeOriginal)
                        restored.extendedWith(result.restored, closeOriginal)
                        skipped.extendedWith(result.skipped, closeOriginal)
                        if (closeOriginal) {
                            result.close()
                        }
                    }
                return Details(
                    guidAssignments,
                    qualifiedNames,
                    created,
                    updated,
                    restored,
                    skipped,
                    totalCreated,
                    totalUpdated,
                    totalRestored,
                )
            }
        }

        /** {@inheritDoc} */
        override fun close() {
            var exception: IOException? = null
            try {
                created?.close()
            } catch (e: IOException) {
                exception = e
            }
            try {
                updated?.close()
            } catch (e: IOException) {
                if (exception != null) exception.addSuppressed(e) else exception = e
            }
            try {
                restored?.close()
            } catch (e: IOException) {
                if (exception != null) exception.addSuppressed(e) else exception = e
            }
            try {
                skipped?.close()
            } catch (e: IOException) {
                if (exception != null) exception.addSuppressed(e) else exception = e
            }
            if (exception != null) throw exception
        }
    }

    companion object {
        /**
         * Retrieve all the assets that were created or updated across any of the provided
         * import results.
         *
         * @param client connectivity to the Atlan tenant
         * @param closeOriginal whether to close the original caches
         * @param results one or more import results to combine
         * @return the list of assets that were either created or updated, from across all the provided results
         */
        fun getAllModifiedAssets(
            client: AtlanClient,
            closeOriginal: Boolean = true,
            vararg results: ImportResults?,
        ): OffHeapAssetCache {
            var totalCreated = 0L
            var totalUpdated = 0L
            var totalRestored = 0L
            results.filterNotNull()
                .forEach { result ->
                    totalCreated += result.primary.created?.size() ?: 0
                    totalUpdated += result.primary.updated?.size() ?: 0
                    totalRestored += result.primary.restored?.size() ?: 0
                }
            val combined = OffHeapAssetCache(client, "allModified")
            results.filterNotNull()
                .forEach { result ->
                    combined.extendedWith(result.primary.created, closeOriginal)
                    combined.extendedWith(result.primary.restored, closeOriginal)
                    combined.extendedWith(result.primary.updated, closeOriginal) { asset -> !asset.connectionQualifiedName.isNullOrBlank() && !asset.qualifiedName.isNullOrBlank() }
                    if (closeOriginal) {
                        result.primary.created?.close()
                        result.primary.restored?.close()
                        result.primary.updated?.close()
                        result.primary.skipped?.close()
                        result.related.created?.close()
                        result.related.updated?.close()
                        result.related.restored?.close()
                        result.related.skipped?.close()
                    }
                }
            return combined
        }

        /**
         * Combine all the provided import results together.
         *
         * @param client connectivity to the Atlan tenant
         * @param closeOriginal whether to close the original caches
         * @param results one or more import results to combine
         * @return the combined import results
         */
        fun combineAll(
            client: AtlanClient,
            closeOriginal: Boolean = true,
            vararg results: ImportResults?,
        ): ImportResults? {
            if (results.isEmpty()) return null
            var anyFailures = false
            val primaries = mutableListOf<Details>()
            val related = mutableListOf<Details>()
            results.filterNotNull()
                .forEach { result ->
                    anyFailures = anyFailures || result.anyFailures
                    primaries.add(result.primary)
                    related.add(result.related)
                }
            if (primaries.isEmpty()) return null
            val ir =
                ImportResults(
                    anyFailures,
                    Details.combineAll(client, closeOriginal, *primaries.toTypedArray()),
                    Details.combineAll(client, closeOriginal, *related.toTypedArray()),
                )
            results.filterNotNull()
                .forEach { result ->
                    if (closeOriginal) {
                        result.close()
                    }
                }
            return ir
        }
    }

    /** {@inheritDoc} */
    override fun close() {
        var exception: IOException? = null
        try {
            primary.close()
        } catch (e: IOException) {
            exception = e
        }
        try {
            related.close()
        } catch (e: IOException) {
            if (exception != null) exception.addSuppressed(e) else exception = e
        }
        if (exception != null) throw exception
    }
}
