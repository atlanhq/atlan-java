/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.adoption

import AdoptionExportCfg
import com.atlan.Atlan
import com.atlan.model.assets.Asset
import com.atlan.pkg.Utils
import com.atlan.pkg.adoption.exports.AssetChanges
import com.atlan.pkg.adoption.exports.AssetViews
import com.atlan.pkg.serde.xls.ExcelWriter
import mu.KotlinLogging
import java.io.File
import kotlin.math.min

/**
 * Actually run the export of adoption details.
 */
object AdoptionExporter {
    private val logger = KotlinLogging.logger {}

    @JvmStatic
    fun main(args: Array<String>) {
        val outputDirectory = if (args.isEmpty()) "tmp" else args[0]
        val config = Utils.setPackageOps<AdoptionExportCfg>()

        val includeViews = Utils.getOrDefault(config.includeViews, "BY_VIEWS")
        val includeChanges = Utils.getOrDefault(config.includeChanges, "NO") == "YES"
        val includeSearches = Utils.getOrDefault(config.includeSearches, "NO") == "YES"

        val exportFile = "$outputDirectory${File.separator}adoption-export.xlsx"
        ExcelWriter(exportFile).use { xlsx ->
            if (includeViews != "NONE") {
                val maxAssets = Utils.getOrDefault(config.viewsMax, 100).toInt()
                AssetViews(xlsx, logger, includeViews, maxAssets).export()
            }
            if (includeChanges) {
                val byUsers = Utils.getOrDefault(config.changesByUser, listOf())
                val byAction = Utils.getOrDefault(config.changesTypes, listOf())
                val start = Utils.getOrDefault(config.changesFrom, -1).toLong()
                val end = Utils.getOrDefault(config.changesTo, -1).toLong()
                val maxAssets = Utils.getOrDefault(config.changesMax, 100).toInt()
                AssetChanges(xlsx, logger, byUsers, byAction, start, end, maxAssets).export()
            }
            if (includeSearches) {
                val maxSearches = Utils.getOrDefault(config.maximumSearches, 50)
                logger.error { "Search export is not yet implemented -- coming soon." }
                // TODO: implement exports of searches
            }
        }

        val emails = Utils.getOrDefault(config.emailAddresses, "")
            .split(',')
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .toList()
        if (emails.isNotEmpty()) {
            Utils.sendEmail(
                "[Atlan] Adoption Export results",
                emails,
                "Hi there! As requested, please find attached the results of the Adoption Export package.\n\nAll the best!\nAtlan",
                listOf(File(exportFile)),
            )
        }
    }

    fun getAssetDetails(keyMap: Map<String, Any>): Map<String, Asset> {
        val client = Atlan.getDefaultClient()
        val fullList = keyMap.keys.toList()
        val totalCount = fullList.size
        val idxBatchSize = 50
        val detailMap = mutableMapOf<String, Asset>()
        for (i in 0..totalCount step idxBatchSize) {
            val subList = fullList.subList(i, min(i + idxBatchSize, totalCount))
            client.assets
                .select()
                .where(Asset.GUID.`in`(subList))
                .includeOnResults(Asset.TYPE_NAME)
                .includeOnResults(Asset.QUALIFIED_NAME)
                .includeOnResults(Asset.NAME)
                .stream()
                .forEach {
                    detailMap[it.guid] = it
                }
        }
        return detailMap
    }
}
