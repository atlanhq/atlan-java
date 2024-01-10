/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.csv

import com.atlan.model.assets.Asset

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
    fun combinedWith(other: ImportResults): ImportResults {
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
     * @param created list of (minimal) assets that were created
     * @param updated list of (minimal) assets that were updated
     * @param skipped list of (minimal) assets that were skipped
     */
    data class Details(
        val guidAssignments: Map<String, String>,
        val created: List<Asset>,
        val updated: List<Asset>,
        val skipped: List<Asset>,
    ) {
        /**
         * Combine this set of details with another.
         *
         * @param other the other details to combine with this one
         * @return the combined set of details, as a single set of details
         */
        fun combinedWith(other: Details): Details {
            return Details(
                this.guidAssignments.plus(other.guidAssignments),
                this.created.plus(other.created),
                this.updated.plus(other.updated),
                this.skipped.plus(other.skipped),
            )
        }
    }
}
