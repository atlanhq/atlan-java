/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.csv

import com.atlan.model.assets.Asset
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
     * @param created list of (minimal) assets that were created (note: when tracking is turned off in batch-processing, this will be null)
     * @param updated list of (minimal) assets that were updated (note: when tracking is turned off in batch-processing, this will be null)
     * @param skipped list of (minimal) assets that were skipped
     * @param numCreated number of assets that were created (count only)
     * @param numUpdated number of assets that were updated (count only)
     */
    data class Details(
        val guidAssignments: Map<String, String>,
        val qualifiedNames: Map<AssetIdentity, String>,
        val created: List<Asset>?,
        val updated: List<Asset>?,
        val skipped: List<Asset>,
        val numCreated: Long,
        val numUpdated: Long,
    ) {
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
                this.created?.plus(other.created ?: listOf()),
                this.updated?.plus(other.updated ?: listOf()),
                this.skipped.plus(other.skipped),
                this.numCreated.plus(other.numCreated),
                this.numUpdated.plus(other.numUpdated),
            )
        }
    }
}
