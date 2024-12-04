/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.adoption.exports

import AdoptionExportCfg
import com.atlan.model.assets.Asset
import com.atlan.model.search.SearchLog
import com.atlan.model.search.aggregates.AssetViews
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.adoption.AdoptionExporter.getAssetDetails
import com.atlan.pkg.serde.xls.ExcelWriter
import mu.KLogger
import org.apache.poi.ss.usermodel.Sheet

class AssetViews(
    private val ctx: PackageContext<AdoptionExportCfg>,
    private val xlsx: ExcelWriter,
    private val logger: KLogger,
) {
    fun export() {
        logger.info { "Exporting top ${ctx.config.viewsMax} most-viewed assets..." }
        val sheet = xlsx.createSheet("Views")
        val viewCountMap =
            when (ctx.config.includeViews) {
                "BY_VIEWS" -> {
                    xlsx.addHeader(
                        sheet,
                        mapOf(
                            "Type" to "Type of asset",
                            "Qualified name" to "Unique name of the asset",
                            "Name" to "Simple name for the asset",
                            "Total views" to "Total number of times the asset has been viewed, possibly by the same user more than once",
                            "Distinct users" to "Total number of unique users that have viewed the asset",
                            "Link" to "Link to the asset's profile page in Atlan",
                        ),
                    )
                    SearchLog.mostViewedAssets(ctx.client, ctx.config.viewsMax.toInt(), false).associateBy { it.guid }
                }
                "BY_USERS" -> {
                    xlsx.addHeader(
                        sheet,
                        mapOf(
                            "Type" to "Type of asset",
                            "Qualified name" to "Unique name of the asset",
                            "Name" to "Simple name for the asset",
                            "Distinct users" to "Total number of unique users that have viewed the asset",
                            "Total views" to "Total number of times the asset has been viewed, possibly by the same user more than once",
                            "Link" to "Link to the asset's profile page in Atlan",
                        ),
                    )
                    SearchLog.mostViewedAssets(ctx.client, ctx.config.viewsMax.toInt(), true).associateBy { it.guid }
                }
                else -> mapOf()
            }

        // Then iterate through the unique assets
        val assetMap = getAssetDetails(ctx, viewCountMap)
        viewCountMap.forEach { (k, v) ->
            val asset = assetMap[k]
            if (asset != null) {
                outputAsset(sheet, asset, v)
            }
        }
    }

    private fun outputAsset(
        sheet: Sheet,
        asset: Asset,
        views: AssetViews,
    ) {
        when (ctx.config.includeViews) {
            "BY_VIEWS" -> {
                xlsx.appendRow(
                    sheet,
                    listOf(
                        asset.typeName ?: "",
                        asset.qualifiedName ?: "",
                        asset.name ?: "",
                        views.totalViews ?: "",
                        views.distinctUsers ?: "",
                        Utils.getAssetLink(ctx.client, asset.guid),
                    ),
                )
            }

            "BY_USERS" -> {
                xlsx.appendRow(
                    sheet,
                    listOf(
                        asset.typeName ?: "",
                        asset.qualifiedName ?: "",
                        asset.name ?: "",
                        views.distinctUsers ?: "",
                        views.totalViews ?: "",
                        Utils.getAssetLink(ctx.client, asset.guid),
                    ),
                )
            }
        }
    }
}
