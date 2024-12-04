/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.adoption

import AdoptionExportCfg
import com.atlan.model.assets.Asset
import com.atlan.pkg.PackageContext
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
        Utils.initializeContext<AdoptionExportCfg>().use { ctx ->

            val exportFile = "$outputDirectory${File.separator}adoption-export.xlsx"
            ExcelWriter(exportFile).use { xlsx ->
                if (ctx.config.includeViews != "NONE") {
                    AssetViews(ctx, xlsx, logger).export()
                    if (ctx.config.viewsDetails == "YES") {
                        DetailedUserViews(ctx, xlsx, logger).export()
                    }
                }
                if (ctx.config.includeChanges == "YES") {
                    AssetChanges(ctx, xlsx, logger).export()
                    if (ctx.config.changesDetails == "YES") {
                        DetailedUserChanges(ctx, xlsx, logger).export()
                    }
                }
                if (ctx.config.includeSearches == "YES") {
                    DetailedSearches(ctx, xlsx, logger).export()
                }
            }

            when (ctx.config.deliveryType) {
                "EMAIL" -> {
                    val emails = Utils.getAsList(ctx.config.emailAddresses)
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
                        ctx.config.targetPrefix,
                        ctx.config.targetKey,
                    )
                }
            }
        }
    }

    fun getAssetDetails(
        ctx: PackageContext<AdoptionExportCfg>,
        keyMap: Map<String, Any>,
    ): Map<String, Asset> {
        val fullList = keyMap.keys.toList()
        val totalCount = fullList.size
        val idxBatchSize = 300
        val detailMap = mutableMapOf<String, Asset>()
        for (i in 0..totalCount step idxBatchSize) {
            val subList = fullList.subList(i, min(i + idxBatchSize, totalCount))
            ctx.client.assets
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
