/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.csv

import com.atlan.cache.OffHeapAssetCache
import com.atlan.util.AssetBatch
import com.atlan.util.AssetBatch.AssetIdentity

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
) {
    /**
     * Combine this set of import results details with another.
     *
     * @param other the other import results to combine with this one
     * @return the combined set of results, as a single set of results
     */
    fun combinedWith(other: ImportResults?): ImportResults {
        if (other == null) {
            return this
        }
        return ImportResults(
            this.anyFailures || other.anyFailures,
            this.primary.combinedWith(other.primary),
            this.related.combinedWith(other.related),
        )
    }

    /**
     * Details about the import results.
     *
     * @param guidAssignments mapping from placeholder to actual (resolved) GUIDs, even if no change was made to an asset
     * @param qualifiedNames mapping from case-insensitive to actual (resolved) qualifiedName, even if no change was made to an asset
     * @param createdIn list of (minimal) assets that were created (note: when tracking is turned off in batch-processing, this will be null)
     * @param updatedIn list of (minimal) assets that were updated (note: when tracking is turned off in batch-processing, this will be null)
     * @param restoredIn list of (minimal) assets that were potentially-restored (note: when tracking is turned off in batch-processing, this will be null)
     * @param skippedIn list of (minimal) assets that were skipped
     * @param numCreated number of assets that were created (count only)
     * @param numUpdated number of assets that were updated (count only)
     * @param numRestored number of assets that were potentially restored (count only)
     */
    class Details(
        val guidAssignments: Map<String, String>,
        val qualifiedNames: Map<AssetIdentity, String>,
        createdIn: OffHeapAssetCache?,
        updatedIn: OffHeapAssetCache?,
        restoredIn: OffHeapAssetCache?,
        skippedIn: OffHeapAssetCache?,
        val numCreated: Long,
        val numUpdated: Long,
        val numRestored: Long,
    ) {
        val created = createdIn?.copy()
        val updated = updatedIn?.copy()
        val restored = restoredIn?.copy()
        val skipped = skippedIn?.copy()

        /**
         * Combine this set of details with another.
         *
         * @param other the other details to combine with this one
         * @return the combined set of details, as a single set of details
         */
        fun combinedWith(other: Details?): Details {
            if (other == null) {
                return this
            }
            return Details(
                this.guidAssignments.plus(other.guidAssignments),
                this.qualifiedNames.plus(other.qualifiedNames),
                if (this.created == null) other.created else this.created.combinedWith(other.created),
                if (this.updated == null) other.updated else this.updated.combinedWith(other.updated),
                if (this.restored == null) other.restored else this.restored.combinedWith(other.restored),
                if (this.skipped == null) other.skipped else this.skipped.combinedWith(other.skipped),
                this.numCreated.plus(other.numCreated),
                this.numUpdated.plus(other.numUpdated),
                this.numRestored.plus(other.numRestored),
            )
        }
    }

    companion object {
        /**
         * Retrieve all the assets that were created or updated across any of the provided
         * import results.
         *
         * @param results one or more import results to combine
         * @return the list of assets that were either created or updated, from across all the provided results
         */
        fun getAllModifiedAssets(vararg results: ImportResults?): OffHeapAssetCache {
            var totalCreated = 0
            var totalUpdated = 0
            var totalRestored = 0
            results.filterNotNull()
                .forEach { result ->
                    totalCreated += result.primary.created?.size() ?: 0
                    totalUpdated += result.primary.updated?.size() ?: 0
                    totalRestored += result.primary.restored?.size() ?: 0
                }
            val combined = OffHeapAssetCache("allModified", totalCreated + totalUpdated + totalRestored, AssetBatch.EXEMPLAR_COLUMN)
            results.filterNotNull()
                .forEach { result ->
                    combined.extendedWith(result.primary.created)
                    combined.extendedWith(result.primary.restored)
                    combined.extendedWith(result.primary.updated) { asset -> !asset.connectionQualifiedName.isNullOrBlank() && !asset.qualifiedName.isNullOrBlank() }
                    result.primary.created?.close()
                    result.primary.restored?.close()
                    result.primary.updated?.close()
                    result.primary.skipped?.close()
                    result.related.created?.close()
                    result.related.updated?.close()
                    result.related.restored?.close()
                    result.related.skipped?.close()
                }
            return combined
        }
    }
}
