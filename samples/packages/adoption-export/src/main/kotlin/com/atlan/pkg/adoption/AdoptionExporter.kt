/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.adoption

import AdoptionExportCfg
import com.atlan.Atlan
import com.atlan.model.assets.Asset
import com.atlan.pkg.Utils
import com.atlan.pkg.adoption.exports.AssetChanges
import com.atlan.pkg.adoption.exports.AssetViews
import com.atlan.pkg.adoption.exports.DetailedSearches
import com.atlan.pkg.adoption.exports.DetailedUserChanges
import com.atlan.pkg.adoption.exports.DetailedUserViews
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
        val deliveryType = Utils.getOrDefault(config.deliveryType, "DIRECT")

        val exportFile = "$outputDirectory${File.separator}adoption-export.xlsx"
        ExcelWriter(exportFile).use { xlsx ->
            if (includeViews != "NONE") {
                val maxAssets = Utils.getOrDefault(config.viewsMax, 100).toInt()
                val includeDetails = Utils.getOrDefault(config.viewsDetails, "NO") == "YES"
                val start = Utils.getOrDefault(config.viewsFrom, -1).toLong()
                val end = Utils.getOrDefault(config.viewsTo, -1).toLong()
                AssetViews(xlsx, logger, includeViews, maxAssets).export()
                if (includeDetails) {
                    DetailedUserViews(xlsx, logger, start, end).export()
                }
            }
            if (includeChanges) {
                val byUsers = Utils.getOrDefault(config.changesByUser, listOf())
                val byAction = Utils.getOrDefault(config.changesTypes, listOf())
                val start = Utils.getOrDefault(config.changesFrom, -1).toLong()
                val end = Utils.getOrDefault(config.changesTo, -1).toLong()
                val maxAssets = Utils.getOrDefault(config.changesMax, 100).toInt()
                val includeDetails = Utils.getOrDefault(config.changesDetails, "NO") == "YES"
                AssetChanges(xlsx, logger, byUsers, byAction, start, end, maxAssets).export()
                if (includeDetails) {
                    val includeAutomations = Utils.getOrDefault(config.changesAutomations, "NONE")
                    DetailedUserChanges(xlsx, logger, start, end, includeAutomations).export()
                }
            }
            if (includeSearches) {
                val start = Utils.getOrDefault(config.searchesFrom, -1).toLong()
                val end = Utils.getOrDefault(config.searchesTo, -1).toLong()
                DetailedSearches(xlsx, logger, start, end).export()
            }
        }

        when (deliveryType) {
            "EMAIL" -> {
                val emails = Utils.getAsList(config.emailAddresses)
                if (emails.isNotEmpty()) {
                    Utils.sendEmail(
                        "[Atlan] Adoption Export results",
                        emails,
                        "Hi there! As requested, please find attached the results of the Adoption Export package.\n\nAll the best!\nAtlan",
                        listOf(File(exportFile)),
                    )
                }
            }
            "CLOUD" -> {
                Utils.uploadOutputFile(
                    exportFile,
                    Utils.getOrDefault(config.targetPrefix, ""),
                    Utils.getOrDefault(config.targetKey, ""),
                )
            }
        }
    }

    fun getAssetDetails(keyMap: Map<String, Any>): Map<String, Asset> {
        val client = Atlan.getDefaultClient()
        val fullList = keyMap.keys.toList()
        val totalCount = fullList.size
        val idxBatchSize = 300
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
