/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.rab

import com.atlan.Atlan
import com.atlan.model.assets.Asset
import com.atlan.model.enums.AtlanDeleteType
import com.atlan.model.search.FluentSearch
import mu.KLogger
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.min
import kotlin.math.round

/**
 * Remove assets from Atlan if they exist in Atlan but were entirely absent from an import.
 *
 * @param touchedGuids GUIDs of assets that were present in the import (any matches to this set will NOT be removed)
 * @param removeTypes names of asset types that should be deleted, if untouched
 * @param removalPrefix qualifiedName prefix that must match an untouched asset for it to be deleted
 * @param purge if true, any untouched asset that matches will be permanently deleted (otherwise only archived)
 * @param logger for tracking status and documenting any errors
 */
class AssetRemover(
    private val touchedGuids: Set<String>,
    private val removeTypes: List<String>,
    private val removalPrefix: String,
    private val purge: Boolean,
    private val logger: KLogger,
) {
    private val untouched = ConcurrentHashMap.newKeySet<String>()
    private val client = Atlan.getDefaultClient()

    /**
     * Actually run the removal of any untouched assets.
     */
    fun run() {
        findUntouched()
        deleteUntouched()
    }

    /**
     * Find all untouched assets within the parameters provided (prefix and asset types)
     */
    private fun findUntouched() {
        logger.info { " --- Determining which assets to delete... ---" }
        // Include archived in case we are rerunning with a PURGE semantic
        client.assets.select(true)
            .where(FluentSearch.assetTypes(removeTypes))
            .where(Asset.QUALIFIED_NAME.startsWith(removalPrefix))
            .stream(true)
            .forEach { asset ->
                val candidate = asset.guid
                if (!touchedGuids.contains(candidate)) {
                    untouched.add(candidate)
                }
            }
    }

    /**
     * Delete all untouched assets we have identified, in batches of 20 at a time.
     */
    private fun deleteUntouched() {
        if (untouched.isNotEmpty()) {
            val deletionType = if (purge) AtlanDeleteType.PURGE else AtlanDeleteType.SOFT
            val guidList = untouched.toList()
            val totalToDelete = guidList.size
            logger.info { " --- Deleting ($deletionType) $totalToDelete assets across $removeTypes... ---" }
            val batchSize = 20
            if (totalToDelete < batchSize) {
                client.assets.delete(guidList, deletionType)
            } else {
                for (i in 0..totalToDelete step batchSize) {
                    logger.info { " ... next batch of $batchSize (${round((i.toDouble() / totalToDelete) * 100)}%)" }
                    val sublist = guidList.subList(i, min(i + batchSize, totalToDelete))
                    client.assets.delete(sublist, deletionType)
                }
            }
        }
    }
}
